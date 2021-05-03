package com.test.hsbc.custody.transactionlistner.listener;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class TransactionListener {


	private static final String VALIDATION_QUEUE = "validation.queue";
	private static final String ERROR_QUEUE = "error.queue";
	@Autowired private JmsTemplate jmsTemplate;
	@Autowired private RestTemplate restTemplate;
	private ObjectMapper objectMapper = new ObjectMapper();
	static Logger logger = LoggerFactory.getLogger(TransactionListener.class);



	@JmsListener(destination = "inbound.queue", containerFactory = "myFactory")
	public void receiveMessage(Transaction transaction, MessageHeaders messageHeaders,JmsMessageHeaderAccessor jmsMessageHeaderAccessor) {
		
		String fileId = (String) messageHeaders.get("fileId");
		String fileName = (String) messageHeaders.get("CamelFileNameConsumed");
		transaction.setFileId(fileId);

		logger.info("Message Receieved");

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

			HttpEntity<String> request = getPostHttpEntity(fileId,"RECEIVED",fileName,jsonTransactionString);
			ResponseEntity<String> response =
					restTemplate.postForEntity("http://persistenceservice/app/transaction/", request, String.class);
			logger.info("Message persisted");
			if (!response.getBody().equalsIgnoreCase("\"Success\"" )) {
				sendMessage(ERROR_QUEUE,transaction,messageHeaders);
				logger.info("Send to Error queue");				
				return ;
			}

			sendMessage(VALIDATION_QUEUE,transaction,messageHeaders);
		} catch (Exception ex) {
			HttpEntity<String> request = getPutHttpEntity(fileId,"FAILED",jsonTransactionString);
			restTemplate.put("http://persistenceservice/app/transaction/", request);
			logger.error("Message failed",ex);

		}
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

	private HttpEntity<String> getPostHttpEntity (String fileId, String status,String fileName,String jsonString) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.add("fileId", fileId);
		headers.add("status", status);
		headers.add("fileName", fileName);
		

		return new HttpEntity<String>(jsonString, headers);
	}

	private HttpEntity<String> getPutHttpEntity (String fileId, String status,String jsonString) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.add("fileId", fileId);
		headers.add("status", status);

		return new HttpEntity<String>(jsonString, headers);
	}	
	
	private String getJsonString(Transaction transaction) throws JsonProcessingException {
		return objectMapper.writeValueAsString(transaction);
	}



}



