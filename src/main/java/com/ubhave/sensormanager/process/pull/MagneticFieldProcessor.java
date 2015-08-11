package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.AbstractMotionData;
import com.ubhave.sensormanager.data.pull.MagneticFieldData;

public class MagneticFieldProcessor extends AbstractMotionProcessor
{
	public MagneticFieldProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	@Override
	protected AbstractMotionData getInstance(long pullSenseStartTimestamp, SensorConfig sensorConfig)
	{
		return new MagneticFieldData(pullSenseStartTimestamp, sensorConfig);
	}

	@Override
	protected void processData(ArrayList<float[]> sensorReadings, ArrayList<Long> sensorReadingTimestamps, AbstractMotionData data)
	{
		// Future: feature extraction
	}
}
