package com.carle.pricecache.web;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Price;
import com.carle.pricecache.service.InvalidPriceException;
import com.carle.pricecache.service.PriceCachePublisher;
import com.carle.pricecache.service.PriceCacheRequesterSync;

@Controller
@RequestMapping("/prices")
public class PriceCacheController {
	private static final String VIEW_DESCRIPTION_ATTR_NAME = "viewDescription";

	private static final String PRICE_ATTR_NAME = "price";
	private static final String PRICE_LIST_ATTR_NAME = "prices";
	private static final String PRICES_VIEW = "prices";
	private static final String NEW_PRICE_VIEW = "new-price";

	private static final Logger logger = LoggerFactory.getLogger(PriceCacheController.class);

	@Autowired
	PriceCacheRequesterSync priceCacheRequester;

	@Autowired
	PriceCachePublisher priceCachePublisher;

	@RequestMapping(value = { "/", "/latest", "/latest/all" }, method = RequestMethod.GET)
	public String getAllLatestPrices(final Model model) {
		model.addAttribute(VIEW_DESCRIPTION_ATTR_NAME, "Latest Prices for All Instruments from All Vendors");
		model.addAttribute(PRICE_LIST_ATTR_NAME, priceCacheRequester.getLatestPricesForAllInstrumentsFromAllVendors());

		return PRICES_VIEW;
	}

	@RequestMapping(value = "/latest/vendorName/{vendorName}", method = RequestMethod.GET)
	public String getLatestPricesForAllInstrumentsFromVendor(@PathVariable("vendorName") final String vendorName,
			final Model model) {
		model.addAttribute(VIEW_DESCRIPTION_ATTR_NAME,
				String.format("Latest Prices for All Instruments from Vendor '%s'", vendorName));
		model.addAttribute(PRICE_LIST_ATTR_NAME,
				priceCacheRequester.getLatestPricesForAllInstrumentsFromVendor(vendorName));

		return PRICES_VIEW;
	}

	@RequestMapping(value = "/latest/idType/{idType}/idValue/{idValue}", method = RequestMethod.GET)
	public String getLatestPricesForInstrumentFromAllVendors(@PathVariable("idType") final String strIdType,
			@PathVariable("idValue") final String idValue, final Model model) {
		IdType idType = IdType.valueOf(strIdType.toUpperCase());
		model.addAttribute(VIEW_DESCRIPTION_ATTR_NAME,
				String.format("Latest Prices for Instrument %s = '%s' from All Vendors", idType, idValue));
		model.addAttribute(PRICE_LIST_ATTR_NAME,
				priceCacheRequester.getLatestPricesForInstrumentFromAllVendors(idType, idValue));
		return PRICES_VIEW;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newPriceForm(final Model model) {
		model.addAttribute(PRICE_ATTR_NAME, new Price());
		return NEW_PRICE_VIEW;
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String addPrice(final Price price, final BindingResult result, final HttpServletResponse response)
			throws BindException, InvalidPriceException {

		if (result.hasErrors()) {
			throw new BindException(result);
		}

		priceCachePublisher.addPrice(price);

		return "redirect:/prices/latest/all";

	}
}
