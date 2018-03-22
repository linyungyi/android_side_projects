package com.cht.android.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter{

    private ArrayList<SongData> MySongList;
    private LayoutInflater mInflater;

    public SongAdapter(Context context, int rowResID,
            ArrayList<SongData> songList ) { 
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

        SongData mySong = MySongList.get(position);
        
        // Bind the data efficiently with the holder.
        holder.name.setText(mySong.getName());
        holder.Artist.setText(mySong.getArtist());
        if(mySong.getIcon().compareTo("")!=0)
          holder.icon.setImageResource(R.drawable.default_artist);
        else
          holder.icon.setImageBitmap(null);  	
      //  String myIconURL = "http://irbt.emome.net/audiobank/RBT/pic/41/27/412742.gif";
      // Bitmap bm = ImageOperations(mySong.getIcon(),"image.jpg");
        //Bitmap bm = ImageOperations(mySong.getIcon(),"image.jpg");
       // Bitmap bm = null;
		//holder.icon.setImageBitmap(bm);        

        return convertView;
    }
    
    class ViewHolder {
        ImageView icon;
        TextView name;
        TextView Artist;
    }
    
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
