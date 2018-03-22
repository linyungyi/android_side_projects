package com.cht.android.music;


import java.util.ArrayList;



import com.cht.android.music.RecommendAlbumView.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.MusicBox;

import android.app.Activity;
import android.content.Context;
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

public class RecommendMusicBoxView extends View {
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;
    
    public RecommendMusicBoxView(Context context) {
		super(context);
    	httpThread = new HttpThread(MainUrl.getAlbumSongUrl(), new MyHandler(),UPDATE_SETTING_SUCCESS);
		httpThread.start();
		
		// TODO Auto-generated constructor stub
	}
	private ArrayList<SongData> songList;
    // IDs of the context menu items for the list of conversations.
  
	private final String myTag = "RecommendMusicBoxActivity";
    
	private String[] MusicBoxDescription = null;
	private String[] MusicBoxPrice = null;
	private String[] MusicBoxImageUrl = null;
	private String[] MusicBoxListUrl = null;
	private String[] MusicBoxID = null;
	private String[] MusicBoxCPName = null;
	private String[] MusicBoxName = null;

    
    public void initMusicBoxList(Context context){
    	//initial AlbumListUrl[]
    	// get Album List
        songList = new ArrayList<SongData>();
    	if(MainUrl.getMusicBoxUrl()==null)
    		MainUrl.initialRecommendData();
    	if(MainUrl.getMusicBoxID()==null){
     	ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
    	ParserHandler pHandler = pXml.parsingData();
		MusicBox[] musicboxArray = pHandler.getParsedMusicBoxiesArray();
		
		MusicBoxListUrl = new String[musicboxArray.length];
		MusicBoxPrice = new String[musicboxArray.length];
		MusicBoxImageUrl = new String[musicboxArray.length];
		MusicBoxDescription = new String[musicboxArray.length];
		MusicBoxID = new String[musicboxArray.length];
		MusicBoxCPName = new String[musicboxArray.length];
		MusicBoxName = new String[musicboxArray.length];
		// print song information
		
		
		SongData s = null;
		for (int i = 0; i < musicboxArray.length; i++) {

			MusicBoxListUrl[i] = musicboxArray[i].getListUrl();
			MusicBoxPrice[i] = musicboxArray[i].getPrice();
			MusicBoxImageUrl[i] = musicboxArray[i].getBigIcon();
			MusicBoxDescription[i] = musicboxArray[i].getContent();
			MusicBoxID[i] = musicboxArray[i].getMusicId();
			MusicBoxCPName[i] = musicboxArray[i].getCpName();
			MusicBoxName[i] = musicboxArray[i].getMusicName();
		}
		MainUrl.setMusicBoxDescription(MusicBoxDescription);
		MainUrl.setMusicBoxID(MusicBoxID);
		MainUrl.setMusicBoxImageUrl(MusicBoxImageUrl);
		MainUrl.setMusicBoxListUrl(MusicBoxListUrl);
		MainUrl.setMusicBoxPrice(MusicBoxPrice);
		MainUrl.setMusicBoxCPName(MusicBoxCPName);
		MainUrl.setMusicBoxName(MusicBoxName);
    	}
		SongData s = null;
		for (int i = 0; i < MainUrl.getMusicBoxID().length; i++) {
	        s = new SongData(MainUrl.getMusicBoxImageUrl()[i], MainUrl.getMusicBoxName()[i], MainUrl.getMusicBoxCPName()[i]);
	        songList.add(s); 
	     
		}
		
        SongAdapter songAdapater = new SongAdapter(context, R.layout.song_list_item, songList);
        RecommendActivity.musiclist.setAdapter(songAdapater);
        RecommendActivity.musiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
            
            	        Intent aIntent = new Intent(getContext(), MusicBoxListActivity.class);
            	        try{
            	            aIntent.putExtra("MusicBoxListUrl", MainUrl.getMusicBoxListUrl()[pos]);
            	            aIntent.putExtra("MusicBoxPrice", MainUrl.getMusicBoxPrice()[pos]);
            	            aIntent.putExtra("MusicBoxImageUrl", MainUrl.getMusicBoxImageUrl()[pos]);
            	            aIntent.putExtra("MusicBoxDescription", MainUrl.getMusicBoxDescription()[pos]);
            	            aIntent.putExtra("MusicBoxID", MainUrl.getMusicBoxID()[pos]);
            	        }catch(Exception e){
            	        	Log.e(myTag, "ShowMusicBoxList err:"+e.toString(), e);
            	        }
            	        getContext().startActivity(aIntent);   
            }
        });    
       
  	
    }
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
				initMusicBoxList(getContext());
				break;

			}
			super.handleMessage(msg);
		}
	}

}
