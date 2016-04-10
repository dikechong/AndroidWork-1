package com.qiaoxi.bean;
public class Dish {

	private String dishId;

	private String dishName;

	private String abbrevation;

	private String imageUrl;

	private String unit;

	private double price;

	private double clientPrice;

	private double serverPrice;
	public Dish(String dishId, String dishName, String abbrevation,
				 String unit, double price,double clientPrice) {
		super();
		this.dishId = dishId;
		this.dishName = dishName;
		this.abbrevation = abbrevation;
		this.imageUrl = imageUrl;
		this.unit = unit;
		this.price = price;
		this.clientPrice = clientPrice;
		this.serverPrice = serverPrice;
		this.discount = discount;
		this.printDepartment = printDepartment;
	}

	public Dish() {
		super();
		// TODO Auto-generated constructor stub
	}


	private double discount;

	private String printDepartment;
	public String getDishId() {
		return dishId;
	}
	public void setDishId(String dishId) {
		this.dishId = dishId;
	}
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public String getAbbrevation() {
		return abbrevation;
	}
	public void setAbbrevation(String abbrevation) {
		this.abbrevation = abbrevation;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getClientPrice() {
		return clientPrice;
	}
	public void setClientPrice(double clientPrice) {
		this.clientPrice = clientPrice;
	}
	public double getServerPrice() {
		return serverPrice;
	}
	public void setServerPrice(double serverPrice) {
		this.serverPrice = serverPrice;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getPrintDepartment() {
		return printDepartment;
	}
	public void setPrintDepartment(String printDepartment) {
		this.printDepartment = printDepartment;
	}

}
