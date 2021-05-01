package com.test.hsbc.custody.transactionlistner.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.test.hsbc.custody.common.entities.Transaction;

@Component
public class TransactionListener {
	
	
	 @Autowired JmsTemplate jmsTemplate;
	
	  @JmsListener(destination = "inbound.queue", containerFactory = "myFactory")
	  public void receiveMessage(Transaction transaction, MessageHeaders messageHeaders,JmsMessageHeaderAccessor jmsMessageHeaderAccessor) {
	    System.out.println("Received <" + transaction + ">");
	    // stage
	    // validation queue
	    jmsTemplate.convertAndSend("validation.queue",transaction);
	  }
	

}



