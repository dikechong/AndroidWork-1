package com.qiaoxi.shopkeeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableItem extends LinearLayout{

	private Button imagebt;
	private TextView tablenumber;
	private LayoutParams param;
	
	public interface tableClickListerner
	{
		public void imageClick();
	}
	
    private tableClickListerner listerner;
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		
	}
	
	public void setTableClickListener(tableClickListerner listener)
	{
		this.listerner = listener;
	}
	
	@SuppressLint("NewApi") public TableItem(Context context){
		super(context);

		setOrientation(VERTICAL);
		
		imagebt = new Button(context);
		tablenumber = new TextView(context);
		
		imagebt.setBackground(this.getResources().getDrawable(R.drawable.nobody));
		tablenumber.setTextColor(Color.BLACK);
		tablenumber.setGravity(Gravity.CENTER);
		tablenumber.setTextSize(13);
		
		imagebt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listerner.imageClick();
			}
		});
		
		param = new LayoutParams(80, 80);
		param.leftMargin = 5;
		param.topMargin = 5;
		param.rightMargin = 5;
		addView(imagebt,param);
		
		param = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		param.bottomMargin = 5;
		addView(tablenumber,param);
		
	}
	
	@SuppressLint("NewApi") 
	public void setiamgeBackground(int flag){
		switch (flag) {
		case 0:
			imagebt.setBackground(this.getResources().getDrawable(R.drawable.nobody));
			break;
		case 1:
			imagebt.setBackground(this.getResources().getDrawable(R.drawable.eating));
			break;
		case 2:
			imagebt.setBackground(this.getResources().getDrawable(R.drawable.booking));
			break;
		default:
			break;
		}
	}
	
	public void setTable(String tablenum){
		tablenumber.setText(tablenum);
	}
	
	public void setTextcolor(int flag){
		switch (flag) {
		case 1:
			tablenumber.setTextColor(Color.BLACK);
			break;
		case 2:
			tablenumber.setTextColor(Color.BLUE);
			break;
		default:
			break;
		}
	}
	
	public String getTableNum(){
		
		return tablenumber.getText().toString().trim();
	}

}
