package com.youer.activity;

import com.example.youer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


public class NormalWebViewActivity extends Activity implements OnClickListener {
	WebView webview=null;
	int m_type=0;


	@SuppressLint({ "JavascriptInterface", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.normal_webview);
	
	 webview=(WebView) findViewById(R.id.webview); 
	 webview.setWebViewClient(new NewsWebViewClient());
	 webview.getSettings().setJavaScriptEnabled(true);
	 webview.getSettings().setAllowFileAccess(true);
	 webview.getSettings().setPluginState(PluginState.ON);
	 webview.setBackgroundColor(Color.rgb(233, 233, 233));
	 webview.getSettings().setBlockNetworkImage(true);
		
	 webview.addJavascriptInterface(new JavascriptInterface(this), "imagelistner"); 
		webview.setWebViewClient(new NewsWebViewClient());
		 Intent intent = getIntent();
         String url  = intent.getStringExtra("url");
         m_type=intent.getIntExtra("type", 0);
         webview.loadUrl(url);
     	
	}
	 // js閫氫俊鎺ュ彛  

    public class JavascriptInterface {  
  
        private Context context;  
    
        public JavascriptInterface(Context context) {  
            this.context = context;  
        }  
    
        public void openImage(String img) {  
    
        }  
    }  
  
	 // 娉ㄥ叆js鍑芥暟鐩戝惉  
    private void addImageClickListner() {  
        // 杩欐js鍑芥暟鐨勫姛鑳藉氨鏄紝閬嶅巻鎵�湁鐨刬mg鍑犵偣锛屽苟娣诲姞onclick鍑芥暟锛屽嚱鏁扮殑鍔熻兘鏄湪鍥剧墖鐐瑰嚮鐨勬椂鍊欒皟鐢ㄦ湰鍦癹ava鎺ュ彛骞朵紶閫抲rl杩囧幓  
    	webview.loadUrl("javascript:(function(){" +  
        "var objs = document.getElementsByTagName(\"img\"); " +   
                "for(var i=0;i<objs.length;i++)  " +   
        "{"  
                + "    objs[i].onclick=function()  " +   
        "    {  "   
                + "        window.imagelistner.openImage(this.src);  " +   
        "    }  " +   
        "}" +   
        "})()");  
    }  
	
	protected void onDestroy() {
	      super.onDestroy();
	      if(webview!=null){
	    	  webview.removeAllViews();
	    	  webview.destroy();
	      }
	}
	 class NewsWebViewClient extends WebViewClient{ 


	    @Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
	    	view.getSettings().setBlockNetworkImage(false);
	    	  addImageClickListner();  
			super.onPageFinished(view, url);
			
		}

		@Override 

	    public boolean shouldOverrideUrlLoading(WebView view, String url) { 
   

	    	 boolean result= super.shouldOverrideUrlLoading(view, url);
            
             return result;

	   }
	
}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}