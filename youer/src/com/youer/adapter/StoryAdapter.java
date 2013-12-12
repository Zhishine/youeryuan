package com.youer.adapter;

import java.util.List;

import com.example.youer.R;
import com.way.app.PushApplication;
import com.way.bean.User;
import com.youer.modal.MStory;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class StoryAdapter extends BaseAdapter {

	List<MStory> m_storyList;
	Context m_context;
	LayoutInflater m_layoutInflater;
	float m_rate=(float)446/(float)630;
	public StoryAdapter(Context context,List<MStory> storyList){
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
	public View getView(int index, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		MStory story=this.m_storyList.get(index);
		ViewHolder holder=null;
	   if(view==null){
		   view=this.m_layoutInflater.inflate(R.layout.story_list_item,null);
		   holder=new ViewHolder();
		   holder.mTitleImageView=(ImageView) view.findViewById(R.id.story_title_img);
		
		   holder.mTitle=(TextView) view.findViewById(R.id.title);
		   holder.mTime=(TextView) view.findViewById(R.id.time);
		   view.setTag(holder);
	   }else{
		   holder=(ViewHolder) view.getTag();
	   }
	  
	   RelativeLayout.LayoutParams imgLp=new RelativeLayout.LayoutParams(DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20),(int) (m_rate*(DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20))));
	   imgLp.width=DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20);
	   imgLp.height=(int) (imgLp.width*m_rate);
	   
	   BitmapDrawable drawable;
	   Bitmap bitmap;
	 // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小  
	   final BitmapFactory.Options options = new BitmapFactory.Options();  
	   options.inJustDecodeBounds = true;  
		BitmapFactory.decodeResource(m_context.getResources(),story.mTitleImg, options);  
		 // 调用上面定义的方法计算inSampleSize值  
		 options.inSampleSize = Utils.calculateInSampleSize(options, imgLp.width, imgLp.height);  
		    // 使用获取到的inSampleSize值再次解析图片  
		options.inJustDecodeBounds = false;  
		  bitmap= BitmapFactory.decodeResource(m_context.getResources(),story.mTitleImg, options); 
	   
		    drawable= new BitmapDrawable(m_context.getResources(), bitmap); 
			holder.mTitleImageView.setImageDrawable(drawable);
	   
	   
	   
	   holder.mTitleImageView.setLayoutParams(imgLp);
	   holder.mTitle.setText(story.mName);
	   holder.mTime.setText(story.mTime);
		return view;
	}

	
	 static class ViewHolder{
	    	public ImageView mTitleImageView;
	    	public TextView mTitle;
	    	public TextView mTime;
	    }
}
