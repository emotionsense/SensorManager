package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.ContentReaderData;
import com.ubhave.sensormanager.data.pullsensor.ContentReaderResult;
import com.ubhave.sensormanager.process.CommunicationProcessor;

public abstract class ContentReaderProcessor extends CommunicationProcessor
{
	public ContentReaderProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public ContentReaderData process(long pullSenseStartTimestamp, int sensorType, ArrayList<HashMap<String, String>> contentList, SensorConfig sensorConfig)
	{
		ContentReaderData contentData = new ContentReaderData(pullSenseStartTimestamp, sensorConfig);
		contentData.setSensorType(sensorType);
		if (setRawData)
		{
			for (HashMap<String, String> map : contentList)
			{
				ContentReaderResult entry = getEntry(map);
				if (entry != null)
				{
					contentData.addContent(entry);
				}
			}
		}
		return contentData;
	}
	
	protected abstract ContentReaderResult getEntry(final HashMap<String, String> map);
//	{
//		try
//		{
//			ContentReaderResult entry = new ContentReaderResult();
//			Iterator<String> iterator = map.keySet().iterator();
//			while (iterator.hasNext())
//			{
//				String key = iterator.next();
//				String value = map.get(key);
//				if ((value == null) || (value.length() == 0))
//				{
//					value = "";
//				}
//				
//				if (key.equals(ContentReaderConfig.CONTENT_MAP_NUMBER_KEY) || key.equals(ContentReaderConfig.CONTENT_MAP_ADDRESS_KEY))
//				{
//					value = hashPhoneNumber(value);
//				}
//				else if (key.equals(ContentReaderConfig.CONTENT_MAP_BODY_KEY))
//				{
//					int noOfWords = countWords(value);
//					int charCount = countChars(value);
//					entry.set(ContentReaderConfig.CONTENT_MAP_WORDCOUNT_KEY, noOfWords + "");
//
//					key = ContentReaderConfig.CONTENT_MAP_CHARCOUNT_KEY;
//					value = charCount + "";
//				}
//				entry.set(key, value);
//				iterator.remove();
//			}
//			return entry;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//	}
}
