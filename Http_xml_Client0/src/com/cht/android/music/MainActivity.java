package com.cht.android.music;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;




public class MainActivity extends Activity {
    
    private static final String TAG = "MainActivity";
    private static final int EVENT_LOAD_DONE = 1;
    private static final int EVENT_LOAD_CONT = 2;
    private int mCount =0;
    private int i;
    
    private ProgressBar progressHorizontal;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request the progress bar to be shown in the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress);
        setProgressBarVisibility(true);
        
        progressHorizontal = (ProgressBar) findViewById(R.id.progress);
        setProgress(progressHorizontal.getProgress() * 100);
        new Thread(new Runnable()
        {
            public void run()
            {
                for(i=0; i<10;i++)
                {
                    mCount=(i+1)*10;
                try
                {
                Thread.sleep(300);
                }catch(Exception e)
                {
                    Log.d(TAG, "Exception");
                }
                
                if(i==9)
                {
                    Message m = new Message();
                    mCount-=5;
                    m.what = MainActivity.EVENT_LOAD_DONE;
                    MainActivity.this.mMessageHandler.sendMessage(m);
                }else
                {
                    Message m = new Message();
                    m.what = MainActivity.EVENT_LOAD_CONT;
                    MainActivity.this.mMessageHandler.sendMessage(m);
                }
                }
            }
        }).start();
    }
    
   
    Handler mMessageHandler = new Handler()
    {
        public void handleMessage(Message msg) 
        {
            switch (msg.what)
            {
                case EVENT_LOAD_DONE:
                    Thread.currentThread().isInterrupted();
                    loadDone();
                    break;
                case EVENT_LOAD_CONT:
                    if(!Thread.currentThread().isInterrupted())
                    {
                        progressHorizontal.setProgress(mCount);
                    
                    }
                    break;
            }
        }
    };
    
    public void loadDone()
    {
        Intent i = new Intent(this, MusicClientActivity.class);
        startActivity(i);
        finish();
    }
}
