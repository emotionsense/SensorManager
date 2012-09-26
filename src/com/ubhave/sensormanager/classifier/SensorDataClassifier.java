package com.ubhave.sensormanager.classifier;

import com.ubhave.sensormanager.data.SensorData;

public interface SensorDataClassifier
{
	
	public boolean isInteresting(SensorData sensorData);

}
