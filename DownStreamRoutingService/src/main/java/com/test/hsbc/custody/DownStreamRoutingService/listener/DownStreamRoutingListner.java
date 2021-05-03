package com.test.hsbc.custody.DownStreamRoutingService.listener;


import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hsbc.custody.common.entities.Transaction;

@Component
public class DownStreamRoutingListner {

	@Autowired JmsTemplate jmsTemplate;
	ObjectMapper objectMapper = new ObjectMapper();
	private static final String ERROR_QUEUE = "error.queue";
	private static Map<String,String> downStreamRoutingRule ; 
	@Autowired RestTemplate restTemplate;

	
	@JmsListener(destination = "routing.queue", containerFactory = "myFactory")
	public void receiveMessage(Transaction transaction, MessageHeaders messageHeaders,JmsMessageHeaderAccessor jmsMessageHeaderAccessor) {
		
		String fileId = (String) messageHeaders.get("fileId");
		transaction.setFileId(fileId);
		String jsonTransactionString = null;		
		
		try {
			jsonTransactionString = getJsonString(transaction) ;
		} catch (Exception ex) {
			sendMessage(ERROR_QUEUE,transaction,messageHeaders);
			return ;
		}		  
		
		String downStreamQueue = getDownStreamQueue(transaction);
		try {
			if (downStreamQueue != null) {
				HttpEntity<String> request = getHttpEntity(fileId,"COMPLETED",jsonTransactionString);
				restTemplate.put("http://persistenceservice/app/transaction/", request);
				sendMessage(downStreamQueue,transaction,messageHeaders);
			} else {
				HttpEntity<String> request = getHttpEntity(fileId,"FAILED",jsonTransactionString);
				restTemplate.put("http://persistenceservice/app/transaction/", request);
				sendMessage(ERROR_QUEUE,transaction,messageHeaders);			
			} 
		} catch (Exception ex) {
			HttpEntity<String> request = getHttpEntity(fileId,"FAILED",jsonTransactionString);
			restTemplate.put("http://persistenceservice/app/transaction/", request);
			sendMessage(ERROR_QUEUE,transaction,messageHeaders);			
		}
	}
	
	private String getJsonString(Transaction transaction) throws JsonProcessingException {
		return objectMapper.writeValueAsString(transaction);
	}	  
	
	private static String getDownStreamQueue(Transaction transaction) {
		
		
		if (downStreamRoutingRule == null) {
			downStreamRoutingRule = new HashMap<>();
			downStreamRoutingRule.put("10001","outbound.queue.1");
			downStreamRoutingRule.put("10002","outbound.queue.2");			  
			downStreamRoutingRule.put("10003","outbound.queue.3");			  
		}
		return downStreamRoutingRule.get(transaction.getInstrumentId());
	}
	
	private HttpEntity<String> getHttpEntity (String fileId, String status,String jsonString) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.add("fileId", fileId);
		headers.add("status", status);

		return new HttpEntity<String>(jsonString, headers);
	}


	private void sendMessage (String queueName, Transaction transaction,MessageHeaders messageHeaders) {

		jmsTemplate.convertAndSend(queueName,transaction, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				messageHeaders.entrySet().stream().
				forEach(e -> {
					try {
						message.setStringProperty( e.getKey(),e.getValue().toString());
					} catch (JMSException e1) {
					}
				});
				return message;
			}
		});
	}	  
	
	


}
