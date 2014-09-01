package com.ubhave.sensormanager.process.push;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.ProximityData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ProximityProcessor extends AbstractProcessor
{
	public ProximityProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public ProximityData process(long recvTime, SensorConfig config, float distance, float maxRange)
	{
		ProximityData data = new ProximityData(recvTime, config);
		if (setRawData)
		{
			data.setDistance(distance);
			data.setMaxRange(maxRange);
		}
		return data;
	}

}
