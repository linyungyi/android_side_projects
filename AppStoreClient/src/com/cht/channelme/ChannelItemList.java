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

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChannelItemList extends Activity {
    /** Called when the activity is first created. */

    private Context mContext;
	
    private TextView ChannelTitle;
    private ListView ItemList;
    
    private String ItemID = "";
    private String ItemName = "";
    private String Item_Description ;
    private ChannelItem nowItem;
    private String Url = "";
    private String Title = "";
    private Object[] objItem = null;
    private String myTag = "ChannelItemList";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);
        mContext = this;
       
        Bundle bundle = this.getIntent().getExtras();
        
        try{
        	Url = bundle.getString("url");     
        	Title = bundle.getString("title");  
        	//Log.i(myTag, "item url="+Url);
        }catch(Exception e){
        	
        }
        ParsingXML xml = new ParsingXML();
        ParserHandler handler = xml.parsingData(Url,"Item");
       // xml.printParsedInfo(handler, "Item");
        
        HashMap<String, Object[]> channel = handler.parserChannelItem();
        
       
        objItem = (Object[]) channel.get("item");
        ArrayList<ChannelItem> array = new ArrayList<ChannelItem>();
        for(int i=0;i<objItem.length;i++){
        	array.add((ChannelItem)objItem[i]);
        }

        //-------initial panel-----------------
        ChannelTitle = (TextView)findViewById(R.id.ItemTitle);
        ItemList = (ListView)findViewById(R.id.ItemListView);    
        
        ChannelTitle.setText(Title);
        ItemList.setAdapter(new ItemListAdapter(this,R.layout.item_list_layout,array));
        ItemList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i(myTag, ((ChannelItem)objItem[arg2]).getDescription());
				Intent mIntent = new Intent(mContext,ItemWebView.class);
				mIntent.putExtra("title", ((ChannelItem)objItem[arg2]).getPropTitle());
				mIntent.putExtra("desc", ((ChannelItem)objItem[arg2]).getDescription());
				mContext.startActivity(mIntent);
			}
        	
        });
        	
        
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
    
 
