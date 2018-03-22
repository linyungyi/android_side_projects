package com.my.control;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.os.Bundle;




public class ResetDelayIC extends View{
	int screenWidth;
	int screenHeight;
	public int updateId = 0;
	Bundle bundle;
	String strVal;
	GlobalData globalData;
	Paint penPaint = new Paint();
	
	/*@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	*/
	public void init(Context context)
	{ 
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels; 
		screenHeight = dm.heightPixels;		
	}
	
	public ResetDelayIC(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}
	public ResetDelayIC(Context context,  GlobalData gData) {  
        super(context);       
		globalData = gData;
        init(context);
    }
	@Override  
    protected void onDraw(Canvas canvas)
	{  
        super.onDraw(canvas);  
        drawGrid(canvas);       
	}
	public void drawGrid(Canvas canvas)
	{
		int val;
		int fontsize;			
		updateId = (updateId + 1)%1000;
		fontsize = (screenWidth/16);
		penPaint.setTextSize(fontsize);
		if(globalData.ResetCountDownCnt > 0)
		{		
			val = globalData.ResetCountDownCnt;
			penPaint.setColor(Color.BLUE);
			strVal = "Reset Countdown: " + String.format("%-3d", val);			
		}
		else
		{	
			penPaint.setColor(Color.RED);	
			strVal = "Touch IC Reset !!!";				
		}
		canvas.drawText(strVal,screenWidth/4, screenHeight/2, penPaint);	
	}
	
	


	
	
}
