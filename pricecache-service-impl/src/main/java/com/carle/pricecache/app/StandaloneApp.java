package com.carle.pricecache.app;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StandaloneApp {
	private final ApplicationContext appContext;

	public static void main(final String[] args) {
		new StandaloneApp();
	}

	public StandaloneApp() {
		appContext = new ClassPathXmlApplicationContext("classpath:spring-context-service.xml",
				"classpath:spring-context-messaging.xml", "classpath:spring-context-dao.xml",
				"classpath:spring-context-dao-h2.xml");

		Properties h2Props = (Properties) appContext.getBean("h2Properties");

		System.out.println("\n************************************************************");
		System.out.println("PRICE CACHE IS RUNNING: " + appContext);
		System.out.println("H2 Database Path: " + h2Props.getProperty("dbpath"));
		System.out.println("************************************************************\n");
	}

}
