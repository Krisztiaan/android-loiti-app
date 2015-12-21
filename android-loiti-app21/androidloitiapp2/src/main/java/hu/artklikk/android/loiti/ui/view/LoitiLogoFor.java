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

public class LoitiLogoFor extends View {
	
	private static final float DESIRED_LOGORECT_RATIO = 112f / 291f;
	private static final int INITIAL_PADDING_PX = 2;
	
	private Paint paintStroke;
	private Paint paintFill;
	
	private Rect fullRect;
	private RectF logoRect;
	
	private RectF rectF;
	private RectF rectO;
	private RectF rectR;
	
	private Path pathF;
	private Path pathR;
	
	private int initialPadding;
	
	public LoitiLogoFor(Context context) {
		super(context);
		initialPadding = (int) (getContext().getResources().getDisplayMetrics().density * INITIAL_PADDING_PX);
	}
	
	public LoitiLogoFor(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialPadding = (int) (getContext().getResources().getDisplayMetrics().density * INITIAL_PADDING_PX);
	}
	
	public LoitiLogoFor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialPadding = (int) (getContext().getResources().getDisplayMetrics().density * INITIAL_PADDING_PX);
	}
	
	{
		paintStroke = new Paint();
		paintStroke.setStyle(Style.STROKE);
		paintStroke.setColor(getResources().getColor(R.color.loiti_gray));
		paintStroke.setAntiAlias(true);
		
		paintFill = new Paint();
		paintFill.setStyle(Style.FILL);
		paintFill.setColor(getResources().getColor(R.color.loiti_gray));
		
		fullRect = new Rect();
		logoRect = new RectF();
		
		rectF = new RectF();
		rectO = new RectF();
		rectR = new RectF();
		
		pathF = new Path();
		pathR = new Path();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getWidth() - getPaddingRight();
		int bottom = getHeight() - getPaddingBottom();
		
		fullRect.set(left + initialPadding, top + initialPadding, right - initialPadding, bottom - initialPadding);
		
		float rectRatio = (float) fullRect.height() / (float) fullRect.width();
		
		if (rectRatio == DESIRED_LOGORECT_RATIO) {
			logoRect.set(fullRect);
		}
		// tall
		else if (rectRatio > DESIRED_LOGORECT_RATIO) {
			float desiredHeight = fullRect.width() * DESIRED_LOGORECT_RATIO;
			float upper = fullRect.exactCenterY() - (desiredHeight / 2f);
			float lower = fullRect.exactCenterY() + (desiredHeight / 2f);
			logoRect.set(fullRect.left, upper, fullRect.right, lower);
			
		}
		// wide
		else if (rectRatio < DESIRED_LOGORECT_RATIO) {
			float desiredWidth = fullRect.height() / DESIRED_LOGORECT_RATIO;
			float leftDrawing = fullRect.exactCenterX() - (desiredWidth / 2f);
			float rightDrawing = fullRect.exactCenterX() + (desiredWidth / 2f);
			logoRect.set(leftDrawing, fullRect.top, rightDrawing, fullRect.bottom);
		}
		
		float heightWidthRatio = 18f / 99f;
		
		float spacing = logoRect.height() * heightWidthRatio;
		float stroke = spacing;
		
		paintStroke.setStrokeWidth(stroke);
		
		rectF.set(logoRect.left, logoRect.top, logoRect.left + spacing * 3, logoRect.bottom);
		rectO.set(rectF.right + spacing, logoRect.top, rectF.right + spacing + logoRect.height(), logoRect.bottom);
		rectR.set(rectO.right + spacing * 1.5f, logoRect.top, logoRect.right, logoRect.bottom);
		
		pathF.reset();
		pathF.moveTo(rectF.left, rectF.bottom);
		pathF.lineTo(rectF.left, rectF.top);
		pathF.lineTo(rectF.right, rectF.top);
		pathF.rLineTo(0, spacing);
		pathF.rLineTo(-(spacing * 2), 0);
		pathF.rLineTo(0, spacing);
		pathF.rLineTo(spacing * 2, 0);
		pathF.rLineTo(0, spacing);
		pathF.rLineTo(-(spacing * 2), 0);
		pathF.lineTo(rectF.left + spacing, rectF.bottom);
		pathF.close();
		
		// relative movement unit for the curves
		// this is 2.5 in svg
		float unitRCurve = spacing * 1.25f;
		// relative movement unit for Bezier curve control point
		// this is 1.1 in svg
		float smallMove = (unitRCurve / 25f) * 1.1f;
		// relative movement unit for Bezier curve control point
		// this is 1.4 in svg
		float largeMove = (unitRCurve / 25f) * 1.4f;
		// relative movement unit for horizontal movement before/after curve
		// this is 3 in svg
		float horizontalMove = unitRCurve * 1.2f;
		
		pathR.reset();
		pathR.moveTo(rectR.left, rectR.bottom);
		pathR.lineTo(rectR.left, rectR.top + stroke / 2);
		pathR.rLineTo(horizontalMove, 0);
		pathR.rCubicTo(largeMove, 0, unitRCurve, smallMove, unitRCurve, unitRCurve);
		pathR.rCubicTo(0, largeMove, -smallMove, unitRCurve, -unitRCurve, unitRCurve);
		pathR.rLineTo(-horizontalMove, 0);
		pathR.rMoveTo(horizontalMove, 0);
		pathR.rLineTo(horizontalMove, (unitRCurve * 2f));
		
		canvas.drawPath(pathF, paintFill);
		canvas.drawCircle(rectO.centerX(), rectO.centerY(), (logoRect.height() / 2) - (stroke / 2), paintStroke);
		canvas.drawPath(pathR, paintStroke);
		
	}
}
