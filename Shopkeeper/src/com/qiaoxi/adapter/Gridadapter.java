package com.qiaoxi.adapter;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiaoxi.shopkeeper.R;

public class Gridadapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int[] arr;
	private int[] arr1;

	public Gridadapter(Context context, int[] arr, int[] arr1) {
		inflater = LayoutInflater.from(context);
		this.arr = arr;
		this.arr1 = arr1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr[position];
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
			convertView = inflater.inflate(R.layout.gridview_main, null);
			helper.imageView = (ImageView) convertView
					.findViewById(R.id.grid_iv);
			helper.textView = (TextView) convertView.findViewById(R.id.grid_tv);
			convertView.setTag(helper);
		} else {
			helper = (ViewHelper) convertView.getTag();
		}
		helper.imageView.setImageResource(arr[position]);
		helper.textView.setText(arr1[position] + "");
		return convertView;

	}

	class ViewHelper {
		ImageView imageView;
		TextView textView;
	}
}
