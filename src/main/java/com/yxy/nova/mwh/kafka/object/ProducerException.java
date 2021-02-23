package com.yxy.nova.mwh.kafka.object;

public class ProducerException extends Exception {

    private boolean invalidParam = false ;
    private boolean fatal = false;

    public ProducerException(String msg) {
        super(msg);
    }

    public ProducerException(Exception e) {
        super(e);
    }

    public static ProducerException invalidParam(final String msg) {
        final ProducerException e = new ProducerException(msg);
        e.invalidParam = true;
        return e;
    }

    public static ProducerException fatal(Exception parent) {
        final ProducerException e = new ProducerException(parent);
        e.fatal = true;
        return e;
    }

    public boolean isInvalidParam() {
        return invalidParam;
    }

    public boolean isFatal() {
        return fatal;
    }
}
