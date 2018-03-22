package com.cht.android.music;

import java.util.HashMap;

import android.util.Log;

import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;
import com.music.parser.data.Search;
import com.music.parser.data.Song;

public class MainUrl {
    
	
    static private String RankingUrl = null;
    static private String RecommandUrl = null;
    static private String ActivityUrl = null;
    static private String SearchUrl = null;
    static private String SettingUrl = null;
    static private String RankingTitle = null;
    static private String RecommandTitle = null;
    static private String ActivityTitle = null;
    static private String SearchTitle = null;
    static private String SettingTitle = null;
    
    // Recommend Url & Title
    static private String SingleSongUrl = null;
    static private String AlbumSongUrl = null;
    static private String MusicBoxUrl = null;
    static private String SingleSongTitle = null;
    static private String AlbumSongTitle = null;
    static private String MusicBoxTitle = null;
    
    // Recommend Single
    private static Song[] RecSong = null;


    
    // Recommend MusicBox
	private static String[] MusicBoxName = null;
	private static String[] MusicBoxCPName = null;
	private static String[] MusicBoxDescription = null;
	private static String[] MusicBoxPrice = null;
	private static String[] MusicBoxImageUrl = null;
	private static String[] MusicBoxListUrl = null;
	private static String[] MusicBoxID = null;
    //Recommend Album
	private static String[] AlbumListUrl = null;
	private static String[] AlbumName = null;
	private static String[] AlbumArtist = null;
	private static String[] AlbumPublisher = null;
	private static String[] AlbumIssueDate = null;
	private static String[] AlbumIconUrl = null;
    
    //Activity Parameter
    private static String[] ActivityName = null;
    private static String[] ActivityDescription = null;
    private static String[] ActivityImageUrl = null; 
    private static String[] ActivitySongListUrl = null; 
    private static String[] ActivityBeginTime = null; 
    private static String[] ActivityEndTime = null;     
    
    //Ranking Category
    private static String[] RankingCategoryTitle = null;
    private static String[] RankingCategoryUrl = null;
    
    // search content
    private static String[] SearchTypeTitle = null;
    // category
    private static String[] CategoryTitle = null;
    private static String[] CategoryUrl = null;
    // hot topic
    private static String[] HotTopicTitle = null;
    private static String[] HotTopicUrl = null;
    // default keyword
    private static String[] KeywordTitle = null;
    private static String[] KeywordUrl = null;
    
    
    // set & get Monitor size
    static private int screenWidth = 0;
    static private int screenHeight = 0;

    // user ringback tone profile
    
    private static boolean isVaildUser = false;
    private static Song[] BasicRing = null;
    private static Song[] DayRing = null;
    private static Song[] NightRing = null;
    private static Song[] WeekendRing = null;
    private static Song[] Group1 = null;
    private static Song[] Group2 = null;
    private static Song[] Group3 = null;
    private static Song[] Group4 = null;
    private static Song[] Group5 = null;
    
    MainUrl()
    {
        //myIcon.setImageResource(resId);
   
    }
    
    //set method
    
    static public void setRankingUrl(String url)
    {
    	RankingUrl = url;
    }
    
    static public void setRecommandUrl(String url)
    {
    	RecommandUrl = url;
    }    
    
    static public void setActivityUrl(String url)
    {
    	ActivityUrl = url;
    }    
    
    static public void setSearchUrl(String url)
    {
    	SearchUrl = url;
    }    
    
    static public void setSettingUrl(String url)
    {
    	SettingUrl = url;
    }    
    
    
    static public void setRankingTitle(String title)
    {
    	RankingTitle = title;
    }
    
    static public void setRecommandTitle(String title)
    {
    	RecommandTitle = title;
    }    
    
    static public void setActivityTitle(String title)
    {
    	ActivityTitle = title;
    }    
    
    static public void setSearchTitle(String title)
    {
    	SearchTitle = title;
    }    
    
    static public void setSettingTitle(String title)
    {
    	SettingTitle = title;
    }    
    
    
    static public void setSingleSongUrl(String url)
    {
    	SingleSongUrl = url;
    }    
    
    static public void setAlbumSongUrl(String url)
    {
    	AlbumSongUrl = url;
    }    
    
