package com.ubhave.sensormanager.config.sensors.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class MicrophoneConfig
{
	/*
	 * Config Keys
	 */
	public static final String AUDIO_FILES_DIRECTORY = "AUDIO_FILES_DIRECTORY";
	public static final String SAMPLING_RATE = "SAMPLING_RATE";
	
	/*
	 * Default values
	 */
	public static final long DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS = 5000;
	public static final long DEFAULT_SLEEP_INTERVAL = 2 * 60 * 1000;
	public static final int MICROPHONE_SOUND_THRESHOLD = 800;
	public static final int DEFAULT_SAMPLING_RATE = 20000;
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS);
		sensorConfig.setParameter(SAMPLING_RATE, DEFAULT_SAMPLING_RATE);
		sensorConfig.setParameter(AUDIO_FILES_DIRECTORY, null);
		return sensorConfig;
	}
}
