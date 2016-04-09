package com.qiaoxi.shopkeeper;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
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
import java.net.HttpURLConnection;
import java.net.URL;


public class InitDBService extends Service {

    public static String ACTION = "cn.saltyx.shiyan.initdbservice.INITDBSERVICE";

    private String TAG = this.getClass().getName();;
    private DatabaseHelper dbhelper;
    private String packageName;
    private String cookies;//用于获取所有信息的cookie
    public InitDBService() {
    }
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void onCreate(){
        Log.d(TAG, "InitDB服务已经创建");
        super.onCreate();
    }

    public void onDestroy(){
        Log.d(TAG,"InitDB服务已经销毁");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        //TODO:此处需要判断是否是通过离线登陆进来的
        //TODO:如果是离线登录不要执行if里面的操作！！！！！！！！
        Log.d(TAG,"InitDB服务正在运行");
        try {
            this.packageName = intent.getStringExtra("PackageName");
            this.cookies = intent.getStringExtra("Cookies");
            SQLiteDatabase.deleteDatabase(new File("/data/data/"+this.packageName+"/databases/DB_Manager"));

            dbhelper = new DatabaseHelper(getApplicationContext(), 1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PrintWriter out = null;
                    BufferedReader in = null;
                    try {

                    	URL realUrl = new URL("http://ordersystem.yummyonline.net/Waiter/Order/GetAreas");
                    	HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                        //POST请求头属性
                        //conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Cookie", cookies);
                        conn.setRequestMethod("POST");
                        out = new PrintWriter(conn.getOutputStream());
                        /*
                        * 下面是发送的信息*/
                        /*************/
                        // 发送请求参数
                        out.flush();
                        in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        String lines = "";
                        String result_Msg = "";
                        while ((lines = in.readLine()) != null) {
                            result_Msg += "\n" + lines;
                        }
                        System.out.println("区域信息为："+result_Msg);
                        JSONArray jsonArray = new JSONArray(result_Msg);
                        handleAreas(dbhelper, jsonArray);

                        realUrl = new URL("http://ordersystem.yummyonline.net/Waiter/Order/GetMenuInfos");
                        conn = (HttpURLConnection) realUrl.openConnection();
                        //POST请求头属性
                        //conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Cookie", cookies);
                        conn.setRequestMethod("POST");
                        out = new PrintWriter(conn.getOutputStream());
                        /*
                        * 下面是发送的信息*/
                        /*************/
                        // 发送请求参数
                        out.flush();
                        in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        lines = "";
                        result_Msg = "";
                        while ((lines = in.readLine()) != null) {
                            result_Msg += "\n" + lines;
                        }
                        storeJsonForAllMsg(result_Msg);//备份一份
                        handleAllJsonMsg(result_Msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG,e.toString());
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                            if (in != null) {
                                in.close();
                            }
                        } catch (IOException ex) {
                            Log.d(TAG, ex.toString());
                            ex.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
        return START_NOT_STICKY;
    }


    private void storeJsonForAllMsg(String json) {
        try{
            //此处文件夹名称 /sdcard/Android/data/项目包名/
            File dir = new File("/sdcard/Android/data/"+this.packageName+"/");
            if (!dir.exists()){
                //如果文件夹不存在
                dir.mkdirs();
            }
            //此处文件路径名称 /sdcard/Android/data/项目包名/allMsg.json
            File file = new File("/sdcard/Android/data/"+this.packageName+"/allMsg.json");
            if (file.exists()){
                file.delete();
                file.createNewFile();
            }
            if (json!=null ) {
                //如果json数据为空就不修改原来的文件
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter out = new BufferedWriter(fileWriter);
                out.write(json);
                out.close();
            }
        }catch(Exception e){
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }
    }

    private void handleAllJsonMsg(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);

        //删除数据库以便新建个更新本地数据库
        //SQLiteDatabase.deleteDatabase(new File("/data/data/" + this.packageName + "/databases/DB_Manager"));
        //SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/data/cn.saltyx.shiyan.myapplication/databases/DB_Manager.db", null);
        //dbhelper.dropAllTables(db);
        //TODO:===========================================
        //
        try {
            JSONObject discountMethods = jsonObject.getJSONObject("DiscountMethods");
            Intent intent = new Intent("cn.saltyx.shiyan.initdbservice.INITDBSERVICE");
            Global.discount = discountMethods.toString().trim(); 
            intent.putExtra("discount", discountMethods.toString());
            sendBroadcast(intent);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }
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
        //classes
        try{
            JSONArray jsonArray1 = jsonObject.getJSONArray("MenuClasses");
            handleClasses(dbhelper, jsonArray1);
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }

    }

    private void handleClasses(DatabaseHelper dbhelper, JSONArray jsonArray) throws Exception{
        for (int i=0; i<jsonArray.length(); i++){
            try{
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBManagerContract.MenuClassesTable.COLUMN_NAME_Id, jsonObject.getString("Id"));
                contentValues.put(DBManagerContract.MenuClassesTable.COLUMN_NAME_Name, jsonObject.getString("Name"));
                dbhelper.insert(DBManagerContract.MenuClassesTable.TABLE_NAME, contentValues);
            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }

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
        values.put(DBManagerContract.PayKindsTable.COLUMN_NAME_Discount, jsonObject.getDouble("Discount"));
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

                //备注
                JSONArray remarks = menu.getJSONArray("Remarks");
                handleRemarks(dbhelper, remarks, menu.getString("Id"));

                //类别
                JSONArray classes = menu.getJSONArray("MenuClasses");
                handleMenuClasses(dbhelper, classes, menu.getString("Id"));
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
        }
    }

    private void handleMenuClasses(DatabaseHelper dbhelper, JSONArray classes, String menuId) throws Exception{
        for (int i=0; i<classes.length(); i++){
            try{
                String id = (String) classes.get(i);
                ContentValues c_values = new ContentValues();
                c_values.put(DBManagerContract.MenuClassMenusTable.COLUMN_NAME_Menu_id,menuId);
                c_values.put(DBManagerContract.MenuClassMenusTable.COLUMN_NAME_MenuClass_Id, id);
                dbhelper.insert(DBManagerContract.MenuClassMenusTable.TABLE_NAME, c_values);
           }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }
    }

    private void handleRemarks(DatabaseHelper dbhelper, JSONArray jsonArray, String menuId)throws Exception{
        for (int i=0; i< jsonArray.length(); i++){
            try{
                JSONObject temp = (JSONObject) jsonArray.get(i);
                ContentValues values = new ContentValues();
                values.put(DBManagerContract.RemarksTable.COLUMN_NAME_Id, temp.getString("Id"));
                values.put(DBManagerContract.RemarksTable.COLUMN_NAME_Name, temp.getString("Name"));
                values.put(DBManagerContract.RemarksTable.COLUMN_NAME_Price, temp.getString("Price"));
                try {
                    dbhelper.insert(DBManagerContract.RemarksTable.TABLE_NAME, values);
				} catch (Exception e) {
					// TODO: handle exception
				}


                ContentValues m_remarks = new ContentValues();
                m_remarks.put(DBManagerContract.MenuRemarksTable.COLUMN_NAME_Menu_Id, menuId);
                m_remarks.put(DBManagerContract.MenuRemarksTable.COLUMN_NAME_Remark_Id, temp.getString("Id"));
                dbhelper.insert(DBManagerContract.MenuRemarksTable.TABLE_NAME, m_remarks);

            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }

    }


    private void handleDesks(DatabaseHelper dbhelper,JSONArray jsonArray)throws Exception{
        for (int i=0; i<jsonArray.length(); i++){

            JSONObject desk = (JSONObject)jsonArray.get(i);
            ContentValues values = new ContentValues();
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Id,desk.getString("Id") == "" ? "" : desk.getString("Id"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_QrCode,desk.getString("QrCode")== "" ? "" : desk.getString("QrCode"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Name,desk.getString("Name")== "" ? "" : desk.getString("Name"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Description, desk.getString("Description")== "" ? "" : desk.getString("Description"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_Status,desk.getString("Status")== "" ? "" : desk.getString("Status"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME__Order,desk.getString("Order")== "" ? "" : desk.getString("Order"));
            values.put(DBManagerContract.DesksTable.COLUMN_NAME_MinPrice,desk.getString("MinPrice")== "" ? "" : desk.getString("MinPrice"));
            //values.put(DBManagerContract.DesksTable.COLUMN_NAME_USABLE,desk.getString("Usable")== "" ? "" : desk.getString("Usable"));
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
    
    private void handleAreas(DatabaseHelper dbhelper,JSONArray jsonArray)throws Exception{
    	Global.areamap.clear();
    	Global.areamapback.clear();
    	int Id = 1;
        for (int i=0; i<jsonArray.length(); i++){
            try{
                JSONObject area = (JSONObject)jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                Global.areamap.put(area.getString("Id"),String.valueOf(Id));
                Global.areamapback.put(String.valueOf(Id++), area.getString("Id"));
                values.put(DBManagerContract.AreasTable.COLUMN_NAME_id,area.getString("Id") == "" ? "" : area.getString("Id"));
                values.put(DBManagerContract.AreasTable.COLUMN_NAME_name,area.getString("Name")== "" ? "" : area.getString("Name"));
                //values.put(DBManagerContract.AreasTable.COLUMN_NAME_usable,area.getBoolean("Usable"));
                values.put(DBManagerContract.AreasTable.COLUMN_NAME_description,area.getString("Description")== "" ? "" : area.getString("Description"));
                //values.put(DBManagerContract.DesksTable.COLUMN_NAME_USABLE,desk.getString("Usable")== "" ? "" : desk.getString("Usable"));
                dbhelper.insert(DBManagerContract.AreasTable.TABLE_NAME,values);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    
}
