package com.my.gesture;

import com.my.gesture.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    MyView myView;
    TextView myTextView;
    MyLog MyViewLog;
    MyLog MyViewLog01;
    MyRevision MyViewRevision;
    ContextWrapper context;

    private ArrayList<String> textList;
    private int listIndex = 0;
    private int characterLoop = 0;
    public static final int CHARACTERLOOPMAX = 5;
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    //public static final String DOUBLECLICK = "DOUBLECLICK";
    public static final String O = "O";
    public static final String W = "W";
    public static final String M = "M";
    public static final String E = "E";
    public static final String L = "L";
    public static final String S = "S";
    public static final String V = "V";
    public static final String Z = "Z";
    public static final String C = "C";
    public static final String U = "U";

    ArrayList<SAMPLE> sampleTable;

    GestureJNI gj=new GestureJNI();

    protected Handler hTimer1 = new Handler();
    protected int timer1Delay01 = 2500; // /< in ms
    protected int timer1Delay02 = 1000; // /< in ms
    protected int iTestMode = 1;

    private String LOG_TAG = "[ZET]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyViewRevision = new MyRevision();

        setContentView(R.layout.activity_fullscreen);
        myView = (MyView) findViewById(R.id.fullscreen_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("   Zeitecsemi Gesture Touch Database " + MyViewRevision.getRevision());
        toolbar.setTitleTextColor(0xFFFFFFFF); ////< white
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        context = new ContextWrapper(getApplicationContext());

        MyViewLog = new MyLog(context, "mylog.txt", false, false);
        //myView = new MyView(this,MyViewLog);
        //myView = new MyView(this);
        myView.log0 = MyViewLog;

        MyViewLog01 = new MyLog(context, "location.bin", false, true);
        myView.log1 = MyViewLog01;

        textList = new ArrayList<String>();
        //textList.add(LEFT);
        //textList.add(RIGHT);
        //textList.add(UP);
        //textList.add(DOWN);
        textList.add(O);
        textList.add(W);
        textList.add(M);
        textList.add(E);
        textList.add(L);
        textList.add(S);
        textList.add(V);
        textList.add(Z);
        textList.add(C);

        CreateTestView();

        //toolbar.setTitle(gj.hello());

/*        setContentView(R.layout.activity_fullscreen);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
*/

        // /--------------------------------------------------------///
        // / 1. Start Timer#1
        // /--------------------------------------------------------///
        //hTimer1.removeCallbacks(onTimer1);
        //hTimer1.postDelayed(onTimer1, timer1Delay);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {

                case R.id.action_test01:
                    msg += "Test01 start";
                    iTestMode = 1;
                    listIndex = 0;
                    myView.bGestureDatabase = true;
                    hTimer1.removeCallbacks(onTimer1);
                    hTimer1.postDelayed(onTimer1, timer1Delay01);

                    break;

                case R.id.action_test02:
                    msg += "Test02 start";
                    iTestMode = 2;
                    myView.bGestureDatabase = false;
                    sampleTable = new ArrayList<SAMPLE>();
                    ParseLog();

                    //listIndex = 0;
                    hTimer1.removeCallbacks(onTimer1);
                    hTimer1.postDelayed(onTimer1, timer1Delay02);
                    /*
                    String tar="AGCCT";
                    String src="ATCT";
                    int ret;

                    ret = gj.editDistance(tar.length(),src.length(),tar.getBytes(),src.getBytes());

                    Log.d("[ZET]", "ret = " + Integer.toString(ret) );
                    */
                    break;
                case R.id.action_export:
                    msg += "export";
                    iTestMode = 2;
                    myView.bGestureDatabase = false;
                    sampleTable = new ArrayList<SAMPLE>();
                    ParseLog();
                    ExportLog();
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(FullscreenActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
/*    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }
*/

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
/*    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };
*/
    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
/*    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
*/

    public void ParseLog()
    {
        try{
            // Open the file that is the first
            // command line parameter
            InputStream inputStream = openFileInput("mylog.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String strLine;
            String s="";
            int d=0;
            String f="";
            //Read File Line By Line
            while ((strLine = bufferedReader.readLine()) != null)   {
                // Print the content on the console
                String[] AfterSplit = strLine.split(",");
                for(int i = 0; i < AfterSplit.length; i++)
                {
                    switch (i)
                    {
                        case 0:
                            s = AfterSplit[i];
                            break;
                        case 1:
                            d = Integer.parseInt(AfterSplit[i]);
                            break;
                        case 2:
                            f = AfterSplit[i];
                            break;
                        default:
                            break;
                    }
                }

                sampleTable.add(new SAMPLE(s,d,f));
                Log.v(LOG_TAG, s + "  " + f);

            }
            //Close the input stream
            inputStream.close();
        }catch (Exception e){//Catch exception if any
            Log.e(LOG_TAG, "read log file failed : " + e.toString());
        }
    }

    public void ExportLog()
    {
        MyLog bin = new MyLog(context, "gesture.db", true, true);
        String tar;
        int tar_len;
        int id;
        int count;
        String str;
        for(int i=0;i<sampleTable.size()-1;i++)
        {
            id = sampleTable.get(i).getID();
            tar = sampleTable.get(i).getTargetString();
            tar_len = tar.length();

            char[] c = tar.toCharArray();

            //str = Integer.toHexString(id) + ", ";
            str = "0x" + String.format("%02X",id) + ", ";
            str = str + "0x" + String.format("%02X", tar_len) + ", ";

            for(int j=0; j<tar_len; j++)
            {
                str = str + "0x0" + c[j] + ", ";
            }

            str = str + "\n";
            bin.WriteLog(str,true,false);
        }

        str = "0xFF\n";
        bin.WriteLog(str,true,false);
    }

    public void CreateTestView()
    {
        myTextView = (TextView)findViewById(R.id.test_content);
        myTextView.setText("Ready?");
        //myTextView.append("\tExample TextView by corn");
        myTextView.setTextSize(20);
        myTextView.setTextColor(Color.GREEN);
        myTextView.setBackgroundColor(Color.BLUE);
    }

    class SAMPLE{
        String key;
        int id;
        String targetString;
        int distance;

        public SAMPLE(String c, int code, String target){
            this.key = c;
            this.id = code;
            this.targetString = target;
            this.distance = 99;
        }

        public String getCharactor(){
            return key;
        }

        public int getID(){
            return id;
        }

        public String getTargetString(){
            return targetString;
        }

        public int getDistance() { return distance; }

    }

    public String simpleGesture(String src){
        String ret = "";
        int[] code;
        char[] strChar = src.toCharArray();
        int index;

        int Digit_0_count =0;
        int Digit_1_count =0;
        int Digit_2_count =0;
        int Digit_3_count =0;
        int Digit_4_count =0;
        int Digit_5_count =0;
        int Digit_6_count =0;
        int Digit_7_count =0;

        int Digit_0_first = -1;
        int Digit_1_first = -1;
        int Digit_2_first = -1;
        int Digit_3_first = -1;
        int Digit_4_first = -1;
        int Digit_5_first = -1;
        int Digit_6_first = -1;
        int Digit_7_first = -1;

        int Digit_0_last = -1;
        int Digit_1_last = -1;
        int Digit_2_last = -1;
        int Digit_3_last = -1;
        int Digit_4_last = -1;
        int Digit_5_last = -1;
        int Digit_6_last = -1;
        int Digit_7_last = -1;

        code = new int[8];

        Log.d("[ZET]", "simpleGesture");

        for(int i=1;i<src.length()-1;i++)
        {
            index = (int)(strChar[i]-48);
            code[index]++;

            switch(index)
            {
                case 0:
                    Digit_0_last = i;
                    if(Digit_0_count == 0)
                        Digit_0_first = i;
                    Digit_0_count++;
                    break;
                case 1:
                    Digit_1_last = i;
                    if(Digit_1_count == 0)
                        Digit_1_first = i;
                    Digit_1_count++;
                    break;
                case 2:
                    Digit_2_last = i;
                    if(Digit_2_count == 0)
                        Digit_2_first = i;
                    Digit_2_count++;
                    break;
                case 3:
                    Digit_3_last = i;
                    if(Digit_3_count == 0)
                        Digit_3_first = i;
                    Digit_3_count++;
                    break;
                case 4:
                    Digit_4_last = i;
                    if(Digit_4_count == 0)
                        Digit_4_first = i;
                    Digit_4_count++;
                    break;
                case 5:
                    Digit_5_last = i;
                    if(Digit_5_count == 0)
                        Digit_5_first = i;
                    Digit_5_count++;
                    break;
                case 6:
                    Digit_6_last = i;
                    if(Digit_6_count == 0)
                        Digit_6_first = i;
                    Digit_6_count++;
                    break;
                case 7:
                    Digit_7_last = i;
                    if(Digit_7_count == 0)
                        Digit_7_first = i;
                    Digit_7_count++;
                    break;
            }
        }

        if(code[0]==0 && code[1]>=0 && code[2]>0 && code[3]>=0 && code[4]==0 && code[5]==0 && code[6]==0 && code[7]==0)
        {
            ret = UP;
        }else if(code[0]==0 && code[1]==0 && code[2]==0 && code[3]==0 && code[4]==0 && code[5]>=0 && code[6]>0 && code[7]>=0)
        {
            ret = DOWN;
        }else if(code[0]==0 && code[1]==0 && code[2]==0 && code[3]>=0 && code[4]>0 && code[5]>=0 && code[6]==0 && code[7]==0)
        {
            ret = LEFT;
        }else if(code[0]>0 && code[1]>=0 && code[2]==0 && code[3]==0 && code[4]==0 && code[5]==0 && code[6]==0 && code[7]>=0)
        {
            ret = RIGHT;
        }else if(code[0]>0 && code[1]>=0 && code[2]==0 && code[3]==0 && code[4]==0 && code[5]>=0 && code[6]>0 && code[7]>=0)
        {
            Log.d("[ZET]", Digit_6_last + " " + Digit_0_first);

            if(Digit_6_last < Digit_0_first)
                ret = L;
        }

        return ret;
    }
    private Runnable onTimer1 = new Runnable() {
        public void run() {

            int timer1Delay = 1000;

            switch(iTestMode)
            {
                case 1:
                    timer1Delay = timer1Delay01;
                    if(listIndex > textList.size()-1) {
                        myTextView.setText("Ready?");
                        hTimer1.removeCallbacks(onTimer1);
                        myView.bGestureDatabase = false;
                        return;
                    }
                    if(characterLoop < CHARACTERLOOPMAX)
                    {
                        String s = textList.get(listIndex);
                        myTextView.setText(s+"("+Integer.toString(characterLoop+1)+")");
                        myView.GestureCharacter = s;
                        characterLoop++;
                    }else
                    {
                        listIndex++;
                        characterLoop = 0;
                    }

                    break;

                case 2:
                    timer1Delay = timer1Delay02;
                    int ret;
                    int min=99;
                    String tar,src;
                    String answer="Nope!";
                    if(myView.bGestureRegnize && sampleTable.size() != 0)
                    {
                        src = myView.sFreemanCode;
                        answer = simpleGesture(src);
                        if(answer.compareTo("")==0)
                        {
                            for(int i=0;i<sampleTable.size()-1;i++)
                            {

                                tar = sampleTable.get(i).getTargetString();

                                //if(tar.compareTo(LEFT) == 0 || tar.compareTo(RIGHT)==0 || tar.compareTo("UP")==0 || tar.compareTo("DOWN")==0)
                                //    continue;

                                ret = gj.editDistance(tar.length(),src.length(),tar.getBytes(),src.getBytes());

                                if(min >= ret && ret < src.length()) {
                                    min = ret;
                                    answer = sampleTable.get(i).getCharactor();
                                }

                                Log.d("[ZET]", " src = " + src.length() + " tar = " + tar.length() + " char = " + sampleTable.get(i).getCharactor() + " ret = " + Integer.toString(ret) );
                            }
                        }


                        myView.bGestureRegnize = false;
                        myTextView.setText(answer);

                    }
                    break;

                default:
                    break;
            }
            hTimer1.postDelayed(this, timer1Delay);
        }
    };
}
