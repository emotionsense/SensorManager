package com.ubhave.sensormanager.sensors;

import android.content.Context;
import android.content.pm.PackageManager;

import com.ubhave.sensormanager.config.SensorConfig;

public abstract class AbstractSensor implements SensorInterface
{

	protected boolean isSensing;
	protected final Context applicationContext;
	protected final Object senseCompleteNotify;
	
	public AbstractSensor(Context context)
	{
		applicationContext = context;
		senseCompleteNotify = new Object();
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

}
