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



public class LoginActivity extends Activity {
    EditText editText;
    EditText editText1;
    Button button;
    String TAG = getClass().getName();
    String cookies;
    String s1,s2;
    Map<String, List<String>> maps;
    List<String> cookieslist;

    Boolean flags_getdiscountMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(this);

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


                                //以下为自己的逻辑
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("cookie", cookies);

                                Intent intent1 = new Intent(InitDBService.ACTION);
                                intent1.putExtra("PackageName", "com.qiaoxi.shopkeeper");
                                intent1.putExtra("Cookie", cookies);
                                intent1.setPackage("com.qiaoxi.shopkeeper");
                                startService(intent1);
                                Log.d(TAG,"INITDB ON");

                                Global.waiterid = s1;
                                Global.areasaved = false;
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
    public void onStart(){
        super.onStart();
    }
    public void onDestroy(){
        super.onDestroy();
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
