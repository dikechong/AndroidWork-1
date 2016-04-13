package com.qiaoxi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.qiaoxi.bean.Dish;
import com.qiaoxi.shopkeeper.R;
import com.qiaoxi.sqlite.DBManagerContract;
import com.qiaoxi.sqlite.DatabaseHelper;

import android.R.color;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMenuContent extends Fragment {
	//大类的按钮
	private Spinner sp_dl;
	//小类的按钮
	private Spinner sp_xl;
	//大类适配器
	private SpinnerAdapter dlAdapter;
	//小类适配器
	private SpinnerAdapter xlAdapter;
	//大类资源集合
	ArrayList<String> dlSource;
	//小类资源集合
	ArrayList<String> xlSource;
	//设置菜品的listview
	private ListView lv_setting;
	//要加载的视图
	private View v;
	//菜品信息的集合
	private List<Dish> dishList;
	//设置菜品的listview的适配器
	private DishSettingAdapter dishListAdapter;
	//获取父容器
	private LinearLayout ll_menu_content;

	private DatabaseHelper dbhelper;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v=inflater.inflate(R.layout.fragment_menu_content, container, false);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init(v);
	}
	/**
	 * 初始化数据
	 */
	private void init(View v){
		dbhelper = new DatabaseHelper(getActivity(), 1);
		//获取大类的按钮
		sp_dl=(Spinner) v.findViewById(R.id.sp_dl);
		//获取小类按按钮
		sp_xl=(Spinner) v.findViewById(R.id.sp_xl);
		//获取listviewsetting
		lv_setting=(ListView) v.findViewById(R.id.lv_dishSetting);
		//获取父容器
		ll_menu_content=(LinearLayout) v.findViewById(R.id.ll_menu_content);
		//初始化菜品信息的集合
		dishList=new ArrayList<Dish>();
		//初始化集合
		dlSource=new ArrayList<String>();
		xlSource=new ArrayList<String>();

		dlSource.add("全部");
		//添加数据
		Cursor cursor = dbhelper.query(DBManagerContract.MenuClassesTable.TABLE_NAME,
				new String[]{DBManagerContract.MenuClassesTable.COLUMN_NAME_Name},null,null,null,null,null,null);
		while (cursor.moveToNext()){
			dlSource.add(cursor.getString(0));
		}
		cursor.close();

		xlSource.add("全部");
		try{
			cursor = dbhelper.query("select  Menus.Id, Menus.code,  Menus.name,menus.unit,MenuPrices.price, menus.Usable\n" +
					"\tfrom menus, MenuPrices where menus.id = MenuPrices.id");
			while (cursor.moveToNext()){
				dishList.add(new Dish(cursor.getString(0), cursor.getString(2) == "" ? "无" :cursor.getString(2) ,
						cursor.getString(1), cursor.getString(3), cursor.getDouble(4),cursor.getDouble(4), cursor.getString(5) == null ? "true" : "false"));
			}
			cursor.close();
		}catch (Exception e){
			Log.d("WTF", e.toString());
		}


		//初始化适配器
		dlAdapter=new SpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, dlSource);
		xlAdapter=new SpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, xlSource);
		dishListAdapter=new DishSettingAdapter();
		//获取view视图
		View add_footer=View.inflate(getActivity(), R.layout.listview_footer_adddish, null);
		//添加至listview当中
		lv_setting.addFooterView(add_footer);
		//绑定适配器
		sp_dl.setAdapter(dlAdapter);
		sp_xl.setAdapter(xlAdapter);
		lv_setting.setAdapter(dishListAdapter);
	}
	/**
	 * 自定义的spinner适配器类
	 * @author Administrator
	 *
	 */
	private class SpinnerAdapter extends ArrayAdapter<String>{

		private Context context;
		private List<String> items;
		public SpinnerAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
			this.context=context;
			this.items=objects;
		}
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView=LayoutInflater.from(context).
						inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

			}
			TextView tv=(TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(items.get(position));
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(16);
			return convertView;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=LayoutInflater.from(context).
						inflate(android.R.layout.simple_dropdown_item_1line, parent, false);

			}
			TextView tv=(TextView) convertView.findViewById(android.R.id.text1);
			tv.setText(items.get(position));
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(16);
			if (!items.get(position).equals("全部")){
				dishList.clear();
				try{
					Cursor cursor = dbhelper.query(String.format(("select  Menus.Id, Menus.code,  Menus.name,menus.unit,MenuPrices.price, menus.Usable\n" +
							"\tfrom menus, MenuPrices,MenuClasses,menuClassMenus\n" +
							"\twhere menus.id = MenuPrices.id \n" +
							"\t\tand menuClassMenus.Menu_id = menus.id\n" +
							"\t\tand menuClassMenus.MenuClass_Id = MenuClasses.id\n" +
							"\t\tand MenuClasses.Name = '%s'"),items.get(position) ));
					while (cursor.moveToNext()){
						dishList.add(new Dish(cursor.getString(0), cursor.getString(2) == "" ? "无" :cursor.getString(2) ,
								cursor.getString(1), cursor.getString(3), cursor.getDouble(4),cursor.getDouble(4), cursor.getString(5) == null ? "true" : "false"));
					}
					cursor.close();
					dishListAdapter=new DishSettingAdapter();lv_setting.setAdapter(dishListAdapter);
				}catch (Exception e){
					Log.d("WTF", e.toString());
				}
			}else{
				dishList.clear();
				try{
					Cursor cursor = dbhelper.query("select  Menus.Id, Menus.code,  Menus.name,menus.unit,MenuPrices.price, menus.Usable\n" +
							"\tfrom menus, MenuPrices where menus.id = MenuPrices.id");
					while (cursor.moveToNext()){
						dishList.add(new Dish(cursor.getString(0), cursor.getString(2) == "" ? "无" :cursor.getString(2) ,
								cursor.getString(1), cursor.getString(3), cursor.getDouble(4),cursor.getDouble(4), cursor.getString(5) == null ? "true" : "false"));
					}
					cursor.close();
				}catch (Exception e){
					Log.d("WTF", e.toString());
				}
			}
			return convertView;
		}
	}
	/**
	 * 菜品设置的适配器类
	 * @author Administrator
	 *
	 */
	private class DishSettingAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dishList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder=null;
			if(convertView==null){
				holder=new ViewHolder();
				convertView=View.inflate(getActivity(), R.layout.dish_liseview_item, null);
				holder.sequenceNum=(TextView) convertView.findViewById(R.id.tv_bh);
				holder.dishId=(TextView) convertView.findViewById(R.id.tv_cpbh);
				holder.dishName=(TextView) convertView.findViewById(R.id.tv_cm);
				holder.imageUrl=(TextView) convertView.findViewById(R.id.tv_tplj);
				holder.unit=(TextView) convertView.findViewById(R.id.tv_dw);
				holder.serverFee=(TextView) convertView.findViewById(R.id.tv_fwf);
				holder.discount=(TextView) convertView.findViewById(R.id.tv_zkl);
				holder.printDepartment=(TextView) convertView.findViewById(R.id.tv_cdbm);
				holder.edit=(ImageView) convertView.findViewById(R.id.iv_bj);
				holder.delete=(ImageView) convertView.findViewById(R.id.iv_sc);
				holder.useable=(Button) convertView.findViewById(R.id.iv_ky);
				//设置标签
				convertView.setTag(holder);
			}
			else{
				holder=(ViewHolder) convertView.getTag();
			}
			//设置数据
			holder.sequenceNum.setText(String.valueOf(position+1));
			holder.dishId.setText(dishList.get(position).getDishId()+"");
			holder.dishName.setText(dishList.get(position).getDishName()+" "+dishList.get(position).getAbbrevation());
			holder.imageUrl.setText(dishList.get(position).getImageUrl() == "" ? "无" : dishList.get(position).getImageUrl());
			holder.unit.setText(dishList.get(position).getPrice()+"/"+dishList.get(position).getClientPrice()+"("+dishList.get(position).getUnit()+")");
			holder.serverFee.setText(dishList.get(position).getServerPrice()+"");
			holder.discount.setText(dishList.get(position).getDiscount()+"");
			holder.printDepartment.setText(dishList.get(position).getPrintDepartment());
			holder.edit.setImageResource(R.drawable.note_on);
			holder.delete.setImageResource(R.drawable.del_on2);

			if (dishList.get(position).isuable() == "true"){
				holder.useable.setBackgroundResource(R.drawable.choice_on);
			}else{
				holder.useable.setBackgroundResource(R.drawable.choice);
			}

			holder.useable.setTag(dishList.get(position).getDishId());
			holder.useable.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					view.setBackgroundResource(R.drawable.choice);
					ContentValues contentValues = new ContentValues();
					contentValues.put(DBManagerContract.MenusTable.COLUMN_NAME_Id, view.getTag().toString());
					contentValues.put(DBManagerContract.MenusTable.COLUMN_NAME_Usable, "false");
					dbhelper.update(DBManagerContract.MenusTable.TABLE_NAME,contentValues,DBManagerContract.MenusTable.COLUMN_NAME_Id+"=?"
							,new String[]{view.getTag().toString()});

				}
			});
			return convertView;
		}
		private class ViewHolder{
			private TextView sequenceNum;
			private TextView dishId;
			private TextView dishName;
			private TextView imageUrl;
			private TextView unit;
			private TextView serverFee;
			private TextView discount;
			private TextView printDepartment;
			private ImageView edit;
			private ImageView delete;
			private Button  useable;
		}
	}
}