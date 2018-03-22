package com.cht.android.music;

public class ActivityContent {
    
    static private String[] ActivityTitle = null;
    static private String[] ActivityDescription = null;
    static private String[] ActivityImageUrl = null; 
    static private String[] ActivitySongListUrl = null; 
    static private String[] ActivityBeginTime = null; 
    static private String[] ActivityEndTime = null; 

    ActivityContent()
    {
        //myIcon.setImageResource(resId);
   
    }
    
    static public void setActivityTitle(String[] txt)
    {
    	ActivityTitle = txt;
    }
    
    static public void setActivitySongListUrl(String[] txt)
    {
    	ActivitySongListUrl = txt;
    }    
    
    static public void setActivityDescription(String[] txt)
    {
    	ActivityDescription = txt;
    }    
    
    static public void setActivityImageUrl(String[] txt)
    {
    	ActivityImageUrl = txt;
    }    
    
    static public void setActivityBeginTime(String[] txt)
    {
    	ActivityBeginTime = txt;
    }    
    
    
    static public void setActivityEndTime(String[] txt)
    {
    	ActivityEndTime = txt;
    }
    //   get method....................
    
    static public String[] getActivityTitle()
    {
    	return ActivityTitle;
    }
    
    static public String[] getActivitySongListUrl()
    {
    	return ActivitySongListUrl;
    }    
    
    static public String[] getActivityDescription()
    {
    	return ActivityDescription;
    }    
    
    static public String[] getActivityImageUrl()
    {
    	return ActivityImageUrl;
    }    
    
    static public String[] getActivityBeginTime()
    {
    	return ActivityBeginTime;
    }    
    
    
    static public String[] getActivityEndTime()
    {
    	return ActivityEndTime;
    }
}