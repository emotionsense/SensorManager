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

package com.ubhave.sensormanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;

import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.dutycyling.AdaptiveSensing;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.sensors.pull.AbstractPullSensor;
import com.ubhave.sensormanager.tasks.AbstractSensorTask;
import com.ubhave.sensormanager.tasks.PullSensorTask;
import com.ubhave.sensormanager.tasks.PushSensorTask;
import com.ubhave.sensormanager.tasks.Subscription;
import com.ubhave.sensormanager.tasks.SubscriptionList;

public class ESSensorManager implements ESSensorManagerInterface, SensorDataListener
{
	private static final String TAG = "ESSensorManager";

	private static ESSensorManager sensorManager;
	private static Object lock = new Object();

	private final Context applicationContext;
	private PowerManager.WakeLock wakeLock;

	private boolean isSubscribedToBattery;
	private int batterySubscriptionId;

	private final SparseArray<AbstractSensorTask> sensorTaskMap;
	private final SubscriptionList subscriptionList;
	private final GlobalConfig config;

	public static ESSensorManager getSensorManager(final Context context) throws ESException
	{
		if (context == null)
		{
			throw new ESException(ESException.INVALID_PARAMETER, " Invalid parameter, context object passed is null");
		}
		if (sensorManager == null)
		{
			synchronized (lock)
			{
				if (sensorManager == null)
				{
					sensorManager = new ESSensorManager(context);
				}
			}
		}
		return sensorManager;
	}

	private ESSensorManager(final Context appContext)
	{
		applicationContext = appContext;
		sensorTaskMap = new SparseArray<AbstractSensorTask>();
		subscriptionList = new SubscriptionList();
		config = GlobalConfig.getGlobalConfig();
		isSubscribedToBattery = false;

		ArrayList<SensorInterface> sensors = SensorUtils.getAllSensors(appContext);
		for (SensorInterface aSensor : sensors)
		{
			try
			{
				int sensorType = aSensor.getSensorType();
				AbstractSensorTask sensorTask;
				if (SensorUtils.isPullSensor(sensorType))
				{
					sensorTask = new PullSensorTask(aSensor);
				}
				else
				{
					sensorTask = new PushSensorTask(aSensor);
				}

				sensorTask.start();
				sensorTaskMap.put(sensorType, sensorTask);
			}
			catch (ESException e)
			{
				e.printStackTrace();
			}
		}
	}

	public Context getApplicationContext()
	{
		return applicationContext;
	}