    static public void setMusicBoxUrl(String url)
    {
    	MusicBoxUrl = url;
    }    
    
    
    static public void setSingleSongTitle(String title)
    {
    	SingleSongTitle = title;
    }
    
    static public void setAlbumSongTitle(String title)
    {
    	AlbumSongTitle = title;
    }    
    
    static public void setMusicBoxTitle(String title)
    {
    	MusicBoxTitle = title;
    }       
    
    // get method
    static public String getRankingUrl()
    {
    	return RankingUrl;
    }
    
    static public String getRecommandUrl()
    {
    	return RecommandUrl;
    }    
    
    static public String getActivityUrl()
    {
    	return ActivityUrl;
    }    
    
    static public String getSearchUrl()
    {
    	return SearchUrl;
    }    
    
    static public String getSettingUrl()
    {
    	return SettingUrl;
    }    
    
    
    static public String getRankingTitle()
    {
    	return RankingTitle;
    }
    
    static public String getRecommandTitle()
    {
    	return RecommandTitle;
    }    
    
    static public String getActivityTitle(String title)
    {
    	return ActivityTitle;
    }    
    
    static public String getSearchTitle()
    {
    	return SearchTitle;
    }    
    
    static public String getSettingTitle()
    {
    	return SettingTitle;
    }    
    
    static public String getSingleSongUrl()
    {
    	return SingleSongUrl;
    }    
    
    static public String getAlbumSongUrl()
    {
    	return AlbumSongUrl;
    }    
    
    static public String getMusicBoxUrl()
    {
    	return MusicBoxUrl;
    }    
    
    
    static public String getSingleSongTitle()
    {
    	return SingleSongTitle;
    }
    
    static public String getAlbumSongTitle()
    {
    	return AlbumSongTitle;
    }    
    
    static public String getMusicBoxTitle()
    {
    	return MusicBoxTitle;
    }      

   //  get Recommend Url
    static public void initialRecommendData(){
    	
    	ParsingXML parsing = new ParsingXML();
    	ParserHandler myHandler = parsing.parsingData(getRecommandUrl());
		HashMap<String, String> aHashMap = myHandler.getParsedHashMap();
		String[] keysArray = myHandler.getKeysSequence();
		
		for (int i = 0; i < keysArray.length; i++) {
			String key = keysArray[i];
			
		    if(key.compareTo("單曲")==0){
		    	setSingleSongTitle(key);
		    	setSingleSongUrl(aHashMap.get(key));
		    }else if(key.compareTo("專輯")==0){
		    	setAlbumSongTitle(key);
		    	setAlbumSongUrl(aHashMap.get(key));
		    }else if(key.compareTo("音樂盒")==0){
		    	setMusicBoxTitle(key);
		    	setMusicBoxUrl(aHashMap.get(key)); 
		    } 	
		    	
		}
    	
    }   
    
    // get & set screenHeight & screenWidth
    
    public static void setScreenHeight(int heigth){
    	screenHeight = heigth;
    }
    public static void setScreenWidth(int width){
    	screenWidth = width;
    }
    public static int getScreenHeight(){
    	return screenHeight;
    }
    public static int getScreenWidth(){
    	return screenWidth;
    }

	public static void setActivityEndTime(String[] activityEndTime) {
		ActivityEndTime = activityEndTime;
	}

	public static String[] getActivityEndTime() {
		return ActivityEndTime;
	}

	public static void setActivityBeginTime(String[] activityBeginTime) {
		ActivityBeginTime = activityBeginTime;
	}

	public static String[] getActivityBeginTime() {
		return ActivityBeginTime;
	}

	public static void setActivitySongListUrl(String[] activitySongListUrl) {
		ActivitySongListUrl = activitySongListUrl;
	}

	public static String[] getActivitySongListUrl() {
		return ActivitySongListUrl;
	}

	public static void setActivityImageUrl(String[] activityImageUrl) {
		ActivityImageUrl = activityImageUrl;
	}

	public static String[] getActivityImageUrl() {
		return ActivityImageUrl;
	}

	public static void setActivityDescription(String[] activityDescription) {
		ActivityDescription = activityDescription;
	}

	public static String[] getActivityDescription() {
		return ActivityDescription;
	}

	public static void setActivityName(String[] activityName) {
		ActivityName = activityName;
	}

