package com.ubhave.sensormanager.config.sensors.pull;

public class AccelerometerConfig
{
	/*
	 * Config keys
	 */
	public final static String SAMPLING_DELAY = "ACCELEROMETER_SAMPLING_DELAY";
	
	/*
	 * Default values
	 */
	public static final long DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS = 8000;
	public static final long DEFAULT_SLEEP_INTERVAL = 2 * 60 * 1000;
	
	/*
	 * Classifier thresholds
	 */
	public final static int ACCELEROMETER_MOVEMENT_THRESHOLD = 25;
}
