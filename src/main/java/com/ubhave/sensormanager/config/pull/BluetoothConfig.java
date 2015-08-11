package com.ubhave.sensormanager.config.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class BluetoothConfig
{
	public static final String FORCE_ENABLE_SENSOR = "FORCE_ENABLE";
	
	/*
	 * Default values
	 */
	public static final long DEFAULT_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final int DEFAULT_SAMPLING_WINDOW_SIZE_PER_CYCLE_MILLIS = 12000;
	public static final int DEFAULT_SAMPLING_CYCLES = 1;
	public static final boolean DEFAULT_FORCE_ENABLE = false;
	
	/*
	 *  Note:
	 *  Bluetooth is operated in terms of number of sampling cycles
	 *  but adaptive sensing requires a specification in terms of
	 *  sampling window length, so define a constant that contains
	 *  an approximation of the sampling window length
	 */
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(FORCE_ENABLE_SENSOR, DEFAULT_FORCE_ENABLE);
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES, DEFAULT_SAMPLING_CYCLES);
		sensorConfig.setParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_PER_CYCLE_MILLIS, DEFAULT_SAMPLING_WINDOW_SIZE_PER_CYCLE_MILLIS);
		return sensorConfig;
	}
}
