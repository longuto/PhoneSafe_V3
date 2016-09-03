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
	private Context context;	// 上下文
	
	@ViewInject(R.id.contactList_lstv)
	private ListView mContactLstv;	// ListView 展示联系人的控件
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_contact);
		ViewUtils.inject(this);	
		context = this;
		
		// 初始化视图
		initView();
	}

	/**
	 * 1. 从联系人的内容提供者获取所有联系人的数据
	 * 2. 自定义适配器
	 * 3. 设置ListView视图项的监听,当点击后返回数据给调用者
	 */
	private void initView() {
		// 调用业务层方法,获取所有联系人数据
		final List<Contact> contacts = ContactManager.getAllContacts(context);
		// 设置适配器
		mContactLstv.setAdapter(new ContactAdapter(context, contacts));
		// 设置适配器的监听
		mContactLstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent data = new Intent();
				data.putExtra("PHONENUM", contacts.get(position).getPhoneNum());	// 添加手机号码至意图
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
	
}
