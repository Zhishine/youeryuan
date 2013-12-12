package com.youer.activity;

import java.io.IOException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.List;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.youer.adapter.PhotoAdapter;
import com.youer.modal.MImage;
import com.youer.modal.MMedia;
import com.youer.modal.MVideo;
import com.youer.tool.AppDataManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PhotoListActivity extends Activity {
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
		try {
			AppDataManager.getInstance().getMediaData();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 list=(ListView) findViewById(R.id.list);
		PhotoAdapter adapter=new PhotoAdapter(this,AppDataManager.getInstance().mMediaList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int index,
					long length) {
				// TODO Auto-generated method stub
				MMedia media=AppDataManager.getInstance().mMediaList.get(index);
				if(!media.mIsVideo){
				Intent intent=new Intent(PhotoListActivity.this,PhotoActivity.class);
				List<MImage> imageList=media.mPhotoList;
				intent.putExtra("imageList", (Serializable)imageList);
				intent.putExtra("title", AppDataManager.getInstance().mMediaList.get(index).mName);
				startActivity(intent);
				}else{
					if(media.mVideoList!=null&&media.mVideoList.size()>0)
					{
						if(media.mVideoList.size()==1){
							Intent intent=new Intent(PhotoListActivity.this,VideoActivity.class);
							intent.putExtra("video",media.mVideoList.get(0));
							startActivity(intent);
						}else{
					Intent intent=new Intent(PhotoListActivity.this,VideoListActivity.class);
					List<MVideo> videoList=media.mVideoList;
					intent.putExtra("videoList", (Serializable)videoList);
					intent.putExtra("title", AppDataManager.getInstance().mMediaList.get(index).mName);
					startActivity(intent);	
						}
					}
				}
			}
			
		});
		
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoListActivity.this.finish();
			}
			
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int count=list.getChildCount();
		for(int i=0;i<count;i++){
			View view=list.getChildAt(i);
			if(view instanceof ImageView){
				ImageView img=(ImageView)view;
				BitmapDrawable bitmapDrawable=(BitmapDrawable) img.getDrawable();
				if(bitmapDrawable!=null){
					Bitmap bitmap=bitmapDrawable.getBitmap();
					if(!bitmap.isRecycled()){
						bitmap.recycle();
					}
				}
			}
			
			if(view instanceof ViewGroup){
				ViewGroup container=(ViewGroup)view;
				int childCount1=container.getChildCount();
				for(int j=0;j<childCount1;j++){
					View view1=container.getChildAt(j);
					if(view1 instanceof ImageView){
						ImageView img=(ImageView)view1;
						BitmapDrawable bitmapDrawable=(BitmapDrawable) img.getDrawable();
						if(bitmapDrawable!=null){
 						Bitmap bitmap=bitmapDrawable.getBitmap();
							if(!bitmap.isRecycled()){
								bitmap.recycle();
							}
						}
					}
					
					if(view1 instanceof ViewGroup){
						ViewGroup container1=(ViewGroup)view1;
						int childCount2=container1.getChildCount();
						for(int k=0;k<childCount2;k++){
							View view2=container1.getChildAt(k);
							if(view2 instanceof ImageView){
								ImageView img=(ImageView)view2;
								BitmapDrawable bitmapDrawable=(BitmapDrawable) img.getDrawable();
								if(bitmapDrawable!=null){
									Bitmap bitmap=bitmapDrawable.getBitmap();
									if(!bitmap.isRecycled()){
										bitmap.recycle();
									}
								}
							}
							
							
							
							
					}
				}
					
					
			}
		}
	}
  }

}
