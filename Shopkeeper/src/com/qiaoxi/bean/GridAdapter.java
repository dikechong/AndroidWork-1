package com.qiaoxi.bean;

import java.util.ArrayList;
import java.util.Map;

import com.qiaoxi.fragment.Fragment2;
import com.qiaoxi.shopkeeper.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shiyan on 2016/3/20.
 */
public class GridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<String>  name;
    ArrayList<Integer>iconarray;

    public GridAdapter(Context fragment2, ArrayList<String>  name, ArrayList<Integer>iconarray) {
        this.inflater = LayoutInflater.from(fragment2);
        this.name = name;
        this.iconarray = iconarray;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            holder=new ViewHolder();
            convertView=this.inflater.inflate(R.layout.gridview_main, null);
            holder.iv=(ImageView) convertView.findViewById(R.id.grid_iv);
            holder.tv=(TextView) convertView.findViewById(R.id.grid_tv);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder) convertView.getTag();
        }
        if (Global.map.get(name.get(position))){
            //true
            holder.iv.setImageResource(R.drawable.booking);
        }else
        {
            //false
            holder.iv.setImageResource(R.drawable.nobody);
        }

        holder.tv.setText(name.get(position));
        return convertView;
    }
    private class ViewHolder{
        ImageView iv;
        TextView tv;
    }

}


