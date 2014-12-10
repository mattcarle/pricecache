package com.carle.pricecache.client;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.carle.pricecache.model.Instrument;
import com.carle.pricecache.model.Price;
import com.carle.pricecache.model.Vendor;

public class Publisher extends AbstractClient {
	private static final String QUIT_STRING = ":q!";

	public static void main(final String[] args) {
		new Publisher().run();
	}

	@Override
	public void run() {
		System.out.println("\n\n\n\n************************************************************");
		System.out.println("PUBLISHER IS RUNNING: " + getAppContext());
		System.out.println("************************************************************\n");

		System.out.println("Follow the prompts. Enter blank line for null value. Enter :q! to quit");

		JmsTemplate jmsTemplate = getAppContext().getBean(JmsTemplate.class);
		Destination destination = (Destination) getAppContext().getBean("publishDestination");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {

			while (true) {
				System.out.println("----------------------------------------------------------------------");
				final Price price = new Price();
				price.setPrice(readPrice(reader));
				price.setVendor(readVendor(reader));
				price.setInstrument(readInstrument(reader));
				System.out.println(String.format("%nPUBLISHER IS SENDING: %s%n", price));

				try {
					jmsTemplate.send(destination, new MessageCreator() {
						@Override
						public Message createMessage(final Session session) throws JMSException {
							return session.createObjectMessage(price);
						}
					});
				} catch (JmsException jmsEx) {
					System.err.println("Failed to send message - check that PriceCache Service/Message Broker is running: "
							+ jmsEx);
				}
			}
		} catch (QuitAppException e) {
			System.out.println("\nGoodbye");
		}
	}

	private String readStringValue(final BufferedReader reader, final boolean acceptNulls)
			throws QuitAppException {
		while (true) {
			try {
				String strValue = reader.readLine();
				if (isBlank(strValue)) {
					if (!acceptNulls) {
						throw new IllegalArgumentException();
					}
					strValue = null;
				}
				else if (strValue.equals(QUIT_STRING)) {
					throw new QuitAppException();
				}
				return strValue;
			} catch (IOException | IllegalArgumentException ex) {
				System.out.print("Invalid Value. Try again: ");
			}
		}
	}

	private String readStringValueNotNull(final BufferedReader reader) throws QuitAppException {
		return this.readStringValue(reader, false);
	}

	private String readStringValueNullable(final BufferedReader reader) throws QuitAppException {
		return this.readStringValue(reader, true);
	}

	private Instrument readInstrument(final BufferedReader reader) throws QuitAppException {
		Instrument instrument = new Instrument();

		System.out.print("TICKER: ");
		instrument.setTicker(readStringValueNullable(reader));

		System.out.print("RIC: ");
		instrument.setRic(readStringValueNullable(reader));

		System.out.print("BBGID: ");
		instrument.setBbgid(readStringValueNullable(reader));

		System.out.print("ISIN: ");
		instrument.setIsin(readStringValueNullable(reader));

		System.out.print("CUSIP: ");
		instrument.setCusip(readStringValueNullable(reader));

		System.out.print("SEDOL: ");
		instrument.setSedol(readStringValueNullable(reader));

		return instrument;
	}

	private Vendor readVendor(final BufferedReader reader) throws QuitAppException {
		System.out.print("Vendor: ");
		return new Vendor(readStringValueNotNull(reader));
	}

	private BigDecimal readPrice(final BufferedReader reader) throws QuitAppException {
		System.out.print("Price: ");
		BigDecimal priceValue;
		while (true) {
			try {
				String str = reader.readLine();
				if (str != null && str.equals(QUIT_STRING)) {
					throw new QuitAppException();
				}
				priceValue = new BigDecimal(str);
				break;
			} catch (NumberFormatException | IOException ex) {
				System.out.print("Invalid Price. Try again: ");
			}
		}
		return priceValue;
	}
}
