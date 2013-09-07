package com.xiaobawang.zhongxue.yingyu.beidanci;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.recite.assist.HelpMain;
import com.recite.assist.Values;
import com.xiaobawang.zhongxue.yingyu.beidanci.adapter.ListViewModeAdapter;

//public class ListViewModeActivity extends AppRuntimeActivity
public class ListViewModeActivity extends Activity
{
	private ListView mlistView;
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_mode);
		HelpMain.getInstance().cancelToast();
		mlistView = (ListView)findViewById(R.id.aibing_listview_show);
		mTextView = (TextView)findViewById(R.id.listview_mode_textview_bar);
		if(Values.lexiconNameString != null)
		{
		mTextView.setText(Values.lexiconNameString);
		}
		if(Values.fileWords!=null)
		{
		ListViewModeAdapter listViewModeAdapter = new ListViewModeAdapter(Values.fileWords,this);
		mlistView.setAdapter(listViewModeAdapter);	
		}
//		mlistView.setOnItemClickListener(new OnItemClickListener()
//		{
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3)
//			{
//				Intent intent = ListViewModeActivity.this.getIntent();
//				Bundle bundle = intent.getExtras();
//				bundle.putInt("wordsIndex", arg2);
//				intent.putExtras(bundle);
//				ListViewModeActivity.this.setResult(RESULT_OK,intent);
//				ListViewModeActivity.this.finish();
//			}
//			
//		});
		
		
		
		
	}
	
	
	
}
