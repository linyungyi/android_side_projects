package com.my.gesture;

/**
 * Created by linyu on 2016/6/11.
 */
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

public class MyView extends View {

    public static final String LOG_TAG = "[ZET]";
    MyLog log0;
    MyLog log1;
    int ShowCanvasPressure = 1;

    ////< Location
    private ArrayList<xy> drawList;
    boolean touching = false;
    Paint paintTouchPointer_ONE;
    Paint paintTouchPointer_TWO;
    Paint paintTouchPointer;
    Paint paintWhiteText;
    String strPressure;
    int intPressure;
    private ArrayList<xy> gestureList;

    ////< Gesture
    String GestureCharacter;
    String logString="";
    String logXYString="";
    int intFreemanCode;
    int intPreviousFreemanCode;
    String sFreemanCode;
    boolean bGestureRegnize;
    boolean bGestureDatabase;
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

    public MyView(Context context) {
        super(context);
        init();

        Log.d("MyView", "1");
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        Log.d("MyView", "2");
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

         Log.d("MyView", "3");
    }

    public MyView(Context context, MyLog log) {
        super(context);
        init();
        log0 = log;

        Log.d("MyView", "4");
    }

    public void init(){

        bGestureRegnize = false;
        bGestureDatabase = false;
        GestureCharacter = C;

        ////< Location
        paintTouchPointer = new Paint();
        paintTouchPointer.setColor(Color.RED);
        paintTouchPointer.setStyle(Paint.Style.FILL);
        //paintTouchPointer.setAlpha(50);

        paintTouchPointer_ONE = new Paint();
        paintTouchPointer_ONE.setColor(Color.BLUE);
        paintTouchPointer_ONE.setStyle(Paint.Style.STROKE);

        paintTouchPointer_TWO = new Paint();
        paintTouchPointer_TWO.setColor(Color.GRAY);
        paintTouchPointer_TWO.setStyle(Paint.Style.FILL);
        paintTouchPointer_TWO.setAlpha(100);

        paintWhiteText = new Paint();
        paintWhiteText.setColor(Color.WHITE);
        paintWhiteText.setStyle(Paint.Style.FILL);
        paintWhiteText.setTextSize(20);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.GRAY);

