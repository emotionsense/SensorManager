package com.ubhave.sensormanager.tasks;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.SensorUtils;

public abstract class AbstractSensorTask extends Thread
{
	private class StopTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... arg0)
		{
			stopTask();
			return null;
		}

	}

	private static String TAG = "AbstractSensorTask";

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
		String sensorName = "";

		try
		{
			sensorName = SensorUtils.getSensorName(sensor.getSensorType());
		}
		catch (ESException exp)
		{
			ESLogger.error(TAG, exp);
		}
		return "SensorTask:" + sensorName;
	}

	public int getSensorType()
	{
		return sensor.getSensorType();
	}

	public boolean registerSensorDataListener(SensorDataListener listener)
	{
		ESLogger.log(getLogTag(), "registerSensorDataListener() listener: " + listener);

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
				listener.onDataSensed(sensorData);
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
				new StopTask().execute();
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
