package com.longuto.phoneSafe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longuto.phoneSafe.R;
import com.longuto.phoneSafe.db.dao.WatchDogDb;
import com.longuto.phoneSafe.entity.SoftWareInfo;

public class SoftWareAdapter extends BaseAdapter {
	private Context context;	// 上下文
	private List<SoftWareInfo> mUserdData;	// 用户软件集合
	private List<SoftWareInfo> mSysData;	// 系统软件集合

	public SoftWareAdapter(Context context, List<SoftWareInfo> mUserdData, List<SoftWareInfo> mSysData) {
		this.context = context;
		this.mUserdData = mUserdData;
		this.mSysData = mSysData;
	}

	/**
	 * 提供删除一个数据的方法
	 */
	public void uninstallSoftWare(SoftWareInfo softWareInfo) {
		if(mUserdData.contains(softWareInfo)) {
			mUserdData.remove(softWareInfo);
		}else if(mSysData.contains(softWareInfo)) {
			mSysData.remove(softWareInfo);
		}
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position == 0 || position == mUserdData.size() + 1) {			
			TextView textView = new TextView(context);
			textView.setBackgroundColor(Color.GRAY);	// 设置背景
			if(position == 0) {
				textView.setText("用户程序（"+ mUserdData.size() +")个");
			}
			if(position == mUserdData.size() + 1) {
				textView.setText("系统程序（"+ mSysData.size() +")个");
			}
			return textView;
		}
		
		SoftWareInfo info = (SoftWareInfo) getItem(position);	// 当前展板的数据
		View rootView = null;
		if((convertView != null) && (convertView instanceof RelativeLayout)) {
			rootView = convertView;
		}else {
			rootView = LayoutInflater.from(context).inflate(R.layout.soft_ware_item, parent, false);			
		}
		ImageView iconIv = (ImageView) rootView.findViewById(R.id.softIcon_iv);
		TextView nameTv = (TextView) rootView.findViewById(R.id.softName_tv);
		TextView isSdCardTv = (TextView) rootView.findViewById(R.id.softIsSdCard_tv);
		TextView versionCodeTv = (TextView) rootView.findViewById(R.id.softVersion_tv);
		ImageView isLockSoftwareIv = (ImageView) rootView.findViewById(R.id.islockSafe_iv);
		
		iconIv.setImageDrawable(info.getIcon());	// 设置图标
		nameTv.setText(info.getName());	// 设置名称
		versionCodeTv.setText(info.getVersionCode()+"");	// 设置版本号
		if(info.isSdCard()) {
			isSdCardTv.setText("Sd卡");
		}else {
			isSdCardTv.setText("手机内存");
		}
		boolean isWatchDogSoftWare = new WatchDogDb(context).queryWatchDogByPackName(info.getPackageName());	// 判断是否是看门狗软件
		if(!isWatchDogSoftWare) {
			isLockSoftwareIv.setImageResource(R.drawable.unlock);
		}else {
			isLockSoftwareIv.setImageResource(R.drawable.lock);
		}
		return rootView;
	}
//	View v;
}
