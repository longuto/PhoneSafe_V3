package com.longuto.phoneSafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.longuto.phoneSafe.db.dao.BackNumDb;
import com.longuto.phoneSafe.util.ServiceStateUtil;

public class TestClass extends AndroidTestCase {
	public void testService() {
		ServiceStateUtil.isServiceRunning(getContext(), "");
	}
	
	public void testAddBackNum() {
		List<ContentValues> list = new ArrayList<ContentValues>();
		Random random = new Random();
		BackNumDb db = new BackNumDb(getContext());
		for (int i = 0; i < 20; i++) {
			String phoneNum = (18321906193L + 100500*i) + "";
			int mode = random.nextInt(3);
			db.addBackNum(phoneNum, mode);
		}
	}
	
	public void queryBackNum() {
		BackNumDb db = new BackNumDb(getContext());
		Cursor cursor = db.queryBackNumTable();
		while(cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String phoneNum = cursor.getString(cursor.getColumnIndex("B_PHONENUM"));
			int mode = cursor.getInt(cursor.getColumnIndex("B_MODE"));
			System.out.println(id + "\t" + phoneNum + "\t" + mode);
		}
		
	}
}
