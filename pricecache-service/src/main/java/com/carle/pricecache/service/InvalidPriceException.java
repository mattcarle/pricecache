package com.carle.pricecache.service;

import java.util.List;

import com.carle.pricecache.model.Price;

public class InvalidPriceException extends Exception {
	private static final long serialVersionUID = 1L;

	private final Price price;
	private final List<Reason> reasons;

	static enum Reason
	{
		MissingInstrument("Instrument is missing"),
		NoInstrumentIds("No instrument identifiers have been specified"),
		MissingVendor("Vendor is missing"),
		MissingPriceValue("Price value is missing");

		private final String message;

		Reason(final String message) {
			this.message = message;
		}
	};

	public InvalidPriceException(final Price price, final List<Reason> reasons) {
		super(formatMessage(price, reasons));

		this.price = price;
		this.reasons = reasons;
	}

	private static String formatMessage(final Price price, final List<Reason> reasons) {
		StringBuilder sb = new StringBuilder();
		for (Reason reason : reasons) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(reason.message);
		}
		sb.insert(0, "Price is invalid due to: ");
		sb.append(" - ");
		sb.append(price);

		return sb.toString();

	}

	public Price getPrice() {
		return price;
	}

	public List<Reason> getReasons() {
		return reasons;
	}

}
