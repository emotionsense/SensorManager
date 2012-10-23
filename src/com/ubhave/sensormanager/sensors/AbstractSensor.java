package com.ubhave.sensormanager.sensors;

import android.content.Context;
import android.content.pm.PackageManager;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;

public abstract class AbstractSensor implements SensorInterface
{

	protected boolean isSensing;
	protected final Context applicationContext;
	protected final Object senseCompleteNotify;
	protected final SensorConfig sensorConfig;
	
	public AbstractSensor(Context context)
	{
		applicationContext = context;
		senseCompleteNotify = new Object();
		sensorConfig = SensorUtils.getDefaultSensorConfig(getSensorType());
	}
	
	protected static boolean permissionGranted(Context context, String permission)
	{
		return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}

	protected abstract boolean startSensing(SensorConfig sensorConfig);

	protected abstract void stopSensing();

	protected abstract String getLogTag();

	public boolean isSensing()
	{
		return isSensing;
	}
	
	public void setSensorConfig(String configKey, Object configValue) throws ESException
	{
		if (sensorConfig.containsParameter(configKey))
		{
			sensorConfig.set(configKey, configValue);
		}
		else
		{
			throw new ESException(ESException.INVALID_SENSOR_CONFIG, "Invalid sensor config, key: " + configKey + " value: " + configValue);
		}
	}
	
	public Object getSensorConfig(String configKey) throws ESException
	{
		if (sensorConfig.containsParameter(configKey))
		{
			return sensorConfig.get(configKey);
		}
		else
		{
			throw new ESException(ESException.INVALID_SENSOR_CONFIG, "Invalid sensor config, key: " + configKey);
		}
	}

}
