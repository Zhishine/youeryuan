package com.youer.tool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.example.youer.R;
import com.youer.adapter.NotifyAdapter;
import com.youer.modal.MAd;
import com.youer.modal.MChatMsgEntity;
import com.youer.modal.MMedia;
import com.youer.modal.MNews;
import com.youer.modal.MNotify;
import com.youer.modal.MUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.view.View.OnClickListener;
public class AppDataManager {
    private static final AppDataManager m_instance=new AppDataManager();
   
    private Context m_context;
    private SharedPreferences m_setting=null;
    private Drawable m_leftUpIcon=null;
    private String m_leftUpRedirectUrl=null;
    private Drawable m_rightUpIcon=null;
    private String m_rightUpRedirectUrl="http://m.ai9475.com/mimi/index";
    private Drawable m_leftDowmIcon=null;
    private String m_leftDownRedirectUrl="http://www.nhdz.cc/mob/";
    private Drawable m_rightDownIcon=null;
    private String m_rightDownRedirectUrl="http://eladies.sina.cn/?sa=t95d30v545&vt=4&cid=1090&wm=4007_0009";
    public String systemImgDir="/sdcard/ayouer/img/";
    private String systemAdDir="/sdcard/ayouer/ad/";
    
    public String systemImageDir = "/sdcard/ayouer/image/";
    public String systemTempDir= "/sdcard/youer/tmp/";
    private boolean m_bannerIsShow=true;
    private boolean m_adIsShow=true;
    private boolean m_taobaokeIsShow=true;
    private long m_lastUpdateTime=0;
    private long m_lastAddateTime=0;
    private long m_lastAdDateTimeTemp=0;
    List<Drawable> m_adDrawable=null;
    List<MAd> m_adList;
    private AppDataManager(){
    
    }
    
    public static AppDataManager getInstance(){
    	return m_instance;
    }
   
