package com.test.hsbc.custody.persistenceservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.test.hsbc.custody.persistenceservice.entity.TransactionEntity;



public interface TransactionRepository extends CrudRepository<TransactionEntity, Integer>{

}
