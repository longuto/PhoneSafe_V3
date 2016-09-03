package com.longuto.phoneSafe.business;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.longuto.phoneSafe.entity.Contact;

public class ContactManager {
	/**
	 * 通过内容提供者获取所有联系人的数据
	 * @param context 上下文
	 * @return 联系人的集合
	 */
	public static List<Contact> getAllContacts(Context context) {
		List<Contact> contactList = new ArrayList<Contact>();	// 定义一个集合存储所有联系人
		// 获取resolver对象
		ContentResolver resolver = context.getContentResolver();
		String uriStr1 = "content://com.android.contacts/raw_contacts";
		String uriStr2 = "content://com.android.contacts/data";
		// 获取所有联系人的contact_id的游标
		Cursor idsCursor = resolver.query(Uri.parse(uriStr1), new String[]{"contact_id"}, "contact_id is not null", null, null);
		while(idsCursor.moveToNext()) {
			int contactId = idsCursor.getInt(idsCursor.getColumnIndex("contact_id"));
			// 获取每个联系人所有信息的游标
			Cursor datasCursor = resolver.query(Uri.parse(uriStr2), new String[]{"mimetype", "data1"}, "contact_id=?", new String[]{contactId + ""}, null);
			Contact contact = new Contact();	// 定义一个联系人的对象
			while(datasCursor.moveToNext()) {
				String mimetype = datasCursor.getString(datasCursor.getColumnIndex("mimetype"));
				String data = datasCursor.getString(datasCursor.getColumnIndex("data1"));
				if("vnd.android.cursor.item/name".equals(mimetype)) {
					// 设置联系人姓名
					contact.setName(data);
				}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
					// 设置联系人号码
					contact.setPhoneNum(data);
				}
			}
			datasCursor.close();	// 关掉游标
			contactList.add(contact);
		}
		idsCursor.close();	// 关闭游标
		return contactList;
	}
}
