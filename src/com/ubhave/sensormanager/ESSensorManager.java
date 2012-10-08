package com.ubhave.sensormanager;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.SensorList;
import com.ubhave.sensormanager.tasks.AbstractSensorTask;
import com.ubhave.sensormanager.tasks.PullSensorTask;
import com.ubhave.sensormanager.tasks.Subscription;
import com.ubhave.sensormanager.tasks.SubscriptionList;

public class ESSensorManager implements ESSensorManagerInterface
{
	private static final String TAG = "ESSensorManager";

	private static ESSensorManager sensorManager;
	private static Object lock = new Object();

	private final SparseArray<AbstractSensorTask> sensorTaskMap;
	private final SubscriptionList subscriptionList;

	public static ESSensorManager getSensorManager(Context context) throws ESException
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
		return sensorManager;
	}

	private ESSensorManager(final Context appContext)
	{
		sensorTaskMap = new SparseArray<AbstractSensorTask>();
		subscriptionList = new SubscriptionList();

		ArrayList<SensorInterface> sensors = SensorList.getAllSensors(appContext);
		for (SensorInterface aSensor : sensors)
		{
			PullSensorTask pullSensorTask = new PullSensorTask(aSensor);
			sensorTaskMap.put(aSensor.getSensorType(), pullSensorTask);
		}
	}

	public synchronized int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException
	{
		AbstractSensorTask task = sensorTaskMap.get(sensorId);
		if (task != null)
		{
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

	public SensorData getDataFromSensor(int sensorId)
	{
		return null;
	}

	// public void startAllSensors()
	// {
	// // start sensor threads
	// for (SensorTask task : sensorTaskMap.values())
	// {
	// task.start();
	// }
	// }

	// public synchronized void stopAllSensors()
	// {
	// for (SensorTask task : sensorTaskMap.values())
	// {
	// task.stopTask();
	// }
	// try
	// {
	// onSensorsStopped();
	// }
	// catch (ESException exp)
	// {
	// ESLogger.error(TAG, exp);
	// }
	// }

	// public synchronized void pauseAllSensors(long pauseLength) throws
	// ESException
	// {
	// for (SensorTask task : sensorTaskMap.values())
	// {
	// if (!task.isStopped())
	// {
	// task.pauseTask(pauseLength);
	// }
	// }
	// }

	// private void onSensorsStarted()
	// {
	// startNotification();
	// startServiceAlarm();
	// }

	// private void onSensorsStopped() throws ESException
	// {
	// stopNotification();
	// stopServiceAlarm();
	// }

	// private void stopNotification() throws ESException
	// {
	// ExperienceSenseService.getExperienceSenseService().stopNotification();
	// }

	// private void stopServiceAlarm()
	// {
	// ServiceAlarmReceiver.cancelAlarm(applicationContext);
	// }

	// private void startNotification()
	// {
	// // wait for experience service to start for a max of 10 seconds
	// // as this is called during system start-up
	// long totalWaitTime = 0;
	// long sleepTime = 1000;
	// ExperienceSenseService esService = null;
	// while ((totalWaitTime < (long) (10 * 1000)) && (esService == null))
	// {
	// Utilities.sleep(sleepTime);
	// totalWaitTime += sleepTime;
	// try
	// {
	// esService = ExperienceSenseService.getExperienceSenseService();
	// }
	// catch (ESException e)
	// {
	// // ignore
	// }
	// }
	//
	// if (esService != null)
	// {
	// esService.startNotification();
	// }
	// else
	// {
	// ESLogger.error(TAG,
	// " ExperienceSenseService.getExperienceSenseService() returned null.");
	// }
	// }

	// private void startServiceAlarm()
	// {
	// ServiceAlarmReceiver.startAlarm(applicationContext);
	// }

	// public synchronized void startSensor(int sensorType) throws ESException
	// {
	// if (sensorTaskMap.containsKey(sensorType))
	// {
	// SensorTask sensorTask = sensorTaskMap.get(sensorType);
	// sensorTask.startTask();
	// onSensorsStarted();
	// }
	// else
	// {
	// throw new ESException(ESException.UNKNOWN_SENSOR_TYPE,
	// "Invalid sensor type: " + sensorType);
	// }
	// }
	//
	// public synchronized void stopSensor(int sensorType) throws ESException
	// {
	// if (sensorTaskMap.containsKey(sensorType))
	// {
	// SensorTask sensorTask = sensorTaskMap.get(sensorType);
	// sensorTask.stopTask();
	// }
	// else
	// {
	// throw new ESException(ESException.UNKNOWN_SENSOR_TYPE,
	// "Invalid sensor type: " + sensorType);
	// }
	// }

	// public synchronized void pauseSensor(int sensorType, long pauseLength)
	// throws ESException
	// {
	// if (sensorTaskMap.containsKey(sensorType))
	// {
	// SensorTask sensorTask = sensorTaskMap.get(sensorType);
	// sensorTask.pauseTask(pauseLength);
	// }
	// else
	// {
	// throw new ESException(ESException.UNKNOWN_SENSOR_TYPE,
	// "Invalid sensor type: " + sensorType);
	// }
	// }

	// public synchronized boolean isSensorStopped(int sensorType) throws
	// ESException
	// {
	// if (sensorTaskMap.containsKey(sensorType))
	// {
	// SensorTask sensorTask = sensorTaskMap.get(sensorType);
	// return sensorTask.isStopped();
	// }
	// else
	// {
	// throw new ESException(ESException.UNKNOWN_SENSOR_TYPE,
	// "Invalid sensor type: " + sensorType);
	// }
	// }
}