	public static String[] getActivityName() {
		return ActivityName;
	}

	public static void setAlbumListUrl(String[] albumListUrl) {
		AlbumListUrl = albumListUrl;
	}

	public static String[] getAlbumListUrl() {
		return AlbumListUrl;
	}

	public static void setAlbumName(String[] albumName) {
		AlbumName = albumName;
	}

	public static String[] getAlbumName() {
		return AlbumName;
	}

	public static void setAlbumArtist(String[] albumArtist) {
		AlbumArtist = albumArtist;
	}

	public static String[] getAlbumArtist() {
		return AlbumArtist;
	}

	public static void setAlbumPublisher(String[] albumPublisher) {
		AlbumPublisher = albumPublisher;
	}

	public static String[] getAlbumPublisher() {
		return AlbumPublisher;
	}

	public static void setAlbumIssueDate(String[] albumIssueDate) {
		AlbumIssueDate = albumIssueDate;
	}

	public static String[] getAlbumIssueDate() {
		return AlbumIssueDate;
	}

	public static void setAlbumIconUrl(String[] albumIconUrl) {
		AlbumIconUrl = albumIconUrl;
	}

	public static String[] getAlbumIconUrl() {
		return AlbumIconUrl;
	}

	public static void setMusicBoxDescription(String[] musicBoxDescription) {
		MusicBoxDescription = musicBoxDescription;
	}

	public static String[] getMusicBoxDescription() {
		return MusicBoxDescription;
	}

	public static void setMusicBoxPrice(String[] musicBoxPrice) {
		MusicBoxPrice = musicBoxPrice;
	}

	public static String[] getMusicBoxPrice() {
		return MusicBoxPrice;
	}

	public static void setMusicBoxImageUrl(String[] musicBoxImageUrl) {
		MusicBoxImageUrl = musicBoxImageUrl;
	}

	public static String[] getMusicBoxImageUrl() {
		return MusicBoxImageUrl;
	}

	public static void setMusicBoxListUrl(String[] musicBoxListUrl) {
		MusicBoxListUrl = musicBoxListUrl;
	}

	public static String[] getMusicBoxListUrl() {
		return MusicBoxListUrl;
	}

	public static void setMusicBoxID(String[] musicBoxID) {
		MusicBoxID = musicBoxID;
	}

	public static String[] getMusicBoxID() {
		return MusicBoxID;
	}

	public static void setRankingCategoryUrl(String[] rankingCategoryUrl) {
		RankingCategoryUrl = rankingCategoryUrl;
	}

	public static String[] getRankingCategoryUrl() {
		return RankingCategoryUrl;
	}

	public static void setRankingCategoryTitle(String[] rankingCategoryTitle) {
		RankingCategoryTitle = rankingCategoryTitle;
	}

	public static String[] getRankingCategoryTitle() {
		return RankingCategoryTitle;
	}


	
    public static void initRankingList(){
        //TODO define actions when user select item in MusicList
        	//  first time to get url data
        	
    	   // ParserHandler myHandler = parsingData("http://musicphone.emome.net/MAS/DoTask?Task=iRank&xsl=pcIni.xsl&catagoryid=000&pagecount=1&pagesize=10");
               ParsingXML parsing = new ParsingXML();
               ParserHandler myHandler = parsing.parsingData(getRankingUrl());
    	       HashMap<String, String> aHashMap = myHandler.getParsedHashMap();
    		   String[] keysArray = myHandler.getKeysSequence();
    		
    		   RankingCategoryTitle =new String[keysArray.length];
    		   RankingCategoryUrl = new String[keysArray.length];
    		   for (int i = 0; i < keysArray.length; i++) {
    				String key = keysArray[i];
    				//Log.i(Tag,i+"  Key="+key);
    				RankingCategoryTitle[i] = key;
    				RankingCategoryUrl[i] = aHashMap.get(key);

    		   }
    		   
    	
    	       myHandler = null;
    	       keysArray = null;
    		   
    }	
	
