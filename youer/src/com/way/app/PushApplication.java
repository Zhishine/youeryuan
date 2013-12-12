package com.way.app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.youer.activity.MainActivity;
import com.youer.modal.MChatMsgEntity;
import com.youer.modal.MUser;
import com.way.baidupush.client.PushMessageReceiver;
import com.way.baidupush.client.PushMessageReceiver.EventHandler;
import com.way.baidupush.server.BaiduPush;
import com.way.bean.Message;
import com.way.bean.User;
import com.way.common.util.SharePreferenceUtil;
import com.way.db.MessageDB;
import com.way.db.RecentDB;
import com.way.db.UserDB;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.youer.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PushApplication extends Application implements EventHandler {
	public final static String API_KEY = "IT1wrHzZQCOQUSzeaUR1lCgb";
	public final static String SECRIT_KEY = "aNG7Vdv7XjhG1V9ACl63NcMtawnYdHK7";
	public static final String SP_FILE_NAME = "push_msg_sp";
	public static final int[] heads = { R.drawable.h0, R.drawable.h1,
			R.drawable.h2, R.drawable.h3, R.drawable.h4, R.drawable.h5,
			R.drawable.h6, R.drawable.h7, R.drawable.h8, R.drawable.h9,
			R.drawable.h10, R.drawable.h11, R.drawable.h12, R.drawable.h13,
			R.drawable.h14, R.drawable.h15, R.drawable.h16, R.drawable.h17,
			R.drawable.h18 };
    public static final int[] heads1={R.drawable.laoshi1,R.drawable.jiazhang1};
	public static final int NUM_PAGE = 6;// �ܹ��ж���ҳ
	public static int NUM = 20;// ÿҳ20������,�������һ��ɾ��button
	private static PushApplication mApplication;
	private BaiduPush mBaiduPushServer;

	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
	private SharePreferenceUtil mSpUtil;
	private UserDB mUserDB;
	private MessageDB mMsgDB;
	private RecentDB mRecentDB;
	private List<User> mUserList;
	private MediaPlayer mMediaPlayer;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private Gson mGson;
	 List<User> m_userList;
	   List<MChatMsgEntity> m_chatList=null;
	   int index=0;
	public synchronized static PushApplication getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		CrashHandler.getInstance().init(this);
		initFaceMap();
		initData();
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheSize(2*1024*1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	private void initData() {
		this.initUserData();
		//this.initChatContent();
		mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
				SECRIT_KEY, API_KEY);
		// ��ת��û�� @Expose ע����ֶ�
		mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		mUserDB = new UserDB(this);
		mMsgDB = new MessageDB(this);
		mRecentDB = new RecentDB(this);
	
		mMediaPlayer = MediaPlayer.create(this, R.raw.office);
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	
		if (mSpUtil.getId()==0) {
			PushMessageReceiver.ehList.add(this);// �������͵���Ϣ
		    index=1;//��ʦ
		    initFriend();  
		    User user=getCurrentUser();
		    mSpUtil.setId(user.getId());
		    mSpUtil.setRole(user.getRole());
		    mSpUtil.setHeadIcon(user.getHeadIcon());
		    mSpUtil.setNick(user.getNick());
			//mSpUtil.setTag(SexAdapter.SEXS[mSexWheel.getCurrentItem()]);
			
		}
		if(TextUtils.isEmpty( mSpUtil.getUserId()))
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY, PushApplication.API_KEY);
		int id=mSpUtil.getId();
		int role=mSpUtil.getRole();
		String userId=mSpUtil.getUserId();
		String name=mSpUtil.getNick();
		String channelId=mSpUtil.getChannelId();
		mUserList = mUserDB.getUser();
	}
	
	void initFriend(){
		if(index==0){
		mUserDB.addUser(this.m_userList.get(1));
		mUserDB.addUser(this.m_userList.get(2));
		}else{
		mUserDB.addUser(this.m_userList.get(0));
		}
	}
	
	 public User getCurrentUser(){
	    	return this.m_userList.get(index);
	    }
	 
