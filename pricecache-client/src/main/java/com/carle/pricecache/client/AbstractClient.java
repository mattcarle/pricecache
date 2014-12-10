package com.carle.pricecache.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractClient {
	private final ApplicationContext appContext;

	public AbstractClient() {
		appContext = new ClassPathXmlApplicationContext((getConfigLocations()));
	}

	public abstract void run();

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public String[] getConfigLocations() {
		return new String[] { "classpath:spring-context-client.xml" };
	}
}
