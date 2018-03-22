package com.cht.android.music;


import java.util.HashMap;

import com.cht.android.music.HttpThread;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Song;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;




public class MainActivity extends Activity {
	
	private static final String gSoYAUrl = "http://musicphone.emome.net/MAS/DoTask?Task=gSoYA&xsl=pcIni.xsl";
    private static final String myTag = "MainActivity";
    private static final int EVENT_LOAD_DONE = 1;
    private static final int EVENT_LOAD_CONT = 2;
    private int mCount =0;
    private int i;
    
    private ProgressBar progressHorizontal;
    
    public static Song[] songdata = null;
    
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request the progress bar to be shown in the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress);
        setProgressBarVisibility(true);
        // get Monitor size
        
        getDisplayMetrics(this.getApplicationContext());
        
        // progress bar
        progressHorizontal = (ProgressBar) findViewById(R.id.progress);
        setProgress(progressHorizontal.getProgress() * 100);
		httpThread = new HttpThread(gSoYAUrl, new MyHandler(),UPDATE_SETTING_SUCCESS);
		httpThread.start();
        new Thread(new Runnable()
        {
            public void run()
            {
                for(i=0; i<10;i++)
                {
                    mCount=(i+1)*10;
                try
                {
                Thread.sleep(300);
                }catch(Exception e)
                {
                    Log.d(myTag, "Exception");
                }
                
                if(i==9)
                {
                	mCount-=5;
                	/*
                    Message m = new Message();
                    mCount-=5;
                    m.what = MainActivity.EVENT_LOAD_DONE;
                    MainActivity.this.mMessageHandler.sendMessage(m);
                    */
                }else
                {
                	if(!Thread.currentThread().isInterrupted())
                    {
                        progressHorizontal.setProgress(mCount);
                    
                    }
                	/*
                    Message m = new Message();
                    m.what = MainActivity.EVENT_LOAD_CONT;
                    MainActivity.this.mMessageHandler.sendMessage(m);
                    */
                }
                }
            }
        }).start();
    }
    
   /*
    Handler mMessageHandler = new Handler()
    {
        public void handleMessage(Message msg) 
        {
            switch (msg.what)
            {
                case EVENT_LOAD_DONE:
                    Thread.currentThread().isInterrupted();
                    loadDone();
                    break;
                case EVENT_LOAD_CONT:
                    if(!Thread.currentThread().isInterrupted())
                    {
                        progressHorizontal.setProgress(mCount);
                    
                    }
                    break;
            }
        }
    };
    */
    
    public void loadDone()
    {
      //  Intent i = new Intent(this, MusicClientActivity.class);
    	setTagUrl();
    	MainUrl.initialSearchUrl();
    	MainUrl.initialActivities();
    	MainUrl.initRankingList();
    	MainUrl.initialRecommendData();
    	Intent i = new Intent(this, RecommendActivity.class);
        startActivity(i);
        finish();
    }
    
    private void setTagUrl(){
    	
		ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
		ParserHandler pHandler = pXml.parsingData();
		HashMap<String, String> aHashMap = pHandler.getParsedHashMap();
		String[] keysArray = pHandler.getKeysSequence();
		
		
		for (int i = 0; i < keysArray.length; i++) {
			String key = keysArray[i];
			
            if(key.compareTo("推薦")==0){
            	MainUrl.setRecommandUrl(aHashMap.get(key));
            	MainUrl.setRecommandTitle(key);
            }else if(key.compareTo("排行")==0){
            	MainUrl.setRankingUrl(aHashMap.get(key));
            	MainUrl.setRankingTitle(key);
            }else if(key.compareTo("活動")==0){
            	MainUrl.setActivityUrl(aHashMap.get(key));
            	MainUrl.setActivityTitle(key);
            }else if(key.compareTo("搜尋")==0){
            	MainUrl.setSearchUrl(aHashMap.get(key));
            	MainUrl.setSearchTitle(key);
            }else if(key.compareTo("設定")==0){
            	MainUrl.setSettingUrl(aHashMap.get(key));
            	MainUrl.setSettingTitle(key);
            }
    
		}
    	
    }
    
    public static void getDisplayMetrics(Context cx) {
    	
    	String str = "";
    	DisplayMetrics dm = new DisplayMetrics();
    	dm = cx.getApplicationContext().getResources().getDisplayMetrics();
    	int screenWidth = dm.widthPixels;
    	int screenHeight = dm.heightPixels;
    	float density = dm.density;
    	float xdpi = dm.xdpi;
    	float ydpi = dm.ydpi;
    	str += "The absolute width:" + String.valueOf(screenWidth) + "pixels\n";
    	str += "The absolute heightin:" + String.valueOf(screenHeight)
    	    + "pixels\n";
    	str += "The logical density of the display.:" + String.valueOf(density)
    	    + "\n";
    	str += "X dimension :" + String.valueOf(xdpi) + "pixels per inch\n";
    	str += "Y dimension :" + String.valueOf(ydpi) + "pixels per inch\n";
    	
    	MainUrl.setScreenHeight(screenHeight);
    	MainUrl.setScreenWidth(screenWidth);
    	
    	
    //	Log.i(myTag,str);
    //  Log.i(myTag,"screenWidth="+MainUrl.getScreenWidth()+"  screenWidth="+MainUrl.getScreenHeight());
    }    
    
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
				loadDone();
				break;

			}
			super.handleMessage(msg);
		}
	}    
    
    
}
