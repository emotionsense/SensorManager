package com.ubhave.sensormanager.config.sensors.push;

import android.hardware.SensorManager;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.sensors.pull.AccelerometerConfig;
import com.ubhave.sensormanager.config.sensors.pull.PullSensorConfig;

public class BatteryConfig
{

	// low battery threshold
	public static final int LOW_BATTERY_THRESHOLD_LEVEL = 20;
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, AccelerometerConfig.DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(AccelerometerConfig.SAMPLING_DELAY, SensorManager.SENSOR_DELAY_GAME);
		sensorConfig.setParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, AccelerometerConfig.DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS);
		return sensorConfig;
	}
	
}
