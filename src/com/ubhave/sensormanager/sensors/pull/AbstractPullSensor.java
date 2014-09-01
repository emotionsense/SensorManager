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
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.dutycyling.SleepWindowListener;
import com.ubhave.sensormanager.sensors.AbstractSensor;

public abstract class AbstractPullSensor extends AbstractSensor implements PullSensor, SleepWindowListener
{
	protected long pullSenseStartTimestamp;

	protected SensorData prevSensorData;

	protected AbstractPullSensor(Context context)
	{
		super(context);
	}

	protected abstract SensorData getMostRecentRawData();
	
	protected abstract void processSensorData();

	public void onSleepWindowLengthChanged(long sleepWindowLengthMillis)
	{
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, sleepWindowLengthMillis);
	}

	public SensorData sense() throws ESException
	{
		if (isSensing)
		{
			// sensing already started
			throw new ESException(ESException.SENSOR_ALREADY_SENSING, "sensor busy");
		}

		isSensing = true;
		pullSenseStartTimestamp = System.currentTimeMillis();
		SensorData sensorData = null;

		// start sensing
		boolean sensingStarted = startSensing();
		if (sensingStarted)
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d(getLogTag(), "Sensing started.");
			}

			// wait for sensing to complete
			synchronized (senseCompleteNotify)
			{
				try
				{
					if (sensorConfig.containsParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES))
					{
						while (isSensing)
						{
							senseCompleteNotify.wait(500);
						}
					}
					else if (sensorConfig.containsParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS))
					{
						long samplingWindowSize = (Long) sensorConfig.getParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS);
						senseCompleteNotify.wait(samplingWindowSize);
					}
					else
					{
						throw new ESException(ESException.INVALID_SENSOR_CONFIG,
								"Invalid Sensor Config, window size or no. of cycles should in in the config");
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			// stop sensing
			stopSensing();
			isSensing = false;
			if (GlobalConfig.shouldLog())
			{
				Log.d(getLogTag(), "Sensing stopped.");
			}
			processSensorData();
			sensorData = getMostRecentRawData();
			sensorData.setPrevSensorData(prevSensorData);
			prevSensorData = sensorData;
		}
		else
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d(getLogTag(), "Sensing not started.");
			}
			isSensing = false;
		}

		return sensorData;
	}

	protected void notifySenseCyclesComplete()
	{
		synchronized (senseCompleteNotify)
		{
			senseCompleteNotify.notify();
			isSensing = false;
		}
	}
}
