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
	private Context context;	// ������
	private List<SoftWareInfo> mData;	// ���������Ϣ����
	private List<SoftWareInfo> mUserdData;	// �û������Ϣ����
	private List<SoftWareInfo> mSysData;	// ϵͳ�������
	private SoftWareAdapter mAdapter;	// ������
	private PopupWindow mPopup;		// popup����
	private LinearLayout mPopupView;		// popup������ͼ
	private SoftWareInfo mSoftWareInfo;		// �������ѡ������
	
	private ListView mSoftWareLstv;	// ���չʾ��ListView
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
		popupViewClick();	// ����popup��������¼�
		
		// ��ʼ������
		fillData();
		
		// ListView�Ļ�������
		mSoftWareLstv.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem <= mUserdData.size()) {
					mUserdTagTv.setText("�û�����(" + mUserdData.size() + ")��");
				}else {
					mUserdTagTv.setText("ϵͳ����(" + mSysData.size() + ")��");					
				}	
				mUserdTagTv.setVisibility(View.VISIBLE);
			}
		});
			
		// ����ListView�ĵ������
		mSoftWareLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0 || (position == mUserdData.size() + 1)) {
					return;
				}
				// ��ȡ����б��������
				if(position <= mUserdData.size()) {
					mSoftWareInfo = mUserdData.get(position-1);
				}else {
					mSoftWareInfo = mSysData.get(position - mUserdData.size() - 2);
				}	
				String packageName = mSoftWareInfo.getPackageName();	// ��ȡ����
				if(packageName.equals("com.longuto.phoneSafe")) {
					ToastUtil.show(context, "�㲻���Զ��Լ�����!");
					return;
				}
				ImageView isLockSoftWareIv = (ImageView) view.findViewById(R.id.islockSafe_iv);	// ��ȡ��ǰ�б�����ؼ�
				// �ж����ڰ����Ƿ�Ϊ�������
				if(new WatchDogDb(context).queryWatchDogByPackName(packageName)) {
					new WatchDogDb(context).delWatchDogByPackName(packageName);	// �������ӿ��Ź����ݿ�ɾ��
					isLockSoftWareIv.setImageResource(R.drawable.unlock);
				}else {					
					new WatchDogDb(context).addWatchDogByPackName(packageName);	// ���������뿴�Ź����ݿ�
					isLockSoftWareIv.setImageResource(R.drawable.lock);
				}
			}
		});
		
		// ����ListView�ĳ�������
		mSoftWareLstv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0 || (position == mUserdData.size() + 1)) {
					return true;
				}
				// ��ȡ����б��������
				if(position <= mUserdData.size()) {
					mSoftWareInfo = mUserdData.get(position-1);
				}else {
					mSoftWareInfo = mSysData.get(position - mUserdData.size() - 2 );
				}	
				mPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));	// ����͸��������ֻ�����ñ������ܻ�ȡ����
				mPopup.setFocusable(true);
				mPopup.showAsDropDown(view, 110, -view.getHeight());
				return true;	// ����true,��ʾ����¼��Ѿ��ľ�,����ص���������
			}
		});
	}
	
	/**
	 * ����popupView��ͼ�ĵ�������¼�
	 */
	private void popupViewClick() {
		int count = mPopupView.getChildCount();
		for (int i = 0; i < count; i++) {
			mPopupView.getChildAt(i).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		mPopup.dismiss();	// ��ʧpopupwindow
		switch (v.getId()) {
		case R.id.uninstall_tv:	// ж��
			if("com.longuto.phoneSafe".equals(mSoftWareInfo.getPackageName())) {
				ToastUtil.show(context, "���ж���ң����������...");
				return;
			}else if(!mSoftWareInfo.isUserd()) {
				ToastUtil.show(context, "��û��Ȩ��ɾ��ϵͳӦ�ã�");
				return;
			}
			Intent uninstallIntent = new Intent();
			uninstallIntent.setAction(Intent.ACTION_DELETE);
			uninstallIntent.setData(Uri.parse("package:" + mSoftWareInfo.getPackageName()));
			startActivityForResult(uninstallIntent, 6);
			break;
		case R.id.start_tv:	// ����
			PackageManager pm = getPackageManager();	// ��ȡ��������
			Intent launchIntent = pm.getLaunchIntentForPackage(mSoftWareInfo.getPackageName());	// ͨ������������ȡ������ͼ
			if(launchIntent == null) {
				ToastUtil.show(context, "ϵͳ���񣬲���ֱ��������");
			}else {
				startActivity(launchIntent);
			}
			break;
		case R.id.share_tv:	// ����
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, "android�����������");//��������ı�����Ӧ�ø��Ӹ�key
			startActivity(shareIntent);
			break;
		case R.id.detail_tv:	// ����
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
//			mAdapter.uninstallSoftWare(mSoftWareInfo);	�������ô˷����������޷��ж��û���ж�أ�����δж��
			fillData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void fillData() {
		// �����첽������ִ���������
		new AsyncTask<String, Integer, String>() {
			@Override
			protected void onPreExecute() {
				mUserdData.clear();
				mSysData.clear();
				super.onPreExecute();
			}
			@Override
			protected String doInBackground(String... params) {
				mData = SoftWareManager.getAllSoftWares(context);	// ��ȡ���������Ϣ����
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
