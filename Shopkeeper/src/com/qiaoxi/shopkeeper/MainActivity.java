package com.qiaoxi.shopkeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.zj.btsdk.BluetoothService;
import javax.crypto.spec.IvParameterSpec;

import org.json.JSONArray;
import org.json.JSONObject;

import com.qiaoxi.adapter.PaymentListView;
import com.qiaoxi.bean.Global;
import com.qiaoxi.fragment.CheckFragment;
import com.qiaoxi.fragment.ChooseCheckFragment;
import com.qiaoxi.fragment.Fragment1;
import com.qiaoxi.fragment.Fragment2;
import com.qiaoxi.fragment.Fragment2_order;
import com.qiaoxi.fragment.FragmentMenu;
import com.qiaoxi.fragment.FragmentMenuContent;


import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;










import com.zj.btsdk.BluetoothService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	private RadioButton button1, button2, button3, button4, button5, button6,
			button7, button8,button9;
	private ImageView logo;
	private RadioGroup titlegroup;
	private RadioListener listener;
	static public SQLiteDatabase db;
	TEMP temp;
	String TAG = getClass().getName();
	DineInfoReceiver dineInfoReceiver;
	String cookies;
	private static final int REQUEST_ENABLE_BT = 5;
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	private static final int REQUEST_CONNECT_DEVICE = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_menu);
		
		mService = new BluetoothService(this, mHandler);
		
//		Fragment2_order.bluetooth_print.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(Fragment2_order.linear_inventory.getChildCount() == 1){
//					Toast.makeText(getApplicationContext(), "没有订单", Toast.LENGTH_LONG).show();
//				}
//				else{
////					mService = new BluetoothService(this, mHandler);
//					//蓝牙不可用退出程序
//					if( mService.isBTopen() == false)
//					{
//			            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//					}
//					else{
//						Intent serverIntent = new Intent(MainActivity.this,DeviceListActivity.class);      //运行另外一个类的活动
//		  				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
//					}
//				}
//			}
//		});
		
		listener = new RadioListener();
		Intent intent = getIntent();
		cookies = intent.getStringExtra("cookie");
		Global.cookie = cookies;
		init();
		logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().beginTransaction()
				.replace(R.id.fragment1,Global.fragment1 =  new Fragment1(MainActivity.this)).commit();
				getFragmentManager().beginTransaction()
				.replace(R.id.fragment2,Global.fragment2 =  new Fragment2(MainActivity.this)).commit();
				titlegroup.clearCheck();
				button1.setChecked(true);
			}
		});
		
	
		getFragmentManager().beginTransaction()
		.replace(R.id.fragment1,Global.fragment1 =  new Fragment1(MainActivity.this)).commit();
		getFragmentManager().beginTransaction()
		.replace(R.id.fragment2,Global.fragment2 =  new Fragment2(MainActivity.this)).commit();
		button1.setChecked(true);
		
		ContentValues values = new ContentValues();
		
		/*注册广播*/
        dineInfoReceiver = new DineInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.saltyx.shiyan.socketservice.DINEINFO");
        registerReceiver(dineInfoReceiver, intentFilter);
        
        /*注册广播*/
        temp = new TEMP();
        IntentFilter temp_f = new IntentFilter();
        temp_f.addAction("cn.saltyx.shiyan.initdbservice.INITDBSERVICE");
        registerReceiver(temp, temp_f);
        
		
