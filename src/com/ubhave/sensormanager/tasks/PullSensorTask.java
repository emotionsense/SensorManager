package com.ubhave.sensormanager.tasks;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.Utilities;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.SensorList;
import com.ubhave.sensormanager.sensors.pull.PullSensor;

public class PullSensorTask extends AbstractSensorTask
{
	private final static String TAG = "PullSensorTask";
	
	public PullSensorTask(SensorInterface sensor)
	{
		super(sensor);
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
						SensorConfig sensorConfig = getSensorConfig();

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
						else
						{
							long samplingInterval = (Long) sensorConfig.get(SensorConfig.SENSOR_SLEEP_INTERVAL);
							syncObject.wait(samplingInterval);
							if ((state == PAUSED) || (state == STOPPED))
							{
								continue;
							}
						}

						// sense, this is a blocking call and returns when
						// the sensing is complete, the sensorConfig object
						// will have the sampling window, cycle information
						ESLogger.log("SensorManager", "Pulling from: " + SensorList.getSensorName(sensor.getSensorType()));
						SensorData sensorData = ((PullSensor) sensor).sense(sensorConfig);
						// log sensed data
//						logData(sensorData);
						publishData(sensorData);
					}
					catch (InterruptedException exp)
					{
						// ignore
					}
				}
				catch (ESException e)
				{
					ESLogger.error(TAG, e);
					Utilities.sleep(30000);
				}
			}
		}
	}
}
