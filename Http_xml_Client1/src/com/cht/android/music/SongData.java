package com.cht.android.music;

class SongData {
    
    public String myIcon = null;
    public String SongName = null;
    public String SongArtist = null;
    
    SongData(String myIconUrl, String Name, String Artist)
    {
        //myIcon.setImageResource(resId);
        myIcon = myIconUrl;
        SongName = Name;
        SongArtist = Artist;
    }
    
    public String getName()
    {
        return SongName;
    }
    
    public String getArtist()
    {
        return SongArtist;
    }
    
    public String getIcon()
    {
        return myIcon;
    }
    
}