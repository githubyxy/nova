package com.yxy.nova.mwh.elasticsearch.util;

import com.yxy.nova.mwh.elasticsearch.basic.select.*;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortBy;
import com.yxy.nova.mwh.elasticsearch.basic.sort.SortMode;
import com.yxy.nova.mwh.elasticsearch.basic.where.*;
import com.yxy.nova.mwh.elasticsearch.policy.vo.FieldMapCondition;
import com.yxy.nova.mwh.elasticsearch.util.Constants.IDINCONDITION;
import com.yxy.nova.mwh.elasticsearch.util.enumerate.AggType;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析查询条件获取必要数据
 * @author quyuanwen
 */
public class AnalysisConditionUtil {

    /**
     * 从条件中取到路由字段信息
     * @param conditions
     * @return
     */
    public static Map<String, Object> findRouteValue(List<WhereCondition> conditions) {
        Map<String, Object> map = new HashMap<>();
        for (WhereCondition condition : conditions) {
            if (condition instanceof WhereEquals) {
                WhereEquals equals = (WhereEquals) condition;
                map.put(equals.getKey(),equals.getValue());
            }else if (condition instanceof WhereIn){//兼容in的情况，只取第一个值
                WhereIn in = (WhereIn) condition;
                if(!CollectionUtils.isEmpty(in.getValue())){
                    for(Object value : in.getValue()){
                        map.put(in.getKey() ,value);
                        break;
                    }
                }
            }
        }
        return map;
    }


    /**
     * 判断查询条件是否只包含ID
     * @param conditions
     * @return
     */
    public static IDINCONDITION containId(List<WhereCondition> conditions) {
        if (conditions == null) return IDINCONDITION.NOID;
        if (conditions.isEmpty()) return IDINCONDITION.NOID;
        int times = 0 ;
        for (WhereCondition condition : conditions) {
            if (condition instanceof WhereEquals) {
                String key = condition.getKey();
                if (key.equals("_id")) {
                    times ++;
                }else{
                    if(times > 0){
                      return IDINCONDITION.HASID;
                    }
                }
            } else if (condition instanceof WhereIn) {
                String key = condition.getKey();
                if (key.equals("_id")) {
                    times ++;
                }else{
                    if(times > 0){
                        return IDINCONDITION.HASID;
                    }
                }
            }
        }
        if(times == conditions.size() ){
            return IDINCONDITION.ONLYID;
        }
        return IDINCONDITION.NOID;
    }

    /**
     * 字段和条件映射关系
     * @param conditions
     * @return
     */
    public static FieldMapCondition getFieldMapCondition(List<WhereCondition> conditions) {
        FieldMapCondition fieldMapCondition = new FieldMapCondition();
        for (WhereCondition condition : conditions) {
            if (condition instanceof WhereGreater) {
                WhereGreater greater = (WhereGreater) condition;
                String key = greater.getKey();
                fieldMapCondition.putGreaterMap(key, greater);
            } else if (condition instanceof WhereGreaterOrEqual) {
                WhereGreaterOrEqual greaterOrEqual = (WhereGreaterOrEqual) condition;
                String key = greaterOrEqual.getKey();
                fieldMapCondition.putGreaterOrEqualMap(key, greaterOrEqual);
            } else if (condition instanceof WhereLess) {
                WhereLess less = (WhereLess) condition;
                String key = less.getKey();
                fieldMapCondition.putLessMap(key, less);
            } else if (condition instanceof WhereLessOrEqual) {
                WhereLessOrEqual lessOrEqual = (WhereLessOrEqual) condition;
                String key = lessOrEqual.getKey();
                fieldMapCondition.putLessOrEqualMap(key, lessOrEqual);
            } else if (condition instanceof WhereEquals) {
                WhereEquals equals = (WhereEquals) condition;
                String key = equals.getKey();
                fieldMapCondition.putEqualsMap(key, equals);
            } else if (condition instanceof WhereIn) {
                WhereIn in = (WhereIn) condition;
                String key = in.getKey();
                fieldMapCondition.putInMap(key, in);
            }
        }
        return fieldMapCondition;
    }


    /**
     * 获取查询条数限制
     *
     * @param conditions
     * @return
     */
    public static Limit getLimitCondition(List<WhereCondition> conditions) {
        for (WhereCondition condition : conditions) {
            if (condition instanceof Limit) {
                return (Limit) condition;
            }
        }
        return null;
    }
    /**
     * 是否包含聚合函数
     * @param conditions
     * @return
     */
    public static boolean hasAggregation(List<WhereCondition> conditions) {
        for (WhereCondition condition : conditions) {
            if (condition instanceof GroupBy) {
                return true;
            }
            if (condition instanceof SubgroupBy) {
                return true;
            }
            if (condition instanceof GroupByRanges) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据字段获取第一位的排序信息
     *
     * @param conditions
     * @param field
     * @return
     */
    public static SortMode findFirstSort(List<WhereCondition> conditions, final String field) {
        for (WhereCondition condition : conditions) {
            if (!(condition instanceof SortBy)) continue;

            // 只看第一个排序顺序
            SortBy sortBy = (SortBy) condition;
            if (field.equals(sortBy.getField())) {
                return sortBy.getSortMode();
            } else {
                return null;
            }
        }
        return null;
    }


    public static AggTypes getAggTypes(List<WhereCondition> conditions) {
        AggTypes result = new AggTypes();
        AggTypes lastAggTypes = new AggTypes();
        for (WhereCondition condition : conditions) {
            if (condition instanceof GroupBy) {
                GroupBy groupBy = (GroupBy) condition;
                String name = groupBy.getName();
                result.put(name, AggType.TERMS, new AggTypes());
                lastAggTypes = result.getSubAggTypes(name);
            } else if (condition instanceof SubgroupBy) {
                SubgroupBy subgroupBy = (SubgroupBy) condition;
                String name = subgroupBy.getName();
                lastAggTypes.put(name, AggType.TERMS, new AggTypes());
            } else if (condition instanceof NestGroupBy) {
                NestGroupBy nestGroupBy = (NestGroupBy) condition;
                String name = nestGroupBy.getName();
                lastAggTypes.put(name, AggType.TERMS, new AggTypes());
                lastAggTypes = lastAggTypes.getSubAggTypes(name);
            } else if (condition instanceof GroupByRanges) {
                GroupByRanges groupByRanges = (GroupByRanges) condition;
                String name = groupByRanges.getName();
                result.put(name, AggType.RANGE, new AggTypes());
                lastAggTypes = result.getSubAggTypes(name);
            }
        }
        return result;
    }
}
