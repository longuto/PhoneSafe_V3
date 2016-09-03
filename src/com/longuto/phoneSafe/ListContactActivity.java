package com.longuto.phoneSafe;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.longuto.phoneSafe.adapter.ContactAdapter;
import com.longuto.phoneSafe.business.ContactManager;
import com.longuto.phoneSafe.entity.Contact;

public class ListContactActivity extends Activity {
	private Context context;	// ������
	
	@ViewInject(R.id.contactList_lstv)
	private ListView mContactLstv;	// ListView չʾ��ϵ�˵Ŀؼ�
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_contact);
		ViewUtils.inject(this);	
		context = this;
		
		// ��ʼ����ͼ
		initView();
	}

	/**
	 * 1. ����ϵ�˵������ṩ�߻�ȡ������ϵ�˵�����
	 * 2. �Զ���������
	 * 3. ����ListView��ͼ��ļ���,������󷵻����ݸ�������
	 */
	private void initView() {
		// ����ҵ��㷽��,��ȡ������ϵ������
		final List<Contact> contacts = ContactManager.getAllContacts(context);
		// ����������
		mContactLstv.setAdapter(new ContactAdapter(context, contacts));
		// �����������ļ���
		mContactLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent data = new Intent();
				data.putExtra("PHONENUM", contacts.get(position).getPhoneNum());	// ����ֻ���������ͼ
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
	
}
