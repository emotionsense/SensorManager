package com.ubhave.sensormanager.process.push;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.LightData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class LightProcessor extends AbstractProcessor
{
	public LightProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public LightData process(long recvTime, SensorConfig config, float light, float maxRange)
	{
        LightData data = new LightData(recvTime, config);
		if (setRawData)
		{
            data.setLight(light);
			data.setMaxRange(maxRange);
		}
		return data;
	}
}
