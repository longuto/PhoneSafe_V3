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
	private List<ProgressInfo> mUserdData;	// �û�����
	private List<ProgressInfo> mSysData;	// ϵͳ����
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
			textView.setBackgroundColor(Color.GRAY);	// ���ñ���
			if(position == 0) {
				textView.setText("�û����̣�"+ mUserdData.size() +")��");
			}
			if(position == mUserdData.size() + 1) {
				textView.setText("ϵͳ���̣�"+ mSysData.size() +")��");
			}
			return textView;
		}
		
		final ProgressInfo info = (ProgressInfo) getItem(position);	// ��ǰչ�������
		Holder holder = null;
		if((convertView != null) && (convertView instanceof RelativeLayout)) {
			holder = (Holder) convertView.getTag();
		}else {
			convertView = LayoutInflater.from(context).inflate(R.layout.progress_item, parent, false);			
			holder = new Holder();
			holder.iconTv = (ImageView) convertView.findViewById(R.id.progressIcon_iv);	// ͼ��
			holder.nameTv = (TextView) convertView.findViewById(R.id.progressName_tv);		// ����
			holder.memoryTv = (TextView) convertView.findViewById(R.id.progressMemory_tv);	// �ڴ�
			holder.isCheckedCkb = (CheckBox) convertView.findViewById(R.id.progressIschecked_ckb);	// �Ƿ�ѡ��
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
/*		// ���ø�ѡ��ť��������¼�
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
		public ImageView iconTv;	// ͼ��
		public TextView nameTv;		// ����
		public TextView memoryTv;	// �ڴ�
		public CheckBox isCheckedCkb;
	}

}
