package com.ubhave.sensormanager;

import com.ubhave.sensormanager.data.SensorData;

public interface ESSensorManagerInterface
{
	public int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException;

	public void unsubscribeFromSensorData(int subscriptionId) throws ESException;

	public SensorData getDataFromSensor(int sensorId);
	
//	public void setSensorSampleTime(int sensorId, long sampleMillis);
//	
//	public void setSensorSleepTime(int sensorId, long sleepMillis);
}
