package com.ubhave.sensormanager.data.pullsensor;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MicrophoneData extends SensorData
{
	private final String amplitudeString;
	private final long senseWindowLength;

	public MicrophoneData(long senseStartTimestamp, long senseWindowLength, String amplitudeString)
	{
		super(senseStartTimestamp);
		this.amplitudeString = amplitudeString;
		this.senseWindowLength = senseWindowLength;
	}

	public String getAmplitudeString()
	{
		return amplitudeString;
	}
	
	public String getDataString() 
	{
		return amplitudeString;
	}
	
	public long getLengthInMillis()
	{
		return senseWindowLength;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_MICROPHONE;
	}
}