//		/*注册广播*/
//        dineInfoReceiver = new DineInfoReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("cn.saltyx.shiyan.socketservice.DINEINFO");
//        registerReceiver(dineInfoReceiver, intentFilter);
//        /***********************/
//		// values.put(key, value)
//		// databaseHelper.insert(table, values);
	}
	

    public void onStart(){
        super.onStart();
        /*启动service
        * TODO:*/
        Intent intent = new Intent(SocketService.ACTION);
        intent.setPackage("com.qiaoxi.shopkeeper");/*改成你的*/
        startService(intent);
        Log.i(TAG, "serviceOn");
        /*CHANGE tip*/
        Intent intent1 = new Intent(InitDBService.ACTION);
        intent1.putExtra("PackageName", "com.qiaoxi.shopkeeper");
        intent1.putExtra("Cookie", cookies);
        intent1.setPackage("com.qiaoxi.shopkeeper");
        startService(intent1);
        Log.d(TAG,"INITDB ON");
        //bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //Log.i(TAG, "bind");
    }
	
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(SocketService.ACTION);
        intent.setPackage("com.qiaoxi.shopkeeper");
        //unbindService(connection);
        stopService(intent);
        Intent intent1 = new Intent(InitDBService.ACTION);
        intent1.setPackage("com.qiaoxi.shopkeeper");
        //unbindService(connection);
        stopService(intent1);

        Log.i(TAG, "2killed");;
        unregisterReceiver(dineInfoReceiver);
        unregisterReceiver(temp);
    }

    class DineInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            //收到的json
            String tmpJson = intent.getStringExtra("dineInfo");
            try{
                JSONObject jsonObject = new JSONObject(tmpJson);
                Integer hotelId = jsonObject.getInt("HotelId");
                String dineId = jsonObject.getString("DineId");
                Boolean isPaid = jsonObject.getBoolean("IsPaid");
                getDineInfoFromDineId(dineId,"http://ordersystem.yummyonline.net/Waiter/Order/GetDineById");
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
        }
    }
	
	class TEMP extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            //textView.setText(intent.getStringExtra("discount"));
        }
    }
	
	ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"disconnect");
        }
    };
    
    private void getDineInfoFromDineId(final String dineId,final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter out = null;
                BufferedReader in = null;
                //new ToastMessageTask().execute(cookies);
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
                    JSONObject tmpDine = new JSONObject();
                    tmpDine.put("DineId",dineId);
                    out.print(tmpDine.toString());
                    Log.d(TAG,"dineid => "+tmpDine.toString());
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
                    Log.d(TAG,result_Msg);
                    handleDine(result_Msg);
                } catch (Exception e) {
                    Log.d(getClass().getName(), e.toString());
                    //new ToastMessageTask().execute(e.toString());
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        //new ToastMessageTask().execute(ex.toString());
                    }
                }
            }
        }).start();
    }

    
    private void handleDine(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONObject Desk = jsonObject.getJSONObject("Desk");
            JSONArray DineMenus = jsonObject.getJSONArray("DineMenus");
            String deskid = Desk.getString("Id");
            String menuid = jsonObject.getString("Id");
            int type  = jsonObject.getInt("Type");
            int headcount = jsonObject.getInt("HeadCount");
            double Price = jsonObject.getDouble("Price");
            double oriprice = jsonObject.getDouble("OriPrice");
            double discount = jsonObject.getDouble("Discount");
            String discountname = jsonObject.getString("DiscountName");
            int Status = jsonObject.getInt("Status");
            String begintime = jsonObject.getString("BeginTime");
            boolean isonline = jsonObject.getBoolean("IsOnline");
            boolean ispaid = jsonObject.getBoolean("IsPaid");
            String clerkid = jsonObject.getString("ClerkId");
            String waiterid = jsonObject.getString("WaiterId");
            String Userid = jsonObject.getString("UserId");
            
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(),1);
            ContentValues values = new ContentValues();
            values.put("Id",menuid );
            values.put("ClerkID",clerkid);
            values.put("WaiterID",waiterid);
            values.put("UserID",Userid);
            values.put("HeadCount",headcount);
            values.put("_Type",type);
            values.put("DeskId",deskid);
            values.put("BeginTime",begintime);
            values.put("Status",Status);
            values.put("OriPrice",oriprice );
            values.put("Price",Price);
            values.put("Discount",discount);
            values.put("Name",discountname);
            values.put("IsPaid",ispaid);
            values.put("IsOnline",isonline);
            dbHelper.insert(DBManagerContract.DinesTable.TABLE_NAME, values);
            values.clear();

            for(int i =0;i<DineMenus.length();i++){
            	JSONObject dinemenu = DineMenus.getJSONObject(i);
            	JSONObject Menu = dinemenu.getJSONObject("Menu");     
            	JSONArray remarks = dinemenu.getJSONArray("Remarks");
            	String MenuId1 = Menu.getString("Id");
            	for(int j = 0;j<remarks.length();j++){
            		JSONObject remarkob = remarks.getJSONObject(j);
            		int remarkid = remarkob.getInt("Id");
            		ContentValues revalue = new ContentValues();
            		revalue.put("DineMenu_DineId", menuid);
            		revalue.put("DineMenu_MenuId", MenuId1);
            		revalue.put("Remark_Id", remarkid);
            		dbHelper.insert(DBManagerContract.DineMenuRemarksTable.TABLE_NAME, values);
            		revalue.clear();
            	}
            	int Count = dinemenu.getInt("Count");
            	double Price1 = dinemenu.getDouble("Price");
            	double OriPrice1 = dinemenu.getDouble("OriPrice");
            	double RemarkPrice = dinemenu.getDouble("RemarkPrice");
            	int Status1 = dinemenu.getInt("Status");
            	
                values.put("DineId", menuid);
                values.put("MenuId", MenuId1);
                values.put("_Count", Count);
                values.put("Price", Price1);
                values.put("OriPrice", OriPrice1);
                //values.put("RemarkPrice", RemarkPrice);
                values.put("Status", Status1);
                dbHelper.insert(DBManagerContract.DineMenusTable.TABLE_NAME, values);
                values.clear();
            }
            
            String tableid = Desk.getString("Id");
            int status = jsonObject.getInt("Status");
            //Toast.makeText(getApplicationContext(), "status为："+status, Toast.LENGTH_LONG).show();
            //System.out.println("status为："+status);
            DatabaseHelper dbHelper1 = new DatabaseHelper(getApplicationContext(), 1);
            switch (status) {
			case 0:
				String table = DBManagerContract.DesksTable.TABLE_NAME;
	            ContentValues values1 = new ContentValues();
	            values1.put(DBManagerContract.DesksTable.COLUMN_NAME_Status, 1);
				String selection = "Id=?";
				String[] selectionArgs = new String[]{tableid};
				dbHelper1.update(table, values1, selection, selectionArgs);
				break;
			case 2:
				String table1 = DBManagerContract.DesksTable.TABLE_NAME;
	            ContentValues values11 = new ContentValues();
	            values11.put(DBManagerContract.DesksTable.COLUMN_NAME_Status, 0);
				String selection1 = "Id=?";
				String[] selectionArgs1 = new String[]{tableid};
				dbHelper1.update(table1, values11, selection1, selectionArgs1);
				break;
			default:
				break;
			}
            
            GridLayout grid = Fragment2.gridLayout;
            
            for(int i = 0 ;i <grid.getChildCount();i++){
            	TableItem ta = (TableItem) grid.getChildAt(i);
            	if(ta.getTableNum().equals(tableid)){
            		ta.setiamgeBackground(1);
            		break;
            	}
            	
            }
            
            
            Log.d(TAG,"jsonObject=> "+json);
            
        }catch (Exception e){
            Log.d(TAG,e.toString());
            if (json.equals("")){
                Log.d(TAG,"json is null");
            }
        }
    }
	

	private void init() {
		logo = (ImageView) findViewById(R.id.iv_logo);
		button1 = (RadioButton) findViewById(R.id.radio_1);
		button2 = (RadioButton) findViewById(R.id.radio_2);
		button9 = (RadioButton) findViewById(R.id.radio_9);
        button7 = (RadioButton) findViewById(R.id.radio_7);
		titlegroup = (RadioGroup) findViewById(R.id.title_radiogroup);
		button1.setOnCheckedChangeListener(listener);
		button2.setOnCheckedChangeListener(listener);
		button9.setOnCheckedChangeListener(listener);
        button7.setOnCheckedChangeListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class RadioListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.getId() == R.id.radio_1) {
				if (isChecked) {
					getFragmentManager().beginTransaction()
							.replace(R.id.fragment1,Global.fragment1 = new Fragment1(MainActivity.this)).commit();
					getFragmentManager().beginTransaction()
							.replace(R.id.fragment2,Global.fragment2 = new Fragment2(MainActivity.this)).commit();
					titlegroup.clearCheck();
					button9.setChecked(false);
					button1.setChecked(true);
				}
			} else if (buttonView.getId() == R.id.radio_2) {// 切换到菜单设置
				if (isChecked) {
					getFragmentManager().beginTransaction()
							.replace(R.id.fragment1,Global.fragment1 =  new FragmentMenu())
							.commit();
					getFragmentManager().beginTransaction()
							.replace(R.id.fragment2,Global.fragment2 = new FragmentMenuContent())
							.commit();
				}
			}
			else if (buttonView.getId() == R.id.radio_9) {// 切换到菜单设置
				if (isChecked) {
					ExitApplication.getInstance().exit();
				}
			}
			else if (buttonView.getId() == R.id.radio_7){
                if (isChecked){
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment1,new ChooseCheckFragment())
                            .commit();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment2,new CheckFragment())
                            .commit();
                }
            }

		}
	}	
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
	
