package com.youer.modal;


public class MRecentItem implements Comparable<MRecentItem> {
	private String userId;
	private int headImg;//
	private String name;//
	private String message;//
	private int newNum;// 	
	private long time;// 
//	public static final int[] heads = { R.drawable.h0, R.drawable.h1,
//			R.drawable.h2, R.drawable.h3, R.drawable.h4, R.drawable.h5,
//			R.drawable.h6, R.drawable.h7, R.drawable.h8, R.drawable.h9,
//			R.drawable.h10, R.drawable.h11, R.drawable.h12, R.drawable.h13,
//			R.drawable.h14, R.drawable.h15, R.drawable.h16, R.drawable.h17,
//			R.drawable.h18 };

	public MRecentItem() {
	}

	public MRecentItem(String userId, int headImg, String name, String message,
			int newNum, long time) {
		super();
		this.userId = userId;
		this.headImg = headImg;
		this.name = name;
		this.message = message;
		this.newNum = newNum;
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getHeadImg() {
		return headImg;
	}

	public void setHeadImg(int headImg) {
		this.headImg = headImg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNewNum() {
		return newNum;
	}

	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

//	public static int[] getHeads() {
//		return heads;
//	}

	@Override
	public int hashCode() {
		int code = 0;
		code = (31 * (this.userId.hashCode())) >> 2;
		return code;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof MRecentItem) {
			MRecentItem item = (MRecentItem) o;
			if (item.userId.equals(this.userId))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(MRecentItem another) {
		// TODO Auto-generated method stub
		return (int) (another.time - this.time);
	}

}
