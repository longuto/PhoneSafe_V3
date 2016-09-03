package com.longuto.phoneSafe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class RocketActivity extends Activity {
	private Context context;	// 上下文
	private ImageView mRocketIv;	// 火箭图片
	private Matrix mStartMatrix = new Matrix();	//开始矩阵
	private Matrix mCurrMatrix = new Matrix();	//当前矩阵
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mCurrMatrix.postTranslate(0, -15);
			mRocketIv.setImageMatrix(mCurrMatrix);
			super.handleMessage(msg);
		}	
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rocket);
		
		context = this;	
		mRocketIv = (ImageView) findViewById(R.id.rocket_iv);	// 火箭图片	
		// 火箭触摸事件
		mRocketIv.setOnTouchListener(new RocketOnTouchListener());		
	}
	
	/**
	 * 火箭触摸事件
	 * @author longuto
	 */
	private class RocketOnTouchListener implements View.OnTouchListener {	
		private Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);	//震动管理器
		private PointF pointF;	//点	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()&MotionEvent.ACTION_MASK) {
			// 按下
			case MotionEvent.ACTION_DOWN:
				vibrator.vibrate(100);
				pointF = new PointF(event.getY(), event.getY());	// 获取手机按下去点的位置
				mStartMatrix = mRocketIv.getMatrix();
				break;
			// 移动
			case MotionEvent.ACTION_MOVE:
				vibrator.vibrate(100);
				float dx = event.getX() - pointF.x;	//水平偏移量
				float dy = event.getY() - pointF.y;	//垂直偏移量
				mCurrMatrix.set(mStartMatrix);	// 起始位置
				mCurrMatrix.postTranslate(dx, dy);	// 移动
				break;
			// 弹起
			case MotionEvent.ACTION_UP:
				vibrator.cancel();	// 取消震动
				sendRocket();
				break;
			default:
				break;
			}
			mRocketIv.setImageMatrix(mCurrMatrix);
			return true;
		}
	}

	/**
	 * 发送火箭
	 */
	public void sendRocket() {
		new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					SystemClock.sleep(50);
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}	
}
