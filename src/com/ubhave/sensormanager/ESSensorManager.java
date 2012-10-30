package com.ubhave.sensormanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.dutycyling.AdaptiveSensing;
import com.ubhave.sensormanager.logs.ESLogger;
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

	private final SparseArray<AbstractSensorTask> sensorTaskMap;
	private final SubscriptionList subscriptionList;

	public static ESSensorManager getSensorManager(Context context) throws ESException
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
					sensorManager.setup();
					ESLogger.log(TAG, "started.");
				}
			}
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

	private void setup() throws ESException
	{
		// initial setup
		// register with battery sensor
		subscribeToSensorData(SensorUtils.SENSOR_TYPE_BATTERY, this);
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

	private AbstractSensorTask getSensorTask(int sensorId) throws ESException
	{
		AbstractSensorTask sensorTask = sensorTaskMap.get(sensorId);
		if (sensorTask == null)
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Unknown sensor type: " + sensorId);
		}
		return sensorTask;
	}

	public SensorData getDataFromSensor(int sensorId) throws ESException
	{
		SensorData sensorData = null;
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		if (!SensorUtils.isPullSensor(sensorTask.getSensorType()))
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED, "this method is supported only for pull sensors.");
		}
		else if (sensorTask.isRunning())
		{
			throw new ESException(ESException.OPERATION_NOT_SUPPORTED, "this method is supported only for sensors that are not currently running. please unregister all listeners to the sensor and then call this method.");
		}
		else
		{
			sensorData = ((PullSensorTask) sensorTask).getCurrentSensorData();
		}

		return sensorData;
	}

	public void setSensorConfig(int sensorId, String configKey, Object configValue) throws ESException
	{
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		SensorInterface sensor = sensorTask.getSensor();
		sensor.setSensorConfig(configKey, configValue);
	}

	public Object getSensorConfigValue(int sensorId, String configKey) throws ESException
	{
		AbstractSensorTask sensorTask = getSensorTask(sensorId);
		SensorInterface sensor = sensorTask.getSensor();
		return sensor.getSensorConfig(configKey);
	}

	public void setBatteryThresholdValue(int value) throws ESException
	{
		setSensorConfig(SensorUtils.SENSOR_TYPE_BATTERY, SensorConfig.LOW_BATTERY_THRESHOLD, value);
	}

	public Integer getBatteryThresholdValue(int value) throws ESException
	{
		return (Integer) getSensorConfigValue(SensorUtils.SENSOR_TYPE_BATTERY, SensorConfig.LOW_BATTERY_THRESHOLD);
	}

	public void enableAdaptiveSensing(int sensorId) throws ESException
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

	public void disableAdaptiveSensing(int sensorId) throws ESException
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
			sub.getListener().onCrossingLowBatteryThreshold(isBelowThreshold);
		}
	}

}
