package com.yxy.nova.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DebeziumHandler {

    public void handler(String requestStr) {

        JSONObject jsonObject = JSONObject.parseObject(requestStr);

        JSONObject payload = jsonObject.getJSONObject("payload");

        processRowDataChange(payload);
    }

        private void processRowDataChange(JSONObject payload) {
        try{
            // 向kafka发送变更数据
            MyRowData myRowData = new MyRowData();
            myRowData.setSchemaName("jetlinks");
            myRowData.setTableName(payload.getJSONObject("source").getString("table"));
            String op = payload.getString("op");
            String eventType = "";
            switch (op) {
                case "c":
                case "r":
                    eventType = CanalEntry.EventType.INSERT.name();
                    break;
                case "u":
                    eventType = CanalEntry.EventType.UPDATE.name();
                    break;
                case "d":
                    eventType = CanalEntry.EventType.DELETE.name();
                    break;
            }
            myRowData.setEventType(eventType);
            myRowData.setExecuteTime(payload.getLong("ts_ms"));

            JSONObject before = payload.getJSONObject("before");
            JSONObject after = payload.getJSONObject("after");

            myRowData.setColumns(listMyColumns(before, after, eventType));
            log.info("myRowData:{}", JSONObject.toJSONString(myRowData));

        }catch(Exception e){
            log.error("处理数据变更出错", e);
        }
    }

    private List<MyColumn> listMyColumns(JSONObject before, JSONObject after, String eventType) {
        List<MyColumn> list = new ArrayList<>();
        if (StringUtil.isBlank(eventType)) {
            return list;
        }

        Map<String, Object> map = (before == null) ? after.getInnerMap() : before.getInnerMap();
        List<String> columnList = map.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());

        for (int i = 0; i < columnList.size(); i++) {
            MyColumn myColumn = new MyColumn();
            myColumn.setIndex(i);
            myColumn.setName(columnList.get(i));
//            myColumn.setSqlType(getColumnByEventType(beforeColumn, afterColumn, eventType).getSqlType());
            myColumn.setBeforeValue(before == null ? null : before.getString(columnList.get(i)));
            myColumn.setAfterValue(after == null ? null : after.getString(columnList.get(i)));
//            myColumn.setKey(getColumnByEventType(before, after, eventType).getIsKey());
//            myColumn.setUpdated(getColumnByEventType(before, after, eventType).getUpdated());
            list.add(myColumn);
        }

        return list;
    }

    private JSONObject getColumnByEventType(JSONObject beforeColumn, JSONObject afterColumn, String eventType) {
        if (eventType.equals(CanalEntry.EventType.DELETE.name())) {
            return beforeColumn;
        } else if (eventType.equals(CanalEntry.EventType.INSERT.name())) {
            return afterColumn;
        } else {
            return afterColumn;
        }
    }
}
