package com.ubhave.sensormanager.sensors;

import com.ubhave.sensormanager.data.SensorData;

public interface SensorDataListener
{
	public void onDataSensed(SensorData sensorData);
}
