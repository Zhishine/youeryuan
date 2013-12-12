package com.youer.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.youer.R;
import com.youer.modal.MChatMsgEntity;

/**
 * 
 ******************************************
 * @author 寤栦箖娉�
 * @鏂囦欢鍚嶇О	:  ChatMsgAdapter.java
 * @鍒涘缓鏃堕棿	: 2013-1-27 涓嬪崍02:33:16
 * @鏂囦欢鎻忚堪	: 娑堟伅鏁版嵁濉厖璧�
 ******************************************
 */
public class ChatMsgAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<MChatMsgEntity> coll;
	private LayoutInflater mInflater;
	private Context context;
	public ChatMsgAdapter(Context context, List<MChatMsgEntity> coll) {
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		MChatMsgEntity entity = coll.get(position);

		if (entity.getMsgType()) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		MChatMsgEntity entity = coll.get(position);
		boolean isComMsg = entity.getMsgType();

		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSendTime.setText(entity.getDate());
		//SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, entity.getText());
		viewHolder.tvContent.setText(entity.getText());

		return convertView;
	}

	class ViewHolder {
		public TextView tvSendTime;
		public TextView tvContent;
		public boolean isComMsg = true;
	}

}
