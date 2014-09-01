package com.ubhave.sensormanager.process.env;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.env.AmbientTemperatureData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class AmbientTemperatureProcessor extends AbstractProcessor
{
	public AmbientTemperatureProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public AmbientTemperatureData process(long recvTime, SensorConfig config, float temperature)
	{
		return new AmbientTemperatureData(recvTime, config, temperature);
	}
}
