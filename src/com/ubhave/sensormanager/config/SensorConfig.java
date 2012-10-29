package com.ubhave.sensormanager.config;

import java.util.HashMap;

public class SensorConfig implements Cloneable
{

	// sampling window size sets the data capture duration from the sensor, like
	// accelerometer sampling window
	public final static String SENSE_WINDOW_LENGTH_MILLIS = "SENSE_WINDOW_LENGTH_MILLIS";

	// number of sampling cycles sets the number of times a sensor samples the
	// data, and this is relevant for sensors like Bluetooth, Wifi, where there
	// is no fixed sampling window and the amount of sampling time
	// depends on the number of devices in the environment. the no. of cycles
	// sets the number of scans (wifi or bluetooth) to be performed
	public final static String NUMBER_OF_SENSE_CYCLES = "NUMBER_OF_SENSE_CYCLES";

	// this is the sleep interval between two consecutive sensor samplings
	public final static String POST_SENSE_SLEEP_LENGTH_MILLIS = "POST_SENSE_SLEEP_LENGTH_MILLIS";
	
	// location accuracy
	public final static String LOCATION_ACCURACY = "LOCATION_ACCURACY";
	public final static String LOCATION_ACCURACY_COARSE = "LOCATION_ACCURACY_COARSE";
	public final static String LOCATION_ACCURACY_FINE = "LOCATION_ACCURACY_FINE";
	
	// battery level at which sensing should be enabled/disabled
	public final static String LOW_BATTERY_THRESHOLD = "LOW_BATTERY_THRESHOLD";

	private HashMap<String, Object> configParams;

	public SensorConfig()
	{
		configParams = new HashMap<String, Object>();
	}

	public void setParameter(String parameterName, Object parameterValue)
	{
		configParams.put(parameterName, parameterValue);
	}

	public Object getParameter(String parameterName)
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
	
	public void removeParameter(String parameterName)
	{
		if (configParams.containsKey(parameterName))
		{
			configParams.remove(parameterName);
		}
	}

	public SensorConfig clone()
	{
		SensorConfig clonedSensorConfig = new SensorConfig();
		for (String key : configParams.keySet())
		{
			Object obj = configParams.get(key);
			clonedSensorConfig.setParameter(key, obj);
		}

		return clonedSensorConfig;
	}

}
