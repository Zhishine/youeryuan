package com.youer.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.example.youer.R;
import com.youer.modal.MImage;
import com.youer.modal.MNotify;
import com.youer.modal.MVideo;
import com.youer.tool.AppDataManager;
import com.youer.tool.DensityUtil;
import com.youer.tool.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class NotifyContentActivity extends Activity {
	int m_width;
	int m_height;
	LinearLayout content;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify_content);
		LinearLayout container=(LinearLayout)findViewById(R.id.container);
		this.m_width=DensityUtil.getLogicalWidth();
		this.m_height=DensityUtil.getLogicalHeight();
		final MNotify notify=(MNotify) getIntent().getExtras().getSerializable("notify");
		TextView titleTxt=(TextView)findViewById(R.id.titile_txt);
		if(notify.mTitle!=null&&!notify.mTitle.equalsIgnoreCase(""))
			titleTxt.setText(notify.mTitle);
        ScrollView  scrollView=new ScrollView(this);
        scrollView.setVerticalScrollBarEnabled(false);
        LayoutParams contentLp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(contentLp);
        content=new LinearLayout(this);
        scrollView.setBackgroundColor(Color.WHITE);
        content.setOrientation(1);
        LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        content.setLayoutParams(lp);
        int margin=DensityUtil.dip2px(10);
        if(notify.mTitle!=null&&!notify.mTitle.equalsIgnoreCase("")){
            TextView txtTitle=new TextView(this);
            txtTitle.setTextSize(20);
            txtTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            txtTitle.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams txtLp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            txtLp.topMargin=2*margin;
            txtLp.bottomMargin=2*margin;
            txtLp.gravity=Gravity.CENTER;
            txtTitle.setGravity(Gravity.CENTER);
            txtTitle.setLayoutParams(txtLp);
            txtTitle.setText(notify.mContent);
            content.addView(txtTitle);
            }
        if(notify.mContent!=null&&!notify.mContent.equalsIgnoreCase("")){
        TextView txtContent=new TextView(this);
        txtContent.setTextSize(15);
        txtContent.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams txtLp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        txtLp.topMargin=2*margin;
        txtLp.bottomMargin=2*margin;
        txtLp.gravity=Gravity.CENTER;
        txtContent.setGravity(Gravity.CENTER);
        txtContent.setLayoutParams(txtLp);
        txtContent.setText(notify.mContent);
        //content.addView(txtContent);
        }
        if(notify.mImageList!=null&&notify.mImageList.size()>0){
        	 int i=0;
        	for(final MImage image : notify.mImageList){
        		ImageView imageView=new ImageView(this);
        		imageView.setScaleType(ScaleType.FIT_XY);
        		
        		BitmapDrawable drawable;
        		 Bitmap bitmap;
     		    // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
 			    final BitmapFactory.Options options = new BitmapFactory.Options();  
 			    options.inJustDecodeBounds = true;  

 		if(image.mIsLocal){

 		   BitmapFactory.decodeResource(this.getResources(), image.mResourceId, options);  
 		    // 调用上面定义的方法计算inSampleSize值  
 		    options.inSampleSize = Utils.calculateInSampleSize(options, options.outWidth, options.outHeight);  
 		    // 使用获取到的inSampleSize值再次解析图片  
 		    options.inJustDecodeBounds = false;  
 		    bitmap= BitmapFactory.decodeResource(this.getResources(), image.mResourceId, options);  
 		 
 		}
 		else{
 			  BitmapFactory.decodeFile(image.mFilePath, options);
 			   options.inSampleSize = Utils.calculateInSampleSize(options,options.outWidth, options.outHeight);  
 			    // 使用获取到的inSampleSize值再次解析图片  
 			    options.inJustDecodeBounds = false;  
 			    bitmap= BitmapFactory.decodeFile(image.mFilePath, options);
 			  
 		}
 		  drawable= new BitmapDrawable(this.getResources(), bitmap); 
 		 imageView.setImageDrawable(drawable);
 		float rate=(float)options.outHeight/(float)options.outWidth; 
    			int height=(int) (rate*(this.m_width-2*margin));
    			LinearLayout.LayoutParams imageLp=new LinearLayout.LayoutParams((this.m_width-2*margin),height);
    			imageLp.setMargins(DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10));
        		imageView.setLayoutParams(imageLp);
        		content.addView(imageView);
        		imageView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View imageView) {
						// TODO Auto-generated method stub
						//NotifyContentActivity.this.showImagePopup((ImageView) imageView);
						Intent intent=new Intent(NotifyContentActivity.this,GalleryActivity.class);
						intent.putExtra("imageList",(Serializable)notify.mImageList);
						intent.putExtra("index",String.valueOf(imageView.getTag()));
						startActivity(intent);
					}
        			
        		});
        		imageView.setTag(i);
        		i++;
        	}
        }
        if(notify.mVideoList!=null&&notify.mVideoList.size()>0){
        	for(final MVideo video : notify.mVideoList){
        		final ImageView imageView=new ImageView(this);
        		imageView.setScaleType(ScaleType.FIT_XY);
        		BitmapDrawable drawable;
        		Bitmap bitmap;
        		   // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
 			    final BitmapFactory.Options options = new BitmapFactory.Options();  
 			    options.inJustDecodeBounds = true;  

 		if(video.mImage.mIsLocal){

 		   BitmapFactory.decodeResource(this.getResources(),video.mImage.mResourceId, options);  
 		    // 调用上面定义的方法计算inSampleSize值  
 		    options.inSampleSize = Utils.calculateInSampleSize(options, options.outWidth, options.outHeight);  
 		    // 使用获取到的inSampleSize值再次解析图片  
 		    options.inJustDecodeBounds = false;  
 		    bitmap= BitmapFactory.decodeResource(this.getResources(),video.mImage.mResourceId, options);  
 		 
 		}
 		else{
 			  BitmapFactory.decodeFile(video.mImage.mFilePath, options);
 			   options.inSampleSize = Utils.calculateInSampleSize(options,options.outWidth, options.outHeight);  
 			    // 使用获取到的inSampleSize值再次解析图片  
 			    options.inJustDecodeBounds = false;  
 			    bitmap= BitmapFactory.decodeFile(video.mImage.mFilePath, options);
 			  
 		}
 		  drawable= new BitmapDrawable(this.getResources(), bitmap); 
 		 imageView.setImageDrawable(drawable);
    			float rate=(float)bitmap.getHeight()/(float)bitmap.getWidth(); 
    			int height=(int) (rate*(this.m_width-2*margin));
    			LinearLayout.LayoutParams imageLp=new LinearLayout.LayoutParams((this.m_width-2*margin),height);
    			
        		imageView.setLayoutParams(imageLp);
        		FrameLayout imgFrame=new FrameLayout(this);
        		LinearLayout.LayoutParams imgFrameLp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        		imgFrameLp.setMargins(DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10));
        		imgFrame.setLayoutParams(imgFrameLp);
        		imgFrame.addView(imageView);
        		
        		ImageView  videoImg=new ImageView(this);
        		videoImg.setImageResource(R.drawable.video);
        	 		FrameLayout.LayoutParams videoFrameLp=new FrameLayout.LayoutParams(DensityUtil.dip2px(46),DensityUtil.dip2px(46));
        	 		videoFrameLp.gravity=Gravity.CENTER;
        	 		videoImg.setLayoutParams(videoFrameLp);
        	         imgFrame.addView(videoImg);
        		
        		content.addView(imgFrame);
        		imageView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//showVideoPopup(video);
						Intent intent=new Intent(NotifyContentActivity.this,VideoActivity.class);
						intent.putExtra("rate", (float)imageView.getHeight()/(float)imageView.getWidth());
						intent.putExtra("video", video);
						startActivity(intent);
					}
        			
        		});
        	}
        }
        scrollView.addView(content);
        container.addView(scrollView);
        ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NotifyContentActivity.this.finish();
			}
			
		});
	}
	PopupWindow mPopupWindow;
	void showImagePopup(ImageView imageView){
		if (null == mPopupWindow) {  
	          
	        View layout = LayoutInflater.from(this).inflate(  
	                R.layout.image_popup, null);  
	        final ImageView img=(ImageView) layout.findViewById(R.id.img_big);
	        img.setScaleType(ScaleType.FIT_XY);
	        RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(imageView.getWidth(),imageView.getHeight());
	        lp.topMargin=(this.m_height-imageView.getHeight()-DensityUtil.dip2px(54))/2;
	        img.setLayoutParams(lp);
	        img.setImageDrawable(imageView.getDrawable());
	        //设置弹出部分和面积大小  
	        mPopupWindow = new PopupWindow(layout,this.m_width, this.m_height, true);  

	        // 设置半透明灰色  
	        ColorDrawable dw = new ColorDrawable(Color.BLACK);  
	        mPopupWindow.setBackgroundDrawable(dw);  
	          
	        mPopupWindow.setClippingEnabled(true);  
	        ImageView download=(ImageView) layout.findViewById(R.id.download);
	        download.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					download(img);
				}
	        	
	        });
	    }  
	  
	
	    mPopupWindow.showAtLocation(content, Gravity.CENTER | Gravity.CENTER,  
	            0, 0); 
	}
	
	PopupWindow video_popupWindow;
	void showVideoPopup(MVideo video){
		if (null == video_popupWindow) {  
	          
	        View layout = LayoutInflater.from(this).inflate(  
	                R.layout.video_popup, null);  
	        final VideoView videoView=(VideoView) layout.findViewById(R.id.video);
	        
	        //设置弹出部分和面积大小  
	        video_popupWindow = new PopupWindow(layout,this.m_width, this.m_height, true);  

	        // 设置半透明灰色  
	        ColorDrawable dw = new ColorDrawable(Color.BLACK);  
	        video_popupWindow.setBackgroundDrawable(dw);  
	          
	        video_popupWindow.setClippingEnabled(true);  
	     
	        
	        //MediaController mediaController = new MediaController(this);
            //把MediaController对象绑定到VideoView上
            //mediaController.setAnchorView(videoView);
            //设置VideoView的控制器是mediaController
           // videoView.setMediaController(mediaController);

            //这两种方法都可以 videoView.setVideoPath("file:///sdcard/love_480320.mp4");
            if(!video.mIsLocal)
            videoView.setVideoURI(Uri.parse(video.mFilePath));
            else
            videoView.setVideoPath("android.resource://" + getPackageName()+"/"+R.raw.test1);
            //启动后就播放
            videoView.start();
	    }  
	  
	
		video_popupWindow.showAtLocation(content, Gravity.TOP | Gravity.LEFT,  
	           -300, 0); 
	}
	
	
	
	void download(ImageView imgData){
		imgData.setDrawingCacheEnabled(true);
		Bitmap  bitmap = Bitmap.createBitmap(imgData.getDrawingCache());
		imgData.setDrawingCacheEnabled(false);
		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		long ms = System.currentTimeMillis();
		String dt = formatter.format(ms);
		String fileName = dt + "_" + ms + ".png";
		String systemImageDir = "/sdcard/youer/image/";
		if( AppDataManager.getInstance().SaveImage(bitmap, fileName)){
			//success
			String msg = "下载成功" + ":" + systemImageDir;
			Toast toast=Toast.makeText(this, msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
		}else{
			//fail
			Toast toast=Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}
