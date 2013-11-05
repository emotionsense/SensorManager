package com.ubhave.sensormanager.process.pull;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;

import com.ubhave.sensormanager.config.sensors.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.pullsensor.ContentReaderResult;

public class CallContentReaderProcessor extends ContentReaderProcessor
{
	public CallContentReaderProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}
	
	@Override
	protected ContentReaderResult getEntry(final HashMap<String, String> map)
	{
		try
		{
			ContentReaderResult entry = new ContentReaderResult();
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
}