//	public static void Bluetouch(){
//		if(Fragment2_order.linear_inventory.getChildCount() == 1){
//			Toast.makeText(MainActivitythis, "没有订单", Toast.LENGTH_LONG).show();
//		}
//		else{
////			mService = new BluetoothService(this, mHandler);
//			//蓝牙不可用退出程序
//			if( mService.isBTopen() == false)
//			{
//	            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//	            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//			}
//			else{
//				Intent serverIntent = new Intent(MainActivity.this,DeviceListActivity.class);      //运行另外一个类的活动
//  				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
//			}
//		}
//	}
	
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	  switch (requestCode) {
	          case REQUEST_ENABLE_BT:      //请求打开蓝牙
	              if (resultCode == Activity.RESULT_OK) {   //蓝牙已经打开
	            	Intent serverIntent = new Intent(MainActivity.this,DeviceListActivity.class);      //运行另外一个类的活动
	  				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
	              } else {                 //用户不允许打开蓝牙
	                Toast.makeText(getApplicationContext(), "Refuse to open bluetooth",Toast.LENGTH_LONG).show();
	              }
	              break;
	          case  REQUEST_CONNECT_DEVICE:     //请求连接某一蓝牙设备
	          	if (resultCode == Activity.RESULT_OK) {   //已点击搜索列表中的某个设备项
	                  String address = data.getExtras()
	                                       .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //获取列表项中设备的mac地址
	                  con_dev = mService.getDevByMac(address);   
	                  
	                  mService.connect(con_dev);
	              }
	          	else{
	          		Toast.makeText(getApplicationContext(), "connect failed", Toast.LENGTH_LONG).show();
	          	}
	              break;
	    	
	    	
	    	  }
	    }
	 
	 private final  Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case BluetoothService.MESSAGE_STATE_CHANGE:
	                switch (msg.arg1) {
	                case BluetoothService.STATE_CONNECTED:   //已连接
	                	com.qiaoxi.shopkeeper.MenuItem en = null;
	                	String msg1 = "桌号："+Global.deskid+"    人数："+Fragment2_order.head_count_spinner.getSelectedItem().toString().trim()+"\n\n";   	
	                	for(int i=0;i<Fragment2_order.linear_inventory.getChildCount()-1;i++){
	                		en = (com.qiaoxi.shopkeeper.MenuItem) Fragment2_order.linear_inventory.getChildAt(i);
	                		msg1 += en.getdishname()+en.getnotes()+"  "+"x"+en.getcount()+"\n\n";
	                	}
	                    mService.sendMessage(msg1+"\n", "GBK");
	                    if (mService != null) 
	            			mService.stop();
	            		mService = null;
	                    break;
	                case BluetoothService.STATE_CONNECTING:  //正在连接
	                	Log.d("蓝牙调试","正在连接.....");
	                    break;
	                case BluetoothService.STATE_LISTEN:     //监听连接的到来
	                case BluetoothService.STATE_NONE:
	                	Log.d("蓝牙调试","等待连接.....");
	                    break;
	                }
	                break;
	            case BluetoothService.MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
	                Toast.makeText(getApplicationContext(), "Device connection was lost",
	                               Toast.LENGTH_SHORT).show();
	                if (mService != null) 
	        			mService.stop();
	        		mService = null;
	                break;
	            case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
	            	Toast.makeText(getApplicationContext(), "Unable to connect device",
	                        Toast.LENGTH_SHORT).show();
	            	if (mService != null) 
	        			mService.stop();
	        		mService = null;
	            	break;
	            }
	        }
	        
	    };

}
