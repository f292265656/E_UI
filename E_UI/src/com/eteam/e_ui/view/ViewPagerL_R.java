package com.eteam.e_ui.view;

import java.util.ArrayList;
import java.util.List;

import chen.tools.DensityUtil;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerL_R extends ViewPager
{
	private static String TAG = "ViewPagerL_R";
	private Context context;
	private int currentIndex = 0;
	private ViewPagerAdapter adapter;
	private ViewPagerPageChangeListener vpcListener;

	public ViewPagerL_R(Context context)
	{
		super(context);
		this.context = context;
		init();

	}

	public ViewPagerL_R(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}

	protected void init()
	{
		adapter = new ViewPagerAdapter();
		vpcListener = new ViewPagerPageChangeListener();
		bindEvent();

	}

	protected void bindEvent()
	{
		setOnPageChangeListener(new ViewPagerPageChangeListener());
	}

	public void setViewDate(ArrayList<View> viewlist)
	{
		adapter.Setdata(viewlist);
		setAdapter(adapter);

	}

	// 禁用手势

	@Override
	public boolean onTouchEvent(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		return super.onTouchEvent(arg0);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0)
	{
		// TODO Auto-generated method stub

		int bx = 0;
		int state = PluginDrawerLayout.getDrawerState();
		if (arg0.getAction() == MotionEvent.ACTION_DOWN)
		{
			bx = (int) arg0.getX();
			int opWide = getWidth() / 10;
			switch (currentIndex)
			{
			case 0:
			
				if (state == PluginDrawerLayout.DrawerClosed)
				{

					// 当前view响应
					if (bx > (getWidth() - opWide))
					{
						return true;
					}
					// 子View处理
					else
					{
						return false;
					}
				} else if (state == PluginDrawerLayout.DrawerOpened
						|| state == PluginDrawerLayout.DrawerSlide)
				{
					return false;
				}
				break;
			case 1:
			case 2:
				// 当前view响应
				
				if (bx < opWide || bx > (getWidth() - opWide))
				{
					Log.i(TAG, " viewpager");
					return true;
				} else
				{
					Log.i(TAG, " child");
					return false;
				}
			default:
				break;
			}
		}
		return super.onInterceptTouchEvent(arg0);
	}

	public class ViewPagerAdapter extends PagerAdapter
	{
		private List<View> listData = new ArrayList<View>();

		// private List<String> titleData = new ArrayList<String>();

		public void Setdata(List<View> list)
		{
			this.listData = list;

		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return listData.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);

			((ViewPager) container).removeView(listData.get(position));

		}

		@Override
		public int getItemPosition(Object object)
		{
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
			// return POSITION_NONE;
		}

		/*
		 * @Override public CharSequence getPageTitle(int position) { // TODO
		 * Auto-generated method stub return titleData.get(position); }
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			// TODO Auto-generated method stub
			((ViewPager) container).addView(listData.get(position));
			return listData.get(position);

		}
	}

	private class ViewPagerPageChangeListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0)
		{
			// TODO Auto-generated method stub
			currentIndex = arg0;

		}

	}
}
