package com.qiaoxi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qiaoxi.shopkeeper.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by shiyan on 2016/4/9.
 */
public class PaymentListView extends BaseAdapter {
    private Context context;
    private ArrayList<String> data;
    ViewHolder holder = null;
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
        holder.money.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (!arg0.equals("") && arg0 != null) {
                    double al = 0.0;
                    try{
                       al = Double.valueOf(arg0 == "" ? "0" : arg0.toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent("cn.saltyx.shiyan.paymentAdapter.MONEY_CHANGED");
                    intent.putExtra("money",al);
                    intent.putExtra("payment",getItem(position));
                    context.sendBroadcast(intent);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        return convertView;
    }
    final class ViewHolder{
        TextView payment;
        EditText money;
    }
}
