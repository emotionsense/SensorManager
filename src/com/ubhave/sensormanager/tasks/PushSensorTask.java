package com.ubhave.sensormanager.tasks;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.push.PushSensor;

public class PushSensorTask extends AbstractSensorTask implements SensorDataListener
{
	private final static String TAG = "PushSensorTask";
	
	public PushSensorTask(SensorInterface sensor)
	{
		super(sensor);
	}

	public void run()
	{
		while (true)
		{
			try
			{
				if (state == RUNNING)
				{
					if (!(((PushSensor) sensor).isSensing()))
					{
						SensorConfig sensorConfig = getSensorConfig();
						try
						{
							((PushSensor) sensor).startSensing(sensorConfig, this);
						}
						catch (ESException exp)
						{
							ESLogger.error(TAG, exp);
						}
					}
					else
					{
						synchronized (syncObject)
						{
							syncObject.wait();
						}
					}
				}
				else if ((state == PAUSED) || (state == STOPPED))
				{
					if (((PushSensor) sensor).isSensing())
					{
						try
						{
							((PushSensor) sensor).stopSensing(this);
						}
						catch (ESException e)
						{
							ESLogger.error(TAG, e);
						}
					}
					if (state == PAUSED)
					{
						synchronized (syncObject)
						{
							syncObject.wait(pauseTime);
						}
					}
					else if (state == STOPPED)
					{
						synchronized (syncObject)
						{
							syncObject.wait();
						}
					}
					state = RUNNING;
				}
			}
			catch (InterruptedException ie)
			{
				// ignore
			}
		}
	}

	@Override
	public void onDataSensed(SensorData data)
	{
		super.publishData(data);
	}

}
