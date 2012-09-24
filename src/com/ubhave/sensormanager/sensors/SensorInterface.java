/*
 * Interface to be implemented by sensor monitors.
 */

package com.ubhave.sensormanager.sensors;


public interface SensorInterface
{
	public int getSensorType();
	
	public boolean isSensing();
}