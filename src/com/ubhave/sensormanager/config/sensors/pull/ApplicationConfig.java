package com.ubhave.sensormanager.config.sensors.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class ApplicationConfig
{
	/*
	 * Default values
	 */
	public static final long DEFAULT_SLEEP_INTERVAL = 5 * 60 * 1000;
	public static final int APPLICATION_SAMPLING_CYCLES = 1; // application sensor sensing cycles should always be 1
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES, APPLICATION_SAMPLING_CYCLES);
		return sensorConfig;
	}
}
