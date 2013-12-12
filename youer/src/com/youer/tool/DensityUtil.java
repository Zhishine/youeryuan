package com.youer.tool;

import android.content.Context;
import android.util.DisplayMetrics;
 
 
//���㹫ʽ pixels = dips * (density / 160)
 
public class DensityUtil {
    
    private static final String TAG = DensityUtil.class.getSimpleName();
    
    // ��ǰ��Ļ��densityDpi
    private static float dmDensityDpi = 0.0f;
    private static DisplayMetrics dm;
    private static float scale = 0.0f;
 
    
    public DensityUtil(Context context) {
        // ��ȡ��ǰ��Ļ
        dm = new DisplayMetrics();
 
        //���ص�ǰ��Դ�����DispatchMetrics��Ϣ��
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        // ����DensityDpi
        setDmDensityDpi(dm.densityDpi);
        // �ܶ�����
        scale = getDmDensityDpi() / 160;//���� scale=dm.density;
       
    }
    
    public static int getLogicalHeight(){
    	return dm.heightPixels;
    }
    
    public static int getLogicalWidth(){
    	return dm.widthPixels;
    }
    
    public static int getActualHeight(){
    	return px2dip(dm.heightPixels);
    }
    
    public static int getActualWidth(){
    	return px2dip(dm.widthPixels);
    }
    public static float getDmDensityDpi() {
        return dmDensityDpi;
    }
 
    
    public static void setDmDensityDpi(float dmDensityDpi) {
        DensityUtil.dmDensityDpi = dmDensityDpi;
    }
 
    
    public static int dip2px(float dipValue) {
 
        return (int) (dipValue * scale + 0.5f);
 
    }
 
    
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }
 
    @Override
    public String toString() {
        return " dmDensityDpi:" + dmDensityDpi;
    }
    
    
}