package com.ubhave.sensormanager.data.pushsensor;

import com.ubhave.sensormanager.data.SensorData;

public class SmsData extends SensorData
{
	public static final String SMS_RECEIVED = "";
	public static final String SMS_CONTENT_CHANGED = "SMSContentChanged"; 
	
	
	private int contentLength;
	private int noOfWords;
	private String address;
	private String eventType;

	public SmsData(long recvTimestamp, int smsLength, int noOfWords, String addr, String eventType)
	{
		super(recvTimestamp);
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
}
