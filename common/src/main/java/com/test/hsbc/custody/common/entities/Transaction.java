package com.test.hsbc.custody.common.entities;

import java.util.StringJoiner;

public class Transaction {
	
	private String tradeId;
	private String instrumentId;
	private String book;
	private String counterParty;	
	private String orderQuantity;	
	private String side;	
	private String executionQuantity;
	private String orderPrice;
	private String tradeDate;
	private String execPrice;
	private String fileId;
	
	
	public String getTradeId() {
		return tradeId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
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
	public String getCounterParty() {
		return counterParty;
	}
	public void setCounterParty(String counterParty) {
		this.counterParty = counterParty;
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
	public String getExecutionQuantity() {
		return executionQuantity;
	}
	public void setExecutionQuantity(String executionQuantity) {
		this.executionQuantity = executionQuantity;
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
	
	
	public String toPayload() {
		
		StringJoiner sj = new StringJoiner(",");
		sj.add(tradeId).add(instrumentId).add(book).add(counterParty).add(orderQuantity).add(side)
		.add(executionQuantity).add(orderPrice).add(tradeDate).add(execPrice).add(fileId);

		return sj.toString();

	}
	
	public static class TransactionBuilder {
		
		private String tradeId;
		private String instrumentId;
		private String book;
		private String counterParty;	
		private String orderQuantity;	
		private String side;	
		private String executionQuantity;
		private String orderPrice;
		private String tradeDate;
		private String execPrice;
		private String fileId;
		
		public TransactionBuilder setFileId(String fileId) {
			this.fileId = fileId;
			return this;
		}

		
		public TransactionBuilder setTradeId(String tradeId) {
			this.tradeId = tradeId;
			return this;
		}
		public TransactionBuilder setInstrumentId(String instrumentId) {
			this.instrumentId = instrumentId;
			return this;			
		}
		public TransactionBuilder setBook(String book) {
			this.book = book;
			return this;			
		}
		public TransactionBuilder setCounterParty(String counterParty) {
			this.counterParty = counterParty;
			return this;			
		}
		public TransactionBuilder setOrderQuantity(String orderQuantity) {
			this.orderQuantity = orderQuantity;
			return this;			
		}
		public TransactionBuilder setSide(String side) {
			this.side = side;
			return this;			
		}
		public TransactionBuilder setExecutionQuantity(String executionQuantity) {
			this.executionQuantity = executionQuantity;
			return this;			
		}
		public TransactionBuilder setOrderPrice(String orderPrice) {
			this.orderPrice = orderPrice;
			return this;			
		}
		public TransactionBuilder setTradeDate(String tradeDate) {
			this.tradeDate = tradeDate;
			return this;			
		}
		public TransactionBuilder setExecPrice(String execPrice) {
			this.execPrice = execPrice;
			return this;			
		}
		
        public Transaction build(){
        	Transaction transaction = new Transaction();
        	transaction.setBook(this.book);
        	transaction.setCounterParty(this.counterParty);       
        	transaction.setExecPrice(execPrice);
        	transaction.setExecutionQuantity(executionQuantity);
        	transaction.setInstrumentId(instrumentId);
        	transaction.setOrderPrice(orderPrice);
        	transaction.setOrderQuantity(orderQuantity);
        	transaction.setSide(side);
        	transaction.setTradeDate(tradeDate);
        	transaction.setTradeId(tradeId);
        	transaction.setFileId(fileId);
        	return transaction;
        }

		
	}

	@Override
	public String toString() {
		return "Transaction [tradeId=" + tradeId + ", instrumentId=" + instrumentId + ", book=" + book
				+ ", counterParty=" + counterParty + ", orderQuantity=" + orderQuantity + ", side=" + side
				+ ", executionQuantity=" + executionQuantity + ", orderPrice=" + orderPrice + ", tradeDate=" + tradeDate
				+ ", execPrice=" + execPrice + ", fileId=" + fileId + "]";
	}

	
	
	

}
