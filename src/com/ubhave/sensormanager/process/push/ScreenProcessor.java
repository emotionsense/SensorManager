package com.ubhave.sensormanager.process.push;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.ScreenData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ScreenProcessor extends AbstractProcessor
{
	
	public ScreenProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}
	
	public ScreenData process(long timestamp, SensorConfig config, int status)
	{
		ScreenData data = new ScreenData(timestamp, config);
		data.setStatus(status);
		return data;
	}

}
