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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.push.PhoneStateData;
import com.ubhave.sensormanager.process.push.PhoneStateProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PhoneStateSensor extends AbstractPushSensor
{
	private static final String TAG = "PhoneStateSensor";
	private static final String[] REQUIRED_PERMISSIONS = new String[]{
		Manifest.permission.PROCESS_OUTGOING_CALLS,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.READ_PHONE_STATE
	};

	private TelephonyManager telephonyManager;
	private PhoneStateListener phoneStateListener;

	private static PhoneStateSensor phoneStateSensor;
	private static Object lock = new Object();

	public static PhoneStateSensor getSensor(final Context context) throws ESException
	{
		if (phoneStateSensor == null)
		{
			synchronized (lock)
			{
				if (phoneStateSensor == null)
				{
					if (allPermissionsGranted(context, REQUIRED_PERMISSIONS))
					{
						phoneStateSensor = new PhoneStateSensor(context);
					}
					else throw new ESException(ESException. PERMISSION_DENIED, SensorUtils.SENSOR_NAME_PHONE_STATE);
				}
			}
		}
		return phoneStateSensor;
	}

	private PhoneStateSensor(final Context context)
	{
		super(context);
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		phoneStateListener = new PhoneStateListener()
		{

			public void onCallStateChanged(int state, String incomingNumber)
			{
				String stateString = "N/A";
				int stateType = 0;
				switch (state)
				{
				case TelephonyManager.CALL_STATE_IDLE:
					stateType = PhoneStateData.CALL_STATE_IDLE;
					stateString = "CALL_STATE_IDLE";
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					stateType = PhoneStateData.CALL_STATE_OFFHOOK;
					stateString = "CALL_STATE_OFFHOOK";
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					stateType = PhoneStateData.CALL_STATE_RINGING;
					stateString = "CALL_STATE_RINGING";
					break;
				}

				logOnDataSensed(stateType, stateString, incomingNumber);
			}

			public void onCellLocationChanged(CellLocation location)
			{
				if (location != null)
				{
					logOnDataSensed(PhoneStateData.ON_CELL_LOCATION_CHANGED, location.toString(), null);
				}
			}

			public void onDataActivity(int direction)
			{
				logOnDataSensed(PhoneStateData.ON_DATA_ACTIVITY, getDataActivityString(direction), null);
			}

			public void onDataConnectionStateChanged(int state)
			{
				logOnDataSensed(PhoneStateData.ON_DATA_CONNECTION_STATE_CHANGED, getDataConnectionStateString(state), null);
			}

			public void onDataConnectionStateChanged(int state, int networkType)
			{
				// ignore this, the logging is performed in the above method
				// onDataConnectionStateChanged(int state).
			}

			public void onServiceStateChanged(ServiceState serviceState)
			{
				if (serviceState != null)
				{
					String serviceStateStr = getServiceStateString(serviceState.getState());
					logOnDataSensed(PhoneStateData.ON_SERVICE_STATE_CHANGED, serviceStateStr + " " + serviceState.toString(), null);
				}
			}

		};

	}

	private void logOnDataSensed(int eventType, String data, String number)
	{
		if (isSensing)
		{
			PhoneStateProcessor processor = (PhoneStateProcessor) getProcessor();
			if (processor != null)
			{
				PhoneStateData phoneStateData = processor.process(System.currentTimeMillis(), sensorConfig.clone(), eventType, data, number);
				onDataSensed(phoneStateData);
			}
		}
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{
		String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		logOnDataSensed(PhoneStateData.CALL_STATE_OUTGOING, "CALL_STATE_OUTGOING ", outgoingNumber);
	}

	protected IntentFilter[] getIntentFilters()
	{
		IntentFilter[] filters = new IntentFilter[1];
		filters[0] = new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
		return filters;
	}

	public static String getServiceStateString(int serviceState)
	{
		switch (serviceState)
		{
		case ServiceState.STATE_EMERGENCY_ONLY:
			return "STATE_EMERGENCY_ONLY";
		case ServiceState.STATE_IN_SERVICE:
			return "STATE_IN_SERVICE";
		case ServiceState.STATE_OUT_OF_SERVICE:
			return "STATE_OUT_OF_SERVICE";
		case ServiceState.STATE_POWER_OFF:
			return "STATE_POWER_OFF";
		default:
			return "";
		}
	}

	public static String getDataActivityString(int direction)
	{
		switch (direction)
		{
		case TelephonyManager.DATA_ACTIVITY_NONE:
			return "DATA_ACTIVITY_NONE";
		case TelephonyManager.DATA_ACTIVITY_IN:
			return "DATA_ACTIVITY_IN";
		case TelephonyManager.DATA_ACTIVITY_OUT:
			return "DATA_ACTIVITY_OUT";
		case TelephonyManager.DATA_ACTIVITY_INOUT:
			return "DATA_ACTIVITY_INOUT";
		case TelephonyManager.DATA_ACTIVITY_DORMANT:
			return "DATA_ACTIVITY_DORMANT";
		default:
			return "";

		}
	}

	public static String getDataConnectionStateString(int dataConnectionState)
	{
		switch (dataConnectionState)
		{
		case TelephonyManager.DATA_DISCONNECTED:
			return "DATA_DISCONNECTED";
		case TelephonyManager.DATA_CONNECTING:
			return "DATA_CONNECTING";
		case TelephonyManager.DATA_CONNECTED:
			return "DATA_CONNECTED";
		case TelephonyManager.DATA_SUSPENDED:
			return "DATA_SUSPENDED";
		default:
			return "";
		}
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_PHONE_STATE;
	}

	protected boolean startSensing()
	{
		int interestedEvents = PhoneStateListener.LISTEN_CALL_STATE | PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE | PhoneStateListener.LISTEN_SERVICE_STATE;
		telephonyManager.listen(phoneStateListener, interestedEvents);
		return true;
	}

	protected void stopSensing()
	{
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
	}

}
