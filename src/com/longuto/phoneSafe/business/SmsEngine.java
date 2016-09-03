package com.longuto.phoneSafe.business;

import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

public class SmsEngine {
	/**
	 * �����ֻ�����,������д��ָ����Xml�ļ�
	 * @param context ������
	 * @param fos ָ���ļ��������
	 */
	public static void backupSms(Context context, FileOutputStream fos) throws Exception {
		ContentResolver resolver = context.getContentResolver();	// ��ȡ���ݽ����
		String urlStr = "content://sms";
		// ��ȡ��ѯ���α�
		Cursor cursor = resolver.query(Uri.parse(urlStr), new String[]{"address","date","type","body"}, null, null, null);
		XmlSerializer serializer = Xml.newSerializer();	//׼�����л���
		serializer.setOutput(fos, "UTF-8");
		serializer.startDocument("UTF-8", true);	// ��ʼ�ĵ�
		serializer.startTag(null, "smss");
		while(cursor.moveToNext()) {
			String address = cursor.getString(cursor.getColumnIndex("address"));	// ��ַ
			String date = cursor.getString(cursor.getColumnIndex("date"));	// ����
			String type = cursor.getString(cursor.getColumnIndex("type"));	// ����
			String body = cursor.getString(cursor.getColumnIndex("body"));	// ����
			serializer.startTag(null, "sms");
				serializer.startTag(null, "address");
					serializer.text(address);
				serializer.endTag(null, "address");
				serializer.startTag(null, "date");
					serializer.text(date);
				serializer.endTag(null, "date");
				serializer.startTag(null, "type");
					serializer.text(type);
				serializer.endTag(null, "type");
				serializer.startTag(null, "body");
					serializer.text(body);
				serializer.endTag(null, "body");
			serializer.endTag(null, "sms");
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();	// �����ĵ�
		
		cursor.close();
		fos.close();
	}

}
