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

package com.ubhave.sensormanager.sensors.push;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubhave.sensormanager.data.push.ScreenData;
import com.ubhave.sensormanager.process.push.ScreenProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ScreenSensor extends AbstractPushSensor
{
	private static final String TAG = "ScreenSensor";

	private static ScreenSensor screenSensor;
	private static Object lock = new Object();

	public static ScreenSensor getSensor(final Context context)
	{
		if (screenSensor == null)
		{
			synchronized (lock)
			{
				if (screenSensor == null)
				{
					screenSensor = new ScreenSensor(context);
				}
			}
		}
		return screenSensor;
	}

	private ScreenSensor(Context context)
	{
		super(context);
	}

	public String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SCREEN;
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{
		ScreenProcessor processor = (ScreenProcessor) super.getProcessor();
		ScreenData screenData = processor.process(System.currentTimeMillis(), sensorConfig.clone(), intent);
		onDataSensed(screenData);
	}

	protected IntentFilter[] getIntentFilters()
	{
		IntentFilter[] filters = new IntentFilter[2];
		filters[0] = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filters[1] = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		return filters;
	}

	protected boolean startSensing()
	{
		// nothing to do
		return true;
	}

	protected void stopSensing()
	{
		// nothing to do
	}

}
