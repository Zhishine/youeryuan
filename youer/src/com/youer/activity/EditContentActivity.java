package com.youer.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.youer.modal.MImage;
import com.youer.modal.MMedia;
import com.youer.modal.MNotify;
import com.youer.modal.MVideo;
import com.youer.tool.AppDataManager;
import com.youer.tool.DensityUtil;
import com.youer.view.SelectPicPopupWindow;
import com.youer.view.SelectVideoPopupWindow;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class EditContentActivity extends Activity implements OnClickListener {

	LinearLayout m_contentContainer=null;
	LinearLayout m_mediaContentContainer=null;
	EditText m_titleTxt;
	EditText m_contentTxt;
	private String rootPath = "/sdcard/ayouer/avideo";
	private File dir;
	int m_width;
	int m_height;
	int m_topHeight;
	int m_titleHeight;
	int m_contentHeight;
	int m_mediaContentHeight;
	int m_opContainerHeight;
	//int m_socialContainerHeight;
	List<MVideo> m_videoList=null;
	List<MImage> m_imageList=null;

	/* 请求码*/   
    private static final int IMAGE_REQUEST_CODE = 0;    
    private static final int CAMERA_REQUEST_CODE = 1;    
    private static final int VIDEO_REQUEST_CODE = 2;    
    private static final int VIDEO_LOCAL_REQUEST_CODE = 3;  
    
    ImageView send;
    boolean m_canSend=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       String path =rootPath;//创建文件夹存放视频
        
        dir = new File(path);
        if(!dir.exists()){
        	dir.mkdirs();
        }
		setContentView(R.layout.activity_edit_content);
		this.m_contentContainer=(LinearLayout) findViewById(R.id.content_container);
		this.m_mediaContentContainer=(LinearLayout) findViewById(R.id.media_content);
		this.m_titleTxt=(EditText)findViewById(R.id.title);
		this.m_contentTxt=(EditText)findViewById(R.id.content);
		this.m_width=DensityUtil.getLogicalWidth();
		this.m_height=DensityUtil.getLogicalHeight();
		this.m_topHeight=DensityUtil.dip2px(54);
		this.m_titleHeight=DensityUtil.dip2px(54);
		this.m_mediaContentHeight=DensityUtil.dip2px(100);
		//this.m_socialContainerHeight=DensityUtil.dip2px(54);
		this.m_opContainerHeight=DensityUtil.dip2px(70);
		this.m_contentHeight=this.m_height-this.m_topHeight-
				this.m_titleHeight-DensityUtil.dip2px(10)-this.m_mediaContentHeight;
		LinearLayout.LayoutParams contentLp=(LayoutParams) this.m_contentContainer.getLayoutParams();
		contentLp.height=this.m_contentHeight;
		this.m_contentContainer.setLayoutParams(contentLp);
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);
		
		LinearLayout.LayoutParams editContentLp=(LayoutParams) this.m_contentTxt.getLayoutParams();
		editContentLp.height=this.m_contentHeight-DensityUtil.dip2px(120);
		editContentLp.bottomMargin=DensityUtil.dip2px(10);
		editContentLp.topMargin=DensityUtil.dip2px(10);
		this.m_contentTxt.setLayoutParams(editContentLp);
		
		this.m_titleTxt.addTextChangedListener(textWatch);
		this.m_contentTxt.addTextChangedListener(textWatch);
	    send=(ImageView)findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!m_canSend)
					return;
				if(m_titleTxt.getText().toString().equalsIgnoreCase("")||m_contentTxt.getText().toString().equalsIgnoreCase("")){
					Toast toast=Toast.makeText(EditContentActivity.this,"标题或者内容必填", Toast.LENGTH_SHORT);
					toast.show();
					return;
				}
				MNotify notify=new MNotify();
				notify.mContent=(String) m_contentTxt.getText().toString();
				notify.mTitle=(String)m_titleTxt.getText().toString();
				notify.mTime="2013/11/21";
				notify.mImageList=m_imageList;
				//notify.mIsNative=false;
				notify.mVideoList=m_videoList;
				AppDataManager.getInstance().mNotifyList.add(0, notify);
				if(m_imageList!=null&&m_imageList.size()>0){
					MMedia media1=new MMedia();
					media1.mName=notify.mTitle;
					media1.mPhotoList=m_imageList;
					AppDataManager.getInstance().mMediaList.add(0, media1);
				}
				if(m_videoList!=null&&m_videoList.size()>0){
					MMedia media1=new MMedia();
					media1.mName=notify.mTitle;
					media1.mVideoList=m_videoList;
					media1.mIsVideo=true;
					AppDataManager.getInstance().mMediaList.add(0, media1);
				}
				AppDataManager.getInstance().saveData();
				AppDataManager.getInstance().saveMediaData();
				AppDataManager.getInstance().mNotifyAdapter.notifyDataSetChanged();
				Toast toast=Toast.makeText(EditContentActivity.this,"发布成功", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				EditContentActivity.this.finish();
			}
			
		});
		ImageView photo=(ImageView)findViewById(R.id.photo);
		photo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
	
				    	 menuPhotoWindow= new SelectPicPopupWindow(EditContentActivity.this, itemsPhotoOnClick);;
			                //显示窗口  
						menuPhotoWindow.showAtLocation(EditContentActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				
			
			}
			
		});
		
		ImageView video=(ImageView)findViewById(R.id.video);
		video.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	 
					    		menuVideoWindow= new SelectVideoPopupWindow(EditContentActivity.this, itemsVideoOnClick);;
				                //显示窗口  
							menuVideoWindow.showAtLocation(EditContentActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);    
					 
			
			}
			
		});
		
	}
	SelectVideoPopupWindow menuVideoWindow;
	SelectPicPopupWindow menuPhotoWindow;
	OnClickListener  itemsPhotoOnClick = new OnClickListener(){  
		
        public void onClick(View v) {  
            menuPhotoWindow.dismiss();  
            switch (v.getId()) {  
            case R.id.btn_take_photo:  
            	new Handler().postDelayed(new Runnable(){    
            	    public void run() {    
				    	 Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
		            	 intentFromCapture.putExtra("return-data", false);
		                 // 判断存储卡是否可以用，可用进行存储      
		                 startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
				     }    
				 },200);    
		
            
                break;  
            case R.id.btn_pick_photo: 
               	new Handler().postDelayed(new Runnable(){    
            	    public void run() {    
            	    	Intent intentFromGallery = new Intent();    
		                intentFromGallery.setType("image/*"); // 设置文件类型    
		                intentFromGallery.putExtra("return-data", false);
		                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);  
		                startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);
				     }    
				 },200);  
  
            	
            
                break;  
            default:  
                break;  
            }  
              
                  
        }  
          
    };
    TextWatcher textWatch=new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1,
				int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			if(m_titleTxt.getText().toString().equalsIgnoreCase(""))
			{
				send.setImageResource(R.drawable.fabu_no_click);
				m_canSend=false;
			}
			else{
				send.setImageResource(R.drawable.fabu_click);
				m_canSend=true;
			}
		}
		
	};
	
