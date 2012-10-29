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
		// default parameters can be overridden through this method

		if (!sensorConfig.containsParameter(configKey))
		{
			throw new ESException(ESException.INVALID_SENSOR_CONFIG, "Invalid sensor config, key: " + configKey + " value: " + configValue);
		}

		// check permissions for the config
		if (configKey.equals(SensorConfig.LOCATION_ACCURACY_FINE))
		{
			if (!permissionGranted(applicationContext, "android.permission.ACCESS_FINE_LOCATION"))
			{
				throw new ESException(ESException.PERMISSION_DENIED, "Location Sensor: Fine Location Permission Not Granted!");
			}
		}

		sensorConfig.setParameter(configKey, configValue);
	}

	public Object getSensorConfig(String configKey) throws ESException
	{
		if (sensorConfig.containsParameter(configKey))
		{
			return sensorConfig.getParameter(configKey);
		}
		else
		{
			throw new ESException(ESException.INVALID_SENSOR_CONFIG, "Invalid sensor config, key: " + configKey);
		}
	}

}
