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

package com.ubhave.sensormanager.tasks;

import java.util.ArrayList;

import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.BatteryData;
import com.ubhave.sensormanager.dutycyling.AdaptiveSensing;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.SensorUtils;

public abstract class AbstractSensorTask extends Thread
{
	private class StopTask extends Thread
	{
		@Override
		public void run()
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d("StopTask", "Stopping sensor task...");
			}
			stopTask();
		}
	}

	private class NotificationTask extends Thread
	{
		private SensorData sensorData;

		public NotificationTask(SensorData sensorData)
		{
			this.sensorData = sensorData;
		}

		@Override
		public void run()
		{
			notifications(sensorData);
		}
	}

	protected SensorInterface sensor;
	protected Object syncObject = new Object();

	protected int state;
	protected long pauseTime;

	protected ArrayList<SensorDataListener> listenerList;

	public static final int RUNNING = 6123;
	public static final int PAUSED = 6124;
	public static final int STOPPED = 6125;

	public AbstractSensorTask(SensorInterface sensor)
	{
		this.sensor = sensor;
		listenerList = new ArrayList<SensorDataListener>();
	}

	public abstract void run();

	public SensorInterface getSensor()
	{
		return sensor;
	}

	@Override
	public void start()
	{
		state = STOPPED;
		super.start();
	}

	protected String getLogTag()
	{
		try
		{
			String sensorName = SensorUtils.getSensorName(sensor.getSensorType());
			return "SensorTask:" + sensorName;
		}
		catch (ESException exp)
		{
			exp.printStackTrace();
			return null;
		}
	}

	public int getSensorType()
	{
		return sensor.getSensorType();
	}

	public boolean registerSensorDataListener(SensorDataListener listener)
	{
		if (GlobalConfig.shouldLog())
		{
			Log.d(getLogTag(), "registerSensorDataListener() listener: " + listener);
		}
		synchronized (listenerList)
		{
			for (int i = 0; i < listenerList.size(); i++)
			{
				if (listenerList.get(i) == listener)
				{
					return false;
				}
			}
			listenerList.add(listener);
			startTask();
			return true;
		}
	}

	protected void publishData(SensorData sensorData)
	{
		synchronized (listenerList)
		{
			for (SensorDataListener listener : listenerList)
			{
				if (sensorData != null)
				{
					listener.onDataSensed(sensorData);
				}
				else if (GlobalConfig.shouldLog())
				{
					Log.d(getLogTag(), "sensorData is null");
				}
			}
		}

		// check for any triggers/notifications to be sent
		// based on the received sensorData
		new NotificationTask(sensorData).start();
	}

	protected void publishBatteryNotification(boolean isBelowThreshold)
	{
		// publish only to sensor manager, which in turn publishes to
		// all other listeners
		synchronized (listenerList)
		{
			for (SensorDataListener listener : listenerList)
			{
				if (listener instanceof ESSensorManager)
				{
					listener.onCrossingLowBatteryThreshold(isBelowThreshold);
					break;
				}
			}
		}
	}

	private boolean isBelowThresholdNotified = false;
	private boolean isAboveThresholdNotified = false;

	protected void notifications(SensorData sensorData)
	{
		if (sensorData instanceof BatteryData)
		{
			BatteryData batteryData = (BatteryData) sensorData;
			int currLevel = batteryData.getBatteryLevel();

			int batteryThreshold = (Integer) GlobalConfig.getGlobalConfig().getParameter(GlobalConfig.LOW_BATTERY_THRESHOLD);

			if (currLevel < batteryThreshold)
			{
				if (!isBelowThresholdNotified)
				{
					publishBatteryNotification(true);
					isBelowThresholdNotified = true;
					isAboveThresholdNotified = false;
				}
			}
			else if (currLevel > batteryThreshold)
			{
				if (!isAboveThresholdNotified)
				{
					publishBatteryNotification(false);
					isBelowThresholdNotified = false;
					isAboveThresholdNotified = true;
				}
			}

		}
	}

	public void unregisterSensorDataListener(SensorDataListener listener)
	{
		synchronized (listenerList)
		{
			listenerList.remove(listener);
			if (listenerList.isEmpty())
			{
				new StopTask().start();
			}
			else if (listenerList.size() == 1)
			{
				// if adaptive sensing is enabled then it'll also be
				// a listener to the sensor, therefore, if adaptive sensing
				// is the only listener then stop the sensor
				if (AdaptiveSensing.getAdaptiveSensing().isSensorRegistered(this.getSensor())
						&& listenerList.contains(AdaptiveSensing.getAdaptiveSensing()))
				{
					new StopTask().start();
				}

			}
		}
	}

	public void startTask()
	{
		if (state == STOPPED)
		{
			synchronized (syncObject)
			{
				syncObject.notify();
			}
		}
		else
		{
			// ignore
		}
	}

	private void stopTask()
	{
		if (state == STOPPED)
		{
			// ignore
		}
		else
		{
			synchronized (syncObject)
			{
				synchronized (listenerList)
				{
					if (listenerList.isEmpty())
					{
						state = STOPPED;
						this.interrupt();
					}
				}
			}
		}
	}

	public boolean isRunning()
	{
		if (state == RUNNING)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
