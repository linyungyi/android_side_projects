package com.cht.android.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

public class PlayerActivity extends Activity {
    private static final String myTag = "PLAYER";


    // Menu ID
    private static final int MENU_DOWNLOAD = 0;
    private static final int MENU_SET_AS_RINGTONE = 1;

    private static final int EVENT_SHOW_MEDIA_CONTROLLER = 101;

    // Instance variables
    private TextView mArtistName;
    private TextView mSongName;
    private ImageView mCoverImage;
    private View mAnchor;
    private MediaController mController;
    private MediaPlayer mPlayer;
    private String Artist;
    private String MusicName;
    private String AlbumUrl;
    private String SongID;
    private String WavUrl;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_SHOW_MEDIA_CONTROLLER:
                    mController.setAnchorView(mAnchor);
                    mController.show(0);
                    break;
                default:
                    Log.w(myTag, "Unknown message: " + msg.what);
                    return;
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_player);
        
        // setup essential data
        Bundle bundle = this.getIntent().getExtras();
        Artist = bundle.getString("Artist");
        MusicName = bundle.getString("MusicName");
        AlbumUrl = bundle.getString("AlbumUrl");
        SongID = bundle.getString("SongID");
        WavUrl = bundle.getString("WavUrl");
        
        
        
        initLayout();
        initMediaPlayer();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (mPlayer == null) {
            initMediaPlayer();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        try {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        catch (Exception e) {
            Log.e(myTag, "Failed to release player.");
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_DOWNLOAD, 0, R.string.menu_download);
        menu.add(0, MENU_SET_AS_RINGTONE, 0, R.string.menu_set_ringtone);
        
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case MENU_DOWNLOAD:
                // TODO: Invoke browser with corresponding URL.
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://hami.emome.net"));
                startActivity(intent);
                break;
            case MENU_SET_AS_RINGTONE:
                // TODO: Pass the information of current music to the Settings.
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        
        return true;
    }

    private void initLayout() {
        mArtistName = (TextView) findViewById(R.id.artist_name);
        //TODO: Set Artist Name.
        mArtistName.setText(Artist);
        mCoverImage = (ImageView) this.findViewById(R.id.cover_image);
      //  mCoverImage.setImageResource(R.drawable.cover);
        Bitmap bm = ImageOperations(AlbumUrl,"image.jpg");
        mCoverImage.setImageBitmap(bm);    
        
        
        mSongName = (TextView) findViewById(R.id.song_name);
        //TODO: Set Song Name.
        mSongName.setText(MusicName);

        mAnchor = (View) findViewById(R.id.anchor);
        mAnchor.setOnClickListener(new View.OnClickListener() {
			//s@Override
			public void onClick(View v) {
				mController.show(0);
			}
        });
    }

    private void initMediaPlayer() {
        // TODO: Play Audio Stream instead.
        // Init MediaPlayer
       // mPlayer = MediaPlayer.create(this, R.raw.sample);
    	mPlayer = new MediaPlayer();
        mPlayer.setScreenOnWhilePlaying(true);
        try{
         	mPlayer.setDataSource("http://10.1.14.32/audiobank/RBT/web/"+SongID.substring(0,2)+"/"+SongID.substring(2,4)+"/"+SongID+".wma");
            mPlayer.prepare();
            mPlayer.start();
        }catch(Exception e){
        	Log.e(myTag,"Media Player Fail:(URL=http://10.1.14.32/audiobank/RBT/web/"+SongID.substring(0,2)+"/"+SongID.substring(2,4)+"/"+SongID+".wma)"+e.toString());
        }

        // Initialize MusicController
        mController = new MediaController(this, false);
        mController.setMediaPlayer(mPlayerControl);
        mController.setEnabled(true);
        mController.setAnchorView(mAnchor);

        // FIXME: mController.show(0) can not be invoked
        // immediately due to NULL Pointer Exception in View.getLocationOnScreen.
        mHandler.sendMessageDelayed(
                Message.obtain(mHandler, EVENT_SHOW_MEDIA_CONTROLLER), 50);
    }

    private MediaController.MediaPlayerControl mPlayerControl = new
        MediaController.MediaPlayerControl() {

        public int getBufferPercentage() {
            if (mPlayer != null) {
                // TODO: get buffer percentage.
            }
            return 0;
        }

        public int getCurrentPosition() {
            if (mPlayer != null) {
                return mPlayer.getCurrentPosition();
            }
            return 0;
        }

        public int getDuration() {
            if (mPlayer != null) {
                return mPlayer.getDuration();
            }
            return -1;
        }

        public boolean isPlaying() {
            if (mPlayer != null) {
                return mPlayer.isPlaying();
            }
            return false;
        }

        public void start() {
            if (mPlayer != null) {
                mPlayer.start();
            }
        }

        public void seekTo(int pos) {
            if (mPlayer != null) {
                mPlayer.seekTo(pos);
            }
        }

        public void pause() {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
            }
        }
    }; 
    
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
}
