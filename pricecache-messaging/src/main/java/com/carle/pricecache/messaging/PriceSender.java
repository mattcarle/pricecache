package com.carle.pricecache.messaging;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.carle.pricecache.model.Price;

@Component
public class PriceSender {
	private final JmsTemplate jmsTemplate;
	private final Destination destination;

	@Autowired
	public PriceSender(final JmsTemplate jmsTemplate, @Qualifier("outboundDestination") final Destination destination) {
		this.jmsTemplate = jmsTemplate;
		this.destination = destination;
	}

	public void send(final Price price) {
		jmsTemplate.send(destination,
				new MessageCreator() {
					@Override
					public Message createMessage(final Session session) throws JMSException {
						Message message = session.createObjectMessage(price);
						message.setStringProperty("vendorName", price.getVendor()
								.getName());
						message.setStringProperty("bbgid", price.getInstrument()
								.getBbgid());
						message.setStringProperty("cusip", price.getInstrument()
								.getCusip());
						message.setStringProperty("isin", price.getInstrument()
								.getIsin());
						message.setStringProperty("ric", price.getInstrument()
								.getRic());
						message.setStringProperty("sedol", price.getInstrument()
								.getSedol());
						message.setStringProperty("ticker", price.getInstrument()
								.getTicker());
						return message;

					}
				}
				);
	}
}
