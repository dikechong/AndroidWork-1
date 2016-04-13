package com.qiaoxi.bean;

public class Order {
	public String number;// 桌号
	public String systime;// 系统时间
	public long ordernumber;// 订单号
	public String waiterid;
	public String headcount;
	public String clerkid;
	

	public String getWaiterid() {
		return waiterid;
	}

	public void setWaiterid(String waiterid) {
		this.waiterid = waiterid;
	}

	public String getHeadcount() {
		return headcount;
	}

	public void setHeadcount(String headcount) {
		this.headcount = headcount;
	}

	public String getClerkid() {
		return clerkid;
	}

	public void setClerkid(String clerkid) {
		this.clerkid = clerkid;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSystime() {
		return systime;
	}

	public void setSystime(String systime) {
		this.systime = systime;
	}

	public long getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(long ordernumber) {
		this.ordernumber = ordernumber;
	}

}
