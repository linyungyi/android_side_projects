/**
 * This software is licensed under the terms of the GNU General Public
 * License version 2, as published by the Free Software Foundation, and
 * may be copied, distributed, and modified under those terms.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * ZEITEC Semiconductor Co., Ltd
  * @author JLJuang <JL.Juang@zeitecsemi.com>
 * @note Copyright (c) 2010, Zeitec Semiconductor Ltd., all rights reserved.
 * @version $Revision: 10 $
 * @note
*/

package com.my.control;

import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
//import android.provider.Settings.Global;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

//import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.Window;
//import android.view.WindowManager;

import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
//import android.widget.Toast;
//import android.widget.AutoCompleteTextView;

import java.io.DataOutputStream;
//import android.view.MenuInflater;
//import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import android.view.MotionEvent;  
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends Activity {
	public static final String LOG_TAG = "[MY]";
	public static final String CONF_NAME = "MyConf.conf";
	public static final int MAX_BUF_LEN 	 		= 2048;
	public static final int MAX_MDEV_BUF_LEN 	= 2048;
	static Activity thisActivity = null;
	
	GlobalData globalData;
	
	MyClass myClass;
	String helloWords;
	byte ioctl_buf[];
	int  mdevBuf[];
	boolean bInitCreate = false;
	boolean bLockReadEvent = false;
	Button btn_connect;
	
	Button btn_flash_write;
	Button btn_flash_read;
	Button btn_flash_path_get;
	Button btn_default_get;	
	Button btn_info_get;
	Button btn_driver_ver_get;	

	Button btn_file_path_get;
	Button btn_path_name_get;	
	
	Button btn_rst;
	Button btn_rst_low;		
	Button btn_rst_high;
	Button btn_rst_again;
	

	Button btn_gpio_high;
	Button btn_gpio_low;
	Button btn_mdev;
	Button btn_execute_run_mode;
	Button btn_coordinate;
	Button btn_mbase;
	Button btn_Dev_Finger;
	
	EditText edit_dev_name;
	EditText edit_file_path;
	EditText edit_flash_path;
	EditText edit_driver_ver;
	EditText edit_col;
	EditText edit_row;
	EditText edit_sen_id;
	EditText edit_fw_ver;

	Spinner select_run_mode;

	EditText edit_main_freq;
	EditText edit_shift_freq;
	
	Button btn_reset_ui;


	int row = 20;
	int col = 20;	

	ContextWrapper context;
	ArrayList<String> strList = new ArrayList<String>();
	List<String> ItemList;
	ArrayAdapter<String> dataAdapter ;
	String str_dev_name="AAA";
	
	//List<File> listFile = new ArrayList<File>();

	protected String bytesToString(byte [] buf, int bufLen)
	{
		String str;
		int i;
		for( i = 0 ; i < bufLen ; i++)
		 {
			 if (ioctl_buf[i] ==0)
			 {
				 break;
			 }
		 }
		 str = new String(ioctl_buf, Charset.forName("UTF-8"));
		 str = str.substring(0, i);
		
		return str;
	}
	
	protected int stringToBytes(String str, byte [] buf)
	{		
		int i;
		int bufLen = str.length();
		byte [] strByte =	str.getBytes();
		for( i = 0 ; i < bufLen ; i++)
		 {
			 ioctl_buf[i] = strByte[i];
			 ioctl_buf[i + 1] = 0;
			 
		 }
		 str = new String(ioctl_buf, Charset.forName("UTF-8"));
		 str = str.substring(0, i);
		
		return bufLen;
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

	private void writeToFile(File fout, String str, boolean bAppend ) 
	{
    	FileOutputStream osw = null;
    	try {
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
	
	private ArrayList<String> ReadFromFile(File fin) 
	{
	    String line;
	    ArrayList<String> stringList = new ArrayList<String>();
	    BufferedReader reader = null;
	    try {
		        reader = new BufferedReader(new InputStreamReader(
		                 new FileInputStream(fin), "utf-8"));
		        while ((line = reader.readLine()) != null)
				{
		            stringList.add(line);
		        }
	   		}
			catch (Exception e) 
	   		{
	        	;
	    	}
			finally 
			{
	        	try {
	            		reader.close();
	        		} catch (Exception e) {
	            		;
	        		}
		    }
	    return stringList;
	}
	
	public void DelaySleepTime(long ms)
	{
		try 
		{
		    Thread.sleep(ms);
		}
		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
	}

	public int GetTraceYName(byte [] pData, int length)
	{
		int i;
		String str;
		//String llog;
		
		globalData.strTraceYNameList.clear();
		for(i = 0 ; i < length; i++)
		{
			str = MyClass.tracename((int)globalData.bIcModeId, (int)pData[i]);
			globalData.strTraceYNameList.add(str);
			//llog=String.format("GetTraceYName ic_model : 0x%02x pData : 0x%02x str : %s",globalData.bIcModeId, pData[i], str);
			//Log.i(LOG_TAG, llog);
		}
		return 1;
	}
	
	public int GetTraceXName(byte [] pData, int length)
	{
		int i;
		String str;
		//String llog;
		
		globalData.strTraceXNameList.clear();
		for(i = 0 ; i < length; i++)
		{
			str = MyClass.tracename((int)globalData.bIcModeId, (int)pData[i]);
			globalData.strTraceXNameList.add(str);
			//llog=String.format("GetTraceXName ic_model : 0x%02x pData : 0x%02x str : %s",globalData.bIcModeId, pData[i], str);
			//Log.i(LOG_TAG, llog);
		}
		return 1;
	}
	
	public int GetIcInformation(byte [] pData, int length)
	{
		int i;
		int cnt = 0;
		String str;
		/// check the data is null
		for(i = 0 ; i < length ; i ++)
		{
			if(pData[i] == 0)
			{
				cnt ++;
			}
		}
		
		if(cnt == length)
		{
			return 0;
		}

		globalData.bIcModeId = pData[0];
		/// get the flag is 16bit setting
		if((pData[3] &0x02) > 0)
		{
			globalData.b16BitMutualDev = true;
		}
		else
		{
			globalData.b16BitMutualDev = false;
		}
		
		if((pData[3] &0x04) > 0)
		{
			globalData.b16BitInitDev = true;
		}
		else
		{
			globalData.b16BitInitDev = false;
		}

		if((pData[3] &0x60) > 0)	
		{
			globalData.bBigEndian = true;
		}
		else
		{
			globalData.bBigEndian = false;
		}
		
		/// pcode
		globalData.strPcode = "";
		for(i = 0 ; i < 8 ; i++)
		{
			str = Integer.toHexString(pData[i] & 0xFF);
			if (str.length() < 2) 
			{
	        	str = "0" + str;
	      	}
			if(i < 7)
			{
				globalData.strPcode = globalData.strPcode + str + "-";
			}
			else
			{
				globalData.strPcode = globalData.strPcode + str ;
			}
		}

		/// check xy resulton
		if((pData[16] & 0x08) == 0x00)
		{
			globalData.iResultionX = pData[8] + pData[9]*256;
			globalData.iResultionY = pData[10] + pData[11]*256;
		}
		else
		{
			globalData.iResultionY = pData[8] + pData[9]*256;
			globalData.iResultionX = pData[10] + pData[11]*256;
		}
		globalData.iTraceXCnt = pData[13];
		globalData.iTraceYCnt = pData[14];
		
		if((pData[15] & 0x80) > 0)
  		{
    		globalData.iKeyEn = 1;
  		}
  		else
  		{
    		globalData.iKeyEn  = 0;
  		}
  		globalData.iFingers =  (pData[15] & 0x7F);
		globalData.iFingerDataSize = 3+ (4*globalData.iFingers) + globalData.iKeyEn ;
		
		globalData.iTPType = 0;
		if((pData[16] & (0x01<<4)) == 0x10)
		{
			globalData.iTPType = 1; ///< 0:1T1R , 1:1T2R
		}
	//	str = String.format("row = %d, col = %d, %dx%d, F:%d", iTraceXCnt, iTraceYCnt,
	//		iResolutionX, iResolutionY, iFingers);
	//	Log.v(LOG_TAG, str);

		return 1;
	}
	
	public void LoadConfFile(String fileName)
	{
	
		String str;
		int idx = 0;
		CharSequence strMsg;
		File dir = context.getFilesDir();
		File inFile = new File(dir, fileName);
		
		if(inFile.exists() == false)
		{
			return;
		}
		
		strList.clear();
		strList.addAll(ReadFromFile(inFile));

		str = strList.get(idx);
		edit_dev_name.setText(str);
		idx++;

		str = strList.get(idx);
		edit_file_path.setText(str);
		idx++;

		str = strList.get(idx);
		edit_flash_path.setText(str);
		idx++;
		
		str = strList.get(idx);
		edit_driver_ver.setText(str);
		idx++;

		str = strList.get(idx);
		edit_fw_ver.setText(str);
		idx++;

		str = strList.get(idx);
		edit_sen_id.setText(str);
		idx++;

		str = strList.get(idx);
		edit_row.setText(str);
		idx++;

		str = strList.get(idx);
		edit_col.setText(str);
		idx++;
		
		str_dev_name = edit_dev_name.getText().toString();
		TextView label_connect = 	(TextView)findViewById(R.id.label_connect);

		 if(MyClass.init(str_dev_name) == 1)
		 {
			 strMsg= getString(R.string.msg_connect_ok);
			 label_connect.setText(strMsg);
			 label_connect.setTextColor(Color.YELLOW);

		 }
		 else
		 {         			 
			 strMsg= getString(R.string.msg_connect_fail);
			 label_connect.setText(strMsg); 
			 label_connect.setTextColor(Color.RED);
		 }	
		
	}

	public void WriteConfFile(String fileName)
	{
		String str;
		File dir = context.getFilesDir();
		File outFile = new File(dir, fileName);

		if(outFile.exists() == true)
		{
			outFile.deleteOnExit();
		}
	
		str = edit_dev_name.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, false);
		str = edit_file_path.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, true);
		str = edit_flash_path.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, true);
		str = edit_driver_ver.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, true);
		str = edit_fw_ver.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, true);
		str = edit_sen_id.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, true);
		str = edit_row.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, true);
		str = edit_col.getText().toString();
		str = str + '\n';
		writeToFile(outFile, str, true);
		
		
	}
    
	public void SettingDefaultEdit()
	{
		String str;

		str = "/dev/my62xx_ts0";
		edit_dev_name.setText(str);
		edit_dev_name.setTextColor(Color.WHITE);

		str = "/vendor/modules/my62xx.bin";
		edit_file_path.setText(str);
		edit_file_path.setTextColor(Color.WHITE);

		str = "0";
		edit_driver_ver.setText(str);
		edit_driver_ver.setTextColor(Color.WHITE);

		str = "/mnt/sdcard/";
		edit_flash_path.setText(str);
		edit_flash_path.setTextColor(Color.WHITE);

		str = "5/0/0";
		edit_sen_id.setText(str);
		edit_sen_id.setTextColor(Color.WHITE);

		str = "10";
		edit_col.setText(str);
		edit_col.setTextColor(Color.WHITE);

		str = "15";
		edit_row.setText(str);
		edit_row.setTextColor(Color.WHITE);

		str = "0-0-0-0-0-0-0-0";
		edit_fw_ver.setText(str);
		edit_fw_ver.setTextColor(Color.WHITE);
	
	}
	
	Thread ReadIcInfo = new Thread(new Runnable() {
		 @Override
		 public void run() {		
		}
	});

	public void ReadInformation()
	{
		int ret = 0;
		int senid;
		int senid_status;
		int idata;
		String str;
		String strcat;
		String strflashpath;
		String str_tem;
		File root ;//= new File("/vendor/modules");
		//String dri_ver="$Revision: 28 $";
		//String fw_path="/vendor/my62xx.bin";
		//String tr_path="/mnt/sdcard";
		
		///Driver Ver. get
		ret = MyClass.ioctl(GlobalData.MY_IOCTL_CMD_DRIVER_VER_GET, ioctl_buf);
		str = Integer.toString(ret);
		Log.v(LOG_TAG,"ret="+str);
		//ioctl_buf = dri_ver.getBytes();
		
		
		
		str = bytesToString(ioctl_buf, MAX_BUF_LEN);
		Log.v(LOG_TAG, str);		
		if(str.length() >= 14)
		{
			strcat = str.substring(10,14);
			edit_driver_ver.setText(strcat);
			edit_driver_ver.setTextColor(Color.WHITE);
		}
		/// file path get
		ret = MyClass.ioctl(GlobalData.MY_CMD_TRAN_TYPE_PATH_GET, ioctl_buf);
		str = Integer.toString(ret);
		Log.v(LOG_TAG,"ret="+str);
		//ioctl_buf = tr_path.getBytes();
		str = bytesToString(ioctl_buf, MAX_BUF_LEN);
		edit_file_path.setText(str);
		edit_file_path.setTextColor(Color.WHITE);
		btn_path_name_get.setEnabled(true);
		Log.v(LOG_TAG, str);
		
		/// flash path get
		ret = MyClass.ioctl(GlobalData.MY_CMD_FLASH_PATH_GET, ioctl_buf);
		str = Integer.toString(ret);
		Log.v(LOG_TAG,"ret="+str);
		//ioctl_buf = fw_path.getBytes();
		
		str_tem = new String(ioctl_buf);
		Log.v(LOG_TAG,str_tem);
		
		str = bytesToString(ioctl_buf, MAX_BUF_LEN);
		
		strflashpath = str.substring(0,str.indexOf("my62xx.bin"));
		root = new File(strflashpath);
		
		//if(findFile(root, "my62xx.bin")){
		//	edit_flash_path.setText(str+"  YES");
			//edit_flash_path.setText(strflashpath);
		//}else{
		//	edit_flash_path.setText(str+" NO");
		//	//edit_flash_path.setText(strflashpath);
		//}
		
		
		//edit_flash_path.setText(str);
		edit_flash_path.setTextColor(Color.WHITE);
		Log.v(LOG_TAG, str);
		
				/// senid get
				ret = MyClass.ioctl(GlobalData.MY_CMD_SENID_GET, ioctl_buf);
				senid_status = (int)(ioctl_buf[0] & 0xFF);
				senid = ioctl_buf[1];
		
				if((senid_status & 0xF0) > 0)
				{
							senid_status = (int) (0x0F & senid_status);
							idata = senid_status;
							if(idata == 0x0F)  ///< because the default senid is 0~3 
							{
								str = "Driver not support";
							}
							else
							{
								str = Integer.toString(idata)+ " / use FW"; 
							}
						}
						else
						{
							str =  Integer.toString(senid_status)+" / " +Integer.toString(senid);
						}
						
				
				edit_sen_id.setText(str);
				edit_sen_id.setTextColor(Color.WHITE);
				Log.v(LOG_TAG, str);
		
		//		//读�?驱动中�??��??��??�辨??
		//		ret = MyClass.ioctl(GlobalData.MY_CMD_FINGER_XMAX_YMAX_GET, ioctl_buf);
		//		finger_number = ioctl_buf[0]&0xff;
		//		resolution_x = (ioctl_buf[2]&0xff)*256+(ioctl_buf[1]&0xff);
		//		resolution_y = (ioctl_buf[4]&0xff)*256+(ioctl_buf[3]&0xff);
		//		if(finger_number > 0){
		//			str = Integer.toString(finger_number)+"/"+Integer.toString(resolution_x)+
		//					"/"+Integer.toString(resolution_y)	;
		//		
		//		}
		//		else{
		//			str = "0/0/0";
		//		}
		//		edit_sen_id.setText(str);
		//		edit_sen_id.setTextColor(Color.GREEN);


		/// pcode get
		//ret = myClass.ioctl(globalData.MY_CMD_PCODE_GET, ioctl_buf);
		//str = bytesToString(ioctl_buf, MAX_BUF_LEN);
		
		//Log.v(LOG_TAG, str);
		if (globalData.bInitAp == false)
		{	 	
			/// B2
			ret = MyClass.ioctl(GlobalData.MY_CMD_INFO, ioctl_buf);
			//DelaySleepTime(5);
			ret = MyClass.ioctl(GlobalData.MY_CMD_INFO_GET, ioctl_buf);
			if(GetIcInformation(ioctl_buf, 17) == 0)
			{
			  MyClass.ioctl(GlobalData.MY_CMD_PCODE_GET, ioctl_buf);
			  str = bytesToString(ioctl_buf, MAX_BUF_LEN);//MAX_BUF_LEN
			  edit_fw_ver.setText(str);
		   	  edit_fw_ver.setTextColor(Color.WHITE);
			  return;
			}

			idata = ioctl_buf[13];
			str = Integer.toString(idata);
			edit_col.setText(str);
			edit_col.setTextColor(Color.WHITE);
			Log.v(LOG_TAG, str);
			col = idata;

			idata = ioctl_buf[14];
			str = Integer.toString(idata);
			edit_row.setText(str);
			edit_row.setTextColor(Color.WHITE);
			Log.v(LOG_TAG, str);
			row = idata;
			
			edit_fw_ver.setText(globalData.strPcode);
			edit_fw_ver.setTextColor(Color.WHITE);

			/// Trace X
			ret = MyClass.ioctl(GlobalData.MY_CMD_TRACE_X_NAME_SET, ioctl_buf);
			DelaySleepTime(5);
			ret = MyClass.ioctl(GlobalData.MY_CMD_TRACE_X_NAME_GET, ioctl_buf);
			GetTraceXName(ioctl_buf, globalData.iTraceXCnt);
			
			/// Trace Y
			ret = MyClass.ioctl(GlobalData.MY_CMD_TRACE_Y_NAME_SET, ioctl_buf);
			DelaySleepTime(5);
			ret = MyClass.ioctl(GlobalData.MY_CMD_TRACE_Y_NAME_GET, ioctl_buf);
			GetTraceYName(ioctl_buf, globalData.iTraceYCnt);

			globalData.bInitAp = true;
		}
		
	}
	//?�找?�件
	public static boolean findFile(File root,String name){
		if (root.exists() && root.isDirectory()) {
			for (File file : root.listFiles()) {
				if (file.isFile() && file.getName().equals(name)) {
					//System.out.println(file.getName());//这�?输出?�件?��?
					//Log.v(LOG_TAG, "LLLLLmy.bin");
					return true;
				} else if (file.isDirectory()) {
					findFile(file, name);
				}
			}
			return false;
		}
		return false;
	}
	class Task implements Runnable 
	{
	    @Override
	    public void run() 
	    {
		
	    }

	}
	
	public int changeRoot(String str)
	{  

//		int i;
//		String strDev;
		int errNo = 0xFF;
   	 	Process process = null;
   	 	DataOutputStream os = null;
		
   	 	try {
			process = Runtime.getRuntime().exec("su");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
   	 	if(process != null)
		{
			os = new DataOutputStream(process.getOutputStream());
			try {
				//Toast.makeText(getApplicationContext(), "chmod 777 my62xx_ts0....", Toast.LENGTH_SHORT).show();
				os.writeBytes("chmod 777 /dev/my62xx_ts0\n");
				os.writeBytes("chmod 777 /dev/my62xx_ts1\n");
				os.writeBytes("chmod 777 /dev/my62xx_ts2\n");
				//Toast.makeText(getApplicationContext(), "chmod 777 my62xx_ts2....", Toast.LENGTH_SHORT).show();
				os.writeBytes("chmod 777 /dev/my62xx_ts3\n");
				os.writeBytes("exit\n");
				os.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return errNo;
		
	}
	
	public void Finalize() throws Throwable{
			Log.v(LOG_TAG, "Finalize");

	  }
	//实时调�?信息?��?以�?展示
	public void ExecuteRunningMode()
	{
		str_dev_name = select_run_mode.getSelectedItem().toString();//edit_dev_name.getText().toString();

		if(str_dev_name.equals("Coordinate") == true)
		{		 				
			globalData.tranModeType = GlobalData.TRAN_MODE_DYNAMIC;
		 	MyClass.ioctl(GlobalData.MY_CMD_DYNAMIC, ioctl_buf);
			showTransferTestPage();
		}
		else if(str_dev_name.equals("MutualAD") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_MUTUAL_AD;
			showTransferTestPage();
			Log.v(LOG_TAG, "btn_mad CLICKED!!");
		}
		else if(str_dev_name.equals("MutualADDelta") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_MUTUAL_BASE;
			showTransferTestPage();
			Log.v(LOG_TAG, "btn_mbase CLICKED!!");
		}
		else if(str_dev_name.equals("MutualDev") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_MUTUAL_DEV;
			showTransferTestPage();
			Log.v(LOG_TAG, "onClick BTN_MDEV CLICKED!!");
		}
		else if(str_dev_name.equals("InitialDev") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_INIT_DEV;
			showTransferTestPage();
			Log.v(LOG_TAG, "btn_idev CLICKED!!");
		}
		else if(str_dev_name.equals("InitialBase") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_INIT_AD;
			showTransferTestPage();
			Log.v(LOG_TAG, "btn_iad CLICKED!!");
		}
		else if(str_dev_name.equals("FPCOpen") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_FPC_OPEN;
			showTransferTestPage();
			Log.v(LOG_TAG, "btn_iad CLICKED!!");
		}
		else if(str_dev_name.equals("FPCShort") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_FPC_SHORT;
			showTransferTestPage();
		}
		else if(str_dev_name.equals("Finger+Dev") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_FINGER_MIX_MUTUAL_DEV;
			showTransferTestPage();
		}
		else if(str_dev_name.equals("Finger+Hop") == true)
		{
			globalData.tranModeType = GlobalData.TRAN_MODE_FINGER_MIX_HOPPING;
			showTransferTestPage();
		}
      		 
	}
	
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		String str;
		super.onSaveInstanceState(outState);
   	    Log.v(LOG_TAG, "onSaveInstanceState !!");
      
		str = edit_dev_name.getText().toString();
		outState.putString("edit_dev_name", str);
		str = edit_file_path.getText().toString();
		outState.putString("edit_file_path", str);
		str = edit_flash_path.getText().toString();
		outState.putString("edit_flash_path", str);
		str = edit_driver_ver.getText().toString();
		outState.putString("edit_driver_ver", str);
		str = edit_fw_ver.getText().toString();
		outState.putString("edit_fw_ver", str);
		str = edit_sen_id.getText().toString();
		outState.putString("edit_sen_id", str);
		str = edit_row.getText().toString();
		outState.putString("edit_row", str);
		str = edit_col.getText().toString();
		outState.putString("edit_col", str);
		super.onSaveInstanceState(outState);  
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {  

		super.onRestoreInstanceState(savedInstanceState);  
	}  
	
	@SuppressWarnings("static-access")
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		thisActivity = this;
		///--------------------------------------------------------///
		/// MyClass initialization
		///--------------------------------------------------------///
		myClass = new MyClass();
		context = new ContextWrapper(getApplicationContext());
		///--------------------------------------------------------///
		/// Global Data
		///--------------------------------------------------------///
		globalData =((GlobalData)getApplicationContext());							
		globalData.set_myClass(myClass);
		globalData.GlobalDataSetting();				  
		
		ioctl_buf = new byte[MAX_BUF_LEN];		
		
		helloWords = myClass.hello();
		Log.v(LOG_TAG, helloWords);
		/// landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		globalData.TouchEventLog = new MyLog(context,"TouchEvent.log");
					
		///--------------------------------------------------------///
		/// Device Name Path
		///--------------------------------------------------------///	
		edit_dev_name = (EditText) findViewById(R.id.edit_dev_name);
		edit_file_path = (EditText)findViewById(R.id.edit_file_path);		
		edit_flash_path = (EditText)findViewById(R.id.edit_flash_path);
		edit_driver_ver = (EditText)findViewById(R.id.edit_driver_ver);
		edit_row = (EditText)findViewById(R.id.edit_row);
		edit_col = (EditText)findViewById(R.id.edit_col);
		edit_sen_id = (EditText)findViewById(R.id.edit_senid_index);
		edit_fw_ver = (EditText)findViewById(R.id.edit_fw_ver);
		 	

		ItemList = new ArrayList<String>();
		ItemList.add("Please_Select_Mode");
		ItemList.add("Coordinate");
		ItemList.add("MutualAD");
		ItemList.add("MutualADDelta");
		ItemList.add("MutualDev");
		ItemList.add("InitialDev");
		ItemList.add("InitialBase");
		ItemList.add("FPCOpen");
		ItemList.add("FPCShort");
		ItemList.add("Finger+Dev");
		ItemList.add("Finger+Hop");

   	 	///--------------------------------------///
		/// chmod
		///--------------------------------------///            	 
		if(globalData.bRootChange == true)
		{
			changeRoot("su");
		}
		
		///load the default setting data
		LoadConfFile(CONF_NAME);

		///=============================================================================///
		/// I. Connect Function
		///=============================================================================///
		///--------------------------------------------------------///		
		/// 1. Connect Button
		///--------------------------------------------------------///		
		btn_connect = (Button)findViewById(R.id.btn_connect);
		btn_connect.setOnClickListener(new View.OnClickListener() 
         {
             public void onClick(View v) 
             {   


            	 CharSequence strMsg;
            	 str_dev_name = edit_dev_name.getText().toString();
            	 TextView label_connect = 	(TextView)findViewById(R.id.label_connect);
            	 
            	 //ioctl_buf = str_dev_name.getBytes();
            	 Log.v(LOG_TAG, "BTN_CONNECT CLICKED!!");
            	 Log.v(LOG_TAG, str_dev_name);
         		 if(myClass.init(str_dev_name) == 1)
         		 {
         			 strMsg= getString(R.string.msg_connect_ok);
         			 label_connect.setText(strMsg);
         			 label_connect.setTextColor(Color.YELLOW);
         		 }
         		 else
         		 {         			 
         			 strMsg= getString(R.string.msg_connect_fail);
         			 label_connect.setText(strMsg); 
         			 label_connect.setTextColor(Color.RED);
         		 }

             }
         });
		
		///=============================================================================///		
		/// II. Flash Function
		///=============================================================================///		
		
		///--------------------------------------------------------///		
		/// 1. Flash Write Button
		///--------------------------------------------------------///		
		btn_flash_write = (Button)findViewById(R.id.btn_flash_write);
		btn_flash_write.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   				
            	// edit_flash_name.setText(Environment.getExternalStorageDirectory().toString());
				 myClass.ioctl(globalData.MY_CMD_FLASH_WRITE, ioctl_buf);
            	 Log.v(LOG_TAG, "BTN_FLASH_WRITE CLICKED!!");
             }
         });
		
		///--------------------------------------------------------///		
		/// 2. Flash Read Button
		///--------------------------------------------------------///		
		btn_flash_read = (Button)findViewById(R.id.btn_flash_read);
		btn_flash_read.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   				
				String str = edit_flash_path.getText().toString();
				stringToBytes(str, ioctl_buf);	
				myClass.ioctl(globalData.MY_CMD_FLASH_READ, ioctl_buf);
				Log.v(LOG_TAG, "BTN_FLASH_READ CLICKED!!");
             }
         });

		///--------------------------------------------------------///		
		/// 3. information get
		///--------------------------------------------------------///	
		btn_info_get = (Button)findViewById(R.id.btn_info_get);
		btn_info_get.setOnClickListener(new View.OnClickListener() 
		{             			
			public void onClick(View v) 
            {   	
				/// read the ic information
				if(bLockReadEvent == false)
				{
					bLockReadEvent = true;
					globalData.bInitAp = false;
					ReadInformation();
					bLockReadEvent = false;
				}
            }
        });	

		///--------------------------------------------------------///		
		/// 4. Flash Path Set Button
		///--------------------------------------------------------///		
		btn_default_get = (Button)findViewById(R.id.btn_default_get);
		btn_default_get.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   	
				/// default edit setting
				SettingDefaultEdit();
             }
         });	


		///--------------------------------------------------------///		
		/// 6. File Path Set Button
		///--------------------------------------------------------///		
		btn_path_name_get = (Button)findViewById(R.id.btn_path_name_get);
		btn_path_name_get.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   			
				String str = edit_file_path.getText().toString();
				stringToBytes(str, ioctl_buf);				
				myClass.ioctl(globalData.MY_CMD_TRAN_TYPE_PATH_SET, ioctl_buf);				
				Log.v(LOG_TAG, "btn_path_name_get CLICKED!!");

				str = edit_flash_path.getText().toString();
				stringToBytes(str, ioctl_buf);				
				myClass.ioctl(globalData.MY_CMD_FLASH_PATH_SET, ioctl_buf);				
				Log.v(LOG_TAG, "btn_default_get CLICKED!!");
             }
         });		
			
		
			
		///=============================================================================///
		/// II. Reset PIN Function
		///=============================================================================///
					
		///--------------------------------------------------------///		
		/// 1. Reset Button
		///--------------------------------------------------------///		
		btn_rst = (Button)findViewById(R.id.btn_rst);
		btn_rst.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   				
				 globalData.ResetCountDownCnt = 64;
				 //myClass.ioctl(globalData.MY_CMD_RST, ioctl_buf);		
            	 Log.v(LOG_TAG, "BTN_RST CLICKED!!");            	 
            	 
            	 //ReadInformation();
     			 globalData.tranModeType = GlobalData.TRAN_MODE_RESET_COUNT_DOWN;    		 	 
    			 showTransferTestPage();
             }
         });	
		///--------------------------------------------------------///		
		/// 2. Reset Low Button
		///--------------------------------------------------------///		
		btn_rst_low = (Button)findViewById(R.id.btn_rst_low);
		btn_rst_low.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   				
				Log.v(LOG_TAG, "btn_rst_low!!");
		       	 
				myClass.ioctl(globalData.MY_CMD_RST_LOW, ioctl_buf);				 
             }
         });	
		///--------------------------------------------------------///		
		/// 3. Reset high Button
		///--------------------------------------------------------///		
		btn_rst_high = (Button)findViewById(R.id.btn_rst_high);
		btn_rst_high.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   				
				 myClass.ioctl(globalData.MY_CMD_RST_HIGH, ioctl_buf);				 
            	 Log.v(LOG_TAG, "BTN_RST CLICKED!!");
             }
         });	
		
		///--------------------------------------------------------///		
		/// 4. GPIO Low Button
		///--------------------------------------------------------///		
		btn_gpio_low = (Button)findViewById(R.id.btn_gpio_low);
		btn_gpio_low.setOnClickListener(new View.OnClickListener() 
         {             			
			 public void onClick(View v) 
             {   				
				Log.v(LOG_TAG, "MY_CMD_GPIO_LOW!!");
		       	ioctl_buf[0] = 0x01; 
				myClass.ioctl(globalData.MY_CMD_GPIO_LOW, ioctl_buf);				 
             }
         });	
		
		///--------------------------------------------------------///		
		/// 5. GPIO high Button
		///--------------------------------------------------------///		
		btn_gpio_high = (Button)findViewById(R.id.btn_gpio_high);
		btn_gpio_high.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   						 
            	 Log.v(LOG_TAG, "MY_CMD_GPIO_HIGH CLICKED!!");
		       	 ioctl_buf[0] = 0x01; 
				 myClass.ioctl(globalData.MY_CMD_GPIO_HIGH, ioctl_buf);		
             }
         });

		///--------------------------------------------------------///		
		/// 6. Coordinate Button
		///--------------------------------------------------------///		
		btn_coordinate = (Button)findViewById(R.id.btn_coordinate);
		btn_coordinate.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   						 
            	 Log.v(LOG_TAG, "Coordinate BNT !!");
            	 ReadInformation();
     			 globalData.tranModeType = GlobalData.TRAN_MODE_DYNAMIC;
    		 	 MyClass.ioctl(GlobalData.MY_CMD_DYNAMIC, ioctl_buf);
    			 showTransferTestPage();
             }
         });

		///--------------------------------------------------------///		
		/// 7. Mutual Dev Button
		///--------------------------------------------------------///		
		btn_mdev = (Button)findViewById(R.id.btn_mdev);
		btn_mdev.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   						 
				Log.v(LOG_TAG, "onClick BTN_MDEV CLICKED!!");
            	 ReadInformation();
     			globalData.tranModeType = GlobalData.TRAN_MODE_MUTUAL_DEV;
    			showTransferTestPage();
             }
         });		
		///--------------------------------------------------------///		
		/// 8. AD Delta Button
		///--------------------------------------------------------///		
		btn_mbase = (Button)findViewById(R.id.btn_mbase);
		btn_mbase.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   						 
				 Log.v(LOG_TAG, "btn_mbase CLICKED!!");
            	 ReadInformation();
     			globalData.tranModeType = GlobalData.TRAN_MODE_MUTUAL_BASE;
    			showTransferTestPage();;
             }
         });		
		///--------------------------------------------------------///		
		/// 9. Coordinate + Dev Button
		///--------------------------------------------------------///		
		btn_Dev_Finger = (Button)findViewById(R.id.btn_Dev_Finger);
		btn_Dev_Finger.setOnClickListener(new View.OnClickListener() 
         {             			
			public void onClick(View v) 
             {   						 
				 Log.v(LOG_TAG, "btn_mbase CLICKED!!");
            	 ReadInformation();
     			globalData.tranModeType = GlobalData.TRAN_MODE_FINGER_MIX_MUTUAL_DEV;
    			showTransferTestPage();
             }
         });	

		///=============================================================================///
		/// III. Transfer Function
		///=============================================================================///

		///--------------------------------------------------------///		
		/// 1. Select the item to running transfer mode
		///--------------------------------------------------------///
		select_run_mode = (Spinner) findViewById(R.id.spinner_select_item);
		dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, ItemList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		select_run_mode.setAdapter(dataAdapter);
		select_run_mode.setPrompt("test");
		
        select_run_mode.setOnTouchListener(new Spinner.OnTouchListener() {  
  
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Log.d("mark", "onTouch() is invoked!");  
			 	if(globalData.bInitAp == false)
				{
				 	/// read information
					//Toast.makeText(getApplicationContext(), "?�嬬?�寰?�湪?��彇IC?��?棞璩?��?涓�....", Toast.LENGTH_LONG).show();	  
									
				//	ReadInformation();
				}
	           // ExecuteRunningMode();
				return false;
			}  
        });  
		select_run_mode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			
		    public void onItemSelected(AdapterView<?> parent, View view, 
		            int pos, long id) {
		    	if(bInitCreate == true)
		    	{
					    	
				 	if(globalData.bInitAp == false)
					{
					 	/// read information
						//Toast.makeText(getApplicationContext(), "?�嬬?�寰?�湪?��彇IC?��?棞璩?��?涓�....", Toast.LENGTH_LONG).show();	  
										
						ReadInformation();
					}
		            ExecuteRunningMode();

    				Spinner spinner = (Spinner)view.getParent();
    				spinner.setSelection(0); 
		    	}
				else
				{
					//select_run_mode.setSelection(0, false);
				}
		    	bInitCreate = true;
		    }

		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void ChangeToDynamicMode()
	{
		int pack_size;
		int iDriverMode;
		byte [] buf;	
		
		
		/// setting the size
		pack_size = globalData.iFingerDataSize;  
		buf = intToByteArray(pack_size);
		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_SIZE_SET, buf);
		/// setting the type
		iDriverMode = GlobalData.TRAN_MODE_DYNAMIC;
		buf = intToByteArray(iDriverMode);
		MyClass.ioctl(GlobalData.MY_CMD_TRAN_TYPE_SET, buf);
		/// send free back mode
		myClass.ioctl(globalData.MY_CMD_RST, ioctl_buf);	
	}
		
	@Override 
	protected void onActivityResult(int requestCode , int resultCode ,	Intent data)
	{
		Log.v(LOG_TAG, "close the sub activity CLICKED!!");
		globalData.bCloseTimerThread = true;
		ChangeToDynamicMode();
            	//myClass.ioctl(globalData.MY_CMD_DYNAMIC, ioctl_buf);
	}
	
    @Override
    protected void onDestroy() {
		WriteConfFile(CONF_NAME);
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy()");
    }
	
	@Override
    protected void onResume() {
        super.onResume();
	    Log.v(LOG_TAG, "onResume()");
    }

	public void ApSettingMode(boolean bMixFingerMode, int size, int iC1Cmd)
	{
		int pack_size;
		int iDriverMode;
		byte [] buf;	

		if(bMixFingerMode == true)
		{
			iDriverMode = GlobalData.TRAN_MODE_MIX_FINGER_AP_USED_DATA;
		}
		else
		{
			iDriverMode = GlobalData.TRAN_MODE_AP_USED_DATA;
		}
		/// setting the type
		buf = intToByteArray(iDriverMode);
		MyClass.ioctl(GlobalData.MY_CMD_TRAN_TYPE_SET, buf);

		/// setting the size
		pack_size = size;  
		buf = intToByteArray(pack_size);
		MyClass.ioctl(GlobalData.MY_CMD_REPORT_DATA_SIZE_SET, buf);

		ioctl_buf[0]  = 10; ///< size
		ioctl_buf[1]  = (byte)193;  ///< 0xC1
		ioctl_buf[2]  = (byte)0x02;
		ioctl_buf[3]  = (byte)iC1Cmd;
		ioctl_buf[4]  = (byte)85; ///<0x55;
		ioctl_buf[5]  = (byte)170; ///<0xAA;
		ioctl_buf[6]  = (byte)0x00;
		ioctl_buf[7]  = (byte)0x00;
		ioctl_buf[8]  = (byte)0x00;
		ioctl_buf[9]  = (byte)0x00;
		ioctl_buf[10] = (byte)0x00;
		MyClass.ioctl(GlobalData.MY_CMD_WRITE_CMD, ioctl_buf);
			
	}

	public void FPCOpenSetting()
	{	
		int pack_size;
		
   		if(globalData.b16BitInitDev == true)
   		{
			pack_size = (globalData.iTraceXCnt +globalData.iTraceYCnt)*2;  
			ApSettingMode(false, pack_size, GlobalData.TRAN_MODE_INIT_DEV); ///< GlobalData.TRAN_MODE_INIT_DEV
		}
		else
		{
   			MyClass.ioctl(GlobalData.MY_CMD_FPC_OPEN_SET, ioctl_buf);
		}
	}
	
	public void InitialDevSetting()
	{	
		int pack_size;
		
   		if(globalData.b16BitInitDev == true)
   		{
			pack_size = (globalData.iTraceXCnt +globalData.iTraceYCnt)*2;  
			ApSettingMode(false, pack_size, GlobalData.TRAN_MODE_INIT_DEV); ///< GlobalData.TRAN_MODE_INIT_DEV
		}
		else
		{
   			MyClass.ioctl(GlobalData.MY_CMD_IDEV, ioctl_buf);
		}
	}
	
	public void MutualDevSetting()
	{	
		int pack_size;
		String msg;

   		if(globalData.b16BitMutualDev == true)
   		{
			pack_size = ((globalData.iTraceXCnt+2) * (globalData.iTraceYCnt+2))*2;
			msg = String.format("[MY] MutualDevSetting 16bits pack_size = %d", pack_size);
			ApSettingMode(false, pack_size, GlobalData.TRAN_MODE_MUTUAL_DEV); ///< GlobalData.TRAN_MODE_MUTUAL_DEV
		}
		else
		{
			msg = String.format("[MY] MutualDevSetting 8bits pack_size = %d", (globalData.iTraceXCnt+2) * (globalData.iTraceYCnt+2));
			Log.v(LOG_TAG, msg);
   			MyClass.ioctl(GlobalData.MY_CMD_MDEV, ioctl_buf);
		}
	}

	public void FingerMixMDevSetting()
	{	
		int pack_size;
		/// setting the size
		pack_size = globalData.iFingerDataSize;

		if(globalData.b16BitMutualDev == true)
   		{
			pack_size = pack_size + 2*((globalData.iTraceXCnt+2)*(globalData.iTraceYCnt+2));
		}
		else
		{
			pack_size = pack_size + ((globalData.iTraceXCnt+2)*(globalData.iTraceYCnt+2));  
		}

		ApSettingMode(true, pack_size, GlobalData.IC_C1_CMD_MIX_DYNAMIC_MUTUALDEV);

	}
	public void FingerMixFreqSetting()
	{
		int pack_size;
		/// setting the size
		pack_size = 128;  ///< freq setting fix size
	
		ApSettingMode(true, pack_size, GlobalData.IC_C1_CMD_MIX_DYNAMIC_FRH);
	}
	
	public void showTransferTestPage() {		
		Intent intent =	new Intent();
		Bundle bundle = new Bundle();
//		int tmpData;
//		int tmp;
		
		intent.setClass(MainActivity.this , TransferTestActivity.class);
			
		bundle.putString("dev_name" , str_dev_name);		

		bundle.putInt("row", row);
		bundle.putInt("col", col);
		///---------------------------------------///
	   	/// BYTE#0 : SEN_TRACE_NUM
	   	/// BYTE#1 : DRI_TRACE_NUM            	      
		///---------------------------------------///

		String msg = String.format("[MY] showTransferTestPage tranMode = %d", globalData.tranModeType);
		Log.v(LOG_TAG, msg);
		
		GenerateLogFile();
		
	   	if(globalData.tranModeType == GlobalData.TRAN_MODE_MUTUAL_DEV)
	   	{
			MutualDevSetting();
	   	}	 
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_MUTUAL_BASE)
	  	{
			MyClass.ioctl(GlobalData.MY_CMD_MBASE, ioctl_buf);	
	  	}
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_MUTUAL_AD)
	  	{
	   		MyClass.ioctl(GlobalData.MY_CMD_MBASE, ioctl_buf);	
	  	}
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_INIT_DEV)
	   	{
	   		InitialDevSetting();
	   	}
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_INIT_AD)
	   	{
	   		MyClass.ioctl(GlobalData.MY_CMD_IBASE, ioctl_buf);
	   	}
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_DYNAMIC)
	   	{
		 	MyClass.ioctl(GlobalData.MY_CMD_DYNAMIC, ioctl_buf);
			globalData.TouchEventLog.WriteLog("", false, false); //albert++ 20160414 init TouchEvent.log
	   	}
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_FPC_OPEN)
	   	{
			FPCOpenSetting();
	   	}
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_FPC_SHORT)
	   	{
		 	MyClass.ioctl(GlobalData.MY_CMD_FPC_SHORT_SET, ioctl_buf);
	   	}
	   	else if(globalData.tranModeType == GlobalData.TRAN_MODE_FINGER_MIX_MUTUAL_DEV)
	   	{
	   		FingerMixMDevSetting();
		}
		else if(globalData.tranModeType == GlobalData.TRAN_MODE_FINGER_MIX_HOPPING)
	   	{
	   		FingerMixFreqSetting();
		}
		else if(globalData.tranModeType == GlobalData.TRAN_MODE_RESET_COUNT_DOWN)
	   	{
	   		
		}
		
		intent.putExtras(bundle);		

		startActivityForResult(intent , 0) ;
	}
	
	public void showSettingPage() {		
		Intent intent =	new Intent();
		Bundle bundle = new Bundle();
		
		intent.setClass(MainActivity.this, SettingView.class);
			
		bundle.putString("dev_name", str_dev_name);
		///---------------------------------------///
	   	/// Information
	   	///---------------------------------------///  
	   	MyClass.ioctl(GlobalData.MY_CMD_INFO, ioctl_buf);
	   	MyClass.ioctl(GlobalData.MY_CMD_INFO_GET, ioctl_buf);
	   	
	   	if((ioctl_buf[13] > 0 && ioctl_buf[13] < 64 )&& 
	   	    (ioctl_buf[14] > 0 && ioctl_buf[14] < 64))
	   	{
	   		col = ioctl_buf[13];
	   		row = ioctl_buf[14];
	   	}
		bundle.putInt("row", globalData.iTraceYCnt);
		bundle.putInt("col", globalData.iTraceXCnt);
		
		intent.putExtras(bundle);		
		startActivityForResult(intent , 0) ;
	}
	
	public void ResetDelayIC() {		
		Intent intent =	new Intent();
		Bundle bundle = new Bundle();
		
		intent.setClass(MainActivity.this, ResetDelayIC.class);
		
		intent.putExtras(bundle);		
		startActivityForResult(intent , 0) ;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_about:
			Log.v(LOG_TAG, "action_about");
			break;
		case R.id.action_exit:
			Log.v(LOG_TAG, "action_exit");
			finish();
			break;
		case R.id.action_settings:
			Log.v(LOG_TAG, "action_settings");
		//	showSettingPage();
			//Intent intent = new Intent(this, SettingView.class);
            //startActivity(intent);
			break;
		default:
			return super.onOptionsItemSelected(item);
		
		}

		return true;
	}

	public void GenerateLogFile() 
	{		
		String LOGFILE_NAME = "MyLog.log";
		globalData.LogoutFile = new File(context.getFilesDir(), LOGFILE_NAME);
		Log.v(LOG_TAG, "LOG PATH : " + context.getFilesDir() + "/" + LOGFILE_NAME);
		if(globalData.LogoutFile.exists() == true)
		{
			globalData.LogoutFile.deleteOnExit();
		}
		writeToFile(globalData.LogoutFile, "", false);	
	}

	
}
