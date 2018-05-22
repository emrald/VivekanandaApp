package com.clarusapps.SwamiVivekananda;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Biography extends Activity {
	Button homebtn,favbtn,infobtn;
	TextView txtHeader,textQuote;
	Typeface tf;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	       setContentView(R.layout.biopage);
	        
	        tf = Typeface.createFromAsset(getBaseContext().getAssets(),
					"Gabriola.ttf");
	        
	        txtHeader = (TextView)findViewById(R.id.txtHeader);
	        textQuote = (TextView)findViewById(R.id.textQuote);
	        
	        textQuote.setTypeface(tf);
	        txtHeader.setTypeface(tf);
	}

}
