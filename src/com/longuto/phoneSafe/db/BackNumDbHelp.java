package com.longuto.phoneSafe.db;

import com.longuto.phoneSafe.util.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 关于手机黑名单数据库的DbHelp创建
 * @author longuto
 */
public class BackNumDbHelp extends SQLiteOpenHelper {
	public static final String DATABASENAME = "phoneSafe.db";
	public static final int VERSION = 6;
	private static final String TAG = "BackNumDbHelp"; 
	
	private Context context;	// 上下文

	public BackNumDbHelp(Context context) {
		super(context, DATABASENAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.v(TAG, "onCreate");
		String sql = "CREATE TABLE T_BACKNUM (_id INTEGER PRIMARY KEY AUTOINCREMENT, B_PHONENUM TEXT NOT NULL, B_MODE INTEGER NOT NULL)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtil.v(TAG, "onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS T_BACKNUM");
		db.execSQL("CREATE TABLE IF NOT EXISTS T_BACKNUM(_id INTEGER PRIMARY KEY AUTOINCREMENT, B_PHONENUM TEXT UNIQUE NOT NULL, B_MODE INTEGER NOT NULL)");
	}

}
