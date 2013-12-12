package com.youer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class PhotoGallery extends Gallery {

	public PhotoGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PhotoGallery(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
	}

	public PhotoGallery(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);

	}
	private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2)
	{
		float f2 = paramMotionEvent2.getX();
		float f1 = paramMotionEvent1.getX();
		if (f2 > f1)
			return true;
		return false;
	}
	@Override
	 public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	  // TODO Auto-generated method stub
	  return super.onScroll(e1, e2, (float) (distanceX*0.2), distanceY);
	 }
	public boolean onFling(MotionEvent e1,
			MotionEvent e2, float paramFloat1, float paramFloat2)
	{
//		if(!isShown())
//			return false;
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
	          // Check if scrolling left
	          kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
	      } else {
	         // Otherwise scrolling right
	         kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
	      }
	      onKeyDown(kEvent, null);
	      return true;
	}
}
