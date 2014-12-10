package com.carle.pricecache.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.datasetloadstrategy.impl.CleanInsertLoadStrategy;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Instrument;
import com.carle.pricecache.model.Price;
import com.carle.pricecache.model.Vendor;

// TODO: Do proper comparison of expected vs. actual
// TODO: Add more unit tests

@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "spring-context-dao.xml", "test-spring-context-dao.xml" })
@DataSet(value = "/pricecache-dbunit-data.xml", loadStrategy = CleanInsertLoadStrategy.class)
@Transactional(value = TransactionMode.ROLLBACK)
public class PriceDaoTest {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

	@SpringBeanByType
	PriceDao priceDao;

	@SpringBeanByType
	VendorDao vendorDao;

	@SpringBeanByType
	InstrumentDao instrumentDao;

	@Test
	public void testCreate() {
		DateTime createTime = new DateTime();
		BigDecimal priceValue = BigDecimal.valueOf(123.45);

		Vendor vendor = vendorDao.findByName("Reuters");

		Map<IdType, String> idMap = new HashMap<>();
		idMap.put(IdType.RIC, "IBM.N");
		Instrument instrument = instrumentDao.findByAnyId(idMap);

		Price price = priceDao.create(createTime, priceValue, vendor, instrument);
		assertNotNull(price);
	}

	@Test
	public void testFindAllLatestByVendorName() {
		List<Price> latestReutersPrices = priceDao.findAllLatestByVendorName("Reuters");
		assertEquals(3, latestReutersPrices.size());
	}

	@Test
	public void testFindAllLatestByIdTypeAndValue() {
		List<Price> latestAppleStockPrices = priceDao.findAllLatestByIdTypeAndValue(IdType.TICKER, "AAPL");
		assertEquals(2, latestAppleStockPrices.size());
	}

}
