/*
 * Bluetooth monitor
 */

package com.ubhave.sensormanager.sensors.pull;

import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.Utilities;
import com.ubhave.sensormanager.data.pullsensor.BluetoothData;
import com.ubhave.sensormanager.data.pullsensor.ESBluetoothDevice;
import com.ubhave.sensormanager.logs.ESLogger;

public class BluetoothSensor extends AbstractPullSensor
{

	private static final String TAG = "BluetoothSensor";

	private ArrayList<ESBluetoothDevice> btDevices;
	private BluetoothAdapter bluetooth = null;
	private int cyclesRemaining;

	private static BluetoothSensor bluetoothSensor;
	private static Object lock = new Object();

	public static BluetoothSensor getBluetoothSensor(Context context) throws ESException
	{
		if (bluetoothSensor == null)
		{
			synchronized (lock)
			{
				if (bluetoothSensor == null)
				{
					if (permissionGranted(context, "android.permission.BLUETOOTH")
							&& permissionGranted(context, "android.permission.BLUETOOTH_ADMIN"))
					{
						bluetoothSensor = new BluetoothSensor(context);
					}
					else throw new ESException(ESException. PERMISSION_DENIED, "Bluetooth Sensor : Permission not Granted");
				}
			}
		}
		return bluetoothSensor;
	}

	private BluetoothSensor(Context context)
	{
		super(context);
		btDevices = new ArrayList<ESBluetoothDevice>();
		bluetooth = BluetoothAdapter.getDefaultAdapter();
		if (bluetooth == null)
		{
			ESLogger.log(TAG, "Device does not support Bluetooth");
			return;
		}

		// Create a BroadcastReceiver for ACTION_FOUND, sent when a device is
		// discovered
		BroadcastReceiver receiver = new BroadcastReceiver()
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
						ESLogger.log(TAG, "Found Bluetooth device: " + esBluetoothDevice.toString());
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

		// Register the BroadcastReceiver: note that this does NOT start a scan
		// or anything
		IntentFilter found = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		IntentFilter finished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		applicationContext.registerReceiver(receiver, found);
		applicationContext.registerReceiver(receiver, finished);
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return Constants.SENSOR_TYPE_BLUETOOTH;
	}

	protected BluetoothData getMostRecentRawData()
	{
		BluetoothData bluetoothData = new BluetoothData(pullSenseStartTimestamp, btDevices);
		return bluetoothData;
	}

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		btDevices.clear();

		if (!bluetooth.isEnabled())
		{
			bluetooth.enable();
			while (!bluetooth.isEnabled())
			{
				Utilities.sleep(100);
			}
		}
		cyclesRemaining = (Integer) sensorConfig.get(SensorConfig.NUMBER_OF_SAMPLING_CYCLES);
		bluetooth.startDiscovery();
		return true;
	}

	// Called when a scan is finished
	protected void stopSensing()
	{
		if (bluetooth != null)
		{
			bluetooth.cancelDiscovery();
			bluetooth.disable();
		}
	}

}
