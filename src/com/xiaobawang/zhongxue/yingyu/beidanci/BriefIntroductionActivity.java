package com.xiaobawang.zhongxue.yingyu.beidanci;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.recite.assist.HelpMain;

//public class BriefIntroductionActivity extends AppRuntimeActivity
public class BriefIntroductionActivity extends Activity
{
	WebView mWebView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brief_introduction);
		HelpMain.getInstance().cancelToast();
		mWebView = (WebView) findViewById(R.id.webview_brief_introduction);
		try
		{
			mWebView.loadUrl("file:///android_asset/aibing.html");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
