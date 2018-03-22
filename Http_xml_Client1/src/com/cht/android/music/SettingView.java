package com.cht.android.music;



import java.util.ArrayList;

import com.music.parser.data.Song;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SettingView extends View {

    private String[] ringtype = new String[]{"基本音","白天音","晚上音","週末音"};
    private ListView basiclv = null;
    private ListView daylv = null;
    private ListView nightlv = null;
    private ListView weekendlv = null;
    private ArrayList<SongData> songList = null;
    
    private int basicstart = 0;
    private int daystart = 0;
    private int nightstart = 0;
    private int weekendstart = 0;
    public SettingView(Context context,String type) {
		super(context);
		// TODO Auto-generated constructor stub
		
		/*
		setBasicListContent();
		setdDayListContent();
		setNightListContent();
		setWeekendListContent();
		*/
		initialSettingView(context,type);
		
	}

    
    private String myTag = "Setting";
    private int nextview = 0;
    
    public void initialSettingView(Context context,String type){
    	int count = 0;
	    songList = new ArrayList<SongData>();
	    SongData s = null;
       // songList.add(s); 
        if(type.compareTo("basic")==0){
    	    if(MainUrl.getBasicRing()!=null){
    	    	count = MainUrl.getBasicRing().length;
    	        for (int i = 0; i < MainUrl.getBasicRing().length; i++) {
                   s = new SongData(MainUrl.getBasicRing()[i].getImgArtistUrl(), MainUrl.getBasicRing()[i].getSongName(),  MainUrl.getBasicRing()[i].getSinger());
                   songList.add(s); 
            
    	        }
            }else{   
            	count = 0;
                s = new SongData("", "尚未設定答鈴",  "");
                songList.add(s); 
            }
        }else if(type.compareTo("day")==0){
    	    if(MainUrl.getDayRing()!=null){
    	    	count = MainUrl.getDayRing().length;
    	        for (int i = 0; i < MainUrl.getDayRing().length; i++) {
                   s = new SongData(MainUrl.getDayRing()[i].getImgArtistUrl(), MainUrl.getDayRing()[i].getSongName(),  MainUrl.getDayRing()[i].getSinger());
                   songList.add(s); 
            
    	        }
            }else{   
            	count = 0;
                s = new SongData("", "尚未設定答鈴",  "");
                songList.add(s); 
            }
        }else if(type.compareTo("night")==0){
    	    if(MainUrl.getNightRing()!=null){
    	    	count = MainUrl.getNightRing().length;
    	        for (int i = 0; i < MainUrl.getNightRing().length; i++) {
                   s = new SongData(MainUrl.getNightRing()[i].getImgArtistUrl(), MainUrl.getNightRing()[i].getSongName(),  MainUrl.getNightRing()[i].getSinger());
                   songList.add(s); 
            
    	        }
            }else{   
            	count = 0;
                s = new SongData("", "尚未設定答鈴",  "");
                songList.add(s); 
            }
        }else if(type.compareTo("weekend")==0){
    	    if(MainUrl.getWeekendRing()!=null){
    	    	count = MainUrl.getWeekendRing().length;
    	        for (int i = 0; i < MainUrl.getWeekendRing().length; i++) {
                   s = new SongData(MainUrl.getWeekendRing()[i].getImgArtistUrl(), MainUrl.getWeekendRing()[i].getSongName(),  MainUrl.getWeekendRing()[i].getSinger());
                   songList.add(s); 
            
    	        }
            }else{   
            	count = 0;
                s = new SongData("", "尚未設定答鈴",  "");
                songList.add(s); 
            }
        }
     
        SongAdapter songAdapater = new SongAdapter(getContext(), R.layout.song_list_item, songList);
    	SettingsActivity.musiclist.setAdapter(songAdapater);
        // MainPageActivity.musiclist.setFocusable(false);
    	if(count>0){
        	SettingsActivity.musiclist.setOnItemClickListener(new OnItemClickListener(){

     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     					long arg3) {
     				// TODO Auto-generated method stub
     			          
     				SettingsActivity.startPlayer(arg2);
                  
     			}
             	
             });
    	}

     	
    }
    
    
    
    
    public void initialSettingView(Context context) {

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        
        // TODO: mStrings is from database!
    //	MainPageActivity.musiclist.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

        if(MainUrl.getBasicRing()!=null)
        	daystart = MainUrl.getBasicRing().length+1;
        else
        	daystart = 2;
        if(MainUrl.getDayRing()!=null)
        	nightstart = daystart+MainUrl.getDayRing().length+1;
        else
        	nightstart = daystart+2;
        if(MainUrl.getNightRing()!=null)
        	weekendstart = nightstart+MainUrl.getNightRing().length+1;
        else
        	weekendstart = nightstart+2;
        
      //  Log.i(myTag, "basicstart="+basicstart+"  daystart="+daystart+"  nightstart="+nightstart+"  weekendstart="+weekendstart);
        SettingsActivity.musiclist.setAdapter(setListContent(context));
       // MainPageActivity.musiclist.setFocusable(false);
    	SettingsActivity.musiclist.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
                   Song song = null;
                   if(arg2!=0 && arg2!=daystart && arg2!=nightstart && arg2!=weekendstart){
                      if(arg2<daystart){
                    	  if(MainUrl.getBasicRing()!=null){
                	         song = MainUrl.getBasicRing()[arg2-1];
                    	  }else
                    		 return;
                      }else if(arg2>daystart && arg2<nightstart){
                    	  if(MainUrl.getDayRing()!=null){
                    	     song = MainUrl.getDayRing()[arg2-daystart];
                    	  }else
                    		 return;
                      }else if(arg2>daystart && arg2<nightstart){
                    	  if(MainUrl.getNightRing()!=null){
                    	     song = MainUrl.getNightRing()[arg2-nightstart];
                	      }else
                		     return;
                      }else{
                    	  if(MainUrl.getWeekendRing()!=null){
                    	    song = MainUrl.getWeekendRing()[arg2-weekendstart];
                	      }else
                		     return;
                      } 	  
                    	  
				      SettingsActivity.startPlayer(arg2);
                   }
			}
        	
        });
    	
    }


    
    private SongAdapter setListContent(Context context){
        
    	

		    SongData s = null;
		    songList = new ArrayList<SongData>();
            s = new SongData("", "基本音",  "");
            songList.add(s); 
		    if(MainUrl.getBasicRing()!=null){
		        for (int i = 0; i < MainUrl.getBasicRing().length; i++) {
	               s = new SongData(MainUrl.getBasicRing()[i].getImgArtistUrl(), MainUrl.getBasicRing()[i].getSongName(),  MainUrl.getBasicRing()[i].getSinger());
	               songList.add(s); 
	        
		        }
            }else{   
                 s = new SongData("", "尚未設定答鈴",  "");
                 songList.add(s); 
            }
		    
            s = new SongData("", "白天音",  "");
            songList.add(s); 
		    if(MainUrl.getDayRing()!=null){
		        for (int i = 0; i < MainUrl.getDayRing().length; i++) {
	               s = new SongData(MainUrl.getDayRing()[i].getImgArtistUrl(), MainUrl.getDayRing()[i].getSongName(),  MainUrl.getDayRing()[i].getSinger());
	               songList.add(s); 
	        
		        }
            }else{   
                 s = new SongData("", "尚未設定答鈴",  "");
                 songList.add(s); 
            }
		    
            s = new SongData("", "晚上音",  "");
            songList.add(s); 
		    if(MainUrl.getNightRing()!=null){
		        for (int i = 0; i < MainUrl.getNightRing().length; i++) {
	               s = new SongData(MainUrl.getNightRing()[i].getImgArtistUrl(), MainUrl.getNightRing()[i].getSongName(),  MainUrl.getNightRing()[i].getSinger());
	               songList.add(s); 
	        
		        }
            }else{   
                 s = new SongData("", "尚未設定答鈴",  "");
                 songList.add(s); 
            }
		    
            s = new SongData("", "週末音",  "");
            songList.add(s); 
		    if(MainUrl.getWeekendRing()!=null){
		        for (int i = 0; i < MainUrl.getWeekendRing().length; i++) {
	               s = new SongData(MainUrl.getWeekendRing()[i].getImgArtistUrl(), MainUrl.getWeekendRing()[i].getSongName(),  MainUrl.getWeekendRing()[i].getSinger());
	               songList.add(s); 
	        
		        }
            }else{   
                 s = new SongData("", "尚未設定答鈴",  "");
                 songList.add(s); 
            }
		    
		    
            SongAdapter songAdapater = new SongAdapter(getContext(), R.layout.song_list_item, songList);
               

       return songAdapater;
    }  	

    
}
