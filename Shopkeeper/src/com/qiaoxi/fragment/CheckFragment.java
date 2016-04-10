package com.qiaoxi.fragment;


import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.qiaoxi.adapter.PaymentListView;
import com.qiaoxi.bean.Global;
import com.qiaoxi.shopkeeper.CheckShift;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SocketHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {

    ListView check_listview;
    ArrayList<String> tmp;
    DatabaseHelper dbhelper;
    CheckShift checkShift;
    Map<String, Double> map, check_map;
    Button button_check;
    PaymentReceiver_Check paymentReceiver_check;
    public CheckFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        paymentReceiver_check = new PaymentReceiver_Check();
        IntentFilter payment_filter = new IntentFilter();
        payment_filter.addAction("cn.saltyx.shiyan.paymentAdapter.MONEY_CHANGED");
        getActivity().registerReceiver(paymentReceiver_check, payment_filter);

        View view = inflater.inflate(R.layout.fragment_check, container, false);
        check_listview = (ListView) view.findViewById(R.id.check_listview);
        button_check = (Button) view.findViewById(R.id.shift);
        tmp = new ArrayList<>();dbhelper = new DatabaseHelper(getActivity(), 1);

        Cursor cursor = dbhelper.query(DBManagerContract.PayKindsTable.TABLE_NAME,
                new String[]{DBManagerContract.PayKindsTable.COLUMN_NAME_Name},null,null,null,null,null,null);
        int tmp_index = 0;

        while(cursor.moveToNext()){
            tmp.add(tmp_index++, cursor.getString(0));
        }
        //TODO:更新FRAGMENT
        checkShift = new CheckShift(getActivity());
        map = checkShift.getAllDineId();
        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (map.size() == 0){
                    Toast.makeText(getActivity(), "无订单！交接班成功", Toast.LENGTH_SHORT).show();
                }else{
                    if (map.equals(check_map)){
                        Toast.makeText(getActivity(), "交接班成功",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getActivity(), "交接班失败",Toast.LENGTH_SHORT).show();
                }
            }
        });



        check_map = new HashMap<>();

        check_listview.setAdapter(new PaymentListView(getActivity(), tmp));

        return view;
    }
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(paymentReceiver_check);
    }
    /*TODO: Payment*/
    class PaymentReceiver_Check extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){

            String t_payment = intent.getStringExtra("payment");
            Double t_money = intent.getDoubleExtra("money",0);
            check_map.put(t_payment, t_money);
        }
    }
}
