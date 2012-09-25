package com.ubhave.sensormanager.sensors;

import android.content.Context;
import android.content.pm.PackageManager;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;

public abstract class AbstractSensor implements SensorInterface
{

	protected boolean isSensing;
	protected final Context applicationContext;
	protected final Object senseCompleteNotify;
	
	public AbstractSensor(Context context)
	{
		applicationContext = context;
		senseCompleteNotify = new Object();
	}
	
	public static boolean permissionGranted(Context context, String permission)
	{
		return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}

	protected abstract boolean startSensing(SensorConfig sensorConfig);

	protected abstract void stopSensing();

	protected abstract String getLogTag();

	public boolean isSensing()
	{
		return isSensing;
	}

	public static SensorConfig getDefaultSensorConfig(int sensorType)
	{
		SensorConfig sensorConfig = new SensorConfig();
		switch (sensorType)
		{
		case Constants.SENSOR_TYPE_ACCELEROMETER:
			sensorConfig.set(SensorConfig.SENSOR_SLEEP_INTERVAL, Constants.ACCELEROMETER_SLEEP_INTERVAL);
			sensorConfig.set(SensorConfig.SAMPLING_WINDOW_SIZE_IN_MILLIS, Constants.ACCELEROMETER_SAMPLING_WINDOW_SIZE_MILLIS);
			break;
		case Constants.SENSOR_TYPE_BLUETOOTH:
			sensorConfig.set(SensorConfig.SENSOR_SLEEP_INTERVAL, Constants.BLUETOOTH_SLEEP_INTERVAL);
			sensorConfig.set(SensorConfig.NUMBER_OF_SAMPLING_CYCLES, Constants.BLUETOOTH_SAMPLING_CYCLES);
			break;
		case Constants.SENSOR_TYPE_LOCATION:
			sensorConfig.set(SensorConfig.SENSOR_SLEEP_INTERVAL, Constants.LOCATION_SLEEP_INTERVAL);
			sensorConfig.set(SensorConfig.SAMPLING_WINDOW_SIZE_IN_MILLIS, Constants.LOCATION_SAMPLING_WINDOW_SIZE_MILLIS);
			break;
		case Constants.SENSOR_TYPE_MICROPHONE:
			sensorConfig.set(SensorConfig.SENSOR_SLEEP_INTERVAL, Constants.MICROPHONE_SLEEP_INTERVAL);
			sensorConfig.set(SensorConfig.MIC_RECORDER, SensorConfig.MIC_RECORDER_VALUE_AUDIO_RECORD);
			sensorConfig.set(SensorConfig.SAMPLING_WINDOW_SIZE_IN_MILLIS, Constants.MICROPHONE_SAMPLING_WINDOW_SIZE_MILLIS);
			break;
		case Constants.SENSOR_TYPE_WIFI:
			sensorConfig.set(SensorConfig.SENSOR_SLEEP_INTERVAL, Constants.WIFI_SLEEP_INTERVAL);
			sensorConfig.set(SensorConfig.NUMBER_OF_SAMPLING_CYCLES, Constants.WIFI_SAMPLING_CYCLES);
			break;
		}
		return sensorConfig;
	}

	public static String getSensorName(int sensorType)
	{
		switch (sensorType)
		{
		case Constants.SENSOR_TYPE_ACCELEROMETER:
			return "Accelerometer";
		case Constants.SENSOR_TYPE_BATTERY:
			return "Battery";
		case Constants.SENSOR_TYPE_BLUETOOTH:
			return "Bluetooth";
		case Constants.SENSOR_TYPE_LOCATION:
			return "Location";
		case Constants.SENSOR_TYPE_MICROPHONE:
			return "Microphone";
		case Constants.SENSOR_TYPE_PHONE_STATE:
			return "PhoneState";
		case Constants.SENSOR_TYPE_PROXIMITY:
			return "Proximity";
		case Constants.SENSOR_TYPE_SCREEN:
			return "Screen";
		case Constants.SENSOR_TYPE_SMS:
			return "SMS";
		case Constants.SENSOR_TYPE_WIFI:
			return "Wifi";
		default:
			return "Unknown";
		}
	}

}
