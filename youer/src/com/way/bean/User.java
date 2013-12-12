package com.way.bean;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String UserId;//
	private String channelId;
	private String nick;//
	private int headIcon;//
	private int group;
    private int role;
    private int id;
    private String phoneNum="13416111872";
	public User(int id,String UserId,int role, String channelId, String nick, int headIcon,
			int group,String phoneNum) {
		// TODO Auto-generated constructor stub
		this.UserId = UserId;
		this.channelId = channelId;
		this.nick = nick;
		this.headIcon = headIcon;
		this.group = group;
		this.role=role;
		this.id=id;
		this.phoneNum=phoneNum;
	}

	public User() {

	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum=phoneNum;
	}
	
	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public int getRole(){
		return this.role;
	}
	
	public void setRole(int role){
		this.role=role;
	}
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(int headIcon) {
		this.headIcon = headIcon;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "User [UserId=" + UserId + ", channelId=" + channelId
				+ ", nick=" + nick + ", headIcon=" + headIcon + ", group="
				+ group + "]";
	}

}
