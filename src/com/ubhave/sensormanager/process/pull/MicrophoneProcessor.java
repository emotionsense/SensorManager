package com.ubhave.sensormanager.process.pull;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.MicrophoneData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class MicrophoneProcessor extends AbstractProcessor
{
	public MicrophoneProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public MicrophoneData process(long pullSenseStartTimestamp, int[] maxAmpArray, long[] timestampArray, String mediaFilePath, SensorConfig sensorConfig)
	{
		MicrophoneData micData = new MicrophoneData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			micData.setMaxAmplitudeArray(maxAmpArray);
			micData.setTimestampArray(timestampArray);
			if (mediaFilePath != null)
			{
				micData.setMediaFilePath(mediaFilePath);
			}
		}
		return micData;

	}

}
