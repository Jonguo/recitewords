package com.recite.assist;

public class GradePathControl
{
	private static GradePathControl instance = null;

	private GradePathControl()
	{
	}
	
	public static GradePathControl getInstance()
	{
		if(instance == null)
		{
			synchronized (NotbookLogic.class)
			{
				if(instance == null)
				{
					instance = new GradePathControl();
				}
			}
		}
		return instance;		
	}
	
	
	
	
	
}
