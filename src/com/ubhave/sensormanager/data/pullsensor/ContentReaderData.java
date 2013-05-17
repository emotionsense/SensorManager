package com.ubhave.sensormanager.data.pullsensor;

import java.util.HashMap;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;

public class ContentReaderData extends SensorData
{
	private final HashMap<String, String> contentMap;
	private final int sensorType;

	public ContentReaderData(long sensorTimestamp, HashMap<String, String> contentMap, int sensorType, SensorConfig config)
	{
		super(sensorTimestamp, config);
		this.contentMap = contentMap;
		this.sensorType = sensorType;
	}

	public HashMap<String, String> getContentMap()
	{
		return contentMap;
	}

	@Override
	public int getSensorType()
	{
		return sensorType;
	}

}
