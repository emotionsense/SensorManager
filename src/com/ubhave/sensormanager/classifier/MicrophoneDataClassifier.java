package com.ubhave.sensormanager.classifier;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;

public class MicrophoneDataClassifier implements SensorDataClassifier
{

	public boolean isInteresting(SensorData sensorData)
	{
		MicrophoneData data = (MicrophoneData) sensorData;
		if (data.isSilent())
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
