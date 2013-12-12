package com.youer.activity;

import java.util.List;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.youer.adapter.HuibenAdapter;
import com.youer.adapter.HuibenDetailAdapter;
import com.youer.modal.MImage;
import com.youer.modal.MStory1;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HuibenDetailActivity extends Activity implements OnClickListener {
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huiben_detail);
		final MStory1 story=(MStory1) getIntent().getSerializableExtra("story");
		String title=story.mName;
		TextView titleTxt=(TextView)findViewById(R.id.titile_txt);
		if(title!=null&&!title.equalsIgnoreCase(""))
			titleTxt.setText(title);
		 list=(ListView)findViewById(R.id.list);
		HuibenDetailAdapter adapter=new HuibenDetailAdapter(this,story.mImgList);
		list.setAdapter(adapter);
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		this.finish();
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
