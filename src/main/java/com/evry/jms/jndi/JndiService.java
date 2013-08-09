package com.evry.jms.jndi;

import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

public interface JndiService {
	
	TopicConnectionFactory getTopicConnectionFactory();
	
	Topic getTopic();

}
