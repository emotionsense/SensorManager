package com.ubhave.sensormanager.sensors.pull;

import android.content.Context;
import android.provider.CallLog;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class CallContentReaderSensor extends ContentReaderSensor
{
	private static final String LOG_TAG = "CallContentReaderSensor";
	private static CallContentReaderSensor callContentReaderSensor;

	public static CallContentReaderSensor getCallContentReaderSensor(Context context) throws ESException
	{
		if (callContentReaderSensor == null)
		{
			synchronized (lock)
			{
				if (callContentReaderSensor == null)
				{
					if (permissionGranted(context, "android.permission.READ_CALL_LOG "))
					{
						callContentReaderSensor = new CallContentReaderSensor(context);
					}
					else
						throw new ESException(ESException.PERMISSION_DENIED,
								"CallContentReaderSensor : Permission not Granted");
				}
			}
		}
		return callContentReaderSensor;
	}

	private CallContentReaderSensor(Context context)
	{
		super(context);
	}

	@Override
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER;
	}

	@Override
	protected String getContentURL()
	{
		return "content://call_log/calls";
	}

	@Override
	protected String[] getContentKeysArray()
	{
		return new String[] { CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE };
	}

	@Override
	protected String getLogTag()
	{
		return LOG_TAG;
	}

}
