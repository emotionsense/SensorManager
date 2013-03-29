package com.ubhave.sensormanager.process.push;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.ProximityData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ProximityProcessor extends AbstractProcessor
{
	public ProximityProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}

	public ProximityData process(long recvTime, SensorConfig config, float distance, float maxRange)
	{
		ProximityData data = new ProximityData(recvTime, config);

		if (setRawData)
		{
			data.setDistance(distance);
			data.setMaxRange(maxRange);
		}
		
		if (setProcessedData)
		{
			// process
		}
		
		return data;
	}

}
