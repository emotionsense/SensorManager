package com.ubhave.sensormanager.util;

import com.ubhave.sensormanager.logs.ESLogger;

public class Utilities
{
	private static String TAG = "Utilities";

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (Exception exp)
		{
			ESLogger.error(TAG, exp);
		}
	}

}
