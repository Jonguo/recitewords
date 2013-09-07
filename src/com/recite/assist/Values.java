package com.recite.assist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.widget.Toast;

import com.recite.entity.MyTime;
import com.recite.entity.Word;



public class Values
{
	public static int  apkType = 2;//1表示 小学,2表示中学
	
	public static String userNameString = "defaultimpossiblenamecant";
	public static String gradeString = "小学三年级";
	
	public static int grade ;
	public static int currentGrade = 0;
	
	
	public static final String INTERNAL_SDCARD = "/mnt/sdcard/";
	public static final String EXTERNAL_SDCARD = "/mnt/external_sd/";
	
	public static String rootPathString = null;
	public static String[] mergeFilePaths_INTERNAL = new String[6];
	public static String[] mergeFilePaths_EXTERNAL = new String[6];
	public static String[] splitLexiconPaths = new String[6];
	
//	public static String splitLexiconPathString = INTERNAL_SDCARD + rootPathString + "/lexicon";
	public static String mergeFilePostfixString = ".gzc";
	
	public static List<Word> newnotebookwords = new LinkedList<Word>();
	public static List<Word> oldnotebookWords = null;
	public static List<Word> removedwords = new LinkedList<Word>();
	
	
	public static List<Word> fileWords = null;     //type 0
	
	public static List<MyTime> myTimes = null;
	
	public static String bookNameString = null;
	public static String lexiconNameString = null;
	public static String PREFS_NAME_STRING = "reciteWords";
	public static  int unitIndex = -1;
	
	public static boolean aibingOpen = true;
	public static List<Word> aibingOldWords = null;      //type 1
	public static List<Word> aibingInsertWords = new LinkedList<Word>();//type 2
	public static List<Word> aibingUpdatedWords = new LinkedList<Word>();//type 3
	public static List<Word> aibingNotRememberWords = new LinkedList<Word>();//type 4
	public static List<Word> aibingDeleteWords = new LinkedList<Word>();
	public static List<Word> aibingQueueWords =Collections.synchronizedList( new LinkedList<Word>() );
	
	public static Word currentWord = null;
	public static int indexFileWords = 0;
	public static int indexInsertWords = 0;
	public static int indexUpdateWords = 0;
	public static int indexOldWords = 0;
	public static int typeCurrenct = 0;
	
	public final static int typeFileWords = 0;
	public final static int typeOldWords = 1;
	public final static int typeInsertWords = 2;
	public final static int typeUpdateWords = 3;
	public final static int typeNotRememberWords = 4;
	public final static int typeQueueWords = 5;
	
	
	public static Toast toastOnlyOne = null;
	
	
	public static List<String> allUnitNameStrings = new ArrayList<String>();
	public static HashMap<String,String> allUnitNameMap = new HashMap<String, String>();
	
	public static int randCount = new Random().nextInt(4)+1;
	
	public static boolean NeedImportLexicon = false;
	
	public static void init()
	{
		
		switch (apkType)
		{
		case 1:
			 rootPathString = "xiaobawang/xiaoxue/yingyu/beidanci";
			break;

		case 2:
			 rootPathString = "xiaobawang/zhongxue/yingyu/beidanci";
			break;
		}
		
		
		
		for(int i=0;i<6;++i)
		{
			mergeFilePaths_EXTERNAL[i] = EXTERNAL_SDCARD + rootPathString + "/" + String.valueOf(i+1);
			mergeFilePaths_INTERNAL[i] = INTERNAL_SDCARD + rootPathString + "/" + String.valueOf(i+1);
			splitLexiconPaths[i] = INTERNAL_SDCARD + rootPathString + "/" + String.valueOf(i+1)+"/lexicon";
		}
		
		
		
		
		
		
		myTimes = new ArrayList<MyTime>();
		myTimes.add(new MyTime(5,1));
		myTimes.add(new MyTime(30,1));
		myTimes.add(new MyTime(12,2));
		myTimes.add(new MyTime(1,3));
		myTimes.add(new MyTime(2,3));
		myTimes.add(new MyTime(4,3));
		myTimes.add(new MyTime(7,3));
		myTimes.add(new MyTime(15,3));
		
	}
	
	
}