//	 void initChatContent(){
//	    	MChatMsgEntity chat1=new MChatMsgEntity();
//	    	chat1.setDate("2012-12-12 12:00");
//	    	chat1.setName(this.m_userList.get(1).mName);
//	    	chat1.setMsgType(false);
//	    	chat1.setText("�¸�������ҳ������գ�");
//	    	chat1.setTimeShow(false);
//	    	
//	    	MChatMsgEntity chat2=new MChatMsgEntity();
//	    	chat2.setDate("2012-12-12 12:00");
//	    	chat2.setName(this.m_userList.get(1).mName);
//	    	chat2.setMsgType(true);
//	    	chat2.setText("�õ�");
//	    	chat2.setTimeShow(false);
//	    	
//	    	MChatMsgEntity chat3=new MChatMsgEntity();
//	    	chat3.setDate("2012-12-12 12:00");
//	    	chat3.setName(this.m_userList.get(1).mName);
//	    	chat3.setMsgType(true);
//	    	chat3.setText("���ٵ�");
//	    	chat3.setTimeShow(false);
//	    	
//	    	MChatMsgEntity chat4=new MChatMsgEntity();
//	    	chat4.setDate("2012-12-12 12:00");
//	    	chat4.setName(this.m_userList.get(1).mName);
//	    	chat4.setMsgType(false);
//	    	chat4.setText("�ŵ�");
//	    	chat4.setTimeShow(true);
//	    }
//	    
	
	  void initUserData(){
			this.m_userList=new ArrayList<User>();
			User user1=new User();
			user1.setId(1);
			user1.setNick("a����ʦ");
			user1.setRole(0);
			user1.setHeadIcon(0);
			user1.setUserId("954515449122882861");
			user1.setPhoneNum("13416111872");
			m_userList.add(user1);
			
			User user2=new User();
			user2.setId(2);
			user2.setNick("aС���ҳ�");
			user2.setRole(1);
			user2.setUserId("701586223983023931");
			user2.setHeadIcon(1);
			user2.setPhoneNum("13660606574");
			m_userList.add(user2);
			
			User user3=new User();
			user3.setId(3);
			user3.setNick("aС��ҳ�");
			user3.setUserId("828868000250887501");
			user3.setRole(1);
			user3.setHeadIcon(1);
            user3.setPhoneNum("15819095037");
			m_userList.add(user3);
			
		}
	  
