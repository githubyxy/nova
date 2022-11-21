package com.yxy.nova.mwh.elasticsearch.facade.container;

public interface Packer<T> {
    public T pack(Object a);
}
