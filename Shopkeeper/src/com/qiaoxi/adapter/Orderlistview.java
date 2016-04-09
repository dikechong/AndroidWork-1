package com.qiaoxi.adapter;

import java.util.List;

import com.qiaoxi.bean.Order;
import com.qiaoxi.shopkeeper.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Orderlistview extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Order> list;

	public Orderlistview(Context context, List<Order> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHelper helper;
		if (convertView == null) {
			helper = new ViewHelper();
			convertView = inflater.inflate(R.layout.ordernumber_listview, null);
			helper.tv_dingdan = (TextView) convertView
					.findViewById(R.id.tv_dingdan);
			helper.tv_number = (TextView) convertView
					.findViewById(R.id.tv_number);
			helper.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(helper);
		} else {
			helper = (ViewHelper) convertView.getTag();
		}
		helper.tv_dingdan.setText(list.get(position).getOrdernumber() + "");
		helper.tv_number.setText(list.get(position).getNumber());
		helper.tv_time.setText(list.get(position).getSystime());
		return convertView;
	}

	class ViewHelper {
		TextView tv_dingdan, tv_time, tv_number;
	}
}
