package com.clarusapps.SwamiVivekananda;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CAFav extends ArrayAdapter<Quote>{
	public LayoutInflater inflater;
	ArrayList<Quote> arrQuote;
	
	
	public CAFav(Context context, int textViewResourceId,ArrayList<Quote> arrayQuote) {
		super(context, textViewResourceId,arrayQuote);
		this.arrQuote=arrayQuote;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	public static class ViewHolder
	{	
		TextView txtViewQuote;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.fav_row_lay, null);
			
			holder.txtViewQuote= (TextView) convertView.findViewById(R.id.row_tv_quote);
			
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		Quote quote=getItem(position);
		holder.txtViewQuote.setText(quote.getQuote());
		
		return convertView;
	}
}
