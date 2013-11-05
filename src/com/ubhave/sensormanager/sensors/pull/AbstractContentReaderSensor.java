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
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.process.pull.ContentReaderProcessor;

public abstract class AbstractContentReaderSensor extends AbstractPullSensor
{
	protected ArrayList<HashMap<String, String>> contentList;
	protected static Object lock = new Object();

	protected abstract String getContentURL();

	protected abstract String[] getContentKeysArray();

	public AbstractContentReaderSensor(Context context)
	{
		super(context);
	}

	protected boolean startSensing()
	{
		new Thread()
		{
			public void run()
			{
				// Some parameters that could be exposed through the config,
				// which are currently set as constants in the code:
				// 1) limit on number of rows - 1000
				// 2) columns to query

				contentList = new ArrayList<HashMap<String, String>>();
				String url = getContentURL();
				Uri uri = Uri.parse(url);
				String[] contentKeys = getContentKeysArray();
				try
				{
					ContentResolver contentResolver = applicationContext.getContentResolver();
					Cursor cursor = contentResolver.query(uri, contentKeys, null, null, "date LIMIT 1000");
					if (cursor != null)
					{
						cursor.moveToFirst();
						if (GlobalConfig.shouldLog())
						{
							Log.d(getLogTag(), "Total entries in the cursor: " + cursor.getCount());
						}

						while (!cursor.isAfterLast())
						{
							HashMap<String, String> contentMap = new HashMap<String, String>();
							for (String key : contentKeys)
							{
								String value = cursor.getString(cursor.getColumnIndex(key));
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
