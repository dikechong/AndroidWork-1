package com.qiaoxi.fragment;


import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qiaoxi.adapter.PaymentListView;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {

    ListView check_listview;
    ArrayList<String> tmp;
    DatabaseHelper dbhelper;
    public CheckFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        check_listview = (ListView) view.findViewById(R.id.check_listview);
        tmp = new ArrayList<>();dbhelper = new DatabaseHelper(getActivity(), 1);

        Cursor cursor = dbhelper.query(DBManagerContract.PayKindsTable.TABLE_NAME,
                new String[]{DBManagerContract.PayKindsTable.COLUMN_NAME_Name},null,null,null,null,null,null);
        int tmp_index = 0;

        while(cursor.moveToNext()){
            tmp.add(tmp_index++, cursor.getString(0));
        }
        //TODO:更新FRAGMENT

        check_listview.setAdapter(new PaymentListView(getActivity(), tmp));
        return view;
    }
}
