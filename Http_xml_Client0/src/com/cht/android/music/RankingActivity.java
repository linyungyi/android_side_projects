package com.cht.android.music;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RankingActivity extends Activity {

    public ListView musiclist;
    private ArrayList<Song> songList;
    // IDs of the context menu items for the list of conversations.
    public static final int CATEGORY_MEN                = 0;
    public static final int CATEGORY_WOMEN              = 1;
    public static final int CATEGORY_GROUP              = 2;
    public static final int CATEGORY_TAIWAN             = 3;
    
    private static final int CON_INDEX_DOWNLOAD  = 0;
    private static final int CON_INDEX_PLAY     = 1;
    private static final int CON_INDEX_SETUP   = 2;
    
    public static final int SEARCHSTATE_MANU            = 0;
    public static final int SEARCHSTATE_LIST            = 1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);       
        initMusicList();
 
    }//End OnCreate

    public void initMusicList(){
    //TODO define actions when user select item in MusicList

        musiclist = (ListView)findViewById(R.id.search_result);  

        songList = new ArrayList<Song>();
        
        Song s = new Song(R.drawable.icon, "心牆", "小宇");
        songList.add(s);        
        s = new Song(R.drawable.icon, "心牆1", "小宇1");
        songList.add(s);        
        s = new Song(R.drawable.icon, "心牆2", "小宇2");
        songList.add(s);
        s = new Song(R.drawable.icon, "心牆1", "小宇1");
        songList.add(s);        
        s = new Song(R.drawable.icon, "心牆2", "小宇2");
        songList.add(s);
        
        SongAdapter songAdapater = new SongAdapter(this, R.layout.song_list_item, songList);
        
        musiclist.setAdapter(songAdapater);
        
        musiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
                startPlayer(pos, id);
            }
        });
        
        /* Add Context-Menu listener to the ListView. */ 
        musiclist.setOnCreateContextMenuListener(new OnCreateContextMenuListener() { 

            public void onCreateContextMenu(ContextMenu menu, View v,
                    ContextMenuInfo menuInfo) {
                menu.add(0, CON_INDEX_DOWNLOAD, 0, R.string.menu_download);
                menu.add(0, CON_INDEX_PLAY, 0, R.string.menu_play);
                menu.add(0, CON_INDEX_SETUP, 0, R.string.menu_set_ringtone);
                
            } 
        });         
    }
    
    public boolean onContextItemSelected(MenuItem aItem) {   
        Intent intent;
      
        /* Switch on the ID of the item, to get what the user selected. */  
        switch (aItem.getItemId()) {   
            case CON_INDEX_DOWNLOAD:  
                // TODO: Invoke browser with corresponding URL.
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://hami.emome.net"));
                startActivity(intent);               
                return true;
            case CON_INDEX_PLAY:
                Song aSong = (Song)musiclist.getAdapter().getItem(0);
                intent = new Intent(this, PlayerActivity.class);

                intent.putExtra("Name", aSong.getName());
                intent.putExtra("Artist", aSong.getArtist());
                
                this.startActivity(intent);
                
                return true;
            case CON_INDEX_SETUP:
                // TODO: Pass the information of current music to the Settings.
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        
        return false;   
    }   
    
    private void startPlayer(int pos, long id)
    {
        Song src = songList.get(pos);

        Intent nIntent = new Intent(this, PlayerActivity.class);
        // TODO: Pass the data source to player
        nIntent.putExtra("Name", src.getName());
        nIntent.putExtra("Artist", src.getArtist());
        
        this.startActivity(nIntent);
    }
}
