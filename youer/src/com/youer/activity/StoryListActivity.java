package com.youer.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.youer.adapter.StoryAdapter;
import com.youer.modal.MStory;
import com.youer.modal.MVideo;
import com.youer.tool.StoryPlayerManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class StoryListActivity extends Activity implements OnClickListener {

	Button m_playBtn;
	List<MStory> m_storyList;
	StoryMessageReceiver receiver;
	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_list);
		this.m_playBtn=(Button)findViewById(R.id.play);
		this.m_playBtn.setOnClickListener(this);
		 list=(ListView)findViewById(R.id.list);
		this.initStoryListData();
		StoryAdapter adapter=new StoryAdapter(this,this.m_storyList);
		list.setAdapter(adapter);
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int index,
					long length) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StoryListActivity.this,StoryPlayerActivity.class);
				intent.putExtra("story", (MStory)m_storyList.get(index));
				startActivity(intent);
			}
			
		});
	    receiver=new StoryMessageReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.youer.receiver.storyStateChanged");;
		this.registerReceiver(receiver, filter);
	}
	
	void initStoryListData(){
		this.m_storyList=new ArrayList<MStory>();
		MStory story1=new MStory();
		story1.mId=1;
		story1.mName="������ͯ��Ȫˮ";
        story1.mTitleImg=R.drawable.story_01_pic1;
        story1.mFileName="1.mp3";
        story1.mTime="04:53";
        story1.mImgList=new ArrayList<Integer>();
        story1.mImgList.add(R.drawable.story_01_pic1);
        story1.mImgList.add(R.drawable.story_01_pic2);
        story1.mImgList.add(R.drawable.story_01_pic3);
        story1.mImgList.add(R.drawable.story_01_pic4);
        story1.mImgList.add(R.drawable.story_01_pic5);
        this.m_storyList.add(story1);
        
		MStory story2=new MStory();
		story2.mId=2;
		story2.mName="����͵���ķ���";
		story2.mTitleImg=R.drawable.story_02_pic1;
		story2.mFileName="2.mp3";
		story2.mTime="04:11";
		story2.mImgList=new ArrayList<Integer>();
        story2.mImgList.add(R.drawable.story_02_pic1);
        story2.mImgList.add(R.drawable.story_01_pic2);
        story2.mImgList.add(R.drawable.story_01_pic3);
        story2.mImgList.add(R.drawable.story_01_pic4);
        story2.mImgList.add(R.drawable.story_01_pic5);
        story2.mImgList.add(R.drawable.story_01_pic5);
        this.m_storyList.add(story2);
        
        MStory story3=new MStory();
        story3.mId=3;
        story3.mName="��̿�ĸ���";
        story3.mTitleImg=R.drawable.story_05_pic1;
        story3.mFileName="5.mp3";
        story3.mTime="02:47";
        story3.mImgList=new ArrayList<Integer>();
        story3.mImgList.add(R.drawable.story_05_pic1);
        story3.mImgList.add(R.drawable.story_01_pic2);
        story3.mImgList.add(R.drawable.story_01_pic3);
        story3.mImgList.add(R.drawable.story_01_pic4);
        story3.mImgList.add(R.drawable.story_01_pic5);
        story3.mImgList.add(R.drawable.story_01_pic5);
        this.m_storyList.add(story3);
        
        MStory story4=new MStory();
        story4.mId=4;
        story4.mName="�β��۷�";
        story4.mTitleImg=R.drawable.story_06_pic1;
        story4.mFileName="6.mp3";
        story4.mTime="04:36";
        story4.mImgList=new ArrayList<Integer>();
        story4.mImgList.add(R.drawable.story_06_pic1);
        story4.mImgList.add(R.drawable.story_06_pic1);
        story4.mImgList.add(R.drawable.story_06_pic1);
        story4.mImgList.add(R.drawable.story_06_pic1);
        story4.mImgList.add(R.drawable.story_06_pic1);
        story4.mImgList.add(R.drawable.story_06_pic1);
        this.m_storyList.add(story4);
        
        MStory story5=new MStory();
        story5.mId=5;
        story5.mName="�����ٲ�";
        story5.mTitleImg=R.drawable.story_07_pic1;
        story5.mFileName="7.mp3";
        story5.mTime="06:09";
        story5.mImgList=new ArrayList<Integer>();
        story5.mImgList.add(R.drawable.story_07_pic1);
        story5.mImgList.add(R.drawable.story_07_pic1);
        story5.mImgList.add(R.drawable.story_07_pic1);
        story5.mImgList.add(R.drawable.story_07_pic1);
        story5.mImgList.add(R.drawable.story_07_pic1);
        story5.mImgList.add(R.drawable.story_07_pic1);
        this.m_storyList.add(story5);
        
        MStory story6=new MStory();
        story6.mId=6;
        story6.mName="�ɺױ���";
        story6.mTitleImg=R.drawable.story_08_pic1;
        story6.mFileName="8.mp3";
        story6.mTime="06:09";
        story6.mImgList=new ArrayList<Integer>();
        story6.mImgList.add(R.drawable.story_08_pic1);
        story6.mImgList.add(R.drawable.story_08_pic1);
        story6.mImgList.add(R.drawable.story_08_pic1);
        story6.mImgList.add(R.drawable.story_08_pic1);
        story6.mImgList.add(R.drawable.story_08_pic1);
        story6.mImgList.add(R.drawable.story_08_pic1);
        this.m_storyList.add(story6);
        
        MStory story7=new MStory();
        story7.mId=7;
        story7.mName="�ɺױ���";
        story7.mTitleImg=R.drawable.story_10_pic1;
        story7.mFileName="10.mp3";
        story7.mTime="05:55";
        story7.mImgList=new ArrayList<Integer>();
        story7.mImgList.add(R.drawable.story_10_pic1);
        story7.mImgList.add(R.drawable.story_10_pic1);
        story7.mImgList.add(R.drawable.story_10_pic1);
        story7.mImgList.add(R.drawable.story_10_pic1);
        story7.mImgList.add(R.drawable.story_10_pic1);
        story7.mImgList.add(R.drawable.story_10_pic1);
        this.m_storyList.add(story7);
	}
	
	@Override
	public void onStart() {
		super.onStart();

		//PushManager.activityStarted(this);
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		
		//PushManager.activityStoped(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.back:
			this.finish();
			break;
		case R.id.play:
			 StoryPlayerManager.getInstance().play();
			break;
		}
	}
	
	 class StoryMessageReceiver extends BroadcastReceiver {
			
			@Override
			public void onReceive(Context context, Intent intent) {
			   int type=intent.getIntExtra("state", 0);
			  if(type==1){
				  StoryListActivity.this.m_playBtn.setText(StoryListActivity.this.getResources().getString(R.string.playing));
				  StoryListActivity.this.m_playBtn.setVisibility(View.VISIBLE);
			  }
			  else if(type==2){
				  StoryListActivity.this.m_playBtn.setText(StoryListActivity.this.getResources().getString(R.string.play));
				  StoryListActivity.this.m_playBtn.setVisibility(View.VISIBLE);
			  }
			  else if(type==3){
				  StoryListActivity.this.m_playBtn.setVisibility(View.INVISIBLE);
			  }
			}
	}
	 

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			this.unregisterReceiver(receiver);
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