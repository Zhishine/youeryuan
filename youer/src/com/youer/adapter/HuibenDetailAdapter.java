package com.youer.adapter;

import java.util.List;

import com.example.youer.R;
import com.way.app.PushApplication;
import com.way.bean.User;
import com.youer.adapter.PhotoGridAdapter.ViewHolder;
import com.youer.modal.MImage;
import com.youer.modal.MStory;
import com.youer.modal.MStory1;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class HuibenDetailAdapter extends BaseAdapter {

	List<Integer> m_storyList;
	Context m_context;
	LayoutInflater m_layoutInflater;
	//float m_rate=(float)446/(float)630;
	public HuibenDetailAdapter(Context context,List<Integer> storyList){
		this.m_context=context;
		this.m_storyList=storyList;
		this.m_layoutInflater=LayoutInflater.from(m_context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.m_storyList.size();
	}

	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return this.m_storyList.get(index);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int story=this.m_storyList.get(index);
		ImageView imageView = null;
		
				 
		if (convertView == null) {

			imageView = new ImageView(m_context);
			//����Ǻ�����GridView��չʾ4��ͼƬ����Ҫ����ͼƬ�Ĵ�С 
			
			//imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
			convertView=imageView;
		
			// imageView.setPadding(3, 3, 3, 3);
		} else {
	
			imageView=(ImageView) convertView;
		}
		  BitmapDrawable drawable;
		
		    // ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С  
		    final BitmapFactory.Options options = new BitmapFactory.Options();  
		    options.inJustDecodeBounds = true;  
	   BitmapFactory.decodeResource(m_context.getResources(), story, options);  
	    // �������涨��ķ�������inSampleSizeֵ  
	    options.inSampleSize = Utils.calculateInSampleSize(options, options.outWidth, options.outHeight); 
	    float rate=(float)options.outHeight/(float)options.outWidth;
	    // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ  
	    options.inJustDecodeBounds = false;  
	    Bitmap bitmap= BitmapFactory.decodeResource(m_context.getResources(),story, options);  
	    drawable= new BitmapDrawable(m_context.getResources(), bitmap); 
	    imageView.setImageDrawable(drawable);
		ListView.LayoutParams lp;
      
		lp=new ListView.LayoutParams(
				DensityUtil.getLogicalWidth()-DensityUtil.dip2px(6),(int) ((DensityUtil.getLogicalWidth()
				-DensityUtil.dip2px(6))*rate));
		imageView.setLayoutParams(lp);

	
	imageView.setPadding(DensityUtil.dip2px(3), DensityUtil.dip2px(3), DensityUtil.dip2px(3),DensityUtil.dip2px(3));
	
	       /** 
	        * ��ؼ��ڴˣ���options.inJustDecodeBounds = true; 
	       Bitmap bitmap; * ������decodeFile()�����ص�bitmapΪ�գ�����ʱ����options.outHeightʱ���Ѿ�������ͼƬ�ĸ��� 
	        */  
	      
		
		
	   
		   imageView.setImageDrawable(drawable);
	
		return convertView;
	}

	
}
