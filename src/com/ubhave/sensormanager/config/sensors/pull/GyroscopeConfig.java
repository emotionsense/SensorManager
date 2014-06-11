package com.ubhave.sensormanager.config.sensors.pull;

import android.hardware.SensorManager;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.sensors.pull.PullSensorConfig;

public class GyroscopeConfig
{
	/*
	 * Config keys
	 */
	public final static String SAMPLING_DELAY = "GYROSCOPE_SAMPLING_DELAY";
	
	/*
	 * Default values
	 */
	public static final long DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS = 8000L;
	public static final long DEFAULT_SLEEP_INTERVAL = 2 * 60 * 1000L;
	public static final int DEFAULT_SAMPLING_DELAY = SensorManager.SENSOR_DELAY_GAME;

	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(GyroscopeConfig.SAMPLING_DELAY, DEFAULT_SAMPLING_DELAY);
		sensorConfig.setParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS);
		return sensorConfig;
	}
}
