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
import android.widget.EditText;

public class LoginActivity extends Activity {

	DataBaseManager dataBase;
	Typeface font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		dataBase = DataBaseManager.instance();
		font = Typeface.createFromAsset(getAssets(), "font.ttf");
		final EditText usernameET = (EditText) findViewById(R.id.usernameET);
		final EditText passwordET = (EditText) findViewById(R.id.passwordET);
		Button loginBtn = (Button) findViewById(R.id.loginBtn);
		loginBtn.setTypeface(font);
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(usernameET.getText().toString().length()<1)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
			        builder.setTitle("Please Input Username !");
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
				else if(passwordET.getText().toString().length()<1)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
			        builder.setTitle("Please Input Password !");
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
					Cursor loginCsr = dataBase.selectLogin(usernameET.getText().toString());
					if(loginCsr.getCount()>0)
					{
						loginCsr.moveToFirst();
						String pass = loginCsr.getString(0);
						if(passwordET.getText().toString().equals(pass))
						{
							Intent i = new Intent("com.whywib.www.latairmeter.MainActivity");
							startActivity(i);
							finish();
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
					        builder.setTitle("Username Or Password Not Valid !");
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
					}
					else
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				        builder.setTitle("Username Or Password Not Valid !");
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
				}
			}
		});
	}
}
