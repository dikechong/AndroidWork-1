package com.qiaoxi.shopkeeper;

/**
 * Created by shiyan on 2016/3/20.
 */
public class VipDiscounts {

    public Integer Id;
    public Double Discount;
    public String Name;

    public void input(Integer id, Double discount, String name){
        this.Id = id;
        this.Discount = discount;
        this.Name = name;
    }
}
