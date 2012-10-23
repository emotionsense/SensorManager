package com.ubhave.sensormanager.sensors.pull;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.AbstractSensor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public abstract class AbstractPullSensor extends AbstractSensor implements PullSensor
{
	protected long pullSenseStartTimestamp;
	
	protected SensorData prevSensorData;
	
	public AbstractPullSensor(Context context)
	{
		super(context);
	}

	protected abstract SensorData getMostRecentRawData();

	public SensorData sense(SensorConfig sensorConfig) throws ESException
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
		boolean sensingStarted = startSensing(sensorConfig);
		if (sensingStarted)
		{
			ESLogger.log(getLogTag(), "Sensing started.");

			// wait for sensing to complete
			synchronized (senseCompleteNotify)
			{
				try
				{
					if (sensorConfig.containsParameter(SensorConfig.NUMBER_OF_SAMPLING_CYCLES))
					{
						senseCompleteNotify.wait();
					}
					else if (sensorConfig.containsParameter(SensorConfig.SENSOR_SAMPLE_INTERVAL))
					{
						long samplingWindowSize = (Long) sensorConfig.get(SensorConfig.SENSOR_SAMPLE_INTERVAL);
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

	public long getDefaultSamplingInterval() throws ESException
	{
		SensorConfig sensorConfig = SensorUtils.getDefaultSensorConfig(getSensorType());
		if (sensorConfig.containsParameter(SensorConfig.SENSOR_SLEEP_INTERVAL))
		{
			return (Long) sensorConfig.get(SensorConfig.SENSOR_SLEEP_INTERVAL);
		}
		throw new ESException(ESException.CONFIG_NOT_SUPPORTED, "sampling interval parameter not supported for this sensor");
	}
}
