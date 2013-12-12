package com.youer.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class Utils {
	public static final String TAG = "PushDemoActivity";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	public static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	public static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";
	
	// 鑾峰彇AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
        	return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
            	apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options options,  
            int reqWidth, int reqHeight) {  
        // 源图片的高度和宽度  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
        if (height > reqHeight || width > reqWidth) {  
            // 计算出实际宽高和目标宽高的比率  
            final int heightRatio = Math.round((float) height / (float) reqHeight);  
            final int widthRatio = Math.round((float) width / (float) reqWidth);  
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高  
            // 一定都会大于等于目标的宽和高。  
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
        }  
        return inSampleSize;  
    }

}
