package com.yxy.nova.mwh.eunomia.recipes.message;

import com.yxy.nova.mwh.eunomia.client.message.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by toruneko on 2017/2/15.
 */
public class FieldReader {

    private Field primary;

    private List<Field> fieldList;

    private Map<String, Field> fieldMap;

    public FieldReader(Column primary, List<Column> columns) {
        this.primary = new Field(primary);
        fieldList = new ArrayList<>(columns.size());
        fieldMap = new HashMap<>(columns.size());

        for (Column column : columns) {
            fieldList.add(new Field(column));
            fieldMap.put(column.getName(), new Field(column));
        }
    }

    public List<Field> getFields() {
        return new ArrayList<>(fieldList);
    }

    /**
     * 获取主键字段
     *
     * @return
     */
    public Field getPrimary() {
        return primary;
    }

    /**
     * 获取字段
     *
     * @param index 字段序号
     * @return
     */
    public Field getField(int index) {
        Field field = fieldList.get(index);
        if (field.getIndex() == index) {
            return field;
        }
        for (Field f : fieldList) {
            if (f.getIndex() == index) {
                return f;
            }
        }

        return null;
    }

    /**
     * 获取字段
     *
     * @param name 字段名
     * @return
     */
    public Field getField(String name) {
        return fieldMap.get(name);
    }
}
