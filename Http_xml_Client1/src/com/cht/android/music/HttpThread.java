package com.cht.android.music;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpThread extends Thread {

	private String Url;
	private InputStream fInputStream = null;
	private HttpClient hc;
	private HttpGet get;
	private HttpPost post;
	private Handler uiHandler;
	private String key;
	private int mWhat;

	public HttpThread(String url, Handler handler,int what) {

		this.Url = url;
		uiHandler = handler;
		mWhat=what;

	}

	public HttpThread(String url, Handler handler,int what,String keyword) {

		this.Url = url;
		uiHandler = handler;
		key = keyword;
		mWhat=what;

	}

	public InputStream getFinishedInputStream() {

		return fInputStream;

	}

	public void run() {

		try {
			HttpResponse rp;
			hc = new DefaultHttpClient();

			Log.d("test", "run()-> keyword=> " + key);

			Log.d("test", "run()-> get");
			get = new HttpGet(Url);
			if (key != null) {
				// new URLEncoder();
				get.setHeader("keyword", MsgExpand(key));
			}

			rp = hc.execute(get);

			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				fInputStream = rp.getEntity().getContent();
			}

			// ...完成後發出訊息
			Message m = new Message();
			
			m.what = mWhat;
			uiHandler.sendMessage(m);

		} catch (IOException e) {

		}

	}

	public void Abort() {

		if (key == null)
			get.abort();
		else
			post.abort();

		this.stop();

	}

	private static String MsgExpand(String s) {
		StringBuffer result = new StringBuffer();
		try {
			int i, j;
			for (i = 0; i < s.length(); i++) {
				if (s.charAt(i) > 0x007f) {
					result.append("\\");
					result.append('u');
					String hex = Integer.toHexString(s.charAt(i));
					StringBuffer hex4 = new StringBuffer(hex);
					hex4.reverse();
					int len = 4 - hex4.length();
					for (j = 0; j < len; j++) {
						hex4.append('0');
					}
					for (j = 0; j < 4; j++) {
						result.append(hex4.charAt(3 - j));
					}
				} else {
					result.append(s.charAt(i));
				}
			}
		} catch (Exception e) {
			
		}
		return result.toString();
	}
}
