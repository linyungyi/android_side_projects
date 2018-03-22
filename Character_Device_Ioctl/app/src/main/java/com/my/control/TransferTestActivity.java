package com.my.control;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Gravity;
//import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.widget.LinearLayout.LayoutParams;  

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class TransferTestActivity extends Activity {

	public static final String LOG_TAG = "[MY]";

	public static final int TIMER_OUT_MAX_CNT = 1000;
	int timerCut = 0;

	GlobalData globalData;
	MyClass myClass;

	Timer timer = new Timer();
	// /---------------------------------------------------------------------///
	// / Timer
	// /---------------------------------------------------------------------///
	protected Handler hTimer1 = new Handler();
	protected int timer1Delay = 50; // /< in ms
	protected int timer1SecDelay = 1000;
	int row;
	int col;
	int tranMode = GlobalData.TRAN_MODE_MUTUAL_DEV;
	int flag = 0;
	byte[] ioctlBuf;
	int prePublicId = 999;

	Bundle bundle;
	Button btn_show_main;
	MutualDevView mdevView; // /< mutual dev view
	MutualBaseView mbaseView; ///< mutual base view
	InitialDevView idevView; ///< initial dev view
	MutualADView madView; ///< mutual base view
	InitialADView iadView; ///< mutual base view
	DynamicView dynView;
	FreqHoppingView freqView;
	ResetDelayIC resetView;
  
	Button btn_reset_ui;	
	EditText edit_main_freq;
	EditText edit_shift_freq;

 	
	@Override
	protected void onResume() {
		super.onResume();
		// finish();
		// if(Global.putFloat(, name, value))
		Log.v(LOG_TAG, "TransferTestActivity onResume()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		// finish();

		Log.v(LOG_TAG, "TransferTestActivity onStop()");
	}

	public static int byteToUnsignedInt(byte b) {
		return 0x00 << 24 | b & 0xff;
	}

	public static int ByteArraryToInt(byte[] byteBarray) {
		return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN)
				.getInt();
	}

	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

	public static byte[] intToByteArray(int a) {
		byte[] ret = new byte[4];
		ret[0] = (byte) (a & 0xFF);
		ret[1] = (byte) ((a >> 8) & 0xFF);
		ret[2] = (byte) ((a >> 16) & 0xFF);
		ret[3] = (byte) ((a >> 24) & 0xFF);
		return ret;
	}

	public static int byteArrayWordToInt(byte bHigh, byte bLow) 
	{
		int value = 0;
		byte[] bData = new byte[4];
		bData[3] = bLow;
		bData[2] = bHigh;
		bData[1] = 0x00;
		bData[0] = 0x00;

		for (int i = 0; i < 4; i++) 
		{
			int shift = (4 - 1 - i) * 8;
			value += (bData[i] & 0x000000FF) << shift;
		}
		return value;
	}
	
	public void ParseDynamicFingerData() 
	{
		int i;
		
		// / read the fram rate
		MyClass.ioctl(GlobalData.MY_CMD_FRAM_RATE, ioctlBuf);
		dynView.frame_rate = ioctlBuf[0];
		globalData.timer1SecFlag = true;
		
		// / read size
		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_SIZE_GET, ioctlBuf);
		globalData.iPackSize = ByteArraryToInt(ioctlBuf);
		/// read the status data
		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);
		for (i = 0; i < globalData.iPackSize; i++) 
		{
			globalData.pDataBuf[i] = ioctlBuf[i];
		}
		
		// / Hover status on the 7 byte & 7 bit
		if ((ioctlBuf[6] & 0x80) > 0) 
		{
			globalData.bHoverStatus = true;
		}
		else
		{
			globalData.bHoverStatus = false;
		}
		// / charger status on the 11 byte & 7 bit
		if ((ioctlBuf[10] & 0x80) > 0)
		{
			globalData.bChargeStauts = true;
		}
		else
		{
			globalData.bChargeStauts = false;
		}
		
		// / water status on the 11 byte & 4 bit
		if ((ioctlBuf[10] & 0x10) > 0) 
		{
			globalData.bWaterStatus = true;
		}
		else
		{
			globalData.bWaterStatus = false;
		}
		// / much water status on the 11 byte & 5 bit
		if ((ioctlBuf[10] & 0x20) > 0) 
		{
			globalData.bInitFingerStatus = true;
		}
		else
		{
			globalData.bInitFingerStatus = false;
		}

		// / Base Tracking Pending status on the 11 byte & 6 bit
		if ((ioctlBuf[10] & 0x40) > 0) 
		{
			globalData.bBaseTrackingPendingStatus = true;
		}
		else
		{
			globalData.bBaseTrackingPendingStatus = false;
		}
	}
	public void ParseFreqHoppingMY6275() 
	{
		int idx = globalData.iFingerDataSize;
		int iData;
		int iRatioOrder;
		int i;
		int iOPT3;
		int iOPT4;		
		float fData;
		float fMainFreq;
		int cmpData;
		boolean bCopy = false;
		int iMax = 0;
		freqView.fMainFreq	= 42;
		freqView.fShiftFreq	= 100;

		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);

		/// DynamicScanCnt
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.DynamicScanCntSet(iData);
		idx = idx + 1;

		/// DynamicFreqSetCnt
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.DynamicFreqSetCntSet(iData);
		idx = idx + 1;		

		/// DynamicRoundCnt
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.DynamicRoundCntSet(iData);
		idx = idx + 1;	

		/// SelectItem
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.SelectItemSet(iData);
		idx = idx + 1;	

		cmpData = freqView.MaxHopping.SelectItemGet();
		if(iData != cmpData)
		{
			freqView.MaxHopping.SelectItemSet(iData);
			bCopy = true;
		}

		
		/// DynamicFreqSetNum
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.DynamicFreqSetNumSet(iData);
		idx = idx + 1;		

		/// Min delta dev TH
		iData = ((ioctlBuf[idx]&0xFF)<<8);
		idx = idx + 1;
		
		iData = iData + (ioctlBuf[idx]&0xFF);
		idx = idx + 1;
		freqView.CurrHopping.MinDeltaDevTHSet(iData);

		/// DynamicFreqSetNum
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.ADCTXRatioSet(iData);
		idx = idx + 1;	
		iRatioOrder=1;
		/// AD-clk convert to TX-clk
		for(i=0;i<iData;i++)
		{
			iRatioOrder=(iRatioOrder)<<1;
		}
		fMainFreq = (freqView.fMainFreq*(freqView.fShiftFreq/100));
		for(i = 0 ; i < globalData.FREQ_HOPPING_MAX_CNT; i++)
		{
			iData = ((ioctlBuf[idx]&0xFF)<<8);
			idx = idx + 1;			
			iOPT4 = iData + (ioctlBuf[idx]&0xFF);
			idx = idx + 1;
			iOPT3 = iOPT4 * iRatioOrder; /// iOpt3 = iOpt4 * (iRatio);
			fData = ((fMainFreq*1000)/(4*iOPT3));
			freqView.CurrHopping.FreqSet(i, fData);		
		}

		/// Get the dev data 
		for(i = 0 ; i < globalData.FREQ_HOPPING_MAX_CNT; i++)
		{
			iData = ((ioctlBuf[idx]&0xFF)<<8);
			idx = idx + 1;
			iData = iData + (ioctlBuf[idx]&0xFF);
			idx = idx + 1;
			freqView.CurrHopping.DeltaDevSet(i, iData);
			cmpData = freqView.MaxHopping.DeltaDevGet(i);
			if(bCopy == true)
			{
				freqView.MaxHopping.DeltaDevSet(i, iData);
			}
			else
			{
				if(cmpData < iData)
				{
					freqView.MaxHopping.DeltaDevSet(i, iData);
				}
			}
			if(iMax < cmpData || iMax < iData)
			{
				iMax = iData;
			}
		}
		/// find the Max detal Data
		if(iMax > freqView.iMaxDetalData)
		{
			freqView.iMaxDetalData = iMax;
			idx = freqView.iMaxDetalData%50;
			freqView.iMaxDetalData = freqView.iMaxDetalData + (50 - idx);		
		}
	}
	public void ParseFreqHoppingMY6231() 
	{
		int idx = globalData.iFingerDataSize;
		int iData;
		int i;	
		float fData;
		float fMainFreq;
		float fTmpData;
		int cmpData;
		int bDynamicFreqSetNum;
		int bDynamicDefaultID;
		int bDynamicIDDelta;
		int bADCCtrl8;		
		int bTrimID;	
		boolean bCopy = false;
		int iMax = 0;
		freqView.fMainFreq	= 36;
		freqView.fShiftFreq	= 100;

		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);

		/// DynamicScanCnt  //0
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.DynamicScanCntSet(iData);
		idx = idx + 1;

		/// DynamicFreqSetCnt //1
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.DynamicFreqSetCntSet(iData);
		idx = idx + 1;		

		/// DynamicRoundCnt //2
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.DynamicRoundCntSet(iData);
		idx = idx + 1;	

		/// bDynamicCurrentID //3
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.SelectItemSet(iData);
		idx = idx + 1;	

		cmpData = freqView.MaxHopping.SelectItemGet();
		if(iData != cmpData)
		{
			freqView.MaxHopping.SelectItemSet(iData);
			bCopy = true;
		}

		
		/// DynamicFreqSetNum //4
		iData = (ioctlBuf[idx]&0xFF);
		bDynamicFreqSetNum = iData;
		freqView.CurrHopping.DynamicFreqSetNumSet(iData);
		idx = idx + 1;		

		/// Min delta dev TH //5 , 6
		iData = ((ioctlBuf[idx]&0xFF)<<8);
		idx = idx + 1;
		
		iData = iData + (ioctlBuf[idx]&0xFF);
		idx = idx + 1;
		freqView.CurrHopping.MinDeltaDevTHSet(iData);

		/// bDynamicDefaultID //7
		bDynamicDefaultID = (ioctlBuf[idx]&0xFF);		
		idx = idx + 1;	

		/// bDynamicIDDelta //8
		bDynamicIDDelta = (ioctlBuf[idx]&0xFF);		
		idx = idx + 1;	
		
		/// bADCCtrl8 //9
		bADCCtrl8 = (ioctlBuf[idx]&0x0F);		
		idx = idx + 1;	

		fMainFreq = (freqView.fMainFreq*(freqView.fShiftFreq/100));
		for(i = 0 ; i < globalData.FREQ_HOPPING_MAX_CNT; i++)
		{
			if( i < bDynamicFreqSetNum)
			{
				bTrimID = bDynamicDefaultID + i*bDynamicIDDelta;
				fData = freqView.CurrHopping.FreqAllTableGet(bTrimID);
				
				fTmpData = ((freqView.fMainFreq * (freqView.fShiftFreq)/100));
				fData = ((fTmpData + (fTmpData* fData/100))/32)*1000;
				
				fData = fData/((2^bADCCtrl8)*4);
				freqView.CurrHopping.FreqSet(i, fData);
				
			}
			else
			{
				freqView.CurrHopping.FreqSet(i, 0);
			}
		}

		/// Get the dev data 
		for(i = 0 ; i < globalData.FREQ_HOPPING_MAX_CNT; i++)
		{
			iData = ((ioctlBuf[idx]&0xFF)<<8);
			idx = idx + 1;
			iData = iData + (ioctlBuf[idx]&0xFF);
			idx = idx + 1;
			freqView.CurrHopping.DeltaDevSet(i, iData);
			cmpData = freqView.MaxHopping.DeltaDevGet(i);
			if(bCopy == true)
			{
				freqView.MaxHopping.DeltaDevSet(i, iData);
			}
			else
			{
				if(cmpData < iData)
				{
					freqView.MaxHopping.DeltaDevSet(i, iData);
				}
			}
			if(iMax < cmpData || iMax < iData)
			{
				iMax = iData;
			}
		}
		/// find the Max detal Data
		if(iMax > freqView.iMaxDetalData)
		{
			freqView.iMaxDetalData = iMax;
			idx = freqView.iMaxDetalData%50;
			freqView.iMaxDetalData = freqView.iMaxDetalData + (50 - idx);		
		}
	}
	public void ParseFreqHopping() 
	{
		int idx = globalData.iFingerDataSize;
		int iData;
		int cmpData;
		int i;
		int iMax = 0;
		float fData;
		float fTmpData;
		boolean bCopy = false;
		int len = 128;
		
		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);

		/// parse ctrl8
		iData = (ioctlBuf[idx]&0x0F);
		freqView.CurrHopping.AdcCtrl8Set(iData);
		idx = idx + 1;
		
		/// user set count
		iData = (ioctlBuf[idx]&0xFF);
		freqView.CurrHopping.UserCntSet(iData);
		idx = idx + 1;
		
		/// Min delta dev TH
		iData = ((ioctlBuf[idx]&0xFF)<<8);
		idx = idx + 1;
		
		iData = iData + (ioctlBuf[idx]&0xFF);
		idx = idx + 1;
		freqView.CurrHopping.MinDeltaDevTHSet(iData);

		/// get the user setting item hopping table 
		for(i = 0 ; i < globalData.FREQ_HOPPING_MAX_CNT; i++)
		{
			iData = (ioctlBuf[idx]&0xFF);
			fData = freqView.CurrHopping.FreqTableGet(iData);
			freqView.CurrHopping.FreqSet(i, fData);
			idx = idx + 1;			
		}

		/// select ID
		iData = ((ioctlBuf[idx]&0xFF));
		idx = idx + 1;
		
		freqView.CurrHopping.SelectItemSet(iData);
		/// check the select id is same the curr & max hopping.
		cmpData = freqView.MaxHopping.SelectItemGet();
		if(iData != cmpData)
		{
			freqView.MaxHopping.SelectItemSet(iData);
			bCopy = true;
		}

		/// collect num idx count
		iData =  (ioctlBuf[idx]&0xFF);
		idx = idx + 1;
		iData = iData +((ioctlBuf[idx]&0xFF)<<8);
		idx = idx + 1;
		freqView.CurrHopping.CollectNumSet(iData);

		/// get the dev data 
		for(i = 0 ; i < globalData.FREQ_HOPPING_MAX_CNT; i++)
		{
			iData = (ioctlBuf[idx]&0xFF);
			idx = idx + 1;
			iData = iData + ((ioctlBuf[idx]&0xFF)<<8);
			idx = idx + 1;
			freqView.CurrHopping.DeltaDevSet(i, iData);
			cmpData = freqView.MaxHopping.DeltaDevGet(i);
			if(bCopy == true)
			{
				freqView.MaxHopping.DeltaDevSet(i, iData);
			}
			else
			{
				if(cmpData < iData)
				{
					freqView.MaxHopping.DeltaDevSet(i, iData);
				}
			}
			if(iMax < cmpData || iMax < iData)
			{
				iMax = iData;
			}
		}
		
		// fData =  ((fMainFreq  + (fMainFreq * TpDevice6251Freq[bBuf[i]].fFreqShift/100))/32)*1000;
		// fData = fData / ((2^CurrFreqHoppingData[k].bctrl8)*4);
		for(i = 0 ; i < globalData.FREQ_HOPPING_MAX_CNT; i++)
		{
			iData = freqView.CurrHopping.AdcCtrl8Get();
			fData = freqView.CurrHopping.FreqGet(i);
			fTmpData = ((freqView.fMainFreq * (freqView.fShiftFreq)/100));
			fData = ((fTmpData + (fTmpData* fData/100))/32)*1000;
			fData = fData/((2^iData)*4);
			freqView.CurrHopping.FreqSet(i, fData);
			idx = idx + 1;			
		}

		/// find the Max detal Data
		if(iMax > freqView.iMaxDetalData)
		{
			freqView.iMaxDetalData = iMax;
			idx = freqView.iMaxDetalData%50;
			freqView.iMaxDetalData = freqView.iMaxDetalData + (50 - idx);		
		}
	}
	
	public void ParseFingerMdevData()
	{
		int len;
		int i;
		int idx;
		byte[] bTmpData = new byte[1024];

		if(globalData.b16BitMutualDev == true)
		{
			len = 2* (col + 2) * (row + 2) + globalData.iFingerDataSize;
		}
		else
		{
			len = (col + 2) * (row + 2) + globalData.iFingerDataSize;
		}
		
		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);
		if (len > GlobalData.MY_IOCTL_PAGE_MAX_SIZE) 
		{
			
			MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_2,bTmpData);
			idx = 0;
			for (i = GlobalData.MY_IOCTL_PAGE_MAX_SIZE; i < len; i++) 
			{
				ioctlBuf[i] = bTmpData[idx];
				idx++;
			}
		}

		for (i = 0; i < (col + 2) * (row + 2); i++) 
		{		
			if(globalData.b16BitMutualDev == true)
			{
				idx = 2*i + globalData.iFingerDataSize;
				if(globalData.bBigEndian == true)
				{
					mdevView.mdevBuf[i] = (short)((ioctlBuf[idx]<< 8) + (ioctlBuf[idx+1]&0xff));
				}
				else
				{
					mdevView.mdevBuf[i] = (short)((ioctlBuf[idx+1]<< 8) + (ioctlBuf[idx]&0xff));
				}
			}
			else
			{
				mdevView.mdevBuf[i] = byteToUnsignedInt(ioctlBuf[i + globalData.iFingerDataSize]);
			}
		}		
	}


	public void ParseMutualADData()
	{
		int i;
		int j;
		int len;			
		int idx;
		byte[] bTmpData = new byte[1024];
		
		len = row * col * 2;

		MyClass.ioctl(GlobalData.MY_CMD_MBASE_GET, ioctlBuf);

		if (len > GlobalData.MY_IOCTL_PAGE_MAX_SIZE) 
		{
			MyClass.ioctl(GlobalData.MY_CMD_MBASE_EXTERN_GET, bTmpData);
			idx = 0;
			for (i = GlobalData.MY_IOCTL_PAGE_MAX_SIZE; i < len; i++) 
			{
				ioctlBuf[i] = bTmpData[idx];
				idx++;
			}
		}

		//msg = String.format("myClass : MY_CMD_MAD_GET\n");
		//Log.v(LOG_TAG, msg);
		// / parse the mutual base data
		for (i = 0; i < (col * row); i++) 
		{
			idx = 2*i;
			if(globalData.bBigEndian == true)
			{
				madView.madBuf[i] = byteArrayWordToInt(ioctlBuf[idx], ioctlBuf[idx+1]);
			}
			else
			{
				madView.madBuf[i] = byteArrayWordToInt(ioctlBuf[idx+1], ioctlBuf[idx]);
			}				
		}		
	}

	public void ParseMutualBaseData()
	{
		int i;
		int j;
		int len;			
		int idx;
		len = row * col * 2;
		byte[] bTmpData = new byte[1024];
		
		MyClass.ioctl(GlobalData.MY_CMD_MBASE_GET, ioctlBuf);
		if (len > GlobalData.MY_IOCTL_PAGE_MAX_SIZE) 
		{
			MyClass.ioctl(GlobalData.MY_CMD_MBASE_EXTERN_GET,bTmpData);
			idx = 0;

			for (i = GlobalData.MY_IOCTL_PAGE_MAX_SIZE; i < len; i++) 
			{
				ioctlBuf[i] = bTmpData[idx];
				idx++;
			}
		}

		// / parse the mutual base data
		for (i = 0; i < (col * row); i++) 
		{
			idx = 2*i;
			if(globalData.bBigEndian == true)
			{
				mbaseView.mbaseBuf[i] = byteArrayWordToInt(ioctlBuf[idx], ioctlBuf[idx+1]);
			}
			else
			{
				mbaseView.mbaseBuf[i] = byteArrayWordToInt(ioctlBuf[idx+1], ioctlBuf[idx]);
			}				
		}	
		
	}
	protected String bytesToString(byte [] buf, int bufLen)
	{
		String str;
		int i;
		for( i = 0 ; i < bufLen ; i++)
		 {
			 if (buf[i] ==0)
			 {
				 break;
			 }
		 }
		 str = new String(buf, Charset.forName("UTF-8"));
		 str = str.substring(0, i);
		
		return str;
	}
	public void ParseMutualDevData()
	{
		int i;
		String msg;
		if(globalData.b16BitMutualDev == true)
		{
			int len;			
			int idx;
			String str1;
			byte[] bTmpData = new byte[1024];
		
			len = 2 * (col + 2) * (row + 2);

			msg = String.format(
					"[MY] b16Bit MutualDev=========================len=%d max=%d",len,GlobalData.MY_IOCTL_PAGE_MAX_SIZE);
			//Log.v(LOG_TAG, msg);

			MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);
			if (len > GlobalData.MY_IOCTL_PAGE_MAX_SIZE) 
			{
				MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_2,bTmpData);
				idx = 0;
				for (i = GlobalData.MY_IOCTL_PAGE_MAX_SIZE; i < len; i++) 
				{
					ioctlBuf[i] = bTmpData[idx];
					idx++;
				}
			}

			for (i = 0; i < (col + 2) * (row + 2); i++) 
			{
				idx = 2*i;
				if(globalData.bBigEndian == true)
				{
					mdevView.mdevBuf[i] = (short)((ioctlBuf[idx]<< 8) + (ioctlBuf[idx+1]&0xff));
				}
				else
				{
					mdevView.mdevBuf[i] = (short)((ioctlBuf[idx+1]<< 8) + (ioctlBuf[idx]&0xff));
				}				
			}
		}
		else
		{
			msg = String.format(
					"[MY] b8BitMutualDev=========================");
			//Log.v(LOG_TAG, msg);

			MyClass.ioctl(GlobalData.MY_CMD_MDEV_GET, ioctlBuf);
			for (i = 0; i < (col + 2) * (row + 2); i++) 
			{
				mdevView.mdevBuf[i] = byteToUnsignedInt(ioctlBuf[i]);
			}
		}		
		
	}


	public void ParseFPCOpenData()
	{
		int i;
		
		if(globalData.b16BitInitDev == true)
   		{
			MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);

			for (i = 0; i < (col + row); i++)
			{
				if(globalData.bBigEndian == true)
				{
					idevView.idevBuf[i] = (short)((ioctlBuf[2*i] << 8) + (ioctlBuf[2*i + 1]&0xff));
				}
				else
				{
					idevView.idevBuf[i] = (short)((ioctlBuf[2*i+1] << 8) + (ioctlBuf[2*i]&0xff));
				}
			}
   		}
		else
		{
			MyClass.ioctl(GlobalData.MY_CMD_FPC_OPEN_GET, ioctlBuf);

			for (i = 0; i < (col + row); i++)
			{
				idevView.idevBuf[i] = byteToUnsignedInt(ioctlBuf[i]);
			}
		}
	}	

	
	public void ParseInitDevData()
	{
		int i;
		
		if(globalData.b16BitInitDev == true)
   		{
			MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_1, ioctlBuf);

			for (i = 0; i < (col + row); i++)
			{
				if(globalData.bBigEndian == true)
				{
					idevView.idevBuf[i] = (short)((ioctlBuf[2*i] << 8) + (ioctlBuf[2*i + 1]&0xff));
				}
				else
				{
					idevView.idevBuf[i] = (short)((ioctlBuf[2*i+1] << 8) + (ioctlBuf[2*i]&0xff));
				}
			}
   		}
		else
		{
			MyClass.ioctl(GlobalData.MY_CMD_IDEV_GET, ioctlBuf);

			for (i = 0; i < (col + row); i++)
			{
				idevView.idevBuf[i] = byteToUnsignedInt(ioctlBuf[i]);
			}
		}
	}


	public void ParseFPCShortData()
	{
		int i;
		int idx;
		
		myClass.ioctl(globalData.MY_CMD_FPC_SHORT_GET, ioctlBuf);
		
		// / parse the initial AD data
		for (i = 0; i < (col + row); i++) 
		{
			idx = i * 2;
		
			if(globalData.bBigEndian == true)
			{
				iadView.iadBuf[i] = byteArrayWordToInt(ioctlBuf[idx], ioctlBuf[idx+1]);
			}
			else
			{
				iadView.iadBuf[i] = byteArrayWordToInt(ioctlBuf[idx + 1], ioctlBuf[idx]);
			}
		}
	}

	public void ParseInitADData()
	{
		int i;
		int idx;
		
		myClass.ioctl(globalData.MY_CMD_IBASE_GET, ioctlBuf);
		
		// / parse the initial AD data
		for (i = 0; i < (col + row); i++) 
		{
			idx = i * 2;
		
			if(globalData.bBigEndian == true)
			{
				iadView.iadBuf[i] = byteArrayWordToInt(ioctlBuf[idx], ioctlBuf[idx+1]);
			}
			else
			{
				iadView.iadBuf[i] = byteArrayWordToInt(ioctlBuf[idx + 1], ioctlBuf[idx]);
			}
		}
	}

	
	private Runnable onTimer1 = new Runnable() {

		public void run() {

			int i;
			int idx;
			int j;
			int len = 0;
			byte[] bTmpData = new byte[1024];

			String msg = String.format(
					"TRAN MODE :%d=========================",
					globalData.tranModeType);
			// Log.v(LOG_TAG, msg);

			// /=======================================================///
			// / Cheak time need close
			// /=======================================================///
			if (globalData.bCloseTimerThread == true) {
				hTimer1.removeCallbacks(onTimer1);
				return;
			}

			// /=======================================================///
			// / MDEV
			// /=======================================================///
			if (globalData.tranModeType == GlobalData.TRAN_MODE_MUTUAL_DEV) {
				if (mdevView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						hTimer1.removeCallbacks(onTimer1);
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					//Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;
				ParseMutualDevData();
				
				if (mdevView.updateId != prePublicId) {
					mdevView.invalidate();
					prePublicId = mdevView.updateId;
				}
			}
			// /=======================================================///
			// / DYNAMIC
			// /=======================================================///
			else if (tranMode == GlobalData.TRAN_MODE_DYNAMIC) {
				if (dynView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;

				/// get the driver data then parse
				ParseDynamicFingerData();

				// / get the timer
				if (dynView.updateId != prePublicId) {
					dynView.invalidate();
					prePublicId = dynView.updateId;
				}

			}
			// /=======================================================///
			// / Reset
			// /=======================================================///
			else if (tranMode == GlobalData.TRAN_MODE_RESET_COUNT_DOWN) {
				if (resetView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}					
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;
				if(globalData.ResetCountDownCnt > 0)
				{
					globalData.ResetCountDownCnt = globalData.ResetCountDownCnt -1;	
				}
				if(globalData.ResetCountDownCnt == 1)
				{
					myClass.ioctl(globalData.MY_CMD_RST, ioctlBuf);
				}
				/// get the driver data then parse
				//ParseDynamicFingerData();

				// / get the timer
				if (resetView.updateId != prePublicId) {
					resetView.invalidate();
					prePublicId = resetView.updateId;
				}

			}
			// /=======================================================///
			// / FPC OPEN
			// /=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_FPC_OPEN) {
				if (idevView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;
				
				ParseFPCOpenData();		

				if (idevView.updateId != prePublicId) {
					idevView.invalidate();
					prePublicId = idevView.updateId;
				}

			}
			// /=======================================================///
			// / Initial DEV
			// /=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_INIT_DEV) {
				if (idevView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;
				
				ParseInitDevData();

				if (idevView.updateId != prePublicId) {
					idevView.invalidate();
					prePublicId = idevView.updateId;
				}

			}
			///=======================================================///
			/// Initial AD
			///=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_INIT_AD) {
				if (iadView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;

				ParseInitADData();
				
				if (iadView.updateId != prePublicId) {
					iadView.invalidate();
					prePublicId = iadView.updateId;
				}
			}
			// /=======================================================///
			// / Finger + Hopping
			// /=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_FINGER_MIX_HOPPING) {
				if (freqView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						hTimer1.removeCallbacks(onTimer1);
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;

				//String msg = String.format("========= IC : %d ========= ",globalData.bIcModeId);
				//Log.v(LOG_TAG, msg);
				
				if(globalData.bIcModeId == 0x0F || globalData.bIcModeId == 0x11) //MY6275 =0x0F 7130 = 0x11
				{
					ParseFreqHoppingMY6275();
				}
				else if(globalData.bIcModeId == 0x0B) //MY6231 =0x0F
				{
					ParseFreqHoppingMY6231();
				}
				else
				{
					ParseFreqHopping();
				}
				

				if (freqView.updateId != prePublicId) {
					freqView.invalidate();
					prePublicId = freqView.updateId;
				}
			}
			// /=======================================================///
			// / Finger + MDEV
			// /=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_FINGER_MIX_MUTUAL_DEV) {
				if (mdevView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						hTimer1.removeCallbacks(onTimer1);
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;
				
				ParseFingerMdevData();

				if (mdevView.updateId != prePublicId) {
					mdevView.invalidate();
					prePublicId = mdevView.updateId;
				}
			}
			// /=======================================================///
			// / FPC SHORT
			// /=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_FPC_SHORT) {
				if (iadView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;

				ParseFPCShortData();					

				if (iadView.updateId != prePublicId) {
					iadView.invalidate();
					prePublicId = iadView.updateId;
				}
			}

			// /=======================================================///
			// / Mutual BASE
			// /=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_MUTUAL_BASE) {
				if (mbaseView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;
				ParseMutualBaseData();				
				
				if (mbaseView.updateId != prePublicId) {
					mbaseView.invalidate();
					prePublicId = mbaseView.updateId;
				}
			}

			// /=======================================================///
			// / Mutual AD
			// /=======================================================///
			else if (globalData.tranModeType == GlobalData.TRAN_MODE_MUTUAL_AD) {
				if (madView.updateId == prePublicId) {
					if (timerCut >= TIMER_OUT_MAX_CNT) {
						return;
					}
					msg = String.format("TIMER1:%d=========================",
							timerCut);
					Log.v(LOG_TAG, msg);
					hTimer1.postDelayed(this, timer1Delay);
					timerCut = timerCut + 1;
					return;
				}
				timerCut = 0;

				ParseMutualADData();				
				
				if (madView.updateId != prePublicId) {
					madView.invalidate();
					prePublicId = madView.updateId;
				}
			}
			
			//Log.v(LOG_TAG, "TIMER1 : on_timer");
			hTimer1.postDelayed(this, timer1Delay);
		}
	};

	protected void onStop(Bundle savedInstanceState) {
		String msg;

		msg = String.format("onStop tranMode = %d", tranMode);
		Log.v(LOG_TAG, msg);

		hTimer1.removeCallbacks(onTimer1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String msg;
		super.onCreate(savedInstanceState);
		// /--------------------------------------------------------///
		// / Global Data
		// /--------------------------------------------------------///
		globalData = ((GlobalData) getApplicationContext());
		myClass = globalData.get_myClass();

		Bundle bunde = this.getIntent().getExtras();
		timer1Delay = 30;	
		row = bunde.getInt("row");
		col = bunde.getInt("col");
		tranMode = globalData.tranModeType;
		
		if (globalData.bInitAp == true) {
			if (row != globalData.iTraceYCnt) {
				row = globalData.iTraceYCnt;
			}
			if (col != globalData.iTraceXCnt) {
				col = globalData.iTraceXCnt;
			}
		}
		
		msg = String.format("onCreate tranMode = %d", tranMode);
		Log.v(LOG_TAG, msg);

		msg = String.format("Display row : %d , col : %d", row, col);
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

		globalData.bCloseTimerThread = false;

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// / landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// /=================================================================///
		// / MDEV
		// /=================================================================///
		if (tranMode == GlobalData.TRAN_MODE_MUTUAL_DEV) {
			setContentView(R.layout.activity_mdev);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_mdev);
			mdevView = new MutualDevView(this, row, col, globalData);

			mdevView.setMinimumHeight(500);

			mdevView.setMinimumWidth(300);

			layout.addView(mdevView);
		}
		// /=================================================================///
		// / dynamic
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_DYNAMIC) {

			timer1Delay = 20;
			setContentView(R.layout.activity_dynamic);
			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_dynamic);
			dynView = new DynamicView(this, row, col, globalData);
			dynView.setMinimumHeight(500);
			dynView.setMinimumWidth(300);
			layout.addView(dynView);				

		}
		// /=================================================================///
		// / Reset
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_RESET_COUNT_DOWN) {

			timer1Delay = 20;						
			setContentView(R.layout.activity_reset);
			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_reset);
			resetView = new ResetDelayIC(this,globalData);
			layout.addView(resetView);	
			
		}
		// /=================================================================///
		// / FPC OPEN (used the idev view display)
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_FPC_OPEN) {
			setContentView(R.layout.activity_idev);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_idev);
			idevView = new InitialDevView(this, row, col, globalData);

			idevView.setMinimumHeight(500);

			idevView.setMinimumWidth(300);

			layout.addView(idevView);
		}
		// /=================================================================///
		// / FPC short (used the iad view display)
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_FPC_SHORT) {
			setContentView(R.layout.activity_iad);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_iad);
			iadView = new InitialADView(this, row, col, globalData);

			iadView.setMinimumHeight(500);

			iadView.setMinimumWidth(300);

			layout.addView(iadView);
		}
		// /=======================================================///
		// / Finger + MDEV
		// /=======================================================///
		else if (tranMode == GlobalData.TRAN_MODE_FINGER_MIX_MUTUAL_DEV) {
			setContentView(R.layout.activity_mdev);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_mdev);
			mdevView = new MutualDevView(this, row, col, globalData);

			mdevView.setMinimumHeight(500);

			mdevView.setMinimumWidth(300);

			layout.addView(mdevView);
		
		}

		// /=================================================================///
		// / Hopping
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_FINGER_MIX_HOPPING) {
			setContentView(R.layout.activity_hopping);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_hopping);
			LinearLayout layout2 = (LinearLayout) findViewById(R.id.layout_hopping_data);
			

			freqView = new FreqHoppingView(this, globalData);

			freqView.setMinimumHeight(500);

			freqView.setMinimumWidth(300);

			layout.addView(freqView);

			/*
			/// freq. hopping event
			btn_reset_ui = (Button)findViewById(R.id.btn_reset_ui_id);
		 	edit_main_freq = (EditText)findViewById(R.id.edit_main_freq);
		 	edit_shift_freq = (EditText)findViewById(R.id.edit_shift_freq);
		 	
			///--------------------------------------------------------///		
			/// hopping main freq.
			///--------------------------------------------------------///		
			edit_main_freq.addTextChangedListener(new TextWatcher() {
		  		@Override
		  		public void afterTextChanged(Editable s) {
		  			// TODO Auto-generated method stub
		         		Log.v(LOG_TAG, "edit_main_freq");
		  		}
		  		@Override
		  		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		  			// TODO Auto-generated method stub
		  		}
		  		@Override
		  		public void onTextChanged(CharSequence s, int start, int before, int count) {
		  			//doSomething();
	         		Log.v(LOG_TAG, "edit_main_freq onTextChanged");
		  
		  		} 
		 	});
		  
			///--------------------------------------------------------///		
			/// hopping shift freq.
			///--------------------------------------------------------///		
			edit_shift_freq.addTextChangedListener(new TextWatcher() {
		  		@Override
		  		public void afterTextChanged(Editable s) {
		  			// TODO Auto-generated method stub
		         		Log.v(LOG_TAG, "edit_shift_freq");
		  		}
		  		@Override
		  		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		  			// TODO Auto-generated method stub
		  		}
		  		@Override
		  		public void onTextChanged(CharSequence s, int start, int before, int count) {
		  			//doSomething();
	         		Log.v(LOG_TAG, "edit_shift_freq onTextChanged");
		  
		  		} 

	  		});

			///--------------------------------------------------------///		
			/// hopping reset ui
			///--------------------------------------------------------///		
			btn_reset_ui.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{   
					Log.v(LOG_TAG, "Tbtn_reset_ui");
				}
			});
			// */ 
		}
		// /=================================================================///
		// / INIT DEV
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_INIT_DEV) {
			setContentView(R.layout.activity_idev);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_idev);
			idevView = new InitialDevView(this, row, col, globalData);

			idevView.setMinimumHeight(500);

			idevView.setMinimumWidth(300);

			layout.addView(idevView);
		}
		// /=================================================================///
		// / INIT AD
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_INIT_AD) {
			setContentView(R.layout.activity_iad);

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_iad);
			iadView = new InitialADView(this, row, col, globalData);

			iadView.setMinimumHeight(500);

			iadView.setMinimumWidth(300);

			layout.addView(iadView);
		}

		// /=================================================================///
		// / MBASE
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_MUTUAL_BASE) {
			setContentView(R.layout.activity_mbase);

			Log.v(LOG_TAG, "globalData.TRAN_MODE_MUTUAL_BASE");

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_mbase);
			mbaseView = new MutualBaseView(this, row, col, globalData);

			mbaseView.setMinimumHeight(500);

			mbaseView.setMinimumWidth(300);

			layout.addView(mbaseView);
		}
		// /=================================================================///
		// / MAD
		// /=================================================================///
		else if (tranMode == GlobalData.TRAN_MODE_MUTUAL_AD) {
			setContentView(R.layout.activity_mad);

			Log.v(LOG_TAG, "globalData.TRAN_MODE_MUTUAL_AD");

			LinearLayout layout = (LinearLayout) findViewById(R.id.layout_mad);
			madView = new MutualADView(this, row, col, globalData);

			madView.setMinimumHeight(500);

			madView.setMinimumWidth(300);

			layout.addView(madView);
		}
		// /=================================================================///
		// / MAD
		// /=================================================================///
		//else if (tranMode == GlobalData.TRAN_MODE_DYNAMIC) {
		//	timer1Delay = 20;
		//	if(globalData.ResetCountDownCnt > 0)
		//	{			
		//		setContentView(R.layout.activity_dynamic);
		//		Log.v(LOG_TAG, "AAAAAAAA");
		//		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_dynamic);

		//		resetView = new ResetDelayIC(this);
		//		globalData.ResetCountDownCnt = globalData.ResetCountDownCnt -1;
		//		layout.addView(madView);				
		//	}			
		//}
		ioctlBuf = new byte[2048];

		// /--------------------------------------------------------///
		// / 1. Start Timer#1
		// /--------------------------------------------------------///
		hTimer1.removeCallbacks(onTimer1);
		hTimer1.postDelayed(onTimer1, timer1Delay);

	}


}
