package com.ubhave.sensormanager.data.pushsensor;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ProximityData extends SensorData
{
	private float distance;
	private float maxRange;

	public ProximityData(long recvTimestamp, float distance, float maxRange)
	{
		super(recvTimestamp);
		this.distance = distance;
		this.maxRange = maxRange;
	}

	public String getDataString()
	{
		return "Distance " + this.distance + " MaxRange " + maxRange;
	}
	
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_PROXIMITY;
	}
}
