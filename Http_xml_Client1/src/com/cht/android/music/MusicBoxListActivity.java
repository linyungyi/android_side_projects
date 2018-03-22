package com.cht.android.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.cht.android.music.MainActivity.MyHandler;
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

public class MusicBoxListActivity extends Activity {

    TextView TVMusicBoxDetail;
    TextView TVMusicBoxPrice;
    Button BuyButton;
    ListView LSongList;
    private ArrayList<SongData> songList;
    
    private static final int CON_INDEX_DOWNLOAD  = 6;
    private static final int CON_INDEX_PLAY     = 5;
    private static final int CON_INDEX_SETUP   = 4;
    private static final int CON_INDEX_SINGLE   = 0;
    private static final int CON_INDEX_ALBUM   = 1;
    private static final int CON_INDEX_MUSICBOX   = 2;
    
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;
    HttpThread httpThread;

    
    private String MusicBoxDescription = null;
    private String MusicBoxPrice = null;
    private String MusicBoxImageUrl = null; 
    private String MusicBoxListUrl = null; 
 
    private String[] SongID = null;
    private String[] AlbumUrl = null;
    private String[] WavUrl = null;
    
    private String myTag = "MusicBoxListActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.musicbox_list);
        Bundle bundle = this.getIntent().getExtras();
        
        MusicBoxDescription = bundle.getString("MusicBoxDescription");
        MusicBoxPrice = bundle.getString("MusicBoxPrice");
        MusicBoxImageUrl = bundle.getString("MusicBoxImageUrl");
        MusicBoxListUrl = bundle.getString("MusicBoxListUrl");
      
     //   Log.i(myTag, "MusicBoxImageUrl="+MusicBoxImageUrl);
        
        ImageView activityImageView = (ImageView) this.findViewById(R.id.MusicBoxIcon);
        Bitmap bm = ImageOperations(MusicBoxImageUrl,"image.jpg");
        //Bitmap bm = null;
        activityImageView.setImageBitmap(bm);  
        findAPcomponent();
        setListner();
		httpThread = new HttpThread(MusicBoxListUrl, new MyHandler(),UPDATE_SETTING_SUCCESS);
		httpThread.start();
        
   
    }
    
    private void findAPcomponent()
    {
    	TVMusicBoxDetail = (TextView)findViewById(R.id.MusicBoxDetail);
    	TVMusicBoxPrice = (TextView)findViewById(R.id.MusicBoxPrice);
    	BuyButton = (Button)findViewById(R.id.MusicBoxBuyButton);
        LSongList = (ListView)findViewById(R.id.MusicBoxList);
        
        //TODO, to get data 
        TVMusicBoxDetail.setText(MusicBoxDescription);
        TVMusicBoxPrice.setText("»ù®æ¡G"+MusicBoxPrice);        
    }
    
    private void setListner()
    {
    	BuyButton.setOnClickListener( new OnClickListener() {
            public void onClick(View v) {
                showDialog(0);
            }
        });
    }
      
    private String Msg = MusicBoxDescription;
    
    @Override
    protected Dialog onCreateDialog(int id) {

        return new AlertDialog.Builder(MusicBoxListActivity.this)
        .setTitle(R.string.musicbox_buy)
        .setMessage(Msg)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /* User clicked OK so do some stuff */
            }
        })
        .create();
        
    }
    
   
    private void fillListData()
    {
        songList = new ArrayList<SongData>();
        
		ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
		ParserHandler pHandler = pXml.parsingData();
        Song[] songsArray = pHandler.getParsedSongsArray();
        
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
        
        /* Add Context-Menu listener to the ListView. */ 
        LSongList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() { 

            public void onCreateContextMenu(ContextMenu menu, View v,
                    ContextMenuInfo menuInfo) {
                menu.add(0, CON_INDEX_DOWNLOAD, 0, R.string.menu_download);
                menu.add(0, CON_INDEX_PLAY, 0, R.string.menu_play);
                menu.add(0, CON_INDEX_SETUP, 0, R.string.menu_set_ringtone);
                
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
          
            case CON_INDEX_ALBUM:  	
            	
                intent = new Intent(this,RecommendAlbumActivity.class);
                this.startActivity(intent);               
                return true;
                
            case CON_INDEX_DOWNLOAD:  
                // TODO: Invoke browser with corresponding URL.
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://hami.emome.net"));
                startActivity(intent);               
                return true;
            case CON_INDEX_PLAY:
                intent = new Intent(this, PlayerActivity.class);

                
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
				fillListData();
				break;

			}
			super.handleMessage(msg);
		}
	}    
}




