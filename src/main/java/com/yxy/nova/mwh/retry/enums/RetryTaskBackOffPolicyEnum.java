package com.yxy.nova.mwh.retry.enums;


public enum RetryTaskBackOffPolicyEnum {
	FIXED("固定");
	
	private String desc;

	private RetryTaskBackOffPolicyEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
