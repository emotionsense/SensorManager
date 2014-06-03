package com.ubhave.sensormanager.config.sensors.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class PhoneRadioConfig
{
	/*
	 * Default values
	 */
	public static final long DEFAULT_SLEEP_INTERVAL = 1 * 60 * 1000;
	public static final int PHONE_RADIO_SAMPLING_CYCLES = 1; // phone radio sensor sensing cycles should always be 1
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES, PHONE_RADIO_SAMPLING_CYCLES);
		return sensorConfig;
	}
}
