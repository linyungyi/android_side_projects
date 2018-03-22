package com.my.control;

import java.util.ArrayList;

import android.app.Application;
import android.text.format.DateFormat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class  GlobalData extends Application {
    
	private MyClass myClass =null;
	public static final String LOG_TAG = "[MY]";
	///=========================================================///
	/// MY CMD 
	///=========================================================///
	public static final int MY_CMD_FLASH_READ 			=  20;	
	public static final int MY_CMD_FLASH_WRITE 		=  21;
	public static final int MY_CMD_RST 				=  22;
	public static final int MY_CMD_RST_HIGH 			=  23;
	public static final int MY_CMD_RST_LOW 			=  24;
	
	public static final int MY_CMD_DYNAMIC				=  25;	
	
	public static final int MY_CMD_FLASH_PATH_GET 		=  26;	
	public static final int MY_CMD_FLASH_PATH_SET 		=  27;
	
	public static final int MY_CMD_MDEV 				=  28;
	public static final int MY_CMD_MDEV_GET	 		=  29;
		
	public static final int MY_CMD_TRAN_TYPE_PATH_GET	=  30;	
	public static final int MY_CMD_TRAN_TYPE_PATH_SET	=  31;	
	
	public static final int MY_CMD_IDEV				=  32;	
	public static final int MY_CMD_IDEV_GET			=  33;	
		
	public static final int MY_CMD_MBASE				=  34;	
	public static final int MY_CMD_MBASE_GET			=  35;	
		
	public static final int MY_CMD_INFO				=  36;	
	public static final int MY_CMD_INFO_GET			=  37;	
		
	public static final int MY_CMD_TRACE_X				=  38;	
	public static final int MY_CMD_TRACE_X_GET			=  39;
		
	public static final int MY_CMD_TRACE_Y				=  40;	
	public static final int MY_CMD_TRACE_Y_GET			=  41;

	public static final int MY_CMD_IBASE				=  42;	
	public static final int MY_CMD_IBASE_GET			=  43;	

	public static final int MY_IOCTL_CMD_DRIVER_VER_GET 		=  44;		
	public static final int MY_CMD_MBASE_EXTERN_GET 			=  45;		

	public static final int MY_CMD_GPIO_HIGH				= 46;
	public static final int MY_CMD_GPIO_LOW				= 47;
	public static final int MY_CMD_SENID_GET				= 48;
	public static final int MY_CMD_PCODE_GET				= 49;

	public static final int MY_CMD_TRACE_X_NAME_SET		= 50;
	public static final int MY_CMD_TRACE_X_NAME_GET		= 51;
	
	public static final int MY_CMD_TRACE_Y_NAME_SET		= 52;
	public static final int MY_CMD_TRACE_Y_NAME_GET		= 53;
	
	public static final int MY_CMD_WRITE_CMD				= 54;
	public static final int MY_CMD_FINGER_DATA    			= 55;

	public static final int MY_CMD_FRAM_RATE    			= 56;

	public static final int MY_CMD_FPC_OPEN_SET			= 57;
	public static final int MY_CMD_FPC_OPEN_GET	  		= 58;

	public static final int MY_CMD_FPC_SHORT_SET			= 59;
	public static final int MY_CMD_FPC_SHORT_GET	  		= 60;

	public static final int MY_CMD_REPORT_DATA_1			= 61;
	public static final int MY_CMD_REPORT_DATA_2			= 62;
	
	public static final int MY_CMD_REPORT_DATA_SIZE_GET	= 63;
	public static final int MY_CMD_REPORT_DATA_SIZE_SET	= 64;

	public static final int MY_CMD_TRAN_TYPE_SET		    = 65;
	
	public static final int MY_IOCTL_PAGE_MAX_SIZE			= 1024;
	
//	��ӣ�������ָ��ֱ��ʶ�ȡ��ʾ����
	public static final int MY_CMD_FINGER_XMAX_YMAX_GET	= 66;
	//public static final int MY_CMD_X_MAX_GET		    = 67;
	//public static final int MY_CMD_Y_MAX_GET		    = 68;
	
	///=========================================================///
	/// MY TRAN MODE TYPE 
	///=========================================================///
	public static final int TRAN_MODE_DYNAMIC				=  0;
	public static final int TRAN_MODE_MUTUAL_BASE	 		=  1;	
	public static final int TRAN_MODE_MUTUAL_DEV	 		=  2;
	public static final int TRAN_MODE_INIT_AD		 		=  3;
	public static final int TRAN_MODE_INIT_DEV	 			=  4;	
	public static final int TRAN_MODE_KEY_MUTUAL_BASE		=  5;	
	public static final int TRAN_MODE_KEY_MUTUAL_DEV		=  6;	
	public static final int TRAN_MODE_KEY_DATA				=  7;
	public static final int TRAN_MTK_TYPE					=  10;
	public static final int TRAN_FOCAL_TYPE					=  11;
	public static final int TRAN_INFORMATION_TYPE			=  12;
	public static final int TRAN_TRACE_X_TYPE				=  13;
	public static final int TRAN_TRACE_Y_TYPE				=  14;
	public static final int TRAN_MODE_FPC_OPEN				=  15;
	public static final int TRAN_MODE_FPC_SHORT				=  16;
	public static final int TRAN_MODE_AP_USED_DATA			=  17;
	public static final int TRAN_MODE_MIX_FINGER_AP_USED_DATA	=  18;
	public static final int TRAN_MODE_RESET_COUNT_DOWN		=  19;
	public static final int TRAN_MODE_MUTUAL_AD		 		=  99;  ///< base - ad data

	/// mix the finger ui
	public static final int TRAN_MODE_FINGER_MIX_MUTUAL_DEV	=  100;
	public static final int TRAN_MODE_FINGER_MIX_HOPPING	=  101;
	public static final int TRAN_MODE_NULL					=  255;

	///=========================================================///
	/// C1 Command  
	///=========================================================///
	///< [C1][02][TRAN_TYPE][55][AA][00][00][00][00][00] 
	/// TRAN_TYPE_DYNAMIC					(0x00)
	/// TRAN_TYPE_MUTUAL_SCAN_BASE			(0x01)
	/// TRAN_TYPE_MUTUAL_SCAN_DEV			(0x02)
	/// TRAN_TYPE_INIT_SCAN_BASE			      (0x03)
	/// TRAN_TYPE_INIT_SCAN_DEV 				(0x04)
	/// TRAN_TYPE_KEY_MUTUAL_SCAN_BASE		(0x05)
	/// TRAN_TYPE_KEY_MUTUAL_SCAN_DEV		(0x06)
	/// TRAN_TYPE_KEY_DATA					(0x07)
	/// TRAN_TYPE_MTK_TYPE					(0x0A)
	/// TRAN_TYPE_FOCAL_TYPE					(0x0B)
	/// TRAN_TYPE_MIX_DYNAMIC_MUTUALDEV 	(0x0D)
    // TRAN_TYPE_MIX_DYNAMIC_FRH 			(0x0E)
	public static final int IC_C1_CMD_DYNAMIC				=  0;
	public static final int IC_C1_CMD_MUTUAL_BASE	 		=  1;	
	public static final int IC_C1_CMD_MUTUAL_DEV	 		=  2;
	public static final int IC_C1_CMD_INIT_AD		 		=  3;
	public static final int IC_C1_CMD_INIT_DEV	 			=  4;	
	public static final int IC_C1_CMD_KEY_MUTUAL_BASE		=  5;	
	public static final int IC_C1_CMD_KEY_MUTUAL_DEV		=  6;	
	public static final int IC_C1_CMD_KEY_DATA				=  7;
	public static final int IC_C1_CMD_MIX_DYNAMIC_MUTUALDEV =  13;
	public static final int IC_C1_CMD_MIX_DYNAMIC_FRH       =  14;


	
	///=========================================================///
	/// MY TRAN MODE TYPE 
	///=========================================================///	
	public int tranModeType = TRAN_MODE_NULL;
	
	///=========================================================///
	/// MY resolution & xy display change (rotate) 
	///=========================================================///	
	public int iResolutionX = 800;
	public int iResolutionY = 480;
	public int iRotate = 0;
	
	///=========================================================///
	/// pin assigned
	///=========================================================///	
	public static final int MY9130_PIN_CNT = 57;
	public static final int MY6223_PIN_CNT = 48;
	public static final int MY6231_PIN_CNT = 38;
	public static final int MY6270_PIN_CNT = 52;
	public static final int MY7100_PIN_CNT = 78;
	
	
	///=========================================================///
	/// Hopping Freq. 
	///=========================================================///
	public int FREQ_HOPPING_MAX_CNT	= 16;

	///=========================================================///
	/// Reset count down counter
	///=========================================================///
	public int ResetCountDownCnt	= 0;
	
	///=========================================================///
	/// Ic Information
	///=========================================================///
	public byte bIcModeId;
	public boolean b16BitMutualDev = false;		 ///<  (1<<1L)  ///< 0: 8-Bit Dev, 1: 16 Bit Mutual Dev 
	public boolean b16BitInitDev = false;		 ///<  (1<<2L)  ///< 0: 8-Bit Dev, 1: 16 Bit Intial Dev 
	public boolean bBigEndian = false; 			 ///<  (1<<6L)  ///< 0: Little - Endian , 1: Big ?V Endian
	public int iTPType=0;   ///< 0:1T1R , 1:1T2R
	public String strPcode;
	
	public int iTraceXCnt;
	public int iTraceYCnt;
	public int iKeyEn;
	public int iFingers;
	public int iFingerDataSize;
	public int iResultionX ;
	public int iResultionY ;
	public ArrayList<String> strTraceXNameList = new ArrayList<String>();
	public ArrayList<String> strTraceYNameList = new ArrayList<String>();
	
	///=========================================================///
	/// Timer control
	///=========================================================///	
	public boolean bCloseTimerThread = false;
	public int timercnt = 0;
	public boolean timer1SecFlag = false;
	///=========================================================///
	/// dynamic statis
	///=========================================================///	
	public boolean bChargeStauts = false;
	public boolean bWaterStatus = false;
	public boolean bInitFingerStatus = false;
	public boolean bBaseTrackingPendingStatus = false; 
	public boolean bHoverStatus = false;
	public byte[] pDataBuf= new byte[2048];
	public int iPackSize;

	///=========================================================///
	/// Init control flage
	///=========================================================///
	public boolean bInitAp = false;

	///=========================================================///
	/// Root 
	///=========================================================///
	public boolean bRootChange = false;

	public File LogoutFile ;

	public MyLog TouchEventLog;

	public void GlobalDataSetting()
	{
		bIcModeId = MY6251_INFO;
	}

	public String GetTraceXName(int idx)
	{
		String str;
		if(this.strTraceXNameList.size() > idx)
		{
			str = this.strTraceXNameList.get(idx);
		}
		else
		{
			str = "";
		}
		
		return str;
	}
	
	public String GetTraceYName(int idx)
	{
		String str;
		if(this.strTraceYNameList.size() > idx)
		{
			str = this.strTraceYNameList.get(idx);
		}
		else
		{
			str = "";
		}
		
		return str;
	}
		
    public MyClass get_myClass() {
        return myClass;
    }
    
    public void set_myClass(MyClass _myClass) {
        this.myClass = _myClass;
    }

	public void writeLogFile(File fout, String str, boolean bAppend) 
	{	
		long  length;		
		length = LogoutFile.length();
		if(length > (1024*1024*8))
		{		
			return;
		}	
		FileOutputStream osw = null;

		try {
			Date d = new Date();
			CharSequence s  = DateFormat.format("yyyy-MM-dd hh:mm:ss    ", d.getTime());
			str = s + str;
			osw = new FileOutputStream(fout, bAppend);
			osw.write(str.getBytes());
			osw.flush();
			} catch (Exception e) {
				;
			} finally {
				try {
						osw.close();
					} catch (Exception e) {
						;
					}
			}
	}
	
} 
