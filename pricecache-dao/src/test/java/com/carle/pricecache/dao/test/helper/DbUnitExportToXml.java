package com.carle.pricecache.dao.test.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DbUnitExportToXml {
	public static final String FILENAME = "src/test/resources/%s-export.xml";
	public static final String[] TABLES = new String[] { "vendor", "instrument", "price" };

	public static void main(final String[] args) throws SQLException {
		new DbUnitExportToXml().doExport();
	}

	public void doExport() {
		ConfigurableApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("spring-context-dao.xml");
			DataSource dataSource = (DataSource) context.getBean("dataSource");
			String filename = String.format(FILENAME, dataSource.getConnection().getMetaData().getUserName());

			// database connection
			IDatabaseConnection connection = new DatabaseConnection(dataSource.getConnection());

			QueryDataSet dataSet = new QueryDataSet(connection);
			for (String tableName : TABLES) {
				dataSet.addTable(tableName);
			}

			FlatXmlDataSet.write(dataSet, new FileOutputStream(filename));
		} catch (IOException | DatabaseUnitException | SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}
}
