package com.whywib.www.latairmeter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreviewActivity extends Activity {

	DataBaseManager dataBase;
	String NoSaluran = "";
	Typeface font;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		dataBase = DataBaseManager.instance();
		Bundle extras = getIntent().getExtras();
		NoSaluran = extras.getString("NoSaluran");
		font = Typeface.createFromAsset(getAssets(), "font.ttf");
		TextView namaPelangganTV = (TextView) findViewById(R.id.namaPelangganTV);
		TextView alamatTV = (TextView) findViewById(R.id.alamatTV);
		TextView meteranAwalTV = (TextView) findViewById(R.id.meteranAwalTV);
		TextView nosalTV = (TextView) findViewById(R.id.nosalTV);
		Button nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setTypeface(font);
		Cursor preview = dataBase.selectPreview(NoSaluran);
		if(preview.getCount()>0)
		{
			preview.moveToFirst();
			nextBtn.setVisibility(LinearLayout.VISIBLE);
			String namaPelangganStr = preview.getString(0);
			String alamatStr = preview.getString(1);
			String meteranAwalStr = preview.getString(2);
			String nosalStr = preview.getString(4);
			namaPelangganTV.setText(namaPelangganStr);
			alamatTV.setText(alamatStr);
			 
			meteranAwalTV.setText(meteranAwalStr);
			nosalTV.setText(nosalStr);
			nextBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent("com.whywib.www.latairmeter.InputDataActivity");
					i.putExtra("NoSaluran", NoSaluran);
					startActivity(i);
					finish();
				}
			});
			
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(PreviewActivity.this);
	        builder.setTitle("No. Saluran Salah !");
	        String ok = "Ok";
	        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() 
	        {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					finish();
				}
			});
	        
	        builder.create().show();
		}
		
		
	}


}
