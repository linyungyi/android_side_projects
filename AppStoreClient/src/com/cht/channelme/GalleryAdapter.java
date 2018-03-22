package com.cht.channelme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;

import com.cht.channeldata.ChannelList;
import com.cht.channeldata.ThreadDownLoadControl;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class GalleryAdapter extends BaseAdapter{

	private static final int UPDATE_SETTING_SUCCESS = 0x0001;

    private ViewHolder holder = null;
    private ArrayList<ChannelList> list;
    private MyHandler uiHandler;
    private LayoutInflater mInflater;
    private static String URL = null;

    
    public GalleryAdapter(Context context, int rowResID,
    		ArrayList<ChannelList> arraylist) { 
        this.list = arraylist;
        holder = new ViewHolder();
        holder.channel_name = new TextView[arraylist.size()];
        holder.channel_icon = new ImageView[arraylist.size()];  
        uiHandler = new MyHandler();
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {                        
        return list.size();
    }
    public Object getItem(int position) {     
        return list.get(position);
    }

    public long getItemId(int position) {  
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) { 

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
    	if(convertView==null){
            convertView = mInflater.inflate(R.layout.mygallery, null);

            holder.channel_name[position] = (TextView) convertView.findViewById(R.id.channel_category_name);
            holder.channel_icon[position] = (ImageView) convertView.findViewById(R.id.channel_category_image);

    	}else{
    		holder = (ViewHolder)convertView.getTag();
    	}

        // Creates a ViewHolder and store references to the two children views
        // we want to bind data to.
        holder.channel_name[position] = (TextView) convertView.findViewById(R.id.channel_category_name);
        holder.channel_icon[position] = (ImageView) convertView.findViewById(R.id.channel_category_image);
        
        ChannelList channel = list.get(position);
        
        holder.channel_name[position].setText(channel.getChannel_Name());
       // holder.channel_icon[position].setText(channel.getChannel_Name());
        URL = channel.getBigPicUrl();
        File image = new File("/data/data/com.cht.channelme/files/List/"+new File(URL).getName());
        boolean isExists = false;
        
        if(image.exists()){
        	if(image.length()>1000){
        		isExists = true;
        	}
        }
        if(isExists){	
	        Bitmap bmpic = BitmapFactory.decodeFile(image.getAbsolutePath());
	        holder.channel_icon[position].setImageBitmap(bmpic);

        }else{
            new Thread(new Runnable()
            {
                public void run()
                {
             	   ImageOperations(URL,position);
                }
            }).start(); 
            
        }
        convertView.setTag(position);
        return convertView;
    }
    

    class ViewHolder {
        ImageView[] channel_icon;
        TextView[] channel_name;
    }    
    
    
	private void ImageOperations(String address,int pos) {
		String ImageTag = "ImageOperations";
		File pic = null;
	    File dir = null;
        String mainpath = "/data/data/com.cht.channelme/files";
    	dir = new File(mainpath+"/List");
    	pic = new File(mainpath+"/List/",new File(address).getName());
        if(!dir.exists())
        	dir.mkdirs();
        
        
        try{
          if(!pic.exists())
        	pic.createNewFile();
          else{
        	  pic.delete();
        	  pic.createNewFile();
          }

        }catch(Exception e){
        	Log.e("icon path",e.toString());
        }
        if(!ThreadDownLoadControl.ImageLoading.contains(pic.getAbsolutePath())){

        	try{
        		ThreadDownLoadControl.ImageLoading.add(pic.getAbsolutePath());
        	}catch(Exception e){
        		
        	}
        	
        	
    		InputStream is= null;
    		
    		try {
    		//	Log.i(ImageTag,"ImageOperations go:"+address);
    			//address = address.replaceAll("musicphone.emome.net", "10.1.14.109");
    			URL url = new URL(address);
    			URLConnection conn = url.openConnection();
    			conn.setReadTimeout(10000);
    			conn.connect();
    			is = conn.getInputStream();
    			FileOutputStream fos = new FileOutputStream(pic);
    			byte buf[] = new byte[128];
    			while(true){
    				int num = is.read(buf);
    				if(num<=0)
    					break;
    				else
    					fos.write(buf,0,num);
    			}
    			try{
    				fos.close();
    			}catch(Exception e){
    				
    			}
    		//	Log.i("pic", "pic "+pic.getAbsolutePath()+" finished");
    			//Log.i("pic", "url:"+URL);
    			Message m = new Message();
    			
    			m.what = UPDATE_SETTING_SUCCESS;
    			Bundle bundle = new Bundle();
    			bundle.putString("picpath", pic.getAbsolutePath());
    			bundle.putInt("pos", pos);
    			m.setData(bundle);
    			uiHandler.sendMessage(m);
    	    //		Bitmap bitmap = BitmapFactory.decodeStream(is);
    		//	holder.icon.setImageBitmap(bitmap); 
    		
    		} catch (IOException e) {
    			Log.e(ImageTag,"ImageOperations go e="+e.toString());
    			try{
    				pic.delete();
    				
    			}catch(Exception ee){
    				
    			}
    			
    			e.printStackTrace();
    		}finally{
    			
    			try{
    			  is.close();
    			}catch(Exception e){
    				
    			}
    			try{
      			  ThreadDownLoadControl.ImageLoading.remove(pic.getAbsolutePath());
      			}catch(Exception e){
      			  Log.e("image remove vector fail","image remove vector fail:"+e.toString());
      			}
    			is = null;
    		}
        }
	}
	
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
				File pic = null;
		        
		        try{
		        	pic = new File(bundle.getString("picpath"));
			        if(pic.exists()){
			        //	Log.i("pic","pos="+bundle.getInt("pos")+"  bundle.getString(picpath)="+bundle.getString("picpath")+"  pic.length="+pic.length());
				        Bitmap bmpic = BitmapFactory.decodeFile(bundle.getString("picpath"));
				        holder.channel_icon[bundle.getInt("pos")].setImageBitmap(null);
				        holder.channel_icon[bundle.getInt("pos")].setImageBitmap(bmpic);
			        }else
			        	Log.i("pic"," not found bundle.getString(picpath)="+bundle.getString("picpath"));
		        }catch(Exception e){
		        	Log.i("1111", "pic set error:"+e.toString());
		        }


		       
		    
				break;

			}
			super.handleMessage(msg);
		}
	}  	
   
}
