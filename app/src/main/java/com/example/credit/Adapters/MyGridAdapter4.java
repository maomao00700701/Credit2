package com.example.credit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.credit.Entitys.DataManager;
import com.example.credit.R;
import com.example.credit.Views.BaseViewHolder;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class MyGridAdapter4 extends BaseAdapter {
	private Context mContext;
	private String[] arrays1;
	LinearLayout.LayoutParams layoutParam;

	public MyGridAdapter4(Context context, String[] array1 ) {
		super();
		this.mContext = context;
		arrays1=array1;
	}
	public void setmargin(LinearLayout.LayoutParams layoutParam){
		this.layoutParam=layoutParam;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(arrays1.length%2==0){
			return arrays1.length;
		}else{
			return (2-arrays1.length%2)+arrays1.length;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item4, parent, false);

		}
		if(arrays1.length%2!=0){
			if((arrays1.length-1)<position){
				return convertView;
			}
		}
		if(layoutParam!=null){
			TextView ttemp=BaseViewHolder.get(convertView,R.id.tbv);
			ttemp.setTextSize(13);
		}

		TextView tv1 = BaseViewHolder.get(convertView, R.id.tbv);
		switch (arrays1[position]){
			case "企业年报":
				tv1.setText(arrays1[position]+"("+ DataManager.ZZGSModels.data.anbaseInfo.size()+")");
				break;
			case "股东及出资":
				tv1.setText(arrays1[position]+"("+  DataManager.ZZGSModels.data.shareholdersandInvestment.size()+")");
				break;
			case "股权变更":
				tv1.setText(arrays1[position]+"("+ DataManager.ZZGSModels.data.anSubcapitalInfo.size()+")");
				break;
			case "行政许可":
				tv1.setText(arrays1[position]+"("+ DataManager.ZZGSModels.data.eimpermitInfo.size()+")");
				break;
			case "知识产权登记":
				tv1.setText(arrays1[position]+"("+ DataManager.ZZGSModels.data.eimippldgInfo.size()+")");
				break;
			case "行政处罚":
				tv1.setText(arrays1[position]+"("+ DataManager.ZZGSModels.data.eimcaseInfo.size()+")");
				break;
		}

		return convertView;
	}

}
