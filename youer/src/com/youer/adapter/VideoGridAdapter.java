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

	// ������������ ��ͼƬԴ

	// ���� ImageAdapter
	public VideoGridAdapter(Context c,List<MVideo> videoList) {
		m_context = c;
		this.m_layoutInflate=LayoutInflater.from(c);
		this.m_videoList=videoList;
		this.height=this.width=DensityUtil.getLogicalHeight() / imageCol - 6;
	}

	// ��ȡͼƬ�ĸ���
	public int getCount() {
		return m_videoList.size();
	}

	// ��ȡͼƬ�ڿ��е�λ��
	public Object getItem(int position) { 
		MVideo video=this.m_videoList.get(position);
		return video;
	}

	// ��ȡͼƬID
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
			//����Ǻ�����GridView��չʾ4��ͼƬ����Ҫ����ͼƬ�Ĵ�С 
			GridView.LayoutParams lp;
			if (imageCol == 4) {
				
				lp=new GridView.LayoutParams(
						DensityUtil.getLogicalHeight() / imageCol - 6, DensityUtil.getLogicalHeight()
						/ imageCol - 6);
				convertView.setLayoutParams(lp);
			} else {//�����������GridView��չʾ3��ͼƬ����Ҫ����ͼƬ�Ĵ�С 
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
	 
			    // ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С  
			    final BitmapFactory.Options options = new BitmapFactory.Options();  
			    options.inJustDecodeBounds = true;  

		if(image.mIsLocal){

		   BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		    // �������涨��ķ�������inSampleSizeֵ  
		    options.inSampleSize = Utils.calculateInSampleSize(options, width, height);  
		    // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ  
		    options.inJustDecodeBounds = false;  
		    bitmap= BitmapFactory.decodeResource(m_context.getResources(), image.mResourceId, options);  
		 
		}
		else{
			  BitmapFactory.decodeFile(image.mFilePath, options);
			   options.inSampleSize = Utils.calculateInSampleSize(options, width, height);  
			    // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ  
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
