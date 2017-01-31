package com.whywib.www.latairmeter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Context c;
	Typeface font;
	DataBaseManager dataBase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataBase = DataBaseManager.instance();
		font = Typeface.createFromAsset(getAssets(), "font.ttf");
		Button twoWayBtn = (Button) findViewById(R.id.startBtn);
		Button oneWayBtn = (Button) findViewById(R.id.clearBtn);
		twoWayBtn.setTypeface(font);
		oneWayBtn.setTypeface(font);
		twoWayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent("com.whywib.www.latairmeter.InputSaluranActivity");
				startActivity(i);
			}
		});
	}
	@SuppressLint("SdCardPath")
	public static void copyDatabase(Context c, String DATABASE_NAME) {
	        String databasePath = "/mnt/sdcard/metermobile.sqlite";
	        File f = new File(databasePath);
	        OutputStream myOutput = null;
	        InputStream myInput = null;
	       

	        if (f.exists()) {
	            try {

	                File directory = new File(Environment.getExternalStorageDirectory(),"MeterMobile_Export");
	                if (!directory.exists())
	                    directory.mkdir();

	                myOutput = new FileOutputStream(directory.getAbsolutePath()
	                        + "/" + DATABASE_NAME);
	                myInput = new FileInputStream(databasePath);

	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = myInput.read(buffer)) > 0) {
	                    myOutput.write(buffer, 0, length);
	                }

	                myOutput.flush();
	            } catch (Exception e) 
	            {
	            } finally {
	                try {
	                    if (myOutput != null) {
	                        myOutput.close();
	                        myOutput = null;
	                    }
	                    if (myInput != null) {
	                        myInput.close();
	                        myInput = null;
	                    }
	                } catch (Exception e) {
	                }
	            }
	        }	
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
    	super.onCreateOptionsMenu(menu);
    	CreateMenu(menu);
        return true;
    }
	public void CreateMenu(Menu menu)
    {
    	
    		MenuItem mnuExport = menu.add(0, 1, 1,"Export");
    		mnuExport.setOnMenuItemClickListener(new OnMenuItemClickListener() 
        	{
    			
    			@Override
    			public boolean onMenuItemClick(MenuItem arg0) 
    			{
    				copyDatabase(c, "metermobile.sqlite");
    				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    		        builder.setTitle("Export Success !");
    		        String ok = "Ok";
    		        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() 
    		        {
    					
    					@Override
    					public void onClick(DialogInterface arg0, int arg1) 
    					{
    						
    					}
    				});
    		        
    		        builder.create().show();
    				return true;
    			}
    		});
        	MenuItem mnuImport = menu.add(0, 1, 1,"Import");
        	mnuImport.setOnMenuItemClickListener(new OnMenuItemClickListener() 
        	{
    			
    			@Override
    			public boolean onMenuItemClick(MenuItem arg0) 
    			{
    				try
    				{
    					dataBase.openDataBaseFromSDCard();
    					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    			        builder.setTitle("Import Success !");
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
    				catch(Exception e)
    				{
    					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    			        builder.setTitle("No database in SD Card !");
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
    				return true;
    			}
    		});
        	MenuItem mnuReset = menu.add(0, 1, 1,"Hapus Data");
        	mnuReset.setOnMenuItemClickListener(new OnMenuItemClickListener() 
        	{
    			
    			@Override
    			public boolean onMenuItemClick(MenuItem arg0) 
    			{
    				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    		        builder.setTitle("Are You Sure Yo Want To Reset Data ?");
    		        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
    		        {
    					
    					@Override
    					public void onClick(DialogInterface arg0, int arg1) 
    					{
    						dataBase.deleteMeterMobile();
    					}
    				});
    		        builder.setNegativeButton("No", new DialogInterface.OnClickListener() 
    		        {
    					
    					@Override
    					public void onClick(DialogInterface arg0, int arg1) 
    					{
    						
    					}
    				});
    		        
    		        builder.create().show();
    				return true;
    			}
    		});
    	
    }
}