    public static void initialActivities(){
        ParsingXML parsing = new ParsingXML();
        ParserHandler myHandler = parsing.parsingData(getActivityUrl());
       // Log.i(MyTag, "ActivityUrl："+MainUrl.ActivityUrl);
        
		com.music.parser.data.Activity[] activitiesArray = myHandler.getParsedActivitiesArray();
		
		// initial string[]
	
	    ActivityName = new String[activitiesArray.length];
		ActivityDescription = new String[activitiesArray.length];
		ActivityImageUrl = new String[activitiesArray.length];
		ActivitySongListUrl = new String[activitiesArray.length];
		ActivityBeginTime = new String[activitiesArray.length];
		ActivityEndTime = new String[activitiesArray.length];
		
		// set ActivityContent

		
		for (int i = 0; i < activitiesArray.length; i++) {	
		
			ActivityName[i] = activitiesArray[i].getName();
			ActivityDescription[i] = activitiesArray[i].getContent();
			ActivityImageUrl[i] = activitiesArray[i].getImgUrl().replaceAll("60X60.jpg","145X89.gif");
			ActivitySongListUrl[i] = activitiesArray[i].getSongsUrl();
			ActivityBeginTime[i] = activitiesArray[i].getBeginDate();
			ActivityEndTime[i] = activitiesArray[i].getEndDate();
			//Log.i("MainUrl","ActivityImageUrl="+ActivityImageUrl[i]);
		}
		
		
		try{
			activitiesArray = null;
		}catch(Exception e){
			
		}
    }

	public static void setMusicBoxCPName(String[] musicBoxCPName) {
		MusicBoxCPName = musicBoxCPName;
	}

	public static String[] getMusicBoxCPName() {
		return MusicBoxCPName;
	}

	public static void setMusicBoxName(String[] musicBoxName) {
		MusicBoxName = musicBoxName;
	}

	public static String[] getMusicBoxName() {
		return MusicBoxName;
	}

	public static void setCategoryTitle(String[] categoryTitle) {
		CategoryTitle = categoryTitle;
	}

	public static String[] getCategoryTitle() {
		return CategoryTitle;
	}

	public static void setCategoryUrl(String[] categoryUrl) {
		CategoryUrl = categoryUrl;
	}

	public static String[] getCategoryUrl() {
		return CategoryUrl;
	}

	public static void setHotTopicTitle(String[] hotTopicTitle) {
		HotTopicTitle = hotTopicTitle;
	}

	public static String[] getHotTopicTitle() {
		return HotTopicTitle;
	}

	public static void setHotTopicUrl(String[] hotTopicUrl) {
		HotTopicUrl = hotTopicUrl;
	}

	public static String[] getHotTopicUrl() {
		return HotTopicUrl;
	}

	public static void setKeywordTitle(String[] keywordTitle) {
		KeywordTitle = keywordTitle;
	}

	public static String[] getKeywordTitle() {
		return KeywordTitle;
	}

	public static void setKeywordUrl(String[] keywordUrl) {
		KeywordUrl = keywordUrl;
	}

	public static String[] getKeywordUrl() {
		return KeywordUrl;
	}

	public static void setSearchTypeTitle(String[] searchTypeTitle) {
		SearchTypeTitle = searchTypeTitle;
	}

	public static String[] getSearchTypeTitle() {
		return SearchTypeTitle;
	}
	public static void initialSearchUrl(){
        ParsingXML parsing = new ParsingXML();
        ParserHandler myHandler = parsing.parsingData(MainUrl.getSearchUrl());
		HashMap<String, Search[]> aHashMap = myHandler.getParsedSearchHashMap();
		String[] KeysArray = myHandler.getKeysSequence();

		// print
		SearchTypeTitle = new String[KeysArray.length];
		for (int i = 0; i < KeysArray.length; i++) {

			String key = KeysArray[i];
			//tmpString = tmpString + "title->" + key + "\n";
			
            SearchTypeTitle[i] = key;
			if (aHashMap.get(key) != null) {
				Search[] tmpS = aHashMap.get(key);
				if(key.compareTo("分類搜尋")==0){
					CategoryTitle = new String[tmpS.length];
					CategoryUrl = new String[tmpS.length];
				   for (int j = 0; j < tmpS.length; j++) {
                      //  Log.i("setSearchUrl=Category","tmpS[j].getKeyword()="+tmpS[j].getKeyword());
					    CategoryTitle[j] = tmpS[j].getTitle();
					    CategoryUrl[j] = tmpS[j].getUrl();
				   }
			    }else if(key.compareTo("發燒主題")==0){
			    	HotTopicTitle = new String[tmpS.length];
			    	HotTopicUrl = new String[tmpS.length];
					   for (int j = 0; j < tmpS.length; j++) {
				//		   Log.i("setSearchUrl=Hot","tmpS[j].getUrl()="+tmpS[j].getUrl()); 
						   HotTopicTitle[j] = tmpS[j].getTitle();
						   HotTopicUrl[j] = tmpS[j].getUrl();
					   }
			    }else if(key.compareTo("熱門關鍵字")==0){
			    	KeywordTitle = new String[tmpS.length];
			    	KeywordUrl = new String[tmpS.length];
					   for (int j = 0; j < tmpS.length; j++) {
	            //            Log.i("setSearchUrl=Keyword","tmpS[j].getUrl()="+tmpS[j].getUrl());
 
						   KeywordTitle[j] = tmpS[j].getTitle();
						   KeywordUrl[j] = tmpS[j].getUrl();
					   }
			    }
			}
		}
    }

