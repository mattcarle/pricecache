package com.carle.pricecache.client;

import org.springframework.stereotype.Component;

import com.carle.pricecache.model.Price;

@Component
public class ClientPriceHandler {
	public void onReceivePrice(final Price price) {
		System.out.println("RECEIVED PRICE MESSAGE: " + price);
	}
}
