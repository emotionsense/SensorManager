package com.ubhave.sensormanager.data.pullsensor;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;

public class GyroscopeData extends SensorData
{
	private ArrayList<float[]> sensorReadings;
	private ArrayList<Long> sensorReadingTimestamps;

	public GyroscopeData(long senseStartTimestamp, SensorConfig sensorConfig)
	{
		super(senseStartTimestamp, sensorConfig);
	}
	
	public void setSensorReadings(ArrayList<float[]> sensorReadings)
	{
		this.sensorReadings = sensorReadings;
	}

	public ArrayList<float[]> getSensorReadings()
	{
		return sensorReadings;
	}
	
	public void setSensorReadingTimestamps(ArrayList<Long> sensorReadingTimestamps)
	{
		this.sensorReadingTimestamps = sensorReadingTimestamps;
	}
	
	public ArrayList<Long> getSensorReadingTimestamps()
	{
		return sensorReadingTimestamps;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_GYROSCOPE;
	}

}
