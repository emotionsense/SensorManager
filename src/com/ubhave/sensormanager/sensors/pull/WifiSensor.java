/*
 * Wifi sensor
 */

package com.ubhave.sensormanager.sensors.pull;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.ubhave.sensormanager.SurveyApplication;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.WifiData;

public class WifiSensor extends AbstractPullSensor
{

	private static final String TAG = "WifiSensor";

	WifiManager wifiManager;
	BroadcastReceiver wifiReceiver;
	ArrayList<ScanResult> wifiScanResults;

	private int cyclesRemaining;
	private static WifiSensor wifiSensor;
	private static Object lock = new Object();

	public static WifiSensor getWifiSensor()
	{
		if (wifiSensor == null)
		{
			synchronized (lock)
			{
				if (wifiSensor == null)
				{
					wifiSensor = new WifiSensor();
				}
			}
		}
		return wifiSensor;
	}

	private WifiSensor()
	{
		wifiManager = (WifiManager) SurveyApplication.getContext().getSystemService(Context.WIFI_SERVICE);
		wifiReceiver = new BroadcastReceiver()
		{
			public void onReceive(Context context, Intent intent)
			{
				List<ScanResult> wifiList = wifiManager.getScanResults();
				wifiScanResults.addAll(wifiList);

				cyclesRemaining -= 1;
				if ((cyclesRemaining > 0) && (wifiManager.isWifiEnabled()))
				{
					wifiManager.startScan();
				}
				else
				{
					notifySenseCyclesComplete();
				}
			}

		};
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return Constants.SENSOR_TYPE_WIFI;
	}

	protected WifiData getMostRecentRawData()
	{
		WifiData wifiData = new WifiData(pullSenseStartTimestamp, wifiScanResults);
		return wifiData;
	}

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		wifiScanResults = null;
		if (wifiManager.isWifiEnabled())
		{
			wifiScanResults = new ArrayList<ScanResult>();
			cyclesRemaining = (Integer) sensorConfig.get(SensorConfig.NUMBER_OF_SAMPLING_CYCLES);
			SurveyApplication.getContext().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			wifiManager.startScan();
			return true;
		}
		return false;
	}

	// Called when a scan is finished
	protected void stopSensing()
	{
		SurveyApplication.getContext().unregisterReceiver(wifiReceiver);
	}

}
