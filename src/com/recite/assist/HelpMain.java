package com.recite.assist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.xiaobawang.qita.geren.api.UserInfo;
import com.xiaobawang.qita.geren.api.UserInfoHelper;

public class HelpMain
{
	private Context context;
	private static HelpMain instance;
	
	private HelpMain(){};
	
	private HelpMain(Context context)
	{
		this.context = context;
	}
	
	public static HelpMain getInstance()
	{
		if(instance == null)
		{
			synchronized (HelpMain.class)
			{
				if(instance == null)
				{
					instance = new HelpMain();
				}
			}
		}
		return instance;
	}
	
	public List<String> mapUnitName(List<String> list,HashMap<String, String> map)
	{
		
		
		List<String> strings = new ArrayList<String>();
		if(list.size() == 0) return strings;
		for(int i=0;i<list.size();++i)
		{ 
			String s = list.get(i);
			String[] temp = s.split("\r\n");
			s = temp[0] + " " + temp[1];
			strings.add(s);
			map.put(s, temp[0] + "\r\n" + temp[2]);
		}
		return strings;
	}	
	public void init(Activity activity)
	{
		Values.init();
		setUserName(activity);
		setGrade(activity);
	}
	
	public void showToast(String string,Context context)
	{
		if(Values.toastOnlyOne!=null)
		     Values.toastOnlyOne.cancel();
		Values.toastOnlyOne = Toast.makeText(context, string, Toast.LENGTH_SHORT);
		Values.toastOnlyOne.show();
	}
	
	public void cancelToast()
	{
		if(Values.toastOnlyOne!=null)
		     Values.toastOnlyOne.cancel();		
	}
	
	public void setUserName(Activity activity)
	{
		UserInfoHelper myHelper = new UserInfoHelper(activity);
		UserInfo userInfo = myHelper.getCurrentUserInfo();
		if(userInfo !=null)
		{
			Values.userNameString = "sql"+userInfo.username;
		}
	}
	
	public void setGrade(Activity activity)
	{
		UserInfoHelper myHelper = new UserInfoHelper(activity);
		UserInfo userInfo = myHelper.getCurrentUserInfo();
		if(userInfo !=null)
		{
			Values.grade = userInfo.getGrade();//1-12 转成 0-5
			Values.grade -= 1;
			Values.grade = Values.grade%6;
		}
	}
	
}
