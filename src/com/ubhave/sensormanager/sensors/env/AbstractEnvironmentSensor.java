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

package com.ubhave.sensormanager.sensors.env;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.AbstractSensor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public abstract class AbstractEnvironmentSensor extends AbstractSensor
{
	protected static Object lock = new Object();
	protected SensorManager sensorManager;
	
	private SensorEventListener sensorEventListener;

	protected AbstractEnvironmentSensor(Context context)
	{
		super(context);
		sensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
		sensorEventListener = getEventListener();
		// TODO check that the sensor exists
	}
	
	protected final SensorEventListener getEventListener()
	{
		return new SensorEventListener()
		{
			public void onSensorChanged(SensorEvent event)
			{
				try
				{
					SensorData data = processEvent(event);
					// onDataSensed(lightData); // TODO
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
	
	protected abstract SensorData processEvent(final SensorEvent event);
	
	protected abstract Sensor getSensor();

	@Override
	protected final boolean startSensing()
	{
		Sensor sensor = getSensor();
		if (sensor != null)
		{
			return sensorManager.registerListener(sensorEventListener, getSensor(), SensorManager.SENSOR_DELAY_NORMAL);
		}
		else
		{
			return false;
		}
		
	}

	@Override
	protected final void stopSensing()
	{
		sensorManager.unregisterListener(sensorEventListener);
	}
}
