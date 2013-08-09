package com.evry.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

/**
 * @author Henrik Andersson
 */
public class MyTopicMessageListener implements MessageListener {

	private Logger logger = Logger.getLogger(getClass());

	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				logger.info("Recieved text in listener: " + ((TextMessage) message).getText());
			} catch (JMSException e) {
				logger.error("", e);
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException(" ~~~ Error in format ~~~ : " + message.toString());
		}
	}
}
