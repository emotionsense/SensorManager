package com.ubhave.sensormanager.config.sensors.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class CameraConfig
{
	/*
	 * Config Keys
	 */
	public static final String IMAGE_FILES_DIRECTORY = "IMAGE_FILES_DIRECTORY";
	public static final String CAMERA_TYPE = "CAMERA_TYPE";
	
	/*
	 * Config Values
	 */
	public static final int CAMERA_TYPE_FRONT = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
	public static final int CAMERA_TYPE_BACK = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
	
	/*
	 * Default values
	 */
	public static final long DEFAULT_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final int CAMERA_SAMPLING_CYCLES = 1; // camera sensor sensing cycles  should not be changed from 1
	public static final int DEFAULT_CAMERA_TYPE = CAMERA_TYPE_BACK;
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES, CAMERA_SAMPLING_CYCLES);
		sensorConfig.setParameter(CameraConfig.CAMERA_TYPE, DEFAULT_CAMERA_TYPE);
		sensorConfig.setParameter(IMAGE_FILES_DIRECTORY, null);
		return sensorConfig;
	}
}
