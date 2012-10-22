package com.ubhave.sensormanager.data.pushsensor;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ScreenData extends SensorData
{
	private int screenStatus;
	
	public static final int SCREEN_OFF = 0;
	public static final int SCREEN_ON = 1;

	public ScreenData(long recvTimestamp, int screenStatus)
	{
		super(recvTimestamp);
		this.screenStatus = screenStatus;
	}
	
	public String getScreenStatusString()
	{
		switch (screenStatus)
		{
			case SCREEN_OFF:
				return "SCREEN_OFF";
			case SCREEN_ON:
				return "SCREEN_ON";
			default:
				return "UNKNOWN";
		}
	}
	
	public boolean isOn()
	{
		return screenStatus == SCREEN_ON;
	}

	public String getDataString()
	{
		return "ScreenStatus " + getScreenStatusString();
	}
	
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SCREEN;
	}
}
