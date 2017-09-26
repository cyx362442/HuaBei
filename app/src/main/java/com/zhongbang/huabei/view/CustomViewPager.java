package com.zhongbang.huabei.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 
 * @author Administrator
 * 不可手动滑动，只能点击滑动的ViewPager
 */
public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	boolean isScrollable;
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	if (isScrollable == false) {
	return false;
	} else {
	return super.onTouchEvent(ev);
	}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	if (isScrollable == false) {
	return false;
	} else {
	return super.onInterceptTouchEvent(ev);
	}

	}
}