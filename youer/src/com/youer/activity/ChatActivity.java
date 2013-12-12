package com.youer.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youer.R;
import com.google.gson.Gson;
import com.way.adapter.FaceAdapter;
import com.way.adapter.FacePageAdeapter;
import com.way.adapter.MessageAdapter;
import com.way.app.PushApplication;
import com.way.baidupush.client.PushMessageReceiver;
import com.way.bean.MessageItem;
import com.way.bean.RecentItem;
import com.way.bean.User;
import com.way.common.util.HomeWatcher;
import com.way.common.util.HomeWatcher.OnHomePressedListener;
import com.way.common.util.L;
import com.way.common.util.SendMsgAsyncTask;
import com.way.common.util.SharePreferenceUtil;
import com.way.common.util.T;
import com.way.db.MessageDB;
import com.way.db.RecentDB;
import com.way.swipeback.SwipeBackActivity;
import com.way.view.CirclePageIndicator;
import com.way.view.JazzyViewPager;
import com.way.view.JazzyViewPager.TransitionEffect;
import com.way.xlistview.MsgListView;
import com.way.xlistview.MsgListView.IXListViewListener;

public class ChatActivity extends Activity implements
		PushMessageReceiver.EventHandler, OnTouchListener, OnClickListener,
		OnHomePressedListener, IXListViewListener {
	public static final int NEW_MESSAGE = 0x001;// �յ���Ϣ
	private TransitionEffect mEffects[] = { TransitionEffect.Standard,
			TransitionEffect.Tablet, TransitionEffect.CubeIn,
			TransitionEffect.CubeOut, TransitionEffect.FlipVertical,
			TransitionEffect.FlipHorizontal, TransitionEffect.Stack,
			TransitionEffect.ZoomIn, TransitionEffect.ZoomOut,
			TransitionEffect.RotateUp, TransitionEffect.RotateDown,
			TransitionEffect.Accordion, };// ���鷭ҳЧ��
	private PushApplication mApplication;
	private static int MsgPagerNum;
	private MessageDB mMsgDB;
	private RecentDB mRecentDB;
	private int currentPage = 0;
	private boolean isFaceShow = false;
	private Button sendBtn;
	private ImageButton faceBtn;
	private EditText msgEt;
	private LinearLayout faceLinearLayout;
	private JazzyViewPager faceViewPager;
	private WindowManager.LayoutParams params;
	private InputMethodManager imm;
	private List<String> keys;
	private MessageAdapter adapter;
	private MsgListView mMsgListView;
	private SharePreferenceUtil mSpUtil;
	private User mFromUser;
	private TextView mTitle, mTitleRightBtn;
	ImageView mTitleLeftBtn;
	ImageView m_PhoneImg;
	private HomeWatcher mHomeWatcher;
	private Gson mGson;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == NEW_MESSAGE) {
				// String message = (String) msg.obj;
				com.way.bean.Message msgItem = (com.way.bean.Message) msg.obj;
				String userId = msgItem.getUser_id();
				int id=msgItem.getId();
				int role=msgItem.getRole();
				if (!(id==mFromUser.getId()))// ������ǵ�ǰ��������������Ϣ��������
					return;

				int headId = msgItem.getHead_id();
				/*
				 * try { headId = Integer
				 * .parseInt(JsonUtil.getFromUserHead(message)); } catch
				 * (Exception e) { L.e("head is not integer  " + e); }
				 */
				// TODO Auto-generated method stub
				MessageItem item = new MessageItem(
						MessageItem.MESSAGE_TYPE_TEXT, msgItem.getNick(),
						System.currentTimeMillis(), msgItem.getMessage(),
						headId, true, 0);
				adapter.upDateMsg(item);
				mMsgDB.saveMsg(msgItem.getId(), item);
				RecentItem recentItem = new RecentItem(id,userId,role, headId,
						msgItem.getNick(), msgItem.getMessage(), 0,
						System.currentTimeMillis());
				mRecentDB.saveRecent(recentItem);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		initData();
		initView();
		initFacePage();
		PushMessageReceiver.ehList.add(this);// �������͵���Ϣ
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mHomeWatcher = new HomeWatcher(this);
		mHomeWatcher.setOnHomePressedListener(this);
		mHomeWatcher.startWatch();
		
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PushMessageReceiver.ehList.remove(this);// �Ƴ�����
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
		faceLinearLayout.setVisibility(View.GONE);
		isFaceShow = false;
		super.onPause();
		mHomeWatcher.setOnHomePressedListener(null);
		mHomeWatcher.stopWatch();
	
	}

	private void initData() {
		mFromUser = (User) getIntent().getSerializableExtra("user");
		if (mFromUser == null) {// ���Ϊ�գ�ֱ�ӹر�
			finish();
		}
		mApplication = PushApplication.getInstance();
		mSpUtil = mApplication.getSpUtil();
		mGson = mApplication.getGson();
		mMsgDB = mApplication.getMessageDB();
		mRecentDB = mApplication.getRecentDB();
		Set<String> keySet = PushApplication.getInstance().getFaceMap()
				.keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);
		MsgPagerNum = 0;
		adapter = new MessageAdapter(this, initMsgData());
	}

	/**
	 * ������Ϣ��ʷ�������ݿ��ж���
	 */
	private List<MessageItem> initMsgData() {
		List<MessageItem> list = mMsgDB.getMsg(mFromUser.getId(),
				MsgPagerNum);
		List<MessageItem> msgList = new ArrayList<MessageItem>();// ��Ϣ��������
		if (list.size() > 0) {
			for (MessageItem entity : list) {
				if (entity.getName().equals("")) {
					entity.setName(mFromUser.getNick());
				}
				if (entity.getHeadImg() < 0) {
					entity.setHeadImg(mFromUser.getHeadIcon());
				}
				msgList.add(entity);
			}
		}
		return msgList;

	}

	private void initFacePage() {
		// TODO Auto-generated method stub
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < PushApplication.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
		faceViewPager.setAdapter(adapter);
		faceViewPager.setCurrentItem(currentPage);
		faceViewPager.setTransitionEffect(mEffects[mSpUtil.getFaceEffect()]);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(faceViewPager);
		adapter.notifyDataSetChanged();
		faceLinearLayout.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}

	private GridView getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// ����GridViewĬ�ϵ��Ч��
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == PushApplication.NUM) {// ɾ������λ��
					int selection = msgEt.getSelectionStart();
					String text = msgEt.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							msgEt.getText().delete(start, end);
							return;
						}
						msgEt.getText().delete(selection - 1, selection);
					}
				} else {
					int count = currentPage * PushApplication.NUM + arg2;
					// ע�͵Ĳ��֣���EditText����ʾ�ַ���
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// �����ⲿ�֣���EditText����ʾ����
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) PushApplication
									.getInstance().getFaceMap().values()
									.toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// ������������
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// �½�������
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// ����ͼƬ����ת�Ƕ�
						// matrix.postRotate(-30);
						// ����ͼƬ����б
						// matrix.postSkew(0.1f, 0.1f);
						// ��ͼƬ��Сѹ��
						// ѹ����ͼƬ�Ŀ�͸��Լ�kB��С����仯
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this,
								newBitmap);
						String emojiStr = keys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						msgEt.append(spannableString);
					} else {
						String ori = msgEt.getText().toString();
						int index = msgEt.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, keys.get(count));
						msgEt.setText(stringBuilder.toString());
						msgEt.setSelection(index + keys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	private void initView() {
		// TODO Auto-generated method stub
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		params = getWindow().getAttributes();

		mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
		// ����ListView���ر�������뷨
		mMsgListView.setOnTouchListener(this);
		mMsgListView.setPullLoadEnable(false);
		mMsgListView.setXListViewListener(this);
		mMsgListView.setAdapter(adapter);
		mMsgListView.setSelection(adapter.getCount() - 1);
		sendBtn = (Button) findViewById(R.id.send_btn);
		faceBtn = (ImageButton) findViewById(R.id.face_btn);
		msgEt = (EditText) findViewById(R.id.msg_et);
		faceLinearLayout = (LinearLayout) findViewById(R.id.face_ll);
		faceViewPager = (JazzyViewPager) findViewById(R.id.face_pager);
		msgEt.setOnTouchListener(this);
		mTitle = (TextView) findViewById(R.id.nick);
		mTitle.setText(mFromUser.getNick());
		mTitleLeftBtn = (ImageView) findViewById(R.id.back);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		// mTitleRightBtn = (TextView) findViewById(R.id.ivTitleBtnRigh);
		mTitleLeftBtn.setOnClickListener(this);
		
		m_PhoneImg = (ImageView) findViewById(R.id.phone);
		m_PhoneImg.setVisibility(View.VISIBLE);
		// mTitleRightBtn = (TextView) findViewById(R.id.ivTitleBtnRigh);
		m_PhoneImg.setOnClickListener(this);
		// mTitleRightBtn.setOnClickListener(this);
		msgEt.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (params.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
							|| isFaceShow) {
						faceLinearLayout.setVisibility(View.GONE);
						isFaceShow = false;
						// imm.showSoftInput(msgEt, 0);
						return true;
					}
				}
				return false;
			}
		});
		msgEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					sendBtn.setEnabled(true);
				} else {
					sendBtn.setEnabled(false);
				}
			}
		});
		faceBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
	}

	// ��ֹ��pageview�ҹ���
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public void onMessage(com.way.bean.Message message) {
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
		if (!isNetConnected)
			T.showShort(this, "���������ѶϿ�");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.face_btn:
			if (!isFaceShow) {
				imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
				try {
					Thread.sleep(80);// �����ʱ���һ����Ļ������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				faceLinearLayout.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				faceLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
			}
			break;
		case R.id.send_btn:// ������Ϣ
			String msg = msgEt.getText().toString();
			MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT,
					mSpUtil.getNick(), System.currentTimeMillis(), msg,
					mSpUtil.getHeadIcon(), false, 0);
			adapter.upDateMsg(item);
			// if (adapter.getCount() - 10 > 10) {
			// L.i("begin to remove...");
			// adapter.removeHeadMsg();
			// MsgPagerNum--;
			// }
			mMsgListView.setSelection(adapter.getCount() - 1);
			mMsgDB.saveMsg(mFromUser.getId(), item);
			msgEt.setText("");
			com.way.bean.Message msgItem = new com.way.bean.Message(
					System.currentTimeMillis(), msg, "");
			new SendMsgAsyncTask(mGson.toJson(msgItem), mFromUser.getUserId())
					.send();
			RecentItem recentItem = new RecentItem(mFromUser.getId(),mFromUser.getUserId(),mFromUser.getRole(),
					mFromUser.getHeadIcon(), mFromUser.getNick(), msg, 0,
					System.currentTimeMillis());
			mRecentDB.saveRecent(recentItem);
			break;
		case R.id.back:
			finish();
			break;
		case R.id.phone:
			 Intent intent = new Intent();
			    intent.setAction("android.intent.action.DIAL");
			    intent.setData(Uri.parse("tel:"+this.mFromUser.getPhoneNum()));
			    startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.msg_listView:
			imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;
		case R.id.msg_et:
			imm.showSoftInput(msgEt, 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;
		case R.id.phone:
			imm.showSoftInput(msgEt, 0);
			faceLinearLayout.setVisibility(View.GONE);
			isFaceShow = false;
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onNewFriend(User u) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHomePressed() {
		// TODO Auto-generated method stub
		mApplication.showNotification();
	}

	@Override
	public void onHomeLongPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		MsgPagerNum++;
		List<MessageItem> msgList = initMsgData();
		int position = adapter.getCount();
		adapter.setMessageList(msgList);
		mMsgListView.stopRefresh();
		mMsgListView.setSelection(adapter.getCount() - position - 1);
		L.i("MsgPagerNum = " + MsgPagerNum + ", adapter.getCount() = "
				+ adapter.getCount());
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

}