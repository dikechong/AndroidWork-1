package com.qiaoxi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.qiaoxi.adapter.NoteGridAdapter;
import com.qiaoxi.adapter.Orderlistview;
import com.qiaoxi.bean.Global;
import com.qiaoxi.bean.Order;
import com.qiaoxi.shopkeeper.MenuItem;
import com.qiaoxi.shopkeeper.MenuItem.menuClickListerner;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment") public class Fragment1_code extends Fragment {
	private List<Order> list;
	private ListView listView;
	private LinearLayout onlinear;
	private NoteGridAdapter adapter;
	private ImageView onimage;
	private String onordernumber;
	private DatabaseHelper dbhelper;
	private List<String> noteSource;
	@SuppressLint("ValidFragment") public Fragment1_code(List<Order> list) {
		this.list = list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment1_order, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dbhelper = new DatabaseHelper(getActivity(), 1);
		listView = (ListView) getActivity().findViewById(R.id.listview_dingdan);
		listView.setAdapter(new Orderlistview(
				Fragment1_code.this.getActivity(), list));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				LinearLayout layout = (LinearLayout) view
						.findViewById(R.id.ll_on_off);
				ImageView imageView = (ImageView) view
						.findViewById(R.id.iv_on_off);
				if (layout.getVisibility() == LinearLayout.GONE) {
					try {
						onlinear.setVisibility(LinearLayout.GONE);
						onimage.setImageResource(R.drawable.down);
					} catch (Exception e) {
						// TODO: handle exception
					}
					onimage = imageView;
					onlinear = layout;
					TextView ordernumber = (TextView) view.findViewById(R.id.tv_dingdan);
					onordernumber = ordernumber.getText().toString().trim();
					Global.ordernumber = onordernumber;
					LinearLayout ll_on_off = (LinearLayout) view.findViewById(R.id.ll_on_off);
					TextView delete = (TextView) ll_on_off.findViewById(R.id.order_menu_delete);
					delete.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String selection = "DineId=?";
							String[] selectionArgs = new String[]{onordernumber}; 
							dbhelper.delete(DBManagerContract.DineMenusTable.TABLE_NAME, selection, selectionArgs);
							for(int i=0;i<list.size();i++){
								Order o = list.get(i);
								if(String.valueOf(o.getOrdernumber()).equals(onordernumber)){
									list.remove(o);
									listView.setAdapter(new Orderlistview(
			                                    	Fragment1_code.this.getActivity(), list));
									break;
								}
							}
							if(listView.getChildCount()==0){
								Fragment2_order.add_to_menu.setClickable(false);
							}
						}
					});
					
					String[] projection = new String[]{"MenuId","_Count","Price"};
					String selection = "DineId=?";
					String[] selectionArgs = new String[]{onordernumber};
					Cursor c = dbhelper.query(DBManagerContract.DineMenusTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null, null);  
					if(c!=null){	
						Fragment2_order.linear_inventory.removeAllViews();
						if(c.getCount()!=0){
							while(c.moveToNext()){
								String dishid = c.getString(c.getColumnIndex("MenuId"));
								int count = c.getInt(c.getColumnIndex("_Count"));
								double price = c.getDouble(c.getColumnIndex("Price"));
								String dishname = null;
								String[] projection2 = new String[]{"Name"};
								String selection2 = "Id=?";
								String[] selectionArgs2 = new String[]{dishid};
								Cursor c2 = dbhelper.query(DBManagerContract.MenusTable.TABLE_NAME,projection2 , selection2 , selectionArgs2 , null, null, null, null);
								if(c2!=null){
									while(c2.moveToNext()){
										dishname = c2.getString(c2.getColumnIndex("Name"));
									}
								}
								
								final MenuItem me = new MenuItem(getActivity());
								me.setting(dishid, dishname, "", price, 0, count);
								me.setMenuClickListener(new menuClickListerner() {
									
									@Override
									public void subClick() {
										// TODO Auto-generated method stub
										if(me.getcount()==0){
											Fragment2_order.linear_inventory.removeView(me);
											String sele = "DinId=? and MenuId=?";
											String[] seleargs = new String[]{Global.ordernumber,me.getdishid()};
											dbhelper.delete(DBManagerContract.DineMenusTable.TABLE_NAME, sele, seleargs);
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

										//获得填充的视图
										View view=View.inflate(getActivity(), R.layout.editnote, null);
										//获取组件
										GridView gv_editNote=(GridView) view.findViewById(R.id.gv_note);
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
										
										NoteGridAdapter adapter0=new NoteGridAdapter(getActivity(), noteSource);
										gv_editNote.setAdapter(adapter0);
										gv_editNote.setSelector(getResources().getDrawable(R.drawable.notes_bt));
										
										//绑定侦听器
										gv_editNote.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
												// TODO Auto-generated method stub
												//是否点击了添加按钮
												if(position==noteSource.size()){
													// 执行添加
													
												}
//												else{
//													ColorDrawable colorDrawable=(ColorDrawable) view.getBackground();
//													int colorId=colorDrawable.getColor();
//													//更改背景颜色，设置备注
//													if(colorId==getActivity().getResources().getColor(R.color.Mypink)){
//														view.setBackgroundColor(getActivity().getResources().getColor(R.color.Myblue));
//														if(noteSource.get((int) (id)).equals(tv_note1.getText())){
//															tv_note1.setText(tv_note2.getText()); 
//															tv_note2.setText(tv_note3.getText()); 
//															tv_note3.setText("");
//														}
//														else if(noteSource.get((int) (id)).equals(tv_note2.getText())){
//															tv_note2.setText(tv_note3.getText()); 
//															tv_note3.setText("");
//														}
//														else if (noteSource.get((int) (id)).equals(tv_note3.getText())) {
//															tv_note3.setText("");
//														}
//													}
//													else{
//														if(noteSource.get((int) (id)).equals(tv_note1.getText())){
//															tv_note1.setText(tv_note2.getText()); 
//															tv_note2.setText(tv_note3.getText()); 
//															tv_note3.setText("");
//														}
//														else if(noteSource.get((int) (id)).equals(tv_note2.getText())){
//															tv_note2.setText(tv_note3.getText()); 
//															tv_note3.setText("");
//														}
//														else if(noteSource.get((int) (id)).equals(tv_note3.getText())){
//															tv_note3.setText("");
//														}
//														else if(tv_note1.getText().equals("")){
//															tv_note1.setText(noteSource.get((int) id));
//															view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
//														}
//														else if(tv_note2.getText().equals("")){
//															tv_note2.setText(noteSource.get((int) id));
//															view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
//														}
//														else if(tv_note3.getText().equals("")){
//															tv_note3.setText(noteSource.get((int) id));
//															view.setBackgroundColor(getActivity().getResources().getColor(R.color.Mypink));
//														}
//													}
													
//												} 
											}
										});
										//点击编辑图片跳出popupwindow
										PopupWindow popupWindow=new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ,true);
										Drawable ad=new ColorDrawable(0);
										popupWindow.setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.Myblue)));
										popupWindow.showAsDropDown(me.getnotesbt(), -310, 10);;
									
									}
								});
								android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,48);
								Fragment2_order.linear_inventory.addView(me, params);
							}
							double totolprice = 0.0;
							for(int i = 0;i<Fragment2_order.linear_inventory.getChildCount();i++){
								RelativeLayout r = (RelativeLayout) Fragment2_order.linear_inventory.getChildAt(i);
								TextView text = (TextView) r.getChildAt(3);
								totolprice += Double.valueOf(text.getText().toString().trim());			
							}
							Fragment2_order.should_pay.setText(String.valueOf(totolprice));
							//already_pay.setText("0");
							double left = totolprice-Double.valueOf(Fragment2_order.already_pay.getText().toString().trim()); 
							double giveback = -left;
							if(giveback<0){
								giveback = 0;
							}
							if(left<0){
								left = 0;
							}
							Fragment2_order.left_to_pay.setText(String.valueOf(left));			
							
							Fragment2_order.give_back.setText(String.valueOf(giveback));
						}
					}
					layout.setVisibility(LinearLayout.VISIBLE);
					imageView.setImageResource(R.drawable.up);
				} else if (layout.getVisibility() == LinearLayout.VISIBLE) {
					layout.setVisibility(LinearLayout.GONE);
					imageView.setImageResource(R.drawable.down);
				}

			}
		});
	}
}
