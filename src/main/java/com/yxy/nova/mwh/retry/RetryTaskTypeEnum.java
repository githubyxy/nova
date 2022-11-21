package com.yxy.nova.mwh.retry;


public enum RetryTaskTypeEnum {
	MULTITHREAD(""),
	SINGLETHREAD(""),
    ;

	private String desc;

	private RetryTaskTypeEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
