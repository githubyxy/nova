package com.yxy.nova.mwh.eunomia.recipes.message;

import com.yxy.nova.mwh.eunomia.client.message.Column;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Created by toruneko on 2017/4/17.
 */
public class Field {

    private Column column;

    public Field(Column column) {
        this.column = column;
    }

    public int getIndex() {
        return column.getIndex();
    }

    public String getName() {
        return column.getName();
    }

    public boolean isUpdated() {
        return column.isUpdated();
    }

    public boolean isKey() {
        return column.isKey();
    }

    public String getBeforeValue() {
        return column.getBeforeValue();
    }

    public String getAfterValue() {
        return column.getAfterValue();
    }

    public int getSqlType() {
        return column.getSqlType();
    }

    public Class<?> getType() {
        switch (getSqlType()) {
            case Types.BIT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return Integer.class;

            case Types.BIGINT:
                return Long.class;

            case Types.FLOAT:
                return Float.class;

            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
                return Double.class;

            case Types.DECIMAL:
                return BigDecimal.class;

            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
            case Types.BLOB:
                return String.class;

            case Types.DATE:
            case Types.TIMESTAMP:
            case Types.TIME:
                return Timestamp.class;
            default:
                return String.class;
        }
    }
}
