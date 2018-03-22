package com.my.gesture;

/**
 * Created by linyu on 2016/6/11.
 */

import android.content.ContextWrapper;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MyLog {

    private String FileDir;
    private String FileName;
    private File LogoutFile ;
    private String LOG_TAG = "[ZET]";

    public MyLog(ContextWrapper context, String log_name, boolean bDeleteOnExit, boolean bStartNew)
    {
        FileDir = context.getFilesDir().toString();
        FileName = log_name;
        LogoutFile = new File(context.getFilesDir(), log_name);
        Log.v(LOG_TAG, "LOG PATH : " + context.getFilesDir() + "/" + log_name);

        if(LogoutFile.exists() == true && bDeleteOnExit == true)
        {
            LogoutFile.deleteOnExit();
        }

        if(bStartNew)
        {
            WriteLog("", false, false);
        }
    }

    public void WriteLog(String str, boolean isAppend, boolean hasHeader)
    {
        FileOutputStream osw = null;
        try {
            if(hasHeader)
            {
                Date d = new Date();
                CharSequence s  = DateFormat.format("yyyy-MM-dd hh:mm:ss    ", d.getTime());
                str = s + str;
            }
            osw = new FileOutputStream(LogoutFile, isAppend);
            osw.write(str.getBytes());
            osw.flush();
        } catch (Exception e) {
            Log.e(LOG_TAG, "write log file failed : " + FileDir + "/" +FileName);
        } finally {
            try {
                osw.close();
            } catch (Exception e) {
                Log.e(LOG_TAG, "close log file failed : " + FileDir + "/" +FileName);
            }
        }
    }

}