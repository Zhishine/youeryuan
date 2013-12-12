package com.way.baidupush.client;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.youer.activity.MainActivity;
import com.way.app.PushApplication;
import com.way.bean.Message;
import com.way.bean.MessageItem;
import com.way.bean.RecentItem;
import com.way.bean.User;
import com.way.common.util.L;
import com.way.common.util.NetUtil;
import com.way.common.util.SendMsgAsyncTask;
import com.way.common.util.SharePreferenceUtil;
import com.way.common.util.T;
import com.example.youer.R;

@SuppressLint("NewApi")
public class PushMessageReceiver extends BroadcastReceiver {
	public static final String TAG = PushMessageReceiver.class.getSimpleName();
	public static final int NOTIFY_ID = 0x000;
	public static int mNewNum = 0;// ֪ͨ������Ϣ��Ŀ����ֻ������һ��ȫ�ֱ�����
	public static final String RESPONSE = "response";
	public static ArrayList<EventHandler> ehList = new ArrayList<EventHandler>();

	public static abstract interface EventHandler {
		public abstract void onMessage(Message message);

		public abstract void onBind(String method, int errorCode, String content);

		public abstract void onNotify(String title, String content);

		public abstract void onNetChange(boolean isNetConnected);

		public void onNewFriend(User u);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// L.d(TAG, ">>> Receive intent: \r\n" + intent);
		L.i("listener num = " + ehList.size());
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			// ��ȡ��Ϣ����
			String message = intent.getExtras().getString(
					PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			// ��Ϣ���û��Զ������ݶ�ȡ��ʽ
			L.i("onMessage: " + message);
			try {
				Message msgItem = PushApplication.getInstance().getGson()
						.fromJson(message, Message.class);
				parseMessage(msgItem);// Ԥ��������һЩ��Ϣ������˵�����ʺ���Լ����͵�
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			// ����󶨵ȷ����ķ�������
			// PushManager.startWork()�ķ���ֵͨ��PushConstants.METHOD_BIND�õ�
			// ��ȡ����
			final String method = intent
					.getStringExtra(PushConstants.EXTRA_METHOD);
			// �������ش����롣���󶨷��ش��󣨷�0������Ӧ�ý���������������Ϣ��
			// ��ʧ�ܵ�ԭ���ж��֣�������ԭ�򣬻�access token���ڡ�
			// �벻Ҫ�ڳ���ʱ���м򵥵�startWork���ã����п��ܵ�����ѭ����
			// ����ͨ���������Դ���������������ʱ�����µ����������
			final int errorCode = intent
					.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
							PushConstants.ERROR_SUCCESS);
			// ��������
			final String content = new String(
					intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));

			// �û��ڴ��Զ��崦����Ϣ,���´���Ϊdemo����չʾ��
			L.i("onMessage: method : " + method + ", result : " + errorCode
					+ ", content : " + content);
			paraseContent(context, errorCode, content);// ������Ϣ

			// �ص�����
			for (int i = 0; i < ehList.size(); i++)
				((EventHandler) ehList.get(i)).onBind(method, errorCode,
						content);

			// ��ѡ��֪ͨ�û�����¼�����
		} else if (intent.getAction().equals(
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			L.d(TAG, "intent=" + intent.toUri(0));
			String title = intent
					.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
			String content = intent
					.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
			for (int i = 0; i < ehList.size(); i++)
				((EventHandler) ehList.get(i)).onNotify(title, content);
		} else if (intent.getAction().equals(
				"android.net.conn.CONNECTIVITY_CHANGE")) {
			boolean isNetConnected = NetUtil.isNetConnected(context);
			for (int i = 0; i < ehList.size(); i++)
				((EventHandler) ehList.get(i)).onNetChange(isNetConnected);
		}
	}

