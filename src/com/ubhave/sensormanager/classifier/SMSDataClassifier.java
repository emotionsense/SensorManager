package com.ubhave.sensormanager.classifier;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.SmsData;

public class SMSDataClassifier implements SensorDataClassifier
{

	/*
	 * Interesting = was an sms just received?
	 */
	
	public boolean isInteresting(SensorData sensorData)
	{
		SmsData sms = (SmsData) sensorData;
		if (sms.wasReceived())
		{
			return true;
		}
		else return false;
	}

}
