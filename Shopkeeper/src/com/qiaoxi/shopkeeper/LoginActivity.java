package com.qiaoxi.shopkeeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qiaoxi.bean.Global;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



@SuppressLint("NewApi") public class LoginActivity extends Activity {
    EditText editText;
    EditText editText1;
    Button button;
    String TAG = getClass().getName();
    String cookies;
    String s1,s2;
    public SQLiteDatabase db;

    Map<String, List<String>> maps;
    List<String> cookieslist;

    Boolean flags_getdiscountMethods;
    JSONObject discountMethods = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(this);
        db = openOrCreateDatabase("Infors", MODE_PRIVATE, null);
//      db.execSQL("create table if not exists localmenutb(_id integer primary key autoincrement,DeskId text,num text,price double,name text,ordered text,notes text)");
//		db.execSQL("drop table localmenutb");
//		db.execSQL("create table if not exists localmenutb(_id integer primary key autoincrement,DeskId text,num text,price double,name text,ordered text,notes text)");

        editText = (EditText)findViewById(R.id.editText);
        editText1 = (EditText)findViewById(R.id.editText1);
        button = (Button)findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = editText.getText().toString();
                //登录名
                s2 = editText1.getText().toString();
                //密码

                final JSONObject jsonObject = new JSONObject();
                try {
                    //添加登录json信息
                    jsonObject.put("SigninName", s1);
                    jsonObject.put("Password", s2);
                } catch (Exception e) {
                    Log.d(TAG, "76 "+e.toString());
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        flags_getdiscountMethods  = false;//当前线程还没获取数据不允许继续下去等待另一线程结束

                        PrintWriter out = null;
                        BufferedReader in = null;
                        String result = "";
                        //设置cookie
                        CookieManager cookieManager = new CookieManager();
                        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                        CookieHandler.setDefault(cookieManager);
                        try{

                            URL realUrl = new URL("http://ordersystem.yummyonline.net/Waiter/Account/Signin");
                            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            //POST请求头属性，
                            conn.setRequestProperty("Content-Type", "application/json");
                            //设置POST
                            conn.setRequestMethod("POST");
                            out = new PrintWriter(conn.getOutputStream());
                            // 发送请求参数
                            out.print(new String(jsonObject.toString().getBytes(), "UTF-8"));
                            out.flush();
                            Iterator<String> it = null;
                            StringBuffer sbu = null;

                            in = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                            String line = "";
                            while ((line = in.readLine()) != null) {
                                result += "\n" + line;
                            }

                            JSONObject jsonObject1 = new JSONObject(result);
                            Boolean s = (Boolean) jsonObject1.get("Succeeded");

                            //获取cookies
                            maps = conn.getHeaderFields();
                            cookieslist = maps.get("Set-Cookie");
                            it = cookieslist.iterator();
                            sbu = new StringBuffer();

                            while (it.hasNext()) {
                                sbu.append(it.next());
                            }
                            cookies = sbu.toString();
                            /***************************/

                            if (s.equals(true)) {
                                //TODO:注意查看！！
                                //登录后做的事情
                                //目前是到下个ACTIVITY
                                //按自己的业务要求修改
                                /*README
                                    * 本线程用于根据cookie获取饭店信息
                                    * 其中不得修改的部分是
                                    * 1.一定要获取从登陆界面得到的user以及cookie，它们将用于通过cookie获取饭店基本信息
                                    * 2.一定要修改存储路径，目前默认为/sdcard/Android/data/项目包名/xxxAllMsg.json，其中项目包名请根据名称修改
                                    * */
                                String cookiesValues = cookies.substring(0, cookies.indexOf(';'));
                                String cookiesValues1 = cookies.substring(cookies.indexOf("ASPXAUTH"));
                                cookiesValues1 = cookiesValues1.substring(0, cookiesValues1.indexOf(';'));
                                //cookies
                                // 由asp.net_sessionid + aspxauth构成
                                // asp.net_sessionid=xxxxxxx;aspxauth=xxxxxx
                                cookies = cookiesValues + ";" + cookiesValues1;

                                /*
                                *
                                * 下面的线程是用来cookie获取所有信息
                                *
                                * */
//                                try{
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            PrintWriter out = null;
//                                            BufferedReader in = null;
//                                            try {
//                                                URL realUrl = new URL("http://ordersystem.yummyonline.net/Waiter/Order/GetMenuInfos");
//                                                HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
//                                                //POST请求头属性
//                                                //conn.setRequestProperty("Content-Type", "application/json");
//                                                conn.setRequestProperty("Cookie", cookies);
//                                                conn.setRequestMethod("POST");
//                                                out = new PrintWriter(conn.getOutputStream());
//                                            /*
//                                            * 下面是发送的信息*/
//                                                /*************/
//                                                // 发送请求参数
//                                                out.flush();
//                                                in = new BufferedReader(
//                                                        new InputStreamReader(conn.getInputStream()));
//                                                String lines = "";
//                                                String result_Msg = "";
//                                                while ((lines = in.readLine()) != null) {
//                                                    result_Msg += "\n" + lines;
//                                                }
//                                                System.out
//														.println("results为："+result_Msg);
//                                                handleAllJsonMsg(result_Msg);
//                                                discountMethods = (new JSONObject(result_Msg)).getJSONObject("DiscountMethods");
//                                                flags_getdiscountMethods = true;
//                                                //cookie线程已经获取数据可以继续下去
//                                            } catch (Exception e) {
//                                                Log.d(TAG, "177 "+e.toString());
//                                                new ToastMessageTask().execute("cookie "+e.toString());
//                                            } finally {
//                                                try {
//                                                    if (out != null) {
//                                                        out.close();
//                                                    }
//                                                    if (in != null) {
//                                                        in.close();
//                                                    }
//                                                } catch (IOException ex) {
//                                                    new ToastMessageTask().execute(ex.toString());
//                                                }
//                                            }
//                                        }
//                                    }).start();
//                                }catch (Exception e){
//                                    new ToastMessageTask().execute(e.toString());
//                                }

                                //while (!flags_getdiscountMethods);//阻塞当前线程等待另一线程结束
                                //以下为自己的逻辑
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("cookie", cookies);
                                Global.waiterid = s1;
                                Global.areasaved = false;
                                Global.inforssaved = false;
//                                intent.putExtra("user", s1);
//
//                                intent.putExtra("discount", discountMethods.toString());
                                //到主界面的时候 可以带上折扣方案
                                // TimeDiscount
                                // VipDiscount
                                startActivity(intent);
                            } else {
                                //登录失败
                                new ToastMessageTask().execute("帐号密码错误！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            new ToastMessageTask().execute("登录失败！");
                        } finally {
                            //关闭输入输出流
                            try {
                                if (out != null) {
                                    out.close();
                                }
                                if (in != null) {
                                    in.close();
                                }
                            } catch (IOException ex) {
                                new ToastMessageTask().execute("223 "+ex.toString());
                            }
                        }
                    }
                }).start();
            }
        });
    }


    //改用json实现
