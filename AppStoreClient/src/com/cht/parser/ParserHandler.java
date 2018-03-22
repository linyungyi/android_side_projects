package com.cht.parser;

import java.util.HashMap;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cht.channeldata.ChannelItem;
import com.cht.channeldata.ChannelList;


import android.util.Log;

public class ParserHandler extends DefaultHandler {

	// ===========================================================
	// Fields
	// ===========================================================

	private String currentElementValue = "";
	private String parserType = new String();
	private Vector<ChannelItem> tmpChannelItem;
	private Vector<ChannelList> tmpChannelList;
	private ChannelList nowList;
	private ChannelItem nowItem;
	private Vector<String> CategoryID = new Vector<String>();
	private Vector<String> CategoryName = new Vector<String>();
	private Vector<String> ChannelID = new Vector<String>();

	private HashMap<String, Object[]> channel = new HashMap<String, Object[]>();
	private HashMap<String, Object[]> item = new HashMap<String, Object[]>();

	private boolean skipTag = false;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public HashMap<String, Object[]> parserChannelList(){
		return channel;
	}
	
	public HashMap<String, Object[]> parserChannelItem(){
		return item;
	}
	
	// public ListIterator<Song> getParsedSongsListIterator() {
	
	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void startDocument() throws SAXException {
		// this.myParsedExampleDataSet = new ParsedExampleDataSet();
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	/**
	 * Gets be called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

		// start <tag>
   //--------list Start------------------------------------------		
        if(localName.equalsIgnoreCase("List")){
        	channel = new HashMap<String, Object[]>();
        	tmpChannelList = new Vector<ChannelList>();
        	CategoryID = new Vector<String>();
        	CategoryName = new Vector<String>();
        }else if(localName.equalsIgnoreCase("Channel_category")){
        	
        	CategoryID.add(atts.getValue("id"));
        	CategoryName.add(atts.getValue("name"));
        	
        }else if(localName.equalsIgnoreCase("Channel")){
        	
        	nowList = new ChannelList();
        }else if(localName.equalsIgnoreCase("smallpic")){
        	nowList.setSmallPicVersion(Integer.parseInt(atts.getValue("version")));
        }else if(localName.equalsIgnoreCase("bigpic")){
        	nowList.setBigPicVersion(Integer.parseInt(atts.getValue("version")));
        }
      //------item start------------------------------------------
        else if(localName.equalsIgnoreCase("ChannelDetail")){
        	tmpChannelItem = new Vector<ChannelItem>();
        	item = new HashMap<String, Object[]>();
        	ChannelID = new Vector<String>();
        	ChannelID.add(atts.getValue("id"));
        }else if(localName.equalsIgnoreCase("item")){
        	nowItem = new ChannelItem(); 
        	nowItem.setItemID(atts.getValue("id"));
        	
        }else if(localName.equalsIgnoreCase("prop_thumbnail")){
        	
        	nowItem.setProp_Thumbnail_Url(atts.getValue("url"));
        }else if(localName.equalsIgnoreCase("prop_image")){
        	
        	nowItem.setPropImageUrl(atts.getValue("url"));
        }		
        
        
        
        
		
		Log.d("ParsringXML.startElement", "ElementName->" + localName);
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {

		String tmpString = new String(ch, start, length);
		currentElementValue = currentElementValue + tmpString;
		Log.d("ParsringXML", "currentElementValue->" + currentElementValue);
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        
        if(localName.equalsIgnoreCase("List")){
        	
        	channel.put("id", CategoryID.toArray());
        	channel.put("name", CategoryName.toArray());
        	channel.put("list", tmpChannelList.toArray());

        }else if(localName.equalsIgnoreCase("Channel")){
        	tmpChannelList.add(nowList);
        }else if(localName.equalsIgnoreCase("channel_id")){
        	
        	nowList.setChannel_ID(currentElementValue);
        }else if(localName.equalsIgnoreCase("channel_name")){
        	
        	nowList.setChannel_Name(currentElementValue);
        }else if(localName.equalsIgnoreCase("channel_price")){
        	
        	nowList.setPrice(currentElementValue);
        }else if(localName.equalsIgnoreCase("channel_template")){
        	
        	nowList.setChannel_Template(Integer.parseInt(currentElementValue));        	
        }else if(localName.equalsIgnoreCase("smallpic")){
        	
        	nowList.setSmallPicUrl(currentElementValue);
        }else if(localName.equalsIgnoreCase("bigpic")){
        	
        	nowList.setBigPicUrl(currentElementValue);
        }else if(localName.equalsIgnoreCase("desc")){
        	
        	nowList.setDescription(currentElementValue);
        }else if(localName.equalsIgnoreCase("entity_version")){
        	
        	nowList.setEntityVersion(Integer.parseInt(currentElementValue));
        }else if(localName.equalsIgnoreCase("channel_template")){
        	
        	nowList.setEntityVersion(Integer.parseInt(currentElementValue));
        }else if(localName.equalsIgnoreCase("channel_subsrciptiontype")){
        	
        	nowList.setSubscriptionType(currentElementValue);
        }else if(localName.equalsIgnoreCase("traildays")){
        	
        	nowList.setTrialDays(Integer.parseInt(currentElementValue));   
        }else if(localName.equalsIgnoreCase("channel_url")){
        	
        	nowList.setChannelUrl(currentElementValue);
        }else if(localName.equalsIgnoreCase("channel_category")){
        	
        	nowList.setCategory(currentElementValue);        	
        }else if(localName.equalsIgnoreCase("subscribed")){
        	
        	nowList.setSubsricbed(Boolean.getBoolean(currentElementValue));
        }
        //---------------item end---------------------------------
        else if(localName.equalsIgnoreCase("ChannelDetail")){
        	
        	item.put("channelid", ChannelID.toArray());
        	item.put("item", tmpChannelItem.toArray());

        }else if(localName.equalsIgnoreCase("item")){
        	
        	tmpChannelItem.add(nowItem);

        }else if(localName.equalsIgnoreCase("prop_title")){
        	
        	nowItem.setPropTitle(currentElementValue);

        }else if(localName.equalsIgnoreCase("prop_desc")){
        	
        	nowItem.setDescription(currentElementValue);

        }
        
        
        
		currentElementValue = "";
	}
}
