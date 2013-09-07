package com.recite.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MYSQLOpenHelper extends SQLiteOpenHelper
{
	private static final int DEFAULTVERSION = 1;
	private static String DATABASENAME ="" ;
	
	private static final String SQL_CREATE_AllUnitName = 
			" CREATE TABLE if not exists " + FeedReaderContract.AllUnitName.TABLE_NAME + "("
	        +FeedReaderContract.AllUnitName.COLUMN_UNIT_NAME + " TEXT, "
					+FeedReaderContract.AllUnitName.GRADE + " INTEGER,"
					+"constraint pk1 primary key ("+FeedReaderContract.AllUnitName.COLUMN_UNIT_NAME
					+","+FeedReaderContract.AllUnitName.GRADE +") )";

	private static final String SQL_CREATE_ImportedBOOKName = 
			" CREATE TABLE if not exists " + FeedReaderContract.ImportedBookName.TABLE_NAME + "("
	        +FeedReaderContract.ImportedBookName.COLUMN_BOOK_NAME + " TEXT, "
					+FeedReaderContract.ImportedBookName.GRADE + " INTEGER,"
					+"constraint pk1 primary key ("+FeedReaderContract.ImportedBookName.COLUMN_BOOK_NAME
					+","+FeedReaderContract.ImportedBookName.GRADE +") )";	
	
	
	private static final String SQL_CREATE_Notebook = 
			" CREATE TABLE if not exists " +FeedReaderContract.Notebook.TABLE_NAME + "(" 
			+FeedReaderContract.Notebook.COLUMN_WORD + " TEXT," 
			+FeedReaderContract.Notebook.COLUMN_SOUNDMARK + " TEXT,"
			+FeedReaderContract.Notebook.COLUMN_EXPLAIN + " TEXT,"
			+FeedReaderContract.Notebook.COLUMN_EMP3S + " INTEGER,"
			+FeedReaderContract.Notebook.COLUMN_EMP3L + " INTEGER,"
			+FeedReaderContract.Notebook.COLUMN_CMP3S + " INTEGER,"
			+FeedReaderContract.Notebook.COLUMN_CMP3L + " INTEGER,"
			+FeedReaderContract.Notebook.COLUMN_BOOKNAME + " TEXT)";
	
	private static final String SQL_CREATE_Aibing = 
			" CREATE TABLE if not exists " + FeedReaderContract.Aibing.TABLE_NAME + "("
			+FeedReaderContract.Aibing.COLUMN_WORD + " TEXT," 
			+FeedReaderContract.Aibing.COLUMN_SOUNDMARK + " TEXT,"
			+FeedReaderContract.Aibing.COLUMN_EXPLAIN + " TEXT,"
			+FeedReaderContract.Aibing.COLUMN_EMP3S + " INTEGER,"
			+FeedReaderContract.Aibing.COLUMN_EMP3L + " INTEGER,"
			+FeedReaderContract.Aibing.COLUMN_CMP3S + " INTEGER,"
			+FeedReaderContract.Aibing.COLUMN_CMP3L + " INTEGER,"
			+FeedReaderContract.Aibing.COLUMN_BOOKNAME + " TEXT,"
			+FeedReaderContract.Aibing.COLUMN_REVIEW_TIMES + " INTEGER,"
			+FeedReaderContract.Aibing.COLUMN_NEXT_REVIEW_TIME + " INTEGER,"
			+"primary key("+FeedReaderContract.Aibing.COLUMN_WORD+","
			+FeedReaderContract.Aibing.COLUMN_EMP3L+") )";
	
	
	public MYSQLOpenHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
		DATABASENAME = name;
	}
	
	public MYSQLOpenHelper(Context context,String name)
	{
		this(context, name,null,DEFAULTVERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE_AllUnitName);
		db.execSQL(SQL_CREATE_Notebook);
		db.execSQL(SQL_CREATE_Aibing);
		db.execSQL(SQL_CREATE_ImportedBOOKName);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}

}
