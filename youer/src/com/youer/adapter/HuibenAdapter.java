package com.youer.adapter;

import java.util.List;

import com.example.youer.R;
import com.way.app.PushApplication;
import com.way.bean.User;
import com.youer.modal.MStory;
import com.youer.modal.MStory1;
import com.youer.tool.DensityUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class HuibenAdapter extends BaseAdapter {

	List<MStory1> m_storyList;
	Context m_context;
	LayoutInflater m_layoutInflater;
	//float m_rate=(float)446/(float)630;
	public HuibenAdapter(Context context,List<MStory1> storyList){
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
		MStory1 story=this.m_storyList.get(index);
		ViewHolder holder=null;
	   if(view==null){
		   view=this.m_layoutInflater.inflate(R.layout.huiben_list_item,null);
		   holder=new ViewHolder();
		   holder.mTitleImageView=(ImageView) view.findViewById(R.id.story_title_img);
		
		   holder.mTitle=(TextView) view.findViewById(R.id.title);
		   holder.mTime=(TextView) view.findViewById(R.id.time);
		   view.setTag(holder);
	   }else{
		   holder=(ViewHolder) view.getTag();
	   }
	   
	   holder.mTitleImageView.setBackgroundResource(story.mTitleImg);
	   RelativeLayout.LayoutParams imgLp=new RelativeLayout.LayoutParams(DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20),(int) (story.mRate*(DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20))));
	   imgLp.width=DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20);
	   imgLp.height=(int) (imgLp.width*story.mRate);
	   holder.mTitleImageView.setLayoutParams(imgLp);
	   holder.mTitle.setText(story.mName);
	
		return view;
	}

	
	 static class ViewHolder{
	    	public ImageView mTitleImageView;
	    	public TextView mTitle;
	    	public TextView mTime;
	    }
}
