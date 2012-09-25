package com.ubhave.sensormanager;

public interface ESSensorManagerInterface
{
	public void startAllSensors();
	public void stopAllSensors();
	public void pauseAllSensors(long pauseLength) throws ESException;
}
