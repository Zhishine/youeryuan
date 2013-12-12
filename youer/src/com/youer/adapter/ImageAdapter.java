package com.youer.adapter;

import java.util.List;



import com.youer.tool.DensityUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

public class ImageAdapter extends BaseAdapter{
	private Context _context;
    private List<Drawable> m_imgList;
    float m_rate=(float)240/(float)480;
	public ImageAdapter(Context context,List<Drawable> imgList) {
	    _context = context;
	    m_imgList=imgList;
	}

	public int getCount() {
	    return m_imgList.size();
	}

	public Object getItem(int position) {

	    return position;
	}

	public long getItemId(int position) {
	    return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(_context);
		    imageView.setAdjustViewBounds(true);
		    imageView.setBackgroundColor(Color.BLACK);
		    imageView.setScaleType(ScaleType.CENTER_CROP);
		    int height=(int) (DensityUtil.getLogicalWidth()*m_rate+0.5);
		    imageView.setLayoutParams(new Gallery.LayoutParams(
		    		LayoutParams.MATCH_PARENT  ,height));
		    convertView = imageView;
		    viewHolder.imageView = (ImageView)convertView; 
		    convertView.setTag(viewHolder);
			
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
	    viewHolder.imageView.setImageDrawable(m_imgList.get(position%m_imgList.size()));
	    
	    return convertView;

    }
    
    private static class ViewHolder
	{
		ImageView imageView;
	}

}
