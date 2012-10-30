package com.ubhave.sensormanager.config;



public class GlobalConfig extends AbstractConfig
{

	// battery level at which sensing should be enabled/disabled
	public final static String LOW_BATTERY_THRESHOLD = "LOW_BATTERY_THRESHOLD";
	
	
	public static GlobalConfig getDefaultGlobalConfig()
	{
		GlobalConfig config = new GlobalConfig();
		config.setParameter(LOW_BATTERY_THRESHOLD, (Integer) Constants.LOW_BATTERY_THRESHOLD_LEVEL);
		return config;
	}
}
