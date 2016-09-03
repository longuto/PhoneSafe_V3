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
	 * ͨ�������ṩ�߻�ȡ������ϵ�˵�����
	 * @param context ������
	 * @return ��ϵ�˵ļ���
	 */
	public static List<Contact> getAllContacts(Context context) {
		List<Contact> contactList = new ArrayList<Contact>();	// ����һ�����ϴ洢������ϵ��
		// ��ȡresolver����
		ContentResolver resolver = context.getContentResolver();
		String uriStr1 = "content://com.android.contacts/raw_contacts";
		String uriStr2 = "content://com.android.contacts/data";
		// ��ȡ������ϵ�˵�contact_id���α�
		Cursor idsCursor = resolver.query(Uri.parse(uriStr1), new String[]{"contact_id"}, "contact_id is not null", null, null);
		while(idsCursor.moveToNext()) {
			int contactId = idsCursor.getInt(idsCursor.getColumnIndex("contact_id"));
			// ��ȡÿ����ϵ��������Ϣ���α�
			Cursor datasCursor = resolver.query(Uri.parse(uriStr2), new String[]{"mimetype", "data1"}, "contact_id=?", new String[]{contactId + ""}, null);
			Contact contact = new Contact();	// ����һ����ϵ�˵Ķ���
			while(datasCursor.moveToNext()) {
				String mimetype = datasCursor.getString(datasCursor.getColumnIndex("mimetype"));
				String data = datasCursor.getString(datasCursor.getColumnIndex("data1"));
				if("vnd.android.cursor.item/name".equals(mimetype)) {
					// ������ϵ������
					contact.setName(data);
				}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
					// ������ϵ�˺���
					contact.setPhoneNum(data);
				}
			}
			datasCursor.close();	// �ص��α�
			contactList.add(contact);
		}
		idsCursor.close();	// �ر��α�
		return contactList;
	}
}
