package com.ubhave.sensormanager.config.sensor.push;

import com.ubhave.sensormanager.config.SensorConfig;

public class PassiveLocationConfig {
	// minimum time in Millisecond between two location updates
	public static final String MIN_TIME = "min_time";
	// Minimum distance in meter between two location updates
	public static final String MIN_DISTANCE = "min_distance";
	/*
	 * Default values
	 */
	public static final long DEFAULT_MIN_TIME = 10 * 1000;
	public static final float DEFAULT_MIN_DISTANCE = 10.0f;

	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(MIN_TIME, DEFAULT_MIN_TIME);
		sensorConfig.setParameter(MIN_DISTANCE, DEFAULT_MIN_DISTANCE);
		return sensorConfig;
	}

}
