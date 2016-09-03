package com.longuto.phoneSafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.longuto.phoneSafe.adapter.SoftWareAdapter;
import com.longuto.phoneSafe.business.SoftWareManager;
import com.longuto.phoneSafe.db.dao.WatchDogDb;
import com.longuto.phoneSafe.entity.SoftWareInfo;
import com.longuto.phoneSafe.util.ToastUtil;

public class SoftWareActivity extends Activity implements OnClickListener{
	private Context context;	// 上下文
	private List<SoftWareInfo> mData;	// 所有软件信息集合
	private List<SoftWareInfo> mUserdData;	// 用户软件信息集合
	private List<SoftWareInfo> mSysData;	// 系统软件集合
	private SoftWareAdapter mAdapter;	// 适配器
	private PopupWindow mPopup;		// popup弹窗
	private LinearLayout mPopupView;		// popup线性视图
	private SoftWareInfo mSoftWareInfo;		// 被点击的选项数据
	
	private ListView mSoftWareLstv;	// 软件展示的ListView
	private TextView mUserdTagTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soft_ware);
		context = this;
		mData = new ArrayList<SoftWareInfo>();
		mUserdData = new ArrayList<SoftWareInfo>();
		mSysData = new ArrayList<SoftWareInfo>();
		mSoftWareLstv = (ListView) findViewById(R.id.softWare_lst);
		mUserdTagTv = (TextView) findViewById(R.id.userdTag_tv);
		mPopupView = (LinearLayout) View.inflate(context, R.layout.soft_ware_shared, null);
		mPopup = new PopupWindow(mPopupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupViewClick();	// 设置popup弹窗点击事件
		
		// 初始化数据
		fillData();
		
		// ListView的滑动监听
		mSoftWareLstv.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem <= mUserdData.size()) {
					mUserdTagTv.setText("用户程序(" + mUserdData.size() + ")个");
				}else {
					mUserdTagTv.setText("系统程序(" + mSysData.size() + ")个");					
				}	
				mUserdTagTv.setVisibility(View.VISIBLE);
			}
		});
			
		// 设置ListView的点击监听
		mSoftWareLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0 || (position == mUserdData.size() + 1)) {
					return;
				}
				// 获取点击列表项的数据
				if(position <= mUserdData.size()) {
					mSoftWareInfo = mUserdData.get(position-1);
				}else {
					mSoftWareInfo = mSysData.get(position - mUserdData.size() - 2);
				}	
				String packageName = mSoftWareInfo.getPackageName();	// 获取包名
				if(packageName.equals("com.longuto.phoneSafe")) {
					ToastUtil.show(context, "你不可以对自己加锁!");
					return;
				}
				ImageView isLockSoftWareIv = (ImageView) view.findViewById(R.id.islockSafe_iv);	// 获取当前列表的锁控件
				// 判断所在包名是否为加锁软件
				if(new WatchDogDb(context).queryWatchDogByPackName(packageName)) {
					new WatchDogDb(context).delWatchDogByPackName(packageName);	// 将包名从看门狗数据库删除
					isLockSoftWareIv.setImageResource(R.drawable.unlock);
				}else {					
					new WatchDogDb(context).addWatchDogByPackName(packageName);	// 将包名加入看门狗数据库
					isLockSoftWareIv.setImageResource(R.drawable.lock);
				}
			}
		});
		
		// 设置ListView的长按监听
		mSoftWareLstv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0 || (position == mUserdData.size() + 1)) {
					return true;
				}
				// 获取点击列表项的数据
				if(position <= mUserdData.size()) {
					mSoftWareInfo = mUserdData.get(position-1);
				}else {
					mSoftWareInfo = mSysData.get(position - mUserdData.size() - 2 );
				}	
				mPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));	// 设置透明背景，只有设置背景才能获取焦点
				mPopup.setFocusable(true);
				mPopup.showAsDropDown(view, 110, -view.getHeight());
				return true;	// 返回true,表示点击事件已经耗尽,不会回调后续方法
			}
		});
	}
	
	/**
	 * 设置popupView视图的点击监听事件
	 */
	private void popupViewClick() {
		int count = mPopupView.getChildCount();
		for (int i = 0; i < count; i++) {
			mPopupView.getChildAt(i).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		mPopup.dismiss();	// 消失popupwindow
		switch (v.getId()) {
		case R.id.uninstall_tv:	// 卸载
			if("com.longuto.phoneSafe".equals(mSoftWareInfo.getPackageName())) {
				ToastUtil.show(context, "你敢卸载我？不给你机会...");
				return;
			}else if(!mSoftWareInfo.isUserd()) {
				ToastUtil.show(context, "你没有权限删除系统应用！");
				return;
			}
			Intent uninstallIntent = new Intent();
			uninstallIntent.setAction(Intent.ACTION_DELETE);
			uninstallIntent.setData(Uri.parse("package:" + mSoftWareInfo.getPackageName()));
			startActivityForResult(uninstallIntent, 6);
			break;
		case R.id.start_tv:	// 启动
			PackageManager pm = getPackageManager();	// 获取包管理器
			Intent launchIntent = pm.getLaunchIntentForPackage(mSoftWareInfo.getPackageName());	// 通过包管理器获取启动意图
			if(launchIntent == null) {
				ToastUtil.show(context, "系统服务，不能直接启动！");
			}else {
				startActivity(launchIntent);
			}
			break;
		case R.id.share_tv:	// 分享
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, "android开发经验分享");//假如分享文本，则应该附加该key
			startActivity(shareIntent);
			break;
		case R.id.detail_tv:	// 详情
			Intent detailIntent = new Intent();
			detailIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			detailIntent.setData(Uri.parse("package:" + mSoftWareInfo.getPackageName()));
			startActivity(detailIntent);
			break;
		default:
			break;
		}
		
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 6) {
//			mAdapter.uninstallSoftWare(mSoftWareInfo);	本来想用此方法，但是无法判断用户是卸载，还是未卸载
			fillData();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
				mData = SoftWareManager.getAllSoftWares(context);	// 获取所有软件信息集合
				for (SoftWareInfo info : mData) {
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
				mAdapter = new SoftWareAdapter(context, mUserdData, mSysData);
				mSoftWareLstv.setAdapter(mAdapter);	
				super.onPostExecute(result);
			}	
		}.execute();
	}
}
