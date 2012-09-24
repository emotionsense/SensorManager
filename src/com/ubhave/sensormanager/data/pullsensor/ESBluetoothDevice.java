package com.ubhave.sensormanager.data.pullsensor;

public class ESBluetoothDevice
{

	private long timestamp;
	private String bluetoothDeviceAddress;
	private String bluetoothDeviceName;
	private float rssi;

	public ESBluetoothDevice(long ts, String btAddr, String btName, float btRssi)
	{
		this.timestamp = ts;
		this.bluetoothDeviceAddress = btAddr;
		this.bluetoothDeviceName = btName;
		this.rssi = btRssi;
	}

	public String getBluetoothDeviceAddress()
	{
		return bluetoothDeviceAddress;
	}

	public String getBluetoothDeviceName()
	{
		return bluetoothDeviceName;
	}

	public float getRssi()
	{
		return rssi;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}

	public boolean equals(ESBluetoothDevice btDevice)
	{
		if (bluetoothDeviceAddress.equals(btDevice.getBluetoothDeviceAddress()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String toString()
	{
		return "Timestamp: " + timestamp + " Device address: " + bluetoothDeviceAddress + " Device name: " + bluetoothDeviceName + " Rssi: " + rssi;
	}
}