    public List<MNotify> mNotifyList=null;
    public List<MMedia> mMediaList=null;
    public void init(Context context) throws IOException{
    	this.m_context=context;
    	List<MAd> list=new ArrayList<MAd>();
   	  MAd ad1=new MAd();
   	ad1.mIsGallery=true;
   	  ad1.mOrder=1;
   	  ad1.mType="0";
   	ad1.mDescription="家长开放日";
   	  list.add(ad1);
   	  
   	  MAd ad2=new MAd();
  	  ad2.mInfo="http://php1.hontek.com.cn/wordpress/archives/372.html";
  	  ad2.mOrder=2;
  	  ad2.mType="0";
  	ad2.mDescription="开学了";
  	
  	  list.add(ad2);
  	  
  	  MAd ad3=new MAd();
   	  ad3.mInfo="http://www.baidu.com";
   	  ad3.mOrder=3;
   	  ad3.mType="0";
   	ad3.mIsGallery=true;
   	ad3.mDescription="开学了";
   	  list.add(ad3);
   	  
   	  MAd ad4=new MAd();
  	  ad4.mInfo="http://www.baidu.com";
  	  ad4.mOrder=4;
  	  ad4.mType="0";
   	ad4.mIsGallery=true;
  	ad4.mDescription="开学了";
  	  list.add(ad4);
  	  this.m_adList=list;
  	  
  	  
  	m_adDrawable=new ArrayList<Drawable>();
  	m_adDrawable.add(m_context.getResources().getDrawable(R.drawable.kaifangri1));
	m_adDrawable.add(m_context.getResources().getDrawable(R.drawable.kaixue1));
	m_adDrawable.add(m_context.getResources().getDrawable(R.drawable.kaixue3));
	m_adDrawable.add(m_context.getResources().getDrawable(R.drawable.kaixue4));
     }
    
 
   
  
    public boolean SaveImage(Bitmap bm,String fileName){
    	
    	boolean ret  = false;
    	try {
    		ret  = AppDataManager.this.saveImg(bm, this.systemImageDir, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return ret;
    }
    
 public boolean SaveTemp(Bitmap bm,String fileName){
    	
    	boolean ret  = false;
    	
    	try {
    		ret  = AppDataManager.this.saveImg(bm, this.systemTempDir, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return ret;
    }
   
    public int getFontSizeLevel(){
    	m_setting=m_context.getSharedPreferences("app_data", 0);
    	int fontSizeLevel=m_setting.getInt("fontSizeLevel",0);
    	return fontSizeLevel;
    }
    public void setFontSizeLevel(int level){
    	m_setting=m_context.getSharedPreferences("app_data", 0);
    	SharedPreferences.Editor localEditor = m_setting.edit();
    	localEditor.putInt("fontSizeLevel", level);
    	localEditor.commit();
    }
    public void saveData(){
    	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
  		 ObjectOutputStream oos;
  		 try {
  			SharedPreferences dataSetting=m_context.getSharedPreferences("initData", 0);
  	    	SharedPreferences.Editor editor = dataSetting.edit();
  			oos = new ObjectOutputStream(baos);
  			oos.writeObject(this.mNotifyList);
  			String adListBase64 = new String(Base64.encode(baos.toByteArray(),0));
  		
  			editor.putString("data", adListBase64);
  			editor.commit();
  			
  		  } catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		  }
    }
    
    public void saveMediaData(){
   	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 		 ObjectOutputStream oos;
 		 try {
 			SharedPreferences dataSetting=m_context.getSharedPreferences("mediaData", 0);
 	    	SharedPreferences.Editor editor = dataSetting.edit();
 			oos = new ObjectOutputStream(baos);
 			oos.writeObject(this.mMediaList);
 			String adListBase64 = new String(Base64.encode(baos.toByteArray(),0));
 		
 			editor.putString("data", adListBase64);
 			editor.commit();
 			
 		  } catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		  }
   }
    
    public NotifyAdapter mNotifyAdapter=null;
    
    
    public void getData() throws StreamCorruptedException, IOException, ClassNotFoundException{

    	if(this.mNotifyList==null){
    	SharedPreferences dataSetting=m_context.getSharedPreferences("initData", 0);
    	SharedPreferences.Editor editor = dataSetting.edit();
    	//editor.clear();
    	//editor.commit();
     	String dataListStr=dataSetting.getString("data",null);
    	byte[] base64Bytes = Base64.decode(dataListStr,0);
 		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
 		ObjectInputStream ois = new ObjectInputStream(bais);
 		this.mNotifyList= (List<MNotify>) ois.readObject();
 		
    	}
    }
    
    public void getMediaData() throws StreamCorruptedException, IOException, ClassNotFoundException{

    	if(this.mMediaList==null){
    	SharedPreferences dataSetting=m_context.getSharedPreferences("mediaData", 0);
    	SharedPreferences.Editor editor = dataSetting.edit();
    	//editor.clear();
    	//editor.commit();
     	String dataListStr=dataSetting.getString("data",null);
    	byte[] base64Bytes = Base64.decode(dataListStr,0);
 		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
 		ObjectInputStream ois = new ObjectInputStream(bais);
 		this.mMediaList= (List<MMedia>) ois.readObject();
 		
    	}
    }
    
    boolean saveImg(InputStream is,String fileDir, String fileName)  {  
    	
    	try {
    		Bitmap bm = BitmapFactory.decodeStream(is);
			saveImg(bm,fileDir,fileName);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    boolean saveImg(Bitmap bm,String fileDir, String fileName) throws IOException {  
    	if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {   
    		// sd card 锟斤拷锟斤拷                          
    		 File dirFile = new File(fileDir);  
    		 boolean result=false;
    	        if(!dirFile.exists()){ 
    	        	
    	          result=dirFile.mkdirs()   ;
    	        }   
    	        File myFile = new File(fileDir+ fileName);   
    	        if(myFile.exists())
    	        	myFile.delete();
    	        myFile.createNewFile();
    	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myFile));   
    	        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);   
    	        bos.flush();   
    	        bos.close(); 
    	        return true;
    		}else {   
    		// 锟斤拷前锟斤拷锟斤拷锟斤拷   
    			return false;
    		} 
    }   
    
    public List<MNews> getNews(){
    	List<MNews> newsList=new ArrayList<MNews>();
    	MNews news1=new MNews();
    	news1.mId=1;
    	news1.mTitle="幼儿园教师的不当用语及对策";
    	news1.mDescription="教师口语渗透于幼儿园一日活动的各个环节,良好的教师口语不仅能帮助教师轻松组织各项活动、完成各项教育目标,同时对于营造自由宽松、尊重平等的精神环境也起着重要作用";
    	news1.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000016&itemidx=2&sign=c4e5add74b77fabe7a62f48642a4310d#wechat_redirect";
    	news1.mExt3="星期一";
    	newsList.add(news1);
    	
    	MNews news2=new MNews();
    	news2.mId=2;
    	news2.mTitle="14种严重危害儿童健康的食物";
    	news2.mExt2="1";
    	news2.mTitleImageUrl="http://mmbiz.qpic.cn/mmbiz/JpEjJJ289CyBc435YRMTd3nu13fZCZCI3VYhb78gkPszT9ZNyriaTaCs7x6nW7lqgvRvnrzLJEaW1MnATtBHIOA/0";
    	//news2.mDescription="教师口语渗透于幼儿园一日活动的各个环节,良好的教师口语不仅能帮助教师轻松组织各项活动、完成各项教育目标,同时对于营造自由宽松、尊重平等的精神环境也起着重要作用";
        news2.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000016&itemidx=1&sign=19482e65d3518065ac1ce0a44d2e7b77#wechat_redirect";
        news2.mExt3="星期二";
        newsList.add(news2);
    	
    	MNews news3=new MNews();
    	news3.mId=3;
    	news3.mTitle="创造力从涂鸦开始";
    	news3.mExt2="1";
    	news3.mTitleImageUrl="http://mmsns.qpic.cn/mmsns/JpEjJJ289CyH63I2rHjhrcOFr3KJj2IPOQuPET9hogGElHtv6tBulw/0";
    	//news3.mDescription="教师口语渗透于幼儿园一日活动的各个环节,良好的教师口语不仅能帮助教师轻松组织各项活动、完成各项教育目标,同时对于营造自由宽松、尊重平等的精神环境也起着重要作用";
    	news3.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000024&itemidx=2&sign=12931af5515996bd9ef8dbc6582e2451#wechat_redirect";
    	news3.mExt3="星期二";
    	newsList.add(news3);
    	
    	
    	MNews news4=new MNews();
    	news4.mId=4;
    	news4.mTitle="所有的孩子从幼年开始，用一生去忠诚父母";
    	news4.mDescription="每个孩子，其实都是最懂父母心思的人。所有的孩子，都从幼年开始，细细地读父母这本书，然后用其一生的时间去做他认为对父母最有意义的事";
    	news4.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000024&itemidx=3&sign=d71bef4ca17321affa02e3a3662732d9#wechat_redirect";
    	news4.mExt3="星期一";
    	newsList.add(news4);
    	
    	MNews news5=new MNews();
    	news5.mId=5;
    	news5.mTitle="教育是一种幸福完整的生活";
    	news5.mDescription="致力于教育的领导朱永新:教育是一种幸福完整的生活";
    	news5.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000029&itemidx=2&sign=9479185d0c2ce1d04e9a6aafa27c5670#wechat_redirect";
    	news5.mExt3="星期一";
    	newsList.add(news5);
    	
     	MNews news6=new MNews();
     	news6.mId=6;
     	news6.mExt2="1";
     	news6.mExt3="星期一";
     	news6.mTitle="用心理学来认识自己和他人";
     	news6.mTitleImageUrl="http://mmbiz.qpic.cn/mmbiz/JpEjJJ289Cz78nkibhAPCde6PQ1m92HJ5TaibdvadribXJ4z1RszkJazFoia7RJMcSYThJeVnQ7awKq89QzsKcGOXA/0";
     	//news6.mDescription="致力于教育的领导朱永新:教育是一种幸福完整的生活";
     	news6.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000029&itemidx=1&sign=b29dbe7b3f0b421c4e6f930b016dbf8e#wechat_redirect";
    	newsList.add(news6);
    	
    	MNews news7=new MNews();
    	news7.mId=7;
    	news7.mExt3="星期一";
    	//news7.mExt2="1";
    	news7.mTitle="幼儿教师与家长如何进行心理沟通";
    	//news7.mTitleImageUrl="http://mmbiz.qpic.cn/mmbiz/JpEjJJ289Cz78nkibhAPCde6PQ1m92HJ5TaibdvadribXJ4z1RszkJazFoia7RJMcSYThJeVnQ7awKq89QzsKcGOXA/0";
     	news7.mDescription="幼儿教师加深对幼儿的热爱和关怀，是幼儿教师教育好幼儿的根本，也是搞好与家长心理沟通的根本";
    	news7.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000039&itemidx=2&sign=8401cacc1f8b0b2e05262bba973be100#wechat_redirect";
    	newsList.add(news7);
    	
       	MNews news8=new MNews();
       	news8.mExt3="星期一";
       	news8.mId=7;
       	news8.mExt2="1";
       	news8.mTitle="让孩子说出心理话";
    	news8.mTitleImageUrl="http://mmbiz.qpic.cn/mmbiz/JpEjJJ289CwTzW0bLGyhahgeDhicGFcn7kZ1fo3m8LjVNh7RPlYqzGlwVI9JPpibCEqVicr9Qf5VEpIwFTd9A8MYA/0";
       	//news8.mDescription="幼儿教师加深对幼儿的热爱和关怀，是幼儿教师教育好幼儿的根本，也是搞好与家长心理沟通的根本";
       	news8.mRedirectUrl="http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5OTAwNTk3Nw==&appmsgid=10000039&itemidx=1&sign=a6d3f0f92b18dda547733709bf43b21f#wechat_redirect";
    	newsList.add(news8);
    	return newsList;
    }
    public List<Drawable> getDrawableList(){
    	return this.m_adDrawable;
    }
    public MAd getAd(int index){
    	if(m_adList==null||m_adList.size()<=index)
    		return null;
    	return m_adList.get(index);
    }

}
