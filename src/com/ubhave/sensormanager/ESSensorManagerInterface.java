package com.ubhave.sensormanager;

import com.ubhave.sensormanager.data.SensorData;

public interface ESSensorManagerInterface
{
	// To be removed
//	public void startAllSensors();
//	public void stopAllSensors();
//	public void pauseAllSensors(long pauseLength) throws ESException;
	
	// To be implemented
	public int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException;
	public void unsubscribeFromSensorData(int subscriptionId) throws ESException;
	
	public SensorData getDataFromSensor(int sensorId);
}
