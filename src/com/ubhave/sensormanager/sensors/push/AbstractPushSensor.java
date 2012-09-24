package com.ubhave.sensormanager.sensors.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SurveyApplication;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.AbstractSensor;
import com.ubhave.sensormanager.sensors.SensorDataListener;

public abstract class AbstractPushSensor extends AbstractSensor implements PushSensor
{

	protected SensorDataListener sensorDataListener;
	protected BroadcastReceiver broadcastReceiver;

	protected abstract void onBroadcastReceived(Context context, Intent intent);

	protected abstract IntentFilter[] getIntentFilters();

	public AbstractPushSensor()
	{
		broadcastReceiver = new BroadcastReceiver()
		{
			public void onReceive(Context context, Intent intent)
			{
				if (isSensing)
				{
					onBroadcastReceived(context, intent);
				}
				else
				{
					ESLogger.log(getLogTag(), "BroadcastReceiver.onReceive() called while not sensing.");
				}
			}
		};
	}

	public void startSensing(SensorConfig sensorConfig, SensorDataListener listener) throws ESException
	{
		if (isSensing)
		{
			// sensing already started
			ESLogger.log(getLogTag(), "sensing already sensing");
			throw new ESException(ESException.SENSOR_ALREADY_SENSING, "sensor already sensing");
		}
		isSensing = true;
		this.sensorDataListener = listener;
		startSensing(sensorConfig);
		// register broadcast receiver
		Context context = SurveyApplication.getContext();
		IntentFilter[] filters = getIntentFilters();
		if ((filters != null) && (filters.length > 0))
		{
			for (IntentFilter aFilter : filters)
			{
				context.registerReceiver(broadcastReceiver, aFilter);
			}
		}

		ESLogger.log(getLogTag(), "Sensing started.");
	}

	public void stopSensing(SensorDataListener listener) throws ESException
	{
		if (!isSensing)
		{
			// sensing already started
			ESLogger.log(getLogTag(), "sensing not sensing");
			throw new ESException(ESException.SENSOR_NOT_SENSING, "sensor not sensing");
		}
		isSensing = false;
		stopSensing();
		
		IntentFilter[] filters = getIntentFilters();
		if ((filters != null) && (filters.length > 0))
		{
			SurveyApplication.getContext().unregisterReceiver(broadcastReceiver);
		}

		ESLogger.log(getLogTag(), "Sensing stopped.");
	}

	protected void onDataSensed(SensorData sensorData)
	{
		ESLogger.log(getLogTag(), sensorData.toString());
		if (sensorDataListener != null)
		{
			sensorDataListener.onDataSensed(sensorData);
		}
	}

}
