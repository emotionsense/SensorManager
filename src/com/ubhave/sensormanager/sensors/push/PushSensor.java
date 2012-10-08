package com.ubhave.sensormanager.sensors.push;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.sensors.SensorInterface;

public interface PushSensor extends SensorInterface
{
	public void startSensing(SensorConfig sensorConfig, SensorDataListener listener) throws ESException;
	
	public void stopSensing(SensorDataListener listener) throws ESException;
}
