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

package com.ubhave.sensormanager.sensors;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.classifier.AccelerometerDataClassifier;
import com.ubhave.sensormanager.classifier.BluetoothDataClassifier;
import com.ubhave.sensormanager.classifier.LocationDataClassifier;
import com.ubhave.sensormanager.classifier.MicrophoneDataClassifier;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.classifier.WifiDataClassifier;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.sensors.env.AmbientTemperatureSensor;
import com.ubhave.sensormanager.sensors.env.HumiditySensor;
import com.ubhave.sensormanager.sensors.env.LightSensor;
import com.ubhave.sensormanager.sensors.env.PressureSensor;
import com.ubhave.sensormanager.sensors.pull.AccelerometerSensor;
import com.ubhave.sensormanager.sensors.pull.BluetoothSensor;
import com.ubhave.sensormanager.sensors.pull.CallContentReaderSensor;
import com.ubhave.sensormanager.sensors.pull.GyroscopeSensor;
import com.ubhave.sensormanager.sensors.pull.LocationSensor;
import com.ubhave.sensormanager.sensors.pull.MagneticFieldSensor;
import com.ubhave.sensormanager.sensors.pull.MicrophoneSensor;
import com.ubhave.sensormanager.sensors.pull.PhoneRadioSensor;
import com.ubhave.sensormanager.sensors.pull.SMSContentReaderSensor;
import com.ubhave.sensormanager.sensors.pull.StepCounterSensor;
import com.ubhave.sensormanager.sensors.pull.WifiSensor;
import com.ubhave.sensormanager.sensors.push.BatterySensor;
import com.ubhave.sensormanager.sensors.push.ConnectionStateSensor;
import com.ubhave.sensormanager.sensors.push.ConnectionStrengthSensor;
import com.ubhave.sensormanager.sensors.push.PassiveLocationSensor;
import com.ubhave.sensormanager.sensors.push.PhoneStateSensor;
import com.ubhave.sensormanager.sensors.push.ProximitySensor;
import com.ubhave.sensormanager.sensors.push.ScreenSensor;
import com.ubhave.sensormanager.sensors.push.SmsSensor;

import java.util.ArrayList;

public class SensorUtils
{
	public final static int SENSOR_GROUP_PULL = 0;
	public final static int SENSOR_GROUP_PUSH = 1;
	public final static int SENSOR_GROUP_ENVIRONMENT = 2;
	public final static int SENSOR_GROUP_USER = 3;
	
	public final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	public final static int SENSOR_TYPE_BATTERY = 5002;
	public final static int SENSOR_TYPE_BLUETOOTH = 5003;
	public final static int SENSOR_TYPE_LOCATION = 5004;
	public final static int SENSOR_TYPE_MICROPHONE = 5005;
	public final static int SENSOR_TYPE_PHONE_STATE = 5006;
	public final static int SENSOR_TYPE_PROXIMITY = 5007;
	public final static int SENSOR_TYPE_SCREEN = 5008;
	public final static int SENSOR_TYPE_SMS = 5009;
	public final static int SENSOR_TYPE_WIFI = 5010;
	public final static int SENSOR_TYPE_CONNECTION_STATE = 5011;
	public final static int SENSOR_TYPE_SMS_CONTENT_READER = 5013;
	public final static int SENSOR_TYPE_CALL_CONTENT_READER = 5014;
	public final static int SENSOR_TYPE_GYROSCOPE = 5016;
	public final static int SENSOR_TYPE_LIGHT = 5017;
	public final static int SENSOR_TYPE_PHONE_RADIO = 5018;
	public final static int SENSOR_TYPE_CONNECTION_STRENGTH = 5019;
	public final static int SENSOR_TYPE_PASSIVE_LOCATION = 5020;
	public final static int SENSOR_TYPE_AMBIENT_TEMPERATURE = 5021;
	public final static int SENSOR_TYPE_PRESSURE = 5022;
	public final static int SENSOR_TYPE_HUMIDITY = 5023;
	public final static int SENSOR_TYPE_MAGNETIC_FIELD = 5024;
	public final static int SENSOR_TYPE_STEP_COUNTER = 5025;
	public final static int SENSOR_TYPE_INTERACTION = 5026;
	
	public final static String SENSOR_NAME_ACCELEROMETER = "Accelerometer";
	public final static String SENSOR_NAME_BATTERY = "Battery";
	public final static String SENSOR_NAME_BLUETOOTH = "Bluetooth";
	public final static String SENSOR_NAME_LOCATION = "Location";
	public final static String SENSOR_NAME_MICROPHONE = "Microphone";
	public final static String SENSOR_NAME_PHONE_STATE = "PhoneState";
	public final static String SENSOR_NAME_PROXIMITY = "Proximity";
	public final static String SENSOR_NAME_SCREEN = "Screen";
	public final static String SENSOR_NAME_SMS = "SMS";
	public final static String SENSOR_NAME_WIFI = "WiFi";
	public final static String SENSOR_NAME_CONNECTION_STATE = "ConnectionState";
	public final static String SENSOR_NAME_CONNECTION_STRENGTH = "ConnectionStrength";
	public final static String SENSOR_NAME_SMS_CONTENT_READER = "SMSContentReader";
	public final static String SENSOR_NAME_CALL_CONTENT_READER = "CallContentReader";
	public final static String SENSOR_NAME_GYROSCOPE = "Gyroscope";
	public final static String SENSOR_NAME_LIGHT = "Light";
	public final static String SENSOR_NAME_PHONE_RADIO = "PhoneRadio";
	public final static String SENSOR_NAME_PASSIVE_LOCATION = "PassiveLocation";
	public final static String SENSOR_NAME_AMBIENT_TEMPERATURE = "AmbientTemperature";
	public final static String SENSOR_NAME_PRESSURE = "Pressure";
	public final static String SENSOR_NAME_HUMIDITY = "Humidity";
	public final static String SENSOR_NAME_MAGNETIC_FIELD = "MagneticField";
	public final static String SENSOR_NAME_STEP_COUNTER = "StepCounter";
	public final static String SENSOR_NAME_INTERACTION = "Interaction";

