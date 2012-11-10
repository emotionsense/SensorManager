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

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SensorClassifiers
{

	public static SensorDataClassifier getSensorClassifier(int sensorType) throws ESException
	{
		switch (sensorType)
		{
		case SensorUtils.SENSOR_TYPE_ACCELEROMETER:
			return new AccelerometerDataClassifier();
		case SensorUtils.SENSOR_TYPE_BLUETOOTH:
			return new BluetoothDataClassifier();
		case SensorUtils.SENSOR_TYPE_LOCATION:
			return new LocationDataClassifier();
		case SensorUtils.SENSOR_TYPE_MICROPHONE:
			return new MicrophoneDataClassifier();
		case SensorUtils.SENSOR_TYPE_PHONE_STATE:
			return new PhoneStateDataClassifier();
		case SensorUtils.SENSOR_TYPE_SCREEN:
			return new ScreenDataClassifier();
		case SensorUtils.SENSOR_TYPE_SMS:
			return new SMSDataClassifier();
		case SensorUtils.SENSOR_TYPE_WIFI:
			return new WifiDataClassifier();
		default:
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "No classifier available");
		}
	}
}
