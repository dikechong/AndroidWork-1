package com.qiaoxi.shopkeeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.qiaoxi.shopkeeper.TableItem.tableClickListerner;

public class MenuItem extends RelativeLayout {

	private LayoutParams params;
	private TextView dishnum;
	private Button notes;
	private TextView notestext;
	private TextView dishname;
	private TextView price1;
	private TextView price2;
	private TextView num;
	private Button sub;
	private Button plus;
	private String name,dishid,note;
	private int count;
	private double price,oriprice;
	
	public interface menuClickListerner
	{
		public void subClick();
		public void plusClick();
		public void notesClick();
	}
	
    private menuClickListerner listerner;
	
	
	public void setMenuClickListener(menuClickListerner listener)
	{
		this.listerner = listener;
	}
	
	@SuppressLint("NewApi") public MenuItem(Context context){
		super(context);
		
		dishnum = new TextView(context);
		notes = new Button(context);
		notestext = new TextView(context);
		dishname = new TextView(context);
		price1 = new TextView(context);
		price2 = new TextView(context);
		num = new TextView(context);
		sub = new Button(context);
		plus = new Button(context);
		
		dishid = "";
		name = "";
		count = 0;
		price = 0;
		oriprice = 0;
		note = "";

		sub.setBackgroundColor(Color.WHITE);
		plus.setBackgroundColor(Color.WHITE);
		dishname.setEms(7);
		
		dishnum.setText(dishid);
		dishnum.setTextColor(Color.parseColor("#000000"));
		dishnum.setTextSize(13);
		dishnum.setId(1);
		params = new LayoutParams(50,ViewGroup.LayoutParams.WRAP_CONTENT);
		params.leftMargin = 10;
		params.topMargin = 10;
		addView(dishnum, params);
		
		dishname.setText(name);
		dishname.setTextColor(Color.parseColor("#000000"));
		dishname.setTextSize(13);
		dishname.setId(2);
		params = new LayoutParams(100,21);
		params.leftMargin = 70;
		params.topMargin = 10;
		addView(dishname, params);
		
		price1.setText(String.valueOf(oriprice));
		price1.setTextColor(Color.parseColor("#000000"));
		price1.setTextSize(13);
		price1.setId(3);
		params = new LayoutParams(50,ViewGroup.LayoutParams.WRAP_CONTENT);
		params.leftMargin = 140;
		params.topMargin = 10;
		addView(price1, params);
		
		price2.setText(String.valueOf(price));
		price2.setTextColor(Color.parseColor("#000000"));
		price2.setTextSize(13);
		price2.setId(4);
		params = new LayoutParams(50,ViewGroup.LayoutParams.WRAP_CONTENT);
		params.leftMargin = 188;
		params.topMargin = 10;
		addView(price2, params);
		
		notes.setBackground(context.getResources().getDrawable(R.drawable.del));
		notes.setId(5);
		notes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listerner.notesClick();
			}
		});
		params = new LayoutParams(30,30);
		params.leftMargin = 270;
		params.topMargin = 10;
		addView(notes, params);
		
		sub.setBackground(context.getResources().getDrawable(R.drawable.less));
		sub.setId(6);
		sub.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String text = (String) num.getText();
				if(!text.equals("0")){
					num.setText(String.valueOf(Integer.valueOf((String) num.getText())-1));
					count = Integer.valueOf((String) num.getText());
				}
				listerner.subClick();
			}
		});
		
		params = new LayoutParams(28,28);
		params.leftMargin = 330;
		params.topMargin = 10;
		addView(sub, params);
		
		num.setText(String.valueOf(count));
		num.setTextColor(Color.parseColor("#000000"));
		num.setTextSize(13);
		num.setId(7);
		params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,20);
		params.leftMargin = 365;
		params.topMargin = 12;
		addView(num, params);
		
		
		plus.setBackground(context.getResources().getDrawable(R.drawable.plus));
		plus.setId(8);
		plus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				num.setText(String.valueOf(Integer.valueOf((String) num.getText())+1));
				count = Integer.valueOf((String) num.getText());
				listerner.plusClick();
			}
		});
		
		params = new LayoutParams(28,28);
		params.leftMargin = 385;
		params.topMargin = 10;
		addView(plus, params);
		
		notestext.setText(note);
		notestext.setTextColor(Color.GRAY);
		notestext.setTextSize(12);
		notestext.setId(9);
		params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		params.leftMargin = 70;
		params.topMargin = 30;
		addView(notestext,params);
		
	}
	
	public void setNote(String note){
		this.note = note;
		notestext.setText(note);
	}
	
	public int getcount(){
		return count;
	}
	
	public Button getnotesbt(){
		return notes;
	}

	public String getdishid(){
		return dishid;
	}
	public void setting(String id,String name,String note,double price,double oriprice,int count){
		this.dishid = id;
		this.name = name;
		this.note = note;
		this.price = price;
		this.oriprice = oriprice;
		this.count = count;
		dishnum.setText(id);
		dishname.setText(name);
		notestext.setText(note);
		price1.setText(String.valueOf(""));
		price2.setText(String.valueOf(price));
		num.setText(String.valueOf(count));
		
	}
	
}
