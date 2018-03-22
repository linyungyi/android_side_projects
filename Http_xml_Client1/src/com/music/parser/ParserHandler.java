package com.music.parser;

import java.util.HashMap;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.music.parser.data.Activity;
import com.music.parser.data.Album;
import com.music.parser.data.MusicBox;
import com.music.parser.data.Song;
import com.music.parser.data.Search;

import android.util.Log;

public class ParserHandler extends DefaultHandler {

	// ===========================================================
	// Fields
	// ===========================================================

	private String currentElementValue="";
	private String parserType = new String();
	private boolean profile = false;
	private String[] keysSequence;
	private Vector<String> tmpKeys;
	private boolean skipTag=false;

	// song
	private Song[] parsedSongsArray;
	private Vector<Song> tmpSongsVector;
	private Song currentSong;

	// for menu
	// private Hashtable<String, String> parsedHashTable;
	private HashMap<String, String> parsedHashMap;

	// Activity
	private Activity currentActivity;
	private Vector<Activity> tmpActivitiesVector;
	private Activity[] parsedActivitiesArray;

	// Search
	private Vector<Search> tmpSearchVector;
	private Search currentSearch;
	private HashMap<String, Search[]> parsedSearchHashMap;
	private String tmptitle = "";

	// rington
	private String ringtonMember;
	// private Hashtable<String, Vector<Song>> parsedProfileHashTable;
	private HashMap<String, Song[]> parsedProfileHashMap;

	// album
	private Vector<Album> tmpAlbumsVector;
	private Album currentAlbum;
	private Album[] parsedAlbumsArray;

	// musicbox
	private Vector<MusicBox> tmpMusicBoxVector;
	private MusicBox currentMusicBox;
	private MusicBox[] parsedMusicBoxiesArray;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// public ListIterator<Song> getParsedSongsListIterator() {
	public Song[] getParsedSongsArray() {
		return parsedSongsArray;

	}

	public HashMap<String, String> getParsedHashMap() {

		return parsedHashMap;
	}

	public String[] getKeysSequence() {

		return keysSequence;
	}

	public Activity[] getParsedActivitiesArray() {

		return parsedActivitiesArray;
	}

	public HashMap<String, Search[]> getParsedSearchHashMap() {

		return parsedSearchHashMap;
	}

	public HashMap<String, Song[]> getParsedProfileHashMap() {

		return parsedProfileHashMap;
	}

	public String getRingtonMember() {

		return this.ringtonMember;
	}

	public Album[] getParsedAlbumsArray() {

		return parsedAlbumsArray;
	}

	public MusicBox[] getParsedMusicBoxiesArray() {

		return parsedMusicBoxiesArray;
	}

	

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
		if (localName.equals("iRecommendSong") || localName.equals("AlbumSongs") || localName.equals("MusicBoxSongs") || localName.equals("iRank")
				|| localName.equals("ActivitySongList") || localName.equals("iTopicSearch") || localName.equals("Rank") || localName.equals("Keyword")) {
			parserType = "songs";
			tmpSongsVector = new Vector<Song>();

		} else if (localName.equals("Activity")) {
			parserType = "activity";
			tmpActivitiesVector = new Vector<Activity>();

		} else if (localName.equals("AlbumPromotion")) {
			parserType = "album";
			tmpAlbumsVector = new Vector<Album>();

		} else if (localName.equals("iRankManagement") || localName.equals("gMusic") || localName.equals("gRecommend")) {
			parserType = "category";
			
			parsedHashMap = new HashMap<String, String>();
			tmpKeys = new Vector<String>();

		} else if (localName.equals("SongSearch")) {
			parserType = "search";			
			parsedSearchHashMap = new HashMap<String, Search[]>();
			tmpKeys = new Vector<String>();

		} else if (localName.equals("Profile")) {
			parserType = "profile";

			parsedProfileHashMap = new HashMap<String, Song[]>();
			tmpKeys = new Vector<String>();

		} else if (localName.equals("MusicBoxPromotion")) {
			parserType = "musicbox";
			this.tmpMusicBoxVector = new Vector<MusicBox>();
		}

