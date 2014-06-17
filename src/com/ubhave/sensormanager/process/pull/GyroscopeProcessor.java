package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.GyroscopeData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class GyroscopeProcessor extends AbstractProcessor
{
	public GyroscopeProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public GyroscopeData process(long pullSenseStartTimestamp, ArrayList<float[]> sensorReadings,
			ArrayList<Long> sensorReadingTimestamps, SensorConfig sensorConfig)
	{
		GyroscopeData gyroscopeData = new GyroscopeData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
            gyroscopeData.setSensorReadings(sensorReadings);
            gyroscopeData.setSensorReadingTimestamps(sensorReadingTimestamps);
		}
        return gyroscopeData;
	}
}
