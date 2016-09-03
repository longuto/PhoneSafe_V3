package com.longuto.phoneSafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.longuto.phoneSafe.db.WatchDogDbHelp;

/**
 * ���Ź������ݿ����
 * @author longuto
 */
public class WatchDogDb {
	public static final String TABLE = "T_WATCHDOG";	//���ݿ��
	public static final String ID = "_id";	//���˿��Ź������id
	public static final String PACKNAME = "W_PACKAGENAME";	//���˿��Ź�����İ���
	
	private Context context;	// ������
	private WatchDogDbHelp dbHelp;	// ���ݿ�OpenHelp
	
	public WatchDogDb(Context context) {
		dbHelp = new WatchDogDbHelp(context);
		this.context = context;
	}
	
	/**
	 * ���հ�����ѯָ��������Ƿ����
	 * @param packName ָ���İ���
	 * @return ���ڷ���true, ���򷵻�false
	 */
	public boolean queryWatchDogByPackName(String packName) {
		boolean flag = false;
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query(TABLE, new String[]{ID, PACKNAME}, PACKNAME + "=?", new String[]{packName}, null, null, null);
		if(cursor.moveToNext()) {
			flag = true;
		}
		cursor.close();	// �ر��α�
		db.close();	// �ر�DataBase
		return flag;
	}
	
	/**
	 * ����ָ������,����������ݿ�
	 * @param packName ָ������
	 */
	public void addWatchDogByPackName(String packName) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PACKNAME, packName);
		db.insert(TABLE, null, values);
		db.close();
	}
	
	/**
	 * ����ָ���İ���,��������ݿ���ɾ��
	 * @param packName ָ������
	 */
	public void delWatchDogByPackName(String packName) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();	
		db.delete(TABLE, PACKNAME + "=?", new String[]{packName});
		db.close();
	}
}
