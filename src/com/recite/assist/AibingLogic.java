package com.recite.assist;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import android.content.Context;

import com.recite.entity.Word;
import com.recite.sql.AibingOperate;

public class AibingLogic
{
	private AibingOperate aibingOperate  = null;
	
	private TimerTask task;
	
	
	private AibingLogic(Context context)
	{
		aibingOperate = AibingOperate.getInstance(context);
		init();
		task = new TimerTask()
		{
			
			@Override
			public void run()
			{
				if(Values.aibingOpen)
				addWordstoQueue();
			}
		};
	}
	

	public static AibingLogic getInstance(Context context)
	{
		return new AibingLogic(context);		
	}
	
	public void addWordstoQueue()
	{

		for(int i=0;i<Values.aibingOldWords.size();i++)
		{
			Word w = Values.aibingOldWords.get(i);
			if(Values.aibingQueueWords.size()>20)
				return;
			if(w.timeToReview())
			{
				Values.aibingQueueWords.add(w);
			}
		}
		
		for(int i=0;i<Values.aibingUpdatedWords.size();i++)
		{
			Word w = Values.aibingUpdatedWords.get(i);
			if(Values.aibingQueueWords.size()>20)
				return;
			if(w.timeToReview())
			{
				Values.aibingQueueWords.add(w);
			}
		}
		
		for(int i=0;i<Values.aibingInsertWords.size();i++)
		{
			Word w = Values.aibingInsertWords.get(i);
			if(Values.aibingQueueWords.size()>20)
				return;
			if(w.timeToReview())
			{
				Values.aibingQueueWords.add(w);
			}
		}
		
	}
	
	public TimerTask getTask()
	{
		return task;
	}
	
	private void init()
	{
		loadOldWord();
	}
	
	private void loadOldWord()
	{
		
		Values.aibingOldWords = aibingOperate.query();
	}
	
	
	public void rememberListener()
	{
		if(Values.aibingOpen == false)
		{
			Values.indexFileWords = (Values.indexFileWords+1)%Values.fileWords.size();
			Values.typeCurrenct = Values.typeFileWords;
			Values.currentWord = Values.fileWords.get(Values.indexFileWords);
			return ;
		}
		removeExistsFromQueue();
		switch (Values.typeCurrenct)
		{
		case Values.typeFileWords:
			dealFileWords();
			break;
		case Values.typeQueueWords:
			 dealQueue();
			break;
		case Values.typeNotRememberWords:
			 dealNotReWords();
			break;
		}
		if(Values.aibingNotRememberWords.size()>0)
			Values.randCount --;
		Values.currentWord = getNextWord();
	}
	
	public void notRememberlistener()
	{
		if(Values.aibingOpen == false)
		{
			Values.indexFileWords = (Values.indexFileWords+1)%Values.fileWords.size();
			Values.typeCurrenct = Values.typeFileWords;
			Values.currentWord = Values.fileWords.get(Values.indexFileWords);
			return ;
		}
		removeExistsFromQueue();
		Values.aibingNotRememberWords.add(Values.currentWord);
		Values.currentWord = getNextWord();		
	}
	
	
	private Word getNextWord()
	{
		Word word = null;
		if(Values.aibingNotRememberWords.size()>0&&Values.randCount<=0||Values.aibingNotRememberWords.size()>4)
		{
			word = Values.aibingNotRememberWords.get(0);
			Values.aibingNotRememberWords.remove(0);
			Values.typeCurrenct = Values.typeNotRememberWords;
			Values.randCount = new Random().nextInt(4)+1;
		}
		else if(!Values.aibingQueueWords.isEmpty())
		{
			word = Values.aibingQueueWords.get(0);
			Values.aibingQueueWords.remove(0);
			Values.typeCurrenct = Values.typeQueueWords;
		}
		else 
		{
			Values.indexFileWords = (Values.indexFileWords+1)%Values.fileWords.size();
			Values.typeCurrenct = Values.typeFileWords;
			word = Values.fileWords.get(Values.indexFileWords);
		}
		return word;
		
	}

