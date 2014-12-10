package com.carle.pricecache.messaging;

import org.springframework.beans.factory.annotation.Autowired;

import com.carle.pricecache.model.Price;
import com.carle.pricecache.service.InvalidPriceException;
import com.carle.pricecache.service.PriceCachePublisher;

public class PriceHandler {
	@Autowired
	private PriceCachePublisher priceCachePublisher;

	public void onReceivePrice(final Price price) throws InvalidPriceException {
		System.out.println("PRICE CACHE RECEIVED A PRICE MESSAGE: " + price);

		priceCachePublisher.addPrice(price);
	}
}
