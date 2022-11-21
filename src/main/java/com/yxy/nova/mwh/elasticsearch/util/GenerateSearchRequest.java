package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.basic.select.*;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortBy;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortMode;
import com.yxy.nova.mwh.elasticsearch.basic.where.*;
import com.yxy.nova.mwh.elasticsearch.dto.SearchRequest;
import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 生成查询请求
 */
public class GenerateSearchRequest {


    private static Logger logger = LoggerFactory.getLogger(GenerateSearchRequest.class);


    /**
     * 创建查询请求
     *
     * @param indices
     * @param table
     * @param conditions
     * @return
     * @throws ElasticsearchClientException
     */
    public static SearchRequest buildRequest(String[] indices, String table, List<WhereCondition> conditions) throws ElasticsearchClientException {
        if(indices.length == 0){
            logger.warn("未能获取索引信息！");
        }

        // 创建一个新的请求
        SearchRequest result = new SearchRequest();

        // 配置需要查询的索引
        result.setIndex(multiIndices(indices));

        // 文档类型如果不设置，就查找所有的文档
        if (table != null && !table.equals("*")) {
            result.setType(table);
        } else {
            result.setType(null);
        }

        // 创建一个请求
        JSONObject bool = new JSONObject();
        JSONArray must = new JSONArray();
        // 将查询条件填入请求
        setupSearchRequest(result, conditions);

        // Query查询
        JSONObject query = buildQuery(conditions);
        if(query != null && !query.isEmpty()){
            must.add(query);
            bool.put("must", must);
        }
        // 过滤条件、聚类条件
        JSONObject boolFilter = buildAndFilter(conditions);
        if(boolFilter != null && !boolFilter.isEmpty()){
            JSONArray filter = new JSONArray();
            filter.add(boolFilter);
            bool.put("filter", filter);
        }
        //JSONObject aggs = buildAggregation(conditions);
        /*if(aggs != null && !aggs.isEmpty()){
            result.putBody("aggs", aggs);
        }*/

        JSONObject query2 = new JSONObject();
        query2.put("bool", bool);
        result.putBody("query", query2);
        return result;
    }

    private static String multiIndices(String[] indices) {
        if(indices.length == 0){
            logger.warn("未能获取索引信息！");
        }
        if (indices.length == 1) return indices[0];
        StringBuilder result = new StringBuilder();
        for (String e : indices) {
            if (result.length() != 0) result.append(",");
            result.append(e);
        }
        return result.toString();
    }

    private static void setupSearchRequest(SearchRequest request, List<WhereCondition> conditions) throws ElasticsearchClientException {
        JSONArray sort = new JSONArray();
        for (WhereCondition c : conditions) {
            if (c instanceof Limit) {
                // 分页
                request.putQueryString("from", Integer.toString(((Limit) c).getFrom()));
                request.putQueryString("size", Integer.toString(((Limit) c).getSize()));
            } else if (c instanceof SortBy) {
                JSONObject oneSort = new JSONObject();
                String field = ((SortBy) c).getField();
                SortMode sortMode = ((SortBy) c).getSortMode();
                switch (sortMode) {
                    case ASC:
                        oneSort.put(field, getSortCondition("asc"));
                        break;
                    case DESC:
                        oneSort.put(field, getSortCondition("desc"));
                        break;
                    default:
                        throw new ElasticsearchClientException("排序模式错误" , ESExceptionType.CONDITION_ERROR);
                }
                sort.add(oneSort);
            }
        }
        if (!sort.isEmpty()) {
            request.putBody("sort", sort);
        }
    }

    private static JSONObject getSortCondition(String order) {
        JSONObject result = new JSONObject();
        result.put("order", order);
        result.put("unmapped_type", "long");
        return result;
    }

