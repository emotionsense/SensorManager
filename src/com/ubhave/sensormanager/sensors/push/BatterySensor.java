/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.pushsensor.BatteryData;
import com.ubhave.sensormanager.sensors.SensorUtils;

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
		return SensorUtils.SENSOR_TYPE_BATTERY;
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

        BatteryData batteryData = new BatteryData(System.currentTimeMillis(), level, scale, temp, voltage, plugged, status, health, sensorConfig.clone());
        onDataSensed(batteryData);
	}

	protected IntentFilter[] getIntentFilters()
	{
		IntentFilter[] filters = new IntentFilter[1];
		filters[0] = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		return filters;
	}

	protected boolean startSensing()
	{
		// nothing to do
		return true;
	}

	protected void stopSensing()
	{
		// nothing to do
	}

}