	public static void setRecSong(Song[] recSong) {
		RecSong = recSong;
	}

	public static Song[] getRecSong() {
		return RecSong;
	}

	public static void setUserProfile(){
        ParsingXML parsing = new ParsingXML();
        ParserHandler myHandler = parsing.parsingData(MainUrl.getSettingUrl());
		HashMap<String, Song[]> profileHashMap = myHandler.getParsedProfileHashMap();
		String[] keysArray = myHandler.getKeysSequence();
		
		// print song information
		//tmpString = tmpString + "member->" + handler.getRingtonMember() + "\n";
		if(myHandler.getRingtonMember().toLowerCase().compareTo("y")==0)
			isVaildUser = true;
		else
			isVaildUser = false;
		
        if(isVaildUser){
		   for (int i = 0; i < keysArray.length; i++) {
		
		    	String key = keysArray[i];

		    	Log.i("keysArray", "key->" + key);
		    	
		    	

			    if (profileHashMap.get(key) != null) {
                    if(key.toLowerCase().compareTo("basic")==0){
                    	setBasicRing(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("daylight")==0){
                    	setDayRing(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("night")==0){
                    	setNightRing(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("weekend")==0){
                    	setWeekendRing(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("egroup1")==0){
                    	//setGroup1(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("egroup2")==0){
                    	//setGroup2(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("egroup3")==0){
                    	//setGroup3(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("egroup4")==0){
                    	//setGroup4(profileHashMap.get(key));
                    }else if(key.toLowerCase().compareTo("egroup5")==0){
                    	//setGroup5(profileHashMap.get(key));
                    }
				

		        }
		    }
        }
	}

	public static void setBasicRing(Song[] basicRing) {
		BasicRing = basicRing;
	}

	public static Song[] getBasicRing() {
		return BasicRing;
	}

	public static void setDayRing(Song[] dayRing) {
		DayRing = dayRing;
	}

	public static Song[] getDayRing() {
		return DayRing;
	}

	public static void setNightRing(Song[] nightRing) {
		NightRing = nightRing;
	}

	public static Song[] getNightRing() {
		return NightRing;
	}

	public static void setWeekendRing(Song[] weekendRing) {
		WeekendRing = weekendRing;
	}

	public static Song[] getWeekendRing() {
		return WeekendRing;
	}

	public static void setGroup1(Song[] group1) {
		Group1 = group1;
	}

	public static Song[] getGroup1() {
		return Group1;
	}

	public static void setGroup2(Song[] group2) {
		Group2 = group2;
	}

	public static Song[] getGroup2() {
		return Group2;
	}

	public static void setGroup3(Song[] group3) {
		Group3 = group3;
	}

	public static Song[] getGroup3() {
		return Group3;
	}

	public static void setGroup4(Song[] group4) {
		Group4 = group4;
	}

	public static Song[] getGroup4() {
		return Group4;
	}

	public static void setGroup5(Song[] group5) {
		Group5 = group5;
	}

	public static Song[] getGroup5() {
		return Group5;
	}

	public static boolean getUserVaild() {
		return isVaildUser;
	}
	
	public static void setUserVaild(boolean user) {
		isVaildUser = user;
	}
		
}