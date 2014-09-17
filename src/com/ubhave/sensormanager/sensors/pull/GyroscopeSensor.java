/* **************************************************
 Copyright (c) 2014

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

package com.ubhave.sensormanager.sensors.pull;

import android.content.Context;
import android.hardware.Sensor;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.process.pull.GyroscopeProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class GyroscopeSensor extends AbstractMotionSensor
{
	private static final String TAG = "GyroscopeSensor";
	private static GyroscopeSensor gyroscopeSensor;

	public static GyroscopeSensor getSensor(final Context context) throws ESException
	{
		if (gyroscopeSensor == null)
		{
			synchronized (lock)
			{
				if (gyroscopeSensor == null)
				{
					gyroscopeSensor = new GyroscopeSensor(context);
				}
			}
		}
		return gyroscopeSensor;
	}

	private GyroscopeSensor(final Context context) throws ESException
	{
		super(context, Sensor.TYPE_GYROSCOPE);
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_GYROSCOPE;
	}

	protected void processSensorData()
	{
		synchronized (sensorReadings)
		{
			GyroscopeProcessor processor = (GyroscopeProcessor) getProcessor();
			data = processor.process(pullSenseStartTimestamp, sensorReadings, sensorReadingTimestamps, sensorConfig.clone());
		}
	}
}
