package com.carle.pricecache.dao;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Instrument;
import com.carle.pricecache.model.Price;
import com.carle.pricecache.model.Vendor;

@Transactional
public interface PriceDao {
	Price create(Price price);

	Price create(DateTime createTime, BigDecimal priceValue, Vendor vendor, Instrument instrument);

	Price findLatestByInstrumentAndVendor(Vendor vendor, Instrument instrument);

	List<Price> findAllLatest();

	List<Price> findAllLatestByVendorName(String vendorName);

	List<Price> findAllLatestByIdTypeAndValue(IdType idType, String idValue);

}
