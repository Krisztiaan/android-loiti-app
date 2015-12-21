package hu.artklikk.android.loiti.ui.view;

import hu.artklikk.android.loiti.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MenuButton extends View {
	
	private Paint paint;
	private RectF rect;
	private Rect paddingRect;
	
	public MenuButton(Context context) {
		super(context);
	}
	
	public MenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	{
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Style.STROKE);
		paint.setColor(getResources().getColor(R.color.loiti_white));
		paint.setStrokeWidth(3);
		
		rect = new RectF();
		paddingRect = new Rect();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getWidth() - getPaddingRight();
		int bottom = getHeight() - getPaddingBottom();
		
		paddingRect.set(left, top, right, bottom);
		
		if (paddingRect.height() == paddingRect.width()) {
			rect.set(left, top, right, bottom);
		}
		
		else if (paddingRect.height() > paddingRect.width()) {
			float diff = (paddingRect.width() / 2);
			rect.set(paddingRect.left, paddingRect.exactCenterY() - diff, paddingRect.right, paddingRect.exactCenterY()
					+ diff);
		}
		else if (paddingRect.height() < paddingRect.width()) {
			float diff = (paddingRect.height() / 2);
			rect.set(paddingRect.exactCenterX() - diff, paddingRect.top, paddingRect.exactCenterX() + diff,
					paddingRect.bottom);
		}
		
		canvas.drawLine(rect.left, rect.top, rect.left, rect.bottom, paint);
		canvas.drawLine(rect.centerX(), rect.top, rect.centerX(), rect.bottom, paint);
		canvas.drawLine(rect.right, rect.top, rect.right, rect.bottom, paint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int desiredSize = 1000;
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int width = desiredSize;
		int height = desiredSize;
		
		if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
			width = widthSize;
			height = heightSize;
		}
		else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST) {
			width = widthSize;
			if (heightSize >= widthSize) {
				height = widthSize;
			}
			else {
				height = heightSize;
			}
		}
		else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
			height = widthSize;
		}
		else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
			if (widthSize >= heightSize) {
				width = heightSize;
			}
			else {
				width = widthSize;
			}
		}
		else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
			if (heightSize == widthSize) {
				width = widthSize;
				height = heightSize;
			}
			else if (heightSize > widthSize) {
				height = widthSize;
				width = widthSize;
			}
			else {
				height = heightSize;
				width = heightSize;
			}
		}
		else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
			height = widthSize;
		}
		else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
			width = heightSize;
		}
		else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.AT_MOST) {
			height = heightSize;
			width = heightSize;
		}
		else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
			height = desiredSize;
			width = desiredSize;
		}
		
		setMeasuredDimension(width, height);
	}
	
}
