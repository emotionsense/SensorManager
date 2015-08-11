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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.AbstractSensor;

public abstract class AbstractPushSensor extends AbstractSensor implements PushSensor
{
	protected SensorDataListener sensorDataListener;
	protected BroadcastReceiver broadcastReceiver;

	public AbstractPushSensor(Context context)
	{
		super(context);
		broadcastReceiver = new BroadcastReceiver()
		{
			public void onReceive(final Context context, final Intent intent)
			{
				if (isSensing)
				{
					onBroadcastReceived(context, intent);
				}
				else if (GlobalConfig.shouldLog())
				{
					Log.d(getLogTag(), "BroadcastReceiver.onReceive() called while not sensing.");
				}
			}
		};
	}
	
	protected abstract void onBroadcastReceived(Context context, Intent intent);

	protected abstract IntentFilter[] getIntentFilters();

	public void startSensing(SensorDataListener listener) throws ESException
	{
		if (isSensing)
		{
			// sensing already started
			if (GlobalConfig.shouldLog())
			{
				Log.d(getLogTag(), "sensing already sensing");
			}
			throw new ESException(ESException.SENSOR_ALREADY_SENSING, "sensor already sensing");
		}

		this.sensorDataListener = listener;
		startSensing();
		// register broadcast receiver
		IntentFilter[] filters = getIntentFilters();
		if ((filters != null) && (filters.length > 0))
		{
			for (IntentFilter aFilter : filters)
			{
				applicationContext.registerReceiver(broadcastReceiver, aFilter);
			}
		}
		isSensing = true;
		if (GlobalConfig.shouldLog())
		{
			Log.d(getLogTag(), "Sensing started.");
		}
	}

	public void stopSensing(SensorDataListener listener) throws ESException
	{
		if (!isSensing)
		{
			// sensing already started
			if (GlobalConfig.shouldLog())
			{
				Log.d(getLogTag(), "sensor not sensing");
			}
			throw new ESException(ESException.SENSOR_NOT_SENSING, "sensor not sensing");
		}

		stopSensing();
		try
		{
			IntentFilter[] filters = getIntentFilters();
			if ((filters != null) && (filters.length > 0))
			{
				applicationContext.unregisterReceiver(broadcastReceiver);
			}
		}
		catch (IllegalArgumentException e)
		{
			// Receiver not registered
		}
		
		isSensing = false;
		if (GlobalConfig.shouldLog())
		{
			Log.d(getLogTag(), "Sensing stopped.");
		}
	}

	protected void onDataSensed(final SensorData sensorData)
	{
		if (sensorDataListener != null)
		{
			sensorDataListener.onDataSensed(sensorData);
		}
	}
}
