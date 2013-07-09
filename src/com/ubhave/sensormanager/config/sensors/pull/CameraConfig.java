package com.ubhave.sensormanager.config.sensors.pull;

public class CameraConfig
{
	/*
	 * Default values
	 */
	public static final long DEFAULT_SLEEP_INTERVAL = 15 * 60 * 1000;
	// camera sensor sensing cycles // this should always be 1
	public static final int CAMERA_SAMPLING_CYCLES = 1;
	
	public static final String CAMERA_TYPE = "CAMERA_TYPE";
	
	public static final int CAMERA_TYPE_FRONT = 1001;
	public static final int CAMERA_TYPE_BACK = 1002;
	
}
