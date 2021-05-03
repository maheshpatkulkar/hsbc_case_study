package com.test.hsbc.custody.persistenceservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSACTION_TABLE" )
@NamedQuery(name = "TransactionEntity.findByFileId", query = "SELECT t FROM TransactionEntity t WHERE t.fileId=:fileid")
@NamedQuery(name = "TransactionEntity.findByFileIdTradeId", query = "SELECT t FROM TransactionEntity t WHERE t.fileId=:fileid and t.tradeId=:tradeid")

public class TransactionEntity {

 	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
 	
 	@Column (name = "file_id")
 	private String fileId;
 	
 	@Column (name = "status")
 	private String status;
 	
 	@Column (name = "trade_id")
 	private String tradeId; 	
 	
 	@Column (name = "instrument_id") 	
 	private String instrumentId;
 	
 	@Column (name = "book") 	
 	private String book;

 	@Column (name = "counterparty") 	
 	private String counterparty;
 	
 	@Column (name = "order_quantity") 	
 	private String orderQuantity;
 	
 	@Column (name = "side") 	
 	private String side;

 	@Column (name = "exec_quantity") 	
 	private String execQuantity;
 	
 	@Column (name = "order_price") 	
 	private String orderPrice;
 	
 	@Column (name = "trade_date") 	
 	private String tradeDate;

 	@Column (name = "exec_price") 	
 	private String execPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getCounterparty() {
		return counterparty;
	}

	public void setCounterparty(String counterparty) {
		this.counterparty = counterparty;
	}

	public String getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(String orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getExecQuantity() {
		return execQuantity;
	}

	public void setExecQuantity(String execQuantity) {
		this.execQuantity = execQuantity;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getExecPrice() {
		return execPrice;
	}

	public void setExecPrice(String execPrice) {
		this.execPrice = execPrice;
	}

	@Override
	public String toString() {
		return "TransactionEntity [id=" + id + ", fileId=" + fileId + ", status=" + status + ", tradeId=" + tradeId
				+ ", instrumentId=" + instrumentId + ", book=" + book + ", counterparty=" + counterparty
				+ ", orderQuantity=" + orderQuantity + ", side=" + side + ", execQuantity=" + execQuantity
				+ ", orderPrice=" + orderPrice + ", tradeDate=" + tradeDate + ", execPrice=" + execPrice + "]";
	}

 	
}




