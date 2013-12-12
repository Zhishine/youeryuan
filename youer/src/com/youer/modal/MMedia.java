package com.youer.modal;

import java.io.Serializable;
import java.util.List;

public class MMedia implements Serializable{
public boolean mIsVideo=false;
public String mName;
public List<MImage> mPhotoList;
public List<MVideo> mVideoList;
}
