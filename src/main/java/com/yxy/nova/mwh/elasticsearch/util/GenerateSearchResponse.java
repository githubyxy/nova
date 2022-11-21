package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.util.enumerate.AggType;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.common.bytes.ByteBufferBytesReference;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.aggregations.InternalAggregation;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.elasticsearch.search.internal.InternalSearchHits;
import org.elasticsearch.search.internal.InternalSearchResponse;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成查询的返回结果
 * @author quyuanwen
 */
public class GenerateSearchResponse {

    public static SearchResponse convertToSearchResponse(JSONObject searchResult, AggTypes aggTypes) {
        // 构建hits
        JSONObject jsonObject = searchResult;
        JSONObject hitsObject = jsonObject.getJSONObject("hits");
        InternalSearchHits hits = convertSearchResultHits(hitsObject);

        // 构建aggs
        JSONObject aggsObject = jsonObject.getJSONObject("aggregations");
        InternalAggregations aggs = convertSearchResultAggs(aggsObject, aggTypes);

        // 构建InternalResponse
        InternalSearchResponse internalResponse = new InternalSearchResponse(hits, null, aggs, null, false);

        // 构建返回结果
        ShardSearchFailure[] failures = new ShardSearchFailure[0];
        return new SearchResponse(internalResponse, null, 1, 1, 0, failures);
    }


    private static InternalAggregations convertSearchResultAggs(JSONObject aggsObject, AggTypes aggTypes) {
        /*
         * 输入示例： <code> "aggregations": { "a": { "buckets": [ { "doc_count": 20718, "key": "demo" } ] }, } </code>
         */
        // 检查参数
        if (aggsObject == null) {
            return new InternalAggregations(new ArrayList<InternalAggregation>());
        }

        // 封装InternalAggregation
        List<InternalAggregation> aggList = new ArrayList<>();
        for (String name : aggsObject.keySet()) {
            JSONObject agg = aggsObject.getJSONObject(name);
            AggType aggType = aggTypes.getAggType(name);
            AggTypes subAggTypes = aggTypes.getSubAggTypes(name);
            InternalAggregation oneAgg = convertSearchResultAgg(name, agg, aggType, subAggTypes);
            aggList.add(oneAgg);
        }

        // 构建返回结果
        return new InternalAggregations(aggList);
    }

    private static InternalAggregation convertSearchResultAgg(String name, JSONObject agg, AggType type, AggTypes subAggTypes) {
        if (type == null) throw new RuntimeException("Unknown agg type: null");
        if (type == AggType.TERMS) {
            return convertSearchResultAggStringTerms(name, agg, subAggTypes);
        } else if (type == AggType.RANGE) {
            return convertSearchResultAggRange(name, agg, subAggTypes);
        } else {
            throw new RuntimeException("Unknown agg type: " + type);
        }
    }

    private static InternalRange<InternalRange.Bucket> convertSearchResultAggRange(String name, JSONObject agg,
                                                                            AggTypes subAggTypes) {
        // 检查参数
        if (agg == null) {
            return new InternalRange<>(name, new ArrayList<InternalRange.Bucket>(), null, true);
        }

        // 构建bucket
        JSONArray bucketsJson = agg.getJSONArray("buckets");
        List<InternalRange.Bucket> buckets = new ArrayList<>();
        for (Object e : bucketsJson) {
            JSONObject bucketJson = (JSONObject) e;
            String key = bucketJson.getString("key");
            Long docCount = bucketJson.getLong("doc_count");
            Double to = bucketJson.getDouble("to");
            Double from = bucketJson.getDouble("from");
            bucketJson.remove("key");
            bucketJson.remove("to");
            bucketJson.remove("to_as_string");
            bucketJson.remove("from");
            bucketJson.remove("from_as_string");
            bucketJson.remove("doc_count");
            InternalAggregations subAggs = convertSearchResultAggs(bucketJson, subAggTypes);
            InternalRange.Bucket bucket = new InternalRange.Bucket(key, from, to, docCount, subAggs, null);
            buckets.add(bucket);
        }

        // 构建返回结果
        return new InternalRange<>(name, buckets, null, true);
    }

    private static StringTerms convertSearchResultAggStringTerms(String name, JSONObject agg, AggTypes subAggTypes) {
        // 检查参数
        if (agg == null) {
            return new StringTerms(name, null, 0, 0, new ArrayList<InternalTerms.Bucket>());
        }

        // 构建Bucket
        JSONArray bucketsJson = agg.getJSONArray("buckets");
        List<StringTerms.Bucket> buckets = new ArrayList<>();
        for (Object e : bucketsJson) {
            JSONObject bucketJson = (JSONObject) e;
            String key = bucketJson.getString("key");
            Long docCount = bucketJson.getLong("doc_count");
            bucketJson.remove("key");
            bucketJson.remove("doc_count");
            InternalAggregations subAggs = convertSearchResultAggs(bucketJson, subAggTypes);
            buckets.add(new StringTerms.Bucket(new BytesRef(key), docCount, subAggs));
        }

        // 构建返回结果
        return new StringTerms(name, null, 0, 0, (List) buckets);
    }

    private static InternalSearchHits convertSearchResultHits(JSONObject hitsObject) {
        /**
         * 输入格式：<code>
         "hits": {
         "hits": [
         {
         "_id": "1449627808956-93740794",
         "_index": "forseti-20151209",
         "_score": 1.0,
         "_source": {...}
         }
         ]
         }
         </code>
         */

        // 参数安全检查
        if (hitsObject == null) {
            return new InternalSearchHits(new InternalSearchHit[0], 0, 0.0f);
        }

        // 构建hit
        JSONArray hits = hitsObject.getJSONArray("hits");
        if (hits == null) hits = new JSONArray();
        List<InternalSearchHit> hitList = new ArrayList<>();
        for (Object e : hits) {
            InternalSearchHit hit = convertInternalSearchHit((JSONObject) e);
            hitList.add(hit);
        }

        // 构建Hits
        Long total = hitsObject.getLong("total");
        if (total == null) total = 0L;
        Float maxScore = hitsObject.getFloat("max_score");
        if (maxScore == null) maxScore = 0.0f;
        return new InternalSearchHits(hitList.toArray(new InternalSearchHit[hitList.size()]), total, maxScore);
    }

    private static InternalSearchHit convertInternalSearchHit(JSONObject item) {
        // ID、type、假的fields
        String id = item.getString("_id");
        Text type = new StringText(item.getString("_type"));
        Map<String, SearchHitField> fields = new HashMap<>();

        // 构建Hit
        InternalSearchHit hit = new InternalSearchHit(0, id, type, fields);

        // 构建假的SearchShardTarget，避免toString的时候出现空指针
        String index = item.getString("_index");
        SearchShardTarget shardTarget = new SearchShardTarget("unknown-node-id", index, -1);
        hit.shard(shardTarget);

        // 构建source，这样可以通过sourceAsMap等方法获取到里面的结果
        String sourceString = item.getJSONObject("_source").toString();
        byte[] sourceBytes =sourceString.getBytes(Charset.forName("utf8"));
        hit.sourceRef(new ByteBufferBytesReference(ByteBuffer.wrap(sourceBytes)));
        hit.score(1.0f);

        // 返回结果
        return hit;
    }
}