		// different type parser init
		if (parserType.equals("album")) {
			if (localName.equals("gAlbumProfile")) {
				currentAlbum = new Album();
			}

		} else if (parserType.equals("songs")) {

			if (localName.equals("info")) {
				currentSong = new Song();
				// parserType = "songs";
			}

		} else if (parserType.equals("activity")) {

			if (localName.equals("subject")) {
				currentActivity = new Activity();
				String begintime = atts.getValue("begin_time");
				currentActivity.setBegin_time(begintime);
				String content = atts.getValue("content");
				currentActivity.setContent(content);
				String endtime = atts.getValue("end_time");
				currentActivity.setEndDate(endtime);
				String img = atts.getValue("img");
				currentActivity.setImgUrl(img);
				String name = atts.getValue("name");
				currentActivity.setName(name);
				String url = atts.getValue("url");
				currentActivity.setSongsUrl(url);
				this.tmpActivitiesVector.add(currentActivity);
				currentActivity = null;
			}

		} else if (parserType.equals("category")) {

			// Extract an Attribute
			if (localName.equals("Category")) {
				String title = atts.getValue("title");
				String url = atts.getValue("url");
			
				parsedHashMap.put(title, url);
				tmpKeys.add(title);
			}

		} else if (parserType.equals("search")) {
			if (localName.equals("Item")) {
				this.tmpSearchVector = new Vector<Search>();
				tmptitle = atts.getValue("title");

			} else if (localName.equals("list")) {

				currentSearch = new Search();
				String title = atts.getValue("title");
				currentSearch.setTitle(title);
				String keyword = atts.getValue("keyword");
				currentSearch.setKeyword(keyword);
				String url = atts.getValue("url");
				currentSearch.setUrl(url);
			}

		} else if (parserType.equals("profile") && !skipTag) {
			if (localName.equals("basic") || localName.equals("daylight") || localName.equals("night") || localName.equals("weekend")) {
				tmptitle = localName;
				tmpSongsVector = new Vector<Song>();

				profile = true;

				if(localName.equals("weekend")){
					
					skipTag=true;
				}
			} else if (localName.equals("info")) {

				
				parserType = "songs";
				currentSong = new Song();

			}
		} else if (parserType.equals("musicbox")) {

			if (localName.equals("MusicBoxProfile")) {

				this.currentMusicBox = new MusicBox();
			}
		}

