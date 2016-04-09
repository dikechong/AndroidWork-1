package com.qiaoxi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.qiaoxi.shopkeeper.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by shiyan on 2016/4/9.
 */
public class PaymentListView extends BaseAdapter {
    private Context context;
    private ArrayList<String> data;
    public PaymentListView(Context context,ArrayList<String>data){
        this.context = context;
        this.data = data;
    }
    @Override
    public String getItem(int postion){
        return data.get(postion);
    }
    @Override
    public int getCount(){
        return data.size();
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup){
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.payment_listview_item, null);
            holder.payment = (TextView) convertView.findViewById(R.id.payment);

            holder.money = (EditText) convertView.findViewById(R.id.money);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.payment.setText(getItem(position));
        return convertView;
    }
    final class ViewHolder{
        TextView payment;
        EditText money;
    }
}
