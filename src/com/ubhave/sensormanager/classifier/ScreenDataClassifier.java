package com.ubhave.sensormanager.classifier;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.ScreenData;

public class ScreenDataClassifier implements SensorDataClassifier
{
	
	public boolean isInteresting(SensorData sensorData)
	{
		ScreenData screen = (ScreenData) sensorData;
		return screen.isOn();
	}
	
	

}
