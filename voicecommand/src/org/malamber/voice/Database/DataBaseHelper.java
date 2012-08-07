package org.malamber.voice.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.malamber.logging.L;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/org.malamber.clipboard/databases/"; 
    private static String DB_NAME = "clipboard.db"; 
    
    private static String TABLE_HISTORY = "history";   
    
    private static String COL_PACKAGE = "packagename";
    
    private static String COL_ID = "ID";
    
    
    
    private SQLiteDatabase db;  
    private final Context myContext;

    public DataBaseHelper(Context context) 
    {    	
    	super(context, DB_NAME, null, 1);
    	
    	L.Tag="clipboard";
        this.myContext = context;
    }	
 
  
    
    public void deleteAll()
    {
    	int i = db.delete(TABLE_HISTORY, null, null);
    	L.d(this, i + " clips deleted");
    }
    
    public boolean insertFavorite(String s)
    {
    	try {
			ContentValues cv = new ContentValues();
			cv.put(COL_PACKAGE, s);
			db.insert(TABLE_HISTORY, null, cv);
			return true;
		} catch (SQLException e) {
			
		}
    	
    	return false;
    }
    public ArrayList<String> getFavorites()
    {
    	ArrayList<String> ar = new ArrayList<String>();
    	try {    		
			
    		String[] columns = new String[]{COL_ID, COL_PACKAGE };
			Cursor c;
			
			c = db.query(TABLE_HISTORY, 
					columns,null,null,null,null,null);			
			
			while(c.moveToNext())							
				ar.add(c.getString(c.getColumnIndex(COL_PACKAGE)));
			
			c.close();
			
		} catch (Exception e) {
			// 
			L.ex(this, "",e);
		}
    	return ar;
    }
    
    
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase()
    {
    	//L.d(this, "checkDataBase");
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null) 
    		checkDB.close();
    	
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException
    {
    	//L.d(this, "copyDataBase");
 
    	try {
			//Open your local db as the input stream
			InputStream myInput = myContext.getAssets().open(DB_NAME);
 
			// Path to the just created empty db
			String outFileName = DB_PATH + DB_NAME; 
			//Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);
 
			//transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
			}
 
			//Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			
			L.ex(this, "",e);
		} 
    }
    public static void backupDB()
    {
    	try {
			//Open your local db as the input stream
			InputStream myInput = new FileInputStream(DB_PATH+ DB_NAME);
 
			// Path to the just created empty db
			String outFileName = "/mnt/sdcard/" + DB_NAME; 
			//Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);
 
			//transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
			}
 
			//Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			
			//L.ex(this, "",e);
		} 
    }
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	//L.d(this, "createDataBase");
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	} 
    }
    public static DataBaseHelper createDatabase(Context c)
    {
    	DataBaseHelper db = new DataBaseHelper(c);       
 
        try {
 
        	db.createDataBase();
 
	 	} catch (IOException ioe) {	 
	 		L.ex(c, "Unable to create database",ioe);	 
	 	}
	 
	 	try {	 
	 		db.openDataBase();
	 
	 	}catch(SQLException sqle){
	 		L.ex(c,"SQLException",sqle);	
	 	}
	 	
	 	return db;
    }
    public void openDataBase() throws SQLException
    {    	
    	//L.d(this, "openDataBase");
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE); 
    }
 
    @Override
	public synchronized void close() {
    	if(db != null)
    		db.close();
 
    	super.close(); 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
}
