package com.cht.android.music;

import java.util.ArrayList;
import java.util.Hashtable;

import com.cht.android.music.RecommendAlbumView.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Song;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchView extends View {
   
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;
	private final String myTag = "SearchView";

	public Button search_button;
    public EditText search_field;
    public ListView searchlist;
    public ListView musiclist;
    private ArrayList<SongData> songList;
    // IDs of the context menu items for the list of conversations.
    
    private int CON_INDEX_INITIAL = 0;
    private int CON_INDEX_CATEGORY = 1;
    private int CON_INDEX_HOTTOPIC = 2;
    private int CON_INDEX_KEYWORD = 3;

    
    private static final int CON_INDEX_DOWNLOAD  = 0;
    private static final int CON_INDEX_PLAY     = 1;
    private static final int CON_INDEX_SETUP   = 2;
    
    public static final int SEARCHSTATE_MANU            = 0;
    public static final int SEARCHSTATE_LIST            = 1;
    
    private boolean isSearchResultToggled = false;
    private String[] mStrings;
    private String[] mUrl;

    private Song[] songsArray = null;
    private int NOW_INDEX = 0;
    private boolean NOW_ENABLE = false;
    public SearchView(Context context) {
		super(context);
		
		if(MainUrl.getSearchTypeTitle()==null)
			MainUrl.initialSearchUrl();
		
		mStrings = MainUrl.getSearchTypeTitle();
		initSearchList(context,mStrings);
		//toggleSearchResult(NOW_ENABLE);
		// TODO Auto-generated constructor stub
	}
    
    private void setContentXML(){
    	
    	if(NOW_INDEX==CON_INDEX_INITIAL){
            mStrings = MainUrl.getSearchTypeTitle();
     	}else if(NOW_INDEX==CON_INDEX_CATEGORY){
            mStrings = MainUrl.getCategoryTitle();
            mUrl = MainUrl.getCategoryUrl();
     	}else if(NOW_INDEX==CON_INDEX_HOTTOPIC){
             mStrings = MainUrl.getHotTopicTitle();
             mUrl = MainUrl.getHotTopicUrl();
     	}else if(NOW_INDEX==CON_INDEX_KEYWORD){
             mStrings = MainUrl.getKeywordTitle();
             mUrl = MainUrl.getKeywordUrl();
     	}    
    }
    
    public void setButtonListener(){
    	SearchActivity.search_button.setOnClickListener(new Button.OnClickListener() {
    
            public void onClick(View v) {
                //mAdapter.clearPhotos();
                //activate();
                toggleSearchResult(NOW_ENABLE);
            } });   
    }

    public void toggleSearchResult(boolean enabled){
    	setContentXML();
    	
        if (enabled) {
          //  setContentView(R.layout.search_result);
            //initMusicList();
            Log.i(myTag, "initMusicList()");
        }
        else {
          //  setContentView(R.layout.search_activity);       
            //initSearchList();
            Log.i(myTag, "initSearchList()");
        }
       // isSearchResultToggled = enabled;
    }
    

    
    public void initSearchList(Context context,String[] txt){
    //TODO define actions when user select item in SearchList
     //   MainPageActivity.musiclist = (ListView)findViewById(R.id.search_result);  
        mStrings = txt;
      //  Log.i(myTag,"getHotTopicTitle="+MainUrl.getHotTopicTitle()[0]);
      //  Log.i(myTag,"txt[0]="+txt[0]);
        
        if(mStrings[0].compareTo(MainUrl.getSearchTypeTitle()[0])==0)
           SearchActivity.setSearchBackVisibility(android.view.View.INVISIBLE);
        else{
        	SearchActivity.setSearchBackVisibility(android.view.View.VISIBLE);
        	SearchActivity.backbutton.setOnClickListener(new OnClickListener(){

    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				initSearchList(getContext(),MainUrl.getSearchTypeTitle());
    			}
        		
        	});
        }
        SearchActivity.searchlist.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, txt));
        SearchActivity.searchlist.setTextFilterEnabled(true);
        SearchActivity.set_isNext_to_Song(false);
        SearchActivity.searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        
         //   @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	//Log.i(myTag,"arg2="+arg2+"  arg3="+arg3);
            	//arg0.getContext().get
            	   if(mStrings[arg2].compareTo("分類搜尋")==0){
            //		   Log.i(myTag,"MainUrl.getCategoryTitle().length="+MainUrl.getCategoryTitle().length);
            	     //  String[] categorysearch = new String[]{"國語男歌手","國語女歌手","國語團體","台語歌曲","日韓歌曲","西洋歌曲"};
            		   mUrl = MainUrl.getCategoryUrl();
            	       initSearchList(SearchActivity.searchlist.getContext(),MainUrl.getCategoryTitle());
            	   }else if(mStrings[arg2].compareTo("發燒主題")==0){
            		   mUrl = MainUrl.getHotTopicUrl();
            		//   Log.i(myTag,"MainUrl.getHotTopicUrl()="+MainUrl.getHotTopicUrl()[0]);
            		//   Log.i(myTag,"MainUrl.getHotTopicTitle()="+MainUrl.getHotTopicTitle()[0]);
            		   initSearchList(SearchActivity.searchlist.getContext(),MainUrl.getHotTopicTitle());

            	   }else if(mStrings[arg2].compareTo("熱門關鍵字")==0){
            		   mUrl = MainUrl.getKeywordUrl();
            		   initSearchList(SearchActivity.searchlist.getContext(),MainUrl.getKeywordTitle());
            	   }else{
            		  //  Log.i(myTag,"arg2="+arg2+"  arg3="+arg3);
            	        String url = mUrl[arg2].replaceAll("10.1.14.109", "musicphone.emome.net");
            	        url = url.replaceAll("pagesize=", "pagesize=10");
            	        url = url.replaceAll("pagecount=", "pagecount=1");
            	    	httpThread = new HttpThread(url, new MyHandler(),UPDATE_SETTING_SUCCESS);
            			httpThread.start();
            	     	
            	    	
            		   // initMusicList(arg2,SearchActivity.searchlist.getContext());
            	   }
            }
        }
        );        
    }
    
    public void initMusicList(Context mcontext){
    //TODO define actions when user select item in SearchList

    	
    	SearchActivity.setSearchBackVisibility(android.view.View.VISIBLE);
    	SearchActivity.backbutton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				initSearchList(getContext(),mStrings);
			}
    		
    	});
    	ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
    	ParserHandler pHandler = pXml.parsingData();
     
        songsArray = pHandler.getParsedSongsArray();
        songList = new ArrayList<SongData>();
        
		SongData s = null;
		//Log.i(myTag, "url="+url);
		for (int i = 0; i < songsArray.length; i++) {

			
	        s = new SongData(songsArray[i].getImgArtistUrl(), songsArray[i].getSongName(),  songsArray[i].getSinger());
	        songList.add(s); 
			
		}
		SearchActivity.setSong(songsArray);
		SearchActivity.set_isNext_to_Song(true);

        
        SongAdapter songAdapater = new SongAdapter(mcontext, R.layout.song_list_item, songList);
        
        SearchActivity.searchlist.setAdapter(songAdapater);
        
        
        SearchActivity.searchlist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
            	SearchActivity.startPlayer(pos);
            }
        });
        
    }

    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isSearchResultToggled && KeyEvent.KEYCODE_BACK == keyCode) {
            this.toggleSearchResult(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
				initMusicList(SearchActivity.searchlist.getContext());
				break;

			}
			super.handleMessage(msg);
		}
	}    
    
}
