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

package com.ubhave.sensormanager.data.push;

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
	
	public ConnectionStateData(long timestamp, final SensorConfig config)
	{
		super(timestamp, config);
	}

	public void setNetworkType(final NetworkInfo activeNetwork)
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

	public void setWifiDetails(final WifiInfo wifiInfo)
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
	
	public void setSSID(String s)
	{
		ssid = s;
	}

	public String getSSID()
	{
		return ssid;
	}
	
	public void setAvailable(boolean a)
	{
		isAvailable = a;
	}

	public boolean isAvailable()
	{
		return isAvailable;
	}
	
	public void setConnectedOrConnecting(boolean b)
	{
		isConnectedOrConnecting = b;
	}

	public boolean isConnectedOrConnecting()
	{
		return isConnectedOrConnecting;
	}
	
	public void setConnected(boolean b)
	{
		isConnected = b;
	}

	public boolean isConnected()
	{
		return isConnected;
	}
	
	public void setNetworkType(int n)
	{
		networkType = n;
	}

	public int getNetworkType()
	{
		return networkType;
	}
	
	public void setRoamingStatus(int s)
	{
		roamingType = s;
	}

	public int getRoamingStatus()
	{
		return roamingType;
	}

	@Override
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_CONNECTION_STATE;
	}
}
