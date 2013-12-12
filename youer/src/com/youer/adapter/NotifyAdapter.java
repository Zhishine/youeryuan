package com.youer.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.youer.R;
import com.youer.modal.MNews;
import com.youer.modal.MNotify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotifyAdapter extends BaseAdapter {
	private List<MNotify> m_notifyList=null;
	private Context m_context=null;
	LayoutInflater m_layoutInflater=null;

    public NotifyAdapter(Context context,List<MNotify> notifyList){
    	this.m_context=context;
    	this.m_notifyList=notifyList;
    	this.m_layoutInflater=LayoutInflater.from(m_context);
    }

   
    public void addNotify(List<MNotify> notifyList){
    	m_notifyList.addAll(notifyList);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(this.m_notifyList==null)
			return 0;
		return this.m_notifyList.size();
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		MNotify notify=this.m_notifyList.get(position);
		if(notify.mDescription==null||notify.mDescription.equalsIgnoreCase(""))
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
	   return this.m_notifyList.get(index);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}
     int time=0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MNotify entity = this.m_notifyList.get(position);
		int type = getItemViewType(position);
		ViewHolder viewHolder=null;
		ViewHolder1 viewHolder1=null;
		if(type==0){
	    if (convertView!= null &&convertView.getTag()!=null)
	    {
	    	 viewHolder = (ViewHolder) convertView.getTag();
			 
	    }else{
	    	 convertView = this.m_layoutInflater.inflate(R.layout.news_item, null);
	    	  viewHolder = new ViewHolder();
			  viewHolder.mTitleTxt = (TextView) convertView.findViewById(R.id.title);
			  viewHolder.mTitleTxt.setTextColor(Color.BLACK);
			  viewHolder.mTitleTxt.setTextSize(17);
			  viewHolder.mTitleTxt.setMaxLines(1);
			  viewHolder.mDescriptionTxt = (TextView) convertView.findViewById(R.id.description);
			  viewHolder.mDescriptionTxt.setTextColor(Color.GRAY);
			  viewHolder.mDescriptionTxt.setTextSize(14);
			  viewHolder.mDescriptionTxt.setMaxLines(1); 
			  viewHolder.mTimeTxt = (TextView) convertView.findViewById(R.id.time);
			  viewHolder.mTimeTxt.setTextColor(Color.GRAY);
			  viewHolder.mTimeTxt.setTextSize(14);
			  viewHolder.mTimeTxt.setMaxLines(1);
			  convertView.setTag(viewHolder);
	    }
	    viewHolder.mTitleTxt.setText(entity.mTitle);
	    if(entity.mDescription!=null&&!entity.mDescription.equalsIgnoreCase("")){
	    	viewHolder.mDescriptionTxt.setText(entity.mDescription);
        	viewHolder.mDescriptionTxt.setTextSize(14);
        	viewHolder.mDescriptionTxt.setVisibility(View.VISIBLE);
    		}
    		else{
    			viewHolder.mDescriptionTxt.setText(null);
    			viewHolder.mDescriptionTxt.setTextSize(0);
    			viewHolder.mDescriptionTxt.setVisibility(View.INVISIBLE);
    		}
	    viewHolder.mTimeTxt.setText(entity.mTime);
		}
		else if(type==1){
			 if (convertView!= null &&convertView.getTag()!=null)
			    {
			    	 viewHolder1 = (ViewHolder1) convertView.getTag();
					 
			    }else{
			    	 convertView = this.m_layoutInflater.inflate(R.layout.news_item3, null);
			    	 viewHolder1 = new ViewHolder1();
			    	 viewHolder1.mTitleTxt = (TextView) convertView.findViewById(R.id.title);
			    	 viewHolder1.mTitleTxt.setTextColor(Color.BLACK);
			    	 viewHolder1.mTitleTxt.setTextSize(17);
			    	 viewHolder1.mTitleTxt.setMaxLines(1);
					 
			    	 viewHolder1.mTimeTxt = (TextView) convertView.findViewById(R.id.time);
			    	 viewHolder1.mTimeTxt.setTextColor(Color.GRAY);
			    	 viewHolder1.mTimeTxt.setTextSize(14);
			    	 viewHolder1.mTimeTxt.setMaxLines(1);
					  convertView.setTag(viewHolder1);
			    }
			 viewHolder1.mTitleTxt.setText(entity.mTitle);
			    
			 viewHolder1.mTimeTxt.setText(entity.mTime);
		}
	    return convertView;
	}

    static class ViewHolder { 
        public TextView mTitleTxt;
        public TextView mDescriptionTxt;
        public TextView mTimeTxt;
    }
    
    
    static class ViewHolder1 { 
        public TextView mTitleTxt;

        public TextView mTimeTxt;
    }
}
