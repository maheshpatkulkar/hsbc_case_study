package com.test.hsbc.custody.persistenceservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.hsbc.custody.common.entities.Transaction;
import com.test.hsbc.custody.persistenceservice.dao.TransactionService;
import com.test.hsbc.custody.persistenceservice.entity.TransactionEntity;

@RestController
@RequestMapping("/app")
public class PersistenceServiceLayerController {
	
	@Autowired private TransactionService service;
	
	@PostMapping(value="/transaction/", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> insert (@RequestBody Transaction trasTransaction,
			@RequestHeader(value="fileId") String fileId,@RequestHeader(value="status") String status) {
		
		TransactionEntity trasnEntity = new TransactionEntity();
		trasnEntity.setBook(trasTransaction.getBook());
		trasnEntity.setCounterparty(trasTransaction.getCounterParty());
		trasnEntity.setExecPrice(trasTransaction.getExecPrice());
		trasnEntity.setExecQuantity(trasTransaction.getExecutionQuantity());
		trasnEntity.setFileId(fileId);
		trasnEntity.setInstrumentId(trasTransaction.getInstrumentId());
		trasnEntity.setOrderPrice(trasTransaction.getOrderPrice());
		trasnEntity.setOrderQuantity(trasTransaction.getOrderQuantity());
		trasnEntity.setSide(trasTransaction.getSide());
		trasnEntity.setStatus(status);
		trasnEntity.setTradeDate(trasTransaction.getTradeDate());
		trasnEntity.setTradeId(trasTransaction.getTradeId());
		
		service.saveOrUpdate(trasnEntity);
		
		return new ResponseEntity<>("Success",HttpStatus.OK);
		
	}
	
	@PutMapping(value="/transaction/", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> update (@RequestBody Transaction trasTransaction,
			@RequestHeader(value="fileId") String fileId,@RequestHeader(value="status") String status) {
		
		// TransactionEntity trasnEntity = service.getTransaction(fileId, trasTransaction.getTradeId());
		TransactionEntity trasnEntity = service.getTransactionNativeQuery(fileId, trasTransaction.getTradeId());		
		if (trasnEntity != null) {
			trasnEntity.setStatus(status);
			service.saveOrUpdate(trasnEntity);
		} 
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
	

	@GetMapping(value="transaction", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getAll (@RequestBody Transaction trasTransaction) {
		
		
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}

	@GetMapping(value="transaction/{fileid}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> transactionCount (@RequestBody Transaction trasTransaction) {
		
		
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
	
	  @GetMapping("/hello/")
	  public String getHelloWordObject() {
	      return "Welcome";
	  }
	

}
