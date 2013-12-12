package com.youer.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.way.baidupush.client.PushMessageReceiver;
import com.way.baidupush.client.PushMessageReceiver.EventHandler;
import com.way.bean.Message;
import com.way.bean.User;
import com.youer.activity.StoryListActivity.StoryMessageReceiver;
import com.youer.modal.MStory;
import com.youer.tool.DensityUtil;
import com.youer.tool.StoryPlayerManager;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoryPlayerActivity extends Activity implements OnClickListener {

	MStory m_story;
	Button m_playBtn;
	float m_rate=(float)446/(float)630;
	private MediaRecorder m_recorder;
	StoryMessageReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_player);
		
		this.m_playBtn=(Button) findViewById(R.id.play);
		this.m_playBtn.setOnClickListener(this);
		
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);
		
		LinearLayout imgContainer=(LinearLayout)findViewById(R.id.img_container);
		this.m_story=(MStory) getIntent().getSerializableExtra("story");
		imgContainer.removeAllViews();

	if(this.m_story.mImgList!=null&&this.m_story.mImgList.size()>0){
		TextView title=(TextView)findViewById(R.id.titile_txt);
		title.setText(this.m_story.mName);
		for(Integer resId :this.m_story.mImgList){
		ImageView img=new ImageView(this);
		  LinearLayout.LayoutParams imgLp=new LinearLayout.LayoutParams(DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20),(int) (m_rate*(DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20))));
		   imgLp.width=DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20);
		   imgLp.height=(int) (imgLp.width*m_rate);
		   imgLp.topMargin=DensityUtil.dip2px(10);
		   imgLp.bottomMargin=DensityUtil.dip2px(10);
		   img.setLayoutParams(imgLp);
		   img.setImageDrawable(this.getResources().getDrawable(resId));
		   imgContainer.addView(img);
		}
	}
	
		
    receiver=new StoryMessageReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.youer.receiver.storyStateChanged");;
		this.registerReceiver(receiver, filter);

	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void play(){
		 if(this.m_playBtn.getText().toString().equalsIgnoreCase(this.getResources().getString(R.string.play)))
        	    StoryPlayerManager.getInstance().play(this,m_story.mFileName );
			    else
			     StoryPlayerManager.getInstance().play();
	}
	
   

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	    this.unregisterReceiver(receiver);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.play:
		
			try {
				play();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			break;
		case R.id.back:
			this.finish();
			break;
		}
	}

	class StoryMessageReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
		   int type=intent.getIntExtra("state", 0);
		  if(type==1){
			  StoryPlayerActivity.this.m_playBtn.setText(StoryPlayerActivity.this.getResources().getString(R.string.playing));
			  StoryPlayerActivity.this.m_playBtn.setVisibility(View.VISIBLE);
		  }
		  else if(type==2){
			  StoryPlayerActivity.this.m_playBtn.setText(StoryPlayerActivity.this.getResources().getString(R.string.play));
			  StoryPlayerActivity.this.m_playBtn.setVisibility(View.VISIBLE);
		  }
		  else if(type==3){
			  StoryPlayerActivity.this.m_playBtn.setText(StoryPlayerActivity.this.getResources().getString(R.string.play));
			  StoryPlayerActivity.this.m_playBtn.setVisibility(View.VISIBLE);
		  }
		}
}
   
}