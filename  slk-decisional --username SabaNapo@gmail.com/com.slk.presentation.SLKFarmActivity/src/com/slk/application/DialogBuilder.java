package com.slk.application;

import java.util.List;
import com.slk.R;
import com.slk.bean.Farm;
import com.slk.bean.Farmer;
import com.slk.http.HttpConnector;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DialogBuilder
{
	private Context context;
	private Typeface tf;
	private boolean update;
	public boolean isUpdate() 
	{
		return update;
	}


	public void setUpdate(boolean update) 
	{
		this.update = update;
	}


	public DialogBuilder(Context context)
	{
		this.context = context;
		tf = Typeface.createFromAsset(context.getAssets(),"fonts/KMKDSP__.ttf");
	}
	
	
	public Dialog createDialogTwoButtons(int title, int message)
	{
		boolean update;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_twobuttons);
		TextView tv = (TextView) dialog.findViewById(R.id.tvDialog2Title);
		tv.setTypeface(tf);
		tv.setText(title);
		tv = (TextView) dialog.findViewById(R.id.tvDialog2Message);
		tv.setTypeface(tf);
		tv.setText(message);
		Button yes = (Button) dialog.findViewById(R.id.btnDialog2Ok);
		yes.setTypeface(tf);
		Button no = (Button) dialog.findViewById(R.id.btnDialog2No);
		no.setTypeface(tf);
		yes.setOnClickListener(new OnClickListener() 
		{
			
			public void onClick(View arg0) 
			{
				setUpdate(true);
				dialog.dismiss();
			}
		});
		
		no.setOnClickListener(new OnClickListener() 
		{
			
			public void onClick(View v) 
			{
				setUpdate(false);
				dialog.dismiss();
			}
		});
		return dialog;
	}
	
	public Dialog createDialog(int title, int message, final Intent intent, final Activity activity)
	{
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		
		TextView tv = (TextView) dialog.findViewById(R.id.tvDialogTitle);
		tv.setTypeface(tf);
		tv.setText(title);
		tv = (TextView) dialog.findViewById(R.id.tvDialogMessage);
		tv.setTypeface(tf);
		tv.setText(message);
		Button ok = (Button) dialog.findViewById(R.id.btnDialog);
		ok.setTypeface(tf);
		ok.setOnClickListener(new OnClickListener() 
		{			
			public void onClick(View v) 
			{
				if (intent != null)
				{
					context.startActivity(intent);
					dialog.dismiss();
					activity.finish();
				}
				else
					dialog.dismiss();
			}
		});
		return dialog;
	}
	
	public Dialog createProgressDialog()
	{
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_progress);
		ProgressBar bar = (ProgressBar) dialog.findViewById(R.id.progressBarCustom);
		TextView tv = (TextView) dialog.findViewById(R.id.tvCustomProgressLabel);
		tv.setTypeface(tf);
		return dialog;
		
	}
	
	public Toast createToast(int stringId, String message, Activity activity)
	{
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.layoutToast));
		TextView text = (TextView) layout.findViewById(R.id.tvToast);
		text.setTypeface(tf);
		if (stringId != 0)
			text.setText(stringId);
		else
			text.setText(message);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 100);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		return toast;	
	}
	
	
	public Dialog creatSelectDialog(final List<Farm> list, final Farmer farmer, final Activity activity)
	{
		final Dialog dialog = new Dialog(context);
		final Farm farmObject = new Farm();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog_select);
		TextView tv = (TextView) dialog.findViewById(R.id.tvDialogSelectTitle);
		//tv.setTypeface(tf);
		final RadioGroup group = (RadioGroup) dialog.findViewById(R.id.radioGroupFarm);
		
		
		Button ok = (Button) dialog.findViewById(R.id.btnDialogSelect);
		//ok.setTypeface(tf);
		ok.setOnClickListener(new OnClickListener() 
		{			
			public void onClick(View v) 
			{
				if (group.getCheckedRadioButtonId() != -1)
				{					
					dialog.dismiss();
					Intent i = new Intent(context,com.slk.presentation.MenuActivity.class);
					HttpConnector.farmId = ""+list.get((group.getCheckedRadioButtonId()-300)).getId();
					i.putExtra(context.getPackageName() + ".farm", list.get(group.getCheckedRadioButtonId()-300));
					i.putExtra(context.getPackageName() + ".farmer", farmer);
					activity.startActivity(i);
					activity.finish();
				}
				else
				{
					
					Toast t = createToast(R.string.no_farm, null, activity);
					t.show();
				}
			}
		});
		for (int i=0; i<list.size();i++)
		{
			Farm f = list.get(i);
			RadioButton radio = new RadioButton(context);
			radio.setId(300+i);
			radio.setText(f.getName());
			//radio.setTypeface(tf);
			radio.setButtonDrawable(R.drawable.radiobutton_selector);
			radio.setTextColor(Color.parseColor("#43520B"));
			group.addView(radio);
		}	
		return dialog;
	}
	

	
	public int getValue()
	{
		return 1;
	}



}
