package com.ubhave.sensormanager.data.pushsensor;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ConnectionStateData extends SensorData
{
	public final static int NO_CONNECTION = 0;
	public final static int MOBILE_CONNECTION = 1;
	public final static int WIFI_CONNECTION = 2;
	public final static int OTHER_CONNECTION = 3;

	public final static int ROAMING = 0;
	public final static int NOT_ROAMING = 1;
	public final static int UNKNOWN_ROAMING = 2;

	private boolean isConnectedOrConnecting, isConnected, isAvailable;
	private int networkType, roamingType;
	private String ssid;

	public ConnectionStateData(long dataReceivedTimestamp, final NetworkInfo activeNetwork, final WifiInfo wifiInfo, final SensorConfig sensorConfig)
	{
		super(dataReceivedTimestamp, sensorConfig);
		setNetworkType(activeNetwork);
		setWifiDetails(wifiInfo);
	}

	private void setNetworkType(final NetworkInfo activeNetwork)
	{
		if (activeNetwork == null)
		{
			isConnectedOrConnecting = false;
			isConnected = false;
			isAvailable = false;
			roamingType = UNKNOWN_ROAMING;
			networkType = NO_CONNECTION;
		}
		else
		{
			isAvailable = activeNetwork.isAvailable();
			isConnectedOrConnecting = activeNetwork.isConnectedOrConnecting();
			isConnected = activeNetwork.isConnected();
			if (activeNetwork.isRoaming())
			{
				roamingType = ROAMING;
			}
			else
			{
				roamingType = NOT_ROAMING;
			}

			switch (activeNetwork.getType())
			{
			case ConnectivityManager.TYPE_MOBILE:
				networkType = MOBILE_CONNECTION;
				break;
			case ConnectivityManager.TYPE_WIFI:
				networkType = WIFI_CONNECTION;
				break;
			default:
				networkType = OTHER_CONNECTION;
				break;
			}
		}
	}

	private void setWifiDetails(final WifiInfo wifiInfo)
	{
		if (wifiInfo != null)
		{
			ssid = wifiInfo.getSSID();
		}
		else
		{
			ssid = null;
		}
	}
	
	public String getSSID()
	{
		return ssid;
	}

	public boolean isAvailable()
	{
		return isAvailable;
	}

	public boolean isConnectedOrConnecting()
	{
		return isConnectedOrConnecting;
	}

	public boolean isConnected()
	{
		return isConnected;
	}

	public int getNetworkType()
	{
		return networkType;
	}

	public int getRoamingStatus()
	{
		return roamingType;
	}

	@Override
	public String getDataString()
	{
		return "";
	}

	@Override
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_PHONE_STATE;
	}
}
