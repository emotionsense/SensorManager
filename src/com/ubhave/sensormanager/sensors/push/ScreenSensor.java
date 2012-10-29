/*
 * Screen sensor
 */

package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubhave.sensormanager.data.pushsensor.ScreenData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ScreenSensor extends AbstractPushSensor
{
	private static final String TAG = "ScreenSensor";

	private static ScreenSensor ScreenSensor;
	private static Object lock = new Object();

	public static ScreenSensor getScreenSensor(Context context)
	{
		if (ScreenSensor == null)
		{
			synchronized (lock)
			{
				if (ScreenSensor == null)
				{
					ScreenSensor = new ScreenSensor(context);
				}
			}
		}
		return ScreenSensor;
	}

	private ScreenSensor(Context context)
	{
		super(context);
	}

	public String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SCREEN;
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{

		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
		{
			ScreenData screenData = new ScreenData(System.currentTimeMillis(), ScreenData.SCREEN_ON, sensorConfig.clone());
			onDataSensed(screenData);
		}
		else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
		{
			ScreenData screenData = new ScreenData(System.currentTimeMillis(), ScreenData.SCREEN_OFF, sensorConfig.clone());
			onDataSensed(screenData);			
		}
	}

	protected IntentFilter[] getIntentFilters()
	{
		IntentFilter[] filters = new IntentFilter[2];
		filters[0] = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filters[1] = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		return filters;
	}

	protected boolean startSensing()
	{
		// nothing to do
		return true;
	}

	protected void stopSensing()
	{
		// nothing to do
	}

}
