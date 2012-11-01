package com.ubhave.sensormanager.classifier;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.PhoneStateData;

public class PhoneStateDataClassifier implements SensorDataClassifier
{
	/*
	 * Interesting = has a call just ended?
	 */
	
	private boolean isInCall;

	public PhoneStateDataClassifier()
	{
		isInCall = false;
	}

	public boolean isInteresting(SensorData sensorData)
	{
		PhoneStateData phone = (PhoneStateData) sensorData;
		if (phone.isOffHook())
		{
			isInCall = true;
		}
		else if (phone.isIdle())
		{
			if (isInCall)
			{
				isInCall = false;
				return true;
			}
		}
		return false;
	}
}
