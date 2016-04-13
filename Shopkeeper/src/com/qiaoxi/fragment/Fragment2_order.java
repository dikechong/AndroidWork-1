package com.qiaoxi.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiaoxi.adapter.NoteGridAdapter;
import com.qiaoxi.adapter.PaymentListView;
import com.qiaoxi.bean.Cart;
import com.qiaoxi.bean.CartAddition;
import com.qiaoxi.bean.Desk;
import com.qiaoxi.bean.Global;
import com.qiaoxi.bean.OrderedMenus;
import com.qiaoxi.bean.PaidDetail;
import com.qiaoxi.bean.PaidDetails;
import com.qiaoxi.bean.PayKind;
import com.qiaoxi.bean.PostMenu;
import com.qiaoxi.bean.PostMenuWithPay;
import com.qiaoxi.shopkeeper.DeviceListActivity;
import com.qiaoxi.shopkeeper.ExitApplication;
import com.qiaoxi.shopkeeper.LoginActivity;
import com.qiaoxi.shopkeeper.MainActivity;
import com.qiaoxi.shopkeeper.MenuItem;
import com.qiaoxi.shopkeeper.MenuItem.menuClickListerner;
import com.qiaoxi.shopkeeper.PaymentDialog;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.shopkeeper.LoginActivity.ToastMessageTask;
import com.qiaoxi.shopkeeper.R.drawable;
import com.qiaoxi.shopkeeper.TableItem;
import com.qiaoxi.shopkeeper.TimeDiscounts;
import com.qiaoxi.shopkeeper.VipDiscounts;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;
import com.zj.btsdk.BluetoothService;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.ShutterCallback;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment") public class Fragment2_order extends Fragment {
	//获取备注的三个文本框
	String TAG = getClass().getName();
	private TextView tv_note1,tv_note2,tv_note3,num_in_order,dishnum_in_order,
			dishname_in_order,price1_in_order,price2_in_order,confirm,send;
	public static TextView bluetooth_print;
	private EditText discount;
	private ListView lv_payment;
	public double totolprice = 0.0;
	//获取备注编辑图片
	private EditText edi_in_order,cash,credit;
	public static TextView should_pay,already_pay,left_to_pay,give_back,get;
	private Button sub_in_order,plus_in_order;
	public static Button add_to_menu;
	private ImageView iv_editNote;
	private NoteGridAdapter adapter;
	public static LinearLayout linear_inventory;
	private List<String> noteSource;
	private ClickListener listener;
	private String DeskId;
	String [] allDiscountMethod,headcountarr;
	Map<String, Double> map;
	Map<String, Double> payment_map;
	Spinner spinner ;
	public static Spinner head_count_spinner ;
	String cookies,discountMethod;
	DatabaseHelper dbhelper;
	private long systime;
	private Date curDate;
	private String str;
	private SimpleDateFormat formatter;

	private static final int REQUEST_ENABLE_BT = 5;
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	private static final int REQUEST_CONNECT_DEVICE = 6;

	ArrayList<String> payment = new ArrayList<>();
	public static TextView discount_pay;
	private Context context;

	public Fragment2_order(String deskid,Context con){
		this.DeskId = deskid;
		this.context = con;
	}

	@Override
	public void onDestroyView() {
		discount_pay.setText("0");
		super.onDestroyView();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment2_order, container, false);
		init(view);
		return view;
	}
	@SuppressLint("NewApi") private void init(View v){
		//获取三个编辑框和编辑图片
		tv_note1=(TextView) v.findViewById(R.id.tv_note1);
		tv_note2=(TextView) v.findViewById(R.id.tv_note2);
		tv_note3=(TextView) v.findViewById(R.id.tv_note3);
		num_in_order = (TextView) v.findViewById(R.id.num_in_order);
		iv_editNote=(ImageView) v.findViewById(R.id.iv_editNote);
		sub_in_order  = (Button) v.findViewById(R.id.sub_in_order);
		plus_in_order = (Button) v.findViewById(R.id.plus_in_order);
		dishnum_in_order = (TextView) v.findViewById(R.id.dishnum_in_order);
		dishname_in_order = (TextView) v.findViewById(R.id.dishname_in_order);
		price1_in_order = (TextView) v.findViewById(R.id.price1_in_order);
		price2_in_order = (TextView) v.findViewById(R.id.price2_in_order);
		price2_in_order.setTextColor(Color.parseColor("#000000"));
		linear_inventory = (LinearLayout) v.findViewById(R.id.linear_inventory);
		add_to_menu = (Button) v.findViewById(R.id.add_to_menu);
		edi_in_order = (EditText) v.findViewById(R.id.edi_in_order);
		spinner = (Spinner) v.findViewById(R.id.spinner_discount);
		confirm = (Button) v.findViewById(R.id.confirm);
		should_pay = (TextView) v.findViewById(R.id.should_pay);
		already_pay = (TextView) v.findViewById(R.id.already_pay);
		left_to_pay = (TextView) v.findViewById(R.id.left_to_pay);
		bluetooth_print = (TextView) v.findViewById(R.id.bluetooth_print);

		get = (TextView) v.findViewById(R.id.get);
		give_back = (TextView) v.findViewById(R.id.give_back);
		discount = (EditText) v.findViewById(R.id.discount); discount.setEnabled(false);//不可编辑
		send = (TextView) v.findViewById(R.id.sendbt);
		head_count_spinner = (Spinner) v.findViewById(R.id.head_count);

		get.setVisibility(View.INVISIBLE);
		edi_in_order.setInputType(InputType.TYPE_CLASS_TEXT);
		credit = (EditText)v.findViewById(R.id.credit);

		cookies = Global.cookie;
		discountMethod = Global.discount;

		dbhelper = new DatabaseHelper(getActivity(), 1);

		
		/****************************/
		//人数spinner
		
		headcountarr = new String[50];
		for(int i=1;i<51;i++){
			headcountarr[i-1] = String.valueOf(i); 
		}
		
		ArrayAdapter<String> arrayAdapter0 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item
                , headcountarr);
        arrayAdapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        head_count_spinner.setAdapter(arrayAdapter0);
        head_count_spinner.setGravity(Gravity.CENTER);
        /*spinner点击效果，目前是点了就显示打折数*/
        head_count_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    int count = Integer.valueOf(parent.getItemAtPosition(position).toString());
                    try {
						Global.headcount.setText(String.valueOf(count));
					} catch (Exception e) {
						// TODO: handle exception
					}
                    ContentValues values = new ContentValues();
                    values.put("HeadCount", count);
                    String[] selectionArgs = new String[]{Global.ordernumber};
                    dbhelper.update(DBManagerContract.DinesTable.TABLE_NAME, values, "Id=?", selectionArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                   // new ToastMessageTask().execute(e.toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
		
        /********************************/
		
        bluetooth_print.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// MainActivity.Bluetouch();
//				if(linear_inventory.getChildCount() == 1){
//					Toast.makeText(getActivity(), "没有订单", Toast.LENGTH_LONG).show();
//				}
//				else{
//					mService = new BluetoothService(getActivity(), mHandler);
//					//蓝牙不可用退出程序
//					if( mService.isBTopen() == false)
//					{
//			            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//					}
//					else{
//						Intent serverIntent = new Intent(context,DeviceListActivity.class);      //运行另外一个类的活动
//		  				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
//					}
//				}
				
				
			}
		});
        
        
		
		
		/*TODO:添加部分*/
		discount_pay = (TextView) v.findViewById(R.id.discount_pay);
		//TODO:支付成功之后的操作
		//TODO：其中折后价格在discount_pay中，支付金额以及支付方式在payment_map中
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(left_to_pay.getText().equals("0.0") && !Double.valueOf(should_pay.getText().toString()).equals(0.0) ){
					Toast.makeText(getActivity(),"支付成功",Toast.LENGTH_SHORT).show();
					
					String[] projection = new String[]{"HeadCount","DeskId"};
					
					String[] selectionargs = new String[] {Global.ordernumber};
					Cursor dinesc = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, projection, "Id=?", selectionargs, null, null, null, null);
					Cart ca = new Cart();
					if(dinesc!=null){
						while(dinesc.moveToNext()){
							ca.setHeadCount(Integer.valueOf(dinesc.getInt(dinesc.getColumnIndex("HeadCount"))));
							ca.setPrice(Float.valueOf(should_pay.getText().toString().trim()));
							Desk de = new Desk(dinesc.getString(dinesc.getColumnIndex("DeskId")));
							ca.setDesk(de);
						}
					}
					PayKind pa = new PayKind(1);   //之后改成对应的paykind
					ca.setPayKind(pa);
					List <OrderedMenus> ors = new ArrayList<OrderedMenus>();
					
					String[] pro = new String[]{"MenuId","_Count"};
					String[] seleargs = new String[]{Global.ordernumber};
					Cursor menusc = dbhelper.query(DBManagerContract.DineMenusTable.TABLE_NAME, pro, "DineId=?", seleargs, null, null, null, null);
					if(menusc!=null){
						while(menusc.moveToNext()){
							OrderedMenus or = new OrderedMenus();
							or.setId(menusc.getString(menusc.getColumnIndex("MenuId")));
							or.setOrdered(menusc.getInt(menusc.getColumnIndex("_Count")));
							or.setRemarks(null);
							ors.add(or);
						}
					}
					ca.setOrderedMenus(ors);
					CartAddition cartadd = new CartAddition();
					cartadd.setUserId(Global.waiterid);
					List<PaidDetail> pas = new ArrayList<PaidDetail>();
					for (String key : payment_map.keySet()) {
						PaidDetail pa1 = new PaidDetail();
						String[] projec = new String[]{"Id"};
						String[] args = new String[]{key};
						Cursor paykindc =  dbhelper.query(DBManagerContract.PayKindsTable.TABLE_NAME,projec, "Name=?", args, null, null, null, null);
						if(paykindc!=null){
							while(paykindc.moveToNext()){
								pa1.setPayKindId(paykindc.getInt(paykindc.getColumnIndex("Id")));
								pa1.setPrice(Double.valueOf(discount_pay.getText().toString().trim()));
								pas.add(pa1);
							}
						}
						
					}
					
					PostMenuWithPay po = new PostMenuWithPay();
					po.setCart(ca);
					po.setCartAddition(cartadd);
					po.setPaidDetails(pas);
					PaidDetails pai = new PaidDetails();
					pai.setDineId(Global.ordernumber);
					pai.setPaidDetails(pas);
					String[] ars  = new String[]{Global.ordernumber,"0"};
					Cursor testc2 = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, null, "Id=? and Status=?", ars, null, null, null, null);
					Gson gsonbuilder = new GsonBuilder().serializeNulls().create();
					if(testc2!=null){
						if(testc2.getCount()==0){
							final String json = gsonbuilder.toJson(po);
							System.out.println("提交为："+json);
							new Thread(new Runnable() {
			                    @Override
			                    public void run() {
			                        PrintWriter out = null;
			                        BufferedReader in = null;
			                        try {
			                        	URL realUrl = new URL("http://ordersystem.yummyonline.net/Payment/WaiterPayWithPaidDetails");
			                            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			                        	//conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			                        	//POST请求头属性，
			                        	conn.setRequestProperty("Content-Type", "application/json");
			                        	//设置POST
			                        	conn.setRequestMethod("POST");
			                        	out = new PrintWriter(conn.getOutputStream());
			                        	// 发送请求参数
			                        	out.print(new String(json.getBytes(), "UTF-8"));
			                        	out.flush();
			                        	
			                        	System.out.println("状态码："+conn.getResponseCode());
			                        	in = new BufferedReader(
			                        				new InputStreamReader(conn.getInputStream()));
					                    String line = "";
					                    String result_Msg = "";
					                    while ((line = in.readLine()) != null) {
					                    	result_Msg += "\n" + line;
					                    }
			                            System.out.println("提交结果："+result_Msg);
			                            //cookie线程已经获取数据可以继续下去
			                        } catch (Exception e) {
			                        } finally {
			                            try {
			                                if (out != null) {
			                                    out.close();
			                                }
			                                if (in != null) {
			                                    in.close();
			                                }
			                            } catch (IOException ex) {
			                            }
			                        }
			                    }
			                }).start();
							
						}
						else{
							final String json = gsonbuilder.toJson(pai);
							System.out.println("提交为："+json);
							new Thread(new Runnable() {
			                    @Override
			                    public void run() {
			                        PrintWriter out = null;
			                        BufferedReader in = null;
			                        try {
			                        	URL realUrl = new URL("http://ordersystem.yummyonline.net/Payment/WaiterPayCompleted");
			                            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			                        	//conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			                        	//POST请求头属性，
			                        	conn.setRequestProperty("Content-Type", "application/json");
			                        	//设置POST
			                        	conn.setRequestMethod("POST");
			                        	out = new PrintWriter(conn.getOutputStream());
			                        	// 发送请求参数
			                        	out.print(new String(json.getBytes(), "UTF-8"));
			                        	out.flush();
			                        	
			                        	System.out.println("状态码："+conn.getResponseCode());
			                        	in = new BufferedReader(
			                        				new InputStreamReader(conn.getInputStream()));
					                    String line = "";
					                    String result_Msg = "";
					                    while ((line = in.readLine()) != null) {
					                    	result_Msg += "\n" + line;
					                    }
			                            System.out.println("提交结果："+result_Msg);
			                            //cookie线程已经获取数据可以继续下去
			                        } catch (Exception e) {
			                        } finally {
			                            try {
			                                if (out != null) {
			                                    out.close();
			                                }
			                                if (in != null) {
			                                    in.close();
			                                }
			                            } catch (IOException ex) {
			                            }
			                        }
			                    }
			                }).start();
							
						}
					}
					/*******************/
					
					Toast.makeText(getActivity(), "提交订单", Toast.LENGTH_LONG).show();
					tv_note1.setText("");
					tv_note2.setText("");
					tv_note3.setText("");
					dishname_in_order.setText("");
					dishnum_in_order.setText("");
					price1_in_order.setText("");
					price2_in_order.setText("");
					num_in_order.setText("");
					plus_in_order.setBackgroundColor(Color.WHITE);
					sub_in_order.setBackgroundColor(Color.WHITE);
					linear_inventory.removeAllViews();
					edi_in_order.setText("");
					
					//testc = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+DeskId+"'", null);
					String selection = "Id=?";	
					String[] selectionArgs = new String[]{Global.ordernumber};
					//Cursor c = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+id+"'", null);
					dbhelper.delete(DBManagerContract.DinesTable.TABLE_NAME, selection, selectionArgs);
					selection = "DineId=?";
					selectionArgs = new String[]{Global.ordernumber};
					dbhelper.delete(DBManagerContract.DineMenusTable.TABLE_NAME, selection, selectionArgs);
					selection = "DeskId=?";
					selectionArgs = new String[]{DeskId};
					Cursor testc = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, null,
							selection, selectionArgs, null, null, null, null);
					if(testc.getCount()==0){
						DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), 1);
						String table1 = DBManagerContract.DesksTable.TABLE_NAME;
						ContentValues values1 = new ContentValues();
						values1.put(DBManagerContract.DesksTable.COLUMN_NAME_Status, 0);
						String selection1 = "Id=?";
						String[] selectionArgs1 = new String[]{DeskId};
						dbHelper.update(table1, values1, selection1, selectionArgs1);
					}

				}
				else if (!Double.valueOf(should_pay.getText().toString()).equals(0.0)){
					Toast.makeText(getActivity(),"支付失败",Toast.LENGTH_SHORT).show();
				}
			}
		});

		should_pay.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				Double d  = 100.0;
				try{
					d = Double.valueOf(discount.getText().toString());
				}catch (Exception e){
					try{
						String ss = discount.getText().toString().substring(0,discount.getText().length()-1);
						d = Double.valueOf(ss.toString());
					}catch (Exception e1){
						e1.printStackTrace();
					}
					e.printStackTrace();
					Log.d(TAG, e.toString());
				}
				Double _t = Double.valueOf(charSequence.toString());
				discount_pay.setText(String.valueOf(_t*d / 100));
				//should_pay.setText(String.valueOf(Global.should_pay*d / 100));
				float left = Float.valueOf(discount_pay.getText().toString().trim())
						-Float.valueOf(already_pay.getText().toString().trim());
				float giveback = -left;
				if(giveback<0){
					giveback = 0;
				}
				if(left<0){
					left = 0;
				}
				left_to_pay.setText(String.valueOf(left));

				give_back.setText(String.valueOf(giveback));
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});



