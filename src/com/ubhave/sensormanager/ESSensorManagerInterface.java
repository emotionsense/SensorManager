package com.ubhave.sensormanager;

import com.ubhave.sensormanager.data.SensorData;

public interface ESSensorManagerInterface
{
	public int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException;

	public void unsubscribeFromSensorData(int subscriptionId) throws ESException;

	public SensorData getDataFromSensor(int sensorId) throws ESException;
	
	public void setSensorConfig(int sensorId, String configKey, Object configValue) throws ESException;
	
//	public void enableAdaptiveSensing(int sensorId) throws ESException;
//	public void disableAdaptiveSensing(int sensorId) throws ESException;
//	public void setSensingWindowLength(long windowLengthMillis) throws ESException;
//	public void setSleepWindowLength(long windowLengthMillis) throws ESException;
}
