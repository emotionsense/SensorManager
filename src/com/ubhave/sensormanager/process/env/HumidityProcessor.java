package com.ubhave.sensormanager.process.env;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.env.HumidityData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class HumidityProcessor extends AbstractProcessor
{
	public HumidityProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public HumidityData process(long recvTime, SensorConfig config, float humidity)
	{
		HumidityData data = new HumidityData(recvTime, config);
		if (setRawData)
		{
			data.setValue(humidity);
		}
		return data;
	}
}
