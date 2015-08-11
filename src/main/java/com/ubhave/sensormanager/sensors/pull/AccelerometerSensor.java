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

package com.ubhave.sensormanager.sensors.pull;

import android.content.Context;
import android.hardware.Sensor;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.process.pull.AccelerometerProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class AccelerometerSensor extends AbstractMotionSensor
{
	private static final String TAG = "AccelerometerSensor";
	private static AccelerometerSensor accelerometerSensor;

	public static AccelerometerSensor getSensor(Context context) throws ESException
	{
		if (accelerometerSensor == null)
		{
			synchronized (lock)
			{
				if (accelerometerSensor == null)
				{
					accelerometerSensor = new AccelerometerSensor(context);
				}
			}
		}
		return accelerometerSensor;
	}

	private AccelerometerSensor(final Context context) throws ESException
	{
		super(context, Sensor.TYPE_ACCELEROMETER);
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_ACCELEROMETER;
	}

	protected void processSensorData()
	{
		synchronized (sensorReadings)
		{
			AccelerometerProcessor processor = (AccelerometerProcessor) getProcessor();
			data = processor.process(pullSenseStartTimestamp, sensorReadings, sensorReadingTimestamps, sensorConfig.clone());
		}
	}
}
