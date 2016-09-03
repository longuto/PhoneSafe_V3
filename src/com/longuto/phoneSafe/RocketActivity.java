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
	private Context context;	// ������
	private ImageView mRocketIv;	// ���ͼƬ
	private Matrix mStartMatrix = new Matrix();	//��ʼ����
	private Matrix mCurrMatrix = new Matrix();	//��ǰ����
	
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
		mRocketIv = (ImageView) findViewById(R.id.rocket_iv);	// ���ͼƬ	
		// ��������¼�
		mRocketIv.setOnTouchListener(new RocketOnTouchListener());		
	}
	
	/**
	 * ��������¼�
	 * @author longuto
	 */
	private class RocketOnTouchListener implements View.OnTouchListener {	
		private Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);	//�𶯹�����
		private PointF pointF;	//��	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()&MotionEvent.ACTION_MASK) {
			// ����
			case MotionEvent.ACTION_DOWN:
				vibrator.vibrate(100);
				pointF = new PointF(event.getY(), event.getY());	// ��ȡ�ֻ�����ȥ���λ��
				mStartMatrix = mRocketIv.getMatrix();
				break;
			// �ƶ�
			case MotionEvent.ACTION_MOVE:
				vibrator.vibrate(100);
				float dx = event.getX() - pointF.x;	//ˮƽƫ����
				float dy = event.getY() - pointF.y;	//��ֱƫ����
				mCurrMatrix.set(mStartMatrix);	// ��ʼλ��
				mCurrMatrix.postTranslate(dx, dy);	// �ƶ�
				break;
			// ����
			case MotionEvent.ACTION_UP:
				vibrator.cancel();	// ȡ����
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
	 * ���ͻ��
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
