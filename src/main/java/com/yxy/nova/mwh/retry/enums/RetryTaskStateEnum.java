package com.yxy.nova.mwh.retry.enums;


public enum RetryTaskStateEnum {
	NOT_BEGIN("未开始"),
	PROCESSING("处理中"),
	SUCCESS("处理成功"),
	FAIL("处理失败"),
	CANCELLED("取消");

	private String desc;

	private RetryTaskStateEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
