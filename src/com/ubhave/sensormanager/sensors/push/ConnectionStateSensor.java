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

package com.ubhave.sensormanager.sensors.push;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.data.push.ConnectionStateData;
import com.ubhave.sensormanager.process.push.ConnectionStateProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ConnectionStateSensor extends AbstractPushSensor
{
	private static final String TAG = "ConnectionStateSensor";
	private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE};
	
	private static ConnectionStateSensor connectionSensor;
	private static final Object lock = new Object();

	public static ConnectionStateSensor getSensor(final Context context) throws ESException
	{
		if (connectionSensor == null)
		{
			synchronized (lock)
			{
				if (connectionSensor == null)
				{
					if (allPermissionsGranted(context, REQUIRED_PERMISSIONS))
					{
						connectionSensor = new ConnectionStateSensor(context);
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED, SensorUtils.SENSOR_NAME_CONNECTION_STATE);
					}
				}
			}
		}
		return connectionSensor;
	}

	private ConnectionStateSensor(final Context context)
	{
		super(context);
	}

	protected void onBroadcastReceived(final Context context, final Intent intent)
	{
		if (isSensing)
		{
			try
			{
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

				WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

				ConnectionStateProcessor processor = (ConnectionStateProcessor) getProcessor();
				ConnectionStateData data = processor.process(System.currentTimeMillis(), sensorConfig.clone(), activeNetwork, wifiInfo);
				onDataSensed(data);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (GlobalConfig.shouldLog())
		{
			Log.d(getLogTag(), "logOnDataSensed() called while not sensing.");
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
