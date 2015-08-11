/* **************************************************
 Copyright (c) 2014, Idiap
Hugues Salamin, hugues.salamin@idiap.ch

This file was developed to add passive location sensor to the SensorManager library
from https://github.com/nlathia/SensorManager.

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

package com.ubhave.sensormanager.sensors.push;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.push.PassiveLocationConfig;
import com.ubhave.sensormanager.data.push.PassiveLocationData;
import com.ubhave.sensormanager.process.push.PassiveLocationProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PassiveLocationSensor extends AbstractPushSensor
{
	private static final String TAG = "PassiveLocationSensor";
	private volatile static PassiveLocationSensor passiveLocationSensor;
	private static final String[] REQUIRED_PERMISSIONS = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };

	private LocationListener locationListener;

	public static PassiveLocationSensor getSensor(final Context context) throws ESException
	{
		/*
		 * Implement a double checked lock, using volatile. The result variable
		 * is for speed reason (avoid reading the volatile member too many time
		 */
		PassiveLocationSensor result = passiveLocationSensor;
		if (result == null)
		{
			synchronized (ConnectionStrengthSensor.class)
			{
				result = passiveLocationSensor;
				if (result == null)
				{
					if (allPermissionsGranted(context, REQUIRED_PERMISSIONS))
					{
						passiveLocationSensor = result = new PassiveLocationSensor(context);
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED, SensorUtils.SENSOR_NAME_CONNECTION_STRENGTH);
					}
				}
			}
		}
		return result;
	}

	private PassiveLocationSensor(final Context context)
	{
		super(context);
		locationListener = new LocationListener()
		{
			public void onLocationChanged(Location loc)
			{
				try
				{
					PassiveLocationProcessor processor = (PassiveLocationProcessor) getProcessor();
					PassiveLocationData locationtData = processor.process(System.currentTimeMillis(), loc, sensorConfig.clone());
					onDataSensed(locationtData);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras)
			{
				// Ignored
			}

			@Override
			public void onProviderEnabled(String provider)
			{
				// Ignored
			}

			@Override
			public void onProviderDisabled(String provider)
			{
				// Ignored
			}
		};
	}

	@Override
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION;
	}

	@Override
	protected void onBroadcastReceived(final Context context, final Intent intent)
	{
		// We are not listening to broadcast so this is empty
	}

	@Override
	protected IntentFilter[] getIntentFilters()
	{
		return null;
	}

	@Override
	protected boolean startSensing()
	{
		LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
		try
		{
			locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, (Long) getSensorConfig(PassiveLocationConfig.MIN_TIME),
					(Float) getSensorConfig(PassiveLocationConfig.MIN_DISTANCE), locationListener, Looper.getMainLooper());
		}
		catch (ESException e)
		{
			if (GlobalConfig.shouldLog())
			{
				Log.e(TAG, "Error getting parameter value for sensor");
			}
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void stopSensing()
	{
		LocationManager locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(locationListener);
	}

	@Override
	protected String getLogTag()
	{
		return TAG;
	}

}
