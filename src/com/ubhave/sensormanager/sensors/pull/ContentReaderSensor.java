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
	protected HashMap<String, String> contentMap;
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
				contentMap = new HashMap<String, String>();

				String url = getContentURL();
				Uri uri = Uri.parse(url);

				ContentResolver contentResolver = applicationContext.getContentResolver();
				Cursor cursor = contentResolver.query(uri, null, null, null, null);
				Log.d(getLogTag(), "Total entries in the cursor" + cursor.getCount());

				String[] contentKeys = getContentKeysArray();

				while (cursor.moveToNext())
				{
					for (String key : contentKeys)
					{
						String value = cursor.getString(cursor.getColumnIndex(key));

						if (key.equals("number") || key.equals("address"))
						{
							value = AbstractCommunicationSensor.hashPhoneNumber(value);
						}
						else if (key.equals("body"))
						{
							value = value.length() + "";
						}

						contentMap.put(key, value);
					}
				}
				// sensing complete
				notifySenseCyclesComplete();
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
		ContentReaderData crData = new ContentReaderData(pullSenseStartTimestamp, contentMap, getSensorType(), sensorConfig);
		return crData;
	}

}
