package com.whywib.www.latairmeter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class InputSaluranActivity extends Activity {
	
	ListView mainListView ;  
	Typeface font;
	String[] list ;
	List<String> where = new ArrayList<String>();
	List<String> IDList = new ArrayList<String>();
	private ArrayAdapter<String> listAdapter ; 
	DataBaseManager dataBase;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_saluran);
		dataBase = DataBaseManager.instance();
		font = Typeface.createFromAsset(getAssets(), "font.ttf"); 
		final EditText saluranET = (EditText) findViewById(R.id.saluranET);
		mainListView = (ListView) findViewById( R.id.statusLV);
		mainListView.setOnTouchListener(new OnTouchListener() {
		    // Setting on Touch Listener for handling the touch inside ScrollView
			  @Override
			    public boolean onTouch(View v, MotionEvent event) {
			    // Disallow the touch request for parent scroll on touch of child view
			    v.getParent().requestDisallowInterceptTouchEvent(true);
			    return false;
			    }

		});
		Cursor listCsr = dataBase.selectListSaluran();
		if(listCsr.getCount()>0)
		{
			listCsr.moveToFirst();
			while(listCsr.isAfterLast()==false)
			{
				String numSal = listCsr.getString(0);
				String address = listCsr.getString(1);
				where.add(address);
				IDList.add(numSal);
				listCsr.moveToNext();
			}
			list = new String[ where.size() ];
			where.toArray(list);
			ArrayList<String> planetList = new ArrayList<String>();  
		    planetList.addAll( Arrays.asList(list) );  
		    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList); 
		    mainListView.setAdapter( listAdapter );
			mainListView.setOnItemClickListener(new OnItemClickListener() 
		    {
			@Override
				public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) 
		    	{
					String id = IDList.get(myItemInt).toString();
					Intent i = new Intent("com.whywib.www.latairmeter.PreviewActivity");
					i.putExtra("NoSaluran", id);
					startActivity(i);
					finish();
		    	}
	  
			});
		}
		
		
		
		
		Button nextBtn = (Button) findViewById(R.id.nextBtn);
		nextBtn.setTypeface(font);
		nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(saluranET.getText().toString().length()<1)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(InputSaluranActivity.this);
			        builder.setTitle("Please Input No. Saluran !");
			        String ok = "Ok";
			        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() 
			        {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) 
						{
							
						}
					});
			        
			        builder.create().show();
				}
				else
				{
					Intent i = new Intent("com.whywib.www.latairmeter.PreviewActivity");
					i.putExtra("NoSaluran", saluranET.getText().toString());
					startActivity(i);
					finish();
				}
			}
		});
	}
}
