package com.yxy.nova.mwh.elasticsearch.exception;


public class ElasticsearchClientException extends Exception {

    private ExceptionCode type;

    public ElasticsearchClientException(String message, ExceptionCode type){
        super(getMessage(message,type));
        this.type = type;
    }

    public ElasticsearchClientException(String message, ExceptionCode type, Throwable cause){
        super(getMessage(message,type),cause);
        this.type = type;
    }

    public final static String getMessage(String message,ExceptionCode type){
        StringBuilder sb = new StringBuilder(300);
        sb.append("Elasticsearch client occur exception : ").append(type.getDesc())
                .append(". The message is : ").append(message);
        return sb.toString();
    }

    public ExceptionCode getType() {
        return type;
    }
}
