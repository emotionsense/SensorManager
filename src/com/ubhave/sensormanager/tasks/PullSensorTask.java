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

package com.ubhave.sensormanager.tasks;

import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.sensors.pull.PullSensor;

public class PullSensorTask extends AbstractSensorTask
{

	public PullSensorTask(SensorInterface sensor)
	{
		super(sensor);
	}

	public SensorData getCurrentSensorData(boolean oneOffSensing) throws ESException
	{
		SensorData sensorData = ((PullSensor) sensor).sense();
		// since this is a one-off query for sensor data, sleep interval
		// is not relevant in this case
		if (sensorData != null)
		{
			// remove sleep length value for the case of one-off sensing
			if (oneOffSensing)
			{
				SensorConfig sensorConfig = sensorData.getSensorConfig();
				sensorConfig.removeParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS);
			}
		}
		return sensorData;
	}

	public void run()
	{
		synchronized (syncObject)
		{
			while (true)
			{
				try
				{
					try
					{

						if ((state == PAUSED) || (state == STOPPED))
						{
							if (state == PAUSED)
							{
								syncObject.wait(pauseTime);
							}
							else if (state == STOPPED)
							{
								syncObject.wait();
							}
							state = RUNNING;
							continue;
						}

						// SENSE
						// sense() is a blocking call and returns when
						// the sensing is complete, the sensorConfig object
						// will have the sampling window, cycle information
						if (GlobalConfig.shouldLog())
						{
							Log.d(getLogTag(), "Pulling from: " + SensorUtils.getSensorName(sensor.getSensorType()));
						}

						SensorData sensorData = getCurrentSensorData(false);
						// publish sensed data
						publishData(sensorData);

						// SLEEP
						long samplingInterval = (Long) sensor.getSensorConfig(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS);
						syncObject.wait(samplingInterval);
					}
					catch (InterruptedException exp)
					{
						// ignore
					}
				}
				catch (ESException e)
				{
					e.printStackTrace();
					try
					{
						Thread.sleep(30000);
					}
					catch (Exception exp)
					{
						exp.printStackTrace();
					}
				}
			}
		}
	}
}
