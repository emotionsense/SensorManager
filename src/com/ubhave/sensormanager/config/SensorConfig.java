package com.ubhave.sensormanager.config;

import java.util.HashMap;

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
	// data, and this is relevant for sensors like Bluetooth, Wifi, where there
	// is no fixed sampling window and the amount of sampling time
	// depends on the number of devices in the environment. the no. of cycles
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

}
