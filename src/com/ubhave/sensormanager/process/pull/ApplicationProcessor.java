package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.ApplicationData;
import com.ubhave.sensormanager.data.pullsensor.ApplicationDataList;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ApplicationProcessor extends AbstractProcessor
{
	public ApplicationProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public ApplicationDataList process(long pullSenseStartTimestamp, ArrayList<ApplicationData> runningApps, SensorConfig sensorConfig)
	{
		ApplicationDataList appData = new ApplicationDataList(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			appData.setApplications(runningApps);
		}
		return appData;
	}

}
