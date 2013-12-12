package com.way.adapter;

import java.util.List;

import com.example.youer.R;
import com.way.app.PushApplication;
import com.way.bean.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	private LayoutInflater m_inflater;
	List<User> m_userList=null;
	Context m_context;
	public ContactAdapter(Context context,List<User> userList){
		this.m_context=context;
		this.m_userList=userList;
		this.m_inflater= LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_userList.size();
	}

	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return m_userList.get(index);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		User user=this.m_userList.get(index);
		ViewHolder holder=null;
	   if(view==null){
		   view=this.m_inflater.inflate(R.layout.friend_item,null);
		   holder=new ViewHolder();
		   holder.mIcon=(ImageView) view.findViewById(R.id.icon);
		   holder.mNick=(TextView) view.findViewById(R.id.nick);
		   view.setTag(holder);
	   }else{
		   holder=(ViewHolder) view.getTag();
	   }
	   holder.mIcon.setBackgroundResource(PushApplication.getInstance().heads1[user.getHeadIcon()]);
	   holder.mNick.setText(user.getNick());
		return view;
	}

    static class ViewHolder{
    	public ImageView mIcon;
    	public TextView mNick;
    }
}
