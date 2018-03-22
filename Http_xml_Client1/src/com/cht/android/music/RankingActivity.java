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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RankingActivity extends Activity {

	public static ListView rankingmusiclist;
	public static Button rankingback;
    private ArrayList<SongData> songList;
    // IDs of the context menu items for the list of conversations.
    public static final int CATEGORY_MEN                = 0;
    public static final int CATEGORY_WOMEN              = 1;
    public static final int CATEGORY_GROUP              = 2;
    public static final int CATEGORY_TAIWAN             = 3;
    
    private static final int CON_INDEX_RECOMMEND  = R.id.recommend;
    private static final int CON_INDEX_RANKING     = R.id.ranking;
    private static final int CON_INDEX_ACTIVITY   = R.id.showactivity;
    private static final int CON_INDEX_SEARCH   = R.id.keyseach;
    private static final int CON_INDEX_SETTING   = R.id.setting;
    private static final int CON_INDEX_MYLIST   = 5;
    
    public static Song[] songdata = null;
    public static final int SEARCHSTATE_MANU            = 0;
    public static final int SEARCHSTATE_LIST            = 1;
	private final String myTag = "RankingActivity";
	
	
    private String[] SongID = null;
    private String[] AlbumUrl = null;
    private String[] WavUrl = null;
    
    private RankingView rv = null;
	//private TabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankinglayout);
        
        
        rankingback = (Button)findViewById(R.id.RankingBack);
        rankingback.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				rv.initialRankingView(rankingmusiclist.getContext());
			}
        	
        });
        rankingmusiclist = (ListView)findViewById(R.id.RankingContentListView);
        rankingmusiclist.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
      //  Log.i(MY_DEBUG_TAG, "RankingUrl="+RankingUrl);
        ShowListView();
 
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
       
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
      //  menu.add(0, CON_INDEX_ALBUM, 0, R.string.recommend_album);

    	return true;
    } 
    
    private void ShowListView(){
        //TODO define actions when user select item in MusicList

		    //  initial View
	     		
	     rv = new RankingView(rankingmusiclist.getContext());
    }    		  
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{  
        Intent intent;
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
    	
        Intent nIntent = new Intent(rankingmusiclist.getContext(), PlayerActivity.class);
        // TODO: Pass the data source to player
        nIntent.putExtra("Name", songdata[pos].getSongName());
        nIntent.putExtra("Artist", songdata[pos].getSinger());
        nIntent.putExtra("AlbumUrl", songdata[pos].getImgAlbumUrl());
        nIntent.putExtra("SongID", songdata[pos].getProductid());
        nIntent.putExtra("WavUrl", songdata[pos].getWavUrl());
        rankingmusiclist.getContext().startActivity(nIntent);
    }
    


}
