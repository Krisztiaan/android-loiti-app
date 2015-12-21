package hu.artklikk.android.loiti.ui.view;

import hu.artklikk.android.loiti.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public class Stripes extends View {
	
	private static final int DEFAULT_STRIPE_PATTERN_SIZE = 11;
	private static final int MINIMUM_STRIPE_PATTERN_SIZE = 5;
	
	private int color;
	private boolean isOrientationUp;
	private int stripeSize;
	
	private Bitmap bitmap;
	private BitmapDrawable bitmapDrawable;
	
	public Stripes(Context context) {
		super(context);
		color = Color.BLACK;
		isOrientationUp = true;
		stripeSize = DEFAULT_STRIPE_PATTERN_SIZE;
		init();
	}
	
	public Stripes(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public Stripes(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}
	
	private void init(AttributeSet attrs) {
		TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.Stripes);
		color = array.getColor(R.styleable.Stripes_stripeColor, Color.BLACK);
		isOrientationUp = array.getInt(R.styleable.Stripes_orientation, 0) == 0;
		stripeSize = array.getDimensionPixelSize(R.styleable.Stripes_stripeSize, DEFAULT_STRIPE_PATTERN_SIZE);
		if (stripeSize < MINIMUM_STRIPE_PATTERN_SIZE) {
			stripeSize = MINIMUM_STRIPE_PATTERN_SIZE;
		}
		else if (stripeSize % 2 == 0) {
			++stripeSize;
		}
		array.recycle();
		init();
	}
	
	private void init() {
		
		bitmap = Bitmap.createBitmap(stripeSize, stripeSize, Config.ARGB_8888);
		
		if (isOrientationUp) {
			
			int i = 0;
			int j = stripeSize / 2;
			
			while (j >= 0) {
				bitmap.setPixel(i, j, color);
				bitmap.setPixel(stripeSize - 1 - j, stripeSize - 1 - i, color);
				--j;
				if (j < 0) {
					break;
				}
				bitmap.setPixel(i, j, color);
				bitmap.setPixel(stripeSize - 1 - j, stripeSize - 1 - i, color);
				++i;
			}
		}
		else {
			
			int i = 0;
			int j = stripeSize / 2;
			while (j < stripeSize) {
				bitmap.setPixel(i, j, color);
				bitmap.setPixel(j, i, color);
				++j;
				if (j >= stripeSize) {
					break;
				}
				bitmap.setPixel(i, j, color);
				bitmap.setPixel(j, i, color);
				++i;
			}
		}
		bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		bitmapDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		bitmapDrawable.setBounds(0, 0, getWidth(), getHeight());
		bitmapDrawable.draw(canvas);
		
	}
	
}
