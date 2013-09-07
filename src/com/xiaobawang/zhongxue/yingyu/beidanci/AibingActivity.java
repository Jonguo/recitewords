package com.xiaobawang.zhongxue.yingyu.beidanci;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.recite.assist.HelpMain;
import com.recite.assist.Values;
import com.xiaobawang.zhongxue.yingyu.beidanci.adapter.AibingAdapter;

//public class AibingActivity extends AppRuntimeActivity
public class AibingActivity extends Activity
{
	
	Button mbriefButton = null,
		   mdefaultButton = null;
	ListView mlistView = null;
	ToggleButton mToggleButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aibing);
		HelpMain.getInstance().cancelToast();
		mbriefButton = (Button) findViewById(R.id.button_brief);
		mbriefButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(AibingActivity.this, BriefIntroductionActivity.class);
				startActivity(intent);
			}
		});
		mlistView = (ListView)findViewById(R.id.aibing_listview_show);
		AibingAdapter adapter = new AibingAdapter(AibingActivity.this);
		mlistView.setAdapter(adapter);
		
		mToggleButton = (ToggleButton) findViewById(R.id.aibing_togglebutton_open);
		mToggleButton.setChecked(Values.aibingOpen);
		mToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					HelpMain.getInstance().showToast("您已经开启艾宾浩斯功能", AibingActivity.this);
					Values.aibingOpen = true;
				}
				else 
				{
					HelpMain.getInstance().showToast("您已经关闭艾宾浩斯功能", AibingActivity.this);
					Values.aibingOpen = false;					
				}
			}
		});
		
	}

}
