package com.longuto.phoneSafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.longuto.phoneSafe.adapter.BackNumAdapter;
import com.longuto.phoneSafe.db.dao.BackNumDb;
import com.longuto.phoneSafe.entity.BackNumInfo;
import com.longuto.phoneSafe.util.ToastUtil;

public class BackNumActivity extends Activity {
	private static final String TAG = "BackNumActivity";

	private Context context;	// ������
	private List<BackNumInfo> mData;		// ��ȡ���к��������ݼ���
	private BackNumAdapter mAdapter;	// ������
	private int mLimit = 20;	// ���ƴ�С
	private int mOffset = 0;	// ƫ����
	
	@ViewInject(R.id.backNum_lstV)
	private ListView mBackNumLstv;	// ������ListView
	
	// ���¼�������
	public void setmData(List<BackNumInfo> data) {
		mData = data;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_num);
		ViewUtils.inject(this);
		context = this;
		
		// ��ʼ��ʱ��װ��ListView
		mData = limitData(mLimit, mOffset);	// ��ʼװ������
		if(mData == null) {
			mData = new ArrayList<BackNumInfo>();
		}
		mAdapter = new BackNumAdapter(context, mData);
		mBackNumLstv.setAdapter(mAdapter);
		
		/**
		 * ����ListView��������
		 * SCROLL_STATE_FLING: ���ٻ������뿪��ָ�Ļ���
		 * SCROLL_STATE_IDLE�� ֹͣ����
		 * SCROLL_STATE_TOUCH_SCROLL�� ��������
		 */
		mBackNumLstv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// ���绬��ֹͣ
				if(OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
					int lastLocation = mBackNumLstv.getLastVisiblePosition();	// ��ȡ���һλ�ɼ���itemѡ��
					int size = mData.size();
					if(lastLocation == size-1) {
						mOffset += mLimit;
						List<BackNumInfo> data = limitData(mLimit, mOffset);	// �ٴβ�ѯ����
						if(data != null) {
							mAdapter.addListData(data);
							mAdapter.notifyDataSetChanged();	// ֪ͨ�������������Ѿ��ı�													
						}
					}
				}	
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
		
	}
	
	/**
	 * װ������
	 */
	private List<BackNumInfo> limitData(int limit, int offset) {
		return new BackNumDb(context).getDataByLimit(limit, offset);			
	}

	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.back_num, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * �˵�ѡ�����
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.backNum_add:
			final View view = View.inflate(context, R.layout.backnum_add_dialog, null);	// ��ӵ�����ͼ
			final Dialog dialog = new AlertDialog.Builder(context)
			.setView(view)
			.create();	// ������ӶԻ���
			// ȷ����ť����¼�
			view.findViewById(R.id.backNumAdd_btn).setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					EditText backNumEdt = (EditText) view.findViewById(R.id.backNum_edt);	// ����������༭��
					String backNum = backNumEdt.getText().toString().trim();	// ����������
					if(!TextUtils.isEmpty(backNum)) {
						BackNumInfo temp = new BackNumInfo();	// ����һ��������ʵ����
						temp.setNum(backNum);	// ����ʵ����ĺ���
						RadioGroup group = (RadioGroup) view.findViewById(R.id.backNumMode_rgp);	// ѡ����
						switch (group.getCheckedRadioButtonId()) {
						case R.id.all_rb:	// ȫ��
							temp.setMode(BackNumDb.ALL_MODE);
							break;
						case R.id.sms_rb:	// ����
							temp.setMode(BackNumDb.SMS_MODE);
							break;
						case R.id.call_rb:	// �绰
							temp.setMode(BackNumDb.CALL_MODE);
							break;
						default:
							break;
						}
						new BackNumDb(context).addBackNum(temp.getNum(), temp.getMode());	// ���ݿ�ִ�����Ӳ���
						mAdapter.addBackNum(temp);	// ������������������һ������
						mAdapter.notifyDataSetChanged();	// ֪ͨ���ݿ���Ϣ�Ѿ��ı�
						dialog.dismiss();	// ��ʧ�Ի���
					}else {
						ToastUtil.show(context, "���������벻��Ϊ��");
					}
				} 
			});
			// ȡ����ť����¼�
			view.findViewById(R.id.backNumCancel_btn).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();	// ��ʧ�Ի���
				}
			});
			dialog.show();	// ��ʾ�Ի���
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
