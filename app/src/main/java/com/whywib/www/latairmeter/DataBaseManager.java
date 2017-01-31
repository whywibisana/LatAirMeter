package com.whywib.www.latairmeter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
 
@SuppressLint("SdCardPath")
public class DataBaseManager extends SQLiteOpenHelper 
{
  
	
	private static String DB_PATH = "/data/data/com.whywib.www.latairmeter/databases/";
	private static String DB_PATH_SDCARD = "/mnt/sdcard/";
    private static String DB_NAME = "metermobile.sqlite";
    private static SQLiteDatabase mDataBase;
    private static DataBaseManager sInstance = null;
    private static final int DATABASE_VERSION = 1;
 
    private DataBaseManager() 
    {
        super(ApplicationContextProvider.getContext(), DB_NAME, null, DATABASE_VERSION);
 
        try 
        {
            createDataBase();
            openDataBase();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
    }
 
    public static DataBaseManager instance() 
    {
        if (sInstance == null) 
        {
            sInstance = new DataBaseManager();
        }
        return sInstance;
    }
 
    private void createDataBase() throws IOException
    {
        boolean dbExist = checkDataBase();
 
        if (dbExist) 
        {
        } 
        else 
        {
            this.getReadableDatabase();
 
            try 
            {
                copyDataBase();
            } 
            catch (IOException e) 
            {
                throw new Error("Error copying database");
            }
        }
    }
 
    private boolean checkDataBase() 
    {
        SQLiteDatabase checkDB = null;
 
        try 
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } 
        catch (SQLiteException e) 
        {
        }
        if (checkDB != null) 
        {
            checkDB.close();
        }
        return checkDB != null;
    }
 
    public void copyDataBase() throws IOException 
    {
        InputStream myInput = ApplicationContextProvider.getContext().getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) 
        {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
 
    }
 
    private void openDataBase() throws SQLException 
    {
        String myPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }
    public void openDataBaseFromSDCard() throws SQLException 
    {
        String myPath = DB_PATH_SDCARD + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }
    public Cursor select(String query) throws SQLException 
    {
        return mDataBase.rawQuery(query, null);
    }
    
	@Override
	public void onCreate(SQLiteDatabase arg0) 
	{
	
		
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) 
	{
	
		
	}
	public void insert(String table, ContentValues values) throws SQLException 
    {
        mDataBase.insert(table, null, values);
    }
	public void deleteMeterMobile() throws SQLException
	{
	    mDataBase.execSQL("DELETE FROM metermobile");
	}
	public void deleteMeterMobile(String IdentNumberSal) throws SQLException
	{
	    mDataBase.execSQL("DELETE FROM metermobile WHERE IdentNumberSal = '"+IdentNumberSal+"'");
	}
	public Cursor selectPreview(String NoSaluran) throws SQLException
	{
		String command = "SELECT IdentName, IdentAddress, MeterOld, routetrack, IdentNumberSal || '/' || gol as nosal FROM metermobile WHERE IdentNumberSal = '"+NoSaluran+"'";
	    return mDataBase.rawQuery(command, null);
	}
	public Cursor selectLogin(String Username) throws SQLException
	{
		String command = "SELECT password FROM user WHERE username = '"+Username+"'";
	    return mDataBase.rawQuery(command, null);
	}
	public Cursor selectDetail(String NoSaluran) throws SQLException
	{
		String command = "SELECT Ids, DateImport, IdentNumberRek, MeterOld, PeriodMonth, IdentName, IdentAddress, routetrack FROM metermobile WHERE IdentNumberSal = '"+NoSaluran+"'";
		return mDataBase.rawQuery(command, null);
	}
	public Cursor selectListSaluran() throws SQLException
	{
		String command = "SELECT IdentNumberSal, IdentNumberSal || '-' || IdentName as Alamat FROM metermobile WHERE status = '0' ORDER BY IdentAddress, routetrack ASC";
	    return mDataBase.rawQuery(command, null);
	}
	
	 public List<String> getAllLabels(){
	        List<String> labels = new ArrayList<String>();
	 

	        String selectQuery = "SELECT idstatus, status FROM status";
	 
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        if (cursor.moveToFirst()) {
	            do {
	                labels.add(cursor.getString(1));
	            } while (cursor.moveToNext());
	        }
	 
	        cursor.close();
	        db.close();
	 
	        return labels;
	    }
}