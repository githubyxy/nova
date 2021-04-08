package com.yxy.nova.mwh.retry.scheduler;


import com.yxy.nova.mwh.retry.entity.RetryTask;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;

import java.util.Date;


public class SimpleRetryTaskPolicy {

	/**
	 * 判断任务是否到了可以执行的时间
	 * @param task
	 * @return
	 */
	public static boolean isTimeUp(RetryTask task) {
		Date now = new Date();
		Date lastRunTime = task.getEndTime();
		if(lastRunTime == null) {
			return true;
		}
		return DateTimeUtil.plusSeconds(lastRunTime, task.getFixedBackoffPeriod()).compareTo(now) <= 0;
	}

	/**
	 * 判断任务是否还能继续重试
	 * @param task
	 * @return
	 */
	public static boolean isAlive(RetryTask task) {
		Date now = new Date();
		if(task.getTaskDeadline() != null && now.compareTo(task.getTaskDeadline()) > 0) {
			return false;
		}
		if(task.getMaxAttempt() != null && task.getMaxAttempt() > 0 && task.getCurrentAttempt() >= task.getMaxAttempt()) {
			return false;
		}
		return true;
	}

}
