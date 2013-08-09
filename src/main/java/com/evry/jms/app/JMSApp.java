package com.evry.jms.app;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.log4j.Logger;

import com.evry.jms.jndi.JndiService;
import com.evry.jms.jndi.JndiServiceImpl;
import com.evry.jms.listener.MyTopicMessageListener;

/**
 * @author Henrik Andersson
 */
public class JMSApp implements Runnable {

	private Logger logger = Logger.getLogger(getClass());

	public static void main(String[] args) {
		new JMSApp().run();
	}

	public void run() {
		JndiService jndiService = new JndiServiceImpl();
		TopicConnectionFactory topicConnectionFactory = jndiService.getTopicConnectionFactory();
		Topic topic = jndiService.getTopic();

		TopicConnection consumerConnection = null;
		TopicConnection producerConnection = null;
		try {
			consumerConnection = topicConnectionFactory.createTopicConnection();
			producerConnection = topicConnectionFactory.createTopicConnection();
			createConsumer(consumerConnection, topic);
			sendMessages(producerConnection, topic);
			waitForNumberOfSecounds(3);
		} catch (JMSException e) {
			logger.error("", e);
		} finally {
			if (consumerConnection != null) {
				try {
					consumerConnection.close();
				} catch (JMSException e) {
					logger.error("", e);
				}
			}
			if (producerConnection != null) {
				try {
					producerConnection.close();
				} catch (JMSException e) {
					logger.error("", e);
				}
			}
		}
	}

	private void createConsumer(TopicConnection consumerConnection, Topic topic) throws JMSException {
		TopicSession consumerSession = consumerConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		TopicSubscriber consumer = consumerSession.createSubscriber(topic);
		consumerConnection.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			logger.error("", e);
			Thread.currentThread().interrupt();
		}
		MessageListener messageListener = new MyTopicMessageListener();
		consumer.setMessageListener(messageListener);

	}

	private void sendMessages(TopicConnection producerConnection, Topic topic) throws JMSException {
		Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer messageProducer = producerSession.createProducer(topic);
		TextMessage textMessage = producerSession.createTextMessage();
		for (int i = 0; i < 2; i++) {
			textMessage.setText("This is message from JMSSECOND DEMO " + (i + 1));
			logger.info("Sending message: " + textMessage.getText());
			messageProducer.send(textMessage);
		}
	}

	private void waitForNumberOfSecounds(int numberOfSecounds) {
		try {
			Thread.sleep(numberOfSecounds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}