/*
 * For logging to files other than the Android logfile
 */

package com.ubhave.sensormanager.logs;

import android.util.Log;

public class ESLogger
{

	public static void log(String TAG, String message)
	{
		Log.d(TAG, message);
	}

	public static void error(String TAG, String message)
	{
		Log.e(TAG, message);
	}

	public static void error(String TAG, Exception exp)
	{
		exp.printStackTrace();
		error(TAG, Log.getStackTraceString(exp));
	}

}
