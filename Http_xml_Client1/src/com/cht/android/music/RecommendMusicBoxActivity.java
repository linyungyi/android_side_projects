package com.cht.android.music;


import java.util.ArrayList;



import com.cht.android.music.RecommendAlbumView.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.MusicBox;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RecommendMusicBoxActivity extends Activity {

    public ListView musiclist;
    private ArrayList<SongData> songList;
    // IDs of the context menu items for the list of conversations.
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;
    private static final int CON_INDEX_DOWNLOAD  = 6;
    private static final int CON_INDEX_PLAY     = 5;
    private static final int CON_INDEX_SETUP   = 4;
    private static final int CON_INDEX_SINGLE   = 0;
    private static final int CON_INDEX_ALBUM   = 1;
    private static final int CON_INDEX_MUSICBOX   = 2;

    
    public static final int SEARCHSTATE_MANU            = 0;
    public static final int SEARCHSTATE_LIST            = 1;
	private final String myTag = "RecommendMusicBoxActivity";
    
	private String[] MusicBoxDescription = null;
	private String[] MusicBoxPrice = null;
	private String[] MusicBoxImageUrl = null;
	private String[] MusicBoxListUrl = null;
	private String[] MusicBoxID = null;

	
	
 
	//private TabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
        
        
        setContentView(R.layout.search_result);       
       
        if(MainUrl.getSingleSongUrl()==null || MainUrl.getAlbumSongUrl()==null || MainUrl.getMusicBoxUrl()==null){
        	MainUrl.initialRecommendData();
        	httpThread = new HttpThread(MainUrl.getMusicBoxUrl(), new MyHandler(),UPDATE_SETTING_SUCCESS);
    		httpThread.start();
        }else{
        	// if Url Should set pagesize and pagecount,it should code something
        	initMusicBoxList();
        }
    	
        
 
    }//End OnCreate
    /*
    private void ReloadMusicList(int count){
    	pagecount = count;
    	
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onPrepareOptionsMenu(menu);
        /*
    	if(NowIndex==0){
        	//  single
                menu.add(0, CON_INDEX_ALBUM, 0, R.string.recommend_album);
                menu.add(0, CON_INDEX_MUSICBOX, 0, R.string.recommend_musicbox);
        }else if(NowIndex==1){
        	//  album
                menu.add(0, CON_INDEX_SINGLE, 0, R.string.recommend_singlesong);
                menu.add(0, CON_INDEX_MUSICBOX, 0, R.string.recommend_musicbox);
        }else if(NowIndex==2){
        	//  musicbox
                menu.add(0, CON_INDEX_SINGLE, 0, R.string.recommend_singlesong);
                menu.add(0, CON_INDEX_ALBUM, 0, R.string.recommend_album);
        }
        */
        menu.add(0, CON_INDEX_SINGLE, 0, R.string.recommend_singlesong);
        menu.add(0, CON_INDEX_MUSICBOX, 0, R.string.recommend_musicbox);
    	return true;
     }
    
    
    public void initMusicBoxList(){
    	//initial AlbumListUrl[]
    	// get Album List
        musiclist = (ListView)findViewById(R.id.search_result);  
        songList = new ArrayList<SongData>();
 	    ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
	    ParserHandler pHandler = pXml.parsingData();
		MusicBox[] musicboxArray = pHandler.getParsedMusicBoxiesArray();
		
		MusicBoxListUrl = new String[musicboxArray.length];
		MusicBoxPrice = new String[musicboxArray.length];
		MusicBoxImageUrl = new String[musicboxArray.length];
		MusicBoxDescription = new String[musicboxArray.length];
		MusicBoxID = new String[musicboxArray.length];

		// print song information
		
		
		SongData s = null;
		for (int i = 0; i < musicboxArray.length; i++) {

			MusicBoxListUrl[i] = musicboxArray[i].getListUrl();
			MusicBoxPrice[i] = musicboxArray[i].getPrice();
			MusicBoxImageUrl[i] = musicboxArray[i].getBigIcon();
			MusicBoxDescription[i] = musicboxArray[i].getContent();
			MusicBoxID[i] = musicboxArray[i].getMusicId();

	        s = new SongData(musicboxArray[i].getSmallIconUrl(), musicboxArray[i].getMusicName(), musicboxArray[i].getCpName());
	        songList.add(s); 
	     
		}
		
        SongAdapter songAdapater = new SongAdapter(this, R.layout.song_list_item, songList);
        
        musiclist.setAdapter(songAdapater);
       
        
        musiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
            	
            	ShowMusicBoxList(pos);
            }
        });		
        
    }
  
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{  
        Intent intent;
     //   Log.i(myTag, "item:"+item.getItemId());
        /* Switch on the ID of the item, to get what the user selected. */  
        switch (item.getItemId()) {   
            case CON_INDEX_SINGLE:
            	
                intent = new Intent(this,RecommendActivity.class);
                this.startActivity(intent);               
                return true;
          
            case CON_INDEX_ALBUM:  	
            	
                intent = new Intent(this,RecommendAlbumActivity.class);
                this.startActivity(intent);               
                return true;
        }
        
        return false;   
    }   
    private void ShowMusicBoxList(int pos){
    	
        Intent aIntent = new Intent(this, MusicBoxListActivity.class);
        try{
            aIntent.putExtra("MusicBoxListUrl", MusicBoxListUrl[pos]);
            aIntent.putExtra("MusicBoxPrice", MusicBoxPrice[pos]);
            aIntent.putExtra("MusicBoxImageUrl", MusicBoxImageUrl[pos]);
            aIntent.putExtra("MusicBoxDescription", MusicBoxDescription[pos]);
            aIntent.putExtra("MusicBoxID", MusicBoxID[pos]);
        }catch(Exception e){
        	Log.e(myTag, "ShowMusicBoxList err:"+e.toString(), e);
        }
        startActivity(aIntent);   
    }
    
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
				initMusicBoxList();
				break;

			}
			super.handleMessage(msg);
		}
	}     
}
