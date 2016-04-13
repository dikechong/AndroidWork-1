package com.qiaoxi.bean;

public class PaidDetail {
	int PayKindId;
	double Price;
	String RecordId;
	
	public PaidDetail(){
		PayKindId = 0;
		Price = 0;
		RecordId = null;
	}
	
	public int getPayKindId() {
		return PayKindId;
	}
	
	public void setPayKindId(int payKindId) {
		PayKindId = payKindId;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public String getRecordId() {
		return RecordId;
	}
	public void setRecordId(String recordId) {
		RecordId = recordId;
	}
	
	
}
