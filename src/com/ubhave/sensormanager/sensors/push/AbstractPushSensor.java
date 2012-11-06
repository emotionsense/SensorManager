package com.ubhave.sensormanager.sensors.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.AbstractSensor;

public abstract class AbstractPushSensor extends AbstractSensor implements PushSensor
{

	protected SensorDataListener sensorDataListener;
	protected BroadcastReceiver broadcastReceiver;

	protected abstract void onBroadcastReceived(Context context, Intent intent);

	protected abstract IntentFilter[] getIntentFilters();

	public AbstractPushSensor(Context context)
	{
		super(context);
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

	public void startSensing(SensorDataListener listener) throws ESException
	{
		if (isSensing)
		{
			// sensing already started
			ESLogger.log(getLogTag(), "sensing already sensing");
			throw new ESException(ESException.SENSOR_ALREADY_SENSING, "sensor already sensing");
		}
		
		this.sensorDataListener = listener;
		startSensing();
		// register broadcast receiver
		IntentFilter[] filters = getIntentFilters();
		if ((filters != null) && (filters.length > 0))
		{
			for (IntentFilter aFilter : filters)
			{
				applicationContext.registerReceiver(broadcastReceiver, aFilter);
			}
		}

		isSensing = true;
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
		
		stopSensing();
		IntentFilter[] filters = getIntentFilters();
		if ((filters != null) && (filters.length > 0))
		{
			applicationContext.unregisterReceiver(broadcastReceiver);
		}

		isSensing = false;
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
