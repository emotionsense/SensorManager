package com.ubhave.sensormanager.classifier;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SensorClassifiers
{
	
	public static SensorDataClassifier getSensorClassifier(int sensorType) throws ESException
	{
		switch(sensorType)
		{
		case SensorUtils.SENSOR_TYPE_ACCELEROMETER: return new AccelerometerDataClassifier();
		case SensorUtils.SENSOR_TYPE_MICROPHONE: return new MicrophoneDataClassifier();
		default: throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "No classifier available");
		}
	}
}
