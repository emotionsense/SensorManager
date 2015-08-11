package com.ubhave.sensormanager.data.log;

import java.util.HashMap;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class InteractionData extends SensorData
{
	private final HashMap<String, String> values;
	
	public InteractionData(final long timestamp, final HashMap<String, String> values)
	{
		super(timestamp, null);
		this.values = values;
	}
	
	public InteractionData(final HashMap<String, String> values)
	{
		this(System.currentTimeMillis(), values);
	}
	
	@Override
	public final int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_INTERACTION;
	}
	
	public HashMap<String, String> getValues()
	{
		return values;
	}
}
