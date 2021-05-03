package com.test.hsbc.custody.persistenceservice.repository;

import java.util.stream.Stream;

import com.test.hsbc.custody.persistenceservice.entity.TransactionEntity;

public interface TransactionSearchRepository {

	public TransactionEntity getTransactionEntity(String fileId,String tradeid);
	public Stream<TransactionEntity> getTransactionEntitys(String fileId);
	
}
