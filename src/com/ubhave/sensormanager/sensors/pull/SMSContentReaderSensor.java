package com.ubhave.sensormanager.sensors.pull;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SMSContentReaderSensor extends ContentReaderSensor
{
	private static final String LOG_TAG = "SMSContentReaderSensor";
	private static SMSContentReaderSensor smsContentReaderSensor;

	public static SMSContentReaderSensor getSMSContentReaderSensor(Context context) throws ESException
	{
		if (smsContentReaderSensor == null)
		{
			synchronized (lock)
			{
				if (smsContentReaderSensor == null)
				{
					if (permissionGranted(context, "android.permission.READ_SMS"))
					{
						smsContentReaderSensor = new SMSContentReaderSensor(context);
					}
					else
						throw new ESException(ESException.PERMISSION_DENIED,
								"SMSContentReaderSensor : Permission not Granted");
				}
			}
		}
		return smsContentReaderSensor;
	}

	private SMSContentReaderSensor(Context context)
	{
		super(context);
	}

	@Override
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER;
	}

	@Override
	protected String getContentURL()
	{
		return "content://sms";
	}

	@Override
	protected String[] getContentKeysArray()
	{
		return new String[] { "address", "type", "date", "body" };
	}

	@Override
	protected String getLogTag()
	{
		return LOG_TAG;
	}

}
