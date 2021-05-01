package com.test.hsbc.custody.transactionlistner.messageconvertor;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import com.test.hsbc.custody.common.entities.Transaction;

@Component
public class TransactionMessageConvertor implements MessageConverter {

    static Logger logger = LoggerFactory.getLogger(TransactionMessageConvertor.class);


	  @Override
	  public Message toMessage(Object object, Session session)
	      throws JMSException {
		Transaction transaction = (Transaction) object;
	    String payload = transaction.toPayload();

	    TextMessage message = session.createTextMessage();
	    message.setText(payload);
	    return message;
	  }

	  @Override
	  public Object fromMessage(Message message) throws JMSException {
	    TextMessage textMessage = (TextMessage) message;
	    String payload = textMessage.getText();

	    Transaction transaction = null;
	    try {
	    	String[] payloadArray = payload.split(",");
	    	Transaction.TransactionBuilder builder = new Transaction.TransactionBuilder();
	    	builder.setTradeId(payloadArray[0]).setInstrumentId(payloadArray[1]).setBook(payloadArray[2])
	    	.setCounterParty(payloadArray[3]).setOrderQuantity(payloadArray[4]).setSide(payloadArray[5])
	    	.setExecutionQuantity(payloadArray[6]).setOrderPrice(payloadArray[7]).setTradeDate(payloadArray[8])
	    	.setExecPrice(payloadArray[9]);
	    	
	    	transaction = builder.build();
	    	
	    } catch (Exception e) {
	    	logger.error("error converting to transaction", e);
	    }
	    return transaction;
	  }
}



