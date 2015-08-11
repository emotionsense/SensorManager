package com.ubhave.sensormanager.process.pull;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.provider.CallLog;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;
import com.ubhave.sensormanager.data.pull.CallContentListData;
import com.ubhave.sensormanager.data.pull.CallContentReaderEntry;

public class CallContentReaderProcessor extends ContentReaderProcessor
{
	private final static String OUTGOING = "outgoing"; // CallLog.Calls.OUTGOING_TYPE
	private final static String INCOMING = "incoming"; // CallLog.Calls.INCOMING_TYPE
	private final static String MISSED = "missed"; // CallLog.Calls.MISSED_TYPE
	
	public CallContentReaderProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}
	
	@Override
	protected AbstractContentReaderListData getData(long senseStartTime, SensorConfig config)
	{
		return new CallContentListData(senseStartTime, config);
	}
	
	@Override
	protected AbstractContentReaderEntry getEntry(final HashMap<String, String> map)
	{
		try
		{
			AbstractContentReaderEntry entry = new CallContentReaderEntry();
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext())
			{
				String key = iterator.next();
				String value = map.get(key);
				if ((value == null) || (value.length() == 0))
				{
					value = "";
				}
				
				if (key.equals(ContentReaderConfig.CONTENT_MAP_NUMBER_KEY) || key.equals(ContentReaderConfig.CONTENT_MAP_ADDRESS_KEY))
				{
					value = hashPhoneNumber(value);
				}
				else if (key.equals(CallLog.Calls.TYPE))
				{
					value = getType(value);
				}
				entry.set(key, value);
				iterator.remove();
			}
			return entry;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private String getType(String stringValue)
	{
		try
		{
			int type = Integer.valueOf(stringValue);
			switch(type)
			{
			case CallLog.Calls.OUTGOING_TYPE:
				return OUTGOING;
			case CallLog.Calls.INCOMING_TYPE:
				return INCOMING;
			case CallLog.Calls.MISSED_TYPE:
				return MISSED;
			default:
				return stringValue;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return stringValue;
		}
	}
}
