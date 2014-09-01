package com.ubhave.sensormanager.process.push;

import android.content.Context;
import android.content.Intent;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.ScreenData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ScreenProcessor extends AbstractProcessor
{
	
	public ScreenProcessor(Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}
	
	public ScreenData process(long timestamp, SensorConfig config, Intent intent)
	{
		ScreenData data = new ScreenData(timestamp, config);
		
		int status = ScreenData.SCREEN_UNKNOWN;
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
		{
			status = ScreenData.SCREEN_ON;
		}
		else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
		{
			status = ScreenData.SCREEN_OFF;
		}
		
		data.setStatus(status);
		return data;
	}

}