        ////< location
        if(touching){
            for(xy pt : drawList){

                float PRESSURE_ONE_RADIUS = 5;
                float radius = pt.getPressure() * PRESSURE_ONE_RADIUS + PRESSURE_ONE_RADIUS;

                intPressure = (int)(pt.getPressure()*256);
                strPressure = "pressure = " + intPressure ;

                //pt.logValues();
                if(ShowCanvasPressure == 1)
                {
                    canvas.drawCircle(
                            pt.getX(),
                            pt.getY(),
                            PRESSURE_ONE_RADIUS,
                            paintTouchPointer_ONE);

                    canvas.drawCircle(
                            pt.getX(),
                            pt.getY(),
                            radius,
                            paintTouchPointer);

                }else ////< ShowCanvasPressure == 2
                {
                    canvas.drawCircle(
                            pt.getX(),
                            pt.getY(),
                            PRESSURE_ONE_RADIUS,
                            paintTouchPointer);

                    canvas.drawCircle(
                            pt.getX(),
                            pt.getY(),
                            radius,
                            paintTouchPointer_ONE);

                    canvas.drawCircle(
                            pt.getX(),
                            pt.getY(),
                            radius,
                            paintTouchPointer_TWO);
                }
                canvas.drawText(strPressure, pt.getX() - 30, pt.getY() - 20 - radius, paintWhiteText);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //String strLog = "";
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch(action){
            case MotionEvent.ACTION_DOWN:
                //Log.d("MyView", "ACTION_DOWN");
                gestureList = new ArrayList<xy>();
                bGestureRegnize = false;
            case MotionEvent.ACTION_MOVE:
                //Log.d("MyView", "ACTION_MOVE");
                bGestureRegnize = false;
                ArrayList<xy> touchList = new ArrayList<xy>();
                int pointerCount = event.getPointerCount();

                for(int i=0; i < pointerCount; i++){
                    //strLog = strLog + "(" + String.format("%5d",(int) event.getX(i)) +"," + String.format("%5d",(int) event.getY(i)) +")";
                    touchList.add(
                            new xy(
                                    event.getX(i),
                                    event.getY(i),
                                    event.getPressure(i),
                                    i));
                    if(i==0)
                    {
                        gestureList.add(
                                new xy(
                                        event.getX(i),
                                        event.getY(i),
                                        event.getPressure(i),
                                        i));
                    }
                }
                drawList = touchList;
                touching = true;
                //strLog = strLog + '\n';
                //log0.WriteLog(strLog,true,true);
                break;

            case MotionEvent.ACTION_UP:
                //Log.d("MyView", "ACTION_UP");
                touching = false;

                intPreviousFreemanCode = 99;
                logString = GestureCharacter+"," + Integer.toString(getGestureID(GestureCharacter))+",";
                sFreemanCode = "";

                logXYString = "";
                for(int i=0;i<gestureList.size()-1;i++)
                {
                    ////< location log
                    int px = (int)gestureList.get(i).getX();
                    int py = (int)gestureList.get(i).getY();
                    logXYString = logXYString + String.format(", 0x%02x, 0x%02x, 0x%02x, 0x%02x",(px>>8&0x0f),(px&0xff),(py>>8&0x0f),(py&0xff));
                    if(i == gestureList.size()-2)
                    {
                        px = (int)gestureList.get(i+1).getX();
                        py = (int)gestureList.get(i+1).getY();
                        logXYString = logXYString + String.format(", 0x%02x, 0x%02x, 0x%02x, 0x%02x",(px>>8&0x0f),(px&0xff),(py>>8&0x0f),(py&0xff));
                    }
                    ////< freeman code log
                    intFreemanCode = getFreemancode(gestureList.get(i),gestureList.get(i+1));
                    //if(intFreemanCode != intPreviousFreemanCode) {
                        logString = logString + Integer.toString(intFreemanCode);//logString + "," + Integer.toString(intFreemanCode);
                        sFreemanCode = sFreemanCode+Integer.toString(intFreemanCode);
                    //}
                    //intPreviousFreemanCode = intFreemanCode;
                }
                logString += "\n";
                if(bGestureDatabase)
                    log0.WriteLog(logString,true,false);

                Log.d(LOG_TAG, "fcode: "+sFreemanCode);

                logXYString = String.format("0x00, 0x%02X, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00",gestureList.size()) + logXYString;
                log1.WriteLog(logXYString,false,false);

                bGestureRegnize = true;
                break;
            default:
                touching = false;
        }

        invalidate();
        return true;
    }

    int getFreemancode(xy p0,xy p1)
    {
        float xi,yi;
        int code=8;
        xi = p1.getX() - p0.getX();
        yi = p1.getY() - p0.getY();

        if( xi > 0 && yi == 0 )
        {
            code = 0;
        }else if( xi > 0 && yi < 0 )
        {
            code = 1;
        }else if( xi == 0 && yi < 0 )
        {
            code = 2;
        }else if( xi < 0 && yi < 0 )
        {
            code = 3;
        }else if( xi < 0 && yi == 0 )
        {
            code = 4;
        }else if( xi < 0 && yi > 0 )
        {
            code = 5;
        }else if( xi == 0 && yi > 0 )
        {
            code = 6;
        }else if( xi > 0 && yi > 0 )
        {
            code = 7;
        }

        return code;
    }

    int getGestureID(String c)
    {
        int id;
        switch(c)
        {
            case LEFT:
                id = 0x20;
                break;
            case RIGHT:
                id = 0x21;
                break;
            case UP:
                id = 0x22;
                break;
            case DOWN:
                id = 0x23;
                break;
            case O:
                id = 0x30;
                break;
            case W:
                id = 0x31;
                break;
            case M:
                id = 0x32;
                break;
            case E:
                id = 0x33;
                break;
            case L:
                id = 0x44;
                break;
            case S:
                id = 0x46;
                break;
            case V:
                id = 0x54;
                break;
            case Z:
                id = 0x65;
                break;
            case C:
                id = 0x34;
                break;
            default:
                id = 0;
        }
        return id;
    }

    class xy{
        float x;
        float y;
        float pressure;
        int   index;

        public xy(float x, float y, float pressure, int index){
            this.x = x;
            this.y = y;
            this.pressure = pressure;
            this.index = index;
        }

        public float getX(){
            return x;
        }

        public float getY(){
            return y;
        }

        public float getPressure(){
            return pressure;
        }

        public void logValues()
        {
            Log.d("MyView", "Index=" + String.valueOf(index) + " X=" + String.valueOf((int)x) + " Y=" + String.valueOf((int)y) + " Pressure=" + String.valueOf(pressure));
        }
    }

}