//    private void storeJsonForAllMsg(String json) {
//        try{
//            //此处文件夹名称 /sdcard/Android/data/项目包名/
//            File dir = new File("/sdcard/Android/data/cn.saltyx.shiyan.myapplication/");
//            if (!dir.exists()){
//                //如果文件夹不存在
//                dir.mkdirs();
//            }
//            //此处文件路径名称 /sdcard/Android/data/项目包名/allMsg.json
//            File file = new File("/sdcard/Android/data//cn.saltyx.shiyan.myapplication/"+s1+"allMsg.json");
//            if (file.exists()){
//                file.delete();
//            }
//            file.createNewFile();
//            if (json!=null ) {
//                //如果json数据为空就不修改原来的文件
//                FileWriter fileWriter = new FileWriter(file, true);
//                BufferedWriter out = new BufferedWriter(fileWriter);
//                out.write(json);
//                out.close();
//            }
//        }catch(Exception e){
//            new ToastMessageTask().execute(e.toString());
//        }
//    }

    private void getDineInfoFromDineId(final String dineId,final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter out = null;
                BufferedReader in = null;
                new ToastMessageTask().execute(cookies);
                try {
                    URL realUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                    //POST请求头属性
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Cookie", cookies);
                    conn.setRequestMethod("POST");
                    out = new PrintWriter(conn.getOutputStream());

                    /*
                    * 下面是发送的信息*/
                    out.print(dineId);
                    /*************/
                    // 发送请求参数
                    out.flush();
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String lines = "";
                    String result_Msg ="";
                    while ((lines = in.readLine()) != null) {
                        result_Msg += "\n" + lines;
                    }
                    handleDine(result_Msg);
                } catch (Exception e) {
                    Log.d(getClass().getName(), e.toString());
                    new ToastMessageTask().execute(e.toString());
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        new ToastMessageTask().execute(ex.toString());
                    }
                }
            }
        }).start();
    }

    private void handleAllJsonMsg(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);


        //TODO:此处需要判断是否是通过离线登陆进来的
        //TODO:如果是离线登录不要执行if里面的操作！！！！！！！！
        if (true) {
            //删除数据库以便新建个更新本地数据库
            SQLiteDatabase.deleteDatabase(new File("/data/data/com.qiaoxi.shopkeeper/databases/DB_Manager"));
        }
        //TODO:===========================================
        DatabaseHelper dbhelper = new DatabaseHelper(LoginActivity.this, 1);
        //
        JSONArray jsonArray = jsonObject.getJSONArray("Desks");
        handleDesks(dbhelper, jsonArray);
        //hotels
        JSONObject jsonObject_hotels = jsonObject.getJSONObject("Hotel");
        handleHotel(dbhelper, jsonObject_hotels);
        //menus
        JSONArray arrayMenu = jsonObject.getJSONArray("Menus");
        handleMenus(dbhelper, arrayMenu);
        //paykind
        try {
            JSONArray arrayPaykind = jsonObject.getJSONArray("PayKind");
            handlePayKind(dbhelper, arrayPaykind);
        } catch (Exception e) {
            JSONObject arrayPaykind = jsonObject.getJSONObject("PayKind");
            handlePayKind(dbhelper, arrayPaykind);
        }
    }

    private void handlePayKind(DatabaseHelper dbhelper, JSONArray jsonArray) throws Exception{
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject paykinds = (JSONObject)jsonArray.get(i);
            ContentValues values = new ContentValues();
            values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Id, paykinds.getInt("Id"));
            values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Name, paykinds.getString("Name"));
            values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Description, paykinds.getString("Description"));
            values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Discount,paykinds.getDouble("Discount"));
            dbhelper.insert(DBManagerContract.PayKindsTable.TABLE_NAME, values);
        }
    }
    private void handlePayKind(DatabaseHelper dbhelper, JSONObject jsonObject) throws Exception{
        ContentValues values = new ContentValues();
        values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Id, jsonObject.getInt("Id"));
        values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Name, jsonObject.getString("Name"));
        values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Description, jsonObject.getString("Description"));
        values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Discount,jsonObject.getDouble("Discount"));
        dbhelper.insert(DBManagerContract.PayKindsTable.TABLE_NAME, values);
    }
    private void handleMenus(DatabaseHelper dbhelper, JSONArray jsonArray){
/* 格式
  "Menus": [{
            "Id": <string>,
                    "Code": <string>,
                    "Name": <string>,
                    "NameAbbr": <string>,
                    "PicturePath": <string>,
                    "IsFixed": <bool>,
                    "SupplyDate": <int>,
            "Unit": <string>,
                    "MinOrderCount": <int>,
            "Ordered": <int>,
            "Remarks": [{
                "Id": <int>,
                "Name": <string>,
                        "Price": <float>
            }, ...],
            "MenuClasses": [<string>, ...],
            "MenuPrice": {
                "ExcludePayDiscount": <bool>,
                        "Price": <float>,
                "Discount": <float>,
                "Points": <int>
            }
        }, ...]*/

            for (int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject menu = (JSONObject) jsonArray.get(i);
                ContentValues values = new ContentValues();
                values.put(DBManagerContract.MenusTable.COLUMN_NAME_Id, menu.getString("Id"));
                values.put(DBManagerContract.MenusTable.COLUMN_NAME_Code, menu.getString("Code"));
                values.put(DBManagerContract.MenusTable.COLUMN_NAME_Name, menu.getString("Name"));

                values.put(DBManagerContract.MenusTable.COLUMN_NAME_NameAbbr, menu.getString("NameAbbr"));
                values.put(DBManagerContract.MenusTable.COLUMN_NAME_Ordered, menu.getInt("Ordered"));
                values.put(DBManagerContract.MenusTable.COLUMN_NAME_Unit, menu.getString("Unit"));
                values.put(DBManagerContract.MenusTable.COLUMN_NAME_MinOrderCount, menu.getInt("MinOrderCount"));
                dbhelper.insert(DBManagerContract.MenusTable.TABLE_NAME, values);

                //以下为价格
                JSONObject price = menu.getJSONObject("MenuPrice");
                ContentValues values1 = new ContentValues();
                values1.put(DBManagerContract.MenuPricesTable.COLUMN_NAME_Id, menu.getString("Id"));
                values1.put(DBManagerContract.MenuPricesTable.COLUMN_NAME_Price, price.getDouble("Price"));
                values1.put(DBManagerContract.MenuPricesTable.COLUMN_NAME_Discount, price.getDouble("Discount"));
                values1.put(DBManagerContract.MenuPricesTable.COLUMN_NAME_ExcludePayDiscount, price.getBoolean("ExcludePayDiscount"));
                values1.put(DBManagerContract.MenuPricesTable.COLUMN_NAME_Point, price.getInt("Points"));
                dbhelper.insert(DBManagerContract.MenuPricesTable.TABLE_NAME, values1);
            }catch (Exception e){
                    Log.d(TAG,e.toString());
            }
        }
    }

    private void handleDesks(DatabaseHelper dbhelper,JSONArray jsonArray)throws Exception{
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject desk = (JSONObject)jsonArray.get(i);
            ContentValues values = new ContentValues();
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Id,desk.getString("Id"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_QrCode,desk.getString("QrCode"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Name,desk.getString("Name"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Description, desk.getString("Description"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Status,desk.getString("Status"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME__Order,desk.getString("Order"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_MinPrice,desk.getString("MinPrice"));
           // values.put(DBManagerContract.DesksTable.COLUMN_NAME_USABLE,desk.getString("Usable"));
            dbhelper.insert(DBManagerContract.DesksTable.TABLE_NAME,values);
        }
    }

    private void handleHotel(DatabaseHelper dbhelper,JSONObject jsonObject)throws Exception{
        Integer id = jsonObject.getInt("Id");
        Integer ratio = jsonObject.getInt("PointsRatio");
        String name = jsonObject.getString("Name");
        String addr = jsonObject.getString("Address");
        String tel = jsonObject.getString("Tel");
        String openTime = jsonObject.getString("OpenTime");
        String closeTime = jsonObject.getString("CloseTime");
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBManagerContract.HotelsTable.COLUMN_NAME_id,id);
        contentValues.put(DBManagerContract.HotelsTable.COLUMN_NAME_address,addr);
        contentValues.put(DBManagerContract.HotelsTable.COLUMN_NAME_tel,tel);
        contentValues.put(DBManagerContract.HotelsTable.COLUMN_NAME_name,name);

        dbhelper.insert(DBManagerContract.HotelsTable.TABLE_NAME,contentValues);
    }
    private void handleDine(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);

        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    //此类用于调试信息，TOAST消息，如果不需要调试可不用
    public class ToastMessageTask extends AsyncTask<String, String, String> {
        String toastMessage;

        @Override
        protected String doInBackground(String... params) {
            toastMessage = params[0];
            return toastMessage;
        }

        protected void OnProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
        // This is executed in the context of the main GUI thread
        protected void onPostExecute(String result){
            Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
