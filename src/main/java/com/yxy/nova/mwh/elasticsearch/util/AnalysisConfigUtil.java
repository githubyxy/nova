package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.policy.route.RouteMap;
import com.yxy.nova.mwh.elasticsearch.policy.route.RouteShip;
import com.yxy.nova.mwh.elasticsearch.policy.vo.Index;
import com.yxy.nova.mwh.elasticsearch.policy.vo.Table;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 策略解析
 * @author quyuanwen
 */
public class AnalysisConfigUtil {

    /**
     * 充配置文件获取索引信息
     * @param indexConfig
     * @param index
     * @throws ElasticsearchClientException
     */
    public final static void analysisCommenIndex(JSONObject indexConfig, Index index) throws ElasticsearchClientException{
        //获取读索引
        String readIndex = indexConfig.getString("read");
        if(StringUtils.isBlank(readIndex)){
            throw new ElasticsearchClientException("没有定义读索引", ESExceptionType.POLICY_ERROR);
        }
        index.setReadIndex(readIndex);

        index.setWriteIndex(indexConfig.getString("write"));
        if(StringUtils.isBlank(index.getWriteIndex())){
            index.setWriteIndex(readIndex);
        }
        //设置默认表
        index.setDefaultTbale(analysisDefaultTable(indexConfig));

        //设置多表路由
        index.setRouteShip(analysisTableRouteShip(indexConfig,index.getDefaultTbale()));
    }

    /**
     * 获取默认表
     * @param indexConfig
     * @return
     * @throws ElasticsearchClientException
     */
    public final static Table analysisDefaultTable(JSONObject indexConfig) throws ElasticsearchClientException{
        //获取默认表
        JSONObject defaultObject = indexConfig.getJSONObject("defaultTable");
        if(defaultObject == null){
            throw new ElasticsearchClientException("没有定义默认表", ESExceptionType.POLICY_ERROR);
        }
        return new Table(defaultObject.getString("name"));
    }

    /**
     * 获取多表路由
     * @param indexConfig
     * @param defaultTable
     * @return
     * @throws ElasticsearchClientException
     */
    public final static RouteShip<Table> analysisTableRouteShip(JSONObject indexConfig, Table defaultTable) throws ElasticsearchClientException{

        JSONArray tablesArray = indexConfig.getJSONArray("tables");
        if(!CollectionUtils.isEmpty(tablesArray)){
            RouteShip<Table> routeShip = null;
            for(int i = 0; i < tablesArray.size(); i++){
                JSONObject tableObject = tablesArray.getJSONObject(i);
                Table table = new Table(tableObject.getString("name"));

                JSONArray routeMapArray = tableObject.getJSONArray("routeMap");
                if(CollectionUtils.isEmpty(routeMapArray)){
                    throw new ElasticsearchClientException("多表路由配置不正确", ESExceptionType.POLICY_ERROR);
                }
                RouteMap[] routeMaps = new RouteMap[routeMapArray.size()];
                for(int n = 0; n < routeMapArray.size(); n++){
                    JSONObject routeMapObject = routeMapArray.getJSONObject(n);
                    routeMaps[n] = new RouteMap(routeMapObject.getString("field"),routeMapObject.getJSONArray("values"));
                }
                if(routeShip == null){
                    routeShip = new RouteShip(tableObject.getString("routeShip"), table, routeMaps);
                }else{
                    routeShip = routeShip.append(tableObject.getString("routeShip"), table, routeMaps);
                }
            }
            routeShip.appendDefault(defaultTable);
            return routeShip.getRoot();
        }
        return null;
    }


}
