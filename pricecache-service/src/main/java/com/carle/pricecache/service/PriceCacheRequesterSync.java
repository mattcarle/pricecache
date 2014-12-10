package com.carle.pricecache.service;

import java.util.List;

import org.joda.time.LocalDate;

import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Price;

/**
 * API for clients to request data from the PriceCache synchronously. All the methods in this class execute
 * synchronously and return a list of prices to the caller
 * 
 * @author Matt Carle
 */

public interface PriceCacheRequesterSync {

	List<Price> getLatestPricesForAllInstrumentsFromVendor(String vendorName);

	List<Price> getLatestPricesForInstrumentFromAllVendors(IdType idType, String idValue);

	List<Price> getLatestPricesForAllInstrumentsFromAllVendors();

	List<Price> getHistoricPricesForAllInstrumentsFromVendor(String vendorName, LocalDate fromDate, LocalDate toDate);

	List<Price> getHistoricPricesForInstrument(String vendorName, LocalDate fromDate, LocalDate toDate);

}
