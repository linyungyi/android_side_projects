package com.cht.android.music;

class Song {
    
    public int myIcon = 0;
    public String SongName = null;
    public String SongArtist = null;
    
    Song(int resId, String Name, String Artist)
    {
        //myIcon.setImageResource(resId);
        myIcon = resId;
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
    
    public int getIcon()
    {
        return R.drawable.icon;
    }
    
}