package com.ubhave.sensormanager.config.sensors.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class ApplicationConfig
{
	/*
	 * Default values
	 */
	public static final long DEFAULT_SLEEP_INTERVAL = 5 * 60 * 1000;
	// application sensor sensing cycles // this should always be 1
	public static final int APPLICATION_SAMPLING_CYCLES = 1;
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, ApplicationConfig.DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES, ApplicationConfig.APPLICATION_SAMPLING_CYCLES);
		return sensorConfig;
	}
}
