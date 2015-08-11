package com.ubhave.sensormanager.process.pull;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.StepCounterData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class StepCounterProcessor extends AbstractProcessor
{
	public StepCounterProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}
	
	public StepCounterData process(final long senseStartTime, final float numSteps, final long lastBoot, final SensorConfig config)
	{
		StepCounterData data = new StepCounterData(senseStartTime, config);
		data.setNumSteps(numSteps);
		data.setLastBoot(lastBoot);
		return data;
	}
}