	private void parseMessage(Message msg) {
		Gson gson = PushApplication.getInstance().getGson();
		// Message msg = gson.fromJson(message, Message.class);
		L.i("gson ====" + msg.toString());
		String tag = msg.getTag();

		String userId = msg.getUser_id();
		int headId = msg.getHead_id();
		int id=msg.getId();
		int role=msg.getRole();
		// try {
		// headId = Integer.parseInt(JsonUtil.getFromUserHead(message));
		// } catch (Exception e) {
		// L.e("head is not a Integer....");
		// }
		if (!TextUtils.isEmpty(tag)) {// ����Ǵ���tag����Ϣ
			if (userId.equals(PushApplication.getInstance().getSpUtil()
					.getUserId()))
				return;
			User u = new User(id,userId,role, msg.getChannel_id(), msg.getNick(),
					headId, 0,"");
			PushApplication.getInstance().getUserDB().addUser(u);// �������º���
			for (EventHandler handler : ehList)
				handler.onNewFriend(u);
			if (!tag.equals(RESPONSE)) {
				// Intent intenService = new
				// Intent(PushApplication.getInstance(),
				// PreParseService.class);
				// intenService.putExtra("message", message);
				// PushApplication.getInstance().startService(intenService);//
				// ��������ȥ����Ϣ
				// L.i("��������ظ���Ϣ");
				L.i("response start");
				Message item = new Message(System.currentTimeMillis(), "hi",
						PushMessageReceiver.RESPONSE);
				new SendMsgAsyncTask(gson.toJson(item), userId).send();// ͬʱҲ��һ����Ϣ���Է�1
				L.i("response end");
			}
		} else {// ��ͨ��Ϣ��
			if (PushApplication.getInstance().getSpUtil().getMsgSound())// ����û�������������
				PushApplication.getInstance().getMediaPlayer().start();
			if (ehList.size() > 0) {// �м�����ʱ�򣬴�����ȥ
				for (int i = 0; i < ehList.size(); i++)
					((EventHandler) ehList.get(i)).onMessage(msg);
			} else {
				// ֪ͨ�����ѣ��������ݿ�
				// show notify
				showNotify(msg);
				MessageItem item = new MessageItem(
						MessageItem.MESSAGE_TYPE_TEXT, msg.getNick(),
						System.currentTimeMillis(), msg.getMessage(), headId,
						true, 1);
				RecentItem recentItem = new RecentItem(id,userId,role, headId,
						msg.getNick(), msg.getMessage(), 0,
						System.currentTimeMillis());
				PushApplication.getInstance().getMessageDB()
						.saveMsg(id, item);
				PushApplication.getInstance().getRecentDB()
						.saveRecent(recentItem);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void showNotify(Message message) {
		// TODO Auto-generated method stub
		mNewNum++;
		// ����֪ͨ��
		PushApplication application = PushApplication.getInstance();

		int icon = R.drawable.notify_newmessage;
		CharSequence tickerText = message.getNick() + ":"
				+ message.getMessage();
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);

		notification.flags = Notification.FLAG_NO_CLEAR;
		// ����Ĭ������
		// notification.defaults |= Notification.DEFAULT_SOUND;
		// �趨��(���VIBRATEȨ��)
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.contentView = null;

		Intent intent = new Intent(application, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(application, 0,
				intent, 0);
		notification.setLatestEventInfo(PushApplication.getInstance(),
				application.getSpUtil().getNick() + " (" + mNewNum + "������Ϣ)",
				tickerText, contentIntent);
		// ������4.0֪ͨ��api
		// Bitmap headBm = BitmapFactory.decodeResource(
		// application.getResources(), PushApplication.heads[Integer
		// .parseInt(JsonUtil.getFromUserHead(message))]);
		// Notification.Builder mNotificationBuilder = new
		// Notification.Builder(application)
		// .setTicker(tickerText)
		// .setContentTitle(JsonUtil.getFromUserNick(message))
		// .setContentText(JsonUtil.getMsgContent(message))
		// .setSmallIcon(R.drawable.notify_newmessage)
		// .setLargeIcon(headBm).setWhen(System.currentTimeMillis())
		// .setContentIntent(contentIntent);
		// Notification n = mNotificationBuilder.getNotification();
		// n.flags |= Notification.FLAG_NO_CLEAR;
		//
		// n.defaults |= Notification.DEFAULT_VIBRATE;

		application.getNotificationManager().notify(NOTIFY_ID, notification);// ֪ͨһ�²Ż���ЧŶ
	}
  static boolean success=false;
	/**
	 * �����¼���
	 * 
	 * @param errorCode
	 * @param content
	 */
	private void paraseContent(final Context context, int errorCode,
			String content) {
		// TODO Auto-generated method stub
		if (errorCode == 0) {
			String appid = "";
			String channelid = "";
			String userid = "";
            success=true;
			try {
				JSONObject jsonContent = new JSONObject(content);
				JSONObject params = jsonContent
						.getJSONObject("response_params");
				appid = params.getString("appid");
				channelid = params.getString("channel_id");
				userid = params.getString("user_id");
			} catch (JSONException e) {
				L.e(TAG, "Parse bind json infos error: " + e);
			}
			SharePreferenceUtil util = PushApplication.getInstance()
					.getSpUtil();
			util.setAppId(appid);
			util.setChannelId(channelid);
			util.setUserId(userid);
		} else {
			if (NetUtil.isNetConnected(context)) {
				if (errorCode == 30607) {
					T.showLong(context, "�˺��ѹ��ڣ������µ�¼");
					// ��ת�����µ�¼�Ľ���
				} else {
					if(!success){
					T.showLong(context, "����ʧ�ܣ���������...");
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							PushManager.startWork(context,
									PushConstants.LOGIN_TYPE_API_KEY,
									PushApplication.API_KEY);
						}
					}, 2000);// ��������¿�ʼ��֤
					}
				}
			} else {
				T.showLong(context, R.string.net_error_tip);
			}
		}
	}
}
