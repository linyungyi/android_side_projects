package com.cht.channelme;


import java.util.ArrayList;
import java.util.HashMap;

import com.cht.channeldata.ChannelList;
import com.cht.parser.ParserHandler;
import com.cht.parser.ParsingXML;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainPage extends Activity {
	
	
	private Object[] Channel_Category_ID = null;
	private Object[] Channel_Category_Name = null;
	private Object[] channelList = null;
	private String ListUrl = "http://10.1.3.199/ApplicationServer/ApplicationServlet?Task=UserChannelList&ListType=all&testmark=true";
	
	private ImageButton ShowGallery;
	private ImageButton ShowList;
	private Gallery myGallery;
	private ListView myListView;
    private LinearLayout MainLayout;
	
	private Context mContext;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        
        ParsingXML xml = new ParsingXML();
        ParserHandler handler = xml.parsingData(ListUrl,"List");
        //xml.printParsedInfo(handler, "List");
        
        HashMap<String, Object[]> channel = handler.parserChannelList();
        
        Channel_Category_ID = (Object[])channel.get("id");
        Channel_Category_Name = (Object[])channel.get("name");
        channelList = (Object[]) channel.get("list");
        ArrayList<ChannelList> array = new ArrayList<ChannelList>();
        for(int i=0;i<channelList.length;i++){
        	array.add((ChannelList)channelList[i]);
        	//Log.i("1234", "url:"+((ChannelList)channelList[i]).getChannelUrl());
        }
        
        ShowGallery = (ImageButton)findViewById(R.id.TagGallery);
        ShowList = (ImageButton)findViewById(R.id.TagList);
        MainLayout = (LinearLayout)findViewById(R.id.MainLayout);
        
        myGallery = new Gallery(this);
        myGallery.setAdapter(new GalleryAdapter(this,R.layout.mygallery,array));
        myGallery.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			//	Log.i("1234", "arg2="+arg2+"   arg3="+arg3);
				Intent mIntent = new Intent(mContext,ChannelItemList.class);
				mIntent.putExtra("url", ((ChannelList)channelList[arg2]).getChannelUrl());
				mIntent.putExtra("title", ((ChannelList)channelList[arg2]).getChannel_Name());
				mContext.startActivity(mIntent);
			}
        });
        MainLayout.addView(myGallery);
       //     xml.printParsedInfo(handler, "List");
       
       //  xml.parsingData("http://10.1.3.199/ApplicationServer/ApplicationServlet?Task=ChannelDetail&ChannelId=010002");
       //   xml.printParsedInfo(handler, "Item");
    }
}