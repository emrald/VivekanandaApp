package com.clarusapps.SwamiVivekananda;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class MyTabActivity extends TabActivity{
	public static final String MY_BANNER_UNIT_ID ="a1505acfbf6f7bc";
	TabHost tabHost;
	private static final String NOTIFICATION_TITLE = "contentTitle";
	private static final String NOTIFICATION_CONTENT = "contentText";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_lay);
		
		Resources res = getResources();
        
        tabHost=getTabHost();
	    TabSpec tabSpec;
        tabSpec=tabHost.newTabSpec("home").setIndicator("QUOTE",res.getDrawable(R.drawable.icon_home)).setContent(new Intent(this,Mainscreen.class));
        tabHost.addTab(tabSpec);
	        
        tabSpec=tabHost.newTabSpec("biography").setIndicator("BIOGRAPHY",res.getDrawable(R.drawable.icon_create)).setContent(new Intent(this,Biography.class));
        tabHost.addTab(tabSpec);

        tabSpec=tabHost.newTabSpec("favourite").setIndicator("FAVOURITE",res.getDrawable(R.drawable.icon_fav)).setContent(new Intent(this,FavouriteActivity.class));
        tabHost.addTab(tabSpec);
	        
        tabSpec=tabHost.newTabSpec("more").setIndicator("MORE",res.getDrawable(R.drawable.icon_more)).setContent(new Intent(this,MoreActivity.class));
        tabHost.addTab(tabSpec);
        
        tabHost.getTabWidget().setBackgroundColor(Color.BLACK);
        /*for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.BLACK);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.WHITE);
          }*/
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.admoblayout);
		AdView adView = new AdView(this, AdSize.BANNER, MY_BANNER_UNIT_ID);
		layout.addView(adView);
		AdRequest request = new AdRequest();
		//request.setTesting(true);
		adView.loadAd(request);
	}
	public static void showNotification(Context context, Bundle msgExtras) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.notification_icon;
		String title = stripHtmlTags(msgExtras.getString(NOTIFICATION_TITLE));
		title = android.text.Html.fromHtml(title).toString();
		String content = stripHtmlTags(msgExtras
				.getString(NOTIFICATION_CONTENT));
		content = android.text.Html.fromHtml(content).toString();

		Notification notification = new Notification(icon, title,
				System.currentTimeMillis());
		notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
		notification.sound = Uri.parse("android.resource://" +context.getApplicationContext().getPackageName()+ "/" + R.raw.notification_sound);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(content));
		PendingIntent activity = PendingIntent.getActivity(context, 0, intent,0);
		notification.setLatestEventInfo(context, title, content, activity);
		notification.number += 1;
		notificationManager.notify(0, notification);

	}

	static String stripHtmlTags(String html) {
		return Html.fromHtml(html.replaceAll("\\<[^>]*>", "")).toString();
	}

}
