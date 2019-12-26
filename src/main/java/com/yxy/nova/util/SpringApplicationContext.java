package com.yxy.nova.util;

import com.yxy.nova.service.base.UserBaseService;
import org.springframework.context.ApplicationContext;

public class SpringApplicationContext {
	private static ApplicationContext ctx;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ctx = applicationContext;
	}

	/**
	 * 获取spring上下文，在应用启动阶段调用这个方法可能返回null
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	/**
	 * 从spring上下文中获取UserBaseService bean实例
	 * @return
	 */
	public static UserBaseService getUserBaseService() {
		return getApplicationContext().getBean(UserBaseService.class);
	}


	private SpringApplicationContext() {}

}
