package hu.artklikk.android.loiti.ui.view;

import hu.artklikk.android.loiti.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class LoitiLogo extends View {
	
	private static final float DESIRED_LOGORECT_RATIO = 40f / 124f;
	
	private Paint paint;
	private Paint paintStroke;
	
	private Rect fullRect;
	private RectF logoRect;
	
	private RectF rectL;
	private RectF rectO;
	private RectF rectI1;
	private RectF rectT;
	private RectF rectI2;
	
	private Path pathL;
	private Path pathT;
	
	public LoitiLogo(Context context) {
		super(context);
	}
	
	public LoitiLogo(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public LoitiLogo(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	{
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(getResources().getColor(R.color.loiti_gray));
		
		paintStroke = new Paint();
		paintStroke.setStyle(Style.STROKE);
		paintStroke.setAntiAlias(true);
		paintStroke.setColor(getResources().getColor(R.color.loiti_gray));
		
		fullRect = new Rect();
		logoRect = new RectF();
		
		rectL = new RectF();
		rectO = new RectF();
		rectI1 = new RectF();
		rectT = new RectF();
		rectI2 = new RectF();
		
		pathL = new Path();
		pathT = new Path();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getWidth() - getPaddingRight();
		int bottom = getHeight() - getPaddingBottom();
		
		fullRect.set(left, top, right, bottom);
		
		float ratio = (float) fullRect.height() / (float) fullRect.width();
		
		if (ratio == DESIRED_LOGORECT_RATIO) {
			logoRect.set(fullRect);
		}
		// tall
		else if (ratio > DESIRED_LOGORECT_RATIO) {
			float desiredHeight = fullRect.width() * DESIRED_LOGORECT_RATIO;
			float upper = fullRect.exactCenterY() - (desiredHeight / 2f);
			float lower = fullRect.exactCenterY() + (desiredHeight / 2f);
			logoRect.set(fullRect.left, upper, fullRect.right, lower);
			
		}
		// wide
		else if (ratio < DESIRED_LOGORECT_RATIO) {
			float desiredWidth = fullRect.height() / DESIRED_LOGORECT_RATIO;
			float leftDrawing = fullRect.exactCenterX() - (desiredWidth / 2f);
			float rightDrawing = fullRect.exactCenterX() + (desiredWidth / 2f);
			logoRect.set(leftDrawing, fullRect.top, rightDrawing, fullRect.bottom);
		}
		
		float unit = logoRect.height() / 5f;
		
		paintStroke.setStrokeWidth(unit);
		
		rectL.set(logoRect.left, logoRect.top, logoRect.left + (unit * 2), logoRect.bottom);
		rectO.set(rectL.right + unit / 2, logoRect.top, rectL.right + unit / 2 + logoRect.height(), logoRect.bottom);
		rectI1.set(rectO.right + unit, logoRect.top, rectO.right + unit * 2, logoRect.bottom);
		rectT.set(rectI1.right + unit, logoRect.top, rectI1.right + unit * 4, logoRect.bottom);
		rectI2.set(rectT.right + unit, logoRect.top, rectT.right + unit * 2, logoRect.bottom);
		
		pathL.reset();
		pathL.moveTo(rectL.left, rectL.top);
		pathL.lineTo(rectL.left, rectL.bottom);
		pathL.lineTo(rectL.right, rectL.bottom);
		pathL.lineTo(rectL.right, rectL.bottom - unit);
		pathL.lineTo(rectL.centerX(), rectL.bottom - unit);
		pathL.lineTo(rectL.centerX(), rectL.top);
		pathL.close();
		
		pathT.reset();
		pathT.moveTo(rectT.left, rectT.top);
		pathT.lineTo(rectT.left, rectT.top + unit);
		pathT.lineTo(rectT.left + unit, rectT.top + unit);
		pathT.lineTo(rectT.left + unit, rectT.bottom);
		pathT.lineTo(rectT.right - unit, rectT.bottom);
		pathT.lineTo(rectT.right - unit, rectT.top + unit);
		pathT.lineTo(rectT.right, rectT.top + unit);
		pathT.lineTo(rectT.right, rectT.top);
		pathT.close();
		
		canvas.drawPath(pathL, paint);
		canvas.drawCircle(rectO.centerX(), rectO.centerY(), unit * 2f, paintStroke);
		canvas.drawPoint(rectO.left, rectO.top, paint);
		canvas.drawPoint(rectO.right, rectO.bottom, paint);
		canvas.drawRect(rectI1, paint);
		canvas.drawPath(pathT, paint);
		canvas.drawRect(rectI2, paint);
	}
}
