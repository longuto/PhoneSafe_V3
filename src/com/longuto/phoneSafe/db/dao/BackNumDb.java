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
 * �绰������������ݿ����
 * @author longuto
 */
public class BackNumDb {
	public static final int CALL_MODE = 0;	//�绰ģʽ
	public static final int SMS_MODE = 1;	//����ģʽ
	public static final int ALL_MODE = 2;	//����ģʽ
	public static final String TABLE = "T_BACKNUM";	//���ݿ��
	public static final String ID = "_id";	//��������id
	public static final String PHONENUM = "B_PHONENUM";	//����������
	public static final String MODE = "B_MODE";	//������ģʽ
	
	private Context context;	// ������
	private BackNumDbHelp dbHelp;	// ���ݿ�OpenHelp
	
	public BackNumDb(Context context) {
		dbHelp = new BackNumDbHelp(context);
		this.context = context;
	}
	
	/**
	 * ��ѯ���ݿ�������������뽵������
	 * @return ���ݿ��α�
	 */
	public Cursor queryBackNumTable() {
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		return db.query(TABLE, new String[]{ID, PHONENUM, MODE}, null, null, null, null, PHONENUM + " DESC");	
	}
	
	/**
	 * ��ѯ���ݿ��к�������,���������б��¼�ļ���
	 * @return �������ı��¼�ļ���
	 */
	public List<BackNumInfo> getAllData() {
		List<BackNumInfo> backNumList = new ArrayList<BackNumInfo>();	// ����һ�����ϴ�ź���������
		BackNumInfo temp = null;	// ����һ��������ʵ�����
		Cursor cursor = queryBackNumTable();	// ��ȡ��ѯ���α�
		while(cursor.moveToNext()) {
			temp = new BackNumInfo();
			temp.setId(cursor.getInt(cursor.getColumnIndex(ID)));
			temp.setNum(cursor.getString(cursor.getColumnIndex(PHONENUM)));
			temp.setMode(cursor.getInt(cursor.getColumnIndex(MODE)));
			backNumList.add(temp);
		}	
		cursor.close();	// �ر��α�
		return backNumList;
	}
	
	
	/**
	 * ���Ӻ��������¼
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
	 * �޸ĺ��������ģʽ
	 */
	public void updataBackNumMode(int id, int mode) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MODE, mode);
		db.update(TABLE, values, ID+"=?", new String[]{id+""});
		db.close();
	}
	
	/**
	 * ����ĳ��id��ȡ��������Ϣ
	 * @param id �����id���
	 * @return ���غ�������Ϣ
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
			c.close();	// �ر��α�
			return temp;
		}
		c.close();	// �ر��α�
		db.close();	// �ر����ݿ�����
		return null;
	}
	
	/**
	 * ɾ��������
	 */
	public void deleteBackNumMode(int id) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		db.delete(TABLE, ID+"=?", new String[]{id+""});
		db.close();
	}
	
	/**
	 * ���ݴ����limit��offset�ּ���ѯ
	 */
	public List<BackNumInfo> getDataByLimit(int limit, int offset) {
		List<BackNumInfo> data = null;
		SQLiteDatabase db = dbHelp.getReadableDatabase();	
		Cursor c = db.query(TABLE, new String[]{ID, PHONENUM, MODE}, null, null, null, null, null, offset + "," + limit);	// ���Ʋ�ѯ
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
	 * ���ݴ�����ֻ�����,��ѯ������ģʽ
	 * @param phoneNum ����ĵ绰����
	 * @return ��ѯ��������-1
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
