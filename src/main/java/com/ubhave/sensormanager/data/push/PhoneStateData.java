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

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PhoneStateData extends SensorData
{

	public static final int CALL_STATE_IDLE = 54401;
	public static final int CALL_STATE_OFFHOOK = 54402;
	public static final int CALL_STATE_RINGING = 54403;
	public static final int CALL_STATE_OUTGOING = 54404;
	public static final int ON_CELL_LOCATION_CHANGED = 5441;
	public static final int ON_DATA_ACTIVITY = 5442;
	public static final int ON_DATA_CONNECTION_STATE_CHANGED = 5443;
	public static final int ON_SERVICE_STATE_CHANGED = 5444;

	private int eventType;
	private String data, number;

	public PhoneStateData(long dataReceivedTimestamp, SensorConfig sensorConfig)
	{
		super(dataReceivedTimestamp, sensorConfig);
	}
	
	public void setNumber(String n)
	{
		number = n;
	}
	
	public String getNumber()
	{
		return number;
	}
	
	public void setEventType(int e)
	{
		eventType = e;
	}

	public int getEventType()
	{
		return eventType;
	}
	
	public void setData(String d)
	{
		data = d;
	}

	public String getData()
	{
		return data;
	}

	public boolean isRinging()
	{
		return eventType == CALL_STATE_RINGING;
	}

	public boolean isOffHook()
	{
		return eventType == CALL_STATE_OFFHOOK;
	}

	public boolean isIdle()
	{
		return eventType == CALL_STATE_IDLE;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_PHONE_STATE;
	}
}
