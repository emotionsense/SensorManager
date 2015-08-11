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

package com.ubhave.sensormanager.sensors.pull;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.process.pull.ContentReaderProcessor;

public abstract class AbstractContentReaderSensor extends AbstractPullSensor
{
	private final static String TAG = "ContentReaderSensor";
	protected ArrayList<HashMap<String, String>> contentList;
	protected static Object lock = new Object();

	protected abstract String getContentURL();
	
	protected abstract String[] getContentKeysArray();

	protected AbstractContentReaderSensor(final Context context)
	{
		super(context);
	}

	protected boolean startSensing()
	{
		new Thread()
		{
			public void run()
			{
				contentList = new ArrayList<HashMap<String, String>>();
				try
				{
					final String url = getContentURL();
					final String[] contentKeys = getContentKeysArray();
					final ContentResolver contentResolver = applicationContext.getContentResolver();
					
					final long timeLimit = getTimeLimit();
					String selection = null;
					String[] selectionArgs = null;
					if (timeLimit != ContentReaderConfig.NO_TIME_LIMIT)
					{
						selection = getDateKey() + ">=?";
						selectionArgs = new String[]{Long.toString(timeLimit)};
						if (GlobalConfig.shouldLog())
						{
							Log.d(TAG, "Query range: "+selection+" :: "+timeLimit);
						}
					}
					
					Cursor cursor = contentResolver.query(Uri.parse(url), contentKeys, selection, selectionArgs, getSortBy());
					if (cursor != null)
					{
						cursor.moveToFirst();
						if (GlobalConfig.shouldLog())
						{
							Log.d(getLogTag(), "Total entries in the cursor: " + cursor.getCount());
						}
						
						HashMap<String, Integer> columnIndex = new HashMap<String, Integer>();
						for (String key : contentKeys)
						{
							columnIndex.put(key, cursor.getColumnIndex(key));
						}

						while (!cursor.isAfterLast())
						{
							HashMap<String, String> contentMap = new HashMap<String, String>();
							for (String key : contentKeys)
							{
								String value = cursor.getString(columnIndex.get(key));
								contentMap.put(key, value);
							}
							contentList.add(contentMap);
							cursor.moveToNext();
						}
						cursor.close();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					// sensing complete
					notifySenseCyclesComplete();
				}
			}
		}.start();

		return true;
	}
	
	private String getSortBy()
	{
		String sortBy = getDateKey();
		int maxRows;
		try
		{
			maxRows = (Integer) sensorConfig.getParameter(ContentReaderConfig.ROW_LIMIT);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			maxRows = ContentReaderConfig.DEFAULT_ROW_LIMIT;
		}
		
		if (maxRows != ContentReaderConfig.NO_ROW_LIMIT)
		{
			sortBy += " LIMIT "+maxRows;
		}
		
		return sortBy;
	}
	
	private long getTimeLimit()
	{
		try
		{
			return (Long) sensorConfig.getParameter(ContentReaderConfig.TIME_LIMIT_MILLIS);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return ContentReaderConfig.NO_TIME_LIMIT;
		}
	}
	
	protected abstract String getDateKey();

	// Called when a scan is finished
	protected void stopSensing()
	{
	}

	@Override
	protected SensorData getMostRecentRawData()
	{
		ContentReaderProcessor processor = (ContentReaderProcessor) super.getProcessor();
		return processor.process(pullSenseStartTimestamp, getSensorType(), contentList, sensorConfig);
	}

	@Override
	protected void processSensorData()
	{
	}
}
