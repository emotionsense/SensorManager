package com.ubhave.sensormanager.process.push;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.ConnectionStateData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ConnectionStateProcessor extends AbstractProcessor
{
	public ConnectionStateProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public ConnectionStateData process(long recvTime, SensorConfig config, NetworkInfo activeNetwork, WifiInfo wifiInfo)
	{
		ConnectionStateData data = new ConnectionStateData(recvTime, config);
		if (setRawData)
		{
			data.setNetworkType(activeNetwork);
			data.setWifiDetails(wifiInfo);
		}
		return data;
	}

}
