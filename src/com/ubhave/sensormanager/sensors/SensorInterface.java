/*
 * Interface to be implemented by sensor monitors.
 */

package com.ubhave.sensormanager.sensors;

import com.ubhave.sensormanager.ESException;

public interface SensorInterface
{
	public int getSensorType();
	
	public boolean isSensing();
	
	public void setSensorConfig(String configKey, Object configValue) throws ESException;
	
	public Object getSensorConfig(String configKey) throws ESException;
}