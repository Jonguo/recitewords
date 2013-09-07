package com.recite.sql;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.recite.assist.Values;

public class ImportedBookOperate
{
	private Context context;
	private static ImportedBookOperate instance;
	public ImportedBookOperate(Context context)
	{
		this.context = context;
	}
	
	public static ImportedBookOperate getInstance(Context context)
	{
		if(instance == null) 
			synchronized (ImportedBookOperate.class)
			{
				if(instance==null)
					instance = new ImportedBookOperate(context);
			}
		return instance;
	}
	
	public void insert(List<String> strings,int grade)
	{
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context,
				Values.userNameString);
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.beginTransaction();
		try
		{
			for (int i = 0; i < strings.size(); ++i)
			{
				ContentValues values = new ContentValues();
				values.put(FeedReaderContract.ImportedBookName.COLUMN_BOOK_NAME,
						strings.get(i));
				values.put(FeedReaderContract.ImportedBookName.GRADE, String.valueOf(grade));
				db.insert(FeedReaderContract.ImportedBookName.TABLE_NAME, null,
						values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			db.endTransaction();
		}
		
	}
	public List<String> query(int grade)
	{
		List<String> List =  new ArrayList<String>();
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
//		String whereClause = FeedReaderContract.Notebook.COLUMN_WORD + " = ? AND "
//				+ FeedReaderContract.Notebook.COLUMN_EMP3S +  " = ? ";
//		String[] whereArgs = {word.name ,String.valueOf(word.enStart)};
//		db.delete(FeedReaderContract.Notebook.TABLE_NAME, whereClause, whereArgs);		
		
		String whereClause = FeedReaderContract.ImportedBookName.GRADE + "=?";
		String[] whereArgs = {String.valueOf(grade)};
		
		Cursor cursor = db.query(FeedReaderContract.ImportedBookName.TABLE_NAME,new String[]{FeedReaderContract.ImportedBookName.COLUMN_BOOK_NAME} , whereClause, whereArgs, null, null, null);
		while(cursor.moveToNext())
		{
			String name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.ImportedBookName.COLUMN_BOOK_NAME));
			try
			{
				name = new String(name.getBytes(),"UTF-8");
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}			
			List.add(name);
			System.out.println(name);
		}
		
		return List;
	}
	
}
