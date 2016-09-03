package com.longuto.phoneSafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.longuto.phoneSafe.db.BackNumDbHelp;
import com.longuto.phoneSafe.entity.BackNumInfo;

/**
 * 电话黑名单表的数据库操作
 * @author longuto
 */
public class BackNumDb {
	public static final int CALL_MODE = 0;	//电话模式
	public static final int SMS_MODE = 1;	//短信模式
	public static final int ALL_MODE = 2;	//所有模式
	public static final String TABLE = "T_BACKNUM";	//数据库表
	public static final String ID = "_id";	//黑名单表id
	public static final String PHONENUM = "B_PHONENUM";	//黑名单号码
	public static final String MODE = "B_MODE";	//黑名单模式
	
	private Context context;	// 上下文
	private BackNumDbHelp dbHelp;	// 数据库OpenHelp
	
	public BackNumDb(Context context) {
		dbHelp = new BackNumDbHelp(context);
		this.context = context;
	}
	
	/**
	 * 查询数据库黑名单表，按号码降序排序
	 * @return 数据库游标
	 */
	public Cursor queryBackNumTable() {
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		return db.query(TABLE, new String[]{ID, PHONENUM, MODE}, null, null, null, null, PHONENUM + " DESC");	
	}
	
	/**
	 * 查询数据库中黑名单表,并返回所有表记录的集合
	 * @return 黑名单的表记录的集合
	 */
	public List<BackNumInfo> getAllData() {
		List<BackNumInfo> backNumList = new ArrayList<BackNumInfo>();	// 定义一个集合存放黑名单集合
		BackNumInfo temp = null;	// 定义一个黑名单实体变量
		Cursor cursor = queryBackNumTable();	// 获取查询的游标
		while(cursor.moveToNext()) {
			temp = new BackNumInfo();
			temp.setId(cursor.getInt(cursor.getColumnIndex(ID)));
			temp.setNum(cursor.getString(cursor.getColumnIndex(PHONENUM)));
			temp.setMode(cursor.getInt(cursor.getColumnIndex(MODE)));
			backNumList.add(temp);
		}	
		cursor.close();	// 关闭游标
		return backNumList;
	}
	
	
	/**
	 * 增加黑名单表记录
	 */
	public void addBackNum(String phoneNum, int mode) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PHONENUM, phoneNum);
		values.put(MODE, mode);
		db.insert(TABLE, null, values);
		db.close();
	}
	
	/**
	 * 修改黑名单表的模式
	 */
	public void updataBackNumMode(int id, int mode) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MODE, mode);
		db.update(TABLE, values, ID+"=?", new String[]{id+""});
		db.close();
	}
	
	/**
	 * 根据某个id获取黑名单信息
	 * @param id 传入的id编号
	 * @return 返回黑名单信息
	 */
	public BackNumInfo getBackNumInfoById(int id) {
		BackNumInfo temp = null;
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor c = db.query(TABLE, new String[]{ID, PHONENUM, MODE}, ID + "=?", new String[]{id + ""}, null, null, null);
		if(c.moveToNext()) {
			temp = new BackNumInfo();
			temp.setId(id);
			temp.setNum(c.getString(c.getColumnIndex(PHONENUM)));
			temp.setMode(c.getInt(c.getColumnIndex(MODE)));
			c.close();	// 关闭游标
			return temp;
		}
		c.close();	// 关闭游标
		db.close();	// 关闭数据库连接
		return null;
	}
	
	/**
	 * 删除黑名单
	 */
	public void deleteBackNumMode(int id) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		db.delete(TABLE, ID+"=?", new String[]{id+""});
		db.close();
	}
	
	/**
	 * 根据传入的limit和offset分级查询
	 */
	public List<BackNumInfo> getDataByLimit(int limit, int offset) {
		List<BackNumInfo> data = null;
		SQLiteDatabase db = dbHelp.getReadableDatabase();	
		Cursor c = db.query(TABLE, new String[]{ID, PHONENUM, MODE}, null, null, null, null, null, offset + "," + limit);	// 限制查询
		if(c.getCount() > 0) {
			data = new ArrayList<BackNumInfo>();
		}
		BackNumInfo temp = null;
		while(c.moveToNext()) {
			temp = new BackNumInfo();
			temp.setId(c.getInt(c.getColumnIndex(ID)));
			temp.setNum(c.getString(c.getColumnIndex(PHONENUM)));
			temp.setMode(c.getInt(c.getColumnIndex(MODE)));
			data.add(temp);
		}
		c.close();
		db.close();
		return data;
	}	
	
	/**
	 * 根据传入的手机号码,查询黑名单模式
	 * @param phoneNum 传入的电话号码
	 * @return 查询不到返回-1
	 */
	public int queryBackModeByNum(String phoneNum) {
		int mode = -1;
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query(TABLE, new String[]{MODE}, PHONENUM+"=?", new String[]{phoneNum}, null, null, null);
		if(cursor.moveToNext()) {
			mode = cursor.getInt(cursor.getColumnIndex(MODE));
		}
		cursor.close();
		db.close();
		return mode;	
	}
}
