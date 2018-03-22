package com.cht.android.music;



import com.cht.android.music.RecommendView.MyHandler;
import com.music.parser.ParserHandler;
import com.music.parser.ParsingXML;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Activities extends Activity {
    private ListView activitiesList = null;
    private String[] ActivityTitle = null;
    private String[] ActivityDescription = null;
    private String[] ActivityImageUrl = null; 
    private String[] ActivitySongListUrl = null; 
    private String[] ActivityBeginTime = null; 
    private String[] ActivityEndTime = null; 
    
    private String myTag = "Activities";
    
	public static final int UPDATE_SETTING_SUCCESS = 0x0001;

    private static final int CON_INDEX_RECOMMEND  = R.id.recommend;
    private static final int CON_INDEX_RANKING     = R.id.ranking;
    private static final int CON_INDEX_ACTIVITY   = R.id.showactivity;
    private static final int CON_INDEX_SEARCH   = R.id.keyseach;
    private static final int CON_INDEX_SETTING   = R.id.setting;
    
	HttpThread httpThread;

    private static final int CON_INDEX_MYLIST   = 5;    
    com.music.parser.data.Activity[] activitiesArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        
        // TODO: mStrings is from database!
     //   Log.i(MyTag, "Start");
        setContentView(R.layout.activities_mainpage_layout);
        
        activitiesList = (ListView)findViewById(R.id.ActivityContentListView);
        if(ActivityContent.getActivitySongListUrl()==null || ActivityContent.getActivitySongListUrl().length==0){
        	httpThread = new HttpThread(MainUrl.getActivityUrl(), new MyHandler(),UPDATE_SETTING_SUCCESS);
 		    httpThread.start();
        }else{
        	ActivityTitle = ActivityContent.getActivityTitle();
        	ActivityDescription = ActivityContent.getActivityDescription();
        	ActivityImageUrl = ActivityContent.getActivityImageUrl();
        	ActivitySongListUrl = ActivityContent.getActivitySongListUrl();
        	ActivityBeginTime = ActivityContent.getActivityBeginTime();
        	ActivityEndTime = ActivityContent.getActivityEndTime();
        	initialActivityView(this);
        }
        
}
    public void initialActivityView(Context context) {

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        
        // TODO: mStrings is from database!
     //   Log.i(MyTag, "Start");

        ActivitiesListAdapter acitvitiesListAdapter = new ActivitiesListAdapter(context, R.layout.activity_list, ActivityTitle,ActivityImageUrl);

        activitiesList.setAdapter(acitvitiesListAdapter);
        activitiesList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long id) {
        
                Intent aIntent = new Intent(activitiesList.getContext(), ActivityDetails.class);
    	        aIntent.putExtra("ActivityTitle", MainUrl.getActivityName()[pos]);
    	        aIntent.putExtra("ActivityDescription", MainUrl.getActivityDescription()[pos]);
    	        aIntent.putExtra("ActivityImageUrl", MainUrl.getActivityImageUrl()[pos]);
    	        aIntent.putExtra("ActivitySongListUrl", MainUrl.getActivitySongListUrl()[pos]);
    	        aIntent.putExtra("ActivityBeginTime", MainUrl.getActivityBeginTime()[pos]);
    	        aIntent.putExtra("ActivityEndTime", MainUrl.getActivityEndTime()[pos]);
    	        activitiesList.getContext().startActivity(aIntent);
            }
        });    
        
}
    
    
    private void getActivities(){
       // Log.i(MyTag, "ActivityUrl¡G"+MainUrl.ActivityUrl);
        
		
		// initial string[]
		
		ActivityTitle = new String[activitiesArray.length];
		ActivityDescription = new String[activitiesArray.length];
		ActivityImageUrl = new String[activitiesArray.length];
		ActivitySongListUrl = new String[activitiesArray.length];
		ActivityBeginTime = new String[activitiesArray.length];
		ActivityEndTime = new String[activitiesArray.length];
		
		// set ActivityContent

		
		for (int i = 0; i < activitiesArray.length; i++) {	
		
			ActivityTitle[i] = activitiesArray[i].getName();
			ActivityDescription[i] = activitiesArray[i].getContent();
			ActivityImageUrl[i] = activitiesArray[i].getImgUrl();
			ActivitySongListUrl[i] = activitiesArray[i].getSongsUrl();
			ActivityBeginTime[i] = activitiesArray[i].getBeginDate();
			ActivityEndTime[i] = activitiesArray[i].getEndDate();
			
		}
		try{
			activitiesArray = null;
		}catch(Exception e){
			
		}
        ActivityContent.setActivityTitle(ActivityTitle);
        ActivityContent.setActivityDescription(ActivityDescription);
        ActivityContent.setActivityImageUrl(ActivityImageUrl);
        ActivityContent.setActivitySongListUrl(ActivitySongListUrl);
        ActivityContent.setActivityBeginTime(ActivityBeginTime);
        ActivityContent.setActivityEndTime(ActivityEndTime);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onPrepareOptionsMenu(menu);
       
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
      //  menu.add(0, CON_INDEX_ALBUM, 0, R.string.recommend_album);

    	return true;
     }       
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{  
        Intent intent;
        Log.i(myTag, "item.getItemId()="+item.getItemId());
      //  mLinearLayout.removeAllViews();
      //  LayoutInflater.from(MyActivity.this).inflate(R.layout.main2,mLinearLayout,true);
        /* Switch on the ID of the item, to get what the user selected. */  
        switch (item.getItemId()) {   
        
            case CON_INDEX_RECOMMEND:
                
                intent = new Intent(this,RecommendActivity.class);
                this.startActivity(intent);               
                return true;
            case CON_INDEX_RANKING:  	
                intent = new Intent(this,RankingActivity.class);
                this.startActivity(intent);               
                return true;
            case CON_INDEX_ACTIVITY:  
                // TODO: Invoke browser with corresponding URL.
                intent = new Intent(this,Activities.class);
                this.startActivity(intent); 
                startActivity(intent);               
                return true;
            case CON_INDEX_SEARCH:
                intent = new Intent(this, SearchActivity.class);
                this.startActivity(intent);
                
                return true;
            case CON_INDEX_SETTING:
                // TODO: Pass the information of current music to the Settings.
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        
        return false;   
    }       
    
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_SETTING_SUCCESS:
		     //   RecommendActivity.songdata = myHandler.getParsedSongsArray();
				ParsingXML pXml = new ParsingXML(httpThread.getFinishedInputStream());
				ParserHandler pHandler = pXml.parsingData();
				activitiesArray = pHandler.getParsedActivitiesArray();
			
				break;

			}
			super.handleMessage(msg);
		}
	}	
	
}
