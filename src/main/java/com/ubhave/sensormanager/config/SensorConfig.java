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

package com.ubhave.sensormanager.config;

import com.ubhave.sensormanager.config.pull.BluetoothConfig;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.config.pull.LocationConfig;
import com.ubhave.sensormanager.config.pull.MicrophoneConfig;
import com.ubhave.sensormanager.config.pull.MotionSensorConfig;
import com.ubhave.sensormanager.config.pull.PhoneRadioConfig;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.config.pull.StepCounterConfig;
import com.ubhave.sensormanager.config.pull.WifiConfig;
import com.ubhave.sensormanager.config.push.PassiveLocationConfig;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SensorConfig extends AbstractConfig implements Cloneable
{
	/*
	 * Config Keys
	 */
	public final static String DATA_SET_RAW_VALUES = "RAW_DATA";
	public final static String DATA_EXTRACT_FEATURES = "EXTRACT_FEATURES";
	
	// data preferences
	public final static boolean GET_RAW_DATA = true;
	public final static boolean GET_PROCESSED_DATA = false;

	public SensorConfig clone()
	{
		SensorConfig clonedSensorConfig = new SensorConfig();
		for (String key : configParams.keySet())
		{
			Object obj = configParams.get(key);
			clonedSensorConfig.setParameter(key, obj);
		}
		return clonedSensorConfig;
	}
	
	public static SensorConfig getDefaultConfig(int sensorType)
	{
		SensorConfig sensorConfig = new SensorConfig();
		switch (sensorType)
		{
		case SensorUtils.SENSOR_TYPE_ACCELEROMETER:
		case SensorUtils.SENSOR_TYPE_GYROSCOPE:
		case SensorUtils.SENSOR_TYPE_MAGNETIC_FIELD:
			sensorConfig = MotionSensorConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_BLUETOOTH:
			sensorConfig = BluetoothConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_LOCATION:
			sensorConfig = LocationConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_MICROPHONE:
			sensorConfig = MicrophoneConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_WIFI:
			sensorConfig = WifiConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER:
		case SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER:
			sensorConfig = ContentReaderConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_PHONE_RADIO:
			sensorConfig = PhoneRadioConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION:
			sensorConfig = PassiveLocationConfig.getDefault();
			break;
		case SensorUtils.SENSOR_TYPE_STEP_COUNTER:
			sensorConfig = StepCounterConfig.getDefault();
			break;
		}
		sensorConfig.setParameter(PullSensorConfig.ADAPTIVE_SENSING_ENABLED, false);
		return sensorConfig;
	}
}
