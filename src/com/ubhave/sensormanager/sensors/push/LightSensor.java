/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.ubhave.sensormanager.data.pushsensor.LightData;
import com.ubhave.sensormanager.process.push.LightProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class LightSensor extends AbstractPushSensor
{
	private static final String TAG = "LightSensor";

	private static LightSensor LightSensor;
	private static Object lock = new Object();

	private SensorEventListener sensorEventListener;

	public static LightSensor getLightSensor(final Context context)
	{
		if (LightSensor == null)
		{
			synchronized (lock)
			{
				if (LightSensor == null)
				{
					LightSensor = new LightSensor(context);
				}
			}
		}
		return LightSensor;
	}

	private LightSensor(Context context)
	{
		super(context);
		sensorEventListener = new SensorEventListener()
		{
			public void onSensorChanged(SensorEvent event)
			{
				try
				{
					float light = event.values[0];
					float maxRange = event.sensor.getMaximumRange();

					LightProcessor processor = (LightProcessor) getProcessor();
					LightData lightData = processor.process(System.currentTimeMillis(), sensorConfig.clone(), light, maxRange);

					onDataSensed(lightData);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
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
		return SensorUtils.SENSOR_TYPE_LIGHT;
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

	protected boolean startSensing()
	{
		SensorManager sensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
		boolean registered = sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
		return registered;
	}

	protected void stopSensing()
	{
		SensorManager sensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.unregisterListener(sensorEventListener);
	}

}
