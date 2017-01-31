package com.whywib.www.latairmeter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class InputDataActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_HOME = 0;
	private static final int CAPTURE_IMAGE_ACTIVITY_METERAN = 1;
	EditText meteranAkhirET, keteranganET;
	Typeface font;
	ImageView homeIV, MeteranIV;
	Button takeCameraHomeBtn, takeCameraMeteranBtn, clearCameraHomeBtn, clearCameraMeteranBtn, submitBtn;
	int status=0, meterOld, meternew;
	int Ids;
	String DateImport = "";
	String IdentNumberRek ="";
	String PeriodMonth = "";
	String IdentName = "";
	String IdentAddress = "";
	String Keterangan = "";
	String Coordinates = "";
	String DateEntry = "";
	String NoSaluran = "";
	String routetrack = "";
	byte[] pictHome = {0}, pictMeter = {0};
	Double Lat = 0.0,Lon = 0.0;
	DataBaseManager dataBase;
	Spinner statusSpinner;
	
	Bitmap mImageBitmapmeter,mImageBitmaphome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_data);
		dataBase = DataBaseManager.instance();
		Bundle extras = getIntent().getExtras();
		NoSaluran = extras.getString("NoSaluran");
		final LocationManager mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocListener = new MyLocationListener();
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
        final Location location= mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		meteranAkhirET = (EditText) findViewById(R.id.newMeterET);
		keteranganET = (EditText) findViewById(R.id.keteranganET);
		font = Typeface.createFromAsset(getAssets(), "font.ttf");
		
		statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
	//	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.status_array, android.R.layout.simple_spinner_item);
	//	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		
        List<String> lables = dataBase.getAllLabels();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	
        
        
		statusSpinner.setAdapter(adapter);
		homeIV	= (ImageView) findViewById(R.id.homeIV);
		homeIV.setTag(null);
		MeteranIV	= (ImageView) findViewById(R.id.MeteranIV);
		takeCameraHomeBtn	= (Button) findViewById(R.id.takeCameraHome);
		takeCameraMeteranBtn	= (Button) findViewById(R.id.takeCameraMeteran);
		clearCameraHomeBtn	= (Button) findViewById(R.id.clearCameraHome);
		clearCameraMeteranBtn	= (Button) findViewById(R.id.clearCameraMeteran);
		submitBtn	= (Button) findViewById(R.id.submitBtn);
		takeCameraHomeBtn.setTypeface(font);
		takeCameraMeteranBtn.setTypeface(font);
		clearCameraHomeBtn.setTypeface(font);
		clearCameraMeteranBtn.setTypeface(font);
		submitBtn.setTypeface(font);
		Cursor detailCsr = dataBase.selectDetail(NoSaluran);
		detailCsr.moveToFirst();
		Ids = detailCsr.getInt(0);
		DateImport = detailCsr.getString(1);
		IdentNumberRek = detailCsr.getString(2);
		meterOld = detailCsr.getInt(3);
		PeriodMonth = detailCsr.getString(4);
		IdentName = detailCsr.getString(5);
		IdentAddress = detailCsr.getString(6);
		routetrack = detailCsr.getString(7);
		takeCameraHomeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callCameraHome();
			}
		});
		takeCameraMeteranBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callCameraMeteran();
			}
		});
		clearCameraHomeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearImageHome();
			}
		});
		clearCameraMeteranBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearImageMeteran();
			}
		});
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String statusStr =statusSpinner.getSelectedItem().toString();
				if(statusStr.equals("Batal"))
				{
					status = 0;
				}
				else if(statusStr.equals("Input"))
				{
					status = 1;
				}
				else if(statusStr.equals("Ada Anjing"))
				{
					status = 2;
				}
				else if(statusStr.equals("Angka Meter Buram"))
				{
					status = 3;
				}
				else if(statusStr.equals("Angka Meter Dalam"))
				{
					status = 4;
				}
				else if(statusStr.equals("Ganti Meter"))
				{
					status = 5;
				}
				else if(statusStr.equals("Kaca Meter Pecah"))
				{
					status = 6;
				}
				else if(statusStr.equals("Meter Di Bolak Balik/Mundur"))
				{
					status = 7;
				}
				else if(statusStr.equals("Meter Hilang"))
				{
					status = 8;
				}
				else if(statusStr.equals("Meter Macet"))
				{
					status = 9;
				}
				else if(statusStr.equals("Meter Terbalik"))
				{
					status = 10;
				}
				else if(statusStr.equals("Meter Terendam Air"))
				{
					status = 11;
				}
				else if(statusStr.equals("Meter Tertimbun"))
				{
					status = 12;
				}
				else if(statusStr.equals("Pagar Terkunci"))
				{
					status = 13;
				}
				else if(statusStr.equals("Stan Tunggu"))
				{
					status = 14;
				}
				else if(statusStr.equals("Tanpa Meter"))
				{
					status = 15;
				}
				else if(statusStr.equals("Lain-Lain"))
				{
					status = 16;
				}
				

				
				if(meteranAkhirET.getText().toString().length()<1)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(InputDataActivity.this);
			        builder.setTitle("Please Input Meteran Akhir !");
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
				else if(MeteranIV.getTag()==null)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(InputDataActivity.this);
			        builder.setTitle("Please Input Foto Meteran !");
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
					try
					{
						if(keteranganET.getText().toString().length()>1)
						{
							Keterangan = keteranganET.getText().toString();
						}
						if(homeIV.getTag()!=null)
						{
							pictHome = (byte[]) homeIV.getTag();
						}
						pictMeter = (byte[]) MeteranIV.getTag();
						if(location!=null)
						{
							Lat = location.getLatitude();
							Lon = location.getLongitude();
						}
						DateEntry = getTodaysDate();
						meternew = Integer.parseInt(meteranAkhirET.getText().toString());
						Coordinates = Double.toString(Lat)+", "+Double.toString(Lon);
						dataBase.deleteMeterMobile(NoSaluran);
						ContentValues values = new ContentValues();
						values.put("Ids", Ids);
						values.put("DateImport", DateImport);
						values.put("IdentNumberSal", NoSaluran);
						values.put("IdentNumberRek", IdentNumberRek);
						values.put("MeterOld", meterOld);
						values.put("MeterNew", meternew);
						values.put("PeriodMonth", PeriodMonth);
						values.put("IdentName", IdentName);
						values.put("IdentAddress", IdentAddress);
						values.put("PicHome", pictHome);
						values.put("Coordinates", Coordinates);
						values.put("status", status);
						values.put("ket", Keterangan);
						values.put("routetrack", routetrack);
						values.put("DateEntry", DateEntry);
						values.put("PicMeter",pictMeter);
						dataBase.insert("metermobile", values);		
						
						saveImage(mImageBitmaphome,"Home");
						saveImage(mImageBitmapmeter,"Meter");
						
						Toast.makeText(getBaseContext(), "Can't get location",
								Toast.LENGTH_SHORT).show();
						Intent i = new Intent("com.whywib.www.latairmeter.InputSaluranActivity");
			        	startActivity(i);
			        	finish();
									
					}
					catch(Exception e)
					{
						if(keteranganET.getText().toString().length()>1)
						{
							Keterangan = keteranganET.getText().toString();
						}
						if(homeIV.getTag()!=null)
						{
							pictHome = (byte[]) homeIV.getTag();
						}
						pictMeter = (byte[]) MeteranIV.getTag();
						DateEntry = getTodaysDate();
						meternew = Integer.parseInt(meteranAkhirET.getText().toString());
						dataBase.deleteMeterMobile(NoSaluran);
						ContentValues values = new ContentValues();
						values.put("Ids", Ids);
						values.put("DateImport", DateImport);
						values.put("IdentNumberSal", NoSaluran);
						values.put("IdentNumberRek", IdentNumberRek);
						values.put("MeterOld", meterOld);
						values.put("MeterNew", meternew);
						values.put("PeriodMonth", PeriodMonth);
						values.put("IdentName", IdentName);
						values.put("IdentAddress", IdentAddress);
						values.put("PicHome", pictHome);
						values.put("Coordinates", "0.0,0.0");
						values.put("status", status);
						values.put("ket", Keterangan);
						values.put("routetrack", routetrack);
						values.put("DateEntry", DateEntry);
						values.put("PicMeter",pictMeter);
						dataBase.insert("metermobile", values);
						
						saveImage(mImageBitmaphome,"Home");
						saveImage(mImageBitmapmeter,"Meter");
						
						Intent i = new Intent("com.whywib.www.latairmeter.InputSaluranActivity");
			        	startActivity(i);
			        	finish();
					}
				}
				
			}
		});
	}
	public void callCameraHome() 
	{
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_HOME);
	}
	public void callCameraMeteran() 
	{	
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_METERAN);
	}
	public void clearImageHome()
	{
		homeIV.setBackgroundResource(R.drawable.home);
		homeIV.setImageBitmap(null);
		homeIV.setTag(null);
	}
	public void clearImageMeteran()
	{
		MeteranIV.setBackgroundResource(R.drawable.keyboard);
		MeteranIV.setImageBitmap(null);
		MeteranIV.setTag(null);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_HOME) 
		  {
			  if (resultCode == RESULT_OK) 
			  {
				  Bundle extras = data.getExtras();
				  mImageBitmaphome = (Bitmap)extras.get("data");
				  ByteArrayOutputStream stream = new ByteArrayOutputStream();
				  mImageBitmaphome.compress(Bitmap.CompressFormat.PNG, 100, stream);
				  byte[] byteArray = stream.toByteArray();
				  int width = mImageBitmaphome.getWidth();
				  int height = mImageBitmaphome.getHeight();
				  int newWidth = 160;
				  int newHeight  = 120;
				  float scaleWidth = ((float) newWidth) / width;
				  float scaleHeight = ((float) newHeight) / height;
				  Matrix matrix = new Matrix();
				  matrix.postScale(scaleWidth, scaleHeight);
				  matrix.postRotate(0);
				  Bitmap bitmapKecil = Bitmap.createBitmap(mImageBitmaphome, 0, 0, width, height, matrix, true);
				  homeIV.setScaleType(ScaleType.CENTER);
				  homeIV.setImageBitmap(bitmapKecil);
				  homeIV.setTag(byteArray);
			    } 
			    else if (resultCode == RESULT_CANCELED) 
			    {
			      Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
			    } 
			    else 
			    {
			      Toast.makeText(this, "Callout for image capture failed!", 
			                     Toast.LENGTH_LONG).show();
			    }
		  }
		  else if (requestCode == CAPTURE_IMAGE_ACTIVITY_METERAN) 
		  {
			  if (resultCode == RESULT_OK) 
			  {
				  Bundle extras = data.getExtras();
				  mImageBitmapmeter = (Bitmap)extras.get("data");
				  ByteArrayOutputStream stream = new ByteArrayOutputStream();
				  mImageBitmapmeter.compress(Bitmap.CompressFormat.PNG, 100, stream);
				  byte[] byteArray = stream.toByteArray();
				  int width = mImageBitmapmeter.getWidth();
				  int height = mImageBitmapmeter.getHeight();
				  int newWidth = 160;
				  int newHeight  = 120;
				  float scaleWidth = ((float) newWidth) / width;
				  float scaleHeight = ((float) newHeight) / height;
				  Matrix matrix = new Matrix();
				  matrix.postScale(scaleWidth, scaleHeight);
				  matrix.postRotate(0);
				  Bitmap bitmapKecil = Bitmap.createBitmap(mImageBitmapmeter, 0, 0, width, height, matrix, true);
				  MeteranIV.setScaleType(ScaleType.CENTER);
				  MeteranIV.setImageBitmap(bitmapKecil);
				  MeteranIV.setTag(byteArray);
				  
				  
			    } 
			    else if (resultCode == RESULT_CANCELED) 
			    {
			      Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
			    } 
			    else 
			    {
			      Toast.makeText(this, "Callout for image capture failed!", 
			                     Toast.LENGTH_LONG).show();
			    }
		  }
		 
	
	 }
	 private String getTodaysDate() 
	 {
		 final Calendar c = Calendar.getInstance(); 
		 return(new StringBuilder()
		 .append(c.get(Calendar.YEAR)).append("-")
		 .append(c.get(Calendar.MONTH) + 1).append("-")
		 .append(c.get(Calendar.DAY_OF_MONTH)).append("")).toString();
	 }
	 public class MyLocationListener implements LocationListener{        

	        public void onLocationChanged(Location loc) {           
	        	Lat = loc.getLatitude();
	        	Lon = loc.getLongitude();	
	        }
	        public void onProviderDisabled(String arg0) {

	        }
	        public void onProviderEnabled(String provider) {

	        }
	        public void onStatusChanged(String provider, int status, Bundle extras) {

	        }       
	    }	
		public boolean onKeyDown(int keyCode, KeyEvent event) 
	    {
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) 
	        {
	        	
	        	Intent i = new Intent("com.whywib.www.latairmeter.InputSaluranActivity");
	        	startActivity(i);
	        	finish();
	        	
	    		return false;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
		

		public void saveImage(Bitmap image, String Gambarapa) {
			
			String fullPath;
			String namafilegambar;
			File dir;

			try {
				
				
				if (Gambarapa=="Home"){
				
					fullPath = Environment.getExternalStorageDirectory()  +  "/GbrHome/";
					dir = new File(Environment.getExternalStorageDirectory(),"/GbrHome");
					if (!dir.exists()) {
						dir.mkdirs();
					}
				}
				else
				{
					fullPath = Environment.getExternalStorageDirectory()  +  "/GbrMeter/";
					dir = new File(Environment.getExternalStorageDirectory(),"/GbrMeter");
					if (!dir.exists()) {
						dir.mkdirs();
					}
				}
					
				
				namafilegambar= NoSaluran + "_" + PeriodMonth + ".jpg";
				
				OutputStream fOut = null;
				File file = new File(fullPath, namafilegambar);
				file.createNewFile();
				fOut = new FileOutputStream(file);

				image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();

			} catch (Exception e) {
				
			}
		}
}
