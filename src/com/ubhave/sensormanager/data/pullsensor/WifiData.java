package com.ubhave.sensormanager.data.pullsensor;

import java.util.ArrayList;

import com.ubhave.sensormanager.data.SensorData;

import android.net.wifi.ScanResult;

public class WifiData extends SensorData
{

	ArrayList<ScanResult> wifiScanData;

	public WifiData(long senseStartTimestamp, ArrayList<ScanResult> wifiScanResults)
	{
		super(senseStartTimestamp);
		this.wifiScanData = wifiScanResults;
	}

	public ArrayList<ScanResult> getWifiScanData()
	{
		return wifiScanData;
	}

	public String getDataString()
	{
		StringBuilder sb = new StringBuilder();
		if (wifiScanData != null)
		{
			if (wifiScanData.size() > 0)
			{
				for (int i = 0; i < wifiScanData.size(); i++)
				{
					sb.append(wifiScanData.get(i).SSID);
					sb.append(",");
					sb.append(wifiScanData.get(i).BSSID);
					sb.append(",");
					sb.append(wifiScanData.get(i).capabilities);
					sb.append(",");
					sb.append(wifiScanData.get(i).level);
					sb.append(",");
					sb.append(wifiScanData.get(i).frequency);
					sb.append(";");
				}
			}
			else
			{
				sb.append("NO_ACCESS_POINTS");
			}
		}
		else
		{
			sb.append("WIFI_DATA_NOT_AVAILABLE");
		}
		return sb.toString();
	}

}
