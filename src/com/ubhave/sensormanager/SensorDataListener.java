package com.ubhave.sensormanager;

import com.ubhave.sensormanager.data.SensorData;

public interface SensorDataListener
{
	
	public void onDataSensed(SensorData data);
	
	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold);

}
