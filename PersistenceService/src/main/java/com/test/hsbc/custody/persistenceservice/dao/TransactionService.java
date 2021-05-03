package com.test.hsbc.custody.persistenceservice.dao;

import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.hsbc.custody.persistenceservice.entity.TransactionEntity;
import com.test.hsbc.custody.persistenceservice.repository.TransactionRepository;
import com.test.hsbc.custody.persistenceservice.repository.TransactionSearchRepository;

@Service
public class TransactionService {
	
	@Autowired private TransactionRepository transactionRepository;
	@Autowired private TransactionSearchRepository searchRepository;
	
	public void saveOrUpdate(TransactionEntity transaction)  {  
		transactionRepository.save(transaction);  
	}
	
	public long findByFileId(String fileId) {
	
		 Iterable<TransactionEntity> iterable =  transactionRepository.findAll();
		 return StreamSupport.stream(iterable.spliterator(), false)
		.filter( t -> fileId.equalsIgnoreCase( t.getFileId()))
		.count();
	}

	public TransactionEntity getTransaction(String fileId,String tradeId) {
		
		 Iterable<TransactionEntity> iterable =  transactionRepository.findAll();
		 return StreamSupport.stream(iterable.spliterator(), false)
		.filter( t -> fileId.equalsIgnoreCase( t.getFileId()) && tradeId.equalsIgnoreCase(t.getTradeId() ))
		.findAny()
		.orElse(null);

	}
	
	public TransactionEntity getTransactionNativeQuery(String fileId,String tradeId) {
		 return  searchRepository.getTransactionEntity(fileId, tradeId);
	}	

	
}
