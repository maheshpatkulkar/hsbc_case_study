package com.test.hsbc.custody.persistenceservice.repository;

import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.test.hsbc.custody.persistenceservice.entity.TransactionEntity;

@Repository
public class TransactionSearchRepositoryImpl implements TransactionSearchRepository {

	 @PersistenceContext
	 private EntityManager entityManager;
	 
	@Override
	public Stream<TransactionEntity> getTransactionEntitys(String fileId) {
		
		//entityManager.na
		Query namedQuery = entityManager.createNamedQuery("TransactionEntity.findByFileId");
	    namedQuery.setParameter("fileid", fileId);
	    return  namedQuery.getResultStream();		
	}

	@Override
	public TransactionEntity getTransactionEntity(String fileId, String tradeid) {
		Query namedQuery = entityManager.createNamedQuery("TransactionEntity.findByFileIdTradeId");
	    namedQuery.setParameter("fileid", fileId);
	    namedQuery.setParameter("tradeid", tradeid);	    
	    return  (TransactionEntity) namedQuery.getSingleResult();		
	}

}
