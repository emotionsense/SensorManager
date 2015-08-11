package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.AbstractMotionData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public abstract class AbstractMotionProcessor extends AbstractProcessor
{
	public AbstractMotionProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public AbstractMotionData process(long pullSenseStartTimestamp, ArrayList<float[]> sensorReadings,
			ArrayList<Long> sensorReadingTimestamps, SensorConfig sensorConfig)
	{
		AbstractMotionData data = getInstance(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			data.setSensorReadings(sensorReadings);
			data.setSensorReadingTimestamps(sensorReadingTimestamps);
		}
		if (setProcessedData)
		{
			processData(sensorReadings, sensorReadingTimestamps, data);
		}
		return data;
	}
	
	protected abstract AbstractMotionData getInstance(long pullSenseStartTimestamp, SensorConfig sensorConfig);
	
	protected abstract void processData(ArrayList<float[]> sensorReadings, ArrayList<Long> sensorReadingTimestamps, AbstractMotionData data);
}
