package com.youer.tool;

import java.io.IOException;

import com.example.youer.R;
import com.youer.activity.StoryPlayerActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class StoryPlayerManager implements OnCompletionListener {
	MediaPlayer mediaPlayer;
	String m_fileName="";
	Context m_context;
	static final StoryPlayerManager m_instance=new StoryPlayerManager();
	private StoryPlayerManager(){
	
	}
	public static StoryPlayerManager getInstance(){
		return m_instance;
	}
	public void play(){
		if(mediaPlayer!=null&&this.m_context!=null){
			if(mediaPlayer.isPlaying())
			{
				mediaPlayer.pause();
				Intent intent=new Intent("com.youer.receiver.storyStateChanged");
				intent.putExtra("state",2);
				this.m_context.sendBroadcast(intent);
			}
			else 
			{
				mediaPlayer.start();
				Intent intent=new Intent("com.youer.receiver.storyStateChanged");
				intent.putExtra("state",1);
				this.m_context.sendBroadcast(intent);
			}
		}
	}
	public void play(Context context,String fileName){
		this.m_context=context;
		if(!this.m_fileName.equalsIgnoreCase(fileName))
		{
			if(mediaPlayer!=null){
				mediaPlayer.stop();
				mediaPlayer=null;
			}
		AssetFileDescriptor fileDescriptor;
		try {
			fileDescriptor = context.getAssets().openFd(fileName);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
		                              fileDescriptor.getStartOffset(),
		                              fileDescriptor.getLength());
		    mediaPlayer.prepare();
		    mediaPlayer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			if(mediaPlayer!=null){
				mediaPlayer.start();
				
			}
		}
		Intent intent=new Intent("com.youer.receiver.storyStateChanged");
		intent.putExtra("state",1);
		this.m_context.sendBroadcast(intent);
		this.m_fileName=fileName;
	}
	
	public void pause(){
		if(mediaPlayer!=null){
			mediaPlayer.pause();
			Intent intent=new Intent("com.youer.receiver.storyStateChanged");
			intent.putExtra("state",2);
			this.m_context.sendBroadcast(intent);
		}
	}
	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		this.mediaPlayer.seekTo(0);		
		Intent intent=new Intent("com.youer.receiver.storyStateChanged");
		intent.putExtra("state", 3);
		this.m_context.sendBroadcast(intent);
	}
	
}
