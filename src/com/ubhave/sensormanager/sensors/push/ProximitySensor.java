/*
 * Proximity sensor
 */

package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.ProximityData;
import com.ubhave.sensormanager.sensors.SensorList;

public class ProximitySensor extends AbstractPushSensor
{
	private static final String TAG = "ProximitySensor";

	private static ProximitySensor ProximitySensor;
	private static Object lock = new Object();

	private SensorEventListener sensorEventListener;

	public static ProximitySensor getProximitySensor(Context context)
	{
		if (ProximitySensor == null)
		{
			synchronized (lock)
			{
				if (ProximitySensor == null)
				{
					ProximitySensor = new ProximitySensor(context);
				}
			}
		}
		return ProximitySensor;
	}

	private ProximitySensor(Context context)
	{
		super(context);
		sensorEventListener = new SensorEventListener()
		{
			public void onSensorChanged(SensorEvent event)
			{
				float distance = event.values[0];
				float maxRange = event.sensor.getMaximumRange();
				ProximityData proximityData = new ProximityData(System.currentTimeMillis(), distance, maxRange);
				onDataSensed(proximityData);
			}

			public void onAccuracyChanged(Sensor sensor, int accuracy)
			{
				// ignore
			}
		};
	}

	public String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorList.SENSOR_TYPE_PROXIMITY;
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{
		// ignore
	}

	protected IntentFilter[] getIntentFilters()
	{
		// no intent filters
		return null;
	}

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		SensorManager sensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
		boolean registered = sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
		return registered;
	}

	protected void stopSensing()
	{
		SensorManager sensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.unregisterListener(sensorEventListener);
	}

}
