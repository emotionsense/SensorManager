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

import android.Manifest;
import android.content.Context;
import android.provider.CallLog;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class CallContentReaderSensor extends AbstractContentReaderSensor
{
	private static final String LOG_TAG = "CallContentReaderSensor";
	private static CallContentReaderSensor callContentReaderSensor;

	public static CallContentReaderSensor getSensor(final Context context) throws ESException
	{
		if (callContentReaderSensor == null)
		{
			synchronized (lock)
			{
				if (callContentReaderSensor == null)
				{
					if ((android.os.Build.VERSION.SDK_INT <= 15 && permissionGranted(context, Manifest.permission.READ_CONTACTS))
							|| permissionGranted(context, Manifest.permission.READ_CALL_LOG))
					{
						callContentReaderSensor = new CallContentReaderSensor(context);
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED, SensorUtils.SENSOR_NAME_CALL_CONTENT_READER);
					}	
				}
			}
		}
		return callContentReaderSensor;
	}

	private CallContentReaderSensor(Context context)
	{
		super(context);
	}

	@Override
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER;
	}

	@Override
	protected String getContentURL()
	{
		return "content://call_log/calls";
	}
	
	@Override
	protected String getDateKey()
	{
		return CallLog.Calls.DATE;
	}

	@Override
	protected String[] getContentKeysArray()
	{
		return new String[] { CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION };
	}

	@Override
	protected String getLogTag()
	{
		return LOG_TAG;
	}
}
