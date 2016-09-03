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
	 * 备份手机短信,将短信写入指定的Xml文件
	 * @param context 上下文
	 * @param fos 指定文件的输出流
	 */
	public static void backupSms(Context context, FileOutputStream fos) throws Exception {
		ContentResolver resolver = context.getContentResolver();	// 获取内容解决者
		String urlStr = "content://sms";
		// 获取查询的游标
		Cursor cursor = resolver.query(Uri.parse(urlStr), new String[]{"address","date","type","body"}, null, null, null);
		XmlSerializer serializer = Xml.newSerializer();	//准备序列化器
		serializer.setOutput(fos, "UTF-8");
		serializer.startDocument("UTF-8", true);	// 开始文档
		serializer.startTag(null, "smss");
		while(cursor.moveToNext()) {
			String address = cursor.getString(cursor.getColumnIndex("address"));	// 地址
			String date = cursor.getString(cursor.getColumnIndex("date"));	// 日期
			String type = cursor.getString(cursor.getColumnIndex("type"));	// 类型
			String body = cursor.getString(cursor.getColumnIndex("body"));	// 内容
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
		serializer.endDocument();	// 结束文档
		
		cursor.close();
		fos.close();
	}

}
