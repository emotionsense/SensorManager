package com.ubhave.sensormanager.data.pullsensor;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MicrophoneData extends SensorData
{
	private final String amplitudeString;

	public MicrophoneData(long senseStartTimestamp, String amplitudeString)
	{
		super(senseStartTimestamp);
		this.amplitudeString = amplitudeString;
	}

	public String getAmplitudeString()
	{
		return amplitudeString;
	}
	
	public String getDataString() 
	{
		return amplitudeString;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_MICROPHONE;
	}
}
