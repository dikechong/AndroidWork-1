package com.qiaoxi.bean;

import java.util.List;

public class PostMenuWithPay {
	private Cart Cart;
	private CartAddition CartAddition;
	private List <PaidDetail> PaidDetails;
	
	public PostMenuWithPay(){
		Cart = null;
		CartAddition = null;
		PaidDetails = null;
	}
	

	public List<PaidDetail> getPaidDetails() {
		return PaidDetails;
	}


	public void setPaidDetails(List<PaidDetail> paidDetails) {
		PaidDetails = paidDetails;
	}


	public Cart getCart() {
		return Cart;
	}
	public void setCart(Cart cart) {
		Cart = cart;
	}
	public CartAddition getCartAddition() {
		return CartAddition;
	}
	public void setCartAddition(CartAddition cartAddition) {
		CartAddition = cartAddition;
	}
	
	
}