	private void removeExistsFromQueue()
	{
		Word word = Values.currentWord;
		for(int i=0;i<Values.aibingQueueWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingQueueWords.get(i).name)
					&& word.enlen == Values.aibingQueueWords.get(i).enlen)
			{
				Values.aibingQueueWords.remove(i);
			}
		}
	}
	
	private void dealFileWords()
	{
		Word word = Values.currentWord;
		for(int i=0;i<Values.aibingOldWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingOldWords.get(i).name)
					&& word.enlen == Values.aibingOldWords.get(i).enlen)
			{
				Word w = Values.aibingOldWords.get(i);
				if(w.timeToReview())
				{
					Values.aibingOldWords.remove(i);
					if(w.setReviewTime())
					{
						insert(w, Values.aibingUpdatedWords);
					}
					else
					{
						Values.aibingDeleteWords.add(w);
					}
				}
				return ;
			}			
		}
		
		for(int i=0;i<Values.aibingUpdatedWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingUpdatedWords.get(i).name)
					&& word.enlen == Values.aibingUpdatedWords.get(i).enlen)
			{
				Word w = Values.aibingUpdatedWords.get(i);
				if(w.timeToReview())
				{
					if(!w.setReviewTime())
					{
						insert(w, Values.aibingDeleteWords);
						Values.aibingUpdatedWords.remove(i);
					}
				}
				return ;
			}
		}
		for(int i=0;i<Values.aibingInsertWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingInsertWords.get(i).name)
					&& word.enlen == Values.aibingInsertWords.get(i).enlen)
			{
				Word w = Values.aibingInsertWords.get(i);
				if(w.timeToReview())
				{
					if(!w.setReviewTime())
					{
						Values.aibingDeleteWords.add(w);
						Values.aibingInsertWords.remove(i);						
					}
				}
				return ;
			}
		}
		Values.currentWord.setReviewTime();
		insert(Values.currentWord,Values.aibingInsertWords);
		
	}
	
	private void dealQueue()
	{
		Word word = Values.currentWord;
		for(int i=0;i<Values.aibingOldWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingOldWords.get(i).name)
					&& word.enlen == Values.aibingOldWords.get(i).enlen)
			{
				Word w = Values.aibingOldWords.get(i);
					Values.aibingOldWords.remove(i);
					if(w.setReviewTime())
					{
						insert(w, Values.aibingUpdatedWords);
					}
					else
					{
						Values.aibingDeleteWords.add(w);
					}
				return ;
			}			
		}
		
		for(int i=0;i<Values.aibingUpdatedWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingUpdatedWords.get(i).name)
					&& word.enlen == Values.aibingUpdatedWords.get(i).enlen)
			{
				Word w = Values.aibingUpdatedWords.get(i);
					if(!w.setReviewTime())
					{
						insert(w, Values.aibingDeleteWords);
						Values.aibingUpdatedWords.remove(i);
					}
				return ;
			}
		}
		for(int i=0;i<Values.aibingInsertWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingInsertWords.get(i).name)
					&& word.enlen == Values.aibingInsertWords.get(i).enlen)
			{
				Word w = Values.aibingInsertWords.get(i);
					if(!w.setReviewTime())
					{
						Values.aibingDeleteWords.add(w);
						Values.aibingInsertWords.remove(i);						
					}
				return ;
			}
		}	
	}
	
	private void dealNotReWords()
	{
		Word word = Values.currentWord;
		for(int i=0;i<Values.aibingOldWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingOldWords.get(i).name)
					&& word.enlen == Values.aibingOldWords.get(i).enlen)
			{
				Word w = Values.aibingOldWords.get(i);
				Values.aibingOldWords.remove(i);
				insert(w, Values.aibingUpdatedWords);
				return ;
			}			
		}
		
		for(int i=0;i<Values.aibingUpdatedWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingUpdatedWords.get(i).name)
					&& word.enlen == Values.aibingUpdatedWords.get(i).enlen)
			{
				Word w = Values.aibingUpdatedWords.get(i);
				Values.aibingUpdatedWords.remove(i);
			    w.resetTime();
			    insert(w, Values.aibingUpdatedWords);
				return ;
			}
		}
		for(int i=0;i<Values.aibingInsertWords.size();++i)
		{
			if(word.name.endsWith(Values.aibingInsertWords.get(i).name)
					&& word.enlen == Values.aibingInsertWords.get(i).enlen)
			{
				Word w = Values.aibingInsertWords.get(i);
				Values.aibingInsertWords.remove(i);
			    w.resetTime();
			    insert(w, Values.aibingInsertWords);
				return ;
			}
		}
		Values.currentWord.resetTime();
		insert(Values.currentWord,Values.aibingInsertWords);		
	}
	
	private void updateQueue(Word word)
	{
		
	}
	
	private void insert(Word word,List<Word> list)
	{
		for(int i=0;i<list.size();++i)
		{
			if(word.nextReviewTime<list.get(i).reviewTimes)
			{
				list.add(i, word);
				return;
			}
		}
		list.add(word);
	}
}
