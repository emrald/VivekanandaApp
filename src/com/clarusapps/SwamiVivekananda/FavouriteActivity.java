package com.clarusapps.SwamiVivekananda;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class FavouriteActivity extends ListActivity {
	ArrayList<Quote> fav; 
	public static String INTENT_STRING_QUOTE="quote";
	public static String INTENT_STRING_QUOTE_ID="quote_id";
	Typeface tf;
	CAFav adapter;
	ListView lv;
	DatabaseHelper db;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav);
		db=MyApplication.getDataBase();
		
		tf = Typeface.createFromAsset(getBaseContext().getAssets(),
				"Gabriola.ttf");
		lv=getListView();
	    fav=db.getAllFavQuote();
	    adapter=new CAFav(this, R.layout.fav_row_lay, fav);
	    
	    lv.setAdapter(adapter);
	    
	    lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(FavouriteActivity.this,FavouriteInnerActivity.class);
				Quote quote=((Quote)arg0.getAdapter().getItem(arg2));
				Log.e("item id", "quote id:"+quote.getQuoteId());
				
				intent.putExtra(INTENT_STRING_QUOTE, quote.getQuote());
				intent.putExtra(INTENT_STRING_QUOTE_ID,quote.getQuoteId());
				startActivity(intent);
				
			}
		});
	    
	    lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				Log.e("item id", "quote id:"+((Quote)arg0.getAdapter().getItem(arg2)).getQuoteId());
				 AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteActivity.this);
			        builder.setMessage("Do you want to remove Quote from favourite?")
			               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			                   public void onClick(DialogInterface dialog, int id) {
			                       db.removeQuoteFromFav(((Quote)arg0.getAdapter().getItem(arg2)).getQuoteId());
			                       dialog.dismiss();
			                       fav=db.getAllFavQuote();
			               	    adapter=new CAFav(FavouriteActivity.this, R.layout.fav_row_lay, fav);
			               	    adapter.notifyDataSetChanged();
			               	    lv.setAdapter(adapter);
			                   }
			               })
			               .setNegativeButton("No", new DialogInterface.OnClickListener() {
			                   public void onClick(DialogInterface dialog, int id) {
			                       dialog.dismiss();
			                   }
			               }); 
			        builder.show();
				return false;
			}
		});
	}
	@Override
	protected void onResume() {
		fav=db.getAllFavQuote();
	    adapter=new CAFav(this, R.layout.fav_row_lay, fav);
	    adapter.notifyDataSetChanged();
	    lv.setAdapter(adapter);
		super.onResume();
	}
}