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

public class ActivitiesListAdapter extends BaseAdapter{

    private String[] MyActivityList;
    private String[] MyActivityImageList;

    private LayoutInflater mInflater;

    public ActivitiesListAdapter(Context context, int rowResID,
            String[] title,String[] image) { 
        this.MyActivityList = title;
        this.MyActivityImageList = image;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {                        
        return MyActivityList.length;
    }

    public Object getItem(int position) {     
        return MyActivityList[position];
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
            convertView = mInflater.inflate(R.layout.activity_list, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.ActivityListTitleItem);
            holder.icon = (ImageView) convertView.findViewById(R.id.ActivityListIconItem);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        
        // Bind the data efficiently with the holder.
       // Log.i("ActivitiesListAdapter","MyActivityImageList[pos]="+MyActivityImageList[position]);
        holder.title.setText(MyActivityList[position]);
        
        Bitmap bm = ImageOperations(MyActivityImageList[position],"image.jpg");
        holder.icon.setImageBitmap(bm);     
      //  String myIconURL = "http://irbt.emome.net/audiobank/RBT/pic/41/27/412742.gif";
      // Bitmap bm = ImageOperations(mySong.getIcon(),"image.jpg");
        //Bitmap bm = ImageOperations(mySong.getIcon(),"image.jpg");
       // Bitmap bm = null;
		//holder.icon.setImageBitmap(bm);        

        return convertView;
    }
    
    class ViewHolder {
        ImageView icon;
        TextView title;
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
