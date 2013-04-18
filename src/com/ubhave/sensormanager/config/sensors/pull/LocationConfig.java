package com.ubhave.sensormanager.config.sensors.pull;

public class LocationConfig
{
	public final static String ACCURACY_TYPE = "LOCATION_ACCURACY";
	public final static String LOCATION_ACCURACY_COARSE = "LOCATION_ACCURACY_COARSE";
	public final static String LOCATION_ACCURACY_FINE = "LOCATION_ACCURACY_FINE";
	
	public static final long DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS = 60000;
	public static final long DEFAULT_SLEEP_INTERVAL = 15 * 60 * 1000;
	public final static int LOCATION_CHANGE_DISTANCE_THRESHOLD = 100;
}
