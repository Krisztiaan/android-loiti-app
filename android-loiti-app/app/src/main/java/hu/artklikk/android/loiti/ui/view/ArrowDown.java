package hu.artklikk.android.loiti.ui.view;

import hu.artklikk.android.loiti.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ArrowDown extends View {
	
	private Paint paint;
	private Path path;
	private Rect rect;
	
	public ArrowDown(Context context) {
		super(context);
	}
	
	public ArrowDown(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ArrowDown(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	{
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Style.STROKE);
		paint.setColor(getResources().getColor(R.color.loiti_orange));
		paint.setStrokeWidth(3);
		
		path = new Path();
		
		rect = new Rect();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getWidth() - getPaddingRight();
		int bottom = getHeight() - getPaddingBottom();
		
		rect.set(left, top, right, bottom);
		int heightDouble = rect.height() * 2;
		
		path.reset();
		
		if (heightDouble == rect.width()) {
			path.moveTo(rect.left, rect.top);
			path.lineTo(rect.centerX(), rect.bottom);
			path.lineTo(rect.right, rect.top);
		}
		
		else if (heightDouble > rect.width()) {
			int diff = rect.width() / 4;
			int halfHeight = (rect.height() / 2) + top;
			int upper = halfHeight - diff;
			int lower = halfHeight + diff;
			
			path.moveTo(rect.left, upper);
			path.lineTo(rect.centerX(), lower);
			path.lineTo(rect.right, upper);
		}
		else if (heightDouble < rect.width()) {
			int diff = rect.height();
			int leftDrawing = rect.centerX() - diff;
			int rightDrawing = rect.centerX() + diff;
			
			path.moveTo(leftDrawing, rect.top);
			path.lineTo(rect.centerX(), rect.bottom);
			path.lineTo(rightDrawing, rect.top);
		}
		
		canvas.drawPath(path, paint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int desiredWidth = 1000;
		int desiredHeight = 500;
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int width = desiredWidth;
		int height = desiredHeight;
		
		if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
			width = widthSize;
			height = heightSize;
		}
		else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST) {
			width = widthSize;
			if ((heightSize * 2) >= widthSize) {
				height = width / 2;
			}
			else {
				height = heightSize;
			}
		}
		else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
			height = width / 2;
		}
		else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
			if ((heightSize * 2) >= widthSize) {
				width = height * 2;
			}
			else {
				width = widthSize;
			}
		}
		else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
			if ((heightSize * 2) >= widthSize) {
				width = widthSize;
				height = widthSize / 2;
			}
			else {
				height = heightSize;
				width = height * 2;
			}
		}
		else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
			height = width / 2;
		}
		else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
			width = height * 2;
		}
		else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.AT_MOST) {
			height = heightSize;
			width = height * 2;
		}
		else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
			height = desiredHeight;
			width = desiredWidth;
		}
		
		setMeasuredDimension(width, height);
	}
	
}
