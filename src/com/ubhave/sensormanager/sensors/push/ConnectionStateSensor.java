package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.pushsensor.ConnectionStateData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ConnectionStateSensor extends AbstractPushSensor
{
	private static final String TAG = "ConnectionStateSensor";
	private static ConnectionStateSensor connectionSensor;
	private static final Object lock = new Object();

	public static ConnectionStateSensor getConnectionStateSensor(Context context) throws ESException
	{
		if (connectionSensor == null)
		{
			synchronized (lock)
			{
				if (connectionSensor == null)
				{
					if (permissionGranted(context, "android.permission.ACCESS_WIFI_STATE")
							&& permissionGranted(context, "android.permission.ACCESS_NETWORK_STATE"))
					{
						connectionSensor = new ConnectionStateSensor(context);
					}
					else
						throw new ESException(ESException.PERMISSION_DENIED, "Connection Sensor Sensor : Permission not Granted");
				}
			}
		}
		return connectionSensor;
	}

	private ConnectionStateSensor(Context context)
	{
		super(context);
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{
		if (isSensing)
		{
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			
			WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
			
			ConnectionStateData data = new ConnectionStateData(System.currentTimeMillis(), activeNetwork, wifiInfo, sensorConfig.clone());
			onDataSensed(data);
		}
		else
		{
			ESLogger.log(getLogTag(), "logOnDataSensed() called while not sensing.");
		}
	}

	protected IntentFilter[] getIntentFilters()
	{
		IntentFilter[] filters = new IntentFilter[1];
		filters[0] = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		return filters;
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_CONNECTION_STATE;
	}

	protected boolean startSensing()
	{
		// nothing to do
		return true;
	}

	protected void stopSensing()
	{
		// nothing to do
	}

}
