package com.youer.activity;

import java.util.List;

import com.example.youer.R;
import com.example.youer.R.layout;
import com.example.youer.R.menu;
import com.way.adapter.ContactAdapter;
import com.way.app.PushApplication;
import com.way.bean.User;
import com.way.db.MessageDB;
import com.youer.tool.DensityUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class ContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		final List<User> userList=PushApplication.getInstance().getUserList();
		ListView friendListView=(ListView) findViewById(R.id.list);
		ContactAdapter adapter=new ContactAdapter(this,userList);
		friendListView.setAdapter(adapter);
		final MessageDB msgDb=PushApplication.getInstance().getMessageDB();
		friendListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int index,
					long length) {
				// TODO Auto-generated method stub
				User u=userList.get(index);
				msgDb.clearNewCount(u.getId());// ÐÂÏûÏ¢ÖÃ¿Õ
				Intent toChatIntent = new Intent(ContactActivity.this,
						ChatActivity.class);
				toChatIntent.putExtra("user", u);
				startActivity(toChatIntent);
			}
			
		});
		ImageView back=(ImageView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContactActivity.this.finish();
			}
			
		});
		ImageView contactSearch=(ImageView)findViewById(R.id.contact_search_bg);
		contactSearch.setScaleType(ScaleType.FIT_XY);
		int height=(int) (DensityUtil.getLogicalWidth()*((float)122/(float)720));
		LinearLayout.LayoutParams lp=(LayoutParams) contactSearch.getLayoutParams();
		lp.width=DensityUtil.getLogicalWidth();
		lp.height=height;
		contactSearch.setLayoutParams(lp);
	}

	

}
