package com.youer.adapter;

import java.util.List;

import com.example.youer.R;
import com.youer.modal.MImage;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class VideoGridAdapter extends BaseAdapter {


	private int imageCol = 3;
	private int ownposition;
	List<MVideo> m_videoList;
    LayoutInflater m_layoutInflate;
    int width;
    int height;
	public int getOwnposition() {
		return ownposition;
	}

	public void setOwnposition(int ownposition) {
		this.ownposition = ownposition;
	}

	private Context m_context; 

	// 定义整型数组 即图片源

	// 声明 ImageAdapter
	public VideoGridAdapter(Context c,List<MVideo> videoList) {
		m_context = c;
		this.m_layoutInflate=LayoutInflater.from(c);
		this.m_videoList=videoList;
		this.height=this.width=DensityUtil.getLogicalHeight() / imageCol - 6;
	}

	// 获取图片的个数
	public int getCount() {
		return m_videoList.size();
	}

	// 获取图片在库中的位置
	public Object getItem(int position) { 
		MVideo video=this.m_videoList.get(position);
		return video;
	}

	// 获取图片ID
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView = null;
		ViewHolder holder = null;
		if (convertView == null) {
			holder=new ViewHolder();
			convertView = this.m_layoutInflate.inflate(R.layout.video_item, null);
			imageView=(ImageView) convertView.findViewById(R.id.title_img);
			//如果是横屏，GridView会展示4列图片，需要设置图片的大小 
			GridView.LayoutParams lp;
			if (imageCol == 4) {
				
				lp=new GridView.LayoutParams(
						DensityUtil.getLogicalHeight() / imageCol - 6, DensityUtil.getLogicalHeight()
						/ imageCol - 6);
				convertView.setLayoutParams(lp);
			} else {//如果是竖屏，GridView会展示3列图片，需要设置图片的大小 
				lp=new GridView.LayoutParams(
						DensityUtil.getLogicalWidth() / imageCol-DensityUtil.dip2px(6),DensityUtil.getLogicalWidth()
						/ imageCol-DensityUtil.dip2px(6));
				convertView.setLayoutParams(lp);
			}
			
			imageView.setPadding(0, DensityUtil.dip2px(3), DensityUtil.dip2px(3),0);
			//imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.mImageView=imageView;
		
			convertView.setTag(holder);
			// imageView.setPadding(3, 3, 3, 3);
		} else {
			holder = (ViewHolder) convertView.getTag();
			imageView=holder.mImageView;
		}
	     MVideo video=this.m_videoList.get(position);
	     BitmapDrawable drawable;
		 Bitmap bitmap;
		 MImage image=video.mImage;
	 
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
		holder.mImageView.setImageDrawable(drawable);
		return convertView;
	}
	
	static class ViewHolder{
		ImageView mImageView;
	}
}
