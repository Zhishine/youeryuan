package com.youer.modal;

import java.io.Serializable;

public class MNews  implements Serializable{
	public int mId;
	public String mTitle;
	public String mRedirectUrl="";
	public String mDescription="";
	public String mTitleImageUrl="";
	public boolean mIsWebPage=true;
	public String mExt1="";
	public String mExt2="";
	public String mExt3="";
	public boolean mIsGallery=false;
}
