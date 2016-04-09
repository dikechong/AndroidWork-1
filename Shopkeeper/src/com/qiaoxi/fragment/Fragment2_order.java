package com.qiaoxi.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qiaoxi.adapter.NoteGridAdapter;
import com.qiaoxi.adapter.PaymentListView;
import com.qiaoxi.bean.Global;
import com.qiaoxi.shopkeeper.LoginActivity;
import com.qiaoxi.shopkeeper.MainActivity;
import com.qiaoxi.shopkeeper.MenuItem;
import com.qiaoxi.shopkeeper.MenuItem.menuClickListerner;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.shopkeeper.LoginActivity.ToastMessageTask;
import com.qiaoxi.shopkeeper.R.drawable;
import com.qiaoxi.shopkeeper.TableItem;
import com.qiaoxi.shopkeeper.TimeDiscounts;
import com.qiaoxi.shopkeeper.VipDiscounts;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

	private TextView tv_note1,tv_note2,tv_note3,num_in_order,dishnum_in_order,
			dishname_in_order,price1_in_order,price2_in_order,confirm,send;
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
	String [] allDiscountMethod;
	Map<String, Double> map;
	Spinner spinner ;
	String cookies,discountMethod;
	DatabaseHelper dbhelper;
	private long systime;
	private Date curDate;
	private String str;
	private SimpleDateFormat formatter;

	public Fragment2_order(String deskid){
		this.DeskId = deskid;
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
		confirm = (TextView) v.findViewById(R.id.confirm);
		should_pay = (TextView) v.findViewById(R.id.should_pay);
		already_pay = (TextView) v.findViewById(R.id.already_pay);
		left_to_pay = (TextView) v.findViewById(R.id.left_to_pay);
//		cash = (EditText) v.findViewById(R.id.cash);
//		credit = (EditText) v.findViewById(R.id.credit);
		get = (TextView) v.findViewById(R.id.get);
		give_back = (TextView) v.findViewById(R.id.give_back);
		discount = (EditText) v.findViewById(R.id.discount); discount.setEnabled(false);//不可编辑
		send = (TextView) v.findViewById(R.id.sendbt);

		get.setVisibility(View.INVISIBLE);
		edi_in_order.setInputType(InputType.TYPE_CLASS_TEXT);
//		cash.setInputType(InputType.TYPE_CLASS_NUMBER);
//		credit.setInputType(InputType.TYPE_CLASS_NUMBER);

		cookies = Global.cookie;
		discountMethod = Global.discount;

		dbhelper = new DatabaseHelper(getActivity(), 1);

		/*添加部分*/
		lv_payment = (ListView)v.findViewById(R.id.payment_listview);

		ArrayList<String> payment = new ArrayList<>();

		Cursor cursor = dbhelper.query(DBManagerContract.PayKindsTable.TABLE_NAME,
				new String[]{DBManagerContract.PayKindsTable.COLUMN_NAME_Name},null,null,null,null,null,null);
		int tmp_index = 0;
		while(cursor.moveToNext()){
			payment.add(tmp_index++, cursor.getString(0));
		}

		lv_payment.setAdapter(new PaymentListView(getActivity(), payment));

		/*****************************************/


//		credit.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				if(!credit.getText().toString().trim().equals("")){
//					double al = Double.valueOf(cash.getText().toString().trim())+Double.valueOf(credit.getText().toString().trim());
//					already_pay.setText(String.valueOf(al));
//					double left = Double.valueOf(should_pay.getText().toString().trim())-Double.valueOf(already_pay.getText().toString().trim());
//					double giveback = -left;
//					if(giveback<0){
//						giveback = 0;
//					}
//					if(left<0){
//						left = 0;
//					}
//					left_to_pay.setText(String.valueOf(left));
//
//					give_back.setText(String.valueOf(giveback));
//
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//										  int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});

//		cash.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				if(!cash.getText().toString().trim().equals("")){
//					double al = Double.valueOf(cash.getText().toString().trim())+Double.valueOf(credit.getText().toString().trim());
//					already_pay.setText(String.valueOf(al));
//					double left = Double.valueOf(should_pay.getText().toString().trim())-Double.valueOf(already_pay.getText().toString().trim());
//					double giveback = -left;
//					if(giveback<0){
//						giveback = 0;
//					}
//					if(left<0){
//						left = 0;
//					}
//					left_to_pay.setText(String.valueOf(left));
//
//					give_back.setText(String.valueOf(giveback));
//
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//										  int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//			}
//		});

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
			Integer index = 0;
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
			allDiscountMethod[index] = "自定义折扣";
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
							discount.setText(String.valueOf((1-map.get(str))*100)+"%");
						}

					} catch (Exception e) {
						e.printStackTrace();
						// new ToastMessageTask().execute(e.toString());
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
				// TODO Auto-generated method stub
				DatabaseHelper dbhelper = new DatabaseHelper(getActivity(), 1);
				String tex = edi_in_order.getText().toString().trim();
				String table = DBManagerContract.MenusTable.TABLE_NAME;
				String[] projection =new String [] {"Id","Name"};
				String selection = "Id=? or Code=?";
				String[] selectionArgs = new String[]{tex,tex};
				Cursor c = dbhelper.query(table, projection, selection, selectionArgs, null, null, null, null);
				if(c.getCount()!=0){
					plus_in_order.setBackground(getActivity().getResources().getDrawable(R.drawable.plus));
					sub_in_order.setBackground(getActivity().getResources().getDrawable(R.drawable.less));
					while(c.moveToNext()){
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
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		sub_in_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String text = (String) num_in_order.getText();
				if(!text.equals("0")){
					num_in_order.setText(String.valueOf(Integer.valueOf((String) num_in_order.getText())-1));
				}
			}
		});

		plus_in_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				num_in_order.setText(String.valueOf(Integer.valueOf((String) num_in_order.getText())+1));
			}
		});


		add_to_menu.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi") @Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
							// TODO Auto-generated method stub
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
						}

						@Override
						public void plusClick() {
							// TODO Auto-generated method stub
							ContentValues values = new ContentValues();
							values.put("_Count", me.getcount());
							String sele = "DinId=? and MenuId=?";
							String[] seleargs = new String[]{Global.ordernumber,me.getdishid()};
							dbhelper.update(DBManagerContract.DineMenusTable.TABLE_NAME, values, sele, seleargs);
							values.clear();
						}

						@Override
						public void notesClick() {
							// TODO Auto-generated method stub

							// TODO Auto-generated method stub

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
							final int checkedcount = 0;
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
											String notes= "";
											for(int i = 0;i<gv_editNote.getCount();i++){
												View v = gv_editNote.getChildAt(i);
												ColorDrawable viewcolor=(ColorDrawable) view.getBackground();
												if(viewcolor.getColor()==getActivity().getResources().getColor(R.color.Mypink)){
													notes += adapter0.getnoteAt(i)+" ";
												}
											}
											me.setNote(notes);

										}
										else{
											if(checkedcount<3){

											}
											view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
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
						// TODO: handle exception
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
						RelativeLayout r = (RelativeLayout) linear_inventory.getChildAt(i);
						TextView text = (TextView) r.getChildAt(3);
						totolprice += Double.valueOf(text.getText().toString().trim());
					}
					should_pay.setText(String.valueOf(totolprice));
					//already_pay.setText("0");
					double left = totolprice-Double.valueOf(already_pay.getText().toString().trim());
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
				// TODO Auto-generated method stub
				if(Double.valueOf(left_to_pay.getText().toString().trim())==0 && (Double.valueOf(should_pay.getText().toString().trim())!=0)){

					final String json ="{\"Cart\": {\"HeadCount\": "+linear_inventory.getChildCount()+",\"Price\": "+100+",\"PriceInPoints\": null,\"Invoice\":null,\"Desk\": {\"Id\": \"102\"},\"PayKind\": {\"Id\":1},\"OrderedMenus\":[{\"Id\":10004,\"Ordered\": 2,\"Remarks\":null}]},\"CartAddition\": {\"UserId\": 002}}";

					new Thread(new Runnable() {
						@Override
						public void run() {
							PrintWriter out = null;
							BufferedReader in = null;
							try {
								URL realUrl = new URL("http://ordersystem.yummyonline.net/Payment/WaiterPay");
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
								out.print(json);
								out.flush();
								in = new BufferedReader(
										new InputStreamReader(conn.getInputStream()));
								String lines = "";
								String result_Msg = "";
								while ((lines = in.readLine()) != null) {
									result_Msg += "\n" + lines;
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
					Cursor testc = MainActivity.db.rawQuery("select * from menutb where DeskId = '"+DeskId+"'", null);
					if(testc.getCount()!=0){
						String[] arg = new String[]{DeskId};
						MainActivity.db.delete("menutb", "DeskId=?", arg);
					}
					else{
						String[] arg = new String[]{DeskId};
						MainActivity.db.delete("localmenutb", "DeskId=?", arg);
					}
					//testc = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+DeskId+"'", null);
					String selection = "DeskId=?";
					String[] selectionArgs = new String[]{DeskId};
					//Cursor c = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+id+"'", null);
					testc = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, null,
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
				else if(Double.valueOf(should_pay.getText().toString().trim())==0){
					Toast.makeText(getActivity(), "无订单", Toast.LENGTH_LONG).show();
				}
				else if(Double.valueOf(left_to_pay.getText().toString().trim())!=0){
					Toast.makeText(getActivity(), "订单未完全支付", Toast.LENGTH_LONG).show();
				}





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
			// TODO Auto-generated method stub
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

}
