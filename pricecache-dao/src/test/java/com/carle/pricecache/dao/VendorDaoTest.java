package com.carle.pricecache.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.datasetloadstrategy.impl.CleanInsertLoadStrategy;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;
import org.unitils.spring.annotation.SpringBeanByType;

import com.carle.pricecache.model.Price;
import com.carle.pricecache.model.Vendor;

@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "spring-context-dao.xml", "test-spring-context-dao.xml" })
@DataSet(value = "/pricecache-dbunit-data.xml", loadStrategy = CleanInsertLoadStrategy.class)
@Transactional(value = TransactionMode.ROLLBACK)
public class VendorDaoTest {
	@SpringBeanByType
	VendorDao vendorDao;

	@SpringBeanByName
	DataSource dataSource;

	@Ignore
	@Test
	public void testSchemaCreated() {
		Connection conn;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getTables(null, "PUBLIC", "%", null);

			// ResultSet rs = metaData.getTables(null, "pricecache", "%", new String[] { "TABLE" });
			ResultSetMetaData rsMetaData = rs.getMetaData();
			for (int i = 1; i < rsMetaData.getColumnCount(); i++) {
				if (i > 1) {
					System.out.print(", ");
				}
				System.out.print(rsMetaData.getColumnLabel(i));
			}
			System.out.println();
			while (rs.next()) {
				for (int i = 1; i < rsMetaData.getColumnCount(); i++) {
					if (i > 1) {
						System.out.print(", ");
					}
					System.out.print(rs.getString(i));
				}
				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(0, new JdbcTemplate(dataSource).queryForInt("select count(*) from Price"));
	}

	@Test
	public void testCreateVendor() {
		Vendor vendor = vendorDao.create("Moodys");
		Assert.assertNotNull(vendor.getVendorId());
		Assert.assertEquals("Moodys", vendor.getName());
	}

	@Test
	public void testFindVendorByName() {
		Vendor vendor = vendorDao.findByName("Reuters");
		Assert.assertEquals("Reuters", vendor.getName());
		Assert.assertEquals(1005, vendor.getVendorId()
				.intValue());
	}

	@Test
	public void testFindVendorByNameNotFound() {
		Vendor vendor = vendorDao.findByName("XYZ");
		Assert.assertNull(vendor);
	}

	@Test
	public void testGetPrices() {
		Vendor vendor = vendorDao.findByName("Reuters");
		Set<Price> prices = vendor.getPrices();
		Assert.assertEquals(5, prices.size());
	}

}
