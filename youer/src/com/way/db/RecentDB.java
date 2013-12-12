package com.way.db;

import java.util.Collections;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.way.bean.RecentItem;

public class RecentDB {
	public static final String MSG_DBNAME = "message.db";
	private static final String RECENT_TABLE_NAME = "recent";
	private SQLiteDatabase db;

	public RecentDB(Context context) {
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS "
				+ RECENT_TABLE_NAME
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,id TEXT,role TEXT,userId TEXT, name TEXT, img TEXT,time TEXT,num TEXT,message TEXT)");
	}

	public void saveRecent(RecentItem item) {
		if (isExist(item.getId())) {
			ContentValues cv = new ContentValues();
			cv.put("id", item.getId());
			cv.put("role", item.getRole());
			cv.put("name", item.getName());
			cv.put("img", item.getHeadImg());
			cv.put("time", item.getTime());
			cv.put("num", item.getNewNum());
			cv.put("message", item.getMessage());

			db.update(RECENT_TABLE_NAME, cv, "id=?",
					new String[] {String.valueOf(item.getId()) });
		} else {
			db.execSQL(
					"insert into "
							+ RECENT_TABLE_NAME
							+ " (id,role,userId,name,img,time,num,message) values(?,?,?,?,?,?,?,?)",
					new Object[] {item.getId(),item.getRole(), item.getUserId(), item.getName(),
							item.getHeadImg(), item.getTime(),
							item.getNewNum(), item.getMessage() });
		}
	}

	public LinkedList<RecentItem> getRecentList() {
		LinkedList<RecentItem> list = new LinkedList<RecentItem>();
		Cursor c = db.rawQuery("SELECT * from " + RECENT_TABLE_NAME, null);
		while (c.moveToNext()) {
			int id=c.getInt(c.getColumnIndex("id"));
			int role=c.getInt(c.getColumnIndex("role"));
			String userId = c.getString(c.getColumnIndex("userId"));
			String name = c.getString(c.getColumnIndex("name"));
			int icon = c.getInt(c.getColumnIndex("img"));
			long time = c.getLong(c.getColumnIndex("time"));
			int num = c.getInt(c.getColumnIndex("num"));
			String message = c.getString(c.getColumnIndex("message"));
			RecentItem item = new RecentItem(id,userId,role, icon, name, message, num,
					time);
			list.add(item);
		}
		Collections.sort(list);// 
		return list;
	}

	public void delRecent(int id) {
		db.delete(RECENT_TABLE_NAME, "userId=?", new String[] { String.valueOf(id) });
	}

	private boolean isExist(int id) {
		Cursor c = db.rawQuery("SELECT * FROM " + RECENT_TABLE_NAME
				+ " WHERE id = ?", new String[] { String.valueOf(id) });
		return c.moveToFirst();
	}

	public void close() {
		if (db != null)
			db.close();
	}
}
