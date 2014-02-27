package com.ubhave.sensormanager.config.sensors.pull;

public class MicrophoneConfig
{
	public static final String KEEP_AUDIO_FILES = "KEEP_AUDIO_FILES";
	public static final String SAMPLING_RATE = "SAMPLING_RATE";
	/*
	 * Default values for microphone sensing
	 */
	public static final long DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS = 5000;
	public static final long DEFAULT_SLEEP_INTERVAL = 2 * 60 * 1000;
	public static final int MICROPHONE_SOUND_THRESHOLD = 800;
	public static final boolean DEFAULT_KEEP_AUDIO_FILES = false;
	public static final int DEFAULT_SAMPLING_RATE = 20000;
}
