package com.qiaoxi.shopkeeper;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.qiaoxi.adapter.PaymentListView;
import com.qiaoxi.bean.Global;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by shiyan on 2016/4/13.
 */
public class PaymentDialog extends Dialog{

    private String TAG = getClass().getName();

    private Context context;

    private DatabaseHelper dbhelper;

    PaymentReceiver paymentReceiver;
    //控件
    private ListView payment_listview;
    private Spinner spinner;//打折方案
    private EditText discount_edit;
    private Button confirm_button;
    private TextView shouldPay, discountPay, alreadyPay, leftToPay, giveBack;

    //数据
    private Map<String, Double> payment_map, discount_map;
    private ArrayList<String> payment;
    public Double alPay;

    public PaymentDialog(Context context){
        super(context);
        this.context = context;

        dbhelper = new DatabaseHelper(context, 1);

        payment_map = new HashMap();
        discount_map = new HashMap<>();
        payment = new ArrayList<>();

        discount_map.clear();
        payment_map.clear();
        payment.clear();
        alPay = 0.0;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_dialog_layout);
        /*注册广播*/
        paymentReceiver = new PaymentReceiver();
        IntentFilter payment_filter = new IntentFilter();
        payment_filter.addAction("cn.saltyx.shiyan.paymentAdapter.MONEY_CHANGED");
        context.registerReceiver(paymentReceiver, payment_filter);

        payment_listview = (ListView) findViewById(R.id.payment_listview);
        spinner = (Spinner) findViewById(R.id.spinner_discount);
        discount_edit = (EditText) findViewById(R.id.discount);
        confirm_button = (Button) findViewById(R.id.confirm);

        shouldPay = (TextView) findViewById(R.id.should_pay); shouldPay.setText(Global.shouldPay);
        discountPay = (TextView) findViewById(R.id.discount_pay); discountPay.setText(Global.discountPay);
        alreadyPay = (TextView) findViewById(R.id.already_pay); alreadyPay.setText(Global.alreadyPay);
        leftToPay = (TextView) findViewById(R.id.left_to_pay); leftToPay.setText(Global.leftToPay);
        giveBack = (TextView) findViewById(R.id.give_back);  giveBack.setText(Global.giveBack);

        Cursor cursor = dbhelper.query(DBManagerContract.PayKindsTable.TABLE_NAME,
                new String[]{DBManagerContract.PayKindsTable.COLUMN_NAME_Name},null,null,null,null,null,null);
        int tmp_index = 0;
        while(cursor.moveToNext()){
            payment.add(tmp_index++, cursor.getString(0));
            payment_map.put(cursor.getString(0), 0.0);
        }

        payment_listview.setAdapter(new PaymentListView(context, payment));

        setDiscount(Global.discount);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空操作
                dismiss();
            }
        });

        discount_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Double d  = 100.0;
                try{
                    d = Double.valueOf(charSequence.toString());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }
                Double _t = Double.valueOf(Global.shouldPay);
                discountPay.setText(String.valueOf(_t*d / 100));
                //should_pay.setText(String.valueOf(Global.should_pay*d / 100));
                float left = Float.valueOf(discountPay.getText().toString().trim())
                        -Float.valueOf(alreadyPay.getText().toString().trim());
                float giveback = -left;

                if(giveback<0){
                    giveback = 0;
                }
                if(left<0){
                    left = 0;
                }
                leftToPay.setText(String.valueOf(left));

                giveBack.setText(String.valueOf(giveback));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void dismiss(){
        super.dismiss();
        Global.ClearDataForPayDetail();
        context.unregisterReceiver(paymentReceiver);

    }
    public void setDiscount(String discountMethod){
        try{
            /*一下均为会员打折方案
            * 以及绑定Spiner的方案*/
            JSONObject jsonObject = new JSONObject(discountMethod);
            /*discountMethod是打折方案的json文件*/

            JSONArray timeDiscount = jsonObject.getJSONArray("TimeDiscounts");
            JSONArray vipDiscount = jsonObject.getJSONArray("VipDiscounts");

            //遍历各种打折方法
            String []allDiscountMethod = new String[timeDiscount.length()+1];//去掉会员打折

            TimeDiscounts[] timeDiscountses = new TimeDiscounts[timeDiscount.length()];
            Integer index = 0;allDiscountMethod[index++] = "自定义折扣";
            for (int i=0; i<timeDiscount.length(); i++){
                //如果当前系统时间不再优惠范围内是不会收到相应信息的
                JSONObject tmpJson = timeDiscount.getJSONObject(i);
                Integer week = tmpJson.getInt("Week");
                String from  = tmpJson.getString("From");
                String to = tmpJson.getString("To");
                Double dicount = tmpJson.getDouble("Discount");
                String name = tmpJson.getString("Name");
                allDiscountMethod[index ++] = name;
                timeDiscountses[i] = new TimeDiscounts();
                timeDiscountses[i].input(from,to,week,dicount,name);
                discount_map.put(name,dicount);
            }
            for (int i =0; i<vipDiscount.length(); i++){
                JSONObject tmpJson = vipDiscount.getJSONObject(i);
                //vipDiscountses[i] = new VipDiscounts();
                Integer id = tmpJson.getInt("Id");
                Double discount = tmpJson.getDouble("Discount");
                String name =  tmpJson.getString("Name");
                //allDiscountMethod[index ++] = name;
                //vipDiscountses[i].input(id,discount,name);
                discount_map.put(name,discount);
            }
            //显示的打折方案
            //此时Spiner适配器

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item
                    , allDiscountMethod);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            /*spinner点击效果，目前是点了就显示打折数*/
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        String str = parent.getItemAtPosition(position).toString();
                        if(str.equals("自定义折扣")){
                            discount_edit.setEnabled(true);
                            discount_edit.setText("");
                        }else
                        {
                            discount_edit.setEnabled(false);
                            discount_edit.setText(String.valueOf(discount_map.get(str)*100));
                            //discount.setText(String.valueOf((1-map.get(str))*100)+"%");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*TODO: Payment*/
    class PaymentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){

            String t_payment = intent.getStringExtra("payment");
            Double t_money = intent.getDoubleExtra("money",0);
            alPay -= payment_map.get(t_payment);
            alPay += t_money;
            payment_map.put(t_payment, t_money);

            Log.d(TAG, String.valueOf(t_money));
            alreadyPay.setText(String.valueOf(alPay));
            double left = Double.valueOf(discountPay.getText().toString().trim())
                    -Double.valueOf(alreadyPay.getText().toString().trim());
            double giveback = -left;
            if(giveback<0){
                giveback = 0;
            }
            if(left<0){
                left = 0;
            }
            leftToPay.setText(String.valueOf(left));

            giveBack.setText(String.valueOf(giveback));
        }
    }
}
