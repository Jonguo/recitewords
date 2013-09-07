package com.xiaobawang.zhongxue.yingyu.beidanci.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.recite.assist.Values;
import com.xiaobawang.zhongxue.yingyu.beidanci.R;

public class AibingAdapter extends BaseAdapter
{

	private LayoutInflater mInflater = null;
	private Context context;
	private String[] preStrings =
	{ "第一次复习时间:", "第二次复习时间:", "第三次复习时间:", "第四次复习时间:", "第五次复习时间:", "第六次复习时间:",
			"第七次复习时间:", "第八次复习时间:", "第九次复习时间:", "第十次复习时间:" };

	public AibingAdapter(Context context)
	{
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return Values.myTimes.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return Values.myTimes.get(arg0);
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
		if (convertView == null)
		{
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.aibing_adapter, null);
			holder.preStringTextView = (TextView) convertView
					.findViewById(R.id.aibing_adapter_textView_pre);
			holder.numberEditText = (EditText) convertView
					.findViewById(R.id.aibing_adapter_editText_number);
			holder.typeTextView = (TextView) convertView
					.findViewById(R.id.aibing_adapter_textview_time_type);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if (position < Values.myTimes.size())
		{
			holder.preStringTextView.setText(preStrings[position]);
			holder.numberEditText.setText(String.valueOf(Values.myTimes
					.get(position).number));
			String string = "";
			switch (Values.myTimes.get(position).type)
			{
			case 1:
				string = "分钟";
				break;
			case 2:
				string = "小时";
				break;
			case 3:
				string = "天";
				break;
			default:
				break;
			}
			holder.typeTextView.setText(string);
		}
		return convertView;
	}

	static class ViewHolder
	{
		public TextView preStringTextView;
		public EditText numberEditText;
		public TextView typeTextView;
	}

}
