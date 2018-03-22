package com.cht.android.music;


import java.util.ArrayList;



import com.cht.android.music.RecommendAlbumActivity.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Album;


import android.content.Context;
import android.content.Intent;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class RecommendAlbumView extends View {

    private ArrayList<SongData> songList;
    // IDs of the context menu items for the list of conversations.

	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;
    public static final int SEARCHSTATE_MANU            = 0;
    public static final int SEARCHSTATE_LIST            = 1;
	private final String myTag = "RecommendAlbumView";
    
	private String[] AlbumListUrl = null;
	private String[] AlbumName = null;
	private String[] AlbumArtist = null;
	private String[] AlbumPublisher = null;
	private String[] AlbumIssueDate = null;
	private String[] AlbumIconUrl = null;
   
	
    public RecommendAlbumView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
    	httpThread = new HttpThread(MainUrl.getAlbumSongUrl(), new MyHandler(),UPDATE_SETTING_SUCCESS);
		httpThread.start();
		//initAlbumList(context);
		
	}
    
    //End OnCreate
    /*
    private void ReloadMusicList(int count){
    	pagecount = count;
    	
    }
    */
    
    public void initAlbumList(){
    	//initial AlbumListUrl[]
    	// get Album List
    	
    	if(MainUrl.getAlbumSongUrl()==null)
    		MainUrl.initialRecommendData();
    	if(MainUrl.getAlbumListUrl()==null){
    		
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
		   for (int i = 0; i < albumsArray.length; i++) {

			   AlbumListUrl[i] = albumsArray[i].getListUrl();
			   AlbumName[i] = albumsArray[i].getName();
			   AlbumArtist[i] = albumsArray[i].getArtist();
			   AlbumPublisher[i] = albumsArray[i].getPublisher();
			   AlbumIssueDate[i] = albumsArray[i].getDate();
			   AlbumIconUrl[i] = albumsArray[i].getIconUrl();
		   }
		   MainUrl.setAlbumArtist(AlbumArtist);
		   MainUrl.setAlbumName(AlbumName);
		   MainUrl.setAlbumPublisher(AlbumPublisher);
		   MainUrl.setAlbumListUrl(AlbumListUrl);
		   MainUrl.setAlbumIconUrl(AlbumIconUrl);
		   MainUrl.setAlbumIssueDate(AlbumIssueDate);
		   pHandler = null;
	       albumsArray = null;
    	}
    	SongData s = null;
    	songList = new ArrayList<SongData>();
		for (int i = 0; i < MainUrl.getAlbumListUrl().length; i++) {
	
	        s = new SongData(MainUrl.getAlbumIconUrl()[i], MainUrl.getAlbumName()[i],  MainUrl.getAlbumArtist()[i]);
	        songList.add(s); 
			
		}
		
        SongAdapter songAdapater = new SongAdapter(getContext(), R.layout.song_list_item, songList);
        RecommendActivity.musiclist.setAdapter(songAdapater);
        RecommendActivity.musiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
        
            	        Intent aIntent = new Intent(getContext(), AlbumSongListActivity.class);
            	        try{
            	         
            	            aIntent.putExtra("AlbumName", MainUrl.getAlbumName()[pos]);
            	            aIntent.putExtra("AlbumArtist", MainUrl.getAlbumArtist()[pos]);
            	            aIntent.putExtra("AlbumPublisher", MainUrl.getAlbumPublisher()[pos]);
            	            aIntent.putExtra("AlbumIssueDate", MainUrl.getAlbumIssueDate()[pos]);
            	            aIntent.putExtra("AlbumListUrl", MainUrl.getAlbumListUrl()[pos]);
            	            aIntent.putExtra("AlbumIconUrl", MainUrl.getAlbumIconUrl()[pos]);

            	        }catch(Exception e){
            	        	Log.e(myTag, "ShowAlbumList err:"+e.toString(), e);
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
				initAlbumList();
				break;

			}
			super.handleMessage(msg);
		}
	}   
    
}