//	  
	public synchronized BaiduPush getBaiduPush() {
		if (mBaiduPushServer == null)
			mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
					SECRIT_KEY, API_KEY);
		return mBaiduPushServer;

	}

	public synchronized Gson getGson() {
		if (mGson == null)
			// ��ת��û�� @Expose ע����ֶ�
			mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
					.create();
		return mGson;
	}

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}

	public synchronized MediaPlayer getMediaPlayer() {
		if (mMediaPlayer == null)
			mMediaPlayer = MediaPlayer.create(this, R.raw.office);
		return mMediaPlayer;
	}

	public synchronized UserDB getUserDB() {
		if (mUserDB == null)
			mUserDB = new UserDB(this);
		return mUserDB;
	}

	public synchronized RecentDB getRecentDB() {
		if (mRecentDB == null)
			mRecentDB = new RecentDB(this);
		return mRecentDB;
	}

	public synchronized MessageDB getMessageDB() {
		if (mMsgDB == null)
			mMsgDB = new MessageDB(this);
		return mMsgDB;
	}

	public synchronized List<User> getUserList() {
		if (mUserList == null)
			mUserList = getUserDB().getUser();
		return mUserList;
	}

	public synchronized SharePreferenceUtil getSpUtil() {
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		return mSpUtil;
	}

	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

	/**
	 * �����һ�ͼ��
	 */
	@SuppressWarnings("deprecation")
	public void showNotification() {
		if (!mSpUtil.getMsgNotify())// ����û����ò���ʾ�һ�ͼ�ֱ꣬�ӷ���
			return;

		int icon = R.drawable.notify_general;
		CharSequence tickerText = getResources().getString(
				R.string.app_is_run_background);
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);

		// ������"��������"��Ŀ��
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notify_status_bar_latest_event_view);
		contentView.setImageViewResource(R.id.icon,
				heads[mSpUtil.getHeadIcon()]);
		contentView.setTextViewText(R.id.title, mSpUtil.getNick());
		contentView.setTextViewText(R.id.text, tickerText);
		contentView.setLong(R.id.time, "setTime", when);
		// ָ�����Ի���ͼ
		mNotification.contentView = contentView;

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// ָ��������ͼ
		mNotification.contentIntent = contentIntent;
		// ������4.0notify
		// Bitmap icon = BitmapFactory.decodeResource(getResources(),
		// heads[mSpUtil.getHeadIcon()]);
		// Notification.Builder notificationBuilder = new Notification.Builder(
		// this).setContentTitle(mSpUtil.getNick())
		// .setContentText(tickerText)
		// .setSmallIcon(R.drawable.notify_general)
		// .setWhen(System.currentTimeMillis())
		// .setContentIntent(contentIntent).setLargeIcon(icon);
		// Notification n = notificationBuilder.getNotification();
		// n.flags |= Notification.FLAG_NO_CLEAR;

		mNotificationManager.notify(PushMessageReceiver.NOTIFY_ID,
				mNotification);
	}

	private void initFaceMap() {
		// TODO Auto-generated method stub
		mFaceMap.put("[����]", R.drawable.f000);
		mFaceMap.put("[��Ƥ]", R.drawable.f001);
		mFaceMap.put("[����]", R.drawable.f002);
		mFaceMap.put("[͵Ц]", R.drawable.f003);
		mFaceMap.put("[�ټ�]", R.drawable.f004);
		mFaceMap.put("[�ô�]", R.drawable.f005);
		mFaceMap.put("[����]", R.drawable.f006);
		mFaceMap.put("[��ͷ]", R.drawable.f007);
		mFaceMap.put("[õ��]", R.drawable.f008);
		mFaceMap.put("[����]", R.drawable.f009);
		mFaceMap.put("[���]", R.drawable.f010);
		mFaceMap.put("[��]", R.drawable.f011);
		mFaceMap.put("[��]", R.drawable.f012);
		mFaceMap.put("[ץ��]", R.drawable.f013);
		mFaceMap.put("[ί��]", R.drawable.f014);
		mFaceMap.put("[���]", R.drawable.f015);
		mFaceMap.put("[ը��]", R.drawable.f016);
		mFaceMap.put("[�˵�]", R.drawable.f017);
		mFaceMap.put("[�ɰ�]", R.drawable.f018);
		mFaceMap.put("[ɫ]", R.drawable.f019);
		mFaceMap.put("[����]", R.drawable.f020);

		mFaceMap.put("[����]", R.drawable.f021);
		mFaceMap.put("[��]", R.drawable.f022);
		mFaceMap.put("[΢Ц]", R.drawable.f023);
		mFaceMap.put("[��ŭ]", R.drawable.f024);
		mFaceMap.put("[����]", R.drawable.f025);
		mFaceMap.put("[����]", R.drawable.f026);
		mFaceMap.put("[�亹]", R.drawable.f027);
		mFaceMap.put("[����]", R.drawable.f028);
		mFaceMap.put("[ʾ��]", R.drawable.f029);
		mFaceMap.put("[����]", R.drawable.f030);
		mFaceMap.put("[����]", R.drawable.f031);
		mFaceMap.put("[�ѹ�]", R.drawable.f032);
		mFaceMap.put("[����]", R.drawable.f033);
		mFaceMap.put("[����]", R.drawable.f034);
		mFaceMap.put("[˯]", R.drawable.f035);
		mFaceMap.put("[����]", R.drawable.f036);
		mFaceMap.put("[��Ц]", R.drawable.f037);
		mFaceMap.put("[����]", R.drawable.f038);
		mFaceMap.put("[˥]", R.drawable.f039);
		mFaceMap.put("[Ʋ��]", R.drawable.f040);
		mFaceMap.put("[����]", R.drawable.f041);

		mFaceMap.put("[�ܶ�]", R.drawable.f042);
		mFaceMap.put("[����]", R.drawable.f043);
		mFaceMap.put("[�Һߺ�]", R.drawable.f044);
		mFaceMap.put("[ӵ��]", R.drawable.f045);
		mFaceMap.put("[��Ц]", R.drawable.f046);
		mFaceMap.put("[����]", R.drawable.f047);
		mFaceMap.put("[����]", R.drawable.f048);
		mFaceMap.put("[��]", R.drawable.f049);
		mFaceMap.put("[���]", R.drawable.f050);
		mFaceMap.put("[����]", R.drawable.f051);
		mFaceMap.put("[ǿ]", R.drawable.f052);
		mFaceMap.put("[��]", R.drawable.f053);
		mFaceMap.put("[����]", R.drawable.f054);
		mFaceMap.put("[ʤ��]", R.drawable.f055);
		mFaceMap.put("[��ȭ]", R.drawable.f056);
		mFaceMap.put("[��л]", R.drawable.f057);
		mFaceMap.put("[��]", R.drawable.f058);
		mFaceMap.put("[����]", R.drawable.f059);
		mFaceMap.put("[����]", R.drawable.f060);
		mFaceMap.put("[ơ��]", R.drawable.f061);
		mFaceMap.put("[Ʈ��]", R.drawable.f062);

		mFaceMap.put("[����]", R.drawable.f063);
		mFaceMap.put("[OK]", R.drawable.f064);
		mFaceMap.put("[����]", R.drawable.f065);
		mFaceMap.put("[����]", R.drawable.f066);
		mFaceMap.put("[Ǯ]", R.drawable.f067);
		mFaceMap.put("[����]", R.drawable.f068);
		mFaceMap.put("[��Ů]", R.drawable.f069);
		mFaceMap.put("[��]", R.drawable.f070);
		mFaceMap.put("[����]", R.drawable.f071);
		mFaceMap.put("[�]", R.drawable.f072);
		mFaceMap.put("[ȭͷ]", R.drawable.f073);
		mFaceMap.put("[����]", R.drawable.f074);
		mFaceMap.put("[̫��]", R.drawable.f075);
		mFaceMap.put("[����]", R.drawable.f076);
		mFaceMap.put("[����]", R.drawable.f077);
		mFaceMap.put("[����]", R.drawable.f078);
		mFaceMap.put("[����]", R.drawable.f079);
		mFaceMap.put("[����]", R.drawable.f080);
		mFaceMap.put("[����]", R.drawable.f081);
		mFaceMap.put("[��]", R.drawable.f082);
		mFaceMap.put("[����]", R.drawable.f083);

		mFaceMap.put("[��ĥ]", R.drawable.f084);
		mFaceMap.put("[�ٱ�]", R.drawable.f085);
		mFaceMap.put("[����]", R.drawable.f086);
		mFaceMap.put("[�ܴ���]", R.drawable.f087);
		mFaceMap.put("[��ߺ�]", R.drawable.f088);
		mFaceMap.put("[��Ƿ]", R.drawable.f089);
		mFaceMap.put("[�����]", R.drawable.f090);
		mFaceMap.put("[��]", R.drawable.f091);
		mFaceMap.put("[����]", R.drawable.f092);
		mFaceMap.put("[ƹ����]", R.drawable.f093);
		mFaceMap.put("[NO]", R.drawable.f094);
		mFaceMap.put("[����]", R.drawable.f095);
		mFaceMap.put("[���]", R.drawable.f096);
		mFaceMap.put("[תȦ]", R.drawable.f097);
		mFaceMap.put("[��ͷ]", R.drawable.f098);
		mFaceMap.put("[��ͷ]", R.drawable.f099);
		mFaceMap.put("[����]", R.drawable.f100);
		mFaceMap.put("[����]", R.drawable.f101);
		mFaceMap.put("[����]", R.drawable.f102);
		mFaceMap.put("[����]", R.drawable.f103);
		mFaceMap.put("[��̫��]", R.drawable.f104);

		mFaceMap.put("[��̫��]", R.drawable.f105);
		mFaceMap.put("[����]", R.drawable.f106);
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBind(String method, int errorCode, String content) {
		// TODO Auto-generated method stub
		if (errorCode == 0) {// ������˺ųɹ������ڵ�һ�����У���ͬһtag��������һ��������Ϣ
//			User u = new User(mSpUtil.getId(),mSpUtil.getUserId(),mSpUtil.getRole(), mSpUtil.getChannelId(),
//					mSpUtil.getNick(), mSpUtil.getHeadIcon(), 0);
			//mUserDB.addUser(u);// ���Լ���ӵ����ݿ�
//			com.way.bean.Message msgItem = new com.way.bean.Message(
//					System.currentTimeMillis(), "hi", mSpUtil.getTag());
//			task = new SendMsgAsyncTask(mGson.toJson(msgItem), "");
//			task.setOnSendScuessListener(new OnSendScuessListener() {
//
//				@Override
//				public void sendScuess() {
//					startActivity(new Intent(FirstSetActivity.this,
//							MainActivity.class));
//					if (mConnectServerDialog != null
//							&& mConnectServerDialog.isShowing())
//						mConnectServerDialog.dismiss();
//
//					if (mLoginOutTimeProcess != null
//							&& mLoginOutTimeProcess.running)
//						mLoginOutTimeProcess.stop();
//					T.showShort(mApplication, R.string.first_start_scuess);
//					finish();
//				}
//			});
//			task.send();
		}
	}

	@Override
	public void onNotify(String title, String content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewFriend(User u) {
		// TODO Auto-generated method stub
		
	}

}
