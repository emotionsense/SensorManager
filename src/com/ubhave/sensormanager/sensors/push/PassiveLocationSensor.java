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
import com.ubhave.sensormanager.config.sensor.push.PassiveLocationConfig;
import com.ubhave.sensormanager.data.pushsensor.PassiveLocationData;
import com.ubhave.sensormanager.process.push.PassiveLocationProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PassiveLocationSensor extends AbstractPushSensor {
	private static final String TAG = "PassiveLocationSensor";
	private volatile static PassiveLocationSensor passiveLocationSensor;
	private static final String[] REQUIRED_PERMISSIONS = new String[] { Manifest.permission.ACCESS_FINE_LOCATION };

	private LocationListener locationListener;

	public static PassiveLocationSensor getPassiveLocationSensor(
			final Context context) throws ESException
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
						passiveLocationSensor = result = new PassiveLocationSensor(
								context);
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED,
								SensorUtils.SENSOR_NAME_CONNECTION_STRENGTH);
					}
				}
			}
		}
		return result;
	}

	private PassiveLocationSensor(Context context) {
		super(context);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {
				try
				{
					PassiveLocationProcessor processor = (PassiveLocationProcessor) getProcessor();
					PassiveLocationData locationtData = processor.process(System.currentTimeMillis(), loc,
							sensorConfig.clone());
					onDataSensed(locationtData);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			// We ignore those events.
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public int getSensorType() {
		return SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION;
	}

	@Override
	protected void onBroadcastReceived(Context context, Intent intent) {
		// We are not listening to broadcast so this is empty
	}

	@Override
	protected IntentFilter[] getIntentFilters() {
		return null;
	}

	@Override
	protected boolean startSensing() {
		LocationManager locationManager = (LocationManager) applicationContext
				.getSystemService(Context.LOCATION_SERVICE);
		try {
			locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
					(Long) getSensorConfig(PassiveLocationConfig.MIN_TIME),
					(Float) getSensorConfig(PassiveLocationConfig.MIN_DISTANCE),
					locationListener, Looper.getMainLooper());
		} catch (ESException e) {
			if (GlobalConfig.shouldLog()) {
				Log.e(TAG, "Error getting parameter value for sensor");
			}
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void stopSensing() {
		LocationManager locationManager = (LocationManager) applicationContext
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(locationListener);
	}

	@Override
	protected String getLogTag() {
		return TAG;
	}

}
