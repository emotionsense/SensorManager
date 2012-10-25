package com.ubhave.sensormanager.dutycyling;

import java.util.LinkedList;
import java.util.Random;

import android.util.SparseArray;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.util.Utilities;

/*
 * this is based on MobiCom '11 paper: 
 * SociableSense: exploring the trade-offs of adaptive sampling and computation offloading for social sensing
 */

public class AdaptiveSensing implements SensorDataListener
{
	private static String TAG = "AdaptiveSensing";

	class PullSensorDetails
	{
		SensorInterface sensor;
		SensorDataClassifier classifier;
		SleepWindowListener listener;
		SensorConfig sensorConfig;
		int subscriptionId;
		double probability = Constants.PROBABILITY_INITIAL_VALUE;
	}

	private SparseArray<PullSensorDetails> sensorMap;
	private LinkedList<SensorData> sensorDataList;
	private static AdaptiveSensing adaptiveSensing;
	private static Object lock = new Object();
	private Random random;

	public static AdaptiveSensing getAdaptiveSensing() throws ESException
	{
		if (adaptiveSensing == null)
		{
			synchronized (lock)
			{
				if (adaptiveSensing == null)
				{
					adaptiveSensing = new AdaptiveSensing();
				}
			}
		}
		return adaptiveSensing;
	}

	private AdaptiveSensing()
	{
		sensorMap = new SparseArray<PullSensorDetails>();
		sensorDataList = new LinkedList<SensorData>();
		random = new Random();
	}

	public void registerSensor(ESSensorManagerInterface sensorManager, SensorInterface sensor, SensorDataClassifier classifier, SleepWindowListener listener) throws ESException
	{
		PullSensorDetails sensorDetails = new PullSensorDetails();
		sensorDetails.sensor = sensor;
		sensorDetails.classifier = classifier;
		sensorDetails.listener = listener;

		try
		{
			int subscriptionId = sensorManager.subscribeToSensorData(sensor.getSensorType(), this);
			sensorDetails.subscriptionId = subscriptionId;
		}
		catch (ESException exp)
		{
			ESLogger.error(TAG, exp);
			throw exp;
		}
		sensorMap.put(sensor.getSensorType(), sensorDetails);
	}

	public void unregisterSensor(ESSensorManagerInterface sensorManager, SensorInterface sensor) throws ESException
	{
		PullSensorDetails sensorDetails = sensorMap.get(sensor.getSensorType());
		if (sensorDetails != null)
		{
			sensorManager.unsubscribeFromSensorData(sensorDetails.subscriptionId);
			sensorMap.remove(sensor.getSensorType());
		}
	}

	public boolean isSensorRegistered(SensorInterface sensor)
	{
		if (sensorMap.get(sensor.getSensorType()) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private void updateSamplingInterval(SensorData data)
	{
		int sensorType = data.getSensorType();
		PullSensorDetails sensorDetails = sensorMap.get(sensorType);
		double probability = sensorDetails.probability;
		SensorDataClassifier classifier = sensorDetails.classifier;

		// classify as interesting or not
		if (classifier.isInteresting(data))
		{
			probability = probability + (Constants.ALPHA_VALUE * (1 - probability));
		}
		else
		{
			probability = probability - (Constants.ALPHA_VALUE * probability);
		}

		// check min,max bounds
		if (probability < Constants.MIN_PROBABILITY_VALUE)
		{
			probability = Constants.MIN_PROBABILITY_VALUE;
		}
		else if (probability > Constants.MAX_PROBABILITY_VALUE)
		{
			probability = Constants.MAX_PROBABILITY_VALUE;
		}

		// convert probability to sampling intervals in milliseconds
		long sleepWindowMilliSeconds = 1000;

		long senseWindowLengthMillis = (Long) sensorDetails.sensorConfig.get(SensorConfig.SENSE_WINDOW_LENGTH_MILLIS);

		while (true)
		{
			double randomNumber = random.nextDouble();
			if (randomNumber < probability)
			{
				break;
			}
			else
			{
				sleepWindowMilliSeconds += senseWindowLengthMillis;
			}
		}

		// update listener
		sensorDetails.listener.onSleepWindowLengthChanged(sleepWindowMilliSeconds);

	}

	public void onDataSensed(SensorData data)
	{
		synchronized (sensorDataList)
		{
			sensorDataList.addLast(data);
			sensorDataList.notify();
		}
	}

	class EventProcessor extends Thread
	{
		public void run()
		{

			while (true)
			{
				try
				{
					SensorData sensorData = null;
					synchronized (sensorDataList)
					{
						sensorDataList.wait();
						if (sensorDataList.size() == 0)
						{
							continue;
						}
						else
						{
							sensorData = sensorDataList.removeFirst();
						}
					}
					if (sensorData == null)
					{
						continue;
					}
					else
					{
						updateSamplingInterval(sensorData);
					}
				}
				catch (Exception exp)
				{
					ESLogger.error(TAG, exp);
					Utilities.sleep(60000);
				}

			}

		}
	}

}
