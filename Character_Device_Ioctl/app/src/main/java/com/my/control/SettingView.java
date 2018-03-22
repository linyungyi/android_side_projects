package com.my.control;

//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.nio.charset.Charset;

import android.os.Bundle;
//import android.os.Handler;

import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.util.Log;
//import android.content.DialogInterface;

import android.view.MenuInflater;


//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;

import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;

//import android.app.Activity;
//import android.os.Bundle;
import android.view.Menu;
//import android.view.MenuInflater;
import android.view.MenuItem;


public class SettingView extends Activity{

	public static final String LOG_TAG = "[MY]";

	GlobalData globalData;
	MyClass myClass;

	Bundle bundle;
	Button btn_show_main;
	
	int screenWidth;
	int screenHeight;
    int row;
    int col;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//String msg;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		///--------------------------------------------------------///
		/// Global Data
//		///--------------------------------------------------------///
	//	globalData = ((GlobalData) getApplicationContext());
	//	myClass = globalData.get_myClass();

	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

//		bundle = this.getIntent().getExtras();

	//	row = bundle.getInt("row");
//		col = bundle.getInt("col");

			
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
	public boolean onOptionsItemSelected(MenuItem item) {
		   return true;
	   }

}
