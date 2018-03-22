package com.my.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
//import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
//import android.view.Display;
import android.view.View;
//import android.os.Handler;
import android.view.MotionEvent;
//import android.view.Display;

class DynamicFinger {
   public int x;
   public int y;
   public boolean bVaild;
   public String str;
   public DynamicFinger() {
   		this.bVaild = false;
   }
   public String toString() {
	  str = String.format("(%d, %d)", x,y);
      return str;
   }
}


public class DynamicView extends View{
	public static final String LOG_TAG = "[MY]";
	int row;
	int col;
	int i;
	int cellWidth;
	int cellHeight;
	public int updateId = 0;
	Paint p = new Paint();  
    Paint penPaint = new Paint();
	Rect rect = new Rect();
	String strVal;
	GlobalData globalData;

	int screenWidth;
	int screenHeight;
	
	int frame_rate = 0;
	int last_ap_frame_rate = 0;
	int ap_frame_rate = 0;
	//DynamicFinger [] fingerData = new DynamicFinger[16];
	int [] FingerX = new int[16];
	int [] FingerY = new int[16];
	boolean [] FingerVaild = new boolean[16];
	
	
	public void init(Context context)
	{       	
		String msg = String.format("row = %d, col = %d", row, col);
		Log.v(LOG_TAG, msg);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels; 
		screenHeight = dm.heightPixels;
	//	for(i = 0 ; i < 16; i++)
	//	{
		//	fingerData[i] = new DynamicFinger();
	//	}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		String strLog = "";
		int pointerCount = 0;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				ap_frame_rate = ap_frame_rate + 1;
				pointerCount = event.getPointerCount();
				for(int i=0; i < pointerCount; i++){
					strLog = strLog + "TOUCH DOWN (" + String.format("%5d",(int) event.getX(i)) +"," + String.format("%5d",(int) event.getY(i)) +")";
				}
				strLog = strLog + '\n';
				break;
			case MotionEvent.ACTION_MOVE:
				ap_frame_rate = ap_frame_rate + 1;
				pointerCount = event.getPointerCount();
				for(int i=0; i < pointerCount; i++){
					strLog = strLog + "TOUCH MOVE (" + String.format("%5d",(int) event.getX(i)) +"," + String.format("%5d",(int) event.getY(i)) +")";
				}
				strLog = strLog + '\n';
				break;
			case MotionEvent.ACTION_UP:
				ap_frame_rate = ap_frame_rate + 1;
				strLog = strLog + "TOUCH UP ";
				strLog = strLog + '\n';
				break;
		}
		globalData.TouchEventLog.WriteLog(strLog,true,true);
		return true;
	}  

	public DynamicView(Context context) {
		super(context);    
        init(context);
		// TODO Auto-generated constructor stub
	}

	public DynamicView(Context context, int rows, int cols, GlobalData gData) {  
        super(context);
        row = rows;
        col = cols;        
		globalData = gData;
        init(context);
    }  	

	@Override  
    protected void onDraw(Canvas canvas)
	{  
		super.onDraw(canvas);  

		drawFramRate(canvas);
       
	}

	public void drawFramRate(Canvas canvas)
	{
		int color;
		int yStart = 30;
		int yInterval = 20;
		int y;
		int i;
		int idx = 0;
		int fingerCnt;
		int fingerNum;
		int vaild;
		String str;
		String strlog="";
		String strtmp;

		int reportX;
		int reportY;
		
        updateId = (updateId + 1)%1000;
        
        penPaint.setColor(Color.WHITE);

    	y = yStart;	

		strVal = "Rate(Hz):";
		if(frame_rate <10)
		{
			strVal = strVal +" ";
		}		
		if(frame_rate <100)
		{
			strVal = strVal +" ";
		}
        strVal = strVal + Integer.toString(frame_rate);
    	canvas.drawText(strVal, 0, y, penPaint);
		strlog = strlog + strVal;


		//------------------------------------------------//
		//  Raw Data
		/*/------------------------------------------------//		
		strVal = "Raw Data : ";
		for (i = 0; i < globalData.iPackSize; i++) 
		{
			if(i == 0)
			{
				str = String.format("%02X", globalData.pDataBuf[i]);
			}
			else
			{
				str = String.format("-%02X", globalData.pDataBuf[i]);
			}
			strVal = strVal + str;
			
		}
		strlog = strlog + strVal;		
    	y = y + yInterval;
    	canvas.drawText(strVal, 0, y, penPaint);
		//------------------------------------------------//*/
		
		//------------------------------------------------//
		//  Finger Data (x,y)
		//------------------------------------------------//
		vaild =  (globalData.pDataBuf[1]*256 | (globalData.pDataBuf[2]));
		fingerNum = 0;
		for(i = 0 ; i< 16;i++)
		{
			if(((vaild >> (15-i)) & 0x01) == 0x01)
			{
				FingerVaild[i] = true;//fingerNum = fingerNum + 1;
			}
			else
			{
				FingerVaild[i] = false;
			}
		}
		
		strVal = " Finger:";
		fingerCnt =  (globalData.iPackSize - 3)/4;
		fingerNum = 0;
		for(i = 0 ; i< fingerCnt; i++)
		{
			reportX = (globalData.pDataBuf[3+i*4]>>4)& 0x0F;
			reportX = (reportX*256) + (globalData.pDataBuf[3+(i*4)+1]&0xFF);
			reportY = (globalData.pDataBuf[3+i*4]&0x0F);
			reportY = (reportY*256) + (globalData.pDataBuf[3+(i*4)+2]&0xFF);
			if(FingerVaild[i] == true)
			{
				FingerX[i] = reportX;
				FingerY[i] = reportY;//FingerData(reportX,reportY);
				fingerNum++;
			}
		}
		
		idx = 0;
		for(i = 0 ; i< fingerCnt; i++)
		{
			//if(FingerVaild[i] == true)
			{			
				//str = String.format("(%d , %d) , ",  FingerX[i], FingerY[i]);			
				if(FingerX[i] <10)
				{
					str = String.format("(   %d,",  FingerX[i]);
				}		
				else if(FingerX[i] <100)
				{
					str = String.format("(  %d,",  FingerX[i]);
				}
				else if(FingerX[i] <1000)
				{
					str = String.format("( %d,",  FingerX[i]);
				}
				else
				{
					str = String.format("(%d,",  FingerX[i]);
				}

				if(FingerY[i] <10)
				{
					str = str+String.format("   %d)",  FingerY[i]);
				}		
				else if(FingerY[i] <100)
				{
					str = str+String.format("  %d)",  FingerY[i]);
				}
				else if(FingerY[i] <1000)
				{
					str = str+String.format(" %d)",  FingerY[i]);
				}
				else
				{
					str = str+String.format("%d)",  FingerY[i]);
				}

				strVal = strVal + str;
				idx++;
			}
		}
    	y = y + yInterval;
    	canvas.drawText(strVal, 0, y, penPaint);
		strlog = strlog + strVal;

		//------------------------------------------------//
		// Hover State
		//------------------------------------------------//		
		if(globalData.bHoverStatus == true)
		{
			color = this.getResources().getColor(
					com.my.control.R.color.COLOR_YELLOW);
        	penPaint.setColor(color);	
			strlog = strlog + " Hover ON ";
		}
		else
		{
        	penPaint.setColor(Color.BLACK);	
			strlog = strlog + " Hover OFF";
		}
    	y = y + yInterval;
        strVal = "Hover";
    	canvas.drawText(strVal, 0, y, penPaint);
		//------------------------------------------------//

		//------------------------------------------------//
		// Charger State
		//------------------------------------------------//
		if(globalData.bChargeStauts == true)
		{
			color = this.getResources().getColor(
					com.my.control.R.color.COLOR_YELLOW);
        	penPaint.setColor(color);	
			strlog = strlog + " Charge ON ";
		}
		else
		{
        	penPaint.setColor(Color.BLACK);	
			strlog = strlog + " Charge OFF";
		}
    	y = y + yInterval;
        strVal = "Charger";
    	canvas.drawText(strVal, 0, y, penPaint);
		//------------------------------------------------//

		//------------------------------------------------//
		// Water State
		/*/------------------------------------------------//		
		if(globalData.bWaterStatus == true)
		{
			color = this.getResources().getColor(
					com.my.control.R.color.COLOR_YELLOW);
        	penPaint.setColor(color);	
		}
		else
		{
        	penPaint.setColor(Color.BLACK);	
		}
    	y = y + yInterval;
        strVal = "Finger In Water";
    	canvas.drawText(strVal, 0, y, penPaint);
		//------------------------------------------------/*/	

		//------------------------------------------------//
		// INIT FINGER State
		/*/------------------------------------------------//
		if(globalData.bInitFingerStatus == true)
		{
			color = this.getResources().getColor(
					com.my.control.R.color.COLOR_YELLOW);
        	penPaint.setColor(color);	
		}
		else
		{
        	penPaint.setColor(Color.BLACK);	
		}
    	y = y + yInterval;
        strVal = "INIT FINGER";
    	canvas.drawText(strVal, 0, y, penPaint);
		//------------------------------------------------/*/

		//------------------------------------------------//
		// Base Tracking Pending State
		/*/------------------------------------------------//
		if(globalData.bBaseTrackingPendingStatus == true)
		{
			color = this.getResources().getColor(
			com.my.control.R.color.COLOR_YELLOW);
        	penPaint.setColor(color);	
		}
		else
		{
        	penPaint.setColor(Color.BLACK);	
		}
    	y = y + yInterval;
        strVal = "Base Tracking Pending";
    	canvas.drawText(strVal, 0, y, penPaint);
		//------------------------------------------------/*/

		if(vaild==0)
		{
			strlog = strlog + " 0 ";
		}else
		{
			strlog = strlog + " 1 ";
		}

		strlog = strlog +'\n';
		globalData.writeLogFile(globalData.LogoutFile, strlog, true);
		
        /// draw the grid
        /// frist y line
        //x = 0;
        //y = height;
    	//for(i = 1 ; i < row; i++)
    //	{
    	//	canvas.drawLine(x, y, screenWidth, y,  penPaint);
    	//	y = y + height;
    	//}
    	/// send x line
        //x = width;
        //y = 0;
    	//for(i = 1 ; i < col; i++)
    	//{
    	//	canvas.drawLine(x, y, x, screenHeight,  penPaint);
    	//	x = x + width;
    	//}
	}
}
