package com.qiaoxi.bean;

import java.util.List;

public class PaidDetails {
	private String DineId;
	private List<PaidDetail> PaidDetails;
	
	
	public String getDineId() {
		return DineId;
	}
	public void setDineId(String dineId) {
		DineId = dineId;
	}
	public List<PaidDetail> getPaidDetails() {
		return PaidDetails;
	}
	public void setPaidDetails(List<PaidDetail> paidDetails) {
		PaidDetails = paidDetails;
	}
	

}
