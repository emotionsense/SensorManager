package com.ubhave.sensormanager.sensors.pull;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.LocationData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class LocationSensor extends AbstractPullSensor
{

	private static final String TAG = "LocationSensor";

	private LocationManager locationManager;

	private Location lastLocation;
	private LocationListener locListener;
	private static LocationSensor locationSensor;
	private static Object lock = new Object();

	public static LocationSensor getLocationSensor(Context context) throws ESException
	{
		if (locationSensor == null)
		{
			synchronized (lock)
			{
				if (locationSensor == null)
				{
					if (permissionGranted(context, "android.permission.ACCESS_COARSE_LOCATION")
							|| permissionGranted(context, "android.permission.ACCESS_FINE_LOCATION"))
					{
						locationSensor = new LocationSensor(context);
					}
					else throw new ESException(ESException. PERMISSION_DENIED, "Location Sensor: Permission Not Granted!");
				}
			}
		}
		return locationSensor;
	}

	private LocationSensor(Context context)
	{
		super(context);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locListener = new LocationListener()
		{

			public void onLocationChanged(Location loc)
			{
				if (isSensing)
				{
					lastLocation = loc;
					LocationData data = new LocationData(System.currentTimeMillis(), loc, sensorConfig.clone());
					ESLogger.log(TAG, data.toString());
				}
			}

			// Required by the API
			public void onProviderDisabled(String provider)
			{
			}

			// Required by the API
			public void onProviderEnabled(String provider)
			{
			}

			// Required by the API
			public void onStatusChanged(String provider, int status, Bundle extras)
			{
			}
		};
	}

	public String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_LOCATION;
	}

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		lastLocation = null;
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locListener, Looper.getMainLooper());
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener, Looper.getMainLooper());
		return true;
	}

	protected void stopSensing()
	{
		locationManager.removeUpdates(locListener);
	}

	protected SensorData getMostRecentRawData()
	{
		LocationData locationData = new LocationData(pullSenseStartTimestamp, lastLocation, sensorConfig.clone());
		return locationData;
	}
}
