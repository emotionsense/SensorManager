/*
 * For logging to files other than the Android logfile
 */

package com.ubhave.sensormanager.logs;

import android.util.Log;

public class ESLogger
{
	private static final String ERROR_TAG = "ERROR";

	public static void log(String TAG, String message)
	{
		Log.d(TAG, message);
	}

	public static void error(String TAG, String message)
	{
		Log.e(TAG, message);		
		// TODO
//		DataLogger.getDataLogger().logData(ERROR_TAG, TAG + " " + message);
	}

	public static void error(String TAG, Exception exp)
	{
		error(TAG, Log.getStackTraceString(exp));
	}

}
