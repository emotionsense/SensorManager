package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.BluetoothData;
import com.ubhave.sensormanager.data.pullsensor.ESBluetoothDevice;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class BluetoothProcessor extends AbstractProcessor
{

	public BluetoothProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}

	public BluetoothData process(long pullSenseStartTimestamp, ArrayList<ESBluetoothDevice> btDevices,
			SensorConfig sensorConfig)
	{
		BluetoothData bluetoothData = new BluetoothData(pullSenseStartTimestamp, sensorConfig);

		if (setRawData)
		{
			bluetoothData.setBluetoothDevices(btDevices);
		}

		if (setProcessedData)
		{
			// process
		}

		return bluetoothData;
	}

}
