package com.ubhave.sensormanager.data.pullsensor;

import java.util.ArrayList;

import android.net.wifi.ScanResult;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class WifiData extends SensorData
{

	private ArrayList<ScanResult> wifiScanData;

	public WifiData(long senseStartTimestamp, ArrayList<ScanResult> wifiScanResults, SensorConfig sensorConfig)
	{
		super(senseStartTimestamp, sensorConfig);
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
	
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_WIFI;
	}

}
