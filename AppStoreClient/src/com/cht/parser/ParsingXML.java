package com.cht.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.cht.channeldata.ChannelItem;
import com.cht.channeldata.ChannelList;

import android.util.Log;

public class ParsingXML {

	private final static String MY_DEBUG_TAG = "ParsingXML";
	private InputStream ips;
	
	String mainpath = "/data/data/com.cht.channelme/files/";
    private String myTag = "ParsingXML";
    private boolean checked = true;
	//public static void main(String[] args) {

		//ParserHandler handler = parsingData("http://musicphone.emome.net/MAS/DoTask?Task=getProductPrice&xsl=pcIni.xsl&id=011512&sourcetype=05");
		//String s=printParsedInfo(handler,"songPrice");
		//System.out.println(s);
	//}

	public ParsingXML(InputStream aIps) {
		this.ips = aIps;

	}

	public ParsingXML() {
	};

	public ParserHandler parsingData(String xmlUrl,String Type) {

		// Create a new ParserHandler
		ParserHandler parserHandler = new ParserHandler();
		
		try {
		/*
		File localFile = null;
		File localTempFile = null;
		

			if(Type.compareTo("List")==0){
				localFile = new File(mainpath+"List/List.xml");
				if(localFile.exists()){
					localTempFile = new File(mainpath+"List/temp.xml");
					if(localTempFile.exists())
						localTempFile.delete();
					localTempFile.createNewFile();
				}else{
					localTempFile = new File(mainpath+"List/List.xml");
					localTempFile.createNewFile();
				}
			}else{
				localFile = new File(mainpath+"Item/Item-"+xmlUrl.substring(xmlUrl.indexOf("ChannelId=")+10)+".xml");
				if(localFile.exists()){
					localTempFile = new File(mainpath+"Item/temp.xml");
					if(localTempFile.exists())
						localTempFile.delete();
					localTempFile.createNewFile();
				}else{
					localTempFile = new File(mainpath+"Item/temp.xml");
					localTempFile.createNewFile();
				}
			}
			
		
			URL url = new URL(xmlUrl);
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(10000);
			conn.connect();
			ips = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(localTempFile);
			byte buf[] = new byte[128];
			while(true){
				int num = ips.read(buf);
				if(num<=0)
					break;
				else
					fos.write(buf,0,num);
			}
			try{
				fos.close();
			}catch(Exception e){
				
			}
			
			parsingfile(localTempFile, Type);
			*/
			/* Get a SAXParser from the SAXPArserFactory. */
			URL url = new URL(xmlUrl);
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/* Apply a new ContentHandler to the XML-Reader */
			xr.setContentHandler(parserHandler);

			/* Parse the xml-data from our URL. */
			xr.parse(new InputSource(url.openStream()));
            
			

		} catch (Exception e) {
			/* Display any Error to the GUI. */
			// tv.setText("Error: " + e.getMessage());
			Log.e(MY_DEBUG_TAG, "ParsingXmlError", e);
		}

		// Song aSong = parserHandler.getParsedSongsListIterator().next();
		// Log.d("test","test->"+aSong.getWavUrl());

		return parserHandler;

	}

	public ParserHandler parsingData() {

		// Create a new ParserHandler
		ParserHandler parserHandler = new ParserHandler();

		try {

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/* Apply a new ContentHandler to the XML-Reader */
			xr.setContentHandler(parserHandler);

			/* Parse the xml-data from our URL. */
			xr.parse(new InputSource(ips));

		} catch (Exception e) {

			Log.e(MY_DEBUG_TAG, "ParsingXmlError", e);
		}

		return parserHandler;

	}

	public ParserHandler parsingfile(File file,String Type) {

		

		//File file = new File(mainpath + "/xml/", new File(fileName).getName());

		// Create a new ParserHandler
		ParserHandler parserHandler = new ParserHandler();

		try {
			FileInputStream fips = new FileInputStream(file);

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/* Apply a new ContentHandler to the XML-Reader */
			xr.setContentHandler(parserHandler);

			/* Parse the xml-data from our URL. */

			xr.parse(new InputSource(fips));

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(MY_DEBUG_TAG, "ParsingXmlError", e);
		}

		return parserHandler;

	}

	public String printParsedInfo(ParserHandler handler, String type) {
		String tmpString = "";
		
		if(type.compareTo("List")==0){
			HashMap<String, Object[]> channel = handler.parserChannelList();
			Object[] category_id = channel.get("id");
			Object[] category_name = channel.get("name");
			Object[] lists = channel.get("list");
			
			for(int i=0;i<category_id.length;i++){
			//	Log.i(myTag, "channel_category_id="+category_id[i].toString());
				    for(int j=0;j<lists.length;j++){
				    	ChannelList list = (ChannelList)lists[j];
						Log.i(myTag, "channel_id="+list.getChannel_ID());
						Log.i(myTag, "channel_name="+list.getChannel_Name());
						Log.i(myTag, "price="+list.getPrice());
						Log.i(myTag, "small pic url="+list.getSmallPicUrl());
						Log.i(myTag, "small pic version="+list.getSmallPicVersion());
						Log.i(myTag, "big pic url="+list.getBigPicUrl());
						Log.i(myTag, "big pic version="+list.getBigPicVersion());
						Log.i(myTag, "description="+list.getDescription());
						Log.i(myTag, "entity version="+list.getEntityVersion());
						Log.i(myTag, "channel_template="+list.getChannel_Template());
						Log.i(myTag, "channel_subscriptiontype="+list.getSubscriptionType());
						Log.i(myTag, "channel_template="+list.getChannel_Template());
						Log.i(myTag, "traildays="+list.getTrialDays());
						Log.i(myTag, "channel_url="+list.getChannelUrl());
						Log.i(myTag, "channel_category="+list.getCategory());
						Log.i(myTag, "subscribed="+list.isSubsricbed());


						Log.i(myTag, "---------------------------------------------");	
				    }
			}
		}else{
			HashMap<String, Object[]> item = handler.parserChannelItem();
			Object[] channel_id = item.get("channelid");
			Object[] items = item.get("item");
			
			for(int i=0;i<channel_id.length;i++){
				Log.i(myTag, "channel_category_id="+channel_id[i].toString());
				    for(int j=0;j<items.length;j++){
				    	ChannelItem list = (ChannelItem)items[j];
						Log.i(myTag, "item_id="+list.getItemID());
						Log.i(myTag, "item_title="+list.getPropTitle());
						Log.i(myTag, "Thumbnail_Url="+list.getProp_Thumbnail_Url());
						Log.i(myTag, "ImageUrl="+list.getPropImageUrl());
						Log.i(myTag, "getDescription="+list.getDescription());
					

						Log.i(myTag, "---------------------------------------------");	
				    }
			}
		}

		
		
		
		return tmpString;
	}

}
