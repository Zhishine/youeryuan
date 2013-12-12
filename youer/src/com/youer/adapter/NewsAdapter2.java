package com.youer.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;






import com.example.youer.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.youer.modal.MNews;
import com.youer.tool.DensityUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsAdapter2 extends BaseAdapter {
	private List<MNews> m_newsList=null;
	private Context m_context=null;
	LayoutInflater m_layoutInflater=null;

	DisplayImageOptions options=null;
	ImageLoader m_imageLoader=null;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public NewsAdapter2(Context context,List<MNews> newsList){
    	this.m_context=context;
    	this.m_newsList=newsList;
    	this.m_layoutInflater=LayoutInflater.from(m_context);
    	options = new DisplayImageOptions.Builder()

		.cacheInMemory(true)
		.cacheOnDisc(true)
		.build();
       this.m_imageLoader=ImageLoader.getInstance();
    }
    public void replace(List<MNews> news){
    	m_newsList=news;
    }

    public void addNews(List<MNews> news){
    	m_newsList.addAll(news);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(this.m_newsList==null)
			return 0;
		return this.m_newsList.size();
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		MNews news=this.m_newsList.get(position);
		if(news.mExt2.equalsIgnoreCase("1"))
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
	   return this.m_newsList.get(index);
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
		MNews entity = this.m_newsList.get(position);

    	ViewHolder viewHolder = null;	
    	ViewHolder1 viewHolder1 = null;
    	int type = getItemViewType(position);
	    if (convertView!= null &&convertView.getTag()!=null)
	    {
	    	if(type==0)
	    	 viewHolder = (ViewHolder) convertView.getTag();
	    	else
	    		 viewHolder1 = (ViewHolder1) convertView.getTag();	
			 
	    }else{
	    	if(type==0){
	    	 convertView = this.m_layoutInflater.inflate(R.layout.news_item1, null);
			 // convertView.setBackgroundColor(Color.WHITE);
	    	  viewHolder = new ViewHolder();
			  viewHolder.mTitleTxt = (TextView) convertView.findViewById(R.id.title);
			  viewHolder.mTitleTxt.setTextColor(Color.BLACK);
			  viewHolder.mTitleTxt.setTextSize(19);

			  viewHolder.mDescriptionTxt = (TextView) convertView.findViewById(R.id.description);
			  viewHolder.mDescriptionTxt.setTextColor(Color.GRAY);
			  viewHolder.mDescriptionTxt.setTextSize(14);
			  viewHolder.mSourceTxt = (TextView) convertView.findViewById(R.id.source);
			  viewHolder.mSourceTxt.setTextColor(Color.GRAY);
			  viewHolder.mSourceTxt.setTextSize(13);
			  viewHolder.mSourceTxt.setMaxLines(1);
			  viewHolder.mGalleryFlag = (ImageView) convertView.findViewById(R.id.gallery);
			  convertView.setTag(viewHolder);
	    	}
	    	else{
	    		 convertView = this.m_layoutInflater.inflate(R.layout.news_item2, null);
				 // convertView.setBackgroundColor(Color.WHITE);
	    		  int imgWidth=DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20);
	    		  int height=(int) (imgWidth*0.6);
		    	  viewHolder1 = new ViewHolder1();
		    	  viewHolder1.mTitleTxt = (TextView) convertView.findViewById(R.id.title);
		    	  viewHolder1.mTitleTxt.setTextColor(Color.BLACK);
		    	  viewHolder1.mTitleTxt.setTextSize(16);
				  viewHolder1.mTitleImg = (ImageView) convertView.findViewById(R.id.title_img);
				  viewHolder1.mTitleImg.setPadding(DensityUtil.dip2px(2), DensityUtil.dip2px(2), DensityUtil.dip2px(2), DensityUtil.dip2px(2));
				  viewHolder1.mTitleImg.setScaleType(ScaleType.FIT_XY);
				  viewHolder1.mSourceTxt = (TextView) convertView.findViewById(R.id.source);
				  viewHolder1.mSourceTxt.setTextColor(Color.GRAY);
				  viewHolder1.mSourceTxt.setTextSize(13);
				  viewHolder1.mSourceTxt.setMaxLines(1);
				  viewHolder1.mGalleryFlag = (ImageView) convertView.findViewById(R.id.gallery);
				  LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) viewHolder1.mTitleImg.getLayoutParams();
				  lp.width=imgWidth;
				  lp.height=height;
				  viewHolder1.mTitleImg.setLayoutParams(lp);
				  convertView.setTag(viewHolder1);	
	    	}
	    }
	    if(type==0){
	    	viewHolder.mDescriptionTxt.setText(entity.mDescription);
	    viewHolder.mTitleTxt.setText(entity.mTitle);
	    viewHolder.mSourceTxt.setText(entity.mExt3);
	    if(entity.mIsGallery)
	    	viewHolder.mGalleryFlag.setVisibility(View.VISIBLE);
	    else
	    	viewHolder.mGalleryFlag.setVisibility(View.INVISIBLE);
	    }
	    else{
	    	 viewHolder1.mTitleTxt.setText(entity.mTitle);
	    	 viewHolder1.mSourceTxt.setText(entity.mExt3);
	    	 viewHolder1.mTitleImg.setImageDrawable(null);
	    	
	 	    
	    	 this.m_imageLoader.displayImage(entity.mTitleImageUrl,viewHolder1.mTitleImg, options, animateFirstListener);
	    }
	    return convertView;
	}

    static class ViewHolder { 
        public TextView mTitleTxt;
        public TextView mSourceTxt;
        public TextView mDescriptionTxt;
        public ImageView mGalleryFlag;
    }
   
    static class ViewHolder1 { 
        public TextView mTitleTxt;
        public ImageView mTitleImg;
        public TextView mSourceTxt;
        public ImageView mGalleryFlag;
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				imageView.setScaleType(ScaleType.FIT_XY);
				int height=loadedImage.getHeight();
				int width=loadedImage.getWidth();
				float rate=(float)height/(float)width;
				width=DensityUtil.getLogicalWidth();
				height=(int) (rate*width);
				LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(width, height);
				lp.gravity=Gravity.CENTER;
				imageView.setLayoutParams(lp);
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
