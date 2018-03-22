package com.cht.channelme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.cht.channeldata.ChannelItem;
import com.cht.channeldata.ChannelList;
import com.cht.parser.ParserHandler;
import com.cht.parser.ParsingXML;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ItemWebView extends Activity {
    /** Called when the activity is first created. */

    private Context mContext;
	
    private TextView ChannelTitle;
    private WebView webview;
    
    private String ItemName = "";
    private String Item_Description ;
    private Object[] objItem = null;
    private String myTag = "webview";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        mContext = this;
       
        Bundle bundle = this.getIntent().getExtras();
        
        try{
        	Item_Description = bundle.getString("desc");     
        	ItemName = bundle.getString("title");  
        }catch(Exception e){
        	
        }
     

        //-------initial panel-----------------
        ChannelTitle = (TextView)findViewById(R.id.ItemTitle);
        webview = (WebView)findViewById(R.id.webView);    
        ChannelTitle.setText(ItemName);
        try{
        	Item_Description = "<html><body><head><meta http-equiv='Content-Type' charset='utf-8' /></head>"+new String(Item_Description.getBytes(), "ISO-8859-1")+"</body></html>";
        	//Item_Description = new String(Item_Description.getBytes(), "UTF-8");
        	webview.loadData(Item_Description, "text/html", "UTF-8");

        }catch(Exception e){
        	
        }
        	
        
    }    
    //-------------menu handle--------------------------------------------
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
       // super.onPrepareOptionsMenu(menu);
    	menu.clear();
       
      
    	return true;
     }     
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onPrepareOptionsMenu(menu);
 
      //  menu.add(0, CON_INDEX_ALBUM, 0, R.string.recommend_album);

    	return true;
     }     

 	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{  
       // Intent intent;
        /* Switch on the ID of the item, to get what the user selected. */  
        switch (item.getItemId()) {   
        
                     
                //return true;
        }
        
        return false;   
    }       
} 	
    
 
