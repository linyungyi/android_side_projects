package com.cht.android.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.cht.android.music.ActivityDetails.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Song;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumSongListActivity extends Activity {

    TextView TVAlbumName;
    TextView TVAlbumArtist;
    TextView TVAlbumPublisher;
    TextView TVAlbumIssueDate;

    ListView LSongList;
    private ArrayList<SongData> songList;
    
    private static final int CON_INDEX_RECOMMEND  = 0;
    private static final int CON_INDEX_RANKING     = 1;
    private static final int CON_INDEX_ACTIVITY   = 2;
    private static final int CON_INDEX_SEARCH   = 3;
    private static final int CON_INDEX_SETTING   = 4;
    private static final int CON_INDEX_MYLIST   = 5;
    
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    
    private String AlbumName = null;
    private String AlbumArtist = null;
    private String AlbumPublisher = null; 
    private String AlbumIssueDate = null; 
    private String AlbumListUrl = null;
    private String AlbumIconUrl = null;

    Song[] songsArray;
    HttpThread httpThread;
    
    private String[] SongID = null;
    private String[] AlbumUrl = null;
    private String[] WavUrl = null;
    
    private String myTag = "AlbumSongListActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.showalbumlist);
        Bundle bundle = this.getIntent().getExtras();
        
    	httpThread = new HttpThread(AlbumListUrl, new MyHandler(),UPDATE_SETTING_SUCCESS);
		httpThread.start();
		
        AlbumName = bundle.getString("AlbumName");
        AlbumArtist = bundle.getString("AlbumArtist");
        AlbumPublisher = bundle.getString("AlbumPublisher");
        AlbumIssueDate = bundle.getString("AlbumIssueDate");
        AlbumListUrl = bundle.getString("AlbumListUrl");
        AlbumIconUrl = bundle.getString("AlbumIconUrl");

     //   Log.i(myTag, "MusicBoxImageUrl="+MusicBoxImageUrl);
        
        ImageView activityImageView = (ImageView) this.findViewById(R.id.ShowAlbumIcon);
        Bitmap bm = ImageOperations(AlbumIconUrl,"image.jpg");
        //Bitmap bm = null;
        activityImageView.setImageBitmap(bm);  
        findAPcomponent();
        fillListData();
   
    }
    
    private void findAPcomponent()
    {
    	TVAlbumName = (TextView)findViewById(R.id.ShowAlbumName);
    	TVAlbumArtist = (TextView)findViewById(R.id.ShowAlbumArtist);
    	TVAlbumPublisher = (TextView)findViewById(R.id.ShowAlbumPublisher);
    	TVAlbumIssueDate = (TextView)findViewById(R.id.ShowAlbumIssueDate);
        LSongList = (ListView)findViewById(R.id.ShowAlbumSongList);
        
        //TODO, to get data 
        TVAlbumName.setText("專輯名稱："+AlbumName);
        TVAlbumArtist.setText("歌手："+AlbumArtist);      
        TVAlbumPublisher.setText("發行："+AlbumPublisher);
        TVAlbumIssueDate.setText("發行日期："+AlbumIssueDate);     
    }
    
   
    private void fillListData()
    {
        songList = new ArrayList<SongData>();
        
       // MusicBoxListUrl = "http://musicphone.emome.net/MAS/DoTask?Task=MusicBoxSongs&MUSICBOXID=074210&xsl=pcIni.xsl";
        
		SongID = new String[songsArray.length];
		AlbumUrl = new String[songsArray.length];
		WavUrl = new String[songsArray.length];
		
		SongData s = null;
		for (int i = 0; i < songsArray.length; i++) {
			SongID[i] = songsArray[i].getProductid();
			AlbumUrl[i] = songsArray[i].getImgAlbumUrl();
			WavUrl[i] = songsArray[i].getWavUrl();
	        s = new SongData(songsArray[i].getImgArtistUrl(), songsArray[i].getSongName(), songsArray[i].getSinger());
	        songList.add(s); 
			
		}
        
        SongAdapter songAdapater = new SongAdapter(this, R.layout.song_list_item, songList);
        
        LSongList.setAdapter(songAdapater);
        
        LSongList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
                startPlayer(pos, id);
            }
        });
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
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://hami.emome.net"));
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
    
    private void startPlayer(int pos, long id)
    {
        SongData src = songList.get(pos);

        Intent nIntent = new Intent(this, PlayerActivity.class);
        // TODO: Pass the data source to player
        nIntent.putExtra("Name", src.getName());
        nIntent.putExtra("Artist", src.getArtist());
        nIntent.putExtra("AlbumUrl", AlbumUrl[pos]);
        nIntent.putExtra("SongID", SongID[pos]);
        nIntent.putExtra("WavUrl", WavUrl[pos]);
        this.startActivity(nIntent);
    }
    
	private Bitmap ImageOperations(String address, String saveFilename) {
		String ImageTag = "ImageOperations";
		InputStream is= null;
		Bitmap bitmap = null;
		try {
			Log.i(ImageTag,"ImageOperations go:"+address);
			URL url = new URL(address);
			URLConnection conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		
		} catch (IOException e) {
			Log.e(ImageTag,"ImageOperations go e="+e.toString());
			e.printStackTrace();
			return null;
		}finally{
			
			try{
			  is.close();
			}catch(Exception e){
				
			}
			is = null;
		}
	}    
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
		     //   RecommendActivity.songdata = myHandler.getParsedSongsArray();
				ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
				ParserHandler pHandler = pXml.parsingData();
		        songsArray = pHandler.getParsedSongsArray();

				break;

			}
			super.handleMessage(msg);
		}
	}	
    
}
