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

public class AibingOperate
{
	private  Context context;
	private  AibingOperate instance;
	private AibingOperate(Context context)
	{
		this.context = context;
	}
	
	private AibingOperate(){}
	
	public static AibingOperate getInstance(Context context)
	{

		return new AibingOperate(context);
	}
	
	public void insert(List<Word> words)
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
				values.put(FeedReaderContract.Aibing.COLUMN_WORD, word.name);
				values.put(FeedReaderContract.Aibing.COLUMN_SOUNDMARK, word.soundmark);
				values.put(FeedReaderContract.Aibing.COLUMN_EXPLAIN, word.explain);
				values.put(FeedReaderContract.Aibing.COLUMN_EMP3S, word.enStart);
				values.put(FeedReaderContract.Aibing.COLUMN_EMP3L, word.enlen);
				values.put(FeedReaderContract.Aibing.COLUMN_CMP3S, word.chStart);
				values.put(FeedReaderContract.Aibing.COLUMN_CMP3L, word.chlen);
				values.put(FeedReaderContract.Aibing.COLUMN_BOOKNAME, word.bookName);
				values.put(FeedReaderContract.Aibing.COLUMN_REVIEW_TIMES, word.reviewTimes);
				values.put(FeedReaderContract.Aibing.COLUMN_NEXT_REVIEW_TIME, word.nextReviewTime);
				//values.put(FeedReaderContract.Aibing.GRADE, String.valueOf(grade));
				db.insert(FeedReaderContract.Aibing.TABLE_NAME, null, values);
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
	
	public List<Word> query()
	{
		List<Word> words = new LinkedList<Word>();
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		String[] columns = {FeedReaderContract.Aibing.COLUMN_WORD,
				FeedReaderContract.Aibing.COLUMN_SOUNDMARK,
				FeedReaderContract.Aibing.COLUMN_EXPLAIN,
				FeedReaderContract.Aibing.COLUMN_EMP3S,
				FeedReaderContract.Aibing.COLUMN_EMP3L,
				FeedReaderContract.Aibing.COLUMN_CMP3S,
				FeedReaderContract.Aibing.COLUMN_CMP3L,
				FeedReaderContract.Aibing.COLUMN_BOOKNAME,
				FeedReaderContract.Aibing.COLUMN_REVIEW_TIMES,
				FeedReaderContract.Aibing.COLUMN_NEXT_REVIEW_TIME};
		
		String orderByString = FeedReaderContract.Aibing.COLUMN_NEXT_REVIEW_TIME;
		
		Cursor cursor = db.query(FeedReaderContract.Aibing.TABLE_NAME, columns, null, null, null, null, orderByString);
		while(cursor.moveToNext())
		{
			Word word = new Word();
			word.name = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_WORD));
			word.soundmark = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_SOUNDMARK));
			word.explain = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_EXPLAIN));
			word.enStart = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_EMP3S));
			word.enlen = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_EMP3L));
			word.chStart = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_CMP3S));
			word.chlen = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_CMP3L));
			word.bookName = cursor.getString(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_BOOKNAME));
			word.reviewTimes = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_REVIEW_TIMES));
			word.nextReviewTime = cursor.getLong(cursor.getColumnIndex(FeedReaderContract.Aibing.COLUMN_NEXT_REVIEW_TIME));
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
	
	public void delete(List<Word> words)
	{
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.beginTransaction();
		try
		{
			for(int i=0;i<words.size();++i)
			{
				Word word = words.get(i);
				String whereClause = FeedReaderContract.Aibing.COLUMN_WORD + " = ? AND "
						+ FeedReaderContract.Aibing.COLUMN_EMP3L +  " = ? ";
				String[] whereArgs = {word.name ,String.valueOf(word.enlen)};
				db.delete(FeedReaderContract.Aibing.TABLE_NAME, whereClause, whereArgs);
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
	
	public void update(List<Word> words)
	{
		MYSQLOpenHelper sqlHelper = new MYSQLOpenHelper(context, Values.userNameString);
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		db.beginTransaction(); 
		try
		{
			for(int i=0;i<words.size();++i)
			{
				Word word = words.get(i);
				String whereClause = FeedReaderContract.Aibing.COLUMN_WORD + " = ? AND "
						+ FeedReaderContract.Aibing.COLUMN_EMP3S +  " = ? ";
				String[] whereArgs = {word.name ,String.valueOf(word.enStart)};
				ContentValues cv = new ContentValues();
				cv.put(FeedReaderContract.Aibing.COLUMN_NEXT_REVIEW_TIME, word.nextReviewTime);
				cv.put(FeedReaderContract.Aibing.COLUMN_REVIEW_TIMES, word.reviewTimes);
				db.update(FeedReaderContract.Aibing.TABLE_NAME, cv, whereClause, whereArgs);
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
