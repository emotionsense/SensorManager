package com.ubhave.sensormanager.process.pull;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;
import com.ubhave.sensormanager.data.pull.SMSContentListData;
import com.ubhave.sensormanager.data.pull.SMSContentReaderEntry;

public class SMSContentReaderProcessor extends ContentReaderProcessor
{
	private static final String MESSAGE_TYPE_ALL    = "all";
	private static final String MESSAGE_TYPE_INBOX  = "inbox";
	private static final String MESSAGE_TYPE_SENT   = "sent";
	private static final String MESSAGE_TYPE_DRAFT  = "draft";
	private static final String MESSAGE_TYPE_OUTBOX = "outbox";
	private static final String MESSAGE_TYPE_FAILED = "failed"; // for failed outgoing messages
	private static final String MESSAGE_TYPE_QUEUED = "queued"; 
	
	public SMSContentReaderProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}
	
	@Override
	protected AbstractContentReaderListData getData(long senseStartTime, SensorConfig config)
	{
		return new SMSContentListData(senseStartTime, config);
	}
	
	@Override
	protected AbstractContentReaderEntry getEntry(final HashMap<String, String> map)
	{
		try
		{			
			AbstractContentReaderEntry entry = new SMSContentReaderEntry();
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext())
			{
				String key = iterator.next();
				String value = map.get(key);
				if ((value == null) || (value.length() == 0))
				{
					value = "";
				}
				
				if (key.equals(ContentReaderConfig.SMS_CONTENT_ADDRESS_KEY))
				{
					value = hashPhoneNumber(value);
				}
				else if (key.equals(ContentReaderConfig.SMS_CONTENT_BODY_KEY))
				{
					int noOfWords = countWords(value);
					entry.set(ContentReaderConfig.CONTENT_MAP_WORDCOUNT_KEY, noOfWords + "");

					key = ContentReaderConfig.CONTENT_MAP_CHARCOUNT_KEY;
					int charCount = countChars(value);
					value = charCount + "";
				}
				else if (key.equals(ContentReaderConfig.SMS_CONTENT_TYPE_KEY))
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
	
	private String getType(String intType)
	{
		try
		{
			// From android.provider.Telephony.TextBasedSmsColumns
			// http://developer.android.com/reference/android/provider/Telephony.TextBasedSmsColumns.html
//			public static final int MESSAGE_TYPE_ALL    = 0;
//			public static final int MESSAGE_TYPE_INBOX  = 1;
//			public static final int MESSAGE_TYPE_SENT   = 2;
//			public static final int MESSAGE_TYPE_DRAFT  = 3;
//			public static final int MESSAGE_TYPE_OUTBOX = 4;
//			public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
//			public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send later  
			
			int smsType = Integer.valueOf(intType);
			switch (smsType)
			{
			case 0:
				return MESSAGE_TYPE_ALL;
			case 1:
				return MESSAGE_TYPE_INBOX;
			case 2:
				return MESSAGE_TYPE_SENT;
			case 3:
				return MESSAGE_TYPE_DRAFT;
			case 4:
				return MESSAGE_TYPE_OUTBOX;
			case 5:
				return MESSAGE_TYPE_FAILED;
			case 6:
				return MESSAGE_TYPE_QUEUED;
			default:
				return intType;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return intType;
		}
	}
	
	private int countWords(final String text)
	{
		try
		{
			if (text != null)
			{
				return text.split(" ").length;
			}
			else
			{
				return 0;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	private int countChars(final String text)
	{
		try
		{
			if (text != null)
			{
				return text.length();
			}
			else
			{
				return 0;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

}
