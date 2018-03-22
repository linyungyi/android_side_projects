package com.cht.android.music;


import java.util.ArrayList;


import com.cht.android.music.HttpThread;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Song;


import android.content.Context;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;



public class RecommendView extends View {



    private ArrayList<SongData> songList;
    // IDs of the context menu items for the list of conversations.
    
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;

    public static final int SEARCHSTATE_MANU            = 0;
    public static final int SEARCHSTATE_LIST            = 1;
    
    public int ListNum = 0;
	private final String myTag = "RecommendView";
	HttpThread httpThread;
	//private TabHost mTabHost;
    public RecommendView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
    	if(MainUrl.getSingleSongUrl()==null)
    		MainUrl.initialRecommendData();
    	if(MainUrl.getRecSong()==null){
    	   
		   httpThread = new HttpThread(MainUrl.getSingleSongUrl(), new MyHandler(),UPDATE_SETTING_SUCCESS);
		   httpThread.start();
    	}else   
		   initMusicList(context);
		
	}
    
    public void initMusicList(Context context){
    //TODO define actions when user select item in MusicList
    	//  first time to get url data
    	
        songList = new ArrayList<SongData>();

		SongData s = null;
		
		for (int i = 0; i < MainUrl.getRecSong().length; i++) {

			
	        s = new SongData(MainUrl.getRecSong()[i].getImgArtistUrl(), MainUrl.getRecSong()[i].getSongName(),  MainUrl.getRecSong()[i].getSinger());
	        songList.add(s); 
			
		}
	
        SongAdapter songAdapater = new SongAdapter(context, R.layout.song_list_item, songList);
      //  return songAdapater;
        
        RecommendActivity.musiclist.setAdapter(songAdapater);
        RecommendActivity.musiclist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
            	RecommendActivity.startPlayer(pos);
            }
        });
        
      //  return musiclist;
        
        /* Add Context-Menu listener to the ListView. */ 
        /*
        MainPageActivity.musiclist.setOnCreateContextMenuListener(new OnCreateContextMenuListener() { 

            public void onCreateContextMenu(ContextMenu menu, View v,
            		ContextMenu.ContextMenuInfo menuInfo) {
            	
            	
                    menu.add(0, 0, 0, R.string.recommend_album);
                    menu.add(0, 1, 0, R.string.recommend_musicbox);
          
            } 
        });
        */
               
    }
    
    /*
	
	public boolean onOptionsItemSelected(MenuItem item)
	{  
        Intent intent;
      //  mLinearLayout.removeAllViews();
      //  LayoutInflater.from(MyActivity.this).inflate(R.layout.main2,mLinearLayout,true);
        // Switch on the ID of the item, to get what the user selected. 
        switch (item.getItemId()) {   
        
            case CON_INDEX_ALBUM:
                
                intent = new Intent(this,RecommendAlbumActivity.class);
                this.startActivity(intent);               
                return true;
            case CON_INDEX_MUSICBOX:  	
                intent = new Intent(this,RecommendMusicBoxActivity.class);
                this.startActivity(intent);               
                return true;
            case CON_INDEX_DOWNLOAD:  
                // TODO: Invoke browser with corresponding URL.
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://hami.emome.net"));
                startActivity(intent);               
                return true;
            case CON_INDEX_PLAY:
                SongData aSong = (SongData)musiclist.getAdapter().getItem(0);
                intent = new Intent(this, PlayerActivity.class);

                intent.putExtra("MusicName", aSong.getName());
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
 
     */
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
		     //   RecommendActivity.songdata = myHandler.getParsedSongsArray();
		        
				ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
				ParserHandler pHandler = pXml.parsingData();
				RecommendActivity.songdata = pHandler.getParsedSongsArray();
				MainUrl.setRecSong(RecommendActivity.songdata);
			 //	Content = pXml.printParsedInfo(pHandler, "song");
				initMusicList(getContext());
			 //	cTextView.setText(Content);
				break;

			}
			super.handleMessage(msg);
		}
	}
}
