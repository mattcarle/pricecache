package com.carle.pricecache.client;

public class Subscriber extends AbstractClient {
	public static void main(final String[] args) {
		new Subscriber().run();
	}

	@Override
	public void run() {
		System.out.println("\n\n\n\n************************************************************");
		System.out.println(String.format("SUBSCRIBER IS RUNNING WITH SELECTOR \"%s\": %s",
				System.getProperty("selector"), getAppContext()));
		System.out.println("************************************************************\n");
	}

	@Override
	public String[] getConfigLocations() {
		return new String[] { "classpath:spring-context-client.xml", "classpath:spring-context-client-listener.xml" };
	}
}
