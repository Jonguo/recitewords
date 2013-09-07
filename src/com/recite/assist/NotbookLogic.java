package com.recite.assist;

import java.util.List;

import com.recite.entity.Word;

public class NotbookLogic
{
	private static NotbookLogic instance = null;
	private NotbookLogic(){};
	
	public static NotbookLogic getInstance()
	{
		if(instance == null)
		{
			synchronized (NotbookLogic.class)
			{
				if(instance == null)
				{
					instance = new NotbookLogic();
				}
			}
		}
		return instance;
	}
	
	public boolean addToNotbook(Word word)
	{
		if(wordIsAdd(word))
			return false;
		Values.newnotebookwords.add(word);
		return true;
	}

	public boolean wordIsAdd(Word word)
	{
		List<Word> newWords = Values.newnotebookwords;
		List<Word> oldWords = Values.oldnotebookWords;
		for(int i=0;i<newWords.size();++i)
		{
			Word word1 = newWords.get(i);
			if(word1.name.endsWith(word.name)&&word1.enlen == word.enlen)
			{
				return true;
			}
		}
		for(int i=0;i<oldWords.size();++i)
		{
			Word word1 = oldWords.get(i);
			if(word1.name.endsWith(word.name)&&word1.enlen == word.enlen)
			{
				return true;
			}
		}
		return false;
	}	
	
}
