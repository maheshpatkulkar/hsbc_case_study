package com.test.hsbc.custody.DownStreamRoutingService.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.test.hsbc.custody.common.entities.Transaction;

@Component
public class DownStreamRoutingListner {

	 @Autowired JmsTemplate jmsTemplate;
		
	  @JmsListener(destination = "inbound.queue", containerFactory = "myFactory")
	  public void receiveMessage(Transaction transaction, MessageHeaders messageHeaders,JmsMessageHeaderAccessor jmsMessageHeaderAccessor) {
	    System.out.println("Received <" + transaction + ">");
	    // stage
	    // validation queue
	    jmsTemplate.convertAndSend("outbound.queue",transaction);
	  }

	
}
