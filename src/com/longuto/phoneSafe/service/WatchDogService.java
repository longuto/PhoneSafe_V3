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
	private ActivityManager am;	// Activity������
	private boolean flag;	// ��Ϊ�Ƿ����������ı��
	private Context context;	// ������
	private String unlockPackageName;	// WatchDogActivity�ѽ����İ���
	private int unlockTaskId;	// WatchDogActivity�ѽ�����TaskId
	private UnlockBroadcastReceiver unlockReceiver;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		
		// ע��㲥������
		unlockReceiver = new UnlockBroadcastReceiver();	// ��ʼ���㲥������
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.longuto.phoneSafe.unlock");
		registerReceiver(unlockReceiver, filter);
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flag = true;	// onStartCommand�п���������		
		// �������߳�,ִ�к�ʱ����
		new Thread() {
			public void run() {
				while(flag) {
					// ��ȡ���ջ����
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					for (RunningTaskInfo temp : runningTasks) {
						String packageName = temp.baseActivity.getPackageName();
						System.out.println("��ǰ���еİ��� : " + packageName);
						// �����ݿ��в�ѯ�Ƿ��д˰�������
						boolean isLock = new WatchDogDb(context).queryWatchDogByPackName(packageName);
						if(isLock) {
							int lockTaskId = temp.id;
							if(!(lockTaskId == unlockTaskId && packageName.equals(unlockPackageName))) {
								Intent intent = new Intent(context, WatchDogActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("lockPackageName", packageName);	// ���ӵ�ǰ������ʱ�İ���
								intent.putExtra("lockTaskId", lockTaskId);	// ���ӵ�ǰ�����İ���TaskID
								startActivity(intent);								
							} 
						}
					}

					SystemClock.sleep(1000);	// ����1s
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
	 * ע��㲥������ : ��WatchDogActivity����ʱ,���ܹ㲥
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
