package com.longuto.phoneSafe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressManager {
	/**
	 * ͨ���ֻ�����������Ĳ�ѯ���ݿ� -- ���ݿ��ļ���ַ���ֻ��ڴ�files�ļ���
	 * @param num �ֻ�����
	 * @param context ������
	 * @return �ֻ����������
	 */
	public static String queryLocation(String num, Context context) {
		String numLocation = ""; 	// ����һ���ַ���������ʾ�ֻ����������
		String addressPath = context.getFilesDir() + "/address.db";		// �ļ�·��
		SQLiteDatabase db = SQLiteDatabase.openDatabase(addressPath, null, SQLiteDatabase.OPEN_READONLY);	// ��ȡdb
		// ���ƥ���ֻ�����
		if(num.matches("^1[345678]\\d{9}$")) {
			// ��ȡ��ѯ�α�
			Cursor cursor = db.rawQuery("select location from data2 where id in (select outkey from data1 where id=?)", new String[]{num.substring(0, 7)});
			if(cursor.moveToNext()) {
				numLocation = cursor.getString(0);
			}
		}else {	
			int len = num.length();	// ��ȡ�ֻ����볤��
			switch (num.length()) {
			case 3:
				numLocation = "����̨";
				break;
			case 4:
				numLocation = "�������";
				break;
			case 5:
				numLocation = "�������";
				break;
			case 7:
			case 8:
				numLocation = "��������";
				break;
			default:
				// ��;����
				if(num.startsWith("0")) {
					if(len >= 10) {
						Cursor c1 = db.rawQuery("select location from data2 where area=?", new String[]{num.substring(1, 4)});
						if(c1.moveToNext()) {
							numLocation = c1.getString(0);
						}else {
							Cursor c2 = db.rawQuery("select location from data2 where area=?", new String[]{num.substring(1, 3)});
							if(c2.moveToNext()) {
								numLocation = c2.getString(0);
							}
						}
					}
				}
				break;
			}
		}
		return numLocation;
	}
}
