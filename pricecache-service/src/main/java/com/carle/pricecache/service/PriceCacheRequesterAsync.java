package com.carle.pricecache.service;

import org.joda.time.LocalDate;

import com.carle.pricecache.model.IdType;

/**
 * API for clients to request data from the PriceCache asynchronously. Usage of this class assumes that the client has
 * established a subscription to the service for the same instrument/vendors that they are requesting via the method
 * call.
 * 
 * @author Matt Carle
 */
public interface PriceCacheRequesterAsync {

	void requestLatestPricesForAllInstrumentsFromVendor(String vendorName);

	void requestLatestPricesForInstrumentFromAllVendors(IdType idType, String idValue);

	void requestHistoricPricesForAllInstrumentsFromVendor(String vendorName, LocalDate fromDate, LocalDate toDate);

	void requestHistoricPricesForInstrument(String vendorName, LocalDate fromDate, LocalDate toDate);

}
