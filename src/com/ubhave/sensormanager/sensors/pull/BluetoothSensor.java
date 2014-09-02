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

package com.ubhave.sensormanager.sensors.pull;

import java.util.ArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.pull.BluetoothConfig;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.pull.BluetoothData;
import com.ubhave.sensormanager.data.pull.ESBluetoothDevice;
import com.ubhave.sensormanager.process.pull.BluetoothProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BluetoothSensor extends AbstractPullSensor
{
	private static final String TAG = "BluetoothSensor";
	private static final String[] REQUIRED_PERMISSIONS = new String[] {
		Manifest.permission.BLUETOOTH,
		Manifest.permission.BLUETOOTH_ADMIN
	};

	private static BluetoothSensor bluetoothSensor;
	private static Object lock = new Object();

	private ArrayList<ESBluetoothDevice> btDevices;
	private BluetoothAdapter bluetooth = null;
	
	private int cyclesRemaining, sensorStartState;
	private BluetoothData bluetoothData;
	private BroadcastReceiver receiver;

	public static BluetoothSensor getSensor(final Context context) throws ESException
	{
		if (bluetoothSensor == null)
		{
			synchronized (lock)
			{
				if (bluetoothSensor == null)
				{
					if (allPermissionsGranted(context, REQUIRED_PERMISSIONS))
					{
						bluetoothSensor = new BluetoothSensor(context);
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED, SensorUtils.SENSOR_NAME_BLUETOOTH);
					}
				}
			}
		}
		return bluetoothSensor;
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private BluetoothSensor(final Context context)
	{
		super(context);
		btDevices = new ArrayList<ESBluetoothDevice>();
		if (android.os.Build.VERSION.SDK_INT >= 18)
		{
			BluetoothManager bluetoothMan = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
			bluetooth = bluetoothMan.getAdapter();
		}
		else
		{
			bluetooth = BluetoothAdapter.getDefaultAdapter();
		}
		if (bluetooth == null)
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d(TAG, "Device does not support Bluetooth");
			}
			return;
		}

		// Create a BroadcastReceiver for ACTION_FOUND, sent when a device is
		// discovered
		receiver = new BroadcastReceiver()
		{

			public void onReceive(Context context, Intent intent)
			{
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action))
				{
					// Get the BluetoothDevice object from the Intent
					String deviceAddr = ((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)).getAddress();
					String deviceName = ((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)).getName();
					int rssi = (int) intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

					ESBluetoothDevice esBluetoothDevice = new ESBluetoothDevice(System.currentTimeMillis(), deviceAddr, deviceName, rssi);
					if (!(btDevices.contains(esBluetoothDevice)))
					{
						btDevices.add(esBluetoothDevice);
					}
				}
				else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
				{
					cyclesRemaining -= 1;
					if (cyclesRemaining > 0)
					{
						bluetooth.startDiscovery();
					}
					else
					{
						notifySenseCyclesComplete();
					}
				}
			}
		};
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_BLUETOOTH;
	}

	protected BluetoothData getMostRecentRawData()
	{
		return bluetoothData;
	}
	
	private boolean shouldForceEnable()
	{
		try
		{
			return (Boolean) sensorConfig.getParameter(BluetoothConfig.FORCE_ENABLE_SENSOR);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return BluetoothConfig.DEFAULT_FORCE_ENABLE;
		}
	}

	protected void processSensorData()
	{
		BluetoothProcessor processor = (BluetoothProcessor) getProcessor();
		bluetoothData = processor.process(pullSenseStartTimestamp, btDevices, sensorConfig.clone());
	}

	protected boolean startSensing()
	{
		btDevices.clear();
		sensorStartState = bluetooth.getState();
		if (!bluetooth.isEnabled() && shouldForceEnable())
		{
			bluetooth.enable();
			while (!bluetooth.isEnabled())
			{
				try
				{
					Thread.sleep(100);
				}
				catch (Exception exp)
				{
					exp.printStackTrace();
				}
			}
		}
		
		if (bluetooth.isEnabled())
		{
			cyclesRemaining = (Integer) sensorConfig.getParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES);
			
			// Register the BroadcastReceiver: note that this does NOT start a scan
			IntentFilter found = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			IntentFilter finished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			applicationContext.registerReceiver(receiver, found);
			applicationContext.registerReceiver(receiver, finished);
			
			bluetooth.startDiscovery();
			return true;
		}
		else
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d(TAG, "Bluetooth is not enabled.");
			}
			return false;
		}
	}

	// Called when a scan is finished
	protected void stopSensing()
	{
		if (bluetooth != null)
		{
			bluetooth.cancelDiscovery();
			if (sensorStartState == BluetoothAdapter.STATE_OFF && bluetooth.isEnabled())
			{
				bluetooth.disable();
			}
		}

		applicationContext.unregisterReceiver(receiver);
	}

}
