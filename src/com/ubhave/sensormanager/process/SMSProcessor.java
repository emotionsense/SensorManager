package com.ubhave.sensormanager.process;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.SmsData;

public class SMSProcessor extends CommunicationProcessor
{
	
	public SMSProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}

	public SmsData process(long timestamp, SensorConfig config, String content, String address, String event)
	{
		SmsData data = new SmsData(timestamp, config);
		if (super.setRawData)
		{
			data.setNumberOfWords(content.split(" ").length);
			data.setContentLength(content.length());
			data.setAddress(hashPhoneNumber(address));
			data.setEventType(event);
		}
		
		if (super.setProcessedData)
		{
			// TODO 
			data.setDataProcessed(true);
		}	
		return data;
	}
}
