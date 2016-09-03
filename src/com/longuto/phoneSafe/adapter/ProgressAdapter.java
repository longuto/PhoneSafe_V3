package com.longuto.phoneSafe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.entity.ProgressInfo;

public class ProgressAdapter extends BaseAdapter{
	private List<ProgressInfo> mUserdData;	// 用户数据
	private List<ProgressInfo> mSysData;	// 系统数据
	private Context context;
	
	public ProgressAdapter(Context context, List<ProgressInfo> mUserdData, List<ProgressInfo> mSysData) {
		this.context = context;
		this.mUserdData = mUserdData;
		this.mSysData = mSysData;
	}

	public List<ProgressInfo> getUserdData() {
		return mUserdData;
	}
	
	public List<ProgressInfo> getSysData() {
		return mSysData;
	}
	
	@Override
	public int getCount() {
		return mUserdData.size() + mSysData.size() + 2;
	}

	@Override
	public Object getItem(int position) {
		if(position == 0 || position == mUserdData.size() + 1) {
			return null;
		}
		if(position <= mUserdData.size()) {
			return mUserdData.get(position-1);
		}
		return mSysData.get(position - mUserdData.size() - 2);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(position == 0 || position == mUserdData.size() + 1) {			
			TextView textView = new TextView(context);
			textView.setBackgroundColor(Color.GRAY);	// 设置背景
			if(position == 0) {
				textView.setText("用户进程（"+ mUserdData.size() +")个");
			}
			if(position == mUserdData.size() + 1) {
				textView.setText("系统进程（"+ mSysData.size() +")个");
			}
			return textView;
		}
		
		final ProgressInfo info = (ProgressInfo) getItem(position);	// 当前展板的数据
		Holder holder = null;
		if((convertView != null) && (convertView instanceof RelativeLayout)) {
			holder = (Holder) convertView.getTag();
		}else {
			convertView = LayoutInflater.from(context).inflate(R.layout.progress_item, parent, false);			
			holder = new Holder();
			holder.iconTv = (ImageView) convertView.findViewById(R.id.progressIcon_iv);	// 图标
			holder.nameTv = (TextView) convertView.findViewById(R.id.progressName_tv);		// 名称
			holder.memoryTv = (TextView) convertView.findViewById(R.id.progressMemory_tv);	// 内存
			holder.isCheckedCkb = (CheckBox) convertView.findViewById(R.id.progressIschecked_ckb);	// 是否选中
			convertView.setTag(holder);
		}
		holder.iconTv.setImageDrawable(info.getIcon());
		holder.nameTv.setText(info.getName());
		holder.memoryTv.setText(info.getMemory() + "MB");
		if(info.isChecked()) {
			holder.isCheckedCkb.setChecked(true);
		}else {
			holder.isCheckedCkb.setChecked(false);
		}
/*		// 设置复选框按钮点击监听事件
		holder.isCheckedCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(position <= mUserdData.size()) {	
					if(isChecked) {						
						info.setChecked(true);
						buttonView.setChecked(true);
					}else {
						info.setChecked(false);
						buttonView.setChecked(false);
					}
					mUserdData.set(position - 1, info);
				}else {
					if(isChecked) {
						info.setChecked(true);
					}else {
						info.setChecked(false);
					}
					mSysData.set(position - mUserdData.size() - 2, info);
				}					
			}
		});*/
		return convertView;
	}

	private class Holder {
		public ImageView iconTv;	// 图标
		public TextView nameTv;		// 名称
		public TextView memoryTv;	// 内存
		public CheckBox isCheckedCkb;
	}

}
