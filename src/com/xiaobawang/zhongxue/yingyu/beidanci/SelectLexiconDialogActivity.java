package com.xiaobawang.zhongxue.yingyu.beidanci;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.recite.assist.HelpMain;
import com.recite.assist.Values;
import com.recite.sql.AllUnitNameOperate;
import com.xiaobawang.util.api.DownloadMoreUtil;

//public class SelectLexiconDialogActivity extends AppRuntimeActivity
public class SelectLexiconDialogActivity extends Activity
{
	
	private List<String> mGradeNames = new ArrayList<String>();
	private Spinner mSpinner ;
	private ListView mListView ;
	private ArrayAdapter<String> listViewAdapter;
	private Button mDownloadButton;
	
	private int mCurrentGrade_temp = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		init();
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_lexicon_dialog);
		mSpinner = (Spinner)findViewById(R.id.select_lexicon_dialog_spinner);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectLexiconDialogActivity.this,android.R.layout.simple_spinner_item,mGradeNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setSelection(Values.currentGrade, true);
		
		mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
//				System.out.println(mGradeNames.get(arg2));
//				arg0.setVisibility(View.VISIBLE);
//				data.add("12354345");
				
				if(arg2!=mCurrentGrade_temp)
				{
					mCurrentGrade_temp = arg2;
					getLexiconName(mCurrentGrade_temp);
					listViewAdapter.notifyDataSetChanged();
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
			
		});
		
		mListView = (ListView)findViewById(R.id.select_lexicon_dialog_listView);
		
		listViewAdapter = new ArrayAdapter<String>(SelectLexiconDialogActivity.this, R.layout.select_lexicon_dialog_adapter,Values.allUnitNameStrings);
		
		
		mListView.setAdapter(listViewAdapter);
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				if(mCurrentGrade_temp!=Values.currentGrade)
				{
					Values.currentGrade = mCurrentGrade_temp;
				}
				Intent intent = SelectLexiconDialogActivity.this.getIntent();
				Bundle bundle = new Bundle();
				bundle.putInt("which", arg2);
				bundle.putBoolean("selected", true);
				intent.putExtras(bundle);
				SelectLexiconDialogActivity.this.setResult(RESULT_OK, intent);
				SelectLexiconDialogActivity.this.finish();
			}
			
		});
		
		mDownloadButton = (Button)findViewById(R.id.select_lexicon_dialog_download_button);
		
		mDownloadButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				DownloadMoreUtil.open(SelectLexiconDialogActivity.this, mCurrentGrade_temp+1);
				Values.NeedImportLexicon = true;
				SelectLexiconDialogActivity.this.finish();
			}
		});
		
		
	}
	
	private void init()
	{
		mCurrentGrade_temp = Values.currentGrade;
		initGradeName();
		getLexiconName(mCurrentGrade_temp);
	}
	
	private void initGradeName()
	{
		switch (Values.apkType)
		{
		case 1:
			mGradeNames.add("小学一年级");
			mGradeNames.add("小学二年级");
			mGradeNames.add("小学三年级");
			mGradeNames.add("小学四年级");
			mGradeNames.add("小学五年级");
			mGradeNames.add("小学六年级");
			break;
		case 2:
			mGradeNames.add("初中一年级");
			mGradeNames.add("初中二年级");
			mGradeNames.add("初中三年级");
			mGradeNames.add("高中一年级");
			mGradeNames.add("高中二年级");
			mGradeNames.add("高中三年级");
			break;
		}
	}

	private void getLexiconName(int grade)
	{
		
		List<String> temp = HelpMain.getInstance().mapUnitName(AllUnitNameOperate.getInstance(SelectLexiconDialogActivity.this).query(grade), Values.allUnitNameMap);
		Values.allUnitNameStrings.clear();
		for(int i=0;i<temp.size();++i)
		{
			Values.allUnitNameStrings.add(temp.get(i));
		}
	}
}
