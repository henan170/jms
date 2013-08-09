package com.evry.jms.jndi;

import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class JndiServiceImpl implements JndiService {
	
	private Logger logger = Logger.getLogger(getClass());
	
	private Context jndiContext;
	
	public JndiServiceImpl() {
		try {
			jndiContext = new InitialContext();
		} catch (NamingException e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	public TopicConnectionFactory getTopicConnectionFactory() {
		try {
			return (TopicConnectionFactory) jndiContext.lookup("topicConnectionFactory");
		} catch (NamingException e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	public Topic getTopic() {
		try {
			return (Topic) jndiContext.lookup("sample.data");
		} catch (NamingException e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}
}
