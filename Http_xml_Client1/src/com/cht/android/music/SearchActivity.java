package com.cht.android.music;

import java.util.ArrayList;

import com.music.parser.data.Song;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
   
	public static Button search_button;
	public static Button backbutton;

    public static EditText search_field;
    public static ListView searchlist;
    public static Song[] songdata = null;
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
    
    private static boolean isSongList = false;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        searchlist = (ListView)findViewById(R.id.SearchContentListView);
        backbutton = (Button)findViewById(R.id.SearchBack);
		search_button = (Button)findViewById(R.id.SearchButton);
	    search_field = (EditText)findViewById(R.id.SearchTxt);
	    
	    SearchView sv = new SearchView(searchlist.getContext());
	    
    }//End OnCreate
    
    public static void setSearchBackVisibility(int visibility){
    	backbutton.setVisibility(visibility);
    }

    public static void set_isNext_to_Song(boolean next){
    	isSongList = next;
    }
    
    public static void setSong(Song[] data){
    	songdata = data;
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
    	
        Intent nIntent = new Intent(searchlist.getContext(), PlayerActivity.class);
        // TODO: Pass the data source to player
        nIntent.putExtra("Name", songdata[pos].getSongName());
        nIntent.putExtra("Artist", songdata[pos].getSinger());
        nIntent.putExtra("AlbumUrl", songdata[pos].getImgAlbumUrl());
        nIntent.putExtra("SongID", songdata[pos].getProductid());
        nIntent.putExtra("WavUrl", songdata[pos].getWavUrl());
        searchlist.getContext().startActivity(nIntent);
    }
}
