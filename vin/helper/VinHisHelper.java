package com.gwkj.qixiubaodian.module.vin.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VinHisHelper extends SQLiteOpenHelper {

	private static final String name = "vinhis";
	private static final int version = 1;
	private static VinHisHelper instance=null;
	public static VinHisHelper getInstance(Context context){
		if(instance==null){
			instance=new VinHisHelper(context);
		}
		return instance;
	}
	public VinHisHelper(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("info", "create table");
		db.execSQL("CREATE TABLE IF NOT EXISTS vin (id integer primary key autoincrement, name varchar(40), date INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