	public synchronized int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException
	{
		AbstractSensorTask task = sensorTaskMap.get(sensorId);
		if (task != null)
		{
			if (!isSubscribedToBattery)
			{
				if (GlobalConfig.shouldLog())
				{
					Log.d(TAG, "Registering battery subscription.");
				}
				isSubscribedToBattery = true;
				batterySubscriptionId = subscribeToSensorData(SensorUtils.SENSOR_TYPE_BATTERY, this);
			}

			if (GlobalConfig.shouldLog())
			{
				Log.d(TAG, "subscribeToSensorData() subscribing listener to sensor: " + SensorUtils.getSensorName(sensorId));
			}

			Subscription subscription = new Subscription(task, listener);
			int subscriptionId = subscriptionList.registerSubscription(subscription);
			return subscriptionId;
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + SensorUtils.getSensorName(sensorId) + " (Check permissions? Sensor missing from device?)");
		}
	}

	public synchronized void unsubscribeFromSensorData(int subscriptionId) throws ESException
	{
		Subscription subscription = subscriptionList.removeSubscription(subscriptionId);
		if (subscription != null)
		{
			subscription.unregister();
			if (subscriptionList.getAllSubscriptions().size() == 1)
			{
				if (GlobalConfig.shouldLog())
				{
					Log.d(TAG, "Removing battery subscription.");
				}
				unsubscribeFromSensorData(batterySubscriptionId);
				isSubscribedToBattery = false;
			}
		}
		else
		{
			throw new ESException(ESException.INVALID_STATE, "Un-Mapped subscription id: " + subscriptionId);
		}
	}

	private AbstractSensorTask getSensorTask(int sensorId) throws ESException
	{
		AbstractSensorTask sensorTask = sensorTaskMap.get(sensorId);
		if (sensorTask == null)
		{
			try
			{
				String sensorName = SensorUtils.getSensorName(sensorId);
				throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, sensorName + "sensor unavailable. Have you put the required permissions into your manifest?");
			}
			catch (ESException e)
			{
				e.printStackTrace();
				throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Unknown sensor type: " + sensorId);
			}
		}
		return sensorTask;
	}

	public SensorData getDataFromSensor(int sensorId) throws ESException
	{
		SensorData sensorData = null;
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		if (!SensorUtils.isPullSensor(sensorTask.getSensorType()))
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED, "This method is supported only for pull sensors " + " (your request: " + SensorUtils.getSensorName(sensorId) + ")");
		}
		else if (sensorTask.isRunning())
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED,
					"This method is supported only for sensors tasks that are not currently running. Please unregister all your listeners to the sensor to call this method.");
		}
		else
		{
			sensorData = ((PullSensorTask) sensorTask).getCurrentSensorData(true);
		}
		return sensorData;
	}

	public void setSensorConfig(int sensorId, String configKey, Object configValue) throws ESException
	{
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		SensorInterface sensor = sensorTask.getSensor();
		sensor.setSensorConfig(configKey, configValue);

		if (configKey.equals(PullSensorConfig.ADAPTIVE_SENSING_ENABLED))
		{
			if ((Boolean) configValue)
			{
				enableAdaptiveSensing(sensorId);
			}
			else
			{
				disableAdaptiveSensing(sensorId);
			}
		}
	}

	public Object getSensorConfigValue(int sensorId, String configKey) throws ESException
	{
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		SensorInterface sensor = sensorTask.getSensor();
		return sensor.getSensorConfig(configKey);
	}

	@Override
	public void setGlobalConfig(String configKey, Object configValue) throws ESException
	{
		config.setParameter(configKey, configValue);

		if (configKey.equals(GlobalConfig.ACQUIRE_WAKE_LOCK))
		{
			if (applicationContext.checkCallingOrSelfPermission("android.permission.WAKE_LOCK") != PackageManager.PERMISSION_GRANTED)
			{
				throw new ESException(ESException.PERMISSION_DENIED, "Sensor Manager requires android.permission.WAKE_LOCK");
			}

			if ((Boolean) configValue)
			{
				acquireWakeLock();
			}
			else
			{
				releaseWakeLock();
			}
		}
	}

	@Override
	public Object getGlobalConfig(String configKey) throws ESException
	{
		return config.getParameter(configKey);
	}

	private void acquireWakeLock()
	{
		if ((wakeLock != null) && (wakeLock.isHeld()))
		{
			return;
		}
		else
		{
			PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wakelock_" + System.currentTimeMillis());
			wakeLock.acquire();
		}
	}

	private void releaseWakeLock()
	{
		try
		{
			if (wakeLock != null)
			{
				if (wakeLock.isHeld())
				{
					wakeLock.release();
				}
			}
		}
		catch (Throwable thr)
		{
			// ignore, perhaps wake lock has already been released
		}
	}

	private void enableAdaptiveSensing(int sensorId) throws ESException
	{
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		if (SensorUtils.isPullSensor(sensorId))
		{
			AbstractPullSensor pullSensor = (AbstractPullSensor) sensorTask.getSensor();
			AdaptiveSensing.getAdaptiveSensing().registerSensor(sensorManager, sensorTask.getSensor(), SensorUtils.getSensorDataClassifier(sensorId), pullSensor);
		}
		else
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED, " adaptive sensing is supported only for pull sensors");
		}
	}

	private void disableAdaptiveSensing(int sensorId) throws ESException
	{
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		if (AdaptiveSensing.getAdaptiveSensing().isSensorRegistered(sensorTask.getSensor()))
		{
			AdaptiveSensing.getAdaptiveSensing().unregisterSensor(sensorManager, sensorTask.getSensor());
		}
		else
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED, " adaptive sensing not enabled for sensorId: " + sensorId);
		}
	}

	public void onDataSensed(SensorData data)
	{
		// ignore
	}

	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
		List<Subscription> subscribers = subscriptionList.getAllSubscriptions();
		for (Subscription sub : subscribers)
		{
			if (!(sub.getListener() instanceof ESSensorManager))
			{
				sub.getListener().onCrossingLowBatteryThreshold(isBelowThreshold);
			}
		}
	}

	public void pauseSubscription(int subscriptionId) throws ESException
	{
		Subscription s = subscriptionList.getSubscription(subscriptionId);
		s.pause();
	}

	public void unPauseSubscription(int subscriptionId) throws ESException
	{
		Subscription s = subscriptionList.getSubscription(subscriptionId);
		s.unpause();
	}

}