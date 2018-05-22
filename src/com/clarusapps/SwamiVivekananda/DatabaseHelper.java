package com.clarusapps.SwamiVivekananda;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper {
	public static final String DB_NAME = "swamiji.db";
	public static final String DB_TABLE = "table_quote";
	private static String DB_PATH = "";


	public static final int DB_VERSION = 3;
	private SQLiteDatabase db;
	private static Context acontext;
	private final QuoteDataClass quoteDb;

	public DatabaseHelper(Context context) {
		acontext = context;
		DB_PATH = "/data/data/"	+ context.getApplicationContext().getPackageName() + "/databases/";

		this.quoteDb = new QuoteDataClass(context);
		if(this.db!=null){
			if(this.db.isOpen()==false){
				this.db=this.quoteDb.getWritableDatabase();
			}
		}
		else if (this.db == null) 
		{
			this.db = this.quoteDb.getWritableDatabase();
		}
		
	}
	private static class QuoteDataClass extends SQLiteOpenHelper{
		Context contex;
		public QuoteDataClass(Context context) {
			super(context, DB_NAME,null,DB_VERSION);
			this.contex=context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			int i=insertFromFile(contex,R.raw.my,db);
			Log.e("squ statements", "created row="+i);
			/*if(copyDataBase()){
				
			}else{
				Toast.makeText(acontext, "Db not copied", Toast.LENGTH_SHORT).show();
			}*/

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
		public boolean copyDataBase(){
			
			new Thread(){
				ProgressDialog pd=ProgressDialog.show(acontext, "Loading...", "DB importing");
				public void run(){
					try {
						// Open your local db as the input stream
						InputStream myInput = acontext.getAssets().open(DB_NAME);
						// Path to the just created empty db
						String outFileName = DB_PATH + DB_NAME;
						// Open the empty db as the output stream
						OutputStream myOutput = new FileOutputStream(outFileName);
						// transfer bytes from the inputfile to the outputfile
						byte[] buffer = new byte[1024];
						int length;
						while ((length = myInput.read(buffer)) > 0) {
							myOutput.write(buffer, 0, length);
						}
						// Close the streams
						myOutput.flush();
						myOutput.close();
						myInput.close();
						mHandler.sendEmptyMessage(1);
						pd.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(2);
					}
				}
				private Handler mHandler=new Handler(){
					@Override
					public void handleMessage(Message msg){
						if(msg.what==1){
							Toast.makeText(acontext, "Database copied", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}else if(msg.what==2){
							Toast.makeText(acontext, "Database not copied", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}else{
							pd.dismiss();
						}
					}
				};
			}.start();
			return false;
		}
		public int insertFromFile(Context context, int resourceId,SQLiteDatabase db)  {
		    // Reseting Counter
		    try {
		    	int result = 0;

			    // Open the resource
			    InputStream insertsStream = context.getResources().openRawResource(resourceId);
			    BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

			    // Iterate through lines (assuming each insert has its own line and theres no other stuff)
			    while (insertReader.ready()) {
			        String insertStmt = insertReader.readLine();
			        db.execSQL(insertStmt);
			        result++;
			    }
			    insertReader.close();

			    // returning number of inserted rows
			    return result;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			
		}
	}
	
	public void closeDB(){
		if(this.db.isOpen()){
			Log.e("quote", "DB close");
			this.db.close();
		}
	}
	public boolean isOpen(){
		return db.isOpen();
	}
	public ArrayList<Quote> getQuote(){
		ArrayList<Quote> myQuote=new ArrayList<Quote>();
		Cursor quoteCursor=this.db.query(DB_TABLE, null, null, null, null, null, "random()");
		if(quoteCursor.getCount()>0){
			quoteCursor.moveToFirst();
			do {
				
				 myQuote.add(new Quote(quoteCursor.getInt(quoteCursor.getColumnIndex("_id")),quoteCursor.getString(quoteCursor.getColumnIndex("quote")),quoteCursor.getInt(quoteCursor.getColumnIndex("fav"))));
			} while (quoteCursor.moveToNext());
			quoteCursor.close();
		}
		return myQuote;
	}
	public String getWidgetQuote(){
		String s="";
		try {
			Cursor quoteCursor=this.db.query(DB_TABLE, null, null, null, null, null, "random()");
			if(quoteCursor.getCount()>0){
				quoteCursor.moveToFirst();
				s=quoteCursor.getString(quoteCursor.getColumnIndex("quote"));
				quoteCursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return s;
	}
	public boolean makeQuoteFav(int quote_id){
		try {
			ContentValues value=new ContentValues();
			value.put("fav", 1);
			int i=db.update(DB_TABLE, value, "_id = "+quote_id,null);
			if(i>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public ArrayList<Quote> getAllFavQuote(){
		ArrayList<Quote> myFav=new ArrayList<Quote>();
		try {
			Cursor quoteCursor=this.db.query(DB_TABLE, null, "fav = "+1, null, null, null, "random()");
			if(quoteCursor.getCount()>0){
				quoteCursor.moveToFirst();
				do {
					 myFav.add(new Quote(quoteCursor.getInt(quoteCursor.getColumnIndex("_id")),quoteCursor.getString(quoteCursor.getColumnIndex("quote")),quoteCursor.getInt(quoteCursor.getColumnIndex("fav"))));
				} while (quoteCursor.moveToNext());
				quoteCursor.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myFav;
	}
	public boolean removeQuoteFromFav(int quote_id){
		try {
			ContentValues value=new ContentValues();
			value.put("fav", 0);
			int i=db.update(DB_TABLE, value, "_id = "+quote_id,null);
			if(i>0){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
