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

package com.ubhave.sensormanager.classifier;

import java.util.ArrayList;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.BluetoothData;
import com.ubhave.sensormanager.data.pull.ESBluetoothDevice;

public class BluetoothDataClassifier extends SocialClassifier implements SensorDataClassifier
{
	@Override
	public boolean isInteresting(final SensorData sensorData, final SensorConfig sensorConfig)
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
