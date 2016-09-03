package com.longuto.phoneSafe.service;

import java.util.List;

import com.longuto.phoneSafe.WatchDogActivity;
import com.longuto.phoneSafe.db.dao.WatchDogDb;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

public class WatchDogService extends Service {
	private ActivityManager am;	// Activity管理者
	private boolean flag;	// 作为是否开启防盗狗的标记
	private Context context;	// 上下文
	private String unlockPackageName;	// WatchDogActivity已解锁的包名
	private int unlockTaskId;	// WatchDogActivity已解锁的TaskId
	private UnlockBroadcastReceiver unlockReceiver;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		
		// 注册广播接收器
		unlockReceiver = new UnlockBroadcastReceiver();	// 初始化广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.longuto.phoneSafe.unlock");
		registerReceiver(unlockReceiver, filter);
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flag = true;	// onStartCommand中开启防盗狗		
		// 开启子线程,执行耗时操作
		new Thread() {
			public void run() {
				while(flag) {
					// 获取最大栈集合
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					for (RunningTaskInfo temp : runningTasks) {
						String packageName = temp.baseActivity.getPackageName();
						System.out.println("当前运行的包名 : " + packageName);
						// 在数据库中查询是否含有此包名数据
						boolean isLock = new WatchDogDb(context).queryWatchDogByPackName(packageName);
						if(isLock) {
							int lockTaskId = temp.id;
							if(!(lockTaskId == unlockTaskId && packageName.equals(unlockPackageName))) {
								Intent intent = new Intent(context, WatchDogActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("lockPackageName", packageName);	// 附加当前锁定包时的包名
								intent.putExtra("lockTaskId", lockTaskId);	// 附加当前锁定的包的TaskID
								startActivity(intent);								
							} 
						}
					}

					SystemClock.sleep(1000);	// 休眠1s
				}
			}
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(unlockReceiver);
		super.onDestroy();
	}

	/**
	 * 注册广播接收器 : 当WatchDogActivity解锁时,接受广播
	 * @author Administrator
	 */
	private class UnlockBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			unlockPackageName = intent.getStringExtra("unlockPackage");
			unlockTaskId = intent.getIntExtra("unlockTask", 0);
		}
		
	}
}
