//package com.yxy.nova.canal;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.otter.canal.client.CanalConnector;
//import com.alibaba.otter.canal.client.CanalConnectors;
//import com.alibaba.otter.canal.protocol.CanalEntry.*;
//import com.alibaba.otter.canal.protocol.Message;
//import com.google.protobuf.InvalidProtocolBufferException;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang.RandomStringUtils;
//import org.apache.commons.lang.SystemUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Service;
//
//import java.net.InetSocketAddress;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author yuxiaoyu
// * @date 2020/5/31 上午9:47
// * @Description
// */
//@Service
//public class CanalClientLaucher implements ApplicationListener<ContextRefreshedEvent>, DisposableBean {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private volatile boolean running = false;
//
//    private final String             SEP                = SystemUtils.LINE_SEPARATOR;
//    private final String             DATE_FORMAT        = "yyyy-MM-dd HH:mm:ss";
//    private String                   context_format     = null;
//    private String                   row_format         = null;
//    private String                   transaction_format = null;
//
//    @Value("${canal_server_ip}")
//    private String canalServerIp;
//    @Value("${canal_server_port}")
//    private int canalServerPort;
//    // canal hermes.instance
//    @Value("${destination}")
//    private String destination;
//    @Value("${canal_table}")
//    private String canalTable;
//
//    private void init() {
//        context_format = SEP + "****************************************************" + SEP;
//        context_format += "* Batch Id: [{}] ,count : [{}] , memsize : [{}] , Time : {}" + SEP;
//        context_format += "* Start : [{}] " + SEP;
//        context_format += "* End : [{}] " + SEP;
//        context_format += "****************************************************" + SEP;
//
//        row_format = SEP
//                + "----------------> binlog[{}:{}] , name[{},{}] , eventType : {} , executeTime : {} , delay : {}ms"
//                + SEP;
//
//        transaction_format = SEP + "================> binlog[{}:{}] , executeTime : {} , delay : {}ms" + SEP;
//
//    }
//
//
//
//
//
//    /**
//     * Invoked by the containing {@code BeanFactory} on destruction of a bean.
//     *
//     * @throws Exception in case of shutdown errors. Exceptions will get logged
//     *                   but not rethrown to allow other beans to release their resources as well.
//     */
//    @Override
//    public void destroy() throws Exception {
//
//    }
//
//    /**
//     * Handle an application event.
//     *
//     * @param event the event to respond to
//     */
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        init();
//        if (isRootApplicationContext(event.getApplicationContext())) {
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    process();
//                }
//            });
//
//            Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
//
//                @Override
//                public void uncaughtException(Thread t, Throwable e) {
//                    logger.error("parse events has an error", e);
//                }
//            };
//            thread.setUncaughtExceptionHandler(handler);
//            thread.start();
//            running = true;
//        }
//    }
//
//    protected void process() {
//        int batchSize = 512;
//        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalServerIp, canalServerPort), destination, "", "");
//        while (running) {
//            try {
//                connector.connect();
//                connector.subscribe(canalTable);
//                while (running) {
//                    MDC.put("Constants.TRACE_ID", RandomStringUtils.randomAlphanumeric(10));
////                    handler.prepare();
//                    Message message = connector.getWithoutAck(batchSize, 3L, TimeUnit.SECONDS); // 获取指定数量的数据
//                    long batchId = message.getId();
//                    int size = message.getEntries().size();
//                    if (batchId == -1 || size == 0) {
//                        // try {
//                        // Thread.sleep(1000);
//                        // } catch (InterruptedException e) {
//                        // }
//                    } else {
//                        // printSummary(message, batchId, size);
//                        processEntry(message.getEntries());
//                    }
//
//                    connector.ack(batchId); // 提交确认
////                    handler.commit();
//                    // connector.rollback(batchId); // 处理失败, 回滚数据
//                }
//            } catch (Exception e) {
//                logger.error("process error!", e);
//            } finally {
//                connector.disconnect();
//            }
//        }
//
//    }
//
//
//    protected void processEntry(List<Entry> entrys) {
//        for (Entry entry : entrys) {
//            long executeTime = entry.getHeader().getExecuteTime();
//            long delayTime = System.currentTimeMillis() - executeTime;
//
////            if(delayTime > 10 * 1000)
////                Cat.logMetricForCount("canal_sync_lag");
//
//            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
//                if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN) {
//                    TransactionBegin begin = null;
//                    try {
//                        begin = TransactionBegin.parseFrom(entry.getStoreValue());
//                    } catch (InvalidProtocolBufferException e) {
//                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
//                    }
//                    // 打印事务头信息，执行的线程id，事务耗时
//                    logger.info(transaction_format,
//                            new Object[] { entry.getHeader().getLogfileName(),
//                                    String.valueOf(entry.getHeader().getLogfileOffset()),
//                                    String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime) });
//                    logger.info(" BEGIN ----> Thread id: {}", begin.getThreadId());
//                } else if (entry.getEntryType() == EntryType.TRANSACTIONEND) {
//                    TransactionEnd end = null;
//                    try {
//                        end = TransactionEnd.parseFrom(entry.getStoreValue());
//                    } catch (InvalidProtocolBufferException e) {
//                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
//                    }
//                    // 打印事务提交信息，事务id
//                    logger.info("----------------\n");
//                    logger.info(" END ----> transaction id: {}", end.getTransactionId());
//                    logger.info(transaction_format,
//                            new Object[] { entry.getHeader().getLogfileName(),
//                                    String.valueOf(entry.getHeader().getLogfileOffset()),
//                                    String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime) });
//                }
//
//                continue;
//            }
//
//            if (entry.getEntryType() == EntryType.ROWDATA) {
//                RowChange rowChange = null;
//                try {
//                    rowChange = RowChange.parseFrom(entry.getStoreValue());
//                } catch (Exception e) {
//                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
//                }
//
//                EventType eventType = rowChange.getEventType();
//
//                logger.info(row_format,
//                        new Object[] { entry.getHeader().getLogfileName(),
//                                String.valueOf(entry.getHeader().getLogfileOffset()), entry.getHeader().getSchemaName(),
//                                entry.getHeader().getTableName(), eventType,
//                                String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime) });
//
//                if ((eventType == EventType.QUERY || rowChange.getIsDdl()) && StringUtils.isNotBlank(entry.getHeader().getSchemaName()) && StringUtils.isNotBlank(entry.getHeader().getTableName())) {
//                    logger.info(" sql ----> " + rowChange.getSql() + SEP);
//                    continue;
//                }
//
//                for (RowData rowData : rowChange.getRowDatasList()) {
//                    int index = 0;
//                    if (eventType == EventType.DELETE) {
//                        printColumn(rowData.getBeforeColumnsList());
//                    } else if (eventType == EventType.INSERT) {
//                        printColumn(rowData.getAfterColumnsList());
//                    } else {
//                        printColumn(rowData.getAfterColumnsList());
//                    }
//                    processRowDataChange(eventType, entry.getHeader().getTableName(), rowData, index++);
//                }
//            }
//        }
//    }
//
//    protected void printColumn(List<Column> columns) {
//        for (Column column : columns) {
//            StringBuilder builder = new StringBuilder();
//            builder.append(column.getName() + " : " + column.getValue());
//            builder.append("    type=" + column.getMysqlType());
//            if (column.getUpdated()) {
//                builder.append("    update=" + column.getUpdated());
//            }
//            builder.append(SEP);
//            logger.info("printColumn:[{}]",builder.toString());
//        }
//    }
//
//    /**
//     * 处理数据的变动
//     * @param eventType
//     * @param tableName
//     * @param rowData
//     */
//    private void processRowDataChange(EventType eventType, String tableName, RowData rowData, int index) {
//        try{
//            // 向kafka发送变更数据
//            MyRowData myRowData = new MyRowData();
//            myRowData.setSchemaName("hermes");
//            myRowData.setTableName(tableName);
//            myRowData.setEventType(eventType.name());
//            myRowData.setExecuteTime(System.currentTimeMillis());
//            myRowData.setPrimary(getPrimaryColumns(rowData.getBeforeColumnsList(), rowData.getAfterColumnsList(), eventType));
//            myRowData.setColumns(listMyColumns(rowData, eventType));
//            logger.info("myRowData:{}", JSON.toJSONString(myRowData));
//
//        }catch(Exception e){
//            logger.error("处理数据变更出错", e);
//        }
//    }
//
//    private List<MyColumn> listMyColumns(RowData rowData, EventType eventType) {
//        List<MyColumn> list = new ArrayList<>();
//        List<Column> beforeColumns= rowData.getBeforeColumnsList();
//        List<Column> afterColumns = rowData.getAfterColumnsList();
//
//        for (int i = 0; i < beforeColumns.size() || i < afterColumns.size(); i++) {
//            Column beforeColumn = CollectionUtils.isNotEmpty(beforeColumns) ? beforeColumns.get(i) : null;
//            Column afterColumn = CollectionUtils.isNotEmpty(afterColumns) ? afterColumns.get(i) : null;
//
//            MyColumn myColumn = new MyColumn();
//            myColumn.setIndex(i);
//            myColumn.setName(getColumnByEventType(beforeColumn, afterColumn, eventType).getName());
//            myColumn.setSqlType(getColumnByEventType(beforeColumn, afterColumn, eventType).getSqlType());
//            myColumn.setBeforeValue(beforeColumn == null ? null : beforeColumn.getValue());
//            myColumn.setAfterValue(afterColumn == null ? null : afterColumn.getValue());
//            myColumn.setKey(getColumnByEventType(beforeColumn, afterColumn, eventType).getIsKey());
//            myColumn.setUpdated(getColumnByEventType(beforeColumn, afterColumn, eventType).getUpdated());
//
//            list.add(myColumn);
//        }
//
//        return list;
//    }
//
//    private MyColumn getPrimaryColumns(List<Column> beforeColumns, List<Column> afterColumns, EventType eventType) {
//
//        MyColumn myColumn = new MyColumn();
//        for (int i = 0; i < beforeColumns.size() || i < afterColumns.size(); i++) {
//            Column beforeColumn = CollectionUtils.isNotEmpty(beforeColumns) ? beforeColumns.get(i) : null;
//            Column afterColumn = CollectionUtils.isNotEmpty(afterColumns) ? afterColumns.get(i) : null;
//            if (getColumnByEventType(beforeColumn, afterColumn, eventType).getIsKey()) {
//                myColumn.setIndex(i);
//                myColumn.setName(getColumnByEventType(beforeColumn, afterColumn, eventType).getName());
//                myColumn.setSqlType(getColumnByEventType(beforeColumn, afterColumn, eventType).getSqlType());
//                myColumn.setBeforeValue(beforeColumn == null ? null : beforeColumn.getValue());
//                myColumn.setAfterValue(afterColumn == null ? null : afterColumn.getValue());
//                myColumn.setKey(getColumnByEventType(beforeColumn, afterColumn, eventType).getIsKey());
//                myColumn.setUpdated(getColumnByEventType(beforeColumn, afterColumn, eventType).getUpdated());
//                break;
//            }
//        }
//        return myColumn;
//    }
//
//    private Column getColumnByEventType(Column beforeColumn, Column afterColumn, EventType eventType) {
//        if (eventType == EventType.DELETE) {
//            return beforeColumn;
//        } else if (eventType == EventType.INSERT) {
//            return afterColumn;
//        } else {
//            return afterColumn;
//        }
//    }
//
//    private boolean isRootApplicationContext(ApplicationContext context) {
//        return context.getParent() == null;
//    }
//
//}
