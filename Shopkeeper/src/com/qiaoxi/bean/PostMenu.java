package com.qiaoxi.bean;

import java.util.List;

public class PostMenu {
	private Cart Cart;
	private CartAddition CartAddition;
	
	public PostMenu(){
		Cart = null;
		CartAddition = null;
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
