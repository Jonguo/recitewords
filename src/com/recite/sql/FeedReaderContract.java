package com.recite.sql;

import android.provider.BaseColumns;

public class FeedReaderContract
{
	private FeedReaderContract(){}
	
	public static abstract class AllUnitName implements BaseColumns
	{
		public static String TABLE_NAME = "AllUnitName";
		public static String COLUMN_UNIT_NAME = "unitName";
		public static String GRADE = "Grade";
	}
	
	public static abstract class ImportedBookName implements BaseColumns
	{
		public static String TABLE_NAME = "ImportedBookName";
		public static String COLUMN_BOOK_NAME = "BookName";
		public static String GRADE = "Grade";
	}
	
	
	public static abstract class Notebook implements BaseColumns
	{
		public static String TABLE_NAME = "Notebook";
		public static String COLUMN_WORD = "word";
		public static String COLUMN_EXPLAIN = "explain";
		public static String COLUMN_SOUNDMARK = "soundmark";
		public static String COLUMN_EMP3S = "emp3s";
		public static String COLUMN_EMP3L = "emp3l";
		public static String COLUMN_CMP3S = "cmp3s";
		public static String COLUMN_CMP3L = "cmp3l";
		public static String COLUMN_BOOKNAME = "bookname";
	}
	
	public static abstract class Aibing implements BaseColumns
	{
		public static String TABLE_NAME = "AibingReview";
		public static String COLUMN_WORD = "word";
		public static String COLUMN_EXPLAIN = "explain";
		public static String COLUMN_SOUNDMARK = "soundmark";
		public static String COLUMN_EMP3S = "emp3s";
		public static String COLUMN_EMP3L = "emp3l";
		public static String COLUMN_CMP3S = "cmp3s";
		public static String COLUMN_CMP3L = "cmp3l";
		public static String COLUMN_BOOKNAME = "bookname";
		public static String COLUMN_REVIEW_TIMES = "reviewTimes";
		public static String COLUMN_NEXT_REVIEW_TIME = "nextTimes";
		public static String GRADE = "Grade";
	}
	
}
