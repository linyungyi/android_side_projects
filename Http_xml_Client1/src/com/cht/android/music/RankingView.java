package com.cht.android.music;



import java.util.ArrayList;

import com.cht.android.music.MainActivity.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Song;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class RankingView extends View {

	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;
    public RankingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	
		initialRankingView(context);
	}
    
    private String myTag = "RankingView";
    
    
    public void initialRankingView(Context context) {

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        
        // TODO: mStrings is from database!
   
        RankingActivity.rankingmusiclist.setAdapter(new ArrayAdapter<String>(getContext(),
                 android.R.layout.simple_list_item_1, MainUrl.getRankingCategoryTitle()));
        RankingActivity.rankingmusiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
                String RankingUrl = MainUrl.getRankingCategoryUrl()[pos].replaceAll("pagecount=", "pagecount=1");
                RankingUrl = RankingUrl.replaceAll("pagesize=", "pagesize=10");
            	httpThread = new HttpThread(RankingUrl, new MyHandler(),UPDATE_SETTING_SUCCESS);
        		httpThread.start();
            }
        });           
        RankingActivity.rankingback.setVisibility(android.view.View.INVISIBLE);
}
     
    public void initRankingListView(){
    	
        RankingActivity.rankingback.setVisibility(android.view.View.VISIBLE);

    	RankingActivity.rankingmusiclist.setBackgroundResource(R.drawable.listbg);

        ArrayList<SongData> songList = new ArrayList<SongData>();
        

		ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
		ParserHandler pHandler = pXml.parsingData();
        RankingActivity.songdata = pHandler.getParsedSongsArray();

		SongData s = null;
		
		for (int i = 0; i < RankingActivity.songdata.length; i++) {

			
	        s = new SongData(RankingActivity.songdata[i].getImgArtistUrl(), RankingActivity.songdata[i].getSongName(),  RankingActivity.songdata[i].getSinger());
	        songList.add(s); 
			
		}
		//RankingActivity.setSong(songsArray);
		//RankingActivity.set_isNext_to_Song(true);
        SongAdapter songAdapater = new SongAdapter(getContext(), R.layout.song_list_item, songList);
      //  return songAdapater;
        
        RankingActivity.rankingmusiclist.setAdapter(songAdapater);
        RankingActivity.rankingmusiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
            	RankingActivity.startPlayer(pos);
            }
        });
    }
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
				initRankingListView();
				break;

			}
			super.handleMessage(msg);
		}
	}  
}
