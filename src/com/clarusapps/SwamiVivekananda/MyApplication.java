package com.clarusapps.SwamiVivekananda;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application{

	private static DatabaseHelper db;
	
	@Override
	public void onCreate() {
		Log.v("In MyApplication", "On create");
		if(db==null){
			db=new DatabaseHelper(getApplicationContext());
		}else if(db.isOpen()==false){
			db=new DatabaseHelper(getApplicationContext());
		}
		
		super.onCreate();
	}
	public static DatabaseHelper getDataBase(){
		Log.v("In MyApplication", "getDatabase()");
		return db;
	}
	public static void closeDataBase(){
		Log.v("In MyApplication", "in close db");
		db.closeDB();
	}
}
