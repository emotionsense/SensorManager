/*
 * Batterysensor
 */

package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.BatteryData;
import com.ubhave.sensormanager.sensors.SensorList;

public class BatterySensor extends AbstractPushSensor
{
	private static final String TAG = "BatterySensor";

	private static BatterySensor batterySensor;
	private static Object lock = new Object();

	public static BatterySensor getBatterySensor(Context context) throws ESException
	{
		if (batterySensor == null)
		{
			synchronized (lock)
			{
				if (batterySensor == null)
				{
					if (permissionGranted(context, "android.permission.BATTERY_STATS"))
					{
						batterySensor = new BatterySensor(context);
					}
					else throw new ESException(ESException. PERMISSION_DENIED, "Battery : Permission not Granted");
				}
			}
		}
		return batterySensor;
	}

	private BatterySensor(Context context)
	{
		super(context);
	}

	public String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorList.SENSOR_TYPE_BATTERY;
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

        BatteryData batteryData = new BatteryData(System.currentTimeMillis(), level, scale, temp, voltage, plugged, status, health);
        onDataSensed(batteryData);
	}

	protected IntentFilter[] getIntentFilters()
	{
		IntentFilter[] filters = new IntentFilter[1];
		filters[0] = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		return filters;
	}

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		// nothing to do
		return true;
	}

	protected void stopSensing()
	{
		// nothing to do
	}

}
