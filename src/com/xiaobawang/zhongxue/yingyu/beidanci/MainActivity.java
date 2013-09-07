package com.xiaobawang.zhongxue.yingyu.beidanci;
import java.util.Timer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.recite.assist.AibingLogic;
import com.recite.assist.FileOperate;
import com.recite.assist.HelpMain;
import com.recite.assist.NotbookLogic;
import com.recite.assist.Values;
import com.recite.entity.Word;
import com.recite.media.PlayMp3;
import com.recite.sql.AibingOperate;
import com.recite.sql.AllUnitNameOperate;
import com.recite.sql.NotebookOperate;

//public class MainActivity extends AppRuntimeActivity
public class MainActivity extends Activity
{
	public static FileOperate fileOperate = null;
	public static AllUnitNameOperate allUnitNameOperate = null;
	public HelpMain helpMain = HelpMain.getInstance();
	
	private Button mbutton_selectLexicon = null;
    
	private TextView mtextview_word = null,
			     mtextiew_soundmark = null,
				  mtextview_explain = null;
	private Button  mbutton_addToNotebook = null,
			               mbutton_engmp3 = null,
					     mbutton_remember = null,
					  mbutton_notRemember = null,
					      mbutton_notbook = null,
					       mbutton_aibing = null,
				    mbutton_listview_mode = null;


	private Timer timer;
	
	private boolean mplaychMp3 = false;	
	
	
	private View mNotbook_toast_view = null;
	private TextView mNotbook_toast_textView = null;
	private ProgressDialog progressdialog;
	
	private int openNotbookDirectState = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_activity);
 
        helpMain.init(MainActivity.this);
        
        
        fileOperate = new FileOperate(MainActivity.this);
        allUnitNameOperate = fileOperate.allUnitNameOperate;
        
        HelpMain.getInstance().cancelToast();
        
        mbutton_selectLexicon = (Button)findViewById(R.id.main_activity_button_select_lexicon);
        mtextview_word = (TextView)findViewById(R.id.textview_word);
        mtextiew_soundmark = (TextView)findViewById(R.id.textview_soundmark);
        mtextview_explain = (TextView)findViewById(R.id.textview_explain);
        mbutton_aibing = (Button)findViewById(R.id.button_aibing);
        mbutton_addToNotebook = (Button)findViewById(R.id.button_addToNotebook);
        mbutton_engmp3 = (Button)findViewById(R.id.button_engmp3);
        mbutton_notbook = (Button)findViewById(R.id.button_notbook);
        mbutton_notRemember = (Button)findViewById(R.id.button_notRemember);
        mbutton_remember = (Button)findViewById(R.id.button_remember);
        mbutton_listview_mode = (Button)findViewById(R.id.button_listviewMode);
        
        setWidgetListener();
        
        loadData();
        System.out.println("currentGrade" + Values.currentGrade);
        timer = new Timer();
        timer.schedule(AibingLogic.getInstance(MainActivity.this).getTask(), 0, 120000);
        
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        mNotbook_toast_view = inflater.inflate(R.layout.notbook_toast, (ViewGroup)findViewById(R.id.notbook_toast_linearlayout));
        mNotbook_toast_textView = (TextView)mNotbook_toast_view.findViewById(R.id.notbook_toast_textview);
               
        openNotbookDirectState = getOpenNotbookDirect();
	}
	

	
    private void setWidgetListener()
    {
        mbutton_selectLexicon.setOnClickListener(new View.OnClickListener()
        {
			
			@Override
			public void onClick(View v) 
			{
			   showSelectLexicon();
			}
		});
        mbutton_remember.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(Values.currentWord == null) 
				{
					HelpMain.getInstance().showToast("请先下载词库", MainActivity.this);
					return ;
				}
				AibingLogic.getInstance(MainActivity.this).rememberListener();
				updateWordView();
			}
		});
        mbutton_notRemember.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(Values.currentWord == null) 
				{
					HelpMain.getInstance().showToast("请先下载词库", MainActivity.this);
					return ;
				}
				AibingLogic.getInstance(MainActivity.this).notRememberlistener();
				updateWordView();
			}
		});        
        mtextview_explain.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(Values.currentWord == null) 
				{
					HelpMain.getInstance().showToast("请先下载词库", MainActivity.this);
					return ;
				}
				
				if(mplaychMp3 == false)
				{
					if(Values.fileWords!=null)
					mtextview_explain.setText(Values.currentWord.explain);
					mplaychMp3 = true;
				}
				else 
				{
					try
					{
						PlayMp3.getInstance().PlayMp3(MainActivity.this, Values.currentWord, 2,Values.currentGrade);
					} catch (Exception e)
					{
						if(e.getMessage()!=null&&e.getMessage().equals("词库文件损坏"))
						{
						   helpMain.showToast("词库文件损坏",MainActivity.this);	
						}
						else e.printStackTrace();
					}
				}
			}
		});
        
        mbutton_engmp3.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(Values.currentWord == null) 
				{
					HelpMain.getInstance().showToast("请先下载词库", MainActivity.this);
					return ;
				}
				try
				{
					PlayMp3.getInstance().PlayMp3(MainActivity.this, Values.currentWord, 1,Values.currentGrade);
				} catch (Exception e)
				{
					if(e.getMessage()!=null&&e.getMessage().equals("词库文件损坏"))
					{
					   helpMain.showToast("词库文件损坏",MainActivity.this);	
					}
					else e.printStackTrace();
				}
			}
		});
        
        mbutton_addToNotebook.setOnClickListener(new View.OnClickListener()
        {

			@Override
			public void onClick(View v)
			{
				if(Values.currentWord == null) 
				{
					HelpMain.getInstance().showToast("请先下载词库", MainActivity.this);
					return ;
				}
				if(NotbookLogic.getInstance().addToNotbook(Values.currentWord))
				{
					helpMain.cancelToast();
					Values.toastOnlyOne = new Toast(MainActivity.this);
					Values.toastOnlyOne .setDuration(Toast.LENGTH_SHORT);
					mNotbook_toast_textView.setText("添加到生词本成功");
					Values.toastOnlyOne .setView(mNotbook_toast_view);
					Values.toastOnlyOne .setGravity(Gravity.TOP,-350,200);
					Values.toastOnlyOne .show();
				}
				else
				{
					helpMain.cancelToast();
					Values.toastOnlyOne = new Toast(MainActivity.this);
					Values.toastOnlyOne .setDuration(Toast.LENGTH_SHORT);
					mNotbook_toast_textView.setText("不需重复添加");
					Values.toastOnlyOne .setView(mNotbook_toast_view);
					Values.toastOnlyOne .setGravity(Gravity.TOP,-350,200);
					Values.toastOnlyOne .show();
				}
			}
        	
        });
        
        mbutton_notbook.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, NotebookActivity.class);
				startActivity(intent);
			}
		});
        
        mbutton_listview_mode.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ListViewModeActivity.class);
				startActivity(intent);
