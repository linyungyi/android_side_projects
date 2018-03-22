package com.cht.android.music;


import java.util.ArrayList;



import com.cht.android.music.RankingView.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Album;


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

public class RecommendAlbumActivity extends Activity {

    public ListView musiclist;
    private ArrayList<SongData> songList;
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;
    // IDs of the context menu items for the list of conversations.
    
    private static final int CON_INDEX_DOWNLOAD  = 6;
    private static final int CON_INDEX_PLAY     = 5;
    private static final int CON_INDEX_SETUP   = 4;
    private static final int CON_INDEX_SINGLE   = 0;
    private static final int CON_INDEX_ALBUM   = 1;
    private static final int CON_INDEX_MUSICBOX   = 2;

    
    public static final int SEARCHSTATE_MANU            = 0;
    public static final int SEARCHSTATE_LIST            = 1;
	private final String myTag = "RecommendAlbumActivity";
    
	private String[] AlbumListUrl = null;
	private String[] AlbumName = null;
	private String[] AlbumArtist = null;
	private String[] AlbumPublisher = null;
	private String[] AlbumIssueDate = null;
	private String[] AlbumIconUrl = null;

	
	//private TabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
        
        
        setContentView(R.layout.search_result);       
       
        if(MainUrl.getSingleSongUrl()==null || MainUrl.getAlbumSongUrl()==null || MainUrl.getMusicBoxUrl()==null){
        	MainUrl.initialRecommendData();
        }else{
        	// if Url Should set pagesize and pagecount,it should code something
        }
    	httpThread = new HttpThread(MainUrl.getAlbumSongUrl(), new MyHandler(),UPDATE_SETTING_SUCCESS);
		httpThread.start();
 
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
    
    
    public void initAlbumList(){
    	//initial AlbumListUrl[]
    	// get Album List
        musiclist = (ListView)findViewById(R.id.search_result);  
        songList = new ArrayList<SongData>();
		ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
		ParserHandler pHandler = pXml.parsingData();
        
        Album[] albumsArray = pHandler.getParsedAlbumsArray();
        
        AlbumListUrl = new String[albumsArray.length];
        AlbumName = new String[albumsArray.length];
        AlbumArtist = new String[albumsArray.length];
        AlbumPublisher = new String[albumsArray.length];
        AlbumIssueDate = new String[albumsArray.length];
        AlbumIconUrl = new String[albumsArray.length];

		// print song information
		SongData s = null;
		for (int i = 0; i < albumsArray.length; i++) {

			AlbumListUrl[i] = albumsArray[i].getListUrl();
			AlbumName[i] = albumsArray[i].getName();
			AlbumArtist[i] = albumsArray[i].getArtist();
			AlbumPublisher[i] = albumsArray[i].getPublisher();
			AlbumIssueDate[i] = albumsArray[i].getDate();
			AlbumIconUrl[i] = albumsArray[i].getIconUrl();

	        s = new SongData(albumsArray[i].getImgArtistUrl(), albumsArray[i].getName(), albumsArray[i].getArtist());
	        songList.add(s); 
	       
		}
		
        SongAdapter songAdapater = new SongAdapter(this, R.layout.song_list_item, songList);
        
        musiclist.setAdapter(songAdapater);
        
        musiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
            	ShowAlbumList(pos);
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
          
            case CON_INDEX_MUSICBOX:  	
            	
                intent = new Intent(this,RecommendMusicBoxActivity.class);
                this.startActivity(intent);               
                return true;
                
        }
        
        return false;   
    }   
    private void ShowAlbumList(int pos){
    	
        Intent aIntent = new Intent(this, AlbumSongListActivity.class);
        try{
         
            aIntent.putExtra("AlbumName", AlbumName[pos]);
            aIntent.putExtra("AlbumArtist", AlbumArtist[pos]);
            aIntent.putExtra("AlbumPublisher", AlbumPublisher[pos]);
            aIntent.putExtra("AlbumIssueDate", AlbumIssueDate[pos]);
            aIntent.putExtra("AlbumListUrl", AlbumListUrl[pos]);
            aIntent.putExtra("AlbumIconUrl", AlbumIconUrl[pos]);

        //    Log.i(myTag,"ShowAlbumListUrl:"+AlbumListUrl[pos]);
        }catch(Exception e){
        	Log.e(myTag, "ShowAlbumList err:"+e.toString(), e);
        }
        startActivity(aIntent);   
    }
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
				initAlbumList();
				break;

			}
			super.handleMessage(msg);
		}
	}      
    
}
