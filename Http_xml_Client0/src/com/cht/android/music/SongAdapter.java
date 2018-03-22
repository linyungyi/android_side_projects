package com.cht.android.music;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter{

    private ArrayList<Song> MySongList;
    private LayoutInflater mInflater;

    public SongAdapter(Context context, int rowResID,
            ArrayList<Song> songList ) { 
        this.MySongList = songList;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {                        
        return MySongList.size();
    }

    public Object getItem(int position) {     
        return MySongList.get(position);
    }

    public long getItemId(int position) {  
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) { 
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.song_list_item, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.TSongName);
            holder.icon = (ImageView) convertView.findViewById(R.id.ISongIcon);
            holder.Artist = (TextView) convertView.findViewById(R.id.TArtistName);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        Song mySong = MySongList.get(position);
        
        // Bind the data efficiently with the holder.
        holder.name.setText(mySong.getName());
        holder.Artist.setText(mySong.getArtist());
        holder.icon.setImageResource(mySong.getIcon());

        return convertView;
    }
    
    class ViewHolder {
        ImageView icon;
        TextView name;
        TextView Artist;
    }
    
}
