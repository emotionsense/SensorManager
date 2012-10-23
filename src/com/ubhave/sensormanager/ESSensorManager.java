package com.ubhave.sensormanager;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.tasks.AbstractSensorTask;
import com.ubhave.sensormanager.tasks.PullSensorTask;
import com.ubhave.sensormanager.tasks.PushSensorTask;
import com.ubhave.sensormanager.tasks.Subscription;
import com.ubhave.sensormanager.tasks.SubscriptionList;

public class ESSensorManager implements ESSensorManagerInterface
{
	private static final String TAG = "ESSensorManager";

	private static ESSensorManager sensorManager;
	private static Object lock = new Object();

	private final SparseArray<AbstractSensorTask> sensorTaskMap;
	private final SubscriptionList subscriptionList;

	public static void startSensorManager(Context context) throws ESException
	{
		if (sensorManager == null)
		{
			synchronized (lock)
			{
				if (sensorManager == null)
				{
					if (context.checkCallingOrSelfPermission("android.permission.WAKE_LOCK") == PackageManager.PERMISSION_GRANTED)
					{
						sensorManager = new ESSensorManager(context);
						ESLogger.log(TAG, "started.");
					}
					else
						throw new ESException(ESException.PERMISSION_DENIED, "Sensor Manager requires android.permission.WAKE_LOCK");
				}
			}
		}
	}

	public static ESSensorManager getSensorManager() throws ESException
	{
		if (sensorManager == null)
		{
			throw new ESException(ESException.SENSOR_MANAGER_NOT_STARTED, "sensor manager not started, start it before calling this method.");
		}
		return sensorManager;
	}

	private ESSensorManager(final Context appContext)
	{
		sensorTaskMap = new SparseArray<AbstractSensorTask>();
		subscriptionList = new SubscriptionList();

		ArrayList<SensorInterface> sensors = SensorUtils.getAllSensors(appContext);

		for (SensorInterface aSensor : sensors)
		{
			AbstractSensorTask sensorTask;

			if (SensorUtils.isPullSensor(aSensor.getSensorType()))
			{
				sensorTask = new PullSensorTask(aSensor);
			}
			else
			{
				sensorTask = new PushSensorTask(aSensor);
			}

			sensorTask.start();

			sensorTaskMap.put(aSensor.getSensorType(), sensorTask);
		}
	}

	public synchronized int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException
	{
		AbstractSensorTask task = sensorTaskMap.get(sensorId);
		if (task != null)
		{
			ESLogger.log(TAG, "subscribeToSensorData() subscribing listener to sensorId " + sensorId);
			Subscription subscription = new Subscription(task, listener);
			int subscriptionId = subscriptionList.registerSubscription(subscription);
			return subscriptionId;
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + sensorId);
		}
	}

	public synchronized void unsubscribeFromSensorData(int subscriptionId) throws ESException
	{
		Subscription subscription = subscriptionList.removeSubscription(subscriptionId);
		if (subscription != null)
		{
			subscription.unregister();
		}
		else
		{
			throw new ESException(ESException.INVALID_STATE, "Un-Mapped subscription id: " + subscriptionId);
		}
	}

	public SensorData getDataFromSensor(int sensorId) throws ESException
	{
		SensorData sensorData = null;
		AbstractSensorTask sensorTask = sensorTaskMap.get(sensorId);
		if (sensorTask == null)
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Unknown sensor type: " + sensorId);
		}
		else if (!SensorUtils.isPullSensor(sensorTask.getSensorType()))
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED, "this method is supported only for pull sensors.");
		}
		else if (sensorTask.isRunning())
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED, "this method is supported only for sensors that are not currently running. please unregister all listeners to the sensor and then call this method.");
		}
		else
		{
			sensorData = ((PullSensorTask) sensorTask).getCurrentSensorData(SensorUtils.getDefaultSensorConfig(sensorTask.getSensorType()));
		}

		return sensorData;
	}

	@Override
	public void setSensorConfig(int sensorId, String configKey, Object configValue) throws ESException
	{
		// TODO Auto-generated method stub
		
	}

	

}
