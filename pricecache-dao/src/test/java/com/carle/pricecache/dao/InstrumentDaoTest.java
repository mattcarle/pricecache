package com.carle.pricecache.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
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

@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "spring-context-dao.xml", "test-spring-context-dao.xml" })
@DataSet(value = "/pricecache-dbunit-data.xml", loadStrategy = CleanInsertLoadStrategy.class)
@Transactional(value = TransactionMode.ROLLBACK)
public class InstrumentDaoTest {
	@SpringBeanByType
	InstrumentDao instrumentDao;

	@SpringBeanByType
	SessionFactory sessionFactory;

	/*
	 * @BeforeClass public static void createSchema() throws Exception {
	 * RunScript.execute("jdbc:hsqldb:hsql://localhost/test", "sa", "", "classpath:h2-ddl.sql", null, false);
	 * System.out.println("Done creating schema!"); }
	 */

	@Test
	public void testFindByAnyId_oneId_match() {
		Map<IdType, String> instrumentIds = new HashMap<>();
		instrumentIds.put(IdType.ISIN, "GB00B16GWD56"); // Vodafone

		Instrument instrument = instrumentDao.findByAnyId(instrumentIds);

		Assert.assertNotNull(instrument);
		Assert.assertEquals(1004, instrument.getInstrumentId()
				.intValue());
		Assert.assertEquals("GB00B16GWD56", instrument.getIsin());
		Assert.assertEquals("B16GWD5", instrument.getSedol());
		Assert.assertNull(instrument.getCusip());
		Assert.assertEquals("VOD", instrument.getTicker());
		Assert.assertEquals("VOD.L", instrument.getRic());
		Assert.assertEquals("VOD LN", instrument.getBbgid());
	}

	@Test
	public void testFindByAnyId_oneId_noMatch() {
		Map<IdType, String> instrumentIds = new HashMap<>();
		instrumentIds.put(IdType.SEDOL, "A12ABC0");

		Instrument instrument = instrumentDao.findByAnyId(instrumentIds);

		Assert.assertNull(instrument);
	}

	@Test
	public void testFindByAnyId_twoIds_match() {
		Map<IdType, String> instrumentIds = new HashMap<>();
		instrumentIds.put(IdType.RIC, "IBM.N");
		instrumentIds.put(IdType.TICKER, "IBM");

		Instrument instrument = instrumentDao.findByAnyId(instrumentIds);

		Assert.assertNotNull(instrument);
		Assert.assertEquals(1005, instrument.getInstrumentId()
				.intValue());
	}

	@Test
	public void testFindByAnyId_twoIds_noMatch() {
		Map<IdType, String> instrumentIds = new HashMap<>();
		instrumentIds.put(IdType.RIC, "TSCO.L");
		instrumentIds.put(IdType.TICKER, "TSCO");

		Instrument instrument = instrumentDao.findByAnyId(instrumentIds);
		Assert.assertNull(instrument);
	}

	@Test
	public void testFindByAnyId_emptyIdMap() {
		Map<IdType, String> instrumentIds = new HashMap<>();
		Instrument instrument = instrumentDao.findByAnyId(instrumentIds);
		Assert.assertNull(instrument);
	}

	@Test
	public void testCreate() {
		Map<IdType, String> instrumentIds = new HashMap<>();
		instrumentIds.put(IdType.RIC, "XOM.N");
		instrumentIds.put(IdType.TICKER, "XOM");
		instrumentIds.put(IdType.ISIN, "US30231G1022");

		Instrument instrument = instrumentDao.create(instrumentIds);
		Assert.assertNotNull(instrument);
		Assert.assertNotNull(instrument.getInstrumentId());

		// flush and clear the session so that the call below to retrieve the instrument fetches the record from the
		// database rather than from the Hibernate cache
		Session session = sessionFactory.getCurrentSession();
		session.flush();
		session.clear();

		// retrieve the instrument from the DB: it will not be the same instance, but it will be equal
		Instrument retrievedInstrument = instrumentDao.findByAnyId(instrumentIds);
		Assert.assertNotNull(retrievedInstrument);
		Assert.assertNotSame(instrument, retrievedInstrument);
		Assert.assertEquals(instrument, retrievedInstrument);
	}

}