/*		paymentReceiver = new PaymentReceiver();
		IntentFilter payment_filter = new IntentFilter();
		payment_filter.addAction("cn.saltyx.shiyan.paymentAdapter.MONEY_CHANGED");
		getActivity().registerReceiver(paymentReceiver, payment_filter);*/
		lv_payment = (ListView)v.findViewById(R.id.payment_listview);
		payment_map = new HashMap<>();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					payment_map.clear();
					Cursor cursor = dbhelper.query(DBManagerContract.PayKindsTable.TABLE_NAME,
							new String[]{DBManagerContract.PayKindsTable.COLUMN_NAME_Name},null,null,null,null,null,null);
					int tmp_index = 0;
					while(cursor.moveToNext()){
						payment.add(tmp_index++, cursor.getString(0));
						payment_map.put(cursor.getString(0), 0.0);
					}
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lv_payment.setAdapter(new PaymentListView(getActivity(), payment));
						}
					});

					if (tmp_index == 0){
						try{
							Thread.currentThread().sleep(1000);
						}catch (Exception e){
							e.printStackTrace();
						}
					}else break;
				}

			}
		}).start();

		discount.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				Double d  = 100.0;
				try{
					d = Double.valueOf(charSequence.toString());
				}catch (Exception e){
					try{
						charSequence = charSequence.subSequence(0,charSequence.length()-1);
						d = Double.valueOf(charSequence.toString());
					}catch (Exception e1){
						e1.printStackTrace();
					}
					e.printStackTrace();
					Log.d(TAG, e.toString());
				}
				Double _t = Double.valueOf(should_pay.getText().toString());
				discount_pay.setText(String.valueOf(_t*d / 100));
				//should_pay.setText(String.valueOf(Global.should_pay*d / 100));
				float left = Float.valueOf(discount_pay.getText().toString().trim())
						-Float.valueOf(already_pay.getText().toString().trim());
				float giveback = -left;

				if(giveback<0){
					giveback = 0;
				}
				if(left<0){
					left = 0;
				}
				left_to_pay.setText(String.valueOf(left));

				give_back.setText(String.valueOf(giveback));
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		/****************************************/

		try{
            /*一下均为会员打折方案
            * 以及绑定Spiner的方案*/
			JSONObject jsonObject = new JSONObject(discountMethod);
            /*discountMethod是打折方案的json文件*/

			JSONArray timeDiscount = jsonObject.getJSONArray("TimeDiscounts");
			JSONArray vipDiscount = jsonObject.getJSONArray("VipDiscounts");

			//遍历各种打折方法
			allDiscountMethod = new String[timeDiscount.length()+1];//去掉会员打折
			map = new HashMap<>();
			TimeDiscounts[] timeDiscountses = new TimeDiscounts[timeDiscount.length()];
			Integer index = 0;allDiscountMethod[index++] = "自定义折扣";
			for (int i=0; i<timeDiscount.length(); i++){
				//如果当前系统时间不再优惠范围内是不会收到相应信息的
				JSONObject tmpJson = timeDiscount.getJSONObject(i);
				Integer week = tmpJson.getInt("Week");
				String from  = tmpJson.getString("From");
				String to = tmpJson.getString("To");
				Double dicount = tmpJson.getDouble("Discount");
				String name = tmpJson.getString("Name");
				allDiscountMethod[index ++] = name;
				timeDiscountses[i] = new TimeDiscounts();
				timeDiscountses[i].input(from,to,week,dicount,name);
				map.put(name,dicount);
			}
			for (int i =0; i<vipDiscount.length(); i++){
				JSONObject tmpJson = vipDiscount.getJSONObject(i);
				//vipDiscountses[i] = new VipDiscounts();
				Integer id = tmpJson.getInt("Id");
				Double discount = tmpJson.getDouble("Discount");
				String name =  tmpJson.getString("Name");
				//allDiscountMethod[index ++] = name;
				//vipDiscountses[i].input(id,discount,name);
				map.put(name,discount);
			}
			//显示的打折方案
			//此时Spiner适配器

			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item
					, allDiscountMethod);
			arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(arrayAdapter);
            /*spinner点击效果，目前是点了就显示打折数*/
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					try {
						String str = parent.getItemAtPosition(position).toString();
						if(str.equals("自定义折扣")){
							discount.setEnabled(true);
							discount.setText("");
						}else
						{
							discount.setEnabled(false);
							discount.setText(String.valueOf(map.get(str)*100)+"%");
							//discount.setText(String.valueOf((1-map.get(str))*100)+"%");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

		}catch (Exception e){
			e.printStackTrace();
		}
		tv_note1.setText("");
		tv_note2.setText("");
		tv_note3.setText("");

		tv_note1.setTextColor(Color.parseColor("#000000"));
		tv_note2.setTextColor(Color.parseColor("#000000"));
		tv_note3.setTextColor(Color.parseColor("#000000"));


		edi_in_order.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

				DatabaseHelper dbhelper = new DatabaseHelper(getActivity(), 1);
				String tex = edi_in_order.getText().toString().trim();
				String table = DBManagerContract.MenusTable.TABLE_NAME;
				String[] projection =new String [] {"Id","Name","Usable"};
				String selection = "(Id=? or Code=?) ";
				String[] selectionArgs = new String[]{tex,tex};
				Cursor c = dbhelper.query(table, projection, selection, selectionArgs, null, null, null, null);
				if(c!=null){
					while(c.moveToNext()){
						if(c.getString(c.getColumnIndex("Usable"))!=null){
							break;
						}
						plus_in_order.setBackground(getActivity().getResources().getDrawable(R.drawable.plus));
						sub_in_order.setBackground(getActivity().getResources().getDrawable(R.drawable.less));
						String Name = c.getString(c.getColumnIndex("Name"));
						//double Price = c.getDouble(c.getColumnIndex("Price"));
						//String Code = c.getString(c.getColumnIndex("Code"));
						String Id = c.getString(c.getColumnIndex("Id"));
						String table1 = DBManagerContract.MenuPricesTable.TABLE_NAME;
						String[] projection1 =new String [] {"Price"};
						String selection1 = "Id=?";
						String[] selectionArgs1 = new String[]{Id};
						Cursor c2 = dbhelper.query(table1, projection1, selection1, selectionArgs1, null, null, null, null);
						double Price = 0.0;
						if(c2!=null){
							while(c2.moveToNext()){
								Price = c2.getDouble(c2.getColumnIndex("Price"));
							}
						}
						dishname_in_order.setText(Name);
						dishnum_in_order.setText(Id);
						num_in_order.setText("1");
						price2_in_order.setText(String.valueOf(Price));
						price1_in_order.setText("");
						tv_note1.setText("");
						tv_note2.setText("");
						tv_note3.setText("");
					}

				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										  int arg3) {


			}

			@Override
			public void afterTextChanged(Editable arg0) {


			}
		});

		sub_in_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String text = (String) num_in_order.getText();
				if(!text.equals("0")){
					num_in_order.setText(String.valueOf(Integer.valueOf((String) num_in_order.getText())-1));
				}
			}
		});

		plus_in_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				num_in_order.setText(String.valueOf(Integer.valueOf((String) num_in_order.getText())+1));
			}
		});


		add_to_menu.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi") @Override
			public void onClick(View arg0) {
				if(!dishnum_in_order.getText().toString().trim().equals("")){
					LayoutParams params;
					final MenuItem me = new MenuItem(getActivity());
					me.setting(dishnum_in_order.getText().toString().trim(),
							dishname_in_order.getText().toString().trim(),
							tv_note1.getText().toString().trim()+" "+tv_note2.getText().toString().trim()+" "+tv_note3.getText().toString().trim(),
							Double.valueOf(price2_in_order.getText().toString().trim().equals("")? "0" : price2_in_order.getText().toString().trim()),
							Double.valueOf(price1_in_order.getText().toString().trim().equals("")? "0" : price1_in_order.getText().toString().trim()),
							Integer.parseInt(num_in_order.getText().toString().trim().equals("")? "0" : num_in_order.getText().toString().trim()));

					me.setMenuClickListener(new menuClickListerner() {

						@Override
						public void subClick() {

							if(me.getcount()==0){
								linear_inventory.removeView(me);
								String sele = "DinId=? and MenuId=?";
								String[] seleargs = new String[]{Global.ordernumber,me.getdishid()};
								dbhelper.delete(DBManagerContract.DineMenusTable.TABLE_NAME, sele, seleargs);
								//values.clear();
							}
							else{
								ContentValues values = new ContentValues();
								values.put("_Count", me.getcount());
								String sele = "DinId=? and MenuId=?";
								String[] seleargs = new String[]{Global.ordernumber,me.getdishid()};
								dbhelper.update(DBManagerContract.DineMenusTable.TABLE_NAME, values, sele, seleargs);
								values.clear();
							}

							totolprice = 0.0;
							for(int i = 0;i<linear_inventory.getChildCount();i++){
								MenuItem r = (MenuItem) linear_inventory.getChildAt(i);
								totolprice += Double.valueOf(r.getprice()*r.getcount());
							}
							should_pay.setText(String.valueOf(totolprice));
							//already_pay.setText("0");
							double left = Double.valueOf(discount_pay.getText().toString())-Double.valueOf(already_pay.getText().toString().trim());
							double giveback = -left;
							if(giveback<0){
								giveback = 0;
							}
							if(left<0){
								left = 0;
							}
							left_to_pay.setText(String.valueOf(left));

							give_back.setText(String.valueOf(giveback));
							
						}

						@Override
						public void plusClick() {

							ContentValues values = new ContentValues();
							values.put("_Count", me.getcount());
							String sele = "DinId=? and MenuId=?";
							String[] seleargs = new String[]{Global.ordernumber,me.getdishid()};
							dbhelper.update(DBManagerContract.DineMenusTable.TABLE_NAME, values, sele, seleargs);
							values.clear();

							totolprice = 0.0;
							for(int i = 0;i<linear_inventory.getChildCount();i++){
								MenuItem r = (MenuItem) linear_inventory.getChildAt(i);
								totolprice += Double.valueOf(r.getprice()*r.getcount());
							}
							should_pay.setText(String.valueOf(totolprice));
							//already_pay.setText("0");
							double left = Double.valueOf(discount_pay.getText().toString())-Double.valueOf(already_pay.getText().toString().trim());
							double giveback = -left;
							if(giveback<0){
								giveback = 0;
							}
							if(left<0){
								left = 0;
							}
							left_to_pay.setText(String.valueOf(left));

							give_back.setText(String.valueOf(giveback));
						}

						@Override
						public void notesClick() {


							//获得填充的视图
							View view=View.inflate(getActivity(), R.layout.editnote, null);
							//获取组件
							final GridView gv_editNote=(GridView) view.findViewById(R.id.gv_note);
							//gv_editNote.setBackground(Fragment2_order.this.getResources().getDrawable(R.drawable.notes_bt));
							//绑定适配器
							noteSource=new ArrayList<String>();
							//添加数据
							String[] projection = new String[]{"Remark_Id"};
							String selection = "Menu_Id=?";
							String[] selectionArgs = new String[]{me.getdishid()};
							Cursor menuremarksc = dbhelper.query(DBManagerContract.MenuRemarksTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null, null);
							if(menuremarksc!=null){
								while(menuremarksc.moveToNext()){
									int remarkid = menuremarksc.getInt(menuremarksc.getColumnIndex("Remark_Id"));
									String[] projection1= new String[]{"Name"};
									String selection1 = "Id=?";
									String[] selectionArgs1 = new String[]{String.valueOf(remarkid)};
									Cursor remarksc = dbhelper.query(DBManagerContract.RemarksTable.TABLE_NAME, projection1, selection1, selectionArgs1, null, null, null, null);
									if(remarksc!=null){
										while(remarksc.moveToNext()){
											noteSource.add(remarksc.getString(remarksc.getColumnIndex("Name")));
										}
									}
								}
							}

							final NoteGridAdapter adapter0=new NoteGridAdapter(getActivity(), noteSource);
							gv_editNote.setAdapter(adapter0);
							gv_editNote.setSelector(getResources().getDrawable(R.drawable.notes_bt));
							gv_editNote.setId(0);
							//绑定侦听器
							gv_editNote.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

									//是否点击了添加按钮
									if(position==noteSource.size()){
										// 执行添加
									}
									else{
										ColorDrawable colorDrawable=(ColorDrawable) view.getBackground();
										int colorId=colorDrawable.getColor();
										//更改背景颜色，设置备注
										if(colorId==getActivity().getResources().getColor(R.color.Mypink)){
											view.setBackgroundColor(getActivity().getResources().getColor(R.color.Myblue));
											gv_editNote.setId(gv_editNote.getId()-1);
											String notes= "";
											String[] seleargs = new String[]{Global.ordernumber,me.getdishid()};
											dbhelper.delete(DBManagerContract.DineMenuRemarksTable.TABLE_NAME, "tDineMenu_DineId = ? and DineMenu_MenuId=?", seleargs);
											for(int i = 0;i<gv_editNote.getCount();i++){
												View v = parent.getChildAt(i);
												ColorDrawable viewcolor=(ColorDrawable) v.getBackground();
												if(viewcolor.getColor()==getActivity().getResources().getColor(R.color.Mypink)){
													notes += noteSource.get(i)+" ";
													String[] projection = new String[]{"Id"};
													String[] selectionargs = new String[]{noteSource.get(i)};
													Cursor remarksc = dbhelper.query(DBManagerContract.RemarksTable.TABLE_NAME, null, "Name=?", selectionargs, null, null, null, null);
													int remarkid = 0;
													if(remarksc!=null){
														while(remarksc.moveToNext()){
															remarkid = remarksc.getInt(remarksc.getColumnIndex("Id"));
														}
													}
													ContentValues values = new ContentValues();
													values.put("DineMenu_DineId", Global.ordernumber);
													values.put("DineMenu_MenuId", me.getdishid());
													values.put("Remark_Id", remarkid);
													dbhelper.insert(DBManagerContract.DineMenuRemarksTable.TABLE_NAME, values);
												}
											}
											me.setNote(notes);

										}
										else{
											if(gv_editNote.getId()<3){
												view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
												gv_editNote.setId(gv_editNote.getId()+1);
												
												String notes= "";
												String[] seleargs = new String[]{Global.ordernumber,me.getdishid()};
												dbhelper.delete(DBManagerContract.DineMenuRemarksTable.TABLE_NAME, "tDineMenu_DineId = ? and DineMenu_MenuId=?", seleargs);
												for(int i = 0;i<gv_editNote.getCount();i++){
													View v = parent.getChildAt(i);
													ColorDrawable viewcolor=(ColorDrawable) v.getBackground();
													if(viewcolor.getColor()==getActivity().getResources().getColor(R.color.Mypink)){
														notes += noteSource.get(i)+" ";
														String[] projection = new String[]{"Id"};
														String[] selectionargs = new String[]{noteSource.get(i)};
														Cursor remarksc = dbhelper.query(DBManagerContract.RemarksTable.TABLE_NAME, null, "Name=?", selectionargs, null, null, null, null);
														int remarkid = 0;
														if(remarksc!=null){
															while(remarksc.moveToNext()){
																remarkid = remarksc.getInt(remarksc.getColumnIndex("Id"));
															}
														}
														ContentValues values = new ContentValues();
														values.put("DineMenu_DineId", Global.ordernumber);
														values.put("DineMenu_MenuId", me.getdishid());
														values.put("Remark_Id", remarkid);
														dbhelper.insert(DBManagerContract.DineMenuRemarksTable.TABLE_NAME, values);
													}
												}
												me.setNote(notes);
												
											}
										}
									}
								}
							});
							//点击编辑图片跳出popupwindow
							PopupWindow popupWindow=new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ,true);
							Drawable ad=new ColorDrawable(0);
							popupWindow.setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.Myblue)));
							popupWindow.showAsDropDown(me.getnotesbt(), -310, 10);;


						}
					});

					params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,48);
					linear_inventory.addView(me, params);

					String[] pro = new String[]{"Id"};
					String sele = "Id=?";
					String[] selear = new String[]{Global.ordernumber};
					Cursor ctest = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME,pro,sele,selear, null, null, null, null);
					if(ctest!=null){
						if(ctest.getCount()==0){
							formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");// 指定系统时间格式
							curDate = new Date(Long.valueOf(Global.ordernumber));
							str = formatter.format(curDate);

							ContentValues values = new ContentValues();
							values.put("Id", Global.ordernumber);
							values.put("WaiterID",Global.waiterid);
							values.put("HeadCount", 1);
							values.put("DeskId", Global.deskid);
							values.put("BeginTime", str);
							//values.put("RemarkPrice", RemarkPrice);
							//values.put("Status", Status1);
							dbhelper.insert(DBManagerContract.DinesTable.TABLE_NAME, values);
							values.clear();
						}
					}
					try {
						ctest.close();
					} catch (Exception e) {

					}
					ContentValues values = new ContentValues();
					values.put("DineId", Global.ordernumber);
					values.put("MenuId", dishnum_in_order.getText().toString().trim());
					values.put("_Count", Integer.parseInt(num_in_order.getText().toString().trim()));
					values.put("Price", price2_in_order.getText().toString().trim());
					values.put("OriPrice", price1_in_order.getText().toString().trim());
					//values.put("RemarkPrice", RemarkPrice);
					//values.put("Status", Status1);
					dbhelper.insert(DBManagerContract.DineMenusTable.TABLE_NAME, values);
					values.clear();

					totolprice = 0.0;
					for(int i = 0;i<linear_inventory.getChildCount();i++){
						MenuItem r = (MenuItem) linear_inventory.getChildAt(i);
						totolprice += Double.valueOf(r.getprice()*r.getcount());
					}
					should_pay.setText(String.valueOf(totolprice));
					//already_pay.setText("0");
					double left = Double.valueOf(discount_pay.getText().toString())-Double.valueOf(already_pay.getText().toString().trim());
					double giveback = -left;
					if(giveback<0){
						giveback = 0;
					}
					if(left<0){
						left = 0;
					}
					left_to_pay.setText(String.valueOf(left));

					give_back.setText(String.valueOf(giveback));
					//cash.setText("0");
					//credit.setText("0");
				}

			}
		});


		listener=new ClickListener();
		//绑定侦听器
		iv_editNote.setOnClickListener(listener);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(Double.valueOf(should_pay.getText().toString().trim())!=0){
					//TODO::DIALOG 通过GLOBAL传值到DAILOG
					Global.shouldPay = should_pay.getText().toString();
					Global.discountPay = discount_pay.getText().toString();
					Global.alreadyPay = already_pay.getText().toString();
					Global.leftToPay = left_to_pay.getText().toString();
					Global.giveBack = give_back.getText().toString();
					PaymentDialog dialog = new PaymentDialog(getActivity());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();

					//*888888888888888888
					//final String json ="{\"Cart\": {\"HeadCount\": "+linear_inventory.getChildCount()+",\"Price\": "+100+",\"PriceInPoints\": null,\"Invoice\":null,\"Desk\": {\"Id\": \"102\"},\"PayKind\": {\"Id\":1},\"OrderedMenus\":[{\"Id\":10004,\"Ordered\": 2,\"Remarks\":null}]},\"CartAddition\": {\"UserId\": 002}}";

					/*****************/
					//sendinfors
					String[] projection = new String[]{"HeadCount","DeskId"};
					
					String[] selectionargs = new String[] {Global.ordernumber};
					Cursor dinesc = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, projection, "Id=?", selectionargs, null, null, null, null);
					Cart ca = new Cart();
					if(dinesc!=null){
						while(dinesc.moveToNext()){
							ca.setHeadCount(Integer.valueOf(dinesc.getInt(dinesc.getColumnIndex("HeadCount"))));
							ca.setPrice(Float.valueOf(should_pay.getText().toString().trim()));
							Desk de = new Desk(dinesc.getString(dinesc.getColumnIndex("DeskId")));
							ca.setDesk(de);
						}
					}
					PayKind pa = new PayKind(1);   //之后改成对应的paykind
					ca.setPayKind(pa);
					List <OrderedMenus> ors = new ArrayList<OrderedMenus>();
					
					String[] pro = new String[]{"MenuId","_Count"};
					String[] seleargs = new String[]{Global.ordernumber};
					Cursor menusc = dbhelper.query(DBManagerContract.DineMenusTable.TABLE_NAME, pro, "DineId=?", seleargs, null, null, null, null);
					if(menusc!=null){
						while(menusc.moveToNext()){
							OrderedMenus or = new OrderedMenus();
							or.setId(menusc.getString(menusc.getColumnIndex("MenuId")));
							or.setOrdered(menusc.getInt(menusc.getColumnIndex("_Count")));
							or.setRemarks(null);
							ors.add(or);
						}
					}
					ca.setOrderedMenus(ors);
					CartAddition cartadd = new CartAddition();
					cartadd.setUserId(Global.waiterid);
					PostMenu po = new PostMenu();
					po.setCart(ca);
					po.setCartAddition(cartadd);
					Gson gsonbuilder = new GsonBuilder().serializeNulls().create();
					final String json = gsonbuilder.toJson(po);
					System.out.println("递交信息："+json);
					/*******************/
					new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                        PrintWriter out = null;
	                        BufferedReader in = null;
	                        try {
	                            URL realUrl = new URL("http://ordersystem.yummyonline.net/Payment/WaiterPay");
	                            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
	                            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	                            //POST请求头属性，
	                            conn.setRequestProperty("Content-Type", "application/json");
	                            //设置POST
	                            conn.setRequestMethod("POST");
	                            out = new PrintWriter(conn.getOutputStream());
	                            // 发送请求参数
	                            out.print(new String(json.getBytes(), "UTF-8"));
	                            out.flush();

	                            System.out.println("状态码："+conn.getResponseCode());
	                            in = new BufferedReader(
	                                    new InputStreamReader(conn.getInputStream()));
	                            String line = "";
	                            String result_Msg = "";
	                            while ((line = in.readLine()) != null) {
	                                result_Msg += "\n" + line;
	                            }
//	                            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
//	                            //POST请求头属性
//	                            //conn.setRequestProperty("Content-Type", "application/json");
//	                            conn.setRequestProperty("Cookie", cookies);
//	                            System.out.println("cookies为："+cookies);
//	                            conn.setRequestMethod("POST");
//	                            out = new PrintWriter(conn.getOutputStream());
//	                        /*
//	                        * 下面是发送的信息*/
//	                            /*************/
//	                            // 发送请求参数
//	                            out.print(json);
//	                            out.flush();
//	                            System.out.println("状态码："+conn.getResponseCode());
//	                            BufferedReader in2 = new BufferedReader(
//	                                    new InputStreamReader(conn.getErrorStream()));
//	                            String lines2 = "";
//	                            String result_Msg2 = "";
//	                            while ((lines2 = in2.readLine()) != null) {
//	                                result_Msg2 += "\n" + lines2;
//	                            }
//	                            System.out.println("错误信息："+result_Msg2);
//	                            in = new BufferedReader(
//	                                    new InputStreamReader(conn.getInputStream()));
//	                            String lines = "";
//	                            String result_Msg = "";
//	                            while ((lines = in.readLine()) != null) {
//	                                result_Msg += "\n" + lines;
//	                            }
	                            System.out.println("提交结果："+result_Msg);
	                            //cookie线程已经获取数据可以继续下去
	                        } catch (Exception e) {
	                        } finally {
	                            try {
	                                if (out != null) {
	                                    out.close();
	                                }
	                                if (in != null) {
	                                    in.close();
	                                }
	                            } catch (IOException ex) {
	                            }
	                        }
	                    }
	                }).start();
					Toast.makeText(getActivity(), "提交订单", Toast.LENGTH_LONG).show();
					//cash.setText("");
					tv_note1.setText("");
					tv_note2.setText("");
					tv_note3.setText("");
					dishname_in_order.setText("");
					dishnum_in_order.setText("");
					price1_in_order.setText("");
					price2_in_order.setText("");
					num_in_order.setText("");
					plus_in_order.setBackgroundColor(Color.WHITE);
					sub_in_order.setBackgroundColor(Color.WHITE);
					linear_inventory.removeAllViews();
					edi_in_order.setText("");
					
					//testc = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+DeskId+"'", null);
					String selection = "Id=?";	
					String[] selectionArgs = new String[]{Global.ordernumber};
					//Cursor c = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+id+"'", null);
					dbhelper.delete(DBManagerContract.DinesTable.TABLE_NAME, selection, selectionArgs);
					selection = "DineId=?";
					selectionArgs = new String[]{Global.ordernumber};
					dbhelper.delete(DBManagerContract.DineMenusTable.TABLE_NAME, selection, selectionArgs);
					selection = "DeskId=?";
					selectionArgs = new String[]{DeskId};
					Cursor testc = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, null,
							selection, selectionArgs, null, null, null, null);
					if(testc.getCount()==0){
						DatabaseHelper dbHelper = new DatabaseHelper(getActivity(), 1);
						String table1 = DBManagerContract.DesksTable.TABLE_NAME;
						ContentValues values1 = new ContentValues();
						values1.put(DBManagerContract.DesksTable.COLUMN_NAME_Status, 0);
						String selection1 = "Id=?";
						String[] selectionArgs1 = new String[]{DeskId};
						dbHelper.update(table1, values1, selection1, selectionArgs1);
					}

				}
				else {
					Toast.makeText(getActivity(), "无订单", Toast.LENGTH_LONG).show();
				}
