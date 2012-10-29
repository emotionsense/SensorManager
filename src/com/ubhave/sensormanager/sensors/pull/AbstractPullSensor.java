package com.ubhave.sensormanager.sensors.pull;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.dutycyling.SleepWindowListener;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.AbstractSensor;

public abstract class AbstractPullSensor extends AbstractSensor implements PullSensor, SleepWindowListener
{
	protected long pullSenseStartTimestamp;
	
	protected SensorData prevSensorData;
	
	public AbstractPullSensor(Context context)
	{
		super(context);
	}

	protected abstract SensorData getMostRecentRawData();
	
	public void onSleepWindowLengthChanged(long sleepWindowLengthMillis)
	{
		sensorConfig.setParameter(SensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, sleepWindowLengthMillis);
	}

	public SensorData sense() throws ESException
	{
		if (isSensing)
		{
			// sensing already started
			ESLogger.log(getLogTag(), "sensing already started");
			throw new ESException(ESException.SENSOR_ALREADY_SENSING, "sensor busy");
		}

		isSensing = true;
		pullSenseStartTimestamp = System.currentTimeMillis();
		SensorData sensorData = null;

		// start sensing
		boolean sensingStarted = startSensing();
		if (sensingStarted)
		{
			ESLogger.log(getLogTag(), "Sensing started.");

			// wait for sensing to complete
			synchronized (senseCompleteNotify)
			{
				try
				{
					if (sensorConfig.containsParameter(SensorConfig.NUMBER_OF_SENSE_CYCLES))
					{
						senseCompleteNotify.wait();
					}
					else if (sensorConfig.containsParameter(SensorConfig.SENSE_WINDOW_LENGTH_MILLIS))
					{
						long samplingWindowSize = (Long) sensorConfig.getParameter(SensorConfig.SENSE_WINDOW_LENGTH_MILLIS);
						senseCompleteNotify.wait(samplingWindowSize);
					}
					else
					{
						throw new ESException(ESException.INVALID_SENSOR_CONFIG, "Invalid Sensor Config, window size or no. of cycles should in in the config");
					}
				}
				catch (InterruptedException e)
				{
					ESLogger.error(getLogTag(), e);
				}
			}

			// stop sensing
			stopSensing();
			isSensing = false;
			ESLogger.log(getLogTag(), "Sensing stopped.");

			sensorData = getMostRecentRawData();
			sensorData.setPrevSensorData(prevSensorData);
			prevSensorData = sensorData;

			ESLogger.log(getLogTag(), sensorData.toString());
		}
		else
		{
			ESLogger.log(getLogTag(), "Sensing not started.");
			isSensing = false;
		}

		return sensorData;
	}

	protected void notifySenseCyclesComplete()
	{
		synchronized (senseCompleteNotify)
		{
			senseCompleteNotify.notify();
		}
	}

}