    private static JSONObject buildQuery(List<WhereCondition> conditions) throws ElasticsearchClientException {
        // 构建模糊查询语句
        JSONObject result = null;
        for (WhereCondition c : conditions) {
            if (c instanceof WhereLike) {
                // 模糊匹配
                if (result != null) throw new ElasticsearchClientException("Not support multiple whereLike", ESExceptionType.CONDITION_ERROR);
                WhereLike whereLike = (WhereLike) c;
                JSONObject match = new JSONObject();
                match.put(whereLike.getKey(), whereLike.getValue());
                result = new JSONObject();
                result.put("match", match);
            } else if (c instanceof MultiWhereLike){

                MultiWhereLike multiWhereLike = (MultiWhereLike)c;
                String query = multiWhereLike.getQuery();
                String[] fields = multiWhereLike.getFields();
                if (StringUtils.isBlank(query) || fields.length==0) {
                    throw new ElasticsearchClientException("Illegal arguments", ESExceptionType.CONDITION_ERROR);
                }
                JSONObject multiMatch = new JSONObject();
                multiMatch.put("query", query);
                multiMatch.put("fields", fields);
                result = new JSONObject();
                result.put("multi_match",multiMatch);
            } else if(c instanceof WildcardWhereLike){
                WildcardWhereLike wildcardWhereLike = (WildcardWhereLike)c;
                JSONObject wildcard = new JSONObject();
                wildcard.put(wildcardWhereLike.getKey(),wildcardWhereLike.getValue());
                result = new JSONObject();
                result.put("wildcard",wildcard);
            }
        }
        return result;
        /*if (result != null) {

        }

        // 如果找不到模糊匹配的条件，那就返回匹配全部
        result = new JSONObject();
        result.put("match_all", new JSONObject());
        return result;*/
    }

    private static JSONObject buildAndFilter(List<WhereCondition> conditions) {
        JSONObject bool = new JSONObject();
        List<WhereCondition> sortedConditions = getSortedConditions(conditions);
        JSONArray must = buildFilters(sortedConditions);
        bool.put("must", must);
        JSONObject result = new JSONObject();
        result.put("bool", bool);
        return result;
    }

    /**
     * _id放在第一位
     */
    private static List<WhereCondition> getSortedConditions(List<WhereCondition> conditions) {
        List<WhereCondition> sortedConditions = new LinkedList<>();
        for (WhereCondition condition : conditions) {
            if (condition instanceof WhereEquals) {
                String key =condition.getKey();
                if (key.equals("_id")) {
                    sortedConditions.add(0, condition);
                } else {
                    sortedConditions.add(condition);
                }
            } else {
                sortedConditions.add(condition);
            }
        }
        return sortedConditions;
    }

    private static JSONArray buildFilters(List<WhereCondition> conditions) {
        JSONArray result = new JSONArray();
        for (WhereCondition condition : conditions) {
            if (condition instanceof WhereEquals) {
                result.add(termFilter( condition.getKey(), condition.getValue()));
            } else if (condition instanceof WhereNotEquals) {
                result.add(notFilter(termFilter(condition.getKey(), condition.getValue())));
            } else if (condition instanceof WhereContains) {
                result.add(termFilter(condition.getKey(), condition.getValue()));
            } else if (condition instanceof WhereIn) {
                result.add(termsFilter(condition.getKey(), ((WhereIn) condition).getValue()));
            } else if (condition instanceof WhereNotIn) {
                result.add(notFilter(termsFilter(condition.getKey(), ((WhereNotIn) condition).getValue())));
            } else if (condition instanceof WhereGreater) {
                result.add(gtFilter(condition.getKey(), condition.getValue()));
            } else if (condition instanceof WhereLess) {
                result.add(ltFilter(condition.getKey(), condition.getValue()));
            } else if (condition instanceof WhereGreaterOrEqual) {
                result.add(gteFilter(condition.getKey(), condition.getValue()));
            } else if (condition instanceof WhereLessOrEqual) {
                result.add(lteFilter(condition.getKey(), condition.getValue()));
            } else if (condition instanceof WhereIsNull) {
                result.add(notFilter(existsFilter(condition.getKey())));
            } else if (condition instanceof WhereNotIsNull) {
                result.add(existsFilter(condition.getKey()));
            } else if (condition instanceof WhereStartsWith) {
                result.add(prefixFilter(condition.getKey(), condition.getValue()));
            } else if (condition instanceof GroupWhere) {
                GroupWhere g = (GroupWhere) condition;
                if (g.isLogicAnd()) {
                    result.add(buildAndFilter(g.getValue()));
                } else if (g.isLogicOr()) {
                    result.add(buildOrFilter(g.getValue()));
                } else {
                    throw new RuntimeException("unknown logic");
                }
            }
        }
        return result;
    }

    private static JSONObject termFilter(String field, Object value) {
        JSONObject term = new JSONObject();
        term.put(field, value);
        JSONObject result = new JSONObject();
        result.put("term", term);
        return result;
    }

