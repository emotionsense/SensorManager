package com.ubhave.sensormanager.sensors.pull;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorInterface;

public interface PullSensor extends SensorInterface
{
	// performs one cycle of sensing, and returns the sensed data
	// window size, sampling cycles are defined in the sensorconfig
	public SensorData sense() throws ESException;
}
