package com.ubhave.sensormanager.process.pull;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.CameraData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class CameraProcessor extends AbstractProcessor
{
	public CameraProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public CameraData process(long pullSenseStartTimestamp, String imageFullPath, SensorConfig sensorConfig)
	{
		CameraData camData = new CameraData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			camData.setImageFullPath(imageFullPath);
		}
		return camData;
	}

}