    private static JSONObject notFilter(JSONObject filter) {
        JSONArray must_not = new JSONArray();
        must_not.add(filter);
        JSONObject bool = new JSONObject();
        bool.put("must_not", must_not);
        JSONObject result = new JSONObject();
        result.put("bool", bool);
        return result;
    }

    private static JSONObject termsFilter(String field, Collection values) {
        JSONObject terms = new JSONObject();
        terms.put(field, values);
        JSONObject result = new JSONObject();
        result.put("terms", terms);
        return result;
    }

    private static JSONObject gtFilter(String field, Object value) {
        return rangeFilter(field, "gt", value);
    }

    private static JSONObject gteFilter(String field, Object value) {
        return rangeFilter(field, "gte", value);
    }

    private static JSONObject ltFilter(String field, Object value) {
        return rangeFilter(field, "lt", value);
    }

    private static JSONObject lteFilter(String field, Object value) {
        return rangeFilter(field, "lte", value);
    }

    private static JSONObject buildOrFilter(List<WhereCondition> conditions) {
        JSONObject bool = new JSONObject();
        JSONArray should = buildFilters(conditions);
        bool.put("should", should);
        JSONObject result = new JSONObject();
        result.put("bool", bool);
        return result;
    }





    private static JSONObject rangeFilter(String field, String compare, Object value) {
        JSONObject gt = new JSONObject();
        gt.put(compare, value);
        JSONObject range = new JSONObject();
        range.put(field, gt);
        JSONObject result = new JSONObject();
        result.put("range", range);
        return result;
    }

    private static JSONObject existsFilter(String field) {
        JSONObject exists = new JSONObject();
        exists.put("field", field);
        JSONObject result = new JSONObject();
        result.put("exists", exists);
        return result;
    }

    private static JSONObject prefixFilter(String field, Object value) {
        JSONObject prefix = new JSONObject();
        prefix.put(field, value);
        JSONObject result = new JSONObject();
        result.put("prefix", prefix);
        return result;
    }

    private static JSONObject buildAggregation(List<WhereCondition> conditions) {
        JSONObject lastAgg = null;
        JSONObject result = new JSONObject();
        for (WhereCondition c : conditions) {
            if (c instanceof GroupBy) {
                lastAgg = termsAggs(((GroupBy) c).getField(), ((GroupBy) c).getSize());
                result.put(((GroupBy) c).getName(), lastAgg);
            } else if (c instanceof GroupByRanges) {
                lastAgg = rangesAggs(((GroupByRanges) c).getField(), ((GroupByRanges) c).getRanges());
                result.put(((GroupByRanges) c).getName(), lastAgg);
            } else if (c instanceof SubgroupBy) {
                lastAgg.put("aggs",
                        subAggs(((SubgroupBy) c).getName(),
                                termsAggs(((SubgroupBy) c).getField(), ((SubgroupBy) c).getSize())));
            } else if (c instanceof NestGroupBy) {
                JSONObject agg = termsAggs(((NestGroupBy) c).getField(), ((NestGroupBy) c).getSize());
                lastAgg.put("aggs", subAggs(((NestGroupBy) c).getName(), agg));
                lastAgg = agg;
            }
        }
        return result;
    }

    private static JSONObject termsAggs(String field, int size) {
        JSONObject terms = new JSONObject();
        terms.put("field", field);
        terms.put("size", size);
        terms.put("shard_size", size * 3);
        JSONObject result = new JSONObject();
        result.put("terms", terms);
        return result;
    }

    private static JSONObject rangesAggs(String field, List<? extends Number> ranges) {
        JSONArray ranges2 = new JSONArray();
        Number[] numbers = ranges.toArray(new Number[0]);
        for (int i = 1; i < numbers.length; i++) {
            Number from = numbers[i - 1];
            Number to = numbers[i];
            JSONObject r = new JSONObject();
            r.put("from", from);
            r.put("to", to);
            ranges2.add(r);
        }

        JSONObject range = new JSONObject();
        range.put("field", field);
        range.put("ranges", ranges2);
        JSONObject result = new JSONObject();
        result.put("range", range);
        return result;
    }

    private static JSONObject subAggs(String name, JSONObject aggs) {
        JSONObject result = new JSONObject();
        result.put(name, aggs);
        return result;
    }



}
