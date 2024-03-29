package com.appdimension;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	
	public static final String KEY_ROWID="_id";
	public static final String KEY_LATITUD="latitud";
	public static final String KEY_LONGITUD="longitud";
	public static final String KEY_FECHA="fecha";
	private static final String TAG="DBAdapter";
	
	//private static final String DATABASE_NAME="MyDBANDROID";
	private static final String DATABASE_NAME="GPSdatosMovilizacionAsistida";
	private static final String DATABASE_TABLE="datos";
	
	//actualizando la base de datos
	private static final int DATABASE_VERSION=2;
	//private static final int DATABASE_VERSION=2;
	
	private static final String DATABASE_CREATE="create table datos (_id integer primary key autoincrement, "
		+"latitud text not null, longitud text not null, fecha text not null);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx)
	{
		this.context=ctx;
		DBHelper=new DatabaseHelper(context);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper(Context context) {
			// TODO Auto-generated constructor stub
			super(context, DATABASE_NAME,null,DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try{
				db.execSQL(DATABASE_CREATE);
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version "+oldVersion+" to "
					+newVersion+" , which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS datos");
			onCreate(db);
		}
	}
	
	public DBAdapter open() throws SQLException
	{
		db=DBHelper.getWritableDatabase();
		return this;
	}
	public void close()
	{
		DBHelper.close();
	}
	public long insertContact(String latitud, String longitud, String fecha)
	{
		ContentValues initialValues=new ContentValues();
		initialValues.put(KEY_LATITUD, latitud);
		initialValues.put(KEY_LONGITUD, longitud);
		initialValues.put(KEY_FECHA, fecha);
		
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	public boolean deleteContact(long rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ROWID+"="+rowId, null)>0;
	}
	public Cursor getAllContacts()
	{
		//db.query
		return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,KEY_LATITUD,KEY_LONGITUD,KEY_FECHA}, null, null, null, null, null);
	}
	
	//public Cursor getAllContactsWhere(String fecha)throws SQLException
	public Cursor getAllContactsWhere(String fecha)
	{
		//return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,KEY_LATITUD,KEY_LONGITUD,KEY_FECHA}, null, null, null, null, null);
		
		//return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,KEY_LATITUD,KEY_LONGITUD,KEY_FECHA}, KEY_FECHA+"="+fecha, null, null, null, null, null);
		return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,KEY_LATITUD,KEY_LONGITUD,KEY_FECHA}, KEY_FECHA+"=?", new String[]{fecha}, null, null, null, null);
		
		//Log.d("xxx","pair:" + "fecha="+fecha);
		/*
		Cursor mCursor=
			db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,KEY_LATITUD,KEY_LONGITUD,KEY_FECHA}, "fecha="+fecha, null, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
		*/
		//return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,KEY_LATITUD,KEY_LONGITUD,KEY_FECHA},KEY_FECHA+"="+fecha, null, null, null, null);
	}
	public Cursor getContact(long rowId)throws SQLException
	{
		Cursor mCursor=
			db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
					KEY_LATITUD,KEY_LONGITUD,KEY_FECHA}, KEY_ROWID+"="+rowId, null,
					null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public boolean updateContact(long rowId, String latitud, String longitud, String fecha)
	{
		ContentValues args=new ContentValues();
		args.put(KEY_LATITUD, latitud);
		args.put(KEY_LONGITUD, longitud);
		args.put(KEY_FECHA, fecha);
		
		return db.update(DATABASE_TABLE, args, KEY_ROWID+"="+rowId, null)>0;
	}

}
