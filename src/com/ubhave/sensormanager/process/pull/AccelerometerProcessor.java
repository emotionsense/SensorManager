package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.AccelerometerData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class AccelerometerProcessor extends AbstractProcessor
{

	public AccelerometerProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}

	public AccelerometerData process(long pullSenseStartTimestamp, ArrayList<float[]> sensorReadings,
			ArrayList<Long> sensorReadingTimestamps, SensorConfig sensorConfig)
	{
		AccelerometerData accelerometerData = new AccelerometerData(pullSenseStartTimestamp, sensorConfig);

		if (setRawData)
		{
			accelerometerData.setSensorReadings(sensorReadings);
			accelerometerData.setSensorReadingTimestamps(sensorReadingTimestamps);
		}

		if (setProcessedData)
		{
			// process
		}

		return accelerometerData;
	}

}
