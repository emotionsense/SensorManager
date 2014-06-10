/* **************************************************
 Copyright (c) 2014, Idiap
Hugues Salamin, hugues.salamin@idiap.ch

This file was developed to add phone radio sensor to the SensorManager library
from https://github.com/nlathia/SensorManager.

The SensorManager library was developed as part of the EPSRC Ubhave (Ubiquitous
and Social Computing for Positive Behaviour Change) Project. For more
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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.ConnectionStrengthData;
import com.ubhave.sensormanager.process.push.ConnectionStrengthProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ConnectionStrengthSensor extends AbstractPushSensor
{
	private static final String TAG = "ConnectionSrengthSensor";
	private static final String[] REQUIRED_PERMISSIONS = new String[] { Manifest.permission.READ_PHONE_STATE };

	private volatile static ConnectionStrengthSensor connectionSensor;
	StrengthListener sensorEventListener;

	public static ConnectionStrengthSensor getConnectionStrengthSensor(
			final Context context) throws ESException
	{
		/*
		 * Implement a double checked lock, using volatile. The result variable
		 * is for speed reason (avoid reading the volatile member too many time
		 */
		ConnectionStrengthSensor result = connectionSensor;
		if (result == null)
		{
			synchronized (ConnectionStrengthSensor.class)
			{
				result = connectionSensor;
				if (result == null)
				{
					if (allPermissionsGranted(context, REQUIRED_PERMISSIONS))
					{
						connectionSensor = result = new ConnectionStrengthSensor(
								context);
						Log.d(TAG, "Connection Strenght sensor created ");
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED,
								SensorUtils.SENSOR_NAME_CONNECTION_STRENGTH);
					}
				}
			}
		}
		return result;
	}

	private ConnectionStrengthSensor(Context context)
	{
		super(context);
		sensorEventListener = new StrengthListener(this);

	}

	private class StrengthListener extends PhoneStateListener implements
			SensorDataListener {
		/*
		 * This Class listen and save change in the signal strength. Assume GSM
		 * network.
		 */
		private ConnectionStrengthSensor parent;

		public StrengthListener(ConnectionStrengthSensor p) {
			super();
			parent = p;
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			try {
				int data = signalStrength.getGsmSignalStrength();
				ConnectionStrengthProcessor processor = (ConnectionStrengthProcessor) getProcessor();
				ConnectionStrengthData strengthData = processor.process(
						System.currentTimeMillis(), sensorConfig.clone(),
						data);
				Log.d(TAG, "Phone Strength change");
				onDataSensed(strengthData);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onDataSensed(SensorData data) {
			parent.onDataSensed(data);

		}

		@Override
		public void onCrossingLowBatteryThreshold(boolean isBelowThreshold) {
			// TODO Auto-generated method stub

		}
	};

	protected IntentFilter[] getIntentFilters()
	{
		return null;
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{
		// ignore
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_CONNECTION_STRENGTH;
	}

	protected boolean startSensing()
	{
		TelephonyManager telephonyManager = (TelephonyManager) applicationContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(sensorEventListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		Log.d(TAG, "Strart listening Phone Strength change");
		return true;
	}

	protected void stopSensing()
	{
		TelephonyManager telephonyManager = (TelephonyManager) applicationContext
				.getSystemService(Context.TELEPHONY_SERVICE);

		telephonyManager.listen(sensorEventListener,
				PhoneStateListener.LISTEN_NONE);
	}

}
