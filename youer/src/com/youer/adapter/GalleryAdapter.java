package com.youer.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;






import com.youer.modal.MImage;
import com.youer.tool.DensityUtil;
import com.youer.tool.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GalleryAdapter extends BaseAdapter {

	List<MImage> m_imageList=null;
	Context m_context;
	//DisplayImageOptions options=null;
	 float m_rate=(float)480/(float)480;
	//private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public GalleryAdapter(Context context,List<MImage> imageList){
		this.m_imageList=imageList;
		this.m_context=context;
//		options = new DisplayImageOptions.Builder()
//	      
//		.showImageForEmptyUri(R.drawable.image_click)
//		.showImageOnFail(R.drawable.image_click)
//		.bitmapConfig(Bitmap.Config.RGB_565)
//		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		.cacheInMemory(false)
//		.cacheOnDisc(true)
//		.build();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_imageList.size();
	}

	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return m_imageList.get(index);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		 //ImageLoader imageLoader = ImageLoader.getInstance();
		 MImage image=m_imageList.get(index);
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(m_context);
		
		    imageView.setAdjustViewBounds(true);
		   // imageView.setBackgroundColor(Color.BLACK);
		    imageView.setScaleType(ScaleType.FIT_XY );
		    int height=(int) (DensityUtil.getLogicalWidth()*m_rate+0.5);
		    Gallery.LayoutParams lp=new Gallery.LayoutParams(
		    		LayoutParams.MATCH_PARENT ,LayoutParams.MATCH_PARENT);
		    
		  
		    LinearLayout container=new LinearLayout(m_context);
		    container.setGravity(Gravity.CENTER);
		    LinearLayout.LayoutParams containerParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		    containerParams.gravity=Gravity.CENTER;
		    container.setLayoutParams(lp);
		    container.addView(imageView);
		    imageView.setLayoutParams(containerParams);
		    convertView = container;
		    viewHolder.imageView = (ImageView)imageView; 
		    convertView.setTag(viewHolder);
			
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		//imageLoader.displayImage(entity.mImageUrl,viewHolder.imageView, options, animateFirstListener);
		BitmapDrawable drawable;
		 Bitmap bitmap;
		 
	 
			    // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
			    final BitmapFactory.Options options = new BitmapFactory.Options();  
			    options.inJustDecodeBounds = true;  

		if(image.mIsLocal){

		   BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		    // 调用上面定义的方法计算inSampleSize值  
		    options.inSampleSize = Utils.calculateInSampleSize(options, options.outWidth, options.outHeight);  
		    // 使用获取到的inSampleSize值再次解析图片  
		    options.inJustDecodeBounds = false;  
		    bitmap= BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		 
		}
		else{
			  BitmapFactory.decodeFile(image.mFilePath, options);
			   options.inSampleSize = Utils.calculateInSampleSize(options,options.outWidth, options.outHeight);  
			    // 使用获取到的inSampleSize值再次解析图片  
			    options.inJustDecodeBounds = false;  
			    bitmap= BitmapFactory.decodeFile(image.mFilePath, options);
			  
		}
		  drawable= new BitmapDrawable(m_context.getResources(), bitmap); 
		float rate=(float)options.outHeight/(float)options.outWidth; 
		int height=(int) (rate*DensityUtil.getLogicalWidth());
		LinearLayout.LayoutParams imageLp=new LinearLayout.LayoutParams(DensityUtil.getLogicalWidth(),height);
		//imageLp.setMargins(DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10));
		viewHolder.imageView.setLayoutParams(imageLp);
		viewHolder.imageView.setImageDrawable(drawable);

		return convertView;
	}
	private static class ViewHolder
	{
		LinearLayout linear;
		ImageView imageView;
	}
	
//	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//		@Override
//		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				imageView.setScaleType(ScaleType.FIT_XY);
//				int height=loadedImage.getHeight();
//				int width=loadedImage.getWidth();
//				float rate=(float)height/(float)width;
//				width=DensityUtil.getLogicalWidth();
//				height=(int) (rate*width);
//				LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(width, height);
//				lp.gravity=Gravity.CENTER;
//				imageView.setLayoutParams(lp);
//				imageView.invalidate();
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}
}
