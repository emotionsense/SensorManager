package com.ubhave.sensormanager.process.push;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.ConnectionStateData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ConnectionStateProcessor extends AbstractProcessor
{
	public ConnectionStateProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}
	
	public ConnectionStateData process(long recvTime, SensorConfig config, NetworkInfo activeNetwork, WifiInfo wifiInfo)
	{
		ConnectionStateData data = new ConnectionStateData(recvTime, config);
		data.setNetworkType(activeNetwork);
		data.setWifiDetails(wifiInfo);
		return data;
	}

}
