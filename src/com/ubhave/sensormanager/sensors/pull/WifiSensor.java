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

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.WifiData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class WifiSensor extends AbstractPullSensor
{

	private static final String TAG = "WifiSensor";

	private WifiManager wifiManager;
	private BroadcastReceiver wifiReceiver;
	private ArrayList<ScanResult> wifiScanResults;

	private int cyclesRemaining;
	private static WifiSensor wifiSensor;
	private static Object lock = new Object();

	public static WifiSensor getWifiSensor(Context context) throws ESException
	{
		if (wifiSensor == null)
		{
			synchronized (lock)
			{
				if (wifiSensor == null)
				{
					if (permissionGranted(context, "android.permission.ACCESS_WIFI_STATE")
							&& permissionGranted(context, "android.permission.ACCESS_NETWORK_STATE")
							&& permissionGranted(context, "android.permission.CHANGE_WIFI_STATE"))
					{
						wifiSensor = new WifiSensor(context);
					}
					else throw new ESException(ESException. PERMISSION_DENIED, "Wifi Sensor : Permission not Granted");
				}
			}
		}
		return wifiSensor;
	}

	private WifiSensor(Context context)
	{
		super(context);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
		return SensorUtils.SENSOR_TYPE_WIFI;
	}

	protected WifiData getMostRecentRawData()
	{
		WifiData wifiData = new WifiData(pullSenseStartTimestamp, wifiScanResults, sensorConfig.clone());
		return wifiData;
	}

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		wifiScanResults = null;
		if (wifiManager.isWifiEnabled())
		{
			wifiScanResults = new ArrayList<ScanResult>();
			cyclesRemaining = (Integer) sensorConfig.get(SensorConfig.NUMBER_OF_SENSE_CYCLES);
			applicationContext.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			wifiManager.startScan();
			return true;
		}
		return false;
	}

	// Called when a scan is finished
	protected void stopSensing()
	{
		applicationContext.unregisterReceiver(wifiReceiver);
	}

}
