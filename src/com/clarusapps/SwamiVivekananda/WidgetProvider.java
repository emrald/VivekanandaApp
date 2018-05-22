package com.clarusapps.SwamiVivekananda;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.RemoteViews;


public class WidgetProvider extends AppWidgetProvider{
	Typeface tf;
	DatabaseHelper db;
	String quote="";
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		tf = Typeface.createFromAsset(context.getAssets(),
				"Gabriola.ttf");
		
		db=MyApplication.getDataBase();
		quote=db.getWidgetQuote();
		updateWidgetContent(context, appWidgetManager);
	}
	public void updateWidgetContent(Context context,
			AppWidgetManager appWidgetManager) {

		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_layout);

		Intent launchAppIntent = new Intent(context, MyTabActivity.class);

		PendingIntent launchAppPendingIntent = PendingIntent.getActivity(
				context, 0, launchAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		remoteView.setTextViewText(R.id.widget_tv_Quote, quote);
		remoteView.setTextColor(R.id.widget_tv_Quote, Color.WHITE);

		remoteView.setOnClickPendingIntent(R.id.widget_tv_Quote,
				launchAppPendingIntent);

		ComponentName tutListWidget = new ComponentName(context,
				WidgetProvider.class);
		appWidgetManager.updateAppWidget(tutListWidget, remoteView);
	}
}
