package com.youer.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.example.youer.R;
import com.google.gson.Gson;
import com.way.adapter.RecentAdapter;
import com.way.app.PushApplication;
import com.way.baidupush.client.PushMessageReceiver;
import com.way.baidupush.client.PushMessageReceiver.EventHandler;
import com.way.bean.MessageItem;
import com.way.bean.RecentItem;
import com.way.bean.User;
import com.way.common.util.SharePreferenceUtil;
import com.way.common.util.T;
import com.way.db.MessageDB;
import com.way.db.RecentDB;
import com.way.db.UserDB;
import com.way.swipelistview.SwipeListView;
import com.youer.adapter.ChatMsgAdapter;
import com.youer.adapter.ImageAdapter;
import com.youer.adapter.NewsAdapter2;
import com.youer.adapter.NotifyAdapter;
import com.youer.modal.MAd;
import com.youer.modal.MChatMsgEntity;
import com.youer.modal.MImage;
import com.youer.modal.MMedia;
import com.youer.modal.MNews;
import com.youer.modal.MNotify;
import com.youer.modal.MUser;
import com.youer.modal.MVideo;
import com.youer.tool.AppDataManager;
import com.youer.tool.DensityUtil;
import com.youer.tool.Utils;
import com.youer.view.MyGallery;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, EventHandler {

	LinearLayout m_content=null;
	LinearLayout m_firstContent=null;
	LinearLayout m_secondContent=null;
	LinearLayout m_thirdContent=null;
	LinearLayout m_fourthContent=null;
	ImageView m_firstImg=null;
	ImageView m_secondImg=null;
	ImageView m_thirdImg=null;
	ImageView m_fourthImg=null;
	
	int m_screenHeight=0;
	int m_currentSelectIndex=0;
	NotifyAdapter m_notifyAdapter=null;
	List<MNotify> m_notifyList=null;
	int m_width;
	int m_height;
	LayoutInflater m_layoutInflater=null;
	
	
	private Button m_btnSend;
	private EditText m_editTextContent;
	private ListView m_listView;
	private ChatMsgAdapter m_adapter;
	
	
	private List<MChatMsgEntity> m_dataArrays = new ArrayList<MChatMsgEntity>();
	private boolean isLogin = false;
	
	String appid = "";
	String channelid = "";
	String userid = "";
	
	
	private LinkedList<RecentItem> mRecentDatas;
	private RecentAdapter mAdapter;
	private PushApplication mApplication;
	private UserDB mUserDB;
	private MessageDB mMsgDB;
	private RecentDB mRecentDB;
	private SharePreferenceUtil mSpUtil;
	private Gson mGson;
	private SwipeListView mRecentListView;
	public static final int NEW_MESSAGE = 0x000;// 有新消息
	public static final int NEW_FRIEND = 0x001;// 有好友加入
	
	View m_secondView;
	ListView m_thirdView;
	NewsAdapter2 m_newsAdapter;
	 private int preSelImgIndex = 0;
	
	 private final int KEHOUQUAN=100001;
	 private final int STORY=100002;
	 private final int QIMENG=100003;
	 private final int HUIBEN=100004;
	 private final int PHOTO=100005;
	 private final int SETTING=100006;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			AppDataManager.getInstance().init(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.m_layoutInflater=LayoutInflater.from(this);
		DensityUtil util=new DensityUtil(this);
		this.m_content=(LinearLayout) findViewById(R.id.content);
		this.m_firstImg=(ImageView) findViewById(R.id.first);
		this.m_firstImg.setOnClickListener(this);
		this.m_secondImg=(ImageView) findViewById(R.id.second);
		this.m_secondImg.setOnClickListener(this);
		this.m_thirdImg=(ImageView) findViewById(R.id.third);
		this.m_thirdImg.setOnClickListener(this);
		this.m_fourthImg=(ImageView) findViewById(R.id.fourth);
		this.m_fourthImg.setOnClickListener(this);
		this.m_width=DensityUtil.getLogicalWidth();
		this.m_height=DensityUtil.getLogicalHeight();
		this.reMeasure();
		
		//initData();
		
		this.createFirstContent();
		this.createSecondContent();
		this.createThirdContent();
		this.createFourthContent();
		this.m_content.addView(m_firstContent);
		selectView(0);
		
	}
	
	void selectView(int index){
		switch(index){
		case 0:
			this.m_firstImg.setImageDrawable(this.getResources().getDrawable(R.drawable.home_click));
			this.m_secondImg.setImageDrawable(this.getResources().getDrawable(R.drawable.message_no_click));
			this.m_thirdImg.setImageDrawable(this.getResources().getDrawable(R.drawable.news_no_click));
			this.m_fourthImg.setImageDrawable(this.getResources().getDrawable(R.drawable.kehou_no_click));
			break;
		case 1:
			this.m_firstImg.setImageDrawable(this.getResources().getDrawable(R.drawable.home_no_click));
			this.m_secondImg.setImageDrawable(this.getResources().getDrawable(R.drawable.message_click));
			this.m_thirdImg.setImageDrawable(this.getResources().getDrawable(R.drawable.news_no_click));
			this.m_fourthImg.setImageDrawable(this.getResources().getDrawable(R.drawable.kehou_no_click));
			break;
		case 2:
			this.m_firstImg.setImageDrawable(this.getResources().getDrawable(R.drawable.home_no_click));
			this.m_secondImg.setImageDrawable(this.getResources().getDrawable(R.drawable.message_no_click));
			this.m_thirdImg.setImageDrawable(this.getResources().getDrawable(R.drawable.news_click));
			this.m_fourthImg.setImageDrawable(this.getResources().getDrawable(R.drawable.kehou_no_click));
			break;
		case 3:
			this.m_firstImg.setImageDrawable(this.getResources().getDrawable(R.drawable.home_no_click));
			this.m_secondImg.setImageDrawable(this.getResources().getDrawable(R.drawable.message_no_click));
			this.m_thirdImg.setImageDrawable(this.getResources().getDrawable(R.drawable.news_no_click));
			this.m_fourthImg.setImageDrawable(this.getResources().getDrawable(R.drawable.kehou_click));
			break;
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NEW_FRIEND:
				User u = (User) msg.obj;
				// mUserDB.addUser(u);
//				if (mLeftFragment == null)
//					mLeftFragment = (LeftFragment) getSupportFragmentManager()
//							.findFragmentById(R.id.main_left_fragment);
//				mLeftFragment.updateAdapter();// 更新
				T.showShort(mApplication, "好友列表已更新!");
				break;
			case NEW_MESSAGE:
				// String message = (String) msg.obj;
				com.way.bean.Message msgItem = (com.way.bean.Message) msg.obj;
				String userId = msgItem.getUser_id();
				int id=msgItem.getId();
				int role=msgItem.getRole();
				String nick = msgItem.getNick();
				String content = msgItem.getMessage();
				int headId = msgItem.getHead_id();
				// try {
				// headId = Integer
				// .parseInt(JsonUtil.getFromUserHead(message));
				// } catch (Exception e) {
				// L.e("head is not integer  " + e);
				// }
				if (mUserDB.selectInfo(id) == null) {// 如果不存在此好友，则添加到数据库
					User user = new User(id,userId,role, msgItem.getChannel_id(), nick,
							headId, 0,"");
					mUserDB.addUser(user);
					
				}
				// TODO Auto-generated method stub
				MessageItem item = new MessageItem(
						MessageItem.MESSAGE_TYPE_TEXT, nick,
						System.currentTimeMillis(), content, headId, true, 1);
				mMsgDB.saveMsg(id, item);
				// 保存到最近会话列表
				RecentItem recentItem = new RecentItem(id,userId,role, headId, nick,
						content, 0, System.currentTimeMillis());
				mRecentDB.saveRecent(recentItem);
				mAdapter.addFirst(recentItem);
				T.showShort(mApplication, nick + ":" + content);
				break;
			default:
				break;
			}
		}
	};

	
	@Override
	public void onStart() {
		super.onStart();

		//PushManager.activityStarted(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
		setIntent(intent);

		handleIntent(intent);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		//PushManager.activityStoped(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		
		if (!PushManager.isPushEnabled(this))
			PushManager.resumeWork(this);
	
		PushMessageReceiver.ehList.add(this);
		initRecentData();
		mApplication.getNotificationManager().cancel(
				PushMessageReceiver.NOTIFY_ID);
		PushMessageReceiver.mNewNum = 0;
		
		
		//showChannelIds();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mHomeWatcher.setOnHomePressedListener(null);
//		mHomeWatcher.stopWatch();
		PushMessageReceiver.ehList.remove(this);// 暂停就移除监听
	}

	
	private void initRecentData() {
		// TODO Auto-generated method stub
		mRecentDatas = mRecentDB.getRecentList();
		//mAdapter = new RecentAdapter(this, mRecentDatas, mRecentListView);
		//mRecentListView.setAdapter(mAdapter);
		mAdapter = new RecentAdapter(this, mRecentDatas, this.m_listView);
		if(this.m_listView!=null)
		this.m_listView.setAdapter(mAdapter);
	}

	
	/**
	 * 处理Intent
	 * 
	 * @param intent
	 *            intent
	 */
	private void handleIntent(Intent intent) {
		String action = intent.getAction();

		if (Utils.ACTION_RESPONSE.equals(action)) {

			String method = intent.getStringExtra(Utils.RESPONSE_METHOD);

			if (PushConstants.METHOD_BIND.equals(method)) {
				String toastStr = "";
				int errorCode = intent.getIntExtra(Utils.RESPONSE_ERRCODE, 0);
				if (errorCode == 0) {
					String content = intent
							.getStringExtra(Utils.RESPONSE_CONTENT);
				

					try {
						JSONObject jsonContent = new JSONObject(content);
						JSONObject params = jsonContent
								.getJSONObject("response_params");
						appid = params.getString("appid");
						channelid = params.getString("channel_id");
						userid = params.getString("user_id");
					} catch (JSONException e) {
						Log.e(Utils.TAG, "Parse bind json infos error: " + e);
					}

					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(this);
					Editor editor = sp.edit();
					editor.putString("appid", appid);
					editor.putString("channel_id", channelid);
					editor.putString("user_id", userid);
					editor.commit();

					showChannelIds();

					toastStr = "Bind Success";
				} else {
					toastStr = "Bind Fail, Error Code: " + errorCode;
					if (errorCode == 30607) {
						Log.d("Bind Fail", "update channel token-----!");
					}
				}

				Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
			}
		} else if (Utils.ACTION_LOGIN.equals(action)) {
			String accessToken = intent
					.getStringExtra(Utils.EXTRA_ACCESS_TOKEN);
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
			isLogin = true;
	
		} else if (Utils.ACTION_MESSAGE.equals(action)) {
			String message = intent.getStringExtra(Utils.EXTRA_MESSAGE);
			String summary = "Receive message from server:\n\t";
			Log.e(Utils.TAG, summary + message);
			JSONObject contentJson = null;
			String contentStr = message;
			try {
				contentJson = new JSONObject(message);
				contentStr = contentJson.toString(4);
			} catch (JSONException e) {
				Log.d(Utils.TAG, "Parse message json exception.");
			}
			summary += contentStr;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(summary);
			builder.setCancelable(true);
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			Log.i(Utils.TAG, "Activity normally start!");
		}
	}
	
	
	private void showChannelIds() {
		String appId = null;
		String channelId = null;
		String clientId = null;

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		appId = sp.getString("appid", "");
		channelId = sp.getString("channel_id", "");
		clientId = sp.getString("user_id", "");//828868000250887501,
		
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		
	}
	
	void initNotifyData(){
		SharedPreferences dataSetting=getSharedPreferences("initData", 0);
    	SharedPreferences.Editor editor = dataSetting.edit();
    	//editor.clear();
    	//editor.commit();
     	String dataListStr=dataSetting.getString("data",null);
		if(dataListStr==null){
	    List<MMedia> mediaList=new ArrayList<MMedia>();
		final Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR); 

	    int month = c.get(Calendar.MONTH);//
		int day = c.get(Calendar.DAY_OF_MONTH);//
		this.m_notifyList=new ArrayList<MNotify>();
		MNotify notify1=new MNotify();
		notify1.mTitle="春节放假通知";
		notify1.mDescription="今年春节放假时间1月30日至2月5日（星期六）";
		notify1.mTime=year+"/"+month+"/"+day;
		notify1.mContent="今年春节放假时间1月30日至2月5日（星期六）";
	    this.m_notifyList.add(notify1);
	    
	    MNotify notify2=new MNotify();
	    notify2.mTitle="2013招生通知";
	
	    notify2.mTime="2013/4/22";
	    notify2.mIsNative=false;
	    notify2.mRedirectUrl="http://php1.hontek.com.cn/wordpress/archives/372.html";
	    this.m_notifyList.add(notify2);
	    
	    MNotify notify3=new MNotify();
	    notify3.mTitle="开学了【相册】";
	    notify3.mDescription="新学期开学了，孩子在升旗";
	    notify3.mTime="2013/9/1";
	    List<MImage> imageList3=new ArrayList<MImage>();
	    MImage image1=new MImage();
	    image1.mResourceId=R.drawable.kaixue1;
	    MImage image2=new MImage();
	    image2.mResourceId=R.drawable.kaixue2;
	    MImage image3=new MImage();
	    image3.mResourceId=R.drawable.kaixue3;
	    MImage image4=new MImage();
	    image4.mResourceId=R.drawable.kaixue4;
	    MImage image5=new MImage();
	    image5.mResourceId=R.drawable.kaixue5;
	    imageList3.add(image1);
	    imageList3.add(image2);
	    imageList3.add(image3);
	    imageList3.add(image4);
	    imageList3.add(image5);
	    notify3.mImageList=imageList3;
		this.m_notifyList.add(notify3);
		MMedia media1=new MMedia();
		media1.mName="开学了";
		media1.mPhotoList=imageList3;
		mediaList.add(media1);
		
		MNotify notify4=new MNotify();
	    notify4.mTitle="活动图片";
	    notify4.mTime="2013/9/1";
	    List<MImage> imageList4=new ArrayList<MImage>();
	    MImage image6=new MImage();
	    image6.mResourceId=R.drawable.zuopin1;
	    MImage image7=new MImage();
	    image7.mResourceId=R.drawable.zuopin2;
	    MImage image8=new MImage();
	    image8.mResourceId=R.drawable.zuopin3;
	    MImage image9=new MImage();
	    image9.mResourceId=R.drawable.zuopin4;
	    MImage image10=new MImage();
	    image10.mResourceId=R.drawable.zuopin5;
	    imageList4.add(image6);
	    imageList4.add(image7);
	    imageList4.add(image8);
	    imageList4.add(image9);
	    imageList4.add(image10);
	    notify4.mImageList=imageList4;
	    this.m_notifyList.add(notify4);
	    MMedia media2=new MMedia();
	    media2.mName="活动图片";
	    media2.mPhotoList=imageList4;
		mediaList.add(media2);
	    
	    
	    MNotify notify5=new MNotify();
	    notify5.mTitle="大一班歌词活动【视频】";
	    notify5.mTime="2013/9/1";
	    List<MVideo> videoList=new ArrayList<MVideo>();
	    MVideo video=new MVideo();
	    MImage image=new MImage();
	    image.mResourceId=R.drawable.test1;
	    video.mImage=image;
	    video.mFilePath="test1";
	    videoList.add(video);
	    notify5.mVideoList=videoList;
		this.m_notifyList.add(notify5);
		MMedia media3=new MMedia();
	    media3.mName="大一班歌词活动";
		media3.mVideoList=videoList;
		media3.mIsVideo=true;
	    mediaList.add(media3);
		
		
	    AppDataManager.getInstance().mMediaList=mediaList;
		AppDataManager.getInstance().mNotifyList=this.m_notifyList;
        AppDataManager.getInstance().saveData();
        AppDataManager.getInstance().saveMediaData();
		}
		else{
			try {
     			AppDataManager.getInstance().getData();
     			AppDataManager.getInstance().getMediaData(); 
         		m_notifyList=AppDataManager.getInstance().mNotifyList;
				
			} catch (OptionalDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}
	
	public void initView() {
		m_listView = (ListView) this.m_secondView.findViewById(R.id.list);
		m_listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				RecentItem item = (RecentItem) mAdapter
						.getItem(position);
				User u = new User(item.getId(),item.getUserId(), item.getRole(),"", item.getName(),
						item.getHeadImg(), 0,"");
				mMsgDB.clearNewCount(item.getId());
				Intent toChatIntent = new Intent(MainActivity.this,
						ChatActivity.class);
				toChatIntent.putExtra("user", u);
				startActivity(toChatIntent);
			}
			
		});
		ImageView contact=(ImageView)this.m_secondView.findViewById(R.id.contact);
		contact.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,ContactActivity.class);
				startActivity(intent);
			}
			
		});
		this.m_listView.setAdapter(mAdapter);
	}

	private String[] msgArray = new String[] { "[媚眼]测试啦[媚眼]", "测试啦", "测试啦",
			"测试啦", "测试啦", "你妹[苦逼]", "测[惊讶]你妹", "测你妹[胜利]",
			"测试啦" };

	private String[] m_dataArray = new String[] { "2012-12-12 12:00",
			"2012-12-12 12:10", "2012-12-12 12:11", "2012-12-12 12:20",
			"2012-12-12 12:30", "2012-12-12 12:35", "2012-12-12 12:40",
			"2012-12-12 12:50", "2012-12-12 12:50" };

	private final static int COUNT = 8;

	public void initData() {
//		for (int i = 0; i < COUNT; i++) {
//			MChatMsgEntity entity = new MChatMsgEntity();
//			entity.setDate(m_dataArray[i]);
//			if (i % 2 == 0) {
//				entity.setName("你妹");
//				entity.setMsgType(true);
//			} else {
//				entity.setName("没么");
//				entity.setMsgType(false);
//			}
//
//
//			entity.setText(msgArray[i]);
//			m_dataArrays.add(entity);
//		}
//
//		m_adapter = new ChatMsgAdapter(this, m_dataArrays);
//		m_listView.setAdapter(m_adapter);
		
		mApplication = PushApplication.getInstance();
		mSpUtil = mApplication.getSpUtil();
		mGson = mApplication.getGson();
		mUserDB = mApplication.getUserDB();
		mMsgDB = mApplication.getMessageDB();
		mRecentDB = mApplication.getRecentDB();

	}
	
	@SuppressLint("NewApi")
	void createFirstContent(){
		this.initNotifyData();
		m_firstContent=new LinearLayout(this);
		m_firstContent.setOrientation(1);
		View view=this.m_layoutInflater.inflate(R.layout.first_layout, m_firstContent);
		
		ImageView fabu=(ImageView)view.findViewById(R.id.fabu);
		fabu.setOnClickListener(this);
		ImageView website=(ImageView)view.findViewById(R.id.website);
		website.setOnClickListener(this);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    m_firstContent.setLayoutParams(lp);
	    this.m_notifyAdapter=new NotifyAdapter(this,this.m_notifyList);
	    AppDataManager.getInstance().mNotifyAdapter=this.m_notifyAdapter;
	    ListView notifyListView=(ListView) view.findViewById(R.id.list);
	    notifyListView.setVerticalScrollBarEnabled(false);
	    notifyListView.setCacheColorHint(Color.WHITE);
	    //notifyListView.setDividerHeight(DensityUtil.dip2px(7));
	    notifyListView.setDivider(this.getResources().getDrawable(R.drawable.news_diver));
	    notifyListView.setFadingEdgeLength(0);
	    //actualListView.setSelector(R.drawable.news_item_select);
		//actualListView.setVerticalFadingEdgeEnabled(false);
	    notifyListView.setHorizontalFadingEdgeEnabled(false);
	    if (Integer.parseInt(Build.VERSION.SDK) >= 9)
	    	notifyListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
	    else 
	    	notifyListView.setVerticalFadingEdgeEnabled(false);
	    notifyListView.setDividerHeight(DensityUtil.dip2px(8));
	    this.createAdBanner(notifyListView);
	    notifyListView.setAdapter(m_notifyAdapter);
	  
	    //m_firstContent.addView(notifyListView);
	    notifyListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				MNotify notify=m_notifyList.get(index-1);
				if(notify.mIsNative){
					Intent intent=new Intent(MainActivity.this,NotifyContentActivity.class);
					 intent.putExtra("notify",(Serializable)notify);
					startActivity(intent);
				}
				else if(notify.mRedirectUrl!=null&&!notify.mRedirectUrl.equalsIgnoreCase("")){
					Intent intent=new Intent(MainActivity.this,NormalWebViewActivity.class);
					intent.putExtra("url", notify.mRedirectUrl);
					startActivity(intent);
				}
			}
	    	
	    });
	    
	}
	 private List<Drawable> m_imgList = new ArrayList<Drawable>();
	 private void InitImgList() {
			// 加载图片数据（本demo仅获取本地资源，实际应用中，可异步加载网络数据）
		 m_imgList=AppDataManager.getInstance().getDrawableList();
		    }
	void createAdBanner(ListView listView){

		InitImgList();
		//final LinearLayout focusContainer=new LinearLayout(this);
		FrameLayout adFrameLayout=new FrameLayout(this);
		ListView.LayoutParams lp1=new ListView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		//FrameLayout.LayoutParams lp1=new FrameLayout.LayoutParams(DensityUtil.getLogicalWidth()-30,LayoutParams.WRAP_CONTENT);
		//lp1.gravity=Gravity.CENTER;
		adFrameLayout.setLayoutParams(lp1);
		
	
		//create nav and game
		
		
		MyGallery gallery=new MyGallery(this);
		gallery.setFadingEdgeLength(0);
		gallery.setSoundEffectsEnabled(false);
		gallery.setKeepScreenOn(true);
		//gallery.setBackgroundColor(Color.RED);
		LayoutParams lp4=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);//DensityUtil.dip2px(m_adHeight)
		gallery.setLayoutParams(lp4);
		adFrameLayout.addView(gallery);
		gallery.setAdapter(new ImageAdapter(this,this.m_imgList));
		gallery.setFocusable(true);
		final TextView desTxt=new TextView(this);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

		    public void onItemSelected(AdapterView<?> arg0, View arg1,
			    int selIndex, long arg3) {
			//修改上一次选中项的背景
		    	selIndex = selIndex % m_imgList.size();
		    	desTxt.setText(AppDataManager.getInstance().getAd(selIndex).mDescription);
			//ImageView preSelImg = (ImageView) focusContainer.findViewById(preSelImgIndex);
		//preSelImg.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.ic_focus));
			//修改当前选中项的背景
			//ImageView curSelImg = (ImageView) focusContainer.findViewById(selIndex);
			//curSelImg.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.ic_focus_select));
			preSelImgIndex = selIndex;
		    }

		    public void onNothingSelected(AdapterView<?> arg0) {
		    }
		});
		gallery.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View item, int index,
					long arg3) {
				// TODO Auto-generated method stub
		
			  MAd ad=AppDataManager.getInstance().getAd(index);
			  if(ad.mIsGallery){
				  List<MImage> m_imageList=new ArrayList<MImage>();
				  MImage img1=new MImage();
                  img1.mResourceId=R.drawable.kaifangri1;
                  m_imageList.add(img1);
                  
                  MImage img2=new MImage();
                  img2.mResourceId=R.drawable.kaifangri2;
                  m_imageList.add(img2);
                  
                  MImage img3=new MImage();
                  img3.mResourceId=R.drawable.kaifangri3;
                  m_imageList.add(img3);
                  
                  MImage img4=new MImage();
                  img4.mResourceId=R.drawable.kaifangri1;
                  m_imageList.add(img4);
				  Intent intent=new Intent(MainActivity.this,GalleryActivity.class);
					intent.putExtra("imageList",(Serializable)m_imageList);
					intent.putExtra("index",String.valueOf(index));
					startActivity(intent);
			  }
			  else{
			  if(ad.mType.equalsIgnoreCase("0")){
				  Intent intent = new Intent(MainActivity.this,NormalWebViewActivity.class);
	              intent.putExtra("url",ad.mInfo);
	              MainActivity.this.startActivity(intent);
			  }
			  
			}
			
		}});
		
		RelativeLayout bottomNavPoint=new RelativeLayout(this);
		bottomNavPoint.setBackgroundResource(R.drawable.banner_bottom_bg);
		bottomNavPoint.setId(1000);
		FrameLayout.LayoutParams lp3=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil.dip2px(20));
		
		//lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		//lp3.setMargins(0, 50, 0, 10);
	   // lp3.topMargin=100;
		lp3.gravity=Gravity.BOTTOM;
		//lp3.bottomMargin= DensityUtil.dip2px(5);
		//bottomNavPoint.setOrientation(LinearLayout.VERTICAL);
		//bottomNavPoint.setBackgroundColor(Color.RED);
		bottomNavPoint.setLayoutParams(lp3);
		adFrameLayout.addView(bottomNavPoint);
	    desTxt.setPadding(DensityUtil.dip2px(20), 0, 0, 0);
		desTxt.setTextSize(13);
		desTxt.setTextColor(Color.WHITE);
		RelativeLayout.LayoutParams lp6=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil.dip2px(20));
		desTxt.setLayoutParams(lp6);
		bottomNavPoint.addView(desTxt);
		View view=new View(this);
        RelativeLayout.LayoutParams lp5=new RelativeLayout.LayoutParams(0,0 );
        lp5.addRule(RelativeLayout.BELOW,1000);
        lp5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		view.setLayoutParams(lp5);
		adFrameLayout.addView(view);
		
		LayoutParams lp2=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT );
		lp2.gravity=Gravity.CENTER;
		//adFrameLayout.setBackgroundResource(R.drawable.news_bg);
		listView.addHeaderView(adFrameLayout);
	}
	
	void createSecondContent(){
		m_secondContent=new LinearLayout(this);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		m_secondContent.setLayoutParams(lp);
		m_secondView=this.m_layoutInflater.inflate(R.layout.second_layout,this.m_secondContent);
		//initView();
		initData();
		
	
		
	}
	
	@SuppressLint("NewApi")
	void createThirdContent(){
		m_thirdView=new ListView(this);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(DensityUtil.getLogicalWidth()-DensityUtil.dip2px(20), LayoutParams.MATCH_PARENT);
		lp.setMargins(DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10), DensityUtil.dip2px(10));
		m_thirdView.setLayoutParams(lp);
		m_thirdView.setVerticalScrollBarEnabled(false);
		m_thirdView.setCacheColorHint(Color.WHITE);
		m_thirdView.setDividerHeight(DensityUtil.dip2px(7));
		m_thirdView.setDivider(this.getResources().getDrawable(R.drawable.news_diver));
		m_thirdView.setFadingEdgeLength(0);
		m_thirdView.setHorizontalFadingEdgeEnabled(false);
		    if (Integer.parseInt(Build.VERSION.SDK) >= 9)
		    	m_thirdView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		    else 
		    	m_thirdView.setVerticalFadingEdgeEnabled(false);
		    
	   final List<MNews> newsList=AppDataManager.getInstance().getNews();
	if(newsList!=null)
		 m_newsAdapter=new NewsAdapter2(this,newsList);
	 else
		m_newsAdapter=new NewsAdapter2(this,new ArrayList<MNews>());
			   
	    m_thirdView.setAdapter(m_newsAdapter);	
	    this.m_thirdView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				MNews news=newsList.get(index);
				Intent intent=new Intent(MainActivity.this,NormalWebViewActivity.class);
				intent.putExtra("url", news.mRedirectUrl);
				startActivity(intent);
			}
	    	
	    });
	}
	
	void createFourthContent(){
		m_fourthContent=new LinearLayout(this);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		m_fourthContent.setLayoutParams(lp);
		View view=this.m_layoutInflater.inflate(R.layout.fourth_layout, m_fourthContent);
		LinearLayout firstCol=(LinearLayout) view.findViewById(R.id.first_col);

		LinearLayout secondCol=(LinearLayout) view.findViewById(R.id.second_col);
		int leftMargin=DensityUtil.dip2px(40);
		int rightMargin=DensityUtil.dip2px(25);
		int topMargin=DensityUtil.dip2px(20);
		int bottomMargin=DensityUtil.dip2px(20);
		int textTopMargin=DensityUtil.dip2px(10);
		int textSize=15;
		int textColor=Color.GRAY;
		int unitWidth=DensityUtil.getLogicalWidth()/2-leftMargin-rightMargin;
		int unitHeight=unitWidth;
		
	
	    
	    //gushi start
        LinearLayout gushiLayout=new LinearLayout(this);
        gushiLayout.setId(this.STORY);
        gushiLayout.setOrientation(1);
        LinearLayout.LayoutParams lp3=new LinearLayout.LayoutParams(unitWidth, LayoutParams.WRAP_CONTENT);
        lp3.leftMargin=leftMargin;
        lp3.rightMargin=rightMargin;
        lp3.topMargin=topMargin;
        lp3.bottomMargin=bottomMargin;
        gushiLayout.setLayoutParams(lp3);
        
        ImageView gushiImg=new ImageView(this);
        gushiImg.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams lp31=new LinearLayout.LayoutParams(unitWidth,unitHeight);
        gushiImg.setLayoutParams(lp31);
        gushiImg.setImageDrawable(this.getResources().getDrawable(R.drawable.gushi));
        gushiLayout.addView(gushiImg);
        
        TextView gushiTxt=new TextView(this);
        LinearLayout.LayoutParams lp32=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp32.gravity=Gravity.CENTER;
        gushiTxt.setGravity(Gravity.CENTER);
        lp32.topMargin=textTopMargin; 
        gushiTxt.setLayoutParams(lp32);
        gushiTxt.setTextSize(textSize);
        gushiTxt.setTextColor(textColor);
        gushiTxt.setText(this.getResources().getString(R.string.gushi));
	    gushiLayout.addView(gushiTxt);
	    
	    gushiLayout.setOnClickListener(this);
	    firstCol.addView(gushiLayout);
	    //gushi end
	    
	    
	    //huiben start
	    LinearLayout lianxirenLayout=new LinearLayout(this);
	    lianxirenLayout.setId(this.HUIBEN);
	    lianxirenLayout.setOrientation(1);
        LinearLayout.LayoutParams lp2=new LinearLayout.LayoutParams(unitWidth, LayoutParams.WRAP_CONTENT);
        lp2.leftMargin=rightMargin;
        lp2.rightMargin=leftMargin;
        lp2.topMargin=topMargin;
        lp2.bottomMargin=bottomMargin;
        lianxirenLayout.setLayoutParams(lp2);
        
        ImageView lianxirenImg=new ImageView(this);
        lianxirenImg.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams lp21=new LinearLayout.LayoutParams(unitWidth,unitHeight);
        lianxirenImg.setLayoutParams(lp21);
        lianxirenImg.setImageDrawable(this.getResources().getDrawable(R.drawable.huiben));
        lianxirenLayout.addView(lianxirenImg);
        
        TextView lianxirenTxt=new TextView(this);
        LinearLayout.LayoutParams lp22=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp22.gravity=Gravity.CENTER;
        lianxirenTxt.setGravity(Gravity.CENTER);
        lp22.topMargin=textTopMargin;
        lianxirenTxt.setLayoutParams(lp22);
        lianxirenTxt.setTextSize(textSize);
        lianxirenTxt.setTextColor(textColor);
        lianxirenTxt.setText(this.getResources().getString(R.string.huiben));
	    lianxirenLayout.addView(lianxirenTxt);
	    
	    lianxirenLayout.setOnClickListener(this);
	    secondCol.addView(lianxirenLayout);
	    //huiben end
	    
		//kehouquan start
        LinearLayout kehouquanLayout=new LinearLayout(this);
        kehouquanLayout.setId(this.KEHOUQUAN);
        kehouquanLayout.setOrientation(1);
        LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(unitWidth, LayoutParams.WRAP_CONTENT);
        lp1.leftMargin=leftMargin;
        lp1.rightMargin=rightMargin;
        lp1.topMargin=0;
        lp1.bottomMargin=bottomMargin;
        kehouquanLayout.setLayoutParams(lp1);
        
        ImageView kehouquanImg=new ImageView(this);
        kehouquanImg.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams lp11=new LinearLayout.LayoutParams(unitWidth,unitHeight);
        kehouquanImg.setLayoutParams(lp11);
        kehouquanImg.setImageDrawable(this.getResources().getDrawable(R.drawable.kehouquan));
        kehouquanLayout.addView(kehouquanImg);
        
        TextView kehouquanTxt=new TextView(this);
        LinearLayout.LayoutParams lp12=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp12.gravity=Gravity.CENTER;
        kehouquanTxt.setGravity(Gravity.CENTER);
        lp12.topMargin=textTopMargin;
        kehouquanTxt.setLayoutParams(lp12);
	    kehouquanTxt.setTextSize(textSize);
	    kehouquanTxt.setTextColor(textColor);
	    kehouquanTxt.setText(this.getResources().getString(R.string.kehouquan));
	    kehouquanLayout.addView(kehouquanTxt);
	    
	    kehouquanLayout.setOnClickListener(this);
	    firstCol.addView(kehouquanLayout);
	    //kehouquan end 
	    
	    //photo start
	    LinearLayout photoLayout=new LinearLayout(this);
	    photoLayout.setId(this.PHOTO);
	    photoLayout.setOrientation(1);
        LinearLayout.LayoutParams lp4=new LinearLayout.LayoutParams(unitWidth, LayoutParams.WRAP_CONTENT);
        lp4.leftMargin=rightMargin;
        lp4.rightMargin=leftMargin;
        lp4.topMargin=0;
        lp4.bottomMargin=bottomMargin;
        photoLayout.setLayoutParams(lp4);
        
        ImageView photoImg=new ImageView(this);
        photoImg.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams lp41=new LinearLayout.LayoutParams(unitWidth,unitHeight);
        photoImg.setLayoutParams(lp41);
        photoImg.setImageDrawable(this.getResources().getDrawable(R.drawable.photo));
        photoLayout.addView(photoImg);
        
        TextView photoTxt=new TextView(this);
        LinearLayout.LayoutParams lp42=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp42.gravity=Gravity.CENTER;
        photoTxt.setGravity(Gravity.CENTER);
        lp42.topMargin=textTopMargin;
        photoTxt.setLayoutParams(lp42);
        photoTxt.setTextSize(textSize);
        photoTxt.setTextColor(textColor);
        photoTxt.setText(this.getResources().getString(R.string.photo));
        photoLayout.addView(photoTxt);
	    
        photoLayout.setOnClickListener(this);
	    secondCol.addView(photoLayout);
	    //photo end
	    
	    //qimeng start
        LinearLayout qimengLayout=new LinearLayout(this);
        qimengLayout.setId(this.QIMENG);
        qimengLayout.setOrientation(1);
        LinearLayout.LayoutParams lp5=new LinearLayout.LayoutParams(unitWidth, LayoutParams.WRAP_CONTENT);
        lp5.leftMargin=leftMargin;
        lp5.rightMargin=rightMargin;
        lp5.topMargin=0;
        lp5.bottomMargin=0;
        qimengLayout.setLayoutParams(lp5);
        
        ImageView qimengImg=new ImageView(this);
        qimengImg.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams lp51=new LinearLayout.LayoutParams(unitWidth,unitHeight);
        qimengImg.setLayoutParams(lp51);
        qimengImg.setImageDrawable(this.getResources().getDrawable(R.drawable.qimeng));
        qimengLayout.addView(qimengImg);
        
        TextView qimengTxt=new TextView(this);
        LinearLayout.LayoutParams lp52=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp52.gravity=Gravity.CENTER;
        qimengTxt.setGravity(Gravity.CENTER);
        lp52.topMargin=textTopMargin; 
        qimengTxt.setLayoutParams(lp52);
        qimengTxt.setTextSize(textSize);
        qimengTxt.setTextColor(textColor);
        qimengTxt.setText(this.getResources().getString(R.string.qimeng));
        qimengLayout.addView(qimengTxt);
	    
        qimengLayout.setOnClickListener(this);
	    firstCol.addView(qimengLayout);
	    //qimeng end
	    
	    //setting start
	    LinearLayout settingLayout=new LinearLayout(this);
	    settingLayout.setId(this.SETTING);
	    settingLayout.setOrientation(1);
        LinearLayout.LayoutParams lp6=new LinearLayout.LayoutParams(unitWidth, LayoutParams.WRAP_CONTENT);
        lp6.leftMargin=rightMargin;
        lp6.rightMargin=leftMargin;
        lp6.topMargin=0;
        lp6.bottomMargin=0;
        settingLayout.setLayoutParams(lp6);
        
        ImageView settingImg=new ImageView(this);
        settingImg.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams lp61=new LinearLayout.LayoutParams(unitWidth,unitHeight);
        settingImg.setLayoutParams(lp61);
        settingImg.setImageDrawable(this.getResources().getDrawable(R.drawable.setting));
        settingLayout.addView(settingImg);
         
        TextView settingTxt=new TextView(this);
        LinearLayout.LayoutParams lp62=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp62.gravity=Gravity.CENTER;
        settingTxt.setGravity(Gravity.CENTER);
        lp62.topMargin=textTopMargin;
        settingTxt.setLayoutParams(lp62);
        settingTxt.setTextSize(textSize);
        settingTxt.setTextColor(textColor);
        settingTxt.setText(this.getResources().getString(R.string.setting));
        settingLayout.addView(settingTxt);
	    
        settingLayout.setOnClickListener(this);
	    secondCol.addView(settingLayout);
	    //setting end
	}
		
	void reMeasure(){

		float rate=(float)123/(float)180;
	  
	    int imgWidth=this.m_width/4;
	    int imgHeight=(int) (imgWidth*rate+0.5);
	    LinearLayout.LayoutParams firstLp=new LinearLayout.LayoutParams(imgWidth,imgHeight);
	    this.m_firstImg.setLayoutParams(firstLp);
	    
	    LinearLayout.LayoutParams secondLp=new LinearLayout.LayoutParams(imgWidth,imgHeight);
	    this.m_secondImg.setLayoutParams(secondLp);
	    
	    LinearLayout.LayoutParams thirdLp=new LinearLayout.LayoutParams(imgWidth,imgHeight);
	    this.m_thirdImg.setLayoutParams(thirdLp);
	    
 	    LinearLayout.LayoutParams fourthLp=new LinearLayout.LayoutParams(imgWidth,imgHeight);
	    this.m_fourthImg.setLayoutParams(fourthLp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public int getBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

	void removeView(int currentSelectIndex){
		switch(currentSelectIndex){
		case 0:
			
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		}
	}
	private final static String MSGKEY = "msgkey";
	private void send() {
		String contString = m_editTextContent.getText().toString();
		if (contString.length() > 0) {
			MChatMsgEntity entity = new MChatMsgEntity();
			entity.setDate(getDate());
			entity.setMsgType(false);
			entity.setText(contString);

			m_dataArrays.add(entity);
			m_adapter.notifyDataSetChanged();
			m_editTextContent.setText("");
			m_listView.setSelection(m_listView.getCount() - 1);
			PushManager.sendMsgToUser(this,appid ,"954515449122882861", MSGKEY, contString);
		}
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}
	boolean m_secondViewHasCreate=false;
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.first:
			if(this.m_currentSelectIndex==0)
				return;
			 this.m_currentSelectIndex=0;
			 this.m_content.removeAllViews();
			 this.m_content.addView(m_firstContent);
			break;
		case R.id.second:
			if(this.m_currentSelectIndex==1)return;
				this.m_currentSelectIndex=1;
				if(!this.m_secondViewHasCreate){
					this.initView();
				    this.m_secondViewHasCreate=true;
				}
			    this.m_content.removeAllViews();
			   this.m_content.addView(m_secondContent);
				
			break;
		case R.id.third:
			if(this.m_currentSelectIndex==2)
				return;
			this.m_currentSelectIndex=2;
			 this.m_content.removeAllViews();
			 this.m_content.addView(m_thirdView);
			break;
		case R.id.fourth:
			if(this.m_currentSelectIndex==3)
				return;
			this.m_currentSelectIndex=3;
			 this.m_content.removeAllViews();
			 this.m_content.addView(m_fourthContent);
			break;
		case R.id.btn_send:
			send();
			break;
		case STORY:
			Intent intent=new Intent(MainActivity.this,StoryListActivity.class);
			startActivity(intent);
			break;
		case HUIBEN:
			 intent=new Intent(MainActivity.this,HuibenListActivity.class);
			startActivity(intent);
			break;	
		case PHOTO:
			 intent=new Intent(MainActivity.this,PhotoListActivity.class);
			startActivity(intent);
			break;	
		case R.id.fabu:
			 intent=new Intent(MainActivity.this,EditContentActivity.class);
			 startActivity(intent);
			break;
		case R.id.website:
			 String host = "bcs.duapp.com";
			BCSCredentials credentials = new BCSCredentials("IT1wrHzZQCOQUSzeaUR1lCgb", "aNG7Vdv7XjhG1V9ACl63NcMtawnYdHK7");
			BaiduBCS baiduBCS = new BaiduBCS(credentials, host);
			// baiduBCS.setDefaultEncoding("GBK");
			baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8
			
			InputStream fileContent=this.getResources().openRawResource(R.raw.baba1);
			//File file =new File("/sdcard/ayouer/image/");
			//InputStream fileContent;
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType("image/jpg");
			
			try {
				objectMetadata.setContentLength(fileContent.available());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PutObjectRequest request = new PutObjectRequest("sst-youer", "/image/test", fileContent, objectMetadata);
			
			ObjectMetadata result = baiduBCS.putObject(request).getResult();
			;
		    intent=new Intent(MainActivity.this,NormalWebViewActivity.class);
			intent.putExtra("url", "http://hpefhy.zgyey.com/Default.aspx");
			startActivity(intent);
			break;
		}
		this.selectView(this.m_currentSelectIndex);
	}

	@Override
	public void onMessage(com.way.bean.Message message) {
		// TODO Auto-generated method stub
		Message handlerMsg = handler.obtainMessage(NEW_MESSAGE);
		handlerMsg.obj = message;
		handler.sendMessage(handlerMsg);
	}

	@Override
	public void onBind(String method, int errorCode, String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotify(String title, String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		// TODO Auto-generated method stub
		if (!isNetConnected) {
			T.showShort(this, R.string.net_error_tip);
			//mNetErrorView.setVisibility(View.VISIBLE);
		} else {
			//mNetErrorView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNewFriend(User u) {
		// TODO Auto-generated method stub
		Message handlerMsg = handler.obtainMessage(NEW_FRIEND);
		handlerMsg.obj = u;
		handler.sendMessage(handlerMsg);
	}
}
