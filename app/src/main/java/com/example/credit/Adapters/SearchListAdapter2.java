package com.example.credit.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.credit.Activitys.SearchFirmActivty;
import com.example.credit.Entitys.DataManager;
import com.example.credit.Entitys.DataManager.search;
import com.example.credit.R;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter2 extends BaseAdapter {
	private Context context;
	private  List<search.DataBean.ResultBean> list;
	int lastPosition = -1;
	public SearchListAdapter2(Context context, List<search.DataBean.ResultBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return  0;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh=null;
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.search_result_list_item, null);
			vh=new ViewHolder();
			vh.firm_name=(TextView) view.findViewById(R.id.firm_name);
			vh.corporate=(TextView) view.findViewById(R.id.corporate);
			vh.funds=(TextView) view.findViewById(R.id.funds);
			vh.status=(TextView) view.findViewById(R.id.status);
			vh.time=(TextView) view.findViewById(R.id.date);
			view.setTag(vh);
		}else{
			vh=(ViewHolder) view.getTag();
		}
		if (position > lastPosition) {//这里就是动画的应用
//			Animation animation = AnimationUtils.loadAnimation(context, R.anim.list_anim);
			Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.list_anim : R.anim.list_anim);
			view.startAnimation(animation);
			lastPosition = position;
		}

		search.DataBean.ResultBean c=list.get(position);
		switch (SearchFirmActivty.type){
			case 0:
				String str=SearchFirmActivty.searchEt.getText().toString();
				String[] strR=str.split("");
				SpannableStringBuilder style = new SpannableStringBuilder(c.ENTNAME);
				for(int i=0;i<strR.length;i++){
					if(!strR[i].equals("") && (c.ENTNAME).indexOf(strR[i]) != -1){
						int start=c.ENTNAME.indexOf(strR[i]);
						int end=start+1;
//				style.setSpan(new BackgroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的背景颜色
						style.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
					}
				}
				vh.firm_name.setText(style);
				vh.corporate.setText(c.NAME);
				break;
			case 1:
				String str1=SearchFirmActivty.searchEt.getText().toString();
				String[] strR1=str1.split("");
				SpannableStringBuilder style1 = new SpannableStringBuilder(c.NAME);
				for(int j=0;j<strR1.length;j++){
					if(!strR1[j].equals("") && (c.NAME).indexOf(strR1[j]) != -1){
						int start=c.NAME.indexOf(strR1[j]);
						int end=start+1;
//				style.setSpan(new BackgroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的背景颜色
						style1.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
					}
				}
				vh.corporate.setText(style1);
				vh.firm_name.setText(c.ENTNAME);
				break;
			default:
				vh.corporate.setText(c.NAME);
				vh.firm_name.setText(c.ENTNAME);
				break;
		}
		if(vh.corporate.getText().toString().equals("")){
			vh.corporate.setText("无");
		}
		if(c.REGCAP.equals("")){
			vh.funds.setText("无");
		}else{
			if(c.REGCAP.indexOf(".") == -1){
				vh.funds.setText(c.REGCAP+ "万元");
			}else{
				vh.funds.setText(c.REGCAP.substring(0,c.REGCAP.indexOf(".")) + "万元");
			}
		}

		vh.status.setText(c.REGSTATE_CN);
		vh.time.setText(c.ESTDATE);

		return view;
	}
	public class ViewHolder{
		TextView firm_name;
		TextView corporate;
		TextView funds;
		TextView status;
		TextView time;
	}
}
