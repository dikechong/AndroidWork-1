package com.qiaoxi.bean;

import java.util.List;

public class Cart {
	private int HeadCount;
	private float Price;
	private float PriceInPoints;
	private String Invoice;
	private Desk Desk;
	private PayKind PayKind;
	private List <OrderedMenus> OrderedMenus;
	public int getHeadCount() {
		return HeadCount;
	}
	public void setHeadCount(int headCount) {
		HeadCount = headCount;
	}
	public float getPrice() {
		return Price;
	}
	public void setPrice(float price) {
		Price = price;
	}
	public float getPriceInPoints() {
		return PriceInPoints;
	}
	public void setPriceInPoints(float priceInPoints) {
		PriceInPoints = priceInPoints;
	}
	public String getInvoice() {
		return Invoice;
	}
	public void setInvoice(String invoice) {
		Invoice = invoice;
	}
	public Desk getDesk() {
		return Desk;
	}
	public void setDesk(Desk desk) {
		Desk = desk;
	}
	public PayKind getPayKind() {
		return PayKind;
	}
	public void setPayKind(PayKind payKind) {
		PayKind = payKind;
	}
	public List<OrderedMenus> getOrderedMenus() {
		return OrderedMenus;
	}
	public void setOrderedMenus(List<OrderedMenus> orderedMenus) {
		OrderedMenus = orderedMenus;
	}
	
	
}
