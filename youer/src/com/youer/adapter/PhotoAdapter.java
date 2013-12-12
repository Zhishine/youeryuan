package com.youer.adapter;

import java.util.List;

import com.example.youer.R;
import com.youer.adapter.NewsAdapter2.ViewHolder1;
import com.youer.adapter.StoryAdapter.ViewHolder;
import com.youer.modal.MImage;
import com.youer.modal.MMedia;
import com.youer.modal.MNews;
import com.youer.modal.MStory;
import com.youer.modal.MVideo;
import com.youer.tool.DensityUtil;
import com.youer.tool.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PhotoAdapter extends BaseAdapter {

	Context m_context;
	List<MMedia> m_mediaList;
	LayoutInflater m_layoutInflater;
	int width;
	int height;
	public PhotoAdapter(Context context,List<MMedia> mediaList){
		this.m_context=context;
		this.m_mediaList=mediaList;
		this.m_layoutInflater=LayoutInflater.from(m_context);
		this.height=this.width=DensityUtil.dip2px(200);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.m_mediaList.size();  
	}


	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		MMedia media=this.m_mediaList.get(position);
		if(media.mIsVideo)
			return 1;
		else 
			return 0;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		MMedia media=this.m_mediaList.get(index);
		return media;
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MMedia media=this.m_mediaList.get(index);
		ViewHolder viewHolder=null;
		int type = getItemViewType(index);
	    if (convertView!= null &&convertView.getTag()!=null)
	    	 viewHolder = (ViewHolder) convertView.getTag();
	    else{
	    	if(type==0)
	    		convertView=this.m_layoutInflater.inflate(R.layout.photo_item, null);
	    	else 
	    		convertView=this.m_layoutInflater.inflate(R.layout.photo_item1, null);
	    	viewHolder=new ViewHolder();
	    	viewHolder.mTitleImageView=(ImageView) convertView.findViewById(R.id.title_img);
	    	viewHolder.mTitleTxt=(TextView) convertView.findViewById(R.id.title);
	    	convertView.setTag(viewHolder);
	    }
	    viewHolder.mTitleTxt.setText(media.mName);
	    if(media.mIsVideo){
	    	List<MVideo> videoList=media.mVideoList;
	    	if(videoList!=null&&videoList.size()>0){
	    		MVideo video=videoList.get(0);
	    		
	    		BitmapDrawable drawable;
       		    Bitmap bitmap;
       		 // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
			    final BitmapFactory.Options options = new BitmapFactory.Options();  
			    options.inJustDecodeBounds = true;  
        MImage image=video.mImage;
		if(image.mIsLocal){

		   BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		    // 调用上面定义的方法计算inSampleSize值  
		    options.inSampleSize = Utils.calculateInSampleSize(options, width, height);  
		    // 使用获取到的inSampleSize值再次解析图片  
		    options.inJustDecodeBounds = false;  
		    bitmap= BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		 
		}
		else{
			  BitmapFactory.decodeFile(image.mFilePath, options);
			   options.inSampleSize = Utils.calculateInSampleSize(options, width, height);  
			    // 使用获取到的inSampleSize值再次解析图片  
			    options.inJustDecodeBounds = false;  
			    bitmap= BitmapFactory.decodeFile(image.mFilePath, options);
			  
		}
		  drawable= new BitmapDrawable(m_context.getResources(), bitmap); 
       	 	viewHolder.mTitleImageView.setImageDrawable(drawable);
	    	}
	    }else{
	    	List<MImage> imageList=media.mPhotoList;
	    	if(imageList!=null&&imageList.size()>0){
	    		MImage image=imageList.get(0);
	    		BitmapDrawable drawable;
       		    Bitmap bitmap;
       		 // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
			    final BitmapFactory.Options options = new BitmapFactory.Options();  
			    options.inJustDecodeBounds = true;  

		if(image.mIsLocal){

		   BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		    // 调用上面定义的方法计算inSampleSize值  
		    options.inSampleSize = Utils.calculateInSampleSize(options, width, height);  
		    // 使用获取到的inSampleSize值再次解析图片  
		    options.inJustDecodeBounds = false;  
		    bitmap= BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		 
		}
		else{
			  BitmapFactory.decodeFile(image.mFilePath, options);
			   options.inSampleSize = Utils.calculateInSampleSize(options, width, height);  
			    // 使用获取到的inSampleSize值再次解析图片  
			    options.inJustDecodeBounds = false;  
			    bitmap= BitmapFactory.decodeFile(image.mFilePath, options);
			  
		}
		  drawable= new BitmapDrawable(m_context.getResources(), bitmap); 
       	 	viewHolder.mTitleImageView.setImageDrawable(drawable);
	    	}
	    }
		
		return convertView;
	}

    static class ViewHolder { 
        public TextView mTitleTxt;
        public ImageView mTitleImageView;
    }
   
}
