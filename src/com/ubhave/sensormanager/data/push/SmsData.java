/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.sensormanager.data.push;

import java.util.HashMap;

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
	private String messageType;
	private String eventType;
	private final HashMap<String, Integer> wordCategories;
	
	public SmsData(long recvTimestamp, SensorConfig config)
	{
		super(recvTimestamp, config);
		wordCategories = new HashMap<String, Integer>();
	}
	
	public void addCategory(String key)
	{
		Integer count = wordCategories.get(key);
		if (count == null)
		{
			count = 0;
		}
		wordCategories.put(key, count + 1);
	}
	
	public void setContentLength(int n)
	{
		contentLength = n;
	}

	public int getContentLength()
	{
		return contentLength;
	}
	
	public void setNumberOfWords(int n)
	{
		noOfWords = n;
	}

	public int getNoOfWords()
	{
		return noOfWords;
	}
	
	public void setAddress(String a)
	{
		address = a;
	}

	public String getAddress()
	{
		return address;
	}
	
	public void setEventType(String e)
	{
		eventType = e;
	}
	
	public String getEventType()
	{
		return eventType;
	}
	
	public void setMessageType(String mt)
	{
		messageType = mt;
	}
	
	public String getMessageType()
	{
		return messageType;
	}

	public boolean wasReceived()
	{
		return eventType.equals(SMS_RECEIVED);
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SMS;
	}
}
