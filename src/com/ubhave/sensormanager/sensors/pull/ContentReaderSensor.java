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

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.ContentReaderData;
import com.ubhave.sensormanager.sensors.push.AbstractCommunicationSensor;

public abstract class ContentReaderSensor extends AbstractPullSensor
{
	protected ArrayList<HashMap<String, String>> contentList;
	protected static Object lock = new Object();

	protected abstract String getContentURL();

	protected abstract String[] getContentKeysArray();

	public ContentReaderSensor(Context context)
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
				// 1) limit on number of rows - 100
				// 2) columns to query

				contentList = new ArrayList<HashMap<String, String>>();
				String url = getContentURL();
				Uri uri = Uri.parse(url);
				String[] contentKeys = getContentKeysArray();
				try
				{
					ContentResolver contentResolver = applicationContext.getContentResolver();
					Cursor cursor = contentResolver.query(uri, contentKeys, null, null, "date LIMIT 100");
					Log.d(getLogTag(), "Total entries in the cursor" + cursor.getCount());
					while (cursor.moveToNext())
					{
						HashMap<String, String> contentMap = new HashMap<String, String>();
						for (String key : contentKeys)
						{
							String value = cursor.getString(cursor.getColumnIndex(key));

							if ((value == null) || (value.length() == 0))
							{
								value = "";
							}
							else
							{
								if (key.equals("number") || key.equals("address"))
								{
									value = AbstractCommunicationSensor.hashPhoneNumber(value);
								}
								else if (key.equals("body"))
								{
									int noOfWords = 0;
									int charCount = 0;
									if ((value != null) && (value.length() > 0))
									{
										charCount = value.length();
										noOfWords = value.split(" ").length;
									}

									// no. of words
									contentMap.put("bodyWordCount", noOfWords + "");

									// no. of characters
									key = "bodyLength";
									value = charCount + "";
								}
							}
							contentMap.put(key, value);
						}
						contentList.add(contentMap);
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
		ContentReaderData crData = new ContentReaderData(pullSenseStartTimestamp, contentList, getSensorType(), sensorConfig);
		return crData;
	}

}
