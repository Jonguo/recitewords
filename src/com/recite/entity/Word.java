package com.recite.entity;

import java.util.Date;

import com.recite.assist.Values;

public class Word
{
	public String  name = null;   // 英文拼写
	public String  explain = null; // 中文
	public String  soundmark = null; // 音标
	public String  bookName = null;//书名
	public int 	   enStart = -1,  //英文MP3起始位置
				   enlen = 0, //英文MP3长度
				   chStart = -1,  // 中文MP3起始
				   chlen = 0;//中文MP3长度
	
	public int reviewTimes = 0;//现在已经复习了几次
	public long nextReviewTime = 0;
	
	public Word ()
	{}
	public Word(String name,String soundmark,String explain,String pos)
	{
		this.name = name;
		this.explain = explain;
		if(soundmark.equals("#") ) this.soundmark = "";
		else 
		this.soundmark = soundmark;
		setMp3Position(pos);
	}
	
	public void setMp3Position(String s)
	{
		String pos[] = s.split(" ");
		assert pos.length == 4;
		enStart = Integer.parseInt(pos[0]);
		enlen = Integer.parseInt(pos[1]);
		chStart = Integer.parseInt(pos[2]);
		chlen = Integer.parseInt(pos[3]);
	}
	
	public boolean setReviewTime(long rememberTime)
	{
		if(reviewTimes >= Values.myTimes.size()) return false;
		MyTime myTime = Values.myTimes.get(reviewTimes);
		nextReviewTime =  rememberTime + myTime.getMillisecond();
		reviewTimes++;
		return true;
	}
	
	public boolean setReviewTime()
	{
		long rememberTime = new Date().getTime();
		if(reviewTimes >= Values.myTimes.size()) return false;
		MyTime myTime = Values.myTimes.get(reviewTimes);
		nextReviewTime =  rememberTime + myTime.getMillisecond();
		reviewTimes++;
		return true;
	}
	
	public boolean timeToReview()
	{
		long time = new Date().getTime();
		if(nextReviewTime <= time)
		{
			return true;
		}
		return false;
	}
	
	public void resetTime()
	{
		reviewTimes =  0;
		setReviewTime();
	}
	
	public boolean equals(Word w)
	{
		return name.equals(w.name)&&bookName.equals(w.bookName)&&enStart == w.enStart;
	}
	
}
