package com.ubhave.sensormanager.config;

import java.util.HashMap;

import com.ubhave.sensormanager.ESException;

public class SensorConfig
{

	public final static String SENSOR_NAME = "SENSOR_NAME";
	public final static String SENSOR_ENABLED = "SENSOR_ENABLED";
	public final static String SENSOR_ENABLED_VALUE = "yes";

	public final static String MIC_RECORDER = "MIC_RECORDER";
	public final static String MIC_RECORDER_VALUE_MEDIA_RECORDER = "MIC_RECORDER_VALUE_MEDIA_RECORDER";
	public final static String MIC_RECORDER_VALUE_AUDIO_RECORD = "MIC_RECORDER_VALUE_AUDIO_RECORD";

	// sampling window size sets the data capture duration from the sensor, like
	// accelerometer sampling window
	public final static String SAMPLING_WINDOW_SIZE_IN_MILLIS = "SAMPLING_WINDOW_SIZE_IN_MILLIS";
	// number of sampling cycles sets the number of times a sensor samples the
	// data, and this is relevan for sensors like Bluetooth, Wifi, where there
	// is no fixed sampling window and the amount of sampling time
	// depends on the number of devices in the environment. the no. of cylces
	// sets the number of scans (wifi or bluetooth) to be performed
	public final static String NUMBER_OF_SAMPLING_CYCLES = "NUMBER_OF_SAMPLING_CYCLES";

	// this is the sleep interval between two consecutive sensor samplings
	public final static String SENSOR_SLEEP_INTERVAL = "SENSOR_SLEEP_INTERVAL";

	private HashMap<String, Object> configParams;

	public SensorConfig()
	{
		configParams = new HashMap<String, Object>();
	}

	public void set(String parameterName, Object parameterValue)
	{
		configParams.put(parameterName, parameterValue);
	}

	public Object get(String parameterName)
	{
		Object parameterValue = null;
		if (configParams.containsKey(parameterName))
		{
			parameterValue = configParams.get(parameterName);
		}
		return parameterValue;
	}

	public boolean containsParameter(String parameterName)
	{
		if (configParams.containsKey(parameterName))
		{
			return true;
		}
		return false;
	}

	public boolean isSensorEnabled()
	{
		if (containsParameter(SENSOR_ENABLED))
		{
			if (get(SENSOR_ENABLED).equals(SENSOR_ENABLED_VALUE))
			{
				return true;
			}
		}
		return false;
	}

	public static int getSensorType(String sensorName) throws ESException
	{
		if (sensorName.equals(Constants.SENSOR_NAME_ACCELEROMETER))
		{
			return Constants.SENSOR_TYPE_ACCELEROMETER;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_BATTERY))
		{
			return Constants.SENSOR_TYPE_BATTERY;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_BLUETOOTH))
		{
			return Constants.SENSOR_TYPE_BLUETOOTH;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_LOCATION))
		{
			return Constants.SENSOR_TYPE_LOCATION;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_MICROPHONE))
		{
			return Constants.SENSOR_TYPE_MICROPHONE;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_PHONE_STATE))
		{
			return Constants.SENSOR_TYPE_PHONE_STATE;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_PROXIMITY))
		{
			return Constants.SENSOR_TYPE_PROXIMITY;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_SCREEN))
		{
			return Constants.SENSOR_TYPE_SCREEN;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_SMS))
		{
			return Constants.SENSOR_TYPE_SMS;
		}
		else if (sensorName.equals(Constants.SENSOR_NAME_WIFI))
		{
			return Constants.SENSOR_TYPE_WIFI;
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_NAME, "unknown sensor name " + sensorName);
		}

	}

}
