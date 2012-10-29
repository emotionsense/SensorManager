package com.ubhave.sensormanager.data.pushsensor;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SmsData extends SensorData
{
	public static final String SMS_RECEIVED = "SMSReceived";
	public static final String SMS_CONTENT_CHANGED = "SMSContentChanged"; 
	
	private int contentLength;
	private int noOfWords;
	private String address;
	private String eventType;

	public SmsData(long recvTimestamp, int smsLength, int noOfWords, String addr, String eventType, SensorConfig sensorConfig)
	{
		super(recvTimestamp, sensorConfig);
		this.contentLength = smsLength;
		this.noOfWords = noOfWords;
		this.address = addr;
		this.eventType = eventType;
	}

	public int getContentLength()
	{
		return contentLength;
	}

	public int getNoOfWords()
	{
		return noOfWords;
	}

	public String getAddress()
	{
		return address;
	}
	
	public boolean wasReceived()
	{
		return eventType.equals(SMS_RECEIVED);
	}

	public String getDataString()
	{
		return "SMSLength " + contentLength + " NoOfWords " + noOfWords + " Address " + address + " Type " + eventType;
	}
	
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SMS;
	}
}