//				else if(Double.valueOf(left_to_pay.getText().toString().trim())!=0){
//					Toast.makeText(getActivity(), "订单未完全支付", Toast.LENGTH_LONG).show();
//				}
			}
		});

	}


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
			Toast toast = Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * 该界面下的所有点击侦听事件
	 * @author Administrator
	 *
	 */
	private class ClickListener implements OnClickListener{

		@SuppressLint("NewApi") @Override
		public void onClick(View v) {

			int id=v.getId();
			if(iv_editNote.getId()==id){
				//获得填充的视图
				View view=View.inflate(getActivity(), R.layout.editnote, null);
				//获取组件
				GridView gv_editNote=(GridView) view.findViewById(R.id.gv_note);
				//gv_editNote.setBackground(Fragment2_order.this.getResources().getDrawable(R.drawable.notes_bt));
				//绑定适配器
				gv_editNote.getCheckedItemPositions();

				noteSource=new ArrayList<String>();
				//添加数据

				String[] projection = new String[]{"Remark_Id"};
				String selection = "Menu_Id=?";
				String[] selectionArgs = new String[]{dishnum_in_order.getText().toString().trim()};
				Cursor menuremarksc = dbhelper.query(DBManagerContract.MenuRemarksTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null, null);
				if(menuremarksc!=null){
					while(menuremarksc.moveToNext()){
						int remarkid = menuremarksc.getInt(menuremarksc.getColumnIndex("Remark_Id"));
						String[] projection1= new String[]{"Name"};
						String selection1 = "Id=?";
						String[] selectionArgs1 = new String[]{String.valueOf(remarkid)};
						Cursor remarksc = dbhelper.query(DBManagerContract.RemarksTable.TABLE_NAME, projection1, selection1, selectionArgs1, null, null, null, null);
						if(remarksc!=null){
							while(remarksc.moveToNext()){
								noteSource.add(remarksc.getString(remarksc.getColumnIndex("Name")));
							}
						}
					}
				}
//				noteSource.add("加急");
//				noteSource.add("上菜");
//				noteSource.add("微辣");
//				noteSource.add("中辣");
//				noteSource.add("重辣");
//				noteSource.add("少甜");
//				noteSource.add("免蒜");
//				noteSource.add("免葱");
//				noteSource.add("免生姜");
//				noteSource.add("添加");

				adapter=new NoteGridAdapter(getActivity(), noteSource);
				gv_editNote.setAdapter(adapter);

				//绑定侦听器
				gv_editNote.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						//是否点击了添加按钮
						if(position==noteSource.size()){
							// 执行添加

						}
						else{
							ColorDrawable colorDrawable=(ColorDrawable) view.getBackground();
							int colorId=colorDrawable.getColor();
							//更改背景颜色，设置备注
							if(colorId==getActivity().getResources().getColor(R.color.Mypink)){

								view.setBackgroundColor(getActivity().getResources().getColor(R.color.Myblue));
								if(noteSource.get((int) (id)).equals(tv_note1.getText())){
									tv_note1.setText(tv_note2.getText());
									tv_note2.setText(tv_note3.getText());
									tv_note3.setText("");
								}
								else if(noteSource.get((int) (id)).equals(tv_note2.getText())){
									tv_note2.setText(tv_note3.getText());
									tv_note3.setText("");
								}
								else if (noteSource.get((int) (id)).equals(tv_note3.getText())) {
									tv_note3.setText("");
								}
							}
							else{
								if(noteSource.get((int) (id)).equals(tv_note1.getText())){
									tv_note1.setText(tv_note2.getText());
									tv_note2.setText(tv_note3.getText());
									tv_note3.setText("");
								}
								else if(noteSource.get((int) (id)).equals(tv_note2.getText())){
									tv_note2.setText(tv_note3.getText());
									tv_note3.setText("");
								}
								else if(noteSource.get((int) (id)).equals(tv_note3.getText())){
									tv_note3.setText("");
								}
								else if(tv_note1.getText().equals("")){
									tv_note1.setText(noteSource.get((int) id));
									view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
								}
								else if(tv_note2.getText().equals("")){
									tv_note2.setText(noteSource.get((int) id));
									view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
								}
								else if(tv_note3.getText().equals("")){
									tv_note3.setText(noteSource.get((int) id));
									view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
								}
							}

						}
					}
				});
				//点击编辑图片跳出popupwindow
				PopupWindow popupWindow=new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ,true);
				Drawable ad=new ColorDrawable(0);
				popupWindow.setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.Myblue)));
				popupWindow.showAsDropDown(v, -310, 10);;
			}
		}

	}
	
	
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	  switch (requestCode) {
	          case REQUEST_ENABLE_BT:      //请求打开蓝牙
	              if (resultCode == Activity.RESULT_OK) {   //蓝牙已经打开
	            	Intent serverIntent = new Intent(context,DeviceListActivity.class);      //运行另外一个类的活动
	  				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
	              } else {                 //用户不允许打开蓝牙
	                Toast.makeText(getActivity(), "Refuse to open bluetooth",Toast.LENGTH_LONG).show();
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
	          		Toast.makeText(getActivity(), "connect failed", Toast.LENGTH_LONG).show();
	          	}
	              break;
	    	
	    	
	    	  }
	    }
	 
	
