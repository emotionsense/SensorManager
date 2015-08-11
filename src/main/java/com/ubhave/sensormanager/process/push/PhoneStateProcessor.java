package com.ubhave.sensormanager.process.push;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.PhoneStateData;
import com.ubhave.sensormanager.process.CommunicationProcessor;

public class PhoneStateProcessor extends CommunicationProcessor
{
	public PhoneStateProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public PhoneStateData process(long timestamp, SensorConfig config, int event, String details, String number)
	{
		PhoneStateData data = new PhoneStateData(timestamp, config);
		if (setRawData)
		{
			data.setEventType(event);
			data.setData(details);
			if (number != null)
			{
				number = hashPhoneNumber(number);
				data.setNumber(number);
			}
		}
		return data;
	}
}
