package com.youer.activity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.example.youer.R;
import com.youer.modal.MVideo;
import com.youer.tool.AppDataManager;
import com.youer.tool.DensityUtil;
import com.youer.view.FullScreenVideoView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.VideoView;



public class VideoActivity extends Activity implements OnClickListener {
	ImageView m_view=null;
	int m_currentIndex=0;
	LinearLayout m_popupView=null;
	LinearLayout m_videoContainer=null;
	View m_top=null;
	boolean m_fullScreen=false;
	LinearLayout view;
	int m_videoContainerHeight;
	int m_width;
	int m_height;
	FrameLayout.LayoutParams m_lp=null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_popup);
		this.m_width=DensityUtil.getLogicalWidth();
		this.m_height=DensityUtil.getLogicalHeight();
		MVideo video=(MVideo) getIntent().getExtras().getSerializable("video");
		ImageView back=(ImageView) findViewById(R.id.back);
		float rate=getIntent().getFloatExtra("rate", 0);
		if(rate==0){
			BitmapDrawable drawable;
    		Bitmap bitmap;
    		if(video.mImage.mIsLocal){
    		    drawable=(BitmapDrawable) this.getResources().getDrawable(video.mImage.mResourceId);
    		    bitmap= drawable.getBitmap();
    		}
    		else{
    			bitmap= BitmapFactory.decodeFile(video.mImage.mFilePath);
    			  drawable= new BitmapDrawable(this.getResources(), bitmap);   
    		}

		 rate=(float)bitmap.getHeight()/(float)bitmap.getWidth();
			
		}
		m_top=findViewById(R.id.top_bg);
		this.m_videoContainerHeight=this.m_height;
		this.m_videoContainer=(LinearLayout)findViewById(R.id.video_container);
		VideoView video1=(VideoView) findViewById(R.id.video);
		//video1.setVisibility(View.INVISIBLE);
		
		FrameLayout.LayoutParams videoContainerLp=(LayoutParams) this.m_videoContainer.getLayoutParams();
		videoContainerLp.height=Math.min((int)(rate*this.m_width),this.m_height-DensityUtil.dip2px(54)-this.getBarHeight());
		videoContainerLp.width=this.m_width;
		videoContainerLp.topMargin=Math.max((this.m_height-DensityUtil.dip2px(54)-this.getBarHeight()-videoContainerLp.height)/2,DensityUtil.dip2px(54));
		this.m_videoContainer.setLayoutParams(videoContainerLp);
		//this.m_videoContainer.setVisibility(View.INVISIBLE);
		back.setOnClickListener(this);
	
		
		   final FullScreenVideoView videoView=(FullScreenVideoView) findViewById(R.id.video);
	        MediaController mediaController = new MediaController(this);
           //把MediaController对象绑定到VideoView上
           mediaController.setAnchorView(videoView);
           //设置VideoView的控制器是mediaController
           videoView.setMediaController(mediaController);

           //这两种方法都可以 videoView.setVideoPath("file:///sdcard/love_480320.mp4");
           if(!video.mIsLocal)
           videoView.setVideoURI(Uri.parse(video.mFilePath));
           else
           videoView.setVideoPath("android.resource://" + getPackageName()+"/"+R.raw.test1);
           //启动后就播放
           videoView.start();
           
           m_popupView=(LinearLayout) findViewById(R.id.popup);
   		m_popupView.setOnClickListener(this);
   		m_popupView.setVisibility(View.VISIBLE);
   		this.m_lp=(FrameLayout.LayoutParams) this.m_popupView.getLayoutParams();
   		this.m_lp.height=0;
   		this.m_lp.width=0;
//   		android:layout_marginTop="57dp"
//   			    android:layout_marginRight="5dp"
//   			    android:layout_gravity="right"
   		this.m_popupView.setLayoutParams(m_lp);
//   	 android:layout_width="198dp"
//   		    android:layout_height="87dp"
   		
   		
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
		   		this.m_lp.height=LayoutParams.MATCH_PARENT;
		   		this.m_lp.width=LayoutParams.MATCH_PARENT;
		   		this.m_popupView.setLayoutParams(m_lp);
		    	//fadeIn(m_popupView, 500);
  	  }
		    else {
		    	//m_popupView.setVisibility(View.INVISIBLE);
		    	m_popupView.setVisibility(View.INVISIBLE);
		    	this.m_lp.height=DensityUtil.dip2px(0);
		   		this.m_lp.width=DensityUtil.dip2px(0);
		   		this.m_popupView.setLayoutParams(m_lp);
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
	
	
}
