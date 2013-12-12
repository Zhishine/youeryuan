package com.way.db;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.way.bean.User;

public class UserDB {
	private UserDBHelper helper;

	public UserDB(Context context) {
		helper = new UserDBHelper(context);
	}

	public User selectInfo(int id) {
		User u = new User();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from user where id=?",
				new String[] { id + "" });
		if (c.moveToFirst()) {
			u.setHeadIcon(c.getInt(c.getColumnIndex("img")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setChannelId(c.getString(c.getColumnIndex("channelId")));
			u.setGroup(c.getInt(c.getColumnIndex("_group")));
			u.setRole(c.getInt(c.getColumnIndex("role")));
			u.setUserId(c.getString(c.getColumnIndex("userId")));
			u.setPhoneNum(c.getString(c.getColumnIndex("phoneNum")));
			u.setId(id);
		} else {
			return null;
		}
		return u;
	}

	public void addUser(List<User> list) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for (User u : list) {
			db.execSQL(
					"insert into user (id,role,userId,nick,img,channelId,_group,phoneNum) values(?,?,?,?,?,?,?,?)",
					new Object[] { u.getId(),u.getRole(),u.getUserId(), u.getNick(), u.getHeadIcon(),
							u.getChannelId(), u.getGroup(),u.getPhoneNum() });
		}
		db.close();
	}

	public void addUser(User u) {
		if (selectInfo(u.getId()) != null) {
			update(u);
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into user (id,role,userId,nick,img,channelId,_group,phoneNum) values(?,?,?,?,?,?,?,?)",
				new Object[] { u.getId(),u.getRole(),u.getUserId(), u.getNick(), u.getHeadIcon(),
						u.getChannelId(), u.getGroup(),u.getPhoneNum() });
		db.close();

	}

	public User getUser(int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from user where id=?",
				new String[] { id +""});
		User u = new User();
		if (c.moveToNext()) {
			u.setId(c.getInt(c.getColumnIndex("id")));
			u.setRole(c.getInt(c.getColumnIndex("role")));
			u.setUserId(c.getString(c.getColumnIndex("userId")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setHeadIcon(c.getInt(c.getColumnIndex("img")));
			u.setChannelId(c.getString(c.getColumnIndex("channelId")));
			u.setGroup(c.getInt(c.getColumnIndex("_group")));
			u.setPhoneNum(c.getString(c.getColumnIndex("phoneNum")));
		}
		return u;
	}

	public void updateUser(List<User> list) {
		if (list.size() > 0) {
			delete();
			addUser(list);
		}
	}

	public List<User> getUser() {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<User> list = new LinkedList<User>();
		Cursor c = db.rawQuery("select * from user", null);
		while (c.moveToNext()) {
			User u = new User();
			u.setId(c.getInt(c.getColumnIndex("id")));
			u.setRole(c.getInt(c.getColumnIndex("role")));
			u.setUserId(c.getString(c.getColumnIndex("userId")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setHeadIcon(c.getInt(c.getColumnIndex("img")));
			u.setChannelId(c.getString(c.getColumnIndex("channelId")));
			u.setGroup(c.getInt(c.getColumnIndex("_group")));
			u.setPhoneNum(c.getString(c.getColumnIndex("phoneNum")));
			list.add(u);
		}
		c.close();
		db.close();
		return list;
	}

	public void update(User u) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update user set nick=?,img=?,_group=? where id=?",
				new Object[] { u.getNick(), u.getHeadIcon(), u.getGroup(),
						u.getId() });
		db.close();
	}

	public User getLastUser() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from user", null);
		User u = new User();
		while (c.moveToLast()) {
			u.setId(c.getInt(c.getColumnIndex("id")));
			u.setRole(c.getInt(c.getColumnIndex("role")));
			u.setUserId(c.getString(c.getColumnIndex("userId")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setHeadIcon(c.getInt(c.getColumnIndex("img")));
			u.setChannelId(c.getString(c.getColumnIndex("channelId")));
			u.setGroup(c.getInt(c.getColumnIndex("_group")));
			u.setPhoneNum(c.getString(c.getColumnIndex("phoneNum")));
		}
		c.close();
		db.close();
		return u;
	}

	public void delUser(User u) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from user where id=?",
				new Object[] { u.getId() });
		db.close();
	}

	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from user");
		db.close();
	}
}
