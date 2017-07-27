package pucit.edu.pk.fp;

import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class LoginDataBaseAdapter {

	static final String DATABASE_NAME = "login.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 4;

	static final String DATABASE_CREATE = "create table "+"LOGIN"+
			"( " +"ID integer primary key autoincrement,"+"Email text,"+ "PASSWORD  text,"+"REPASSWORD text,"+ "SECURITYHINT text) ";

	public  SQLiteDatabase db;
	private final Context context;
	private DataBaseHelper dbHelper;

	public  LoginDataBaseAdapter(Context _context) 
	{
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

	}
	public  LoginDataBaseAdapter open() throws SQLException 
	{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	public void close() 
	{
		db.close();
	}

	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}

	public void insertEntry(String email, String password,String repassword,String securityhint)
	{
		ContentValues newValues = new ContentValues();
		newValues.put("Email",email);
		newValues.put("PASSWORD", password);
		newValues.put("REPASSWORD",repassword);
		newValues.put("SECURITYHINT",securityhint);

		db.insert("LOGIN", null, newValues);
	}

	public int deleteEntry(String password)
	{
		String where="PASSWORD=?";
		int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{password}) ;
		return numberOFEntriesDeleted;
	}	

	public String getSinlgeEntryPassword(String password)
	{
		Cursor cursor=db.query("LOGIN", null, "Password=? ", new String[]{password}, null, null, null);
		if(cursor.getCount()<1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String pass = cursor.getString(cursor.getColumnIndex("Password"));

		cursor.close();
		return pass;

	}
	public String getSinlgeEntry(String email,String pass)
	{
		Cursor cursor=db.query("LOGIN", null, "Email=? and Password=?", new String[]{email, pass}, null, null, null);
		if(cursor.getCount()<1) // UserName Not Exist
		{
			cursor.close();
			return "NOT EXIST";
		}
		cursor.moveToFirst();
		String a = cursor.getString(cursor.getColumnIndex("Email"));

		cursor.close();
		return a;
	}

	public String getAllTags(String a) {


		Cursor c = db.rawQuery("SELECT * FROM " + "LOGIN" + " where SECURITYHINT = '" +a + "'" , null);
		String str = null;
		if (c.moveToFirst()) {
			do {
				str = c.getString(c.getColumnIndex("PASSWORD"));
			} while (c.moveToNext());
		}
		return str;
	}


	public void  updateEntry(String email,String password,String repassword)
	{
		ContentValues updatedValues = new ContentValues();
		updatedValues.put("Email",email);
		updatedValues.put("PASSWORD", password);
		updatedValues.put("REPASSWORD",repassword);
		updatedValues.put("SECURITYHINT",repassword);

		String where="USERNAME = ?";
		db.update("LOGIN",updatedValues, where, new String[]{password});			   
	}	



	public HashMap<String, String> getAnimalInfo(String id) {
		HashMap<String, String> wordList = new HashMap<String, String>();
		String selectQuery = "SELECT * FROM LOGIN where SECURITYHINT='"+id+"'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				wordList.put("PASSWORD", cursor.getString(1));
			} while (cursor.moveToNext());
		}				    
		return wordList;
	}	
}
