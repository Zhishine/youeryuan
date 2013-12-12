package com.youer.activity;

import java.io.Serializable;
import java.util.List;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.youer.adapter.PhotoGridAdapter;
import com.youer.modal.MImage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoActivity extends Activity implements OnClickListener {

	private PhotoGridAdapter ia;
	private GridView g;
	private int imageCol = 3;
	 LinearLayout m_popupView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		final List<MImage> imageList=(List<MImage>) getIntent().getSerializableExtra("imageList");
		String title=getIntent().getStringExtra("title");
		TextView titleTxt=(TextView)findViewById(R.id.titile_txt);
		if(title!=null&&!title.equalsIgnoreCase(""))
			titleTxt.setText(title);
		ia = new PhotoGridAdapter(this,imageList);
		g = (GridView) findViewById(R.id.myGrid);
		g.setAdapter(ia);
		g.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int index,
					long length) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PhotoActivity.this,GalleryActivity.class);
				intent.putExtra("imageList",(Serializable)imageList);
				intent.putExtra("index",String.valueOf(index));
				startActivity(intent);
			}
			
		});
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoActivity.this.finish();
			}
			
		});
		
		
		m_popupView=(LinearLayout) findViewById(R.id.popup);
		m_popupView.setOnClickListener(this);
		m_popupView.setVisibility(View.INVISIBLE);
		ImageView expand=(ImageView) findViewById(R.id.expand);
		expand.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				updatePopup();
			}}); 
	}

	void updatePopup(){
  	  if(m_popupView.getVisibility()==View.INVISIBLE)
  	  {
		    	m_popupView.setVisibility(View.VISIBLE);
		    	fadeIn(m_popupView, 500);
  	  }
		    else {
		    	//m_popupView.setVisibility(View.INVISIBLE);
		    	m_popupView.setVisibility(View.INVISIBLE);
		    }
     }
	
	public static void fadeIn(View view, int durationMillis) {
		AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
		fadeImage.setDuration(durationMillis);
		fadeImage.setInterpolator(new DecelerateInterpolator());
		view.startAnimation(fadeImage);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
	 case R.id.popup:
 		m_popupView.setVisibility(View.INVISIBLE);
		break;
		}
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int count=g.getChildCount();
		for(int i=0;i<count;i++){
			View view=g.getChildAt(i);
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