//	 private final  Handler mHandler = new Handler() {
//	        @Override
//	        public void handleMessage(Message msg) {
//	            switch (msg.what) {
//	            case BluetoothService.MESSAGE_STATE_CHANGE:
//	                switch (msg.arg1) {
//	                case BluetoothService.STATE_CONNECTED:   //已连接
//	                	MenuItem en = null;
//	                	String msg1 = "桌号："+DeskId+"    人数："+head_count_spinner.getSelectedItem().toString().trim()+"\n\n";   	
//	                	for(int i=0;i<linear_inventory.getChildCount()-1;i++){
//	                		en = (MenuItem) linear_inventory.getChildAt(i);
//	                		msg1 += en.getdishname()+en.getnotes()+"  "+"x"+en.getcount()+"\n\n";
//	                	}
//	                    mService.sendMessage(msg1+"\n", "GBK");
//	                    if (mService != null) 
//	            			mService.stop();
//	            		mService = null;
//	                    break;
//	                case BluetoothService.STATE_CONNECTING:  //正在连接
//	                	Log.d("蓝牙调试","正在连接.....");
//	                    break;
//	                case BluetoothService.STATE_LISTEN:     //监听连接的到来
//	                case BluetoothService.STATE_NONE:
//	                	Log.d("蓝牙调试","等待连接.....");
//	                    break;
//	                }
//	                break;
//	            case BluetoothService.MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
//	                Toast.makeText(getActivity(), "Device connection was lost",
//	                               Toast.LENGTH_SHORT).show();
//	                if (mService != null) 
//	        			mService.stop();
//	        		mService = null;
//	                break;
//	            case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
//	            	Toast.makeText(getActivity(), "Unable to connect device",
//	                        Toast.LENGTH_SHORT).show();
//	            	if (mService != null) 
//	        			mService.stop();
//	        		mService = null;
//	            	break;
//	            }
//	        }
//	        
//	    };
	    
	   
	    
	    

}