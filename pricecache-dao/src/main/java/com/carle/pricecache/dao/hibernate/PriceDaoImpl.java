package com.carle.pricecache.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.carle.pricecache.dao.PriceDao;
import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Instrument;
import com.carle.pricecache.model.Price;
import com.carle.pricecache.model.Vendor;

@Repository
public class PriceDaoImpl extends AbstractHibernateDaoImpl implements PriceDao {
	private static final Logger logger = LoggerFactory.getLogger(PriceDaoImpl.class);

	private static final String FIND_LATEST_BY_INSTRUMENT_AND_VENDOR = "select price from Price price join price.vendor vendor join price.instrument inst "
			+ "where vendor.vendorId = :vendorId and inst.instrumentId = :instrumentId and price.isLatest = true";
	private static final String FIND_ALL_LATEST_BY_VENDOR_NAME = "select price from Price price join price.vendor vendor "
			+ "where vendor.name = :vendorName and price.isLatest = true";
	private static final String FIND_ALL_LATEST_BY_ID_TYPE_AND_VALUE = "select price from Price price join price.instrument inst "
			+ "where inst.%s = :idValue and price.isLatest = true";
	private static final String FIND_ALL_LATEST = "select price from Price price join price.instrument inst join price.vendor vendor "
			+ "where price.isLatest = true order by inst.ticker, vendor.name";

	@Autowired
	public PriceDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Price create(final Price newPrice) {
		// see if we have an existing price from this vendor for this instrument
		Price currentPrice = findLatestByInstrumentAndVendor(newPrice.getVendor(), newPrice.getInstrument());

		// if so, update it to reflect that it is no longer the latest
		if (currentPrice != null) {
			currentPrice.setIsLatest(false);
			currentSession().save(currentPrice);
		}

		return (Price) currentSession().merge(newPrice);

	}

	@Override
	public Price create(final DateTime createTime, final BigDecimal priceValue, final Vendor vendor,
			final Instrument instrument) {

		// now create the new price as the latest
		Price newPrice = new Price();
		newPrice.setCreateTime(createTime);
		newPrice.setPrice(priceValue);
		newPrice.setVendor(vendor);
		newPrice.setInstrument(instrument);

		return create(newPrice);
	}

	@Override
	public Price findLatestByInstrumentAndVendor(final Vendor vendor, final Instrument instrument) {
		return (Price) currentSession().createQuery(FIND_LATEST_BY_INSTRUMENT_AND_VENDOR)
				.setInteger("vendorId", vendor.getVendorId())
				.setInteger("instrumentId", instrument.getInstrumentId())
				.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Price> findAllLatest() {
		List<Price> prices = currentSession().createQuery(FIND_ALL_LATEST)
				.list();
		return prices;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Price> findAllLatestByVendorName(final String vendorName) {
		List<Price> prices = currentSession().createQuery(FIND_ALL_LATEST_BY_VENDOR_NAME)
				.setString("vendorName", vendorName)
				.list();
		return prices;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Price> findAllLatestByIdTypeAndValue(final IdType idType, final String idValue) {
		// replace placeholder in the HQL string with the column name for the given identifier type
		String idTypeColumnName = idType.name()
				.toLowerCase();

		String queryString = String.format(FIND_ALL_LATEST_BY_ID_TYPE_AND_VALUE, idTypeColumnName);

		if (logger.isDebugEnabled()) {
			logger.debug("Formatted query for findAllLatestByIdTypeAndValue(): " + queryString);
		}

		return currentSession().createQuery(queryString)
				.setString("idValue", idValue)
				.list();
	}
}
