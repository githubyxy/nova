package com.yxy.nova.mwh.retry.scheduler;

import com.yxy.nova.mwh.retry.api.HandlerStatus;
import com.yxy.nova.mwh.retry.api.RetryTaskExecutionContext;
import com.yxy.nova.mwh.retry.api.RetryTaskHandler;
import com.yxy.nova.mwh.retry.api.event.EventTypeEnum;
import com.yxy.nova.mwh.retry.api.event.RetryTaskEvent;
import com.yxy.nova.mwh.retry.convertor.RetryTaskConverter;
import com.yxy.nova.mwh.retry.entity.RetryTask;
import com.yxy.nova.mwh.retry.enums.RetryTaskStateEnum;
import com.yxy.nova.mwh.retry.repository.RetryTaskRepository;
import com.yxy.nova.mwh.utils.log.TraceIdUtil;
import com.yxy.nova.mwh.utils.time.DateTimeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;

public class RetryTaskTemplate implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RetryTaskScheduler scheduler;

	private RetryTaskRepository retryTaskRepository;

	private RetryTask task;

	private RetryTaskHandler handler;

	private RetryTaskEventDispatcher eventDispatcher;
	
	public RetryTaskTemplate(RetryTaskScheduler scheduler, RetryTask task, RetryTaskHandler handler){
		this.scheduler = scheduler;
		this.task= task;
		this.handler = handler;
		retryTaskRepository = scheduler.getConfiguration().getRetryTaskRepository();
		eventDispatcher = scheduler.getEventDispatcher();
	}

	public void execute(RetryTask task, RetryTaskHandler handler) throws Exception {
		String oldState = task.getState();
		
		if (!setTaskStart(task)){
			logger.info("设置任务开始执行失败");
			return;
		}

		if (!SimpleRetryTaskPolicy.isAlive(task)) {
			logger.info("该任务已经过期或已结束， 不再执行");
			terminateTaskAbruptly(task, "该任务已经过期或已结束， 不再执行");

			task.setCurrentAttempt(task.getCurrentAttempt() + 1);
			RetryTaskExecutionContext context = RetryTaskConverter.retryTask2RetryTaskExecutionContext(task);
			triggerExpireEvent(context);
			return;
		}
		
		if(!SimpleRetryTaskPolicy.isTimeUp(task)){
			logger.info("还未到重试时间");
			restoreState(task, oldState);
			return;
		}


		task.setCurrentAttempt(task.getCurrentAttempt() + 1);
		RetryTaskExecutionContext context = RetryTaskConverter.retryTask2RetryTaskExecutionContext(task);
		HandlerStatus status = new HandlerStatus();
		logger.info("开始第{}次重试", task.getCurrentAttempt());
		triggerBeginEvent(context);
		try {
			handler.execute(context, status);
			if (status.isFail()) {
				setTaskFail(task, status.getMessage(), context);
			} else {
				setTaskSuccess(task, context);
			}
		} catch (Throwable throwable) {
			logger.error("", throwable);
			setTaskFail(task, throwable, context);
		} finally {
			endTaskExcution(task, context);
		}
	}

	/**
	 * 触发开始执行事件
	 * @param context
	 */
	private void triggerBeginEvent(RetryTaskExecutionContext context) {
		RetryTaskEvent event = new RetryTaskEvent();
		event.setEventType(EventTypeEnum.BEGIN);
		event.setContext(context);
		eventDispatcher.dispatchEvent(event);
	}

	/**
	 * 触发任务过期事件
	 * @param context
	 */
	private void triggerExpireEvent(RetryTaskExecutionContext context) {
		RetryTaskEvent event = new RetryTaskEvent();
		event.setEventType(EventTypeEnum.EXPIRE);
		event.setContext(context);
		eventDispatcher.dispatchEvent(event);
	}

	private void restoreState(RetryTask task, String oldState) throws Exception{
		retryTaskRepository.updateState(task.getShardingNumber(), task.getId(), oldState);
	}

	private void endTaskExcution(RetryTask task, RetryTaskExecutionContext context) throws Exception {
		Date now = new Date();
		task.setEndTime(now);

		boolean successFinished = RetryTaskStateEnum.SUCCESS.name().equals(task.getState());
		boolean failFinished = !SimpleRetryTaskPolicy.isAlive(task) && !successFinished;
		boolean finished = failFinished || successFinished;

		retryTaskRepository.endTaskExcution(task.getShardingNumber(), task.getId(), task.getState(), finished, now, task.getMessage(), task.getCurrentAttempt());

		if (failFinished) {
			triggerExpireEvent(context);
		}

	}

	/**
	 * 设置任务执行失败
	 * @param task
	 * @param throwable
	 * @param context
	 */
	private void setTaskFail(RetryTask task, Throwable throwable, RetryTaskExecutionContext context) {
		task.setState(RetryTaskStateEnum.FAIL.name());
		task.setMessage(trimMessage(ExceptionUtils.getStackTrace(throwable)));

		logger.info("任务执行失败，任务ID:{}", task.getTaskId());

		triggerFailEvent(context, null, throwable);
	}
	
	private String trimMessage(String message){
		if(StringUtils.isEmpty(message)) {
			return message;
		}

		final int MAX_LENGTH = 4096;

		if(message.length() > MAX_LENGTH) {
			return message.substring(0, MAX_LENGTH);
		} else {
			return message;
		}
	}

	/**
	 * 设置任务成功 
	 * @param task
	 */
	private void setTaskSuccess(RetryTask task, RetryTaskExecutionContext context) {
		task.setState(RetryTaskStateEnum.SUCCESS.name());

		logger.info("任务执行成功，任务ID:{}", task.getTaskId());

		triggerSuccessEvent(context);
	}

	private void triggerSuccessEvent(RetryTaskExecutionContext context) {
		RetryTaskEvent event = new RetryTaskEvent();
		event.setEventType(EventTypeEnum.SUCCESS);
		event.setContext(context);
		eventDispatcher.dispatchEvent(event);
	}

	/**
	 * 设置任务失败
	 * @param task
	 * @param message
	 */
	private void setTaskFail(RetryTask task, String message, RetryTaskExecutionContext context) {
		task.setState(RetryTaskStateEnum.FAIL.name());
		task.setMessage(trimMessage(message));

		logger.info("任务执行失败，任务ID:{}", task.getTaskId());

		triggerFailEvent(context, message, null);
	}

	private void triggerFailEvent(RetryTaskExecutionContext context, String message, Throwable throwable) {
		RetryTaskEvent event = new RetryTaskEvent();
		event.setEventType(EventTypeEnum.FAIL);
		event.setContext(context);
		event.setMessage(message);
		event.setThrowable(throwable);
		eventDispatcher.dispatchEvent(event);
	}

	/**
	 * 异常终止任务执行 
	 * @param task
	 * @param message
	 * @throws Exception
	 */
	private void terminateTaskAbruptly(RetryTask task, String message) throws Exception{

		retryTaskRepository.terminateTaskAbruptly(task.getShardingNumber(), task.getId(), RetryTaskStateEnum.FAIL.name(), trimMessage(message));

	}

	/**
	 * 设置任务执行开始
	 * 
	 * @param task
	 * @return
	 */
	private boolean setTaskStart(final RetryTask task) {
		try {
			Date now = new Date();
			String newVersion = DateTimeUtil.datetime14() + "-" + RandomStringUtils.randomNumeric(3);
			return retryTaskRepository.setTaskStart(task.getShardingNumber(), task.getId(), task.getBusinessTransactionVersion(),
					RetryTaskStateEnum.PROCESSING.name(), now, newVersion);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	@Override
	public void run() {
		MDC.put(TraceIdUtil.TRACEID_NAME, TraceIdUtil.genId());
		try{
			logger.info("执行重试任务开始: taskType={}, taskId={}, maxAttempt={}, currentAttempt={}, earliestExecutionTime={}, taskDeadline={}, data={}, state={}, endTime={}",
					task.getTaskType(), task.getTaskId(), task.getMaxAttempt(), task.getCurrentAttempt(), task.getEarliestExecutionTime(), task.getTaskDeadline(), task.getData(), task.getState(), task.getEndTime());
			execute(task, handler);
		}catch(Exception e){
			logger.error("执行重试任务开始出现异常", e);
		}finally{
			MDC.remove(TraceIdUtil.TRACEID_NAME);
		}
	}

}
