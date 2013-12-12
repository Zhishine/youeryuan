package com.youer.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.youer.adapter.HuibenAdapter;
import com.youer.adapter.StoryAdapter;
import com.youer.modal.MStory;
import com.youer.modal.MStory1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class HuibenListActivity extends Activity implements OnClickListener {
	List<MStory1> m_storyList;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huiben_list);
	   list=(ListView)findViewById(R.id.list);
		this.initStoryListData();
		HuibenAdapter adapter=new HuibenAdapter(this,this.m_storyList);
		list.setAdapter(adapter);
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int index,
					long length) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(HuibenListActivity.this,HuibenDetailActivity.class);
				intent.putExtra("story", (MStory1)m_storyList.get(index));
				startActivity(intent);
			}
			
		});
	}

	void initStoryListData(){
		this.m_storyList=new ArrayList<MStory1>();
		MStory1 story1=new MStory1();
		story1.mId=1;
		story1.mName="彼得的椅子";
        story1.mTitleImg=R.drawable.bide1;
        story1.mRate=(float) 1.05;
        story1.mImgList=new ArrayList<Integer>();
        story1.mImgList.add(R.drawable.bide1);
        story1.mImgList.add(R.drawable.bide2);
        story1.mImgList.add(R.drawable.bide3);
        story1.mImgList.add(R.drawable.bide4);
        story1.mImgList.add(R.drawable.bide5);
        story1.mImgList.add(R.drawable.bide6);
        story1.mImgList.add(R.drawable.bide7);
        story1.mImgList.add(R.drawable.bide8);
        story1.mImgList.add(R.drawable.bide9);
        story1.mImgList.add(R.drawable.bide10);
        story1.mImgList.add(R.drawable.bide11);
        story1.mImgList.add(R.drawable.bide12);
        story1.mImgList.add(R.drawable.bide13);
        story1.mImgList.add(R.drawable.bide14);
        story1.mImgList.add(R.drawable.bide15);
        story1.mImgList.add(R.drawable.bide16);
        story1.mImgList.add(R.drawable.bide17);
        story1.mImgList.add(R.drawable.bide18);
        story1.mImgList.add(R.drawable.bide19);
        story1.mImgList.add(R.drawable.bide20);
        story1.mImgList.add(R.drawable.bide21);
        story1.mImgList.add(R.drawable.bide22);
        story1.mImgList.add(R.drawable.bide23);
        story1.mImgList.add(R.drawable.bide24);
        story1.mImgList.add(R.drawable.bide25);
        story1.mImgList.add(R.drawable.bide26);
        story1.mImgList.add(R.drawable.bide27);
        story1.mImgList.add(R.drawable.bide28);
        story1.mImgList.add(R.drawable.bide29);
        story1.mImgList.add(R.drawable.bide30);
        this.m_storyList.add(story1);
        
        MStory1 story2=new MStory1();
		story2.mId=2;
		story2.mName="忘了说我爱你";
		  story2.mRate=(float) 1.33;
		story2.mTitleImg=R.drawable.aini;
		story2.mImgList=new ArrayList<Integer>();
        story2.mImgList.add(R.drawable.story_02_pic1);
        story2.mImgList.add(R.drawable.aini);
        story2.mImgList.add(R.drawable.aini);
        story2.mImgList.add(R.drawable.aini);
        story2.mImgList.add(R.drawable.aini);
        story2.mImgList.add(R.drawable.aini);
        this.m_storyList.add(story2);
        
        
        MStory1 story3=new MStory1();
        story3.mId=2;
        story3.mName="我爸爸";
        story3.mRate=(float) 1.33;
        story3.mTitleImg=R.drawable.baba1;
        story3.mImgList=new ArrayList<Integer>();
        story3.mImgList.add(R.drawable.story_02_pic1);
        story3.mImgList.add(R.drawable.aini);
        story3.mImgList.add(R.drawable.aini);
        story3.mImgList.add(R.drawable.aini);
        story3.mImgList.add(R.drawable.aini);
        story3.mImgList.add(R.drawable.aini);
        this.m_storyList.add(story3);
     
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
