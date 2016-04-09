package com.qiaoxi.shopkeeper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.cert.LDAPCertStoreParameters;

public class SocketService extends Service {

    private static Integer headerLengthSize = 10;
    private static byte[] header = {0x00, (byte)0xff, 0x11,(byte)0xee};
    private String TAG = this.getClass().getName();;
    public static String ACTION ="cn.saltyx.shiyan.socketservice.SOCKET";
    private Socket socket;
    private String ip;
    private int port;
    private DataInputStream input;
    private PrintStream output;
    Thread thread_socket;
    Thread thread_rc;
    private Boolean iskilled;
    private Boolean isOn;
    public SocketService(){
        this.ip = "122.114.96.157";
        this.port = 18000;
    }
    public SocketService(String ip , int port ) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "服务绑定成功");
        /*thread_socket =  new Thread(socketRunnable);
        thread_socket.start();
        thread_rc = new Thread(receiveRunnable);
        thread_rc.start();*/
        return null;
    }

    public void onCreate(){
        iskilled = false;
        isOn = false;
        super.onCreate();
        thread_socket =  new Thread(socketRunnable);
        thread_socket.start();
        thread_rc = new Thread(receiveRunnable);
        thread_rc.start();

        Log.d(TAG,"服务创建成功");
    }

    public void onDestroy(){
        super.onDestroy();
        iskilled = true;
        try{
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }

        //thread_socket.interrupt();
       // thread_rc.interrupt();
        Log.d(TAG, "SocketService服务已经销毁");
    }

     @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //此服务被终止之后会自动重启
        return START_STICKY;
    }

    private Runnable socketRunnable = new Runnable() {
        @Override
        public void run() {
            boolean tag = true;
            while(iskilled == false){
                //用于重建socket连接和判断网络情况
                if (!isNetworkAvailable(getApplicationContext())){
                    new ToastMessageTask().execute("当前网络不可用，检查网络连接");
                    try{
                        if (socket != null){
                            socket.close();
                            socket.shutdownInput();
                            socket.shutdownOutput();
                        }

                    }catch (Exception e){
                        Log.d(TAG,e.toString());
                    }
                    tag = true;//网络不可用需要重建
                }
                if(tag == true){ //如果需要重建socket
                    try{
                        socket = new Socket(ip,port);
                        output = new PrintStream(socket.getOutputStream(), true, "utf-8");
                        input=new DataInputStream(socket.getInputStream());
                        String tmpStr = "{\"Type\": \"{053A168C-D4B8-409A-A058-7E2208B57CDA}\"}";
                        byte[] sendStr1 = tmpStr.getBytes("UTF8");

                        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream(6);
                        bytesOut.write(sendStr1.length);

                        byte[] sendStr2 =bytesOut.toByteArray();
                        byte[] sendStr3 = new byte[sendStr1.length+headerLengthSize];

                        System.arraycopy(header,0,sendStr3, 0,4);
                        System.arraycopy(sendStr2,0,sendStr3,4,sendStr2.length);
                        System.arraycopy(sendStr1, 0, sendStr3,headerLengthSize,sendStr1.length);

                        // sendStr 为验证的字节数组
                        //主要为了验证socket传输内容
                        // 如果连接成功没有报错不需要留意
                        byte[] sendStr={0x00,(byte)0xff,0x11,(byte)0xee,
                                        0x32,0x00,0x00,0x00,0x00,
                                        0x00,0x7b,0x22,0x54,0x79,
                                        0x70,0x65,0x22,0x3a,0x20,
                                        0x22,0x7b,0x30,0x35,0x33,
                                        0x41,0x31,0x36,0x38,0x43,
                                        0x2d,0x44,0x34,0x42,0x38,
                                        0x2d,0x34,0x30,0x39,0x41,
                                        0x2d,0x41,0x30,0x35,0x38,
                                        0x2d,0x37,0x45,0x32,0x32,
                                        0x30,0x38,0x42,0x35,0x37,
                                        0x43,0x44,0x41,0x7d,0x22,
                                        0x7d};
                        Log.d(TAG,new String(sendStr3));
                        Log.d(TAG, new String(sendStr));
                        output.write(sendStr3);

                        Log.d(TAG,"socket建立成功");

                        //可以接受
                        isOn = true;

                        tag = false;//建立成功不需要重建
                    }catch (Exception e){
                        Log.d(TAG,"socket建立失败");
                        isOn = false;
                        Log.d(TAG, e.toString());
                        new ToastMessageTask().execute(e.toString());
                        tag = true;//建立失败尝试重建
                    }finally {
                        //休眠5s后判断
                        try{
                            Thread.currentThread().sleep(5000);
                        }catch (Exception e){
                            Log.d(TAG,e.toString());
                        }
                    }
                }
            }
        }
    };

    //用于接受订单消息
    private Runnable receiveRunnable = new Runnable() {
        @Override
        public void run() {
            while (iskilled == false){
                while (!isOn);
                try {
                    Thread.currentThread().sleep(2000);
                    //接收字符串
                    byte[] buffer = new byte[4096];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int number;

                    while ((number = input.read(buffer, 0, buffer.length)) > 0) {
                        baos.write(buffer, 0, number);
                        byte []bytes = baos.toByteArray();
                        byte []reallyByte = new byte[bytes.length-headerLengthSize];
                        System.arraycopy(bytes,headerLengthSize,reallyByte,0,
                                bytes.length-headerLengthSize);
                        String json = new String(reallyByte);
                        baos.reset();
                        //json = json.substring(0,json.length());
                        Log.d(TAG, json);
                        new ToastMessageTask().execute(json);
                        Intent intent = new Intent("cn.saltyx.shiyan.socketservice.DINEINFO");
                        intent.putExtra("dineInfo",json);
                        sendBroadcast(intent);
                    }

                } catch(Exception e){
                    if (!socket.isClosed()){
                        Log.d(TAG,"接收过程发生异常");
                        Log.d(TAG,e.toString());
                        new ToastMessageTask().execute(e.toString());
                    }else{
                        Log.d(TAG,"socket closed");
                        Log.d(TAG,e.toString());
                    }
                }
            }
        }
    };

    public boolean onUnbind(Intent intent){
        try{
            Log.d(TAG,"已解绑");
        }catch (Exception e){
            Log.d(TAG,"解绑过程出错");
            Log.d(TAG,e.toString());
        }
        return super.onUnbind(intent);
    }



    //调试信息 TOAST
    private class ToastMessageTask extends AsyncTask<String, String, String> {
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

    //判断网络情况
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
