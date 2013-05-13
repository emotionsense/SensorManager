package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.WifiData;
import com.ubhave.sensormanager.data.pullsensor.WifiScanResult;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class WifiProcessor extends AbstractProcessor
{

	public WifiProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}

	public WifiData process(long pullSenseStartTimestamp, ArrayList<WifiScanResult> wifiScanResults,
			SensorConfig sensorConfig)
	{
		WifiData wifiData = new WifiData(pullSenseStartTimestamp, sensorConfig);

		if (setRawData)
		{
			wifiData.setWifiScanData(wifiScanResults);
		}

		if (setProcessedData)
		{
			// process
		}

		return wifiData;
	}

}
