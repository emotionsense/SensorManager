package com.ubhave.sensormanager.process.pull;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class AudioProcessor extends AbstractProcessor
{
	public AudioProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public MicrophoneData process(long pullSenseStartTimestamp, int[] maxAmpArray, long[] timestampArray, SensorConfig sensorConfig)
	{
		MicrophoneData micData = new MicrophoneData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			micData.setMaxAmplitudeArray(maxAmpArray);
			micData.setTimestampArray(timestampArray);
		}
		return micData;

	}

}
