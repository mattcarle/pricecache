package com.carle.pricecache.service;

import java.math.BigDecimal;
import java.util.Map;

import org.joda.time.DateTime;

import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Price;

/**
 * API for clients to publish prices to the PriceCache. Two methods are available: one to supply a Price object and the
 * other which takes the same information, but in separate arguments.
 * 
 * @author Matt Carle
 */
public interface PriceCachePublisher {
	void addPrice(Price price) throws InvalidPriceException;

	void addPrice(DateTime createTime, BigDecimal price, String vendorName, Map<IdType, String> instrumentIds)
			throws InvalidPriceException;

}
