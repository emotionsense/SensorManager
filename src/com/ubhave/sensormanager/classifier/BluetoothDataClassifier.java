package com.ubhave.sensormanager.classifier;

import java.util.ArrayList;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.BluetoothData;
import com.ubhave.sensormanager.data.pullsensor.ESBluetoothDevice;

public class BluetoothDataClassifier extends SocialClassifier implements SensorDataClassifier
{

	public boolean isInteresting(SensorData sensorData)
	{
		BluetoothData data = (BluetoothData) sensorData;
		BluetoothData prevData = (BluetoothData) sensorData.getPrevSensorData();

		String[] currDevices = getDeviceMacs(data);
		String[] prevDevices = getDeviceMacs(prevData);

		if (areSameDeviceAddrSets(prevDevices, currDevices))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	protected String[] getDeviceMacs(BluetoothData data)
	{
		String[] deviceList = null;
		if (data != null)
		{
			ArrayList<ESBluetoothDevice> list = data.getBluetoothDevices();
			if (list != null)
			{
				deviceList = new String[list.size()];
				int i = 0;
				for (ESBluetoothDevice btDevice : list)
				{
					deviceList[i++] = btDevice.getBluetoothDeviceAddress();
				}
			}
		}
		return deviceList;
	}

}
