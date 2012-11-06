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
		case SensorUtils.SENSOR_TYPE_BLUETOOTH: return new BluetoothDataClassifier();
		case SensorUtils.SENSOR_TYPE_LOCATION: return new LocationDataClassifier();
		case SensorUtils.SENSOR_TYPE_MICROPHONE: return new MicrophoneDataClassifier();
		case SensorUtils.SENSOR_TYPE_PHONE_STATE: return new PhoneStateDataClassifier();
		case SensorUtils.SENSOR_TYPE_SCREEN: return new ScreenDataClassifier();
		case SensorUtils.SENSOR_TYPE_SMS: return new SMSDataClassifier();
		case SensorUtils.SENSOR_TYPE_WIFI: return new WifiDataClassifier();
		default: throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "No classifier available");
		}
	}
}
