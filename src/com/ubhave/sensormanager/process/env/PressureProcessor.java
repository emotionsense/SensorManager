package com.ubhave.sensormanager.process.env;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.env.PressureData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class PressureProcessor extends AbstractProcessor
{
	public PressureProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public PressureData process(long recvTime, SensorConfig config, float pressure)
	{
		return new PressureData(recvTime, config, pressure);
	}
}