//				startActivityForResult(intent, 0);
			}
		});
        
        mbutton_aibing.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AibingActivity.class);
				startActivity(intent);
			}
		});
        
    }

    
    
    
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (resultCode)
		{
		case RESULT_OK:
			Bundle bunde = data.getExtras();
			int which = bunde.getInt("which");
			boolean selected = bunde.getBoolean("selected", false);
			if(selected)
			{
				selectedLexicon(Values.currentGrade,which);
			}
			break;

		default:
			break;
		}
	}



	private void showSelectLexicon()
	{
//		int mode = Values.bookNameString == null?0:1;
//		if(allUnitNameStrings == null) getAllUnitName();
//		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
//		                    .setTitle("选择词库")
//		                    .setIcon(R.drawable.ic_launcher)
//		                    .setItems(allUnitNameStrings,new DialogInterface.OnClickListener()
//							{	
//								@Override
//								public void onClick(DialogInterface dialog, int which)
//								{
//									selectedLexicon(which);
//								}
//							}).setNegativeButton("下载更多",new DialogInterface.OnClickListener()
//							{
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which)
//								{	
////									Intent intent = MainActivity.this.getPackageManager().getLaunchIntentForPackage(Values.PACKAGENAME_DOWNLOADMORE);
////									if (intent != null) {
////										intent.putExtra(Values.PACKAGENAME, "你的应用包名");
////										MainActivity.this.startActivity(intent);
////									}
//									DownloadMoreUtil.open(MainActivity.this, false);
//								}
//							}).create();
//		dialog.show();
//		if (mode == 0)
//		{
//			dialog.setCancelable(false);
//			dialog.setOnKeyListener(new DialogInterface.OnKeyListener()
//			{
//
//				@Override
//				public boolean onKey(DialogInterface dialog, int keyCode,
//						KeyEvent event)
//				{
//					if (keyCode == KeyEvent.KEYCODE_BACK)
//					{
//						helpMain.showToast("请先下载或者选择词库", MainActivity.this);
//						MainActivity.this.finish();
//					}
//					return false;
//				}
//			});
//		}
		
		Intent intent = new Intent();
		intent.setClass(MainActivity.this,SelectLexiconDialogActivity.class);
		startActivityForResult(intent, 0);
	}
    private void importData()
    {
    	 progressdialog = ProgressDialog.show(MainActivity.this, "请稍等...", "正在导入词库..");
    	new Thread()
    	{
    		public void run()
    		{
    			try
    			{
//    				if(Values.bookNameString == null)
//    				{
//                     fileOperate.importFile();
//  					Message msg = new Message();
//  					msg.what = 5;
//  					MyHandler.sendMessage(msg);	
//                      progressdialog.dismiss();               
//    				}				
//    				else 
    				if(fileOperate.importFile())
    				{
					Message msg = new Message();
					msg.what = 3;
					MyHandler.sendMessage(msg);					
					progressdialog.dismiss();
    				}
    				else
    				progressdialog.dismiss();
    			}
    			catch(Exception e)
    			{
    				if(e.getMessage().equals("内存卡异常"))
    				{
    					Message msg = new Message();
    					msg.what = 1;
    					MyHandler.sendMessage(msg);
    				}
    				else e.printStackTrace();
    			}finally
    			{
    				progressdialog.dismiss();
    			}
    		}
    	}.start();
    }
    
