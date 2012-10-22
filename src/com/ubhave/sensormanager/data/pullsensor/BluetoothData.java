package com.ubhave.sensormanager.data.pullsensor;

import java.util.ArrayList;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BluetoothData extends SensorData
{

	ArrayList<ESBluetoothDevice> bluetoothDevices;

	public BluetoothData(long senseStartTimestamp, ArrayList<ESBluetoothDevice> btDevices)
	{
		super(senseStartTimestamp);
		bluetoothDevices = btDevices;
	}

	public ArrayList<ESBluetoothDevice> getBluetoothDevices()
	{
		return bluetoothDevices;
	}

	public String getDataString()
	{
		StringBuilder sb = new StringBuilder();
		if (bluetoothDevices.size() > 0)
		{
			for (ESBluetoothDevice esBtDevice : bluetoothDevices)
			{
				sb.append(esBtDevice.getTimestamp() + ",");
				sb.append(esBtDevice.getBluetoothDeviceAddress() + ",");
				sb.append(esBtDevice.getBluetoothDeviceName() + ",");
				sb.append(esBtDevice.getRssi() + "; ");
			}
		}
		else
		{
			sb.append("NO_DEVICES");
		}
		return sb.toString();
	}
	
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_BLUETOOTH;
	}

}
