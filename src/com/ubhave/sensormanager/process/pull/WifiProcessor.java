package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.WifiData;
import com.ubhave.sensormanager.data.pull.WifiScanResult;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class WifiProcessor extends AbstractProcessor
{
	public WifiProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public WifiData process(long pullSenseStartTimestamp, ArrayList<WifiScanResult> wifiScanResults, SensorConfig sensorConfig)
	{
		WifiData wifiData = new WifiData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			wifiData.setWifiScanData(wifiScanResults);
		}
		return wifiData;
	}

}
