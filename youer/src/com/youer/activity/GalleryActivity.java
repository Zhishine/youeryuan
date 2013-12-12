package com.youer.activity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.youer.adapter.GalleryAdapter;
import com.youer.modal.MImage;
import com.youer.modal.MVideo;
import com.youer.tool.AppDataManager;
import com.youer.tool.DensityUtil;
import com.youer.view.FullScreenVideoView;
import com.youer.view.PhotoGallery;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

public class GalleryActivity extends Activity implements OnClickListener {

	 List<MImage> m_galleryList=null;
	 ImageView m_view=null;
	 int m_currentIndex=0;
	 LinearLayout m_popupView=null;

	 View m_top=null;
	 boolean m_fullScreen=false;
	 LinearLayout view;
	 int m_width;
		int m_height;
		PhotoGallery gallery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		this.m_width=DensityUtil.getLogicalWidth();
		this.m_height=DensityUtil.getLogicalHeight();
		MVideo video=(MVideo) getIntent().getExtras().getSerializable("video");
		ImageView back=(ImageView) findViewById(R.id.back);
		String indexStr=getIntent().getStringExtra("index");
		int index=Integer.valueOf(indexStr);
		m_top=findViewById(R.id.top_bg);
		m_galleryList=(List<MImage>)getIntent().getSerializableExtra("imageList");
	    gallery=(PhotoGallery) findViewById(R.id.banner_gallery);
	    view=(LinearLayout) findViewById(R.id.gallery_container);
		view.setClickable(true);
		view.setOnClickListener(this);
		//m_top.setBackgroundColor(Color.RED);
		GalleryAdapter adapter=new GalleryAdapter(this,m_galleryList);
		gallery.setAdapter(adapter);
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> adpter, View view,
					int index, long arg3) {
				// TODO Auto-generated method stub
				
				m_currentIndex=index;
				LinearLayout linear=(LinearLayout)view;
				m_view=(ImageView)linear.getChildAt(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		gallery.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				MImage gallery=m_galleryList.get(index);
			
				GalleryActivity.this.updateFullScreen();
				
			}
			
		});
		
		gallery.setSelection(index);
		
		back.setOnClickListener(this);
	
         m_popupView=(LinearLayout) findViewById(R.id.popup);
   		m_popupView.setOnClickListener(this);
   		m_popupView.setVisibility(View.INVISIBLE);
   		ImageView expand=(ImageView) findViewById(R.id.expand);
   		expand.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View arg0) {
   				// TODO Auto-generated method stub
   				updatePopup();
   			}}); 
	}
	public int getBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
	void updatePopup(){
  	  if(m_popupView.getVisibility()==View.INVISIBLE)
  	  {
		    	m_popupView.setVisibility(View.VISIBLE);
		    	fadeIn(m_popupView, 500);
  	  }
		    else {
		    	//m_popupView.setVisibility(View.INVISIBLE);
		    	m_popupView.setVisibility(View.INVISIBLE);
		    }
  }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back:
			this.finish();
			break;
		case R.id.download_container:
			updatePopup();
			String systemImageDir = "/sdcard/meinvqiushi/image/";
			m_view.setDrawingCacheEnabled(true);
			Bitmap  bitmap = Bitmap.createBitmap(m_view.getDrawingCache());
			m_view.setDrawingCacheEnabled(false);
			SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
			formatter.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			long ms = System.currentTimeMillis();
			String dt = formatter.format(ms);
			String fileName = dt + "_" + ms + ".png";
			if( AppDataManager.getInstance().SaveImage(bitmap, fileName)){
				//success
				//Log.i("downLoadImage", "downLoadImage click"+fileName);
				String msg ="成功下载" + ":" + systemImageDir;
				Toast toast=Toast.makeText(this, msg, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}else{
				//fail
				Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
			}
			break;	
		
		case R.id.share_container:
			updatePopup();
			
			break;	
         case R.id.popup:
        		m_popupView.setVisibility(View.INVISIBLE);
			break;
         case R.id.gallery_container:
        	 updateFullScreen();
        	 break;
		}
		
	}
	
	public void updateFullScreen(){
		if(m_fullScreen){
			FrameLayout.LayoutParams lp=(android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
			lp.topMargin=DensityUtil.dip2px(56);
			view.setLayoutParams(lp);
		
			m_top.setVisibility(View.VISIBLE);
	
			fadeIn(m_top,200);
			m_fullScreen=false;
		}
		else{
			 
			m_top.setVisibility(View.INVISIBLE);
			FrameLayout.LayoutParams lp=(android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
			lp.topMargin=0;
			view.setLayoutParams(lp);
			m_fullScreen=true;
		}
	}
	public static void fadeIn(View view, int durationMillis) {
		AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
		fadeImage.setDuration(durationMillis);
		fadeImage.setInterpolator(new DecelerateInterpolator());
		view.startAnimation(fadeImage);
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int count=gallery.getChildCount();
		for(int i=0;i<count;i++){
			View view=gallery.getChildAt(i);
			if(view instanceof ImageView){
				ImageView img=(ImageView)view;
				BitmapDrawable bitmapDrawable=(BitmapDrawable) img.getDrawable();
				if(bitmapDrawable!=null){
					Bitmap bitmap=bitmapDrawable.getBitmap();
					if(!bitmap.isRecycled()){
						bitmap.recycle();
					}
				}
			}
			
			if(view instanceof ViewGroup){
				ViewGroup container=(ViewGroup)view;
				int childCount1=container.getChildCount();
				for(int j=0;j<childCount1;j++){
					View view1=container.getChildAt(j);
					if(view1 instanceof ImageView){
						ImageView img=(ImageView)view1;
						BitmapDrawable bitmapDrawable=(BitmapDrawable) img.getDrawable();
						if(bitmapDrawable!=null){
 						Bitmap bitmap=bitmapDrawable.getBitmap();
							if(!bitmap.isRecycled()){
								bitmap.recycle();
							}
						}
					}
					
					if(view1 instanceof ViewGroup){
						ViewGroup container1=(ViewGroup)view1;
						int childCount2=container1.getChildCount();
						for(int k=0;k<childCount2;k++){
							View view2=container1.getChildAt(k);
							if(view2 instanceof ImageView){
								ImageView img=(ImageView)view2;
								BitmapDrawable bitmapDrawable=(BitmapDrawable) img.getDrawable();
								if(bitmapDrawable!=null){
									Bitmap bitmap=bitmapDrawable.getBitmap();
									if(!bitmap.isRecycled()){
										bitmap.recycle();
									}
								}
							}
							
							
							
							
					}
				}
					
					
			}
		}
	}
  }
}
