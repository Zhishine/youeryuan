package com.youer.modal;

import java.io.Serializable;
import java.util.List;

public class MNotify implements Serializable{
public int mId;
public String mTitle;
public String mDescription;
public String mContent;
public List<MVideo> mVideoList;
public List<MImage> mImageList;
public boolean mIsNative=true;
public String mRedirectUrl;
public String mTime;
}
