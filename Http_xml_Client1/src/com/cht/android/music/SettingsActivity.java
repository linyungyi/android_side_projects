package com.cht.android.music;

import java.util.ArrayList;

import com.music.parser.data.Song;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends Activity {

    public static ListView musiclist;
    private ArrayList<SongData> songList;
    // IDs of the context menu items for the list of conversations.
    
    private static final int CON_INDEX_RECOMMEND  = R.id.recommend;
    private static final int CON_INDEX_RANKING     = R.id.ranking;
    private static final int CON_INDEX_ACTIVITY   = R.id.showactivity;
    private static final int CON_INDEX_SEARCH   = R.id.keyseach;
    private static final int CON_INDEX_SETTING   = R.id.setting;
    private static final int CON_INDEX_MYLIST   = 5;	
	
    public static Song[] songdata = null;
    
    
    private Button setting_basic = null;
    private Button setting_day = null;
    private Button setting_night = null;
    private Button setting_weekend = null;

    
    private int nowTag = 0;
  //  public int ListNum = 0;
	private final String myTag = "SettingActivity";    
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
		
        //TODO: Check if user is reading or modifying the settings.
        setContentView(R.layout.settinglayout);     
        musiclist = (ListView)findViewById(R.id.SettingContentListView);
        nowTag = R.id.TabSettingBasic;
        if((MainUrl.getBasicRing()==null && MainUrl.getDayRing()==null && MainUrl.getNightRing()==null && MainUrl.getWeekendRing()==null) || !MainUrl.getUserVaild()){
			MainUrl.setUserProfile();
        }else{
        	// if Url Should set pagesize and pagecount,it should code something
        }
        
        setImageTag();
        setting_basic.setBackgroundResource(R.drawable.bluebutton);
        ShowListView();
    }  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onPrepareOptionsMenu(menu);
       
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
      //  menu.add(0, CON_INDEX_ALBUM, 0, R.string.recommend_album);

    	return true;
    }     
    
    private void setImageTag(){
    	int screenwidth = MainUrl.getScreenWidth();
    	int tagwidth = screenwidth/5;
    //	Log.i(myTag, "tagwidth="+tagwidth);
    	
    	setting_basic = (Button)findViewById(R.id.TabSettingBasic);
    	setting_day = (Button)findViewById(R.id.TabSettingDay);
    	setting_night = (Button)findViewById(R.id.TabSettingNight);
    	setting_weekend = (Button)findViewById(R.id.TabSettingWeekend);

    	setting_basic.setOnClickListener(new View.OnClickListener() {
			
    		public void onClick(View v) {
				// Log.i(myTag,"settingTag:"+v.getId());
				// TODO Auto-generated method stub
    			if(nowTag!=v.getId())
				  changeTagStatus(v.getId());
			}
		});
    	
    	//recommend_album.setMaxWidth(tagwidth);
    	setting_day.setOnClickListener(new View.OnClickListener() {
			
    		public void onClick(View v) {
				// Log.i(myTag,"settingTag:"+v.getId());
				// TODO Auto-generated method stub
    			if(nowTag!=v.getId())
				  changeTagStatus(v.getId());
			}
		});
    	
    	setting_night.setOnClickListener(new View.OnClickListener() {
			
    		public void onClick(View v) {
			//	 Log.i(myTag,"settingTag:"+v.getId());
				// TODO Auto-generated method stub
    			if(nowTag!=v.getId())
				  changeTagStatus(v.getId());
			}
		});
    
    	setting_weekend.setOnClickListener(new View.OnClickListener() {
			
    		public void onClick(View v) {
			//	 Log.i(myTag,"settingTag:"+v.getId());
				// TODO Auto-generated method stub
    			if(nowTag!=v.getId())
				  changeTagStatus(v.getId());
			}
		});
    }

     private void changeTagStatus(int tagid){
     	
     	if(nowTag != tagid){
     	  setPreviousTag();
     	  nowTag = tagid;
     	  if(tagid==R.id.TabSettingBasic){
     		
     		 setting_basic.setBackgroundResource(R.drawable.bluebutton);
     		 
     		 ShowListView();
     	  }else if(tagid==R.id.TabSettingDay){
     		  
     		 setting_day.setBackgroundResource(R.drawable.bluebutton);
     		 ShowListView();
     		
     	  }else if(tagid==R.id.TabSettingNight){
     		  
     		 setting_night.setBackgroundResource(R.drawable.bluebutton);

     		 ShowListView();
     	  }else if(tagid==R.id.TabSettingWeekend){
     		  
      		 setting_weekend.setBackgroundResource(R.drawable.bluebutton);

      		 ShowListView();
          }
     	}
     }     
     
     private void setPreviousTag(){
     	if(nowTag==R.id.TabSettingBasic){
     		setting_basic.setBackgroundResource(R.drawable.graybutton);
     	}else if(nowTag==R.id.TabSettingDay){
        	setting_day.setBackgroundResource(R.drawable.graybutton);
     	}else if(nowTag==R.id.TabSettingNight){
    		setting_night.setBackgroundResource(R.drawable.graybutton);
     	}else if(nowTag==R.id.TabSettingWeekend){
    		setting_weekend.setBackgroundResource(R.drawable.graybutton);
     	}
     }
     
     private void ShowListView(){
         //TODO define actions when user select item in MusicList

 		    //  initial View
 	    	if(nowTag==R.id.TabSettingBasic){
 	    		 // Log.i(myTag,"basic");
 	    		  new SettingView(musiclist.getContext(),"basic");
 	    		  songdata = MainUrl.getBasicRing();
 		    	 
 	        }else if(nowTag==R.id.TabSettingDay){
	    		  new SettingView(musiclist.getContext(),"day");
 	    		  songdata = MainUrl.getDayRing();

 	    	      
 	    	}else if(nowTag==R.id.TabSettingNight){
	    		  new SettingView(musiclist.getContext(),"night");
 	    		  songdata = MainUrl.getNightRing();

 	    	}else if(nowTag==R.id.TabSettingWeekend){
	    		  new SettingView(musiclist.getContext(),"weekend");
 	    		  songdata = MainUrl.getWeekendRing();


	 	    }
 	    	
     }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{  
        Intent intent;
        Log.i(myTag, "item.getItemId()="+item.getItemId());
      //  mLinearLayout.removeAllViews();
      //  LayoutInflater.from(MyActivity.this).inflate(R.layout.main2,mLinearLayout,true);
        /* Switch on the ID of the item, to get what the user selected. */  
        switch (item.getItemId()) {   
        
            case CON_INDEX_RECOMMEND:
                
                intent = new Intent(this,RecommendActivity.class);
                this.startActivity(intent);               
                return true;
            case CON_INDEX_RANKING:  	
                intent = new Intent(this,RankingActivity.class);
                this.startActivity(intent);               
                return true;
            case CON_INDEX_ACTIVITY:  
                // TODO: Invoke browser with corresponding URL.
                intent = new Intent(this,Activities.class);
                this.startActivity(intent); 
                startActivity(intent);               
                return true;
            case CON_INDEX_SEARCH:
                intent = new Intent(this, SearchActivity.class);
                this.startActivity(intent);
                
                return true;
            case CON_INDEX_SETTING:
                // TODO: Pass the information of current music to the Settings.
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        
        return false;   
    }   
    
    public static void startPlayer(int pos)
    {
    	
        Intent nIntent = new Intent(musiclist.getContext(), PlayerActivity.class);
        // TODO: Pass the data source to player
        nIntent.putExtra("Name", songdata[pos].getSongName());
        nIntent.putExtra("Artist", songdata[pos].getSinger());
        nIntent.putExtra("AlbumUrl", songdata[pos].getImgAlbumUrl());
        nIntent.putExtra("SongID", songdata[pos].getProductid());
        nIntent.putExtra("WavUrl", songdata[pos].getWavUrl());
        musiclist.getContext().startActivity(nIntent);
    }

}
