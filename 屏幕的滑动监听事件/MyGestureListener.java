package com.example.testandroid.screenfling;

import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class MyGestureListener implements OnGestureListener {
	private MyRightLeftListener listener;
	// ��ֱ�����ƶ��ľ��룬����ֵ
	private float distanceY;
	// ˮƽ�����ƶ��ľ��룬����ֵ
	private float distanceX;
	// �ƶ�������������ֵʱ���Ŵ���������Ļ�ļ���
	private float distance = 100;

	public MyGestureListener(MyRightLeftListener listener) {
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	/**
	 * һ�����������£������ϲ���onDown�¼�
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ����˴�����������û���ƶ��͵���Ķ���onShowPress��onDown���������� onDown�ǣ�
	 * һ�����������£������ϲ���onDown�¼�������onShowPress��onDown�¼�������
	 * һ��ʱ���ڣ����û���ƶ����͵����¼�������Ϊ��onShowPress�¼���
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * ����������󣬵��������������в�onLongPress��onScroll��onFling�¼��� �Ͳ��� ����onSingleTapUp�¼���
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ��������Ļ�ϻ��������д�����������onFlingһ����ע�����ߵ�����
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ������Ļʱ����
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	/**
	 * ��������Ļ�ϻ��������뿪��Ļʱ����
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// ����ʱ��x����
		float startX = e1.getX();
		// ����ʱ��y����
		float startY = e1.getY();
		// ̧��ʱ��x����
		float endtX = e2.getX();
		// ̧��ʱ��y����
		float endtY = e2.getY();

		// ˮƽ�����ƶ��ľ��룬����ֵ
		distanceX = Math.abs(endtX - startX);
		// ��ֱ�����ƶ��ľ��룬����ֵ
		distanceY = Math.abs(endtY - startY);
		// �����ж���ˮƽ�����ƶ����Ǵ�ֱ�����ƶ�
		if (distanceX > distanceY) {
			// ˵��ˮƽ�����ƶ��ľ���Զ�����϶�Ϊˮƽ�ƶ�
			if (distanceX > distance) {
				// ˮƽ�����ƶ�������Ĭ�Ͼ����ǲŴ������������¼�
				if (startX > endtX) {
					// ��ָ�������󻬶�
					Log.e("tag", "��ָ�������󻬶�");
					listener.onLeft();
				} else {
					// ��ָ�������һ���
					Log.e("tag", "��ָ�������һ���");
					listener.onRight();
				}
			}
		} else if (distanceX < distanceY) {
			// ˵����ֱ�����ƶ��ľ���Զ�����϶�Ϊ��ֱ�ƶ�
			if (distanceY > distance) {
				// ��ֱ�����ƶ�������Ĭ�Ͼ����ǲŴ������������¼�
				if (startY > endtY) {
					// ��ָ�������ϻ���
					Log.e("tag", "��ָ�������ϻ���");
					listener.onUp();
				} else {
					// ��ָ�������»���
					Log.e("tag", "��ָ�������»���");
					listener.onDown();
				}
			}
		} else {
			// ˮƽ����ʹ�ֱ�����ƶ�������ͬʱ��Ĭ��Ϊˮƽ������ƶ�
			if (distanceX > distance) {
				// ˮƽ�����ƶ�������Ĭ�Ͼ����ǲŴ������������¼�
				if (startX > endtX) {
					// ��ָ�������󻬶�
					Log.e("tag", "��ָ�������󻬶�");
					listener.onLeft();
				} else {
					// ��ָ�������һ���
					Log.e("tag", "��ָ�������һ���");
					listener.onRight();
				}
			}
		}
		return false;
	}

	public interface MyRightLeftListener {
		/**
		 * ��ָ�������һ���
		 */
		void onRight();

		/**
		 * ��ָ�������󻬶�
		 */
		void onLeft();
		/**
		 * ��ָ�������ϻ���
		 */
		void onUp();
		/**
		 * ��ָ�������»���
		 */
		void onDown();
	}

}
