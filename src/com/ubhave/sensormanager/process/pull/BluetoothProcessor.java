package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.BluetoothData;
import com.ubhave.sensormanager.data.pull.ESBluetoothDevice;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class BluetoothProcessor extends AbstractProcessor
{
	public BluetoothProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public BluetoothData process(long pullSenseStartTimestamp, ArrayList<ESBluetoothDevice> btDevices, SensorConfig sensorConfig)
	{
		BluetoothData bluetoothData = new BluetoothData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			bluetoothData.setBluetoothDevices(btDevices);
		}
		return bluetoothData;
	}

}
