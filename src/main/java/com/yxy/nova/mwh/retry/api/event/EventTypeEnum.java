package com.yxy.nova.mwh.retry.api.event;


public enum EventTypeEnum {
	BEGIN("开始执行"),
	SUCCESS("执行成功"),
	FAIL("执行失败"),
	EXPIRE("任务过期");

	private String desc;

	private EventTypeEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
