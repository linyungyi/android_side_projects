package com.music.parser;

import java.net.URL;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;
import android.widget.TextView;

public class ParsingXML_20091019 {

	private final String MY_DEBUG_TAG = "ParsingXML";
	private TextView tv;

	/** Called when the activity is first created. */

	public ParsingXML_20091019 () {};
    /*
	private void printParsedInfo(ParserHandler handler, String type) {
		String tmpString = "";
		if (type.equals("song")) {

			// Our ParserHandler now provides the parsed data to us. 
			ListIterator<Song> songsListIterator = handler.getParsedSongsListIterator();

			// print song information
			while (songsListIterator.hasNext()) {

				Song aSong = songsListIterator.next();
				tmpString = tmpString + "ºq¦±->" + aSong.getSongName() + "\n";
				tmpString = tmpString + "ºq¤â->" + aSong.getSinger() + "\n";
				tmpString = tmpString + "CP->" + aSong.getCpname() + "\n";
				tmpString = tmpString + "ID->" + aSong.getProductid() + "\n";
				tmpString = tmpString + "Wav->" + aSong.getWavUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if (type.equals("rank") || type.equals("menu") || type.equals("songsearch")) {
			Hashtable<String, String> rankHashTable = handler.getParsedHashTable();

			Enumeration<String> title = rankHashTable.keys();

			// print ranklist information
			for (int i = 0; i < rankHashTable.size(); i++) {
				String key = (String) title.nextElement();

				tmpString = tmpString + "title->" + key + "\n";
				tmpString = tmpString + "url->" + rankHashTable.get(key) + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if (type.equals("activity")) {
			ListIterator<com.music.parser.data.Activity> activitiesListIterator = handler.getParsedActivitysListIterator();

			// print activity information
			while (activitiesListIterator.hasNext()) {

				com.music.parser.data.Activity aActivity = activitiesListIterator.next();
				tmpString = tmpString + "title->" + aActivity.getName() + "\n";
				tmpString = tmpString + "date->" + aActivity.getBeginDate() + "~" + aActivity.getEndDate() + "\n";
				tmpString = tmpString + "content->" + aActivity.getContent() + "\n";
				tmpString = tmpString + "Url->" + aActivity.getSongsUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if (type.equals("search")) {
			Hashtable<String, Vector<Search>> searchHashTable = handler.getParsedSearchHashTable();
			Enumeration<String> title = searchHashTable.keys();
			
			//print
			for (int i = 0; i < searchHashTable.size(); i++) {
				String key = (String) title.nextElement();
				tmpString = tmpString + "title->" + key + "\n";

				Vector<Search> tmpV = searchHashTable.get(key);
				for (int j = 0; j < tmpV.size(); j++) {
					Search aSearch = tmpV.get(j);
					tmpString = tmpString + "keyword->" + aSearch.getKeyword() + "\n";
					tmpString = tmpString + "url->" + aSearch.getUrl() + "\n\n";				
				}
				tmpString = tmpString + "------------------------------------------\n";
			}
		}else if(type.equals("album")){
			
			// Our ParserHandler now provides the parsed data to us. 
			ListIterator<Album> albumsListIterator = handler.getParsedAlbumsListIterator();

			// print song information
			while (albumsListIterator.hasNext()) {

				Album aAlbum = albumsListIterator.next();
				tmpString = tmpString + "Name->" + aAlbum.getName() + "\n";
				tmpString = tmpString + "Singer->" + aAlbum.getArtist() + "\n";
				tmpString = tmpString + "ListUrl->" + aAlbum.getListUrl() + "\n";
				tmpString = tmpString + "CP->" + aAlbum.getPublisher() + "\n";
				tmpString = tmpString + "ImgUrl->" + aAlbum.getImgArtistUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if(type.equals("musicbox")){
			
			// Our ParserHandler now provides the parsed data to us. 
			ListIterator<MusicBox> musicboxListIterator = handler.getParsedMusicBoxListIterator();

			// print song information
			while (musicboxListIterator.hasNext()) {

				MusicBox aMusicBox = musicboxListIterator.next();
				tmpString = tmpString + "Name->" +aMusicBox.getMusicName() + "\n";
				tmpString = tmpString + "Content->" + aMusicBox.getContent() + "\n";
				tmpString = tmpString + "Id->" + aMusicBox.getMusicId() + "\n";
				tmpString = tmpString + "IconUrl->" + aMusicBox.getSmallIconUrl() + "\n";
				tmpString = tmpString + "Url->" + aMusicBox.getListUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		tv.setText(tmpString);
	}
	*/

	public ParserHandler parsingData(String xmlUrl) {

		// Create a new ParserHandler
		ParserHandler parserHandler = new ParserHandler();

		try {

			/* Create a URL we want to load some xml-data from. */
			URL url = new URL(xmlUrl);
			/* Get a SAXParser from the SAXPArserFactory. */
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
			//tv.setText("Error: " + e.getMessage());
			Log.e(MY_DEBUG_TAG, "ParsingXmlError", e);
		}

		// Song aSong = parserHandler.getParsedSongsListIterator().next();
		// Log.d("test","test->"+aSong.getWavUrl());

		return parserHandler;

	}
}
