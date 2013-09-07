package com.recite.sql;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.recite.assist.Values;
import com.recite.entity.Word;

public class NotebookOperate
{
	public static Context context;
	private NotebookOperate()
	{
	}
	public NotebookOperate(Context context)
	{
		this.context = context;
	}	
	public static void insert(List<Word> words)
	{
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.beginTransaction();
		try
		{
			for(int i=0;i<words.size();++i)
			{
				Word word = words.get(i);
				ContentValues values = new ContentValues();
				values.put(FeedReaderContract.Notebook.COLUMN_WORD, word.name);
				values.put(FeedReaderContract.Notebook.COLUMN_SOUNDMARK, word.soundmark);
				values.put(FeedReaderContract.Notebook.COLUMN_EXPLAIN, word.explain);
				values.put(FeedReaderContract.Notebook.COLUMN_EMP3S, word.enStart);
				values.put(FeedReaderContract.Notebook.COLUMN_EMP3L, word.enlen);
				values.put(FeedReaderContract.Notebook.COLUMN_CMP3S, word.chStart);
				values.put(FeedReaderContract.Notebook.COLUMN_CMP3L, word.chlen);
				values.put(FeedReaderContract.Notebook.COLUMN_BOOKNAME, word.bookName);
				db.insert(FeedReaderContract.Notebook.TABLE_NAME, null, values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally
		{
			db.endTransaction();
		}
	}

	public static List<Word> query()
	{
		List<Word> words = new LinkedList<Word>();
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		String[] columns = {FeedReaderContract.Notebook.COLUMN_WORD,
				FeedReaderContract.Notebook.COLUMN_SOUNDMARK,
				FeedReaderContract.Notebook.COLUMN_EXPLAIN,
				FeedReaderContract.Notebook.COLUMN_EMP3S,
				FeedReaderContract.Notebook.COLUMN_EMP3L,
				FeedReaderContract.Notebook.COLUMN_CMP3S,
				FeedReaderContract.Notebook.COLUMN_CMP3L,
				FeedReaderContract.Notebook.COLUMN_BOOKNAME};
		Cursor cursor = db.query(FeedReaderContract.Notebook.TABLE_NAME, columns, null, null, null, null, null);
		while(cursor.moveToNext())
		{
			Word word = new Word();
			word.name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_WORD));
			word.soundmark = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_SOUNDMARK));
			word.explain = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_EXPLAIN));
			word.enStart = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_EMP3S));
			word.enlen = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_EMP3L));
			word.chStart = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_CMP3S));
			word.chlen = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_CMP3L));
			word.bookName = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Notebook.COLUMN_BOOKNAME));
			try
			{
				word.name = new String(word.name.getBytes(),"UTF-8");
				word.soundmark = new String(word.soundmark.getBytes(),"UTF-8");
				word.explain = new String(word.explain.getBytes(),"UTF-8");
				word.bookName = new String(word.bookName.getBytes(),"UTF-8");
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			words.add(word);
		}
		return words;
	}
	
	public static void delete(List<Word> words)
	{
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.beginTransaction();
		try
		{
			for(int i=0;i<words.size();++i)
			{
				Word word = words.get(i);
				String whereClause = FeedReaderContract.Notebook.COLUMN_WORD + " = ? AND "
						+ FeedReaderContract.Notebook.COLUMN_EMP3S +  " = ? ";
				String[] whereArgs = {word.name ,String.valueOf(word.enStart)};
				db.delete(FeedReaderContract.Notebook.TABLE_NAME, whereClause, whereArgs);
			}
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally
		{
			db.endTransaction();
		}
	}
}
