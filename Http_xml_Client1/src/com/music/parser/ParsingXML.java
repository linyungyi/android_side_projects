package com.music.parser;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.music.parser.data.Album;
import com.music.parser.data.MusicBox;
import com.music.parser.data.Search;
import com.music.parser.data.Song;
import android.util.Log;

public class ParsingXML {

	private final String MY_DEBUG_TAG = "ParsingXML";
	private InputStream ips;

	public ParsingXML(InputStream aIps) {
		this.ips = aIps;

	}
	public ParsingXML() {};
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
	public ParserHandler parsingData() {

		// Create a new ParserHandler
		ParserHandler parserHandler = new ParserHandler();

		try {

			/* Create a URL we want to load some xml-data from. */
			// URL url = new URL(xmlUrl);

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/* Apply a new ContentHandler to the XML-Reader */
			xr.setContentHandler(parserHandler);

			/* Parse the xml-data from our URL. */
			// xr.parse(new InputSource(url.openStream()));
			xr.parse(new InputSource(ips));

		} catch (Exception e) {
			
			Log.e(MY_DEBUG_TAG, "ParsingXmlError", e);
		}	
		
		return parserHandler;

	}

	public String printParsedInfo(ParserHandler handler, String type) {
		String tmpString = "";
		if (type.equals("song")) {

			/* Our ParserHandler now provides the parsed data to us. */

			Song[] songsArray = handler.getParsedSongsArray();
			// print song information

			for (int i = 0; i < songsArray.length; i++) {
				tmpString = tmpString + "ºq¦±->" + songsArray[i].getSongName() + "\n";
				tmpString = tmpString + "ºq¤â->" + songsArray[i].getSinger() + "\n";
				tmpString = tmpString + "CP->" + songsArray[i].getCpname() + "\n";
				tmpString = tmpString + "ID->" + songsArray[i].getProductid() + "\n";
				tmpString = tmpString + "Wav->" + songsArray[i].getWavUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if (type.equals("rank") || type.equals("menu") || type.equals("songsearch")) {

			HashMap<String, String> aHashMap = handler.getParsedHashMap();
			String[] keysArray = handler.getKeysSequence();

			// print list information
			for (int i = 0; i < keysArray.length; i++) {
				String key = keysArray[i];
				tmpString = tmpString + "title->" + key + "\n";
				if (aHashMap.get(key) != null)
					tmpString = tmpString + "url->" + aHashMap.get(key) + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if (type.equals("activity")) {

			com.music.parser.data.Activity[] activitiesArray = handler.getParsedActivitiesArray();
			// print activity information

			for (int i = 0; i < activitiesArray.length; i++) {

				tmpString = tmpString + "title->" + activitiesArray[i].getName() + "\n";
				tmpString = tmpString + "date->" + activitiesArray[i].getBeginDate() + "~" + activitiesArray[i].getEndDate() + "\n";
				tmpString = tmpString + "content->" + activitiesArray[i].getContent() + "\n";
				tmpString = tmpString + "Url->" + activitiesArray[i].getSongsUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if (type.equals("search")) {

			HashMap<String, Search[]> aHashMap = handler.getParsedSearchHashMap();
			String[] KeysArray = handler.getKeysSequence();

			// print
			for (int i = 0; i < KeysArray.length; i++) {

				String key = KeysArray[i];
				tmpString = tmpString + "title->" + key + "\n";

				if (aHashMap.get(key) != null) {
					Search[] tmpS = aHashMap.get(key);
					for (int j = 0; j < tmpS.length; j++) {

						tmpString = tmpString + "keyword->" + tmpS[j].getKeyword() + "\n";
						tmpString = tmpString + "keyword->" + tmpS[j].getTitle() + "\n";
						tmpString = tmpString + "url->" + tmpS[j].getUrl() + "\n\n";
					}
					tmpString = tmpString + "------------------------------------------\n";
				}
			}
		} else if (type.equals("profile")) {

			/* Our ParserHandler now provides the parsed data to us. */

			HashMap<String, Song[]> profileHashMap = handler.getParsedProfileHashMap();
			String[] keysArray = handler.getKeysSequence();

			// print song information
			tmpString = tmpString + "member->" + handler.getRingtonMember() + "\n";
			Log.d("profileHahsMap", "size->" + profileHashMap.size());
			Log.d("keysArray", "length->" + keysArray.length);

			for (int i = 0; i < keysArray.length; i++) {

				String key = keysArray[i];

				tmpString = tmpString + "title->" + key + "\n";

				if (profileHashMap.get(key) != null) {

					Song[] tmpS = profileHashMap.get(key);
					for (int j = 0; j < tmpS.length; j++) {
						tmpString = tmpString + "ºq¦±->" + tmpS[j].getSongName() + "\n";
						tmpString = tmpString + "ºq¤â->" + tmpS[j].getSinger() + "\n";
						tmpString = tmpString + "CP->" + tmpS[j].getCpname() + "\n";
						tmpString = tmpString + "ID->" + tmpS[j].getProductid() + "\n";
						tmpString = tmpString + "Wav->" + tmpS[j].getWavUrl() + "\n";
						tmpString = tmpString + "\n";
					}
					tmpString = tmpString + "------------------------------------------\n";
				}
			}

		} else if (type.equals("album")) {

			/* Our ParserHandler now provides the parsed data to us. */

			Album[] albumsArray = handler.getParsedAlbumsArray();
			// print song information

			for (int i = 0; i < albumsArray.length; i++) {

				tmpString = tmpString + "Name->" + albumsArray[i].getName() + "\n";
				tmpString = tmpString + "Singer->" + albumsArray[i].getArtist() + "\n";
				tmpString = tmpString + "ListUrl->" + albumsArray[i].getListUrl() + "\n";
				tmpString = tmpString + "CP->" + albumsArray[i].getPublisher() + "\n";
				tmpString = tmpString + "ImgUrl->" + albumsArray[i].getImgArtistUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		} else if (type.equals("musicbox")) {

			/* Our ParserHandler now provides the parsed data to us. */

			MusicBox[] musicboxArray = handler.getParsedMusicBoxiesArray();
			// print song information

			for (int i = 0; i < musicboxArray.length; i++) {

				tmpString = tmpString + "Name->" + musicboxArray[i].getMusicName() + "\n";
				tmpString = tmpString + "Content->" + musicboxArray[i].getContent() + "\n";
				tmpString = tmpString + "Id->" + musicboxArray[i].getMusicId() + "\n";
				tmpString = tmpString + "IconUrl->" + musicboxArray[i].getSmallIconUrl() + "\n";
				tmpString = tmpString + "Url->" + musicboxArray[i].getListUrl() + "\n";
				tmpString = tmpString + "------------------------------------------\n";
			}

		}
		
		return tmpString;
	}

}
