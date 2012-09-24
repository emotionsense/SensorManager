/*
 * Screen sensor
 */

package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.ScreenData;

public class ScreenSensor extends AbstractPushSensor
{
	private static final String TAG = "ScreenSensor";

	private static ScreenSensor ScreenSensor;
	private static Object lock = new Object();

	public static ScreenSensor getScreenSensor()
	{
		if (ScreenSensor == null)
		{
			synchronized (lock)
			{
				if (ScreenSensor == null)
				{
					ScreenSensor = new ScreenSensor();
				}
			}
		}
		return ScreenSensor;
	}

	private ScreenSensor()
	{
	}

	public String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return Constants.SENSOR_TYPE_SCREEN;
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{

		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
		{
			ScreenData screenData = new ScreenData(System.currentTimeMillis(), ScreenData.SCREEN_ON);
			onDataSensed(screenData);
		}
		else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
		{
			ScreenData screenData = new ScreenData(System.currentTimeMillis(), ScreenData.SCREEN_OFF);
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

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		// nothing to do
		return true;
	}

	protected void stopSensing()
	{
		// nothing to do
	}

}
