package com.cht.android.music;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityDetails extends Activity {

    TextView TVTopic;
    TextView TVPeriod;
    Button BTNDetails;
    ListView LSongList;
    private ArrayList<Song> songList;
    private static final int CON_INDEX_DOWNLOAD  = 0;
    private static final int CON_INDEX_PLAY     = 1;
    private static final int CON_INDEX_SETUP   = 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
   
        findAPcomponent();
        setListner();
        fillListData();
   
    }
    
    private void findAPcomponent()
    {
        TVTopic = (TextView)findViewById(R.id.Topic);
        TVPeriod = (TextView)findViewById(R.id.Period);
        BTNDetails = (Button)findViewById(R.id.btnDetails);
        LSongList = (ListView)findViewById(R.id.DetailSongList);
        
        //TODO, to get data 
        TVTopic.setText("再次愛上<<小宇>>的盛夏情歌<<就站在這裡>>化身傳情小天使 徵求告白");
        TVPeriod.setText("06-22 ~ 07-12");        
    }
    
    private void setListner()
    {
        BTNDetails.setOnClickListener( new OnClickListener() {
            public void onClick(View v) {
                showDialog(0);
            }
        });
    }
      
    private String Msg = "【想要大聲對喜歡的人告白嗎？沒問題<<小宇就站在這裡>>幫你唱給你的另一半聽！】"
        + "，即日起~7/12止，凡是中華電信行動電話用戶，於手機直撥760或上emome網站、emome手機網…";
    
    @Override
    protected Dialog onCreateDialog(int id) {

        return new AlertDialog.Builder(ActivityDetails.this)
        .setTitle(R.string.title_detail)
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
                Song aSong = (Song)LSongList.getAdapter().getItem(0);
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