OnClickListener  itemsVideoOnClick = new OnClickListener(){  
		
        public void onClick(View v) {  
            menuVideoWindow.dismiss();  
            switch (v.getId()) {  
            case R.id.btn_take_photo:  
            	new Handler().postDelayed(new Runnable(){    
            	    public void run() {    
            	    	Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		            	mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//画质0.5
		            	mIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 70000);//70s
		            	startActivityForResult(mIntent,VIDEO_REQUEST_CODE);//CAMERA_ACTIVITY = 1
				     }    
				 },200);  
            	
                break;  
  
            case R.id.btn_pick_photo:        
            	new Handler().postDelayed(new Runnable(){    
            	    public void run() {    
            	  	  Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		            	   intent.setType("video/*");
		            	   startActivityForResult(intent,VIDEO_LOCAL_REQUEST_CODE);
				     }    
				 },200);  
            	   

                break;  
            default:  
                break;  
            }  
              
                  
        }  
          
    };
    
    
	@SuppressLint("CommitPrefEdits")
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {  
        // 结果码不等于取消时候  
        if (resultCode != RESULT_CANCELED) {  
            switch (requestCode) {  
            case IMAGE_REQUEST_CODE:  
              if (hasSdcard()) {  
                	  Uri selectedImage = intent.getData();
                      String[] filePathColumn = {MediaStore.Images.Media.DATA};

                      Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                      cursor.moveToFirst();

                      int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                      String filePath = cursor.getString(columnIndex);
                      cursor.close();
                      Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
                      MImage img=new MImage();
                      img.mIsLocal=false;
                      img.mFilePath=filePath;
                      addToMediaLayout(myBitmap,img,false);
                      if(!myBitmap.isRecycled()){
                    	  myBitmap.recycle();
                      }
                } else {  
                    //showToast("未找到存储卡，无法存储照片！");  
                }  
                break;  
            case CAMERA_REQUEST_CODE:  
            	 try {  
                     Bundle extras = intent.getExtras();  
                     Bitmap myBitmap = (Bitmap) extras.get("data");  
                     String filen=(String) new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA));
                    if(AppDataManager.getInstance().SaveImage(myBitmap,filen+".jpg")){
                    String systemImageDir = "/sdcard/ayouer/image/";
                     MImage img=new MImage();
                     img.mIsLocal=false;
                     img.mFilePath=systemImageDir+filen+".jpg";
                     addToMediaLayout(myBitmap,img,false);
                     if(!myBitmap.isRecycled()){
                   	  myBitmap.recycle();
                     }
                    }

                 } catch (Exception e) {  
                     e.printStackTrace();  
                     // TODO: handle exception  
                 }  
  
                break;  
            case VIDEO_REQUEST_CODE:  
            	try {
            	AssetFileDescriptor videoAsset = getContentResolver()
				.openAssetFileDescriptor(intent.getData(), "r");
		        FileInputStream fis = videoAsset.createInputStream();
		        String filen=(String) new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA));
		        File tmpFile = new File(dir,filen+ ".mp4");
		        FileOutputStream fos = new FileOutputStream(tmpFile);
		        byte[] buf = new byte[1024];
		        int len;
		        while ((len = fis.read(buf)) > 0) {
			        fos.write(buf, 0, len);
		        }
		        fis.close();
		        fos.close();
		        Bitmap bm=createVideoThumbnail(rootPath+"/"+filen+ ".mp4");
		        MVideo video=new MVideo();
		        video.mIsLocal=false;
		        video.mFilePath=rootPath+"/"+filen+ ".mp4";
		        video.mImage=new MImage();
		        if(AppDataManager.getInstance().SaveTemp(bm, filen)){
		            video.mImage.mFilePath=AppDataManager.getInstance().systemTempDir+"/"+filen;
		            video.mImage.mIsLocal=false;
		        }
		        this.addToMediaLayout(bm,video,true);
            	} catch (IOException e) {
            		 e.printStackTrace();  
        		}
            	break;
            case VIDEO_LOCAL_REQUEST_CODE:
            	 if (hasSdcard()) 
            	 {
            		 Uri selectedVideo = intent.getData();
					     String[] filePathColumn = {MediaStore.Video.Media.DATA};

					     Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
					     cursor.moveToFirst();

					     int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					     String filePath = cursor.getString(columnIndex);
					     cursor.close();
					    Bitmap bm=createVideoThumbnail(filePath);
					    String filen=(String) new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA));
					    MVideo video=new MVideo();
				        video.mIsLocal=false;
				        video.mFilePath=filePath;
				        video.mImage=new MImage();
				        if(AppDataManager.getInstance().SaveTemp(bm, filen)){
				            video.mImage.mFilePath=AppDataManager.getInstance().systemTempDir+"/"+filen;
				            video.mImage.mIsLocal=false;
				        }
				        this.addToMediaLayout(bm,video,true);
				        if(!bm.isRecycled())
				        	bm.recycle();
               } else {  
                   //showToast("未找到存储卡，无法存储照片！");  
               }  
            	break;
            }  
        }  
        super.onActivityResult(requestCode, resultCode, intent);  
    }  
    
    @SuppressLint({ "libmedia_jni.so", "NewApi" })
	private Bitmap createVideoThumbnail(String filePath) {
	Bitmap bitmap = null;
	MediaMetadataRetriever retriever = new MediaMetadataRetriever();
	try {//MODE_CAPTURE_FRAME_ONLY
		//retriever.setMode(MediaMetadataRetriever.MODE_CAPTURE_FRAME_ONLY);
		retriever.setDataSource(filePath);
		bitmap = retriever.getFrameAtTime();
		} catch(IllegalArgumentException ex) {
		// Assume this is a corrupt video file
		} catch (RuntimeException ex) {
		// Assume this is a corrupt video file.
		} finally {
		try {
		retriever.release();
		} catch (RuntimeException ex) {
		// Ignore failures while cleaning up.
		}
		}
		return bitmap;
	}
    
    int i=0;
    
    void addToMediaLayout(Bitmap bitmap,final Object obj,boolean isVideo){
    	Bitmap com=comp(bitmap);
    	FrameLayout imgFrame = null;
    	ImageView video=null;
    	if(isVideo){
    	    imgFrame=new FrameLayout(this);
    		LayoutParams imgFrameLp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    		imgFrameLp.setMargins(DensityUtil.dip2px(4), DensityUtil.dip2px(4), DensityUtil.dip2px(4), DensityUtil.dip2px(4));
    		imgFrame.setLayoutParams(imgFrameLp);
    		
    	}
    	final ImageView img=new ImageView(this);
         img.setImageBitmap(com);
         img.setScaleType(ScaleType.FIT_XY);
         int height=DensityUtil.dip2px(92);
         float rate=(float)com.getWidth()/(float)com.getHeight();
         LayoutParams lp=new LayoutParams((int) (rate*height),height);
         
         img.setLayoutParams(lp);
         if(isVideo){
        	 if(this.m_videoList==null)
        		 this.m_videoList=new ArrayList<MVideo>();
        	 this.m_videoList.add((MVideo)obj);
         imgFrame.addView(img);
         
         video=new ImageView(this);
 		video.setImageResource(R.drawable.video);
 		FrameLayout.LayoutParams videoFrameLp=new FrameLayout.LayoutParams(DensityUtil.dip2px(46),DensityUtil.dip2px(46));
 		videoFrameLp.gravity=Gravity.CENTER;
 		video.setLayoutParams(videoFrameLp);
         imgFrame.addView(video);
         imgFrame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(EditContentActivity.this,VideoActivity.class);
				intent.putExtra("rate", (float)img.getHeight()/(float)img.getWidth());
				intent.putExtra("video", (MVideo)obj);
				startActivity(intent);
			}
        	 
         });
         this.m_mediaContentContainer.addView(imgFrame);
         }else{
        	 if(this.m_imageList==null)
        		 this.m_imageList=new ArrayList<MImage>();
        	 this.m_imageList.add((MImage)obj);
        	 img.setTag(i);
        	 img.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(EditContentActivity.this,GalleryActivity.class);
					intent.putExtra("imageList",(Serializable)m_imageList);
					intent.putExtra("index",String.valueOf(img.getTag()));
					startActivity(intent);
				}
        		 
        	 });
        	 lp.setMargins(DensityUtil.dip2px(4), DensityUtil.dip2px(4), DensityUtil.dip2px(4), DensityUtil.dip2px(4));
        	 this.m_mediaContentContainer.addView(img);
        	  i++;
         }
 
     
    }
    
    
	private Bitmap comp(Bitmap image) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = DensityUtil.dip2px(92);//这里设置高度为800f
		float ww = hh*((float)newOpts.outHeight/(float)newOpts.outWidth);//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	    return compressImage(bitmap);
	}
    
	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
    
    public boolean hasSdcard() {
        // 判断SDCard是否存在
        boolean sdExist = Environment.getExternalStorageState().equals(
        android.os.Environment.MEDIA_MOUNTED);
        return sdExist;
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

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.back:
			this.finish();
			break;
		}
	}
}
