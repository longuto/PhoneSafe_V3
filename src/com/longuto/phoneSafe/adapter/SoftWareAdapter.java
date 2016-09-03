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
	private Context context;	// ������
	private List<SoftWareInfo> mUserdData;	// �û��������
	private List<SoftWareInfo> mSysData;	// ϵͳ�������

	public SoftWareAdapter(Context context, List<SoftWareInfo> mUserdData, List<SoftWareInfo> mSysData) {
		this.context = context;
		this.mUserdData = mUserdData;
		this.mSysData = mSysData;
	}

	/**
	 * �ṩɾ��һ�����ݵķ���
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
			textView.setBackgroundColor(Color.GRAY);	// ���ñ���
			if(position == 0) {
				textView.setText("�û�����"+ mUserdData.size() +")��");
			}
			if(position == mUserdData.size() + 1) {
				textView.setText("ϵͳ����"+ mSysData.size() +")��");
			}
			return textView;
		}
		
		SoftWareInfo info = (SoftWareInfo) getItem(position);	// ��ǰչ�������
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
		
		iconIv.setImageDrawable(info.getIcon());	// ����ͼ��
		nameTv.setText(info.getName());	// ��������
		versionCodeTv.setText(info.getVersionCode()+"");	// ���ð汾��
		if(info.isSdCard()) {
			isSdCardTv.setText("Sd��");
		}else {
			isSdCardTv.setText("�ֻ��ڴ�");
		}
		boolean isWatchDogSoftWare = new WatchDogDb(context).queryWatchDogByPackName(info.getPackageName());	// �ж��Ƿ��ǿ��Ź����
		if(!isWatchDogSoftWare) {
			isLockSoftwareIv.setImageResource(R.drawable.unlock);
		}else {
			isLockSoftwareIv.setImageResource(R.drawable.lock);
		}
		return rootView;
	}
//	View v;
}
