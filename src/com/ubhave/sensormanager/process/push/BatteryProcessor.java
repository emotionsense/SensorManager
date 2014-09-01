package com.ubhave.sensormanager.process.push;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.BatteryData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class BatteryProcessor extends AbstractProcessor
{
	public BatteryProcessor(Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public BatteryData process(long recvTime, SensorConfig config, Intent dataIntent)
	{
		BatteryData data = new BatteryData(recvTime, config);
		if (setRawData)
		{
			int level = dataIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			data.setBatteryLevel(level);

			int scale = dataIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			data.setScale(scale);

			int temp = dataIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
			data.setTemperature(temp);

			int voltage = dataIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
			data.setVoltage(voltage);

			int plugged = dataIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			data.setPlugged(plugged);

			int status = dataIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			data.setStatus(status);

			int health = dataIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
			data.setHealth(health);
		}
		return data;
	}

}
