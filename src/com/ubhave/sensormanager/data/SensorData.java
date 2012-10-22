package com.ubhave.sensormanager.data;

public abstract class SensorData
{
	// for pull sensor: this is the time at which sensing cycle is started
	// for push sensor: this is the time at which data is received by the sensor
	private final long sensorDataTimestamp;

	private SensorData prevSensorData;

	public abstract String getDataString();
	
	public abstract int getSensorType();

	public SensorData(long sensorTimestamp)
	{
		sensorDataTimestamp = sensorTimestamp;
	}

	public long getTimestamp()
	{
		return sensorDataTimestamp;
	}

	public SensorData getPrevSensorData()
	{
		return prevSensorData;
	}

	public void setPrevSensorData(SensorData prevSensorData)
	{
		// when a sensordata object is set as prev to another sensordata
		// object then its prev should be set to null to avoid memory leaks
		if (prevSensorData != null)
		{
			prevSensorData.setPrevSensorData(null);
		}

		this.prevSensorData = prevSensorData;
	}

	public String toString()
	{
		return sensorDataTimestamp + " " + getDataString();
	}

}
