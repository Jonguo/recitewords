package com.xiaobawang.zhongxue.yingyu.beidanci.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.recite.assist.HelpMain;
import com.recite.assist.NotbookLogic;
import com.recite.assist.Values;
import com.recite.entity.Word;
import com.recite.media.PlayMp3;
import com.xiaobawang.zhongxue.yingyu.beidanci.R;

public class ListViewModeAdapter extends BaseAdapter
{

	private LayoutInflater mInflater = null;
	private List<Word> words = null;
	private Context context;
	public ListViewModeAdapter(List<Word> words,Context context)
	{
		this.context = context;
		this.words = words;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount()
	{
		return words.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return words.get(arg0);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		final int index = position;
		if(convertView == null)
		{
			holder = new ViewHolder();
			
			convertView = mInflater.inflate(R.layout.listview_mode_adapter, null);
			holder.wordTextView = (TextView)convertView.findViewById(R.id.listview_mode_adapter_textView_word);
			holder.explainTextView = (TextView)convertView.findViewById(R.id.listview_mode_adapter_textView_explain_notbook);
			holder.addToNotebook = (Button)convertView.findViewById(R.id.listview_mode_adapter_button_add_to_notbook);
			
			convertView.setTag(holder);
		}else
		{
		   holder = (ViewHolder)convertView.getTag();	
		}
		holder.wordTextView.setText((String)words.get(position).name +" "+ words.get(position).soundmark );
		holder.explainTextView.setText((String)words.get(position).explain);
		
		holder.wordTextView.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					PlayMp3.getInstance().PlayMp3(context,words.get(index), 1,Values.currentGrade);
				} catch (Exception e)
				{
					if(e.getMessage()!=null&&e.getMessage().equals("词库文件损坏"))
					{
						HelpMain.getInstance().showToast("词库文件损坏", context);
					}
					else e.printStackTrace();
				}
			}
		});

		holder.explainTextView.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					PlayMp3.getInstance().PlayMp3(context,words.get(index), 2,Values.currentGrade);
				} catch (Exception e)
				{
					if(e.getMessage()!=null&&e.getMessage().equals("词库文件损坏"))
					{
						HelpMain.getInstance().showToast("词库文件损坏", context);
					}
					else e.printStackTrace();
				}
			}
		});		
		
		holder.addToNotebook.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(NotbookLogic.getInstance().addToNotbook(words.get(index)))
				{
					HelpMain.getInstance().showToast("添加到生词本成功", context);				
				}
				else
				{
					HelpMain.getInstance().showToast("不须重复添加", context);
				}
			}
		});
		return convertView;
	}
	
	static class ViewHolder
	{
		public TextView wordTextView;
		public TextView explainTextView;
		public Button addToNotebook;
	}

}
