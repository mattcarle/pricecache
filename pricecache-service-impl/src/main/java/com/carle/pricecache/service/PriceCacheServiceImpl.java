package com.carle.pricecache.service;

import static com.carle.pricecache.service.InvalidPriceException.Reason.MissingInstrument;
import static com.carle.pricecache.service.InvalidPriceException.Reason.MissingVendor;
import static com.carle.pricecache.service.InvalidPriceException.Reason.NoInstrumentIds;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carle.pricecache.dao.InstrumentDao;
import com.carle.pricecache.dao.PriceDao;
import com.carle.pricecache.dao.VendorDao;
import com.carle.pricecache.messaging.PriceSender;
import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Instrument;
import com.carle.pricecache.model.Price;
import com.carle.pricecache.model.Vendor;
import com.carle.pricecache.service.InvalidPriceException.Reason;

@Service
public class PriceCacheServiceImpl implements PriceCacheRequesterSync, PriceCacheRequesterAsync, PriceCachePublisher {
	private static final Logger logger = LoggerFactory.getLogger(PriceCacheServiceImpl.class);

	private final InstrumentDao instrumentDao;
	private final VendorDao vendorDao;
	private final PriceDao priceDao;
	private final PriceSender priceSender;

	@Autowired
	public PriceCacheServiceImpl(final InstrumentDao instrumentDao, final VendorDao vendorDao, final PriceDao priceDao,
			final PriceSender priceSender) {
		this.instrumentDao = instrumentDao;
		this.vendorDao = vendorDao;
		this.priceDao = priceDao;
		this.priceSender = priceSender;
	}

	@Override
	public void addPrice(final Price price) throws InvalidPriceException {
		validatePrice(price);

		Instrument instrument = price.getInstrument();
		if (instrument.getInstrumentId() == null) {
			// see if we have a persisted instrument matching the one passed in
			Instrument persistedInstrument = instrumentDao.findByAnyId(instrument.getInstrumentIdsAsMap());
			if (persistedInstrument == null) {
				// if not, create one
				persistedInstrument = instrumentDao.create(instrument.getInstrumentIdsAsMap());
			}

			// if we have additional instrument identifiers on this price object, merge them in
			persistedInstrument.mergeIdentifiers(instrument);

			// and now set the instrument on the price
			price.setInstrument(persistedInstrument);
		}

		Vendor vendor = price.getVendor();
		if (vendor.getVendorId() == null) {
			// see if we have a persisted vendor matching the one passed in
			Vendor persistedVendor = vendorDao.findByName(vendor.getName());
			if (persistedVendor == null) {
				// if not, create one
				persistedVendor = vendorDao.create(vendor.getName());
			}

			// now set it on the price
			price.setVendor(persistedVendor);
		}

		// persist the price
		Price insertedPrice = priceDao.create(price);

		// publish the price out to consumers
		priceSender.send(insertedPrice);
	}

	protected void validatePrice(final Price price) throws InvalidPriceException {
		List<Reason> reasons = new ArrayList<>();

		Instrument instrument = price.getInstrument();
		if (instrument == null) {
			reasons.add(MissingInstrument);
		}
		if (instrument.getInstrumentIdsAsMap()
				.isEmpty()) {
			reasons.add(NoInstrumentIds);
		}

		Vendor vendor = price.getVendor();
		if (vendor == null || isBlank(vendor.getName())) {
			reasons.add(MissingVendor);
		}

		if (price.getPrice() == null) {
			reasons.add(Reason.MissingPriceValue);
		}

		if (!reasons.isEmpty()) {
			throw new InvalidPriceException(price, reasons);
		}
	}

	@Override
	public void addPrice(final DateTime createTime, final BigDecimal priceValue, final String vendorName,
			final Map<IdType, String> instrumentIds) throws InvalidPriceException {
		addPrice(new Price(null, priceValue, createTime, new Vendor(vendorName), new Instrument(instrumentIds)));
	}

	@Override
	public List<Price> getLatestPricesForAllInstrumentsFromAllVendors() {
		return priceDao.findAllLatest();
	}

	@Override
	public List<Price> getLatestPricesForAllInstrumentsFromVendor(final String vendorName) {
		return priceDao.findAllLatestByVendorName(vendorName);
	}

	@Override
	public List<Price> getLatestPricesForInstrumentFromAllVendors(final IdType idType, final String idValue) {
		return priceDao.findAllLatestByIdTypeAndValue(idType, idValue);
	}

	@Override
	public List<Price> getHistoricPricesForAllInstrumentsFromVendor(final String vendorName, final LocalDate fromDate,
			final LocalDate toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Price> getHistoricPricesForInstrument(final String vendorName, final LocalDate fromDate,
			final LocalDate toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestLatestPricesForAllInstrumentsFromVendor(final String vendorName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestLatestPricesForInstrumentFromAllVendors(final IdType idType, final String idValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestHistoricPricesForAllInstrumentsFromVendor(final String vendorName, final LocalDate fromDate,
			final LocalDate toDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestHistoricPricesForInstrument(final String vendorName, final LocalDate fromDate,
			final LocalDate toDate) {
		// TODO Auto-generated method stub

	}
}
