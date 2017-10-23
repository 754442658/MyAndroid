package com.example.testandroid.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.testandroid.R;
import com.example.testandroid.screenfling.MyGestureListener;
import com.example.testandroid.screenfling.MyGestureListener.MyRightLeftListener;

public class FlingActivity extends Activity {

	GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fling);
		detector = new GestureDetector(this, new MyGestureListener(
				new MyRightLeftListener() {
					@Override
					public void onRight() {
						findViewById(R.id.activity_fling_tv)
								.setBackgroundColor(Color.GREEN);
					}

					@Override
					public void onLeft() {
						findViewById(R.id.activity_fling_tv)
								.setBackgroundColor(Color.RED);

					}

					@Override
					public void onUp() {
						// TODO Auto-generated method stub
						findViewById(R.id.activity_fling_tv)
								.setBackgroundColor(Color.BLUE);
					}

					@Override
					public void onDown() {
						// TODO Auto-generated method stub
						findViewById(R.id.activity_fling_tv)
								.setBackgroundColor(Color.YELLOW);
					}
				}));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}

}
