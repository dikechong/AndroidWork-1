package com.qiaoxi.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qiaoxi.adapter.Gridadapter;
import com.qiaoxi.bean.Global;
import com.qiaoxi.bean.GridAdapter;
import com.qiaoxi.bean.Order;
import com.qiaoxi.shopkeeper.LoginActivity;
import com.qiaoxi.shopkeeper.MainActivity;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.shopkeeper.TableItem;
import com.qiaoxi.shopkeeper.TableItem.tableClickListerner;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import android.R.fraction;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment") public class Fragment2 extends Fragment {
	private View view;
	public static GridLayout gridLayout;
	public static ArrayList<String> arrayList;//字符串
	public static ArrayList<Integer> arrayList1;//图片
	private long systime;
	private Date curDate;
	private String str;
	public static String DeskId = "";
	private SimpleDateFormat formatter;
	private List<Order> list = new ArrayList<Order>();
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment2_main, container, false);
		return view;
		
	}
	
	public Fragment2(Context c) {
		// TODO Auto-generated constructor stub
		context = c;
	}
	
	class GameThread implements Runnable{
		public void run() {
			while (!Thread.currentThread().isInterrupted()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO: handle exception
					Thread.currentThread().interrupt();
				}
				gridLayout.postInvalidate();
			}
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		gridLayout = (GridLayout) view.findViewById(R.id.gridlayout);
		gridLayout.setColumnCount(10);
		String area = " ";
		Cursor desksc = null;
		DatabaseHelper dbhelper = new DatabaseHelper(getActivity(), 1);
		String table = DBManagerContract.DesksTable.TABLE_NAME;
		String[] projection =new String [] {"Id","Status"};
		//String selection = "AreaId = ?";
		//String[] selectionArgs = new String[]{area};
		//String orderby = "Order";
		desksc = dbhelper.query(table, projection, null, null, null, null, null, null);
		if(desksc!=null){
			while(desksc.moveToNext()){
				String id = desksc.getString(desksc.getColumnIndex("Id"));
			    int status = desksc.getInt(desksc.getColumnIndex("Status"));
			    final TableItem ta = new TableItem(getActivity());
			    ta.setTable(id);
			    ta.setiamgeBackground(status);
			    String[] projection1 =new String [] {"Id"};
			    String selection = "DeskId=?";
			    String[] selectionArgs = new String[]{id};
				//Cursor c = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+id+"'", null);
				Cursor c = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, projection1, 
						selection, selectionArgs, null, null, null, null);
			    if(c.getCount()!=0){
			    	ta.setiamgeBackground(1);
			    }
			    ta.setTableClickListener(new tableClickListerner() {
					
					public void imageClick() {
						// TODO Auto-generated method stub
						String s = ta.getTableNum();
						DeskId = ta.getTableNum();
						Global.deskid = DeskId;
						formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");// 指定系统时间格式
						systime = System.currentTimeMillis();
						curDate = new Date(systime);
						str = formatter.format(curDate);// 把获得的系统时间格式化称自己指定的
						Order order = new Order();
						order.setNumber(s);
						order.setOrdernumber(systime);
						order.setSystime(str);
						order.setWaiterid(Global.waiterid);
						order.setClerkid("");
						order.setHeadcount("1");
						list.add(order);
						Global.ordernumber = String.valueOf(systime);
						
						
						DatabaseHelper dbDatabaseHelper = new DatabaseHelper(getActivity(), 1);
						String table = DBManagerContract.DinesTable.TABLE_NAME;
						String[] projection1 = new String[] {"Id","BeginTime","DeskId","ClerkID","WaiterID","HeadCount"};
						String selection = "DeskId=?";
						String[] selectionArgs = new String[]{DeskId};
						Cursor c = dbDatabaseHelper.query(table, projection1, selection, selectionArgs, null, null, null, null);
						if(c!=null){
							while(c.moveToNext()){
								Order order2 = new Order();
								formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");// 指定系统时间格式
								str = c.getString(c.getColumnIndex("BeginTime"));
								Date date = null;
								try {
									date = formatter.parse(str);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//curDate = new Date(systime);
								//str = formatter.format(curDate);// 把获得的系统时间格式化称自己指定的
								try {
									str = formatter.format(date);
								} catch (Exception e) {
									// TODO: handle exception
								}

								order2.setNumber(c.getString(c.getColumnIndex("DeskId")));
								order2.setSystime(str);
								order2.setOrdernumber(Long.parseLong(c.getString(c.getColumnIndex("Id"))));
								order2.setHeadcount(c.getString(c.getColumnIndex("HeadCount")));
								order2.setWaiterid(c.getString(c.getColumnIndex("WaiterID")));
								order2.setClerkid(c.getString(c.getColumnIndex("ClerkID")));

								list.add(order2);
							}
						}
						// Toast.makeText(Fragment2.this.getActivity(), str + systime,
						// 0)
						// .show();
						getActivity().getFragmentManager().beginTransaction()
								.replace(R.id.fragment1,Global.fragment1 =  new Fragment1_code(list))
								.commit();
						getActivity().getFragmentManager().beginTransaction()
								.replace(R.id.fragment2,Global.fragment2 =  new Fragment2_order(DeskId,context))
								.commit();
					}
				});
			    gridLayout.addView(ta);
			    
			}
		}
		desksc.close();
		
		
		new Thread(new GameThread()).start();
	
//		class GameThread implements Runnable {
//			　　public void run() {
//			　　　　while (!Thread.currentThread().isInterrupted()) {
//			　　　　　　try {
//			　　　　　　　　Thread.sleep(100);
//			　　　　　　} catch (InterruptedException e) {
//			　　　　　　　　Thread.currentThread().interrupt();
//			　　　　　　}
//			 
//			　　　　　　// 使用postInvalidate可以直接在线程中更新界面
//			　　　　　gridLayout.postInvalidate();
//			　　　　}
//			　　}
//			}
		
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				textView = (TextView) view.findViewById(R.id.grid_tv);
//				String s = (String) textView.getText();
//				formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");// 指定系统时间格式
//				systime = System.currentTimeMillis();
//				curDate = new Date(systime);// 获取当前时间
//				str = formatter.format(curDate);// 把获得的系统时间格式化称自己指定的
//				Order order = new Order();
//				order.setNumber(s);
//				order.setOrdernumber(systime);
//				order.setSystime(str);
//				list.add(order);
//				// Toast.makeText(Fragment2.this.getActivity(), str + systime,
//				// 0)
//				// .show();
//				getActivity().getFragmentManager().beginTransaction()
//						.replace(R.id.fragment1, new Fragment1_code(list))
//						.commit();
//				getActivity().getFragmentManager().beginTransaction()
//						.replace(R.id.fragment2, new Fragment2_order())
//						.commit();
//			}
//		});
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				while(true){
//					try {
//						Thread.currentThread().wait(10000);
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//				}
//				
//			}
//		}).start();
	}
}
