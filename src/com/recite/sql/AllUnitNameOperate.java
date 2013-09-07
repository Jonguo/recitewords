package com.recite.sql;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.recite.assist.Values;

public class AllUnitNameOperate
{
	private Context context;
	private static AllUnitNameOperate instance;
	public AllUnitNameOperate(Context context)
	{
		this.context = context;
	}
	
	public static AllUnitNameOperate getInstance(Context context)
	{
		if(instance == null) 
			instance = new AllUnitNameOperate(context);
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
				values.put(FeedReaderContract.AllUnitName.COLUMN_UNIT_NAME,
						strings.get(i));
				values.put(FeedReaderContract.AllUnitName.GRADE, String.valueOf(grade));
				db.insert(FeedReaderContract.AllUnitName.TABLE_NAME, null,
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
		List<String> allUnitList =  new ArrayList<String>();
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
//		String whereClause = FeedReaderContract.Notebook.COLUMN_WORD + " = ? AND "
//				+ FeedReaderContract.Notebook.COLUMN_EMP3S +  " = ? ";
//		String[] whereArgs = {word.name ,String.valueOf(word.enStart)};
//		db.delete(FeedReaderContract.Notebook.TABLE_NAME, whereClause, whereArgs);		
		
		String whereClause = FeedReaderContract.AllUnitName.GRADE + "=?";
		String[] whereArgs = {String.valueOf(grade)};
		
		Cursor cursor = db.query(FeedReaderContract.AllUnitName.TABLE_NAME,new String[]{FeedReaderContract.AllUnitName.COLUMN_UNIT_NAME} , whereClause, whereArgs, null, null, null);
		while(cursor.moveToNext())
		{
			String name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.AllUnitName.COLUMN_UNIT_NAME));
			try
			{
				name = new String(name.getBytes(),"UTF-8");
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}			
			allUnitList.add(name);
			System.out.println(name);
		}
		
		return allUnitList;
	}
	
}
