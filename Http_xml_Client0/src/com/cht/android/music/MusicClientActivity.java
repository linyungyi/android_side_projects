package com.cht.android.music;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;



public class MusicClientActivity extends TabActivity {
    private static final String TAG = "Music Client";
	

	private static final int TAB_INDEX_ACTIVITIES  = 0;
    
    private static final int SETTING_ID = Menu.FIRST;
    private static final int ABOUT_ID = Menu.FIRST + 1;
 
    private TabHost mTabHost;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        mTabHost = getTabHost();

        // Setup the tabs
        setupActivitiesTab();
        setupRankingTab();
        setupRecommendTab();
        setupSearchTab();

        mTabHost.setCurrentTab(TAB_INDEX_ACTIVITIES);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        //mTabHost.getCurrentTab();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onPrepareOptionsMenu(menu);
        menu.add(0, SETTING_ID, 0,R.string.music_menu_setting).setIcon(R.drawable.icon_menu_settings);
        menu.add(0, ABOUT_ID, 0,R.string.music_menu_about).setIcon(R.drawable.icon_menu_about);

      return true;
     }

    
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId()) 
		{       
			case SETTING_ID:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			case ABOUT_ID:
				aboutDialog();
				return true;
			default:
			    return super.onOptionsItemSelected(item);
		}
}
    
    private void aboutDialog(){
    
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage(R.string.music_menu_content);
        confirm.setTitle(R.string.music_menu_about);
        confirm.setPositiveButton(
            android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        confirm.show();
    }
    private void setupActivitiesTab() {
        // Force the class since overriding tab entries doesn't work
        Intent i = new Intent(this, Activities.class);
        
        // TODO: Set correct icon of each tab.
        mTabHost.addTab(mTabHost.newTabSpec("Activities")
                .setIndicator(getString(R.string.music_client_activies), 
                        getResources().getDrawable(R.drawable.ic_tab_recent))
                .setContent(i));
    }

    private void setupRankingTab() {
        Intent i = new Intent(this, RankingActivity.class);

        // TODO: Set correct icon of each tab.
        mTabHost.addTab(mTabHost.newTabSpec("Ranking")
                .setIndicator(getString(R.string.music_client_ranking), 
                        getResources().getDrawable(R.drawable.ic_tab_recent))
                .setContent(i));
    }

    private void setupRecommendTab() {
        // TODO: Setup RecommendActivity.
        Intent i = new Intent(this, RankingActivity.class);

        // TODO: Set correct icon of each tab.
        mTabHost.addTab(mTabHost.newTabSpec("Recommend")
                .setIndicator(getText(R.string.music_client_rcommend), 
                        getResources().getDrawable(R.drawable.ic_tab_recent))
                .setContent(i));
    }

    private void setupSearchTab() {
        Intent i = new Intent(this, SearchActivity.class);

        // TODO: Set correct icon of each tab.
        mTabHost.addTab(mTabHost.newTabSpec("Search")
                .setIndicator(getString(R.string.music_client_search), 
                        getResources().getDrawable(R.drawable.ic_tab_recent))
                .setContent(i));
    }

}