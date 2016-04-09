package com.qiaoxi.bean;

public class OrderedMenus {
	private String Id;
	private int Ordered;
	private int[] Remarks;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public int getOrdered() {
		return Ordered;
	}
	public void setOrdered(int ordered) {
		Ordered = ordered;
	}
	public int[] getRemarks() {
		return Remarks;
	}
	public void setRemarks(int[] remarks) {
		Remarks = remarks;
	}
	
}
