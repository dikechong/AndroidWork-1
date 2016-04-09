package com.qiaoxi.bean;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.app.Fragment;

/**
 * Created by shiyan on 2016/3/21.
 */
public class Global {

	public static Fragment fragment1;
	public static Fragment fragment2;
    public static Map<String,Boolean> map;
    public static Map<String,String> areamap = new HashMap<String, String>();
    public static Map<String,String> areamapback = new HashMap<String, String>();
    public static String cookie;
    public static String discount;
    public static int frag1checkedid;
    public static boolean areasaved;
    public static String ordernumber;
    public static String waiterid;
    public static String deskid;
}
