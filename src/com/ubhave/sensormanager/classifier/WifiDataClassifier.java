package com.ubhave.sensormanager.classifier;

import java.util.ArrayList;

import android.net.wifi.ScanResult;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.WifiData;

public class WifiDataClassifier extends SocialClassifier implements SensorDataClassifier
{

	public boolean isInteresting(SensorData sensorData)
	{
		WifiData data = (WifiData) sensorData;
		WifiData prevData = (WifiData) sensorData.getPrevSensorData();

		String[] currDevices = getDeviceMacs(data);
		String[] prevDevices = getDeviceMacs(prevData);
		
		if (areSameDeviceAddrSets(prevDevices, currDevices))
		{
			return false;
		}
		else
		{
			return true;
		}

	}

	protected String[] getDeviceMacs(WifiData data)
	{
		String[] deviceList = null;

		if (data != null)
		{
			ArrayList<ScanResult> list = data.getWifiScanData();
			if (list != null)
			{
				deviceList = new String[list.size()];
				int i = 0;
				for (ScanResult sr : list)
				{
					deviceList[i++] = sr.BSSID;
				}
			}
		}
		return deviceList;
	}

}