	private static SensorEnum getSensor(int sensorType) throws ESException
	{
		for (SensorEnum s : SensorEnum.values())
		{
			if (s.getType() == sensorType)
			{
				return s;
			}
		}
		throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Unknown sensor type " + sensorType);
	}

	public static String getSensorName(int sensorType) throws ESException
	{
		return getSensor(sensorType).getName();
	}

	public static boolean isPullSensor(final int sensorType) throws ESException
	{
		return getSensor(sensorType).isPull();
	}

	public static int getSensorType(final String sensorName) throws ESException
	{
		for (SensorEnum s : SensorEnum.values())
		{
			if (s.getName().equals(sensorName))
			{
				return s.getType();
			}
		}
		throw new ESException(ESException.UNKNOWN_SENSOR_NAME, "Unknown sensor name " + sensorName);
	}

	public static ArrayList<SensorInterface> getAllSensors(final Context applicationContext)
	{
		ArrayList<SensorInterface> sensors = new ArrayList<>();
		for (SensorEnum s : SensorEnum.values())
		{
			try
			{
				SensorInterface sensor = getSensor(s.getType(), applicationContext);
				sensors.add(sensor);
			}
			catch (ESException e)
			{
				if (GlobalConfig.shouldLog())
				{
					Log.d("SensorUtils", "Warning: " + e.getMessage());
				}
			}
		}
		return sensors;
	}

	private static SensorInterface getSensor(int id, Context context) throws ESException
	{
		switch (id)
		{
		case SENSOR_TYPE_ACCELEROMETER:
			return AccelerometerSensor.getSensor(context);
		case SENSOR_TYPE_BATTERY:
			return BatterySensor.getSensor(context);
		case SENSOR_TYPE_BLUETOOTH:
			return BluetoothSensor.getSensor(context);
		case SENSOR_TYPE_LOCATION:
			return LocationSensor.getSensor(context);
		case SENSOR_TYPE_MICROPHONE:
			return MicrophoneSensor.getSensor(context);
		case SENSOR_TYPE_PHONE_STATE:
			return PhoneStateSensor.getSensor(context);	
		case SENSOR_TYPE_PROXIMITY:
			return ProximitySensor.getSensor(context);
		case SENSOR_TYPE_SCREEN:
			return ScreenSensor.getSensor(context);
		case SENSOR_TYPE_SMS:
			return SmsSensor.getSensor(context);
		case SENSOR_TYPE_WIFI:
			return WifiSensor.getSensor(context);
		case SENSOR_TYPE_CONNECTION_STATE:
			return ConnectionStateSensor.getSensor(context);
		case SENSOR_TYPE_SMS_CONTENT_READER:
			return SMSContentReaderSensor.getSensor(context);
		case SENSOR_TYPE_CALL_CONTENT_READER:
			return CallContentReaderSensor.getSensor(context);
		case SENSOR_TYPE_GYROSCOPE:
			return GyroscopeSensor.getSensor(context);
		case SENSOR_TYPE_LIGHT:
			return LightSensor.getSensor(context);
		case SENSOR_TYPE_PHONE_RADIO:
			return PhoneRadioSensor.getPhoneRadioSensor(context);
		case SENSOR_TYPE_CONNECTION_STRENGTH:
			return ConnectionStrengthSensor.getSensor(context);
		case SENSOR_TYPE_PASSIVE_LOCATION:
			return PassiveLocationSensor.getSensor(context);
		case SENSOR_TYPE_AMBIENT_TEMPERATURE:
			return AmbientTemperatureSensor.getSensor(context);
		case SENSOR_TYPE_PRESSURE:
			return PressureSensor.getSensor(context);
		case SENSOR_TYPE_HUMIDITY:
			return HumiditySensor.getSensor(context);
		case SENSOR_TYPE_MAGNETIC_FIELD:
			return MagneticFieldSensor.getSensor(context);
		case SENSOR_TYPE_STEP_COUNTER:
			return StepCounterSensor.getSensor(context);
		default:
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Unknown sensor id: " + id);
		}
	}

	public static SensorDataClassifier getSensorDataClassifier(int sensorType) throws ESException
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
		case SensorUtils.SENSOR_TYPE_WIFI:
			return new WifiDataClassifier();
		default:
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Sensor data classifier not support for the sensor type " + sensorType);
		}
	}
}
