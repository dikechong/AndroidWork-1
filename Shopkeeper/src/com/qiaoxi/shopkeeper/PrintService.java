package com.qiaoxi.shopkeeper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

/* WIFI打印机连接服务
* 只需向目标打印机发送数据即可，因此为单向传递
*
* */
public class PrintService extends Service {
    public String TAG = getClass().getName();
    public static String ACTION = "cn.saltyx.shiyan.printdemo.PRINT_SERVER";

    //Default Setting
    private String IP = "192.168.1.160";
    private Integer PORT = 9100;

    private Socket socket;
    private DataInputStream dataInputStream;
    private PrintStream printStream;//向目标发送数据

    private PrinterReceiver printerReceiver;

    public PrintService() {

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        try{
            printerReceiver = new PrinterReceiver();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("cn.saltyx.shiyan.printdemo.PRINT_ACTION");
            registerReceiver(printerReceiver, intentFilter);

        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }

        Log.d(TAG, "PRINT服务启动");
        /*服务建立成功之后尝试连接SOCKET*/
        reBuildSocket();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //此服务被终止之后会自动重启

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(printerReceiver);
        shutdownSocket();
    }

    public void shutdownSocket(){
        try{
            if (!socket.isClosed()){
                socket.close();
                socket.shutdownOutput();
                printStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }

    public void reBuildSocket(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(IP,PORT);
                    printStream = new PrintStream(socket.getOutputStream(), true, "utf-8");
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }

            }
        }).start();
    }

    public void printIt(String str){
        try{
            byte[] bytes = str.getBytes();
            printStream.write(bytes);
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, e.toString());//TODO：尝试打印失败，重建SOCKET
        }

    }

    class PrinterReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("printerMsg");
            printIt(str);
            Log.d(TAG, "收到"+str);
        }
    }
}