		Log.d("ParsringXML.startElement", "ElementName->" + localName);
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {

		String tmpString=new String(ch, start, length);
		currentElementValue = currentElementValue+tmpString;
		Log.d("ParsringXML", "currentElementValue->" + currentElementValue);
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if (parserType.equals("songs")) {

			if (!profile) { // for rinton profile
				// final end <tag>
				if (localName.equals("iRecommendSong") || localName.equals("AlbumSongs") || localName.equals("MusicBoxSongs")
						|| localName.equals("Keyword") || localName.equals("iRank") || localName.equals("ActivitySongList") || localName.equals("iTopicSearch") || localName.equals("Rank")) {
					// parsedSongsListIterator = tmpSongsVector.listIterator();

					parsedSongsArray = new Song[tmpSongsVector.size()];
					parsedSongsArray = tmpSongsVector.toArray(parsedSongsArray);
					tmpSongsVector = null;
					return;

				}
			}

			if (localName.equals("info")) {
				tmpSongsVector.add(currentSong);
				if (profile) {

					parserType = "profile";
				}
			}

			// parse Song info
			if (localName.equals("song")) {
				currentSong.setSongName(currentElementValue);
			} else if (localName.equals("singer")) {
				currentSong.setSinger(currentElementValue);
			} else if (localName.equals("cpname")) {
				currentSong.setCpname(currentElementValue);
			} else if (localName.equals("productid")) {
				currentSong.setProductid(currentElementValue);
			} else if (localName.equals("price")) {
				currentSong.setPrice(currentElementValue);
			} else if (localName.equals("img_artist")) {
				currentSong.setImgArtistUrl(currentElementValue);
			} else if (localName.equals("img_album")) {
				currentSong.setImgAlbumUrl(currentElementValue);
			} else if (localName.equals("wav")) {
				currentSong.setWavUrl(currentElementValue);
			}

			// parse rington profile
		} else if (parserType.equals("profile")) {

			if (localName.equals("member")) {

				ringtonMember = currentElementValue;

			}

			if (localName.equals("basic") || localName.equals("daylight") || localName.equals("night") || localName.equals("weekend")) {
				Log.d("test222", "size->" + tmpSongsVector.size());
				if (tmpSongsVector.size() != 0) {
					Song[] aSong = new Song[tmpSongsVector.size()];
					aSong = tmpSongsVector.toArray(aSong);
					parsedProfileHashMap.put(tmptitle, aSong);
				}
				
				tmpKeys.addElement(tmptitle);
				tmpSongsVector = null;
				tmptitle = "";

			} else if (parserType.equals("profile")) {
				keysSequence = new String[tmpKeys.size()];
				keysSequence = tmpKeys.toArray(keysSequence);

				return;
			}

			// parse album
		} else if (parserType.equals("album")) {

			if (localName.equals("AlbumPromotion")) {
				
				parsedAlbumsArray = new Album[tmpAlbumsVector.size()];
				parsedAlbumsArray = tmpAlbumsVector.toArray(parsedAlbumsArray);
				this.tmpAlbumsVector = null;
				return;
			}

			// set current album to vector
			if (localName.equals("gAlbumProfile")) {
				this.tmpAlbumsVector.add(currentAlbum);
				currentAlbum = null;

				// parse value
			} else if (localName.equals("AlbumName")) {
				currentAlbum.setName(currentElementValue);
			} else if (localName.equals("Artist")) {
				currentAlbum.setArtist(currentElementValue);
			} else if (localName.equals("IssueDate")) {
				currentAlbum.setDate(currentElementValue);
			} else if (localName.equals("Publisher")) {
				currentAlbum.setPublisher(currentElementValue);
			} else if (localName.equals("img_artist")) {
				currentAlbum.setImgArtistUrl(currentElementValue);
			} else if (localName.equals("IconURL")) {
				currentAlbum.setIconUrl(currentElementValue);
			} else if (localName.equals("ListURL")) {
				currentAlbum.setListUrl(currentElementValue);
			}

		} else if (parserType.equals("activity")) {

			if (localName.equals("Activity")) {
				
				parsedActivitiesArray = new Activity[tmpActivitiesVector.size()];
				parsedActivitiesArray = tmpActivitiesVector.toArray(parsedActivitiesArray);
				tmpActivitiesVector = null;
				return;

			}
		} else if (parserType.equals("search")) {

			if (localName.equals("Item")) {
				Search[] aSearch = new Search[tmpSearchVector.size()];
				aSearch = tmpSearchVector.toArray(aSearch);
				parsedSearchHashMap.put(tmptitle, aSearch);
				tmpKeys.add(tmptitle);
				tmptitle = "";
				tmpSearchVector = null;

			} else if (localName.equals("list")) {
				tmpSearchVector.add(currentSearch);
				currentSearch = null;

			} else if (localName.equals("SongSearch")) {
				keysSequence = new String[tmpKeys.size()];
				keysSequence = tmpKeys.toArray(keysSequence);
				// tmpKeys = null;
				return;

			}
		} else if (parserType.equals("musicbox")) {
			if (localName.equals("MusicBoxPromotion")) {
				
				parsedMusicBoxiesArray = new MusicBox[tmpMusicBoxVector.size()];
				parsedMusicBoxiesArray = tmpMusicBoxVector.toArray(parsedMusicBoxiesArray);
				tmpMusicBoxVector = null;
				return;
			}

			// set current album to vector
			if (localName.equals("MusicBoxProfile")) {
				this.tmpMusicBoxVector.add(currentMusicBox);
				currentMusicBox = null;

				// parse value
			} else if (localName.equals("MUSICBOXID")) {
				currentMusicBox.setMusicId(currentElementValue);
			} else if (localName.equals("MUSICBOXNAME")) {
				currentMusicBox.setMusicname(currentElementValue);
			} else if (localName.equals("PRICE")) {
				currentMusicBox.setPrice(currentElementValue);
			} else if (localName.equals("CONTENTSHORT")) {
				currentMusicBox.setContent(currentElementValue);
			} else if (localName.equals("CPNAME")) {
				currentMusicBox.setCpname(currentElementValue);
			} else if (localName.equals("BigIcon")) {
				currentMusicBox.setBigicon(currentElementValue);
			} else if (localName.equals("SmallIcon")) {
				currentMusicBox.setSmallicon(currentElementValue);
			} else if (localName.equals("ListURL")) {
				currentMusicBox.setListurl(currentElementValue);
			}

		} else if (parserType.equals("category")) {
			if (localName.equals("iRankManagement") || localName.equals("gMusic") || localName.equals("gRecommend")) {
				keysSequence = new String[tmpKeys.size()];
				keysSequence = tmpKeys.toArray(keysSequence);
				tmpKeys = null;
				return;
			}

		}

		currentElementValue = "";
	}
}
