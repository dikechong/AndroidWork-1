package com.qiaoxi.shopkeeper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.util.Log;

import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by shiyan on 2016/4/10.
 */
public class CheckShift {

    private String TAG = getClass().getName();
    private Context context;
    private DatabaseHelper dbhelper;
    private Integer year;
    private Integer month;
    private Integer day;
    private String today;
    private String next_day;
    public CheckShift(Context context){
        this.context = context;
        dbhelper = new DatabaseHelper(context, 1);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month= c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        today = String.valueOf(year)+"-" + String.valueOf(month+1) + "-" + String.valueOf(day);

        day = day + 1;
        c.set(Calendar.DAY_OF_MONTH, day);
        year = c.get(Calendar.YEAR);
        month= c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        next_day  = String.valueOf(year)+"-" + String.valueOf(month+1) + "-" + String.valueOf(day);
    }
    public  Map<String, Double> getAllDineId(){
        Map<String, Double> map = new HashMap<>();

        Cursor cursor = dbhelper.query(String.format(("select sum(DinePaidDetail.Price) as price, PayKinds.Name \n" +
                "\tfrom dines ,DinePaidDetail ,payKinds \n" +
                "\t\twhere dines.id = DinePaidDetail.DineId \n" +
                "\t\tand dines.begintime between '%s' and '%s' \n" +
                "\t\tand DinePaidDetail.PayKindId = PayKinds.id\n" +
                "\t\t\tgroup by PayKinds.Name"), today, next_day));
        while (cursor.moveToNext()){
            map.put(cursor.getString(1), cursor.getDouble(0));
            Log.d(TAG, cursor.getString(0));
        }
        Log.d(TAG,String.format(("select sum(DinePaidDetail.Price) as price, PayKinds.Name \n" +
                "\tfrom dines ,DinePaidDetail ,payKinds \n" +
                "\t\twhere dines.id = DinePaidDetail.DineId \n" +
                "\t\tand dines.begintime between '%s' and '%s' \n" +
                "\t\tand DinePaidDetail.PayKindId = PayKinds.id\n" +
                "\t\t\tgroup by PayKinds.Name"), today, next_day));
        cursor.close();
        return map;

    }

}
