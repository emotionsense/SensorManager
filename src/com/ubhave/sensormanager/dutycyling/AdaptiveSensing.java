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
 * This algorithm is fully described in the MobiCom '11 paper: 
 * SociableSense: Exploring the Trade-Offs of Adaptive Sampling and Computation Off Loading for Social Sensing
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

		// convert probability to sampling intervals in
		// milliseconds
		long sleepWindowMilliSeconds = 1000;

		long senseWindowLengthMillis = (Long) sensorDetails.sensorConfig.getParameter(SensorConfig.SENSE_WINDOW_LENGTH_MILLIS);

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

	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
	}

}
