package com.yxy.nova.mwh.elasticsearch.policy.vo;

import com.yxy.nova.mwh.elasticsearch.basic.where.*;

/**
 * 分区字段字信息
 */
public class PartitionFieldValue {
    WhereGreater greater;
    WhereGreaterOrEqual greaterOrEqual;
    WhereLess less;
    WhereLessOrEqual lessOrEqual;
    WhereEquals equals;
    WhereIn in;

    public WhereGreater getGreater() {
        return greater;
    }

    public void setGreater(WhereGreater greater) {
        this.greater = greater;
    }

    public WhereGreaterOrEqual getGreaterOrEqual() {
        return greaterOrEqual;
    }

    public void setGreaterOrEqual(WhereGreaterOrEqual greaterOrEqual) {
        this.greaterOrEqual = greaterOrEqual;
    }

    public WhereLess getLess() {
        return less;
    }

    public void setLess(WhereLess less) {
        this.less = less;
    }

    public WhereLessOrEqual getLessOrEqual() {
        return lessOrEqual;
    }

    public void setLessOrEqual(WhereLessOrEqual lessOrEqual) {
        this.lessOrEqual = lessOrEqual;
    }

    public WhereEquals getEquals() {
        return equals;
    }

    public void setEquals(WhereEquals equals) {
        this.equals = equals;
    }

    public WhereIn getIn() {
        return in;
    }

    public void setIn(WhereIn in) {
        this.in = in;
    }
}
