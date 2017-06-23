package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
	private String text;
	private float textSize;
	private float paddingLeft;
	private float paddingRight;
	private float paddingTop;
	private float paddingBottom;
	private int textColor;
	private Paint paint1 = new Paint();
	private float textShowWidth;

	private int name_count;
	private Paint paint2 = new Paint();

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		text = this.getText().toString();
		textSize = this.getTextSize();
		textColor = this.getTextColors().getDefaultColor();
		paddingLeft = this.getPaddingLeft();
		paddingRight = this.getPaddingRight();
		paddingTop = this.getPaddingTop();
		paddingBottom = this.getPaddingBottom();
		paint1.setTextSize(textSize);
		paint1.setColor(textColor);
		paint1.setAntiAlias(true);

	}

	/**
	 * 设置名字的字数，要改变字体颜色
	 * 
	 * @param count
	 */
	public void setNameCount(int count) {
		name_count = count;
	}

	/**
	 * 设置名字的颜色
	 * 
	 * @param color
	 */
	public void setNameColor(int color) {
		paint2.setTextSize(textSize);
		paint2.setColor(color);
		paint2.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		textShowWidth = this.getMeasuredWidth() - paddingLeft - paddingRight;
		int lineCount = 0;
		text = this.getText().toString();
		if (text == null)
			return;
		char[] textCharArray = text.toCharArray();
		float drawedWidth = 0;
		float charWidth;
		for (int i = 0; i < textCharArray.length; i++) {
			charWidth = paint1.measureText(textCharArray, i, 1);
			if (textCharArray[i] == '\n') {
				lineCount++;
				drawedWidth = 0;
				continue;
			}
			if (textShowWidth - drawedWidth < charWidth) {
				lineCount++;
				drawedWidth = 0;
			}
			if (i < name_count) {
				canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth,
						(lineCount + 1) * textSize, paint2);
			} else {
				canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth,
						(lineCount + 1) * textSize, paint1);
			}
			drawedWidth += charWidth;
		}
		setHeight((int) ((lineCount + 1) * (int) textSize)
				+ (int) paddingBottom + (int) paddingTop);
	}
}