package com.qiaoxi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.qiaoxi.bean.Dish;
import com.qiaoxi.shopkeeper.R;

import android.R.color;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
		//添加数据
		dlSource.add("冷菜");
		dlSource.add("热菜");
		dlSource.add("汤");
		dlSource.add("海鲜");
		xlSource.add("百味家常菜");
		xlSource.add("精致陕西菜");
		xlSource.add("田园时蔬");
		xlSource.add("窑洞推荐");
		dishList.add(new Dish(1001, "东坡肉", "dpr", "/images/01.jpg	", "份", 80, 60, 0, 0.8, "A区"));
		dishList.add(new Dish(1002, "糖醋排骨", "tcpg", "/images/01.jpg	", "份", 80, 60, 0, 0.8, "B区"));
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
				holder.useable=(ImageView) convertView.findViewById(R.id.iv_ky);
				//设置标签
				convertView.setTag(holder);
			}
			else{
				holder=(ViewHolder) convertView.getTag();
			}
			//设置数据
			holder.sequenceNum.setText(""+0);
			holder.dishId.setText(dishList.get(position).getDishId()+"");
			holder.dishName.setText(dishList.get(position).getDishName()+" "+dishList.get(position).getAbbrevation());
			holder.imageUrl.setText(dishList.get(position).getImageUrl());
			holder.unit.setText(dishList.get(position).getPrice()+"/"+dishList.get(position).getClientPrice()+"("+dishList.get(position).getUnit()+")");
			holder.serverFee.setText(dishList.get(position).getServerPrice()+"");
			holder.discount.setText(dishList.get(position).getDiscount()+"");
			holder.printDepartment.setText(dishList.get(position).getPrintDepartment());
			holder.edit.setImageResource(R.drawable.note_on);
			holder.delete.setImageResource(R.drawable.del_on2);
			holder.useable.setImageResource(R.drawable.choice_on);
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
			private ImageView  useable;
		}
	}
}
