package com.ubhave.sensormanager.config;



public class GlobalConfig extends AbstractConfig
{

	// battery level at which sensing should be enabled/disabled
	public final static String LOW_BATTERY_THRESHOLD = "LOW_BATTERY_THRESHOLD";
	
	// whether adaptive sensing is enabled
	public final static String ADAPTIVE_SENSING_ENABLED = "ADAPTIVE_SENSING";
	
	public static GlobalConfig getDefaultGlobalConfig()
	{
		GlobalConfig config = new GlobalConfig();
		config.setParameter(LOW_BATTERY_THRESHOLD, (Integer) Constants.LOW_BATTERY_THRESHOLD_LEVEL);
		config.setParameter(ADAPTIVE_SENSING_ENABLED, false);
		return config;
	}
}
