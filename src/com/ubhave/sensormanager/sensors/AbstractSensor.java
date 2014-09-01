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

package com.ubhave.sensormanager.sensors;

import android.content.Context;
import android.content.pm.PackageManager;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.pull.LocationConfig;
import com.ubhave.sensormanager.process.AbstractProcessor;

public abstract class AbstractSensor implements SensorInterface
{	
	protected boolean isSensing;
	protected final Context applicationContext;
	protected final Object senseCompleteNotify;
	protected final SensorConfig sensorConfig;

	protected AbstractSensor(final Context context)
	{
		applicationContext = context;
		senseCompleteNotify = new Object();
		sensorConfig = SensorConfig.getDefaultConfig(getSensorType());
	}

	protected static boolean permissionGranted(final Context context, final String permission)
	{
		return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}
	
	protected static boolean allPermissionsGranted(final Context context, final String[] permissions)
	{
		for (String permission : permissions)
		{
			if (!permissionGranted(context, permission))
			{
				return false;
			}
		}
		return true;
	}
	
	protected static boolean anyPermissionGranted(final Context context, final String[] permissions)
	{
		for (String permission : permissions)
		{
			if (permissionGranted(context, permission))
			{
				return true;
			}
		}
		return false;
	}

	protected abstract boolean startSensing();

	protected abstract void stopSensing();

	protected abstract String getLogTag();

	public boolean isSensing()
	{
		return isSensing;
	}

	public void setSensorConfig(final String configKey, final Object configValue) throws ESException
	{
		// default parameters can be overridden through this method
		if (!sensorConfig.containsParameter(configKey))
		{
			throw new ESException(ESException.INVALID_SENSOR_CONFIG, "Invalid sensor config, key: " + configKey + " value: " + configValue);
		}

		// check permissions for the config
		if (configKey.equals(LocationConfig.LOCATION_ACCURACY_FINE))
		{
			if (!permissionGranted(applicationContext, "android.permission.ACCESS_FINE_LOCATION"))
			{
				throw new ESException(ESException.PERMISSION_DENIED, "Location Sensor: Fine Location Permission Not Granted!");
			}
		}

		sensorConfig.setParameter(configKey, configValue);
	}

	public Object getSensorConfig(final String configKey) throws ESException
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
	
	private boolean getRawData()
	{
		if (sensorConfig.containsParameter(SensorConfig.DATA_SET_RAW_VALUES))
		{
			return (Boolean) sensorConfig.getParameter(SensorConfig.DATA_SET_RAW_VALUES);
		}
		return SensorConfig.GET_RAW_DATA;
	}
	
	private boolean getProcessedData()
	{
		if (sensorConfig.containsParameter(SensorConfig.DATA_EXTRACT_FEATURES))
		{
			return (Boolean) sensorConfig.getParameter(SensorConfig.DATA_EXTRACT_FEATURES);
		}
		return SensorConfig.GET_PROCESSED_DATA;
	}
	
	protected AbstractProcessor getProcessor()
	{
		try
		{
			boolean rawData = getRawData();
			boolean processedData = getProcessedData();
			if (!rawData && !processedData)
			{
				throw new ESException(ESException.INVALID_STATE, "No data requested from processor");
			}
			return AbstractProcessor.getProcessor(applicationContext, getSensorType(), rawData, processedData);
		}
		catch (ESException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
