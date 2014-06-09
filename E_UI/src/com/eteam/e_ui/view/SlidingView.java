package com.eteam.e_ui.view;

import chen.tools.DensityUtil;
import android.content.Context;
import android.drm.DrmStore.Action;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SlidingView extends ViewGroup
{
	private static String TAG = "SlidingView";
	private Context context = null;
	public int height = 0, wide = 0;
	private final int PanelHeight = 75; // dp
	//第一层(内容)---第二层(topPanel---内容)
	private SlidingViewLayoutParams contenLayoutParams,secondContentParams;
	

	protected ViewDragHelper drageHelper; // 滑动辅助类
	protected boolean isDraged = false; // 滑动状态
	protected int PanelHeightToDp;

	public SlidingView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public SlidingView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onFinishInflate()
	{
		// TODO Auto-generated method stub
		super.onFinishInflate();
		drageHelper = ViewDragHelper.create(this, 1.0f, new DragHelper());

	}

	private void init()
	{
		PanelHeightToDp=DensityUtil.dip2px(context,PanelHeight);
		if (contenLayoutParams == null)
		{
			contenLayoutParams = new SlidingViewLayoutParams(0, 0, wide,
					height);
			
		}
		if(secondContentParams==null)
		{
			secondContentParams=new SlidingViewLayoutParams(0, height, wide, 2*height);
		}
		
	}

	
	// ------------------------------------绘图-------------------------------
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO Auto-generated method stub

		int childCount = getChildCount();
		
		if (childCount > 2)
		{
			Log.i(TAG, "childCount should be less than 2 ! ");
		} else
		{
			for (int i = 0; i < childCount; i++)
			{
				getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
			}
			int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
			int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
			setMeasuredDimension(measuredWidth, measuredHeight);
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		wide = getWidth();
		height = getHeight();
		init();
		
		// TODO Auto-generated method stub
		for (int i = 0; i < getChildCount(); i++)
		{
			if (i == 0)
			{
				getChildAt(i).layout(contenLayoutParams.left,
						contenLayoutParams.top, contenLayoutParams.right,
						contenLayoutParams.bottom);
			} 
			else if(i==1)
			{
				getChildAt(i).layout(secondContentParams.left,
						secondContentParams.top,
						secondContentParams.right,
						secondContentParams.bottom);
			}		
		}

	}

	@Override
	public void computeScroll()
	{
		// TODO Auto-generated method stub
		if (drageHelper.continueSettling(true))
		{
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	// ---------------------------------Touch事件---------------------------------------

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
	/*	if (event.getAction() == MotionEvent.ACTION_DOWN
				&& event.getX() < (wide / 10))
		{
			return false;
		}*/
		return super.onTouchEvent(event);
	}

	public class SlidingViewLayoutParams
	{
		public int left = 0, top = 0, right = 0, bottom = 0;

		public SlidingViewLayoutParams(int l, int t, int r, int b)
		{
			left = l;
			top = t;
			right = r;
			bottom = b;
		}
		public void ChangeLayoutParams(int l, int t, int r, int b)
		{
			left = l;
			top = t;
			right = r;
			bottom = b;
		}

	}
	
	
	

	protected class DragHelper extends ViewDragHelper.Callback
	{

		@Override
		public boolean tryCaptureView(View arg0, int arg1)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getViewVerticalDragRange(View child)
		{
			// TODO Auto-generated method stub
			return height;
		}
	}
	
	//-------------------------------------------------外部调用--------------------------------------------
	public boolean getDragedState()
	{
		return isDraged;
	}
	
	public void expand()
	{
		if(!getDragedState())
		{
			isDraged=true;
			drageHelper.smoothSlideViewTo(getChildAt(1), 0, 0);
			ViewCompat.postInvalidateOnAnimation(this);
			secondContentParams.ChangeLayoutParams(0, 0, wide, height);
		}
	}
	public void  shrink()
	{
		if(getDragedState())
		{
			isDraged=false;
			drageHelper.smoothSlideViewTo(getChildAt(1), 0, height);
			ViewCompat.postInvalidateOnAnimation(this);
			secondContentParams.ChangeLayoutParams(0, height, wide,2* height);
		}
	}
}
