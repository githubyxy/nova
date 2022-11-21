package com.yxy.nova.mwh.elasticsearch.policy.route;

import com.yxy.nova.mwh.elasticsearch.exception.ESExceptionType;
import com.yxy.nova.mwh.elasticsearch.exception.ElasticsearchClientException;
import com.yxy.nova.mwh.elasticsearch.util.JsonPath;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 路由解析
 * @param <T>
 */
public class RouteShip<T> {
    public final static String SHIP_AND = "and";

    protected boolean and;

    protected RouteMap[] routeMaps;

    protected T node;

    protected RouteShip next;

    protected RouteShip root;

    public RouteShip(String ship, T node, RouteMap... routeMaps){
        if(SHIP_AND.equals(ship)){
            this.and = true;
        }
        this.node = node;
        this.routeMaps = routeMaps;
    }

    private RouteShip(T defaultNode){
        this.node = defaultNode;
    }

    /**
     * 匹配数据
     * @param param
     * @param flatten 是否用拉平key匹配
     * @return
     */
    protected boolean match(Map<String, Object> param, boolean flatten){
        boolean result = false;
        for(RouteMap routeMap : routeMaps){
            Object value = null;
            if(flatten){
                value = param.get(routeMap.getField());
            }else{
                value = JsonPath.getJsonValue(param, routeMap.getSplitField());
            }
            if(value == null){
                continue;
            }
            if(routeMap.getValueSet().contains(String.valueOf(value))){
                result = true;
                if(!and){
                    break;
                }
            }else{
                if(and){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    /**
     * 获取所有的路由结点
     * @param allTables
     * @return
     */
    public Set<T> getAllNodes(Set<T> allTables){
        if(allTables == null){
            allTables = new HashSet<>();
            this.root.getAllNodes(allTables);
        }else {
            allTables.add(this.node);
            if(this.next != null){
                this.next.getAllNodes(allTables);
            }
        }
        return allTables;
    }


    /**
     * 根据参数信息获取到一个路由结点
     * @param param
     * @param flatten 字段名是否为拉平
     * @return
     * @throws ElasticsearchClientException
     */
    public T getNode(Map<String, Object> param, boolean flatten) throws ElasticsearchClientException{

        if(param != null && !param.isEmpty()){
            if(routeMaps == null && this.next != null){//不是尾部结点没有映射，返回异常
                throw new ElasticsearchClientException("路由映射定义为空", ESExceptionType.POLICY_ERROR);
            }else if(routeMaps == null && this.next == null){ //尾部结点
                return this.node;
            }

            if(match(param,flatten)){
                return this.node;
            }else {
                //尾部结点还有匹配信息，单未匹配上，返回空
                if(this.next == null){
                    return null;
                }
            }
            return (T)this.next.getNode(param,flatten);
        }
        return null;
    }

    /**
     * 添加一个路由策略
     * @param ship
     * @param node
     * @param routeMaps
     * @return
     */
    public RouteShip append(String ship, T node, RouteMap... routeMaps){
        this.next = new RouteShip(ship, node, routeMaps);
        //添加第一个结点
        if(this.root == null){
            this.next.setRoot(this);
        }else{
            this.next.setRoot(this.root);
        }
        return this.next;
    }

    /**
     * 设置默认的路由结点，在所有结点都添加完成后
     * @param defaultNode
     */
    public void appendDefault(T defaultNode){
        this.next = new RouteShip(defaultNode);
    }

    /**
     * 设置root结点
     * @param root
     */
    protected void setRoot(RouteShip root){
        this.root = root;
    }

    /**
     * 获取root
     * @return
     */
    public RouteShip getRoot() {
        return root;
    }
}
