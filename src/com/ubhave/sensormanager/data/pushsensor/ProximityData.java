package com.ubhave.sensormanager.data.pushsensor;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ProximityData extends SensorData
{
	private static final float NEAR_DISTANCE = (float) 0.0;
	
	private float distance;
	private float maxRange;

	public ProximityData(long recvTimestamp, float distance, float maxRange)
	{
		super(recvTimestamp);
		this.distance = distance;
		this.maxRange = maxRange;
	}
	
	public boolean isNear()
	{
		if (distance == NEAR_DISTANCE)
		{
			return true;
		}
		return false;
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
