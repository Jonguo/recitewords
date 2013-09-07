package com.xiaobawang.zhongxue.yingyu.beidanci;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.recite.assist.HelpMain;
import com.recite.assist.Values;
import com.recite.entity.Word;
import com.recite.sql.NotebookOperate;
import com.xiaobawang.zhongxue.yingyu.beidanci.adapter.NotebookAdapter;

//public class NotebookActivity extends AppRuntimeActivity
public class NotebookActivity extends Activity
{

	private ListView mlistView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notbook);
		mlistView = (ListView) findViewById(R.id.listview_notbookShow);
		List<Word> words = new ArrayList<Word>();
		
		for(int i=0;i<Values.oldnotebookWords.size();++i)
		{
			words.add(Values.oldnotebookWords.get(i));
		}		
		
		for(int i=0;i<Values.newnotebookwords.size();++i)
		{
			words.add(Values.newnotebookwords.get(i));
		}
		
		NotebookAdapter adapter = new NotebookAdapter (words,NotebookActivity.this);
		mlistView.setAdapter(adapter);
		HelpMain.getInstance().cancelToast();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		NotebookOperate.context = NotebookActivity.this;
		NotebookOperate.delete(Values.removedwords);		
		Values.removedwords.clear();
	}
	
	
	
}
