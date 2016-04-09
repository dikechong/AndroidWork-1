package com.qiaoxi.shopkeeper;

/**
 * Created by shiyan on 2016/3/20.
 */
public class TimeDiscounts {

    public String From;
    public String To;
    public Integer Week;
    public Double Discount;
    public String Name;

    public void input(String from, String to, Integer week, Double discount,String name){
        this.From = from;
        this.To = to;
        this.Week = week;
        this.Discount = discount;
        this.Name = name;
    }
}
