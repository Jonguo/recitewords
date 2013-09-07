package com.recite.entity;


public class MyTime
{
	public int number ;
	public int type;
	public  MyTime(int number,int type)
	{
		this.number = number;
		this.type = type;//1分钟,2小时,3天
	}
	public MyTime(){}
	
	public long getMillisecond()
	{
		switch (type)
		{
		case 1:
			return (long)number*60*1000;
		case 2:
			return (long)number*60*60*1000;
		default:
			return (long)number*24*60*60*1000;
		}
	}
}
