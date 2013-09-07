package com.recite.media;

import java.io.File;

import android.content.Context;
import android.media.MediaPlayer;

import com.recite.assist.FileOperate;
import com.recite.entity.Word;


public class PlayMp3
{
	
	private PlayMp3(){}
	public static PlayMp3 instance = null;
	
	private File mtempMp3File;
	private MediaPlayer mMediaPlayer01 = null;
	
	public static PlayMp3 getInstance()
	{
		if(instance == null)
		{
			synchronized (PlayMp3.class)
			{
				if(instance == null)
				{
					instance = new PlayMp3();
				}
			}
		}
		return instance;
	}
	
	public void PlayMp3(Context context,Word word, int flag,int grade) throws Exception
	{
		mtempMp3File = FileOperate.getInstance(context).createTempMp3(word, flag,grade);
		if(mMediaPlayer01 == null)
		{
			mMediaPlayer01 = new MediaPlayer();
			mediaPlayersetListener();
		}
		if(mMediaPlayer01.isPlaying() == true)
		{
			mMediaPlayer01.reset();
		}
		mMediaPlayer01.setDataSource(mtempMp3File.getPath());
		mMediaPlayer01.prepare();
		mMediaPlayer01.start();		
	}
	
    private void mediaPlayersetListener()
    {
        mMediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
		{
			
			@Override
			public void onCompletion(MediaPlayer mp)
			{
				mMediaPlayer01.release();
				mtempMp3File.delete();
				mMediaPlayer01 = null;
			}
		} );
    }
	
}
