package com.longuto.phoneSafe;

import java.util.ArrayList;
import java.util.List;

import com.longuto.phoneSafe.adapter.ProgressAdapter;
import com.longuto.phoneSafe.adapter.SoftWareAdapter;
import com.longuto.phoneSafe.business.ProgressManager;
import com.longuto.phoneSafe.business.SoftWareManager;
import com.longuto.phoneSafe.entity.ProgressInfo;
import com.longuto.phoneSafe.entity.SoftWareInfo;
import com.longuto.phoneSafe.util.ToastUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ProgressActivity extends Activity {
	
	private Context context;	// 上下文
	private List<ProgressInfo> mData;	// 所有软件信息集合
	private List<ProgressInfo> mUserdData;	// 用户软件信息集合
	private List<ProgressInfo> mSysData;	// 系统软件集合
	private ProgressAdapter mAdapter;	// 适配器
	private ProgressInfo mProgressInfo;	// 全局的进程信息对象
	private ListView mProgressLstv;	// 软件展示的ListView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);
		context = this;
		mData = new ArrayList<ProgressInfo>();
		mUserdData = new ArrayList<ProgressInfo>();
		mSysData = new ArrayList<ProgressInfo>();
		mProgressLstv = (ListView) findViewById(R.id.progresss_lst);
		
		// 初始化数据
		fillData();
			
		// 设置ListView的长按监听
		mProgressLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0 || (position == mUserdData.size() + 1)) {
					return;
				}
				
			}
		});
		
		// 设置ListView的Item点击事件
		mProgressLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0 || (position == mUserdData.size() + 1)) {
					return;
				}
				CheckBox isCheckedCkb = (CheckBox) view.findViewById(R.id.progressIschecked_ckb);
				if(position <= mUserdData.size()) {
					mProgressInfo = mUserdData.get(position - 1);
					if(mProgressInfo.isChecked()) {
						mProgressInfo.setChecked(false);
						isCheckedCkb.setChecked(false);
					}else {
						mProgressInfo.setChecked(true);	
						isCheckedCkb.setChecked(true);
					}
					mUserdData.set(position - 1, mProgressInfo);
				}else { 
					mProgressInfo = mSysData.get(position - mUserdData.size() - 2);
					if(mProgressInfo.isChecked()) {
						mProgressInfo.setChecked(false);
						isCheckedCkb.setChecked(false);
					}else {
						mProgressInfo.setChecked(true);
						isCheckedCkb.setChecked(true);
					}
					mSysData.set(position - mUserdData.size() - 2, mProgressInfo);
				}
				
			}
		});
	}
	
	private void fillData() {
		// 调用异步工具类执行数据填充
		new AsyncTask<String, Integer, String>() {
			@Override
			protected void onPreExecute() {
				mUserdData.clear();
				mSysData.clear();
				super.onPreExecute();
			}
			@Override
			protected String doInBackground(String... params) {
				mData = ProgressManager.getAllProgress(context);	// 获取所有进程信息集合
				for (ProgressInfo info : mData) {
					if(info.isUserd()) {
						mUserdData.add(info);
					}else {
						mSysData.add(info);
					}
				}
				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				mAdapter = new ProgressAdapter(context, mUserdData, mSysData);
				mProgressLstv.setAdapter(mAdapter);
				super.onPostExecute(result);
			}	
		}.execute();
	}
	
	/**
	 * 全选点击监听 
	 */
	public void allChoose(View v) {
		for (ProgressInfo info : mUserdData) {
			info.setChecked(true);
		}
		for (ProgressInfo info : mSysData) {
			info.setChecked(true);
		}
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 取消点击监听 
	 */
	public void allUnChoose(View v) {
		for (ProgressInfo info : mUserdData) {
			info.setChecked(false);
		}
		for (ProgressInfo info : mSysData) {
			info.setChecked(false);
		}
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 清除点击监听 
	 */
	public void clearProgress(View v) {
		// Activity管理器
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ProgressInfo> userDelData = new ArrayList<ProgressInfo>();
		for (ProgressInfo info : mUserdData) {
			if(info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
				userDelData.add(info);
			}
		}
		for (ProgressInfo info : userDelData) {
			mUserdData.remove(info);
		}
		
		List<ProgressInfo> sysDelData = new ArrayList<ProgressInfo>();
		for (ProgressInfo info : mSysData) {
			if(info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
				sysDelData.add(info);
			}
		}
		for (ProgressInfo info : sysDelData) {
			mSysData.remove(info);
		}
		
		mAdapter.notifyDataSetChanged();	// 通知适配器数据已改变
	}
	
	/**
	 * 设置点击监听 
	 */
	public void setProgress(View v) {

	}
}