//    private void getAllUnitName()
//    {
//    	
//	  allUnitNameStrings = helpMain.mapUnitName(allUnitNameOperate.query(Values.currentGrade), allUnitNameMap);
//
//    }
    
    private Handler MyHandler = new Handler()
    {
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				helpMain.showToast("内存卡异常", MainActivity.this);
				MainActivity.this.finish();
				break;

			case 2:
				helpMain.showToast("文件异常", MainActivity.this);
				MainActivity.this.finish();
				break;
			case 3:
				helpMain.showToast("导入词库成功", MainActivity.this);
				if(Values.bookNameString == null)
				showSelectLexicon();
				break;
			case 4:
				helpMain.showToast("词库文件损坏,请重新下载", MainActivity.this);
				showSelectLexicon();
			case 5:
				showSelectLexicon();
			}

    	}
    };
    
    private void selectedLexicon(int grade,int which)
    {
    	String bookNameAndIndex =  Values.allUnitNameMap.get(Values.allUnitNameStrings.get(which));
    	String[] strings = bookNameAndIndex.split("\r\n");
    	Values.bookNameString = strings[0];
        Values.unitIndex = Integer.parseInt(strings[1]);
        Values.lexiconNameString = Values.allUnitNameStrings.get(which);
    	if(Values.lexiconNameString != null)
    	{
    		mbutton_selectLexicon.setText(Values.lexiconNameString);
    	}
         try
		{
        	Values.fileWords = fileOperate.readWords(Values.bookNameString, Values.unitIndex,grade);
        	Values.currentWord = Values.fileWords.get(0);
			updateWordView();
		} catch (Exception e)
		{
			if(e.getMessage().equals("文件损坏"))
			{
				Message msg = new Message();
				msg.what = 4;
				MyHandler.sendMessage(msg);				
			}
			else e.printStackTrace();
		}
    }
    
    private void updateWordView()
    {
    	if(Values.currentWord == null) return ;
    	mtextview_word.setText(Values.currentWord.name);
    	mtextview_explain.setText("中文释义");
    	mtextiew_soundmark.setText(Values.currentWord.soundmark);
    	mplaychMp3 = false;
    }

	
	
	 private void loadData()
	 {
	        NotebookOperate.context = MainActivity.this;
	        Values.oldnotebookWords = NotebookOperate.query();
	        
	        SharedPreferences sharedPreferences = getSharedPreferences(Values.PREFS_NAME_STRING, 0);
	        
	        int tempGrade = Values.grade;
	        Values.currentGrade = sharedPreferences.getInt("grade", tempGrade);
	        
	        String lastUserName = sharedPreferences.getString("userName", null);
	        
	        if(!Values.userNameString.equals(lastUserName))
	        {
	        	Values.bookNameString = null;
	        	return ;
	        }
	        
	        Values.bookNameString = sharedPreferences.getString("bookName", null);
	        Values.aibingOpen = sharedPreferences.getBoolean("aibingOpen", true);
	        
	        if(Values.bookNameString == null)
	        {
	        	helpMain.showToast("请先选择或下载词库", MainActivity.this);
	        } 
	        else
	        {
	        	Values.unitIndex = sharedPreferences.getInt("unitIndex", 0);
	        	Values.lexiconNameString = sharedPreferences.getString("lexiconName", null);
	        	if(Values.lexiconNameString != null)
	        	{
	        		mbutton_selectLexicon.setText(Values.lexiconNameString);
	        	}
	        	Values.indexFileWords = sharedPreferences.getInt("fileWordsIndex", 0);
	        	Values.currentWord = new Word();
	        	Values.currentWord.name = sharedPreferences.getString("word", null);
	        	if(Values.currentWord.name == null)
	        	{
	        		Values.currentWord = null;
	        	}
	        	else 
	        	{
	        		Values.currentWord.soundmark = sharedPreferences.getString("soundmark", null);
	        		Values.currentWord.explain = sharedPreferences.getString("explain", null);
	        		Values.currentWord.enStart = sharedPreferences.getInt("ens", 0);
	        		Values.currentWord.enlen = sharedPreferences.getInt("enl", 0);
	        		Values.currentWord.chStart = sharedPreferences.getInt("chs", 0);
	        		Values.currentWord.chlen = sharedPreferences.getInt("chl", 0);
	        		Values.currentWord.bookName = sharedPreferences.getString("bookName", null);
	        	}
				try
				{
					Values.fileWords = fileOperate.readWords(Values.bookNameString, Values.unitIndex,Values.currentGrade);
				} catch (Exception e)
				{
					if(e.getMessage().equals("文件损坏"))
					{
						Message msg = new Message();
						msg.what = 4;
						MyHandler.sendMessage(msg);				
					}
					else e.printStackTrace();
				}
				updateWordView();        	
	        }
		 
	 }
	
    
	 private void saveData()
	 {
			if(Values.unitIndex!=-1)
			{
			 SharedPreferences sharedPreferences = getSharedPreferences(Values.PREFS_NAME_STRING, 0);
			 SharedPreferences.Editor editor = sharedPreferences.edit();
			 editor.putString("bookName", Values.bookNameString);
			 editor.putInt("unitIndex", Values.unitIndex);
			 editor.putInt("grade", Values.currentGrade);
			 editor.putString("lexiconName", Values.lexiconNameString);
			 editor.putBoolean("aibingOpen", Values.aibingOpen);
			 editor.putString("userName", Values.userNameString);
			 if(Values.currentWord!=null)
			 {
				 editor.putString("word", Values.currentWord.name);
				 editor.putString("soundmark", Values.currentWord.soundmark);
				 editor.putString("explain", Values.currentWord.explain);
				 editor.putString("bookName", Values.currentWord.bookName);
				 editor.putInt("ens", Values.currentWord.enStart);
				 editor.putInt("enl", Values.currentWord.enlen);
				 editor.putInt("chs", Values.currentWord.chStart);
				 editor.putInt("chl", Values.currentWord.chlen);
				 editor.putInt("fileWordsIndex", Values.indexFileWords);
			 }
			 editor.commit();
			}
			 NotebookOperate.context = MainActivity.this;
			 NotebookOperate.insert(Values.newnotebookwords);
			 for(int i=0;i<Values.newnotebookwords.size();++i)
			 {
			   Values.oldnotebookWords.add(Values.newnotebookwords.get(i));
			 }		 
			 Values.newnotebookwords.clear();	
			 
			 AibingOperate.getInstance(MainActivity.this).insert(Values.aibingInsertWords);
			 AibingOperate.getInstance(MainActivity.this).update(Values.aibingUpdatedWords);
			 AibingOperate.getInstance(MainActivity.this).delete(Values.aibingDeleteWords);		 
	 }
    
		@Override
	protected void onDestroy()
	{
			super.onDestroy();
			timer.cancel();
			saveData();
	}


		@Override
		protected void onStart()
		{
			super.onStart();
			
			if(Values.bookNameString == null||Values.NeedImportLexicon)
			{
			importData();
			Values.NeedImportLexicon = false;
			}
			
			if(openNotbookDirectState == 1)
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, NotebookActivity.class);		
				startActivity(intent);
				openNotbookDirectState = 2;
			}
			else if(openNotbookDirectState == 2)
			{
				finish();
			}
		}
		
		private int getOpenNotbookDirect()
		{
			try
			{
				Bundle bundle = getIntent().getExtras();
				if(bundle == null) return 0;
				String s = bundle.getString("OPEN_NOTBOOK");
				if(s!=null && s.equals("yes"))
				{
					return 1;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return 0;
		}
		
		
	
	 
}
