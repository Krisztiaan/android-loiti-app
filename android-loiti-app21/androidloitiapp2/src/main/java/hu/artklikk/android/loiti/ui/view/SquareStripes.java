package hu.artklikk.android.loiti.ui.view;

import hu.artklikk.android.loiti.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class SquareStripes extends Stripes {
	
	private enum SquareBy {
		WIDTH,
		HEIGHT,
		SMALLEST;
	}
	
	private SquareBy squareBy;
	
	public SquareStripes(Context context) {
		super(context);
		squareBy = SquareBy.SMALLEST;
	}
	
	public SquareStripes(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public SquareStripes(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}
	
	private void init(AttributeSet attrs) {
		TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SquareStripes);
		int squareByInt = array.getInt(R.styleable.SquareStripes_squareBy, 2);
		switch (squareByInt) {
			case 0:
				squareBy = SquareBy.WIDTH;
				break;
			case 1:
				squareBy = SquareBy.HEIGHT;
				break;
			default:
				squareBy = SquareBy.SMALLEST;
				break;
		}
		array.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int size;
		if (widthMode == MeasureSpec.EXACTLY && widthSize == 0) {
			size = widthSize;
		}
		else if (heightMode == MeasureSpec.EXACTLY && heightSize == 0) {
			size = heightSize;
		}
		else {
			
			switch (squareBy) {
				case WIDTH:
					size = widthSize;
					break;
				case HEIGHT:
					size = heightSize;
					break;
				case SMALLEST:
					size = widthSize < heightSize ? widthSize : heightSize;
					break;
				default:
					size = widthSize < heightSize ? widthSize : heightSize;
					break;
			}
			
		}
		int finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
		super.onMeasure(finalMeasureSpec, finalMeasureSpec);
	}
	
}
