package com.cht.android.music;


import java.util.ArrayList;


import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Song;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RecommendActivity extends Activity {

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
    
    
    private Button recommend_single = null;
    private Button recommend_album = null;
    private Button recommend_musicbox = null;
    
    private int nowTag = 0;
  //  public int ListNum = 0;
	private final String myTag = "RecommendActivity";
    
	//private TabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
        
        
        setContentView(R.layout.recommendlayout);     
        musiclist = (ListView)findViewById(R.id.RecommendContentListView);
        nowTag = R.id.TagRecommendSingle;
        if(MainUrl.getSingleSongUrl()==null || MainUrl.getAlbumSongUrl()==null || MainUrl.getMusicBoxUrl()==null){
            MainUrl.initialRecommendData();
        }else{
        	// if Url Should set pagesize and pagecount,it should code something
        }
        
        setImageTag();
        recommend_single.setBackgroundResource(R.drawable.bluebutton);
        ShowListView();
    }//End OnCreate
  
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
    	
    	recommend_single = (Button)findViewById(R.id.TagRecommendSingle);
    	recommend_album = (Button)findViewById(R.id.TagRecommendAlbum);
    	recommend_musicbox = (Button)findViewById(R.id.TagRecommendMusicBox);
        
    	recommend_single.setOnClickListener(new View.OnClickListener() {
			
    		public void onClick(View v) {
				// Log.i(myTag,"settingTag:"+v.getId());
				// TODO Auto-generated method stub
    			if(nowTag!=v.getId())
				  changeTagStatus(v.getId());
			}
		});
    	
    	//recommend_album.setMaxWidth(tagwidth);
    	recommend_album.setOnClickListener(new View.OnClickListener() {
			
    		public void onClick(View v) {
				// Log.i(myTag,"settingTag:"+v.getId());
				// TODO Auto-generated method stub
    			if(nowTag!=v.getId())
				  changeTagStatus(v.getId());
			}
		});
    	
    	recommend_musicbox.setOnClickListener(new View.OnClickListener() {
			
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
     	  if(tagid==R.id.TagRecommendSingle){
     		
     		 recommend_single.setBackgroundResource(R.drawable.bluebutton);
     		 
     		 ShowListView();
     	  }else if(tagid==R.id.TagRecommendAlbum){
     		  
     		 recommend_album.setBackgroundResource(R.drawable.bluebutton);
     		 ShowListView();
     		
     	  }else if(tagid==R.id.TagRecommendMusicBox){
     		  
     		 recommend_musicbox.setBackgroundResource(R.drawable.bluebutton);

     		 ShowListView();
           }
     	}
     }     
     
     private void setPreviousTag(){
     	if(nowTag==R.id.TagRecommendSingle){
     		recommend_single.setBackgroundResource(R.drawable.graybutton);
     	}else if(nowTag==R.id.TagRecommendAlbum){
        	recommend_album.setBackgroundResource(R.drawable.graybutton);
     	}else if(nowTag==R.id.TagRecommendMusicBox){
    		recommend_musicbox.setBackgroundResource(R.drawable.graybutton);
     	}
     }
     
     private void ShowListView(){
         //TODO define actions when user select item in MusicList

 		    //  initial View
 	    	if(nowTag==R.id.TagRecommendSingle){
 	    		
 	    		  new RecommendView(musiclist.getContext());
 		    	 
 	        }else if(nowTag==R.id.TagRecommendAlbum){
 	    	      new RecommendAlbumView(this);
 	    	//      ((ListView)musiclist).setAdapter(recv.initAlbumList(musiclist.getContext()));
 	    	}else if(nowTag==R.id.TagRecommendMusicBox){
 	    		  new RecommendMusicBoxView(this);
 	    		//  ((ListView)musiclist).setAdapter(recv.initMusicBoxList(musiclist.getContext()));
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
