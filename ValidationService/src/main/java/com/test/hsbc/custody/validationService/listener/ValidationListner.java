package com.test.hsbc.custody.validationService.listener;

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
public class ValidationListner {

	@Autowired JmsTemplate jmsTemplate;
	private static Map<String,String> instrument ; 
	private static final String ERROR_QUEUE = "error.queue";
	@Autowired RestTemplate restTemplate;
	ObjectMapper objectMapper = new ObjectMapper();

	@JmsListener(destination = "validation.queue", containerFactory = "myFactory")
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

		if ( messageHeaders.get("jms_redelivered") != null ) {
			boolean reDeliverrd = Boolean.parseBoolean(messageHeaders.get("jms_redelivered").toString());
			if (reDeliverrd && messageHeaders.get("JMSXDeliveryCount")  != null ) {
				int redeliveryCount = Integer.parseInt(messageHeaders.get("JMSXDeliveryCount").toString());
				if (redeliveryCount > 3) {
					sendMessage(ERROR_QUEUE,transaction,messageHeaders);
					return ;
				}
			}
		}		
		
		try {
			if (validateInstrument(transaction.getInstrumentId())) {
				HttpEntity<String> request = getHttpEntity(fileId,"VALIDATED",jsonTransactionString);
				restTemplate.put("http://persistenceservice/app/transaction/", request);
				sendMessage("routing.queue",transaction,messageHeaders);
			} else  {
				HttpEntity<String> request = getHttpEntity(fileId,"FAILED",jsonTransactionString);	
				restTemplate.put("http://persistenceservice/app/transaction/", request);
				sendMessage(ERROR_QUEUE,transaction,messageHeaders);
			} 
		} catch ( Exception ex) {
			try {
				HttpEntity<String> request = getHttpEntity(fileId,"FAILED",jsonTransactionString);	
				restTemplate.put("http://persistenceservice/app/transaction/", request);
			} catch (Exception e) {
			}
			sendMessage(ERROR_QUEUE,transaction,messageHeaders);
		}

	}


	private static boolean validateInstrument(String instrumentId) {

		if (instrument == null) {
			instrument = new HashMap<>();
			instrument.put("10001","AMAZON");
			instrument.put("10002","MS");			  
			instrument.put("10003","APPLE");			  
			instrument.put("10004","FACEBOOK");			  

		}
		return instrument.containsKey(instrumentId);
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


	private String getJsonString(Transaction transaction) throws JsonProcessingException {
		return objectMapper.writeValueAsString(transaction);
	}	  

}
