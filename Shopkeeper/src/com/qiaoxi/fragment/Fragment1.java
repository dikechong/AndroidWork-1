package com.qiaoxi.fragment;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.RandomAccess;

import com.qiaoxi.bean.Global;
import com.qiaoxi.bean.Order;
import com.qiaoxi.shopkeeper.MainActivity;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.shopkeeper.TableItem;
import com.qiaoxi.shopkeeper.TableItem.tableClickListerner;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;

public class Fragment1 extends Fragment {
	private RadioButton radio_qb,radio_yl,radio_rl,radio_sl,radio_wm;
	private RadioGroup rg;
	private String str;
	private SimpleDateFormat formatter;
	private List<Order> list = new ArrayList<Order>();
	private long systime;
	private Date curDate;
	DatabaseHelper dbhelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment1_main, container, false);
		return view;
	}

	@SuppressLint("NewApi") @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		radio_qb = (RadioButton) getActivity().findViewById(R.id.radio_qb);
//		radio_yl = (RadioButton) getActivity().findViewById(R.id.radio_yl);
//		radio_rl = (RadioButton) getActivity().findViewById(R.id.radio_rl);
//		radio_sl = (RadioButton) getActivity().findViewById(R.id.radio_sl);
//		radio_wm = (RadioButton) getActivity().findViewById(R.id.radio_wm);
//		radio_qb.setId(0);
//		radio_yl.setId(1);
//		radio_rl.setId(2);
//		radio_sl.setId(3);
//		radio_wm.setId(4);
		rg = (RadioGroup) getActivity().findViewById(R.id.rg);

		RadioButton radiobt = new RadioButton(getActivity());
		radiobt.setText("全部");
		radiobt.setTextColor(Color.parseColor("#33a9e7"));
		radiobt.setBackground(getActivity().getResources().getDrawable(R.drawable.radio0));
		radiobt.setGravity(Gravity.CENTER);
		radiobt.setId(0);
		radiobt.setButtonDrawable(android.R.color.transparent);
		radiobt.setChecked(true);
		LayoutParams params = new LayoutParams(200, 50);
		rg.addView(radiobt,params);

		//while(Global.areasaved==false);

		dbhelper = new DatabaseHelper(getActivity(), 1);
		String table = DBManagerContract.AreasTable.TABLE_NAME;
		String[] projection = new String[]{"Id","Name"} ;
		Global.areamap.clear();
		Global.areamapback.clear();
		Cursor c = dbhelper.query(table, projection, null, null, null, null, null, null);
		if(c!=null){
			int countid = 1;
			while(c.moveToNext()){
				String AreaId = c.getString(c.getColumnIndex("id"));
				String name = c.getString(c.getColumnIndex("name"));
				Global.areamap.put(AreaId,String.valueOf(countid));
				Global.areamapback.put(String.valueOf(countid++),AreaId);
				String Id = Global.areamap.get(AreaId);
				RadioButton radiobt1 = new RadioButton(getActivity());
				radiobt1.setText(name);
				radiobt1.setTextColor(Color.parseColor("#33a9e7"));
				radiobt1.setBackground(getActivity().getResources().getDrawable(R.drawable.radio0));
				radiobt1.setGravity(Gravity.CENTER);
				radiobt1.setId(Integer.valueOf(Id));
				radiobt1.setButtonDrawable(android.R.color.transparent);
				params = new LayoutParams(200,50);
				rg.addView(radiobt1,params);
			}
		}
		Global.frag1checkedid = 0;
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				try {
					Fragment2.gridLayout.removeAllViews();
				} catch (Exception e) {
					// TODO: handle exception
				}
				Global.frag1checkedid = arg1;
				String area = " ";
				Cursor desksc = null;
				String table = DBManagerContract.DesksTable.TABLE_NAME;
				String[] projection =new String [] {"Id","Status"};
				DatabaseHelper dbhelper = new DatabaseHelper(getActivity(), 1);
				switch (Global.frag1checkedid) {
					case 0:
						//String selection = "AreaId = ?";
						//String[] selectionArgs = new String[]{area};
						//String orderby = "Order";
						desksc = dbhelper.query(table, projection, null, null, null, null, null, null);
						break;
					default:
						area = Global.areamapback.get(String.valueOf(Global.frag1checkedid));
						String selection1 = "AreaId = ?";
						String[] selectionArgs1 = new String[]{area};
						//String orderby = "Order";
						desksc = dbhelper.query(table, projection, selection1, selectionArgs1, null, null, null, null);
						break;
				}
				if(desksc!=null){
					while(desksc.moveToNext()){
						String id = desksc.getString(desksc.getColumnIndex("Id"));
						int status = desksc.getInt(desksc.getColumnIndex("Status"));
						final TableItem ta = new TableItem(getActivity());
						ta.setTable(id);
						ta.setiamgeBackground(status);
						String selection = "DeskId=?";
						String[] selectionArgs = new String[]{id};
						Cursor c = dbhelper.query(DBManagerContract.DinesTable.TABLE_NAME, null,
								selection, selectionArgs, null, null, null, null);
						//Cursor c = MainActivity.db.rawQuery("select * from localmenutb where DeskId = '"+id+"'", null);
						if(c.getCount()!=0){
							ta.setiamgeBackground(1);
						}
						ta.setTableClickListener(new tableClickListerner() {

							public void imageClick() {
								// TODO Auto-generated method stub
								String s = ta.getTableNum();
								Fragment2.DeskId = ta.getTableNum();
								formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");// 指定系统时间格式
								systime = System.currentTimeMillis();
								curDate = new Date(systime);
								str = formatter.format(curDate);// 把获得的系统时间格式化称自己指定的
								Order order = new Order();
								order.setNumber(s);
								order.setOrdernumber(systime);
								order.setSystime(str);
								list.add(order);
								// Toast.makeText(Fragment2.this.getActivity(), str + systime,
								// 0)
								// .show();
								getActivity().getFragmentManager().beginTransaction()
										.replace(R.id.fragment1, new Fragment1_code(list))
										.commit();
								getActivity().getFragmentManager().beginTransaction()
										.replace(R.id.fragment2, new Fragment2_order(Fragment2.DeskId))
										.commit();
							}
						});
						Fragment2.gridLayout.addView(ta);

					}
				}
				try {
					desksc.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

	}
}