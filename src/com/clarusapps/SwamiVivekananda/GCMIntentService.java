package com.clarusapps.SwamiVivekananda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	
	 private NotificationManager notiMgr = null;

	@Override
	public void onRegistered(Context context, String regId){
		final String regiId=regId;
		
		Log.e("From GCMIntentService", "Registration id:"+regId);
		new Thread(){
			public void run(){
				HttpClient client = new DefaultHttpClient();
		        HttpPost post = new HttpPost("http://demo.lauruss.com/PushApp/datastore.php");
		        try {
		            List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		            // Get the deviceID
		            nameValuePairs.add(new BasicNameValuePair("device", "android"));
		            nameValuePairs.add(new BasicNameValuePair("appName", "Thoughts Of Swami Vivekananda"));
		            nameValuePairs.add(new BasicNameValuePair("regId", regiId));
		            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            HttpResponse response = client.execute(post);
		            BufferedReader rd = new BufferedReader(new InputStreamReader( response.getEntity().getContent()));

		            String line = "";
		            while ((line = rd.readLine()) != null) {
		                Log.e("HttpResponse", line);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		}.start();
		
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.e("From GCMIntentService", "Error is:"+arg1);
		if(arg1.equalsIgnoreCase("AUTHENTICATION_FAILED")){
			Toast.makeText(arg0, "First configure your gmail account", Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		notiMgr=(NotificationManager)arg0.getSystemService(NOTIFICATION_SERVICE);
		
		//Notification notify=new Notification();
		MyTabActivity.showNotification(arg0, arg1.getExtras());
		
		Log.e(" From GCMIntentService", "Message received");
		Log.e(" From GCMIntentService", "Message received: "+arg1.getStringExtra("message"));
		Log.e(" From GCMIntentService", "Message received: "+arg1.getStringExtra("tickerText"));
		Log.e(" From GCMIntentService", "Message received: "+arg1.getStringExtra("contentTitle"));
		Log.e(" From GCMIntentService", "Message received: "+arg1.getStringExtra("contentText"));
		//arg1.getStringExtra("message");
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.e("From GCMIntentService", "Unregistration id:"+arg1);
		
	}
	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.e("From GCMIntentService", "Error id:"+errorId);
		return false;
	}
}
