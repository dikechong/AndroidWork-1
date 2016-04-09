package com.qiaoxi.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;


/**
 * Created by shiyan on 2016/3/10.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /*
    *
    *
    * */
    private String TAG;
    SQLiteDatabase db;
    private static final String DBName="DB_Manager";
    public DatabaseHelper(Context context,int version){
        super(context, DBName, null, version);
        TAG = getClass().getName();


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(SQLFile.areas);
        db.execSQL(SQLFile.desks);
        db.execSQL(SQLFile.hotels);
        db.execSQL(SQLFile.menus);
        db.execSQL(SQLFile.menuPrices);
        db.execSQL(SQLFile.menuClassMenus);
        db.execSQL(SQLFile.menuRemarks);
        db.execSQL(SQLFile.menuOnSales);
        db.execSQL(SQLFile.menuSetMeals);
        db.execSQL(SQLFile.dines);
        db.execSQL(SQLFile.dineRemarks);
        db.execSQL(SQLFile.dineMenus);
        db.execSQL(SQLFile.dineMenuRemarks);
        db.execSQL(SQLFile.payKinds);
        db.execSQL(SQLFile.staff);
        db.execSQL(SQLFile.staffRoles);
        db.execSQL(SQLFile.staffStaffRoles);
        db.execSQL(SQLFile.users);
        db.execSQL(SQLFile.menuclasses);
        db.execSQL(SQLFile.dinePaidDetail);
        db.execSQL(SQLFile.remarks);
        createPattern(db);
    }

    public void insert(String table, ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.insert(table, null, values);
            db.close();
            Log.d(TAG, values.toString());
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    public Cursor query(String table,String[] projection,
                        String selection,String[] selectionArgs,
                        String groupby,String having,
                        String sortorder,String limit){
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor =  db.query(table, projection,
                    selection, selectionArgs,
                    groupby, having, sortorder, limit);
            return cursor;
        }catch (Exception e){
            Log.d(TAG,e.toString());
            return null;
        }
    }

    public Cursor query(String sql){
        SQLiteDatabase db = getReadableDatabase();
//        String sql = "select * from " + table;
        try{
            Cursor cs=db.rawQuery(sql, new String[0]);
            return cs;
        }catch (Exception e){
            Log.d(TAG,e.toString());
            return null;
        }
    }


    public void delete(String table,String selection,String[] selectionArgs){
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.delete(table, selection, selectionArgs);
            Log.d(TAG,"delete ok");
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }finally {
            close();
        }
    }

    public void update(String table,ContentValues values,String selection,String[] selectionArgs) {
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.update(table, values, selection, selectionArgs);
            Log.d(TAG,"update ok");
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }finally {
            close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table  if exists hotels");
        db.execSQL("drop table  if exists Areas");
        db.execSQL("drop table  if exists Desks");
        db.execSQL("drop table  if exists Menus");
        db.execSQL("drop table  if exists MenuClassMenus");
        db.execSQL("drop table  if exists MenuPrices");
        db.execSQL("drop table  if exists MenuRemarks");
        db.execSQL("drop table  if exists MenuOnSales");
        db.execSQL("drop table  if exists MenuSetMeals");
        db.execSQL("drop table  if exists Dines");
        db.execSQL("drop table  if exists DineRemarks");
        db.execSQL("drop table  if exists DineMenus");
        db.execSQL("drop table  if exists DineMenuRemarks");
        db.execSQL("drop table  if exists PayKinds");
        db.execSQL("drop table  if exists Staffs");
        db.execSQL("drop table  if exists StaffRoles");
        db.execSQL("drop table  if exists StaffStaffRoles");
        db.execSQL("drop table  if exists Users");
        onCreate(db);
    }


    public void dropAllTables(SQLiteDatabase db){
        db.execSQL("drop table  if exists hotels");
        db.execSQL("drop table  if exists Areas");
        db.execSQL("drop table  if exists Desks");
        db.execSQL("drop table  if exists Menus");
        db.execSQL("drop table  if exists MenuClassMenus");
        db.execSQL("drop table  if exists MenuPrices");
        db.execSQL("drop table  if exists MenuRemarks");
        db.execSQL("drop table  if exists MenuOnSales");
        db.execSQL("drop table  if exists MenuSetMeals");
        db.execSQL("drop table  if exists Dines");
        db.execSQL("drop table  if exists DineRemarks");
        db.execSQL("drop table  if exists DineMenus");
        db.execSQL("drop table  if exists DineMenuRemarks");
        db.execSQL("drop table  if exists PayKinds");
        db.execSQL("drop table  if exists Staffs");
        db.execSQL("drop table  if exists StaffRoles");
        db.execSQL("drop table  if exists StaffStaffRoles");
        db.execSQL("drop table  if exists Users");
        onCreate(db);

    }
    @Override
    public synchronized void close() {
        super.close();

    }

    //
    public void createPattern(SQLiteDatabase db ){
        Cursor cursor = db.rawQuery("select name from sqlite_master where type ='table' order by name", null);
        String pattern = "";
        String pattern1 = "";
        String pattern2 = "";
        while (cursor.moveToNext()){
            String t_name = cursor.getString(0);//表名


            Cursor cursor1 = db.rawQuery("PRAGMA table_info("+t_name+")",null);
            while(cursor1.moveToNext()){
                pattern2 += String.format(SQLFile.CONTRACT_PATTERN_2,
                        cursor1.getString(1),cursor1.getString(1));

                //Log.d(TAG, pattern1);//字段名称
            }
            pattern1 += String.format(SQLFile.CONTRACT_PATTERN_1,t_name,t_name, pattern2);
            pattern2 = "";
            //Log.d(TAG,pattern1);
        }
        pattern = String.format(SQLFile.CONTRACT_PATTERN, TAG, pattern1);
        storeJsonForAllMsg(pattern);
    }

    private void storeJsonForAllMsg(String json) {
        try{
            //此处文件夹名称 /sdcard/Android/data/项目包名/
            File dir = new File("/sdcard/Android/data/autoJavaContract/");
            if (!dir.exists()){
                //如果文件夹不存在
                dir.mkdirs();
            }
            //此处文件路径名称 /sdcard/Android/data/项目包名/allMsg.json
            File file = new File("/sdcard/Android/data/autoJavaContract/DBManagerContract.java");
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            if (json!=null ) {
                //如果json数据为空就不修改原来的文件
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter out = new BufferedWriter(fileWriter);
                out.write(json);
                out.close();
            }
        }catch(Exception e){
            Log.d(TAG,e.toString());
            e.printStackTrace();
        }
    }
}
