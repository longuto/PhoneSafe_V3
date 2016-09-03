package com.longuto.phoneSafe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.entity.SoftWareInfo;

public class SoftWareAdapterDemo extends BaseAdapter {
	private Context context;	// 上下文
	private List<SoftWareInfo> mData;


	
	public SoftWareAdapterDemo(Context context, List<SoftWareInfo> mData) {
		this.context = context;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SoftWareInfo info = mData.get(position);
		Holder holder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.soft_ware_item, parent, false);
			holder = new Holder();
			holder.iconIv = (ImageView) convertView.findViewById(R.id.softIcon_iv);
			holder.nameTv = (TextView) convertView.findViewById(R.id.softName_tv);
			holder.isSdCardTv = (TextView) convertView.findViewById(R.id.softIsSdCard_tv);
			holder.versionTv = (TextView) convertView.findViewById(R.id.version_tv);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.iconIv.setImageDrawable(info.getIcon());
		holder.nameTv.setText(info.getName());
		//holder.versionTv.setText(info.getVersionCode() + "");
		if(info.isSdCard()) {
			holder.isSdCardTv.setText("SD卡");			
		}else {
			holder.isSdCardTv.setText("手机内存");
		}
		return convertView;
	}
	
	private class Holder {
		public ImageView iconIv;
		public TextView nameTv;
		public TextView isSdCardTv;
		public TextView versionTv;
	}
}
