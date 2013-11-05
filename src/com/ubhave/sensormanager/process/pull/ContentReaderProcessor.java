package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.ContentReaderData;
import com.ubhave.sensormanager.data.pullsensor.ContentReaderResult;
import com.ubhave.sensormanager.process.CommunicationProcessor;

public abstract class ContentReaderProcessor extends CommunicationProcessor
{
	public ContentReaderProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public ContentReaderData process(long pullSenseStartTimestamp, int sensorType, ArrayList<HashMap<String, String>> contentList, SensorConfig sensorConfig)
	{
		ContentReaderData contentData = getData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			for (HashMap<String, String> map : contentList)
			{
				ContentReaderResult entry = getEntry(map);
				if (entry != null)
				{
					contentData.addContent(entry);
				}
			}
		}
		return contentData;
	}
	
	protected abstract ContentReaderData getData(long senseStartTime, SensorConfig config);
	
	protected abstract ContentReaderResult getEntry(final HashMap<String, String> map);
}
