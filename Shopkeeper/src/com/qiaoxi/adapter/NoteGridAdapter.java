package com.qiaoxi.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.qiaoxi.shopkeeper.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteGridAdapter extends BaseAdapter{
	private Context context;
	private List<String> listNote;
	private LayoutInflater inflater;
	private AbsListView.LayoutParams params;
	public NoteGridAdapter(Context context,List<String> listNote) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.listNote=listNote;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listNote.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listNote.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public String getnoteAt(int position){
		return listNote.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder holder;
		if(convertView==null){
			
			convertView=new TextView(context);
			params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			holder=new viewHolder();
			holder.tv_Note=(TextView) convertView;
			
			convertView.setTag(holder);
		}
		else{
			holder=(viewHolder) convertView.getTag();
		}
		holder.tv_Note.setLayoutParams(params);
		holder.tv_Note.setBackgroundColor(context.getResources().getColor(R.color.Myblue));
		holder.tv_Note.setText(listNote.get(position));
		holder.tv_Note.setTextColor(Color.WHITE);
		return convertView;
	}

	private class viewHolder{
		private TextView tv_Note;
	}

}
