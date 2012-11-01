package com.ubhave.sensormanager.config;


public class GlobalConfig extends AbstractConfig
{

	// battery level at which sensing should be enabled/disabled
	public final static String LOW_BATTERY_THRESHOLD = "LOW_BATTERY_THRESHOLD";

	// acquire wake lock
	public final static String ACQUIRE_WAKE_LOCK = "ACQUIRE_WAKE_LOCK";

	private static GlobalConfig globalConfig;
	private static final Object lock = new Object();

	public static GlobalConfig getGlobalConfig()
	{
		if (globalConfig == null)
		{
			synchronized (lock)
			{
				if (globalConfig == null)
				{
					globalConfig = getDefaultGlobalConfig();
				}
			}
		}
		return globalConfig;
	}

	private static GlobalConfig getDefaultGlobalConfig()
	{
		GlobalConfig config = new GlobalConfig();
		config.setParameter(LOW_BATTERY_THRESHOLD, (Integer) Constants.LOW_BATTERY_THRESHOLD_LEVEL);
		return config;
	}

}
