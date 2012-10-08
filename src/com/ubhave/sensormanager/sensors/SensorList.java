package com.ubhave.sensormanager.sensors;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.pull.AccelerometerSensor;
import com.ubhave.sensormanager.sensors.pull.BluetoothSensor;
import com.ubhave.sensormanager.sensors.pull.LocationSensor;
import com.ubhave.sensormanager.sensors.pull.WifiSensor;
import com.ubhave.sensormanager.sensors.push.BatterySensor;
import com.ubhave.sensormanager.sensors.push.PhoneStateSensor;
import com.ubhave.sensormanager.sensors.push.ProximitySensor;
import com.ubhave.sensormanager.sensors.push.ScreenSensor;
import com.ubhave.sensormanager.sensors.push.SmsSensor;

public class SensorList
{
	private final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	private final static int SENSOR_TYPE_BATTERY = 5002;
	private final static int SENSOR_TYPE_BLUETOOTH = 5003;
	private final static int SENSOR_TYPE_LOCATION = 5004;
	private final static int SENSOR_TYPE_MICROPHONE = 5005;
	private final static int SENSOR_TYPE_PHONE_STATE = 5006;
	private final static int SENSOR_TYPE_PROXIMITY = 5007;
	private final static int SENSOR_TYPE_SCREEN = 5008;
	private final static int SENSOR_TYPE_SMS = 5009;
	private final static int SENSOR_TYPE_WIFI = 5010;
	
	private final static int[] ALL_SENSORS = new int[]{
		SENSOR_TYPE_ACCELEROMETER,
		SENSOR_TYPE_BLUETOOTH,
		SENSOR_TYPE_LOCATION,
		SENSOR_TYPE_MICROPHONE,
		SENSOR_TYPE_WIFI,
		SENSOR_TYPE_BATTERY,
		SENSOR_TYPE_PHONE_STATE,
		SENSOR_TYPE_PROXIMITY,
		SENSOR_TYPE_SCREEN,
		SENSOR_TYPE_SMS
	};
	
	// Currently Unused
//	private final static int[] PULL_SENSORS = new int[]{
//		SENSOR_TYPE_ACCELEROMETER,
//		SENSOR_TYPE_BLUETOOTH,
//		SENSOR_TYPE_LOCATION,
//		SENSOR_TYPE_MICROPHONE,
//		SENSOR_TYPE_WIFI
//	};
//	
//	private final static int[] PUSH_SENSORS = new int[]{
//		SENSOR_TYPE_BATTERY,
//		SENSOR_TYPE_PHONE_STATE,
//		SENSOR_TYPE_PROXIMITY,
//		SENSOR_TYPE_SCREEN,
//		SENSOR_TYPE_SMS
//	};
	
	public static ArrayList<SensorInterface> getAllSensors(Context applicationContext)
	{
		return getSensorList(ALL_SENSORS, applicationContext);
	}
	
	// Currently Unused
//	public static ArrayList<SensorInterface> getPullSensors(Context applicationContext)
//	{
//		return getSensorList(PULL_SENSORS, applicationContext);
//	}
//	
//	public static ArrayList<SensorInterface> getPushSensors(Context applicationContext)
//	{
//		return getSensorList(PUSH_SENSORS, applicationContext);
//	}

	private static ArrayList<SensorInterface> getSensorList(int[] list, Context applicationContext)
	{
		ArrayList<SensorInterface> sensors = new ArrayList<SensorInterface>();
		for (int sensorId : list)
		{
			try {
				SensorInterface sensor = getSensor(sensorId, applicationContext);
				sensors.add(sensor);
			}
			catch (ESException e)
			{
				// Permission denied or unknown id
			}
		}
		return sensors;
	}
	
	private static SensorInterface getSensor(int id, Context context) throws ESException
	{
		switch(id)
		{
		case SENSOR_TYPE_ACCELEROMETER: return AccelerometerSensor.getAccelerometerSensor(context);
		case SENSOR_TYPE_BLUETOOTH: return BluetoothSensor.getBluetoothSensor(context);
		case SENSOR_TYPE_LOCATION: return LocationSensor.getLocationSensor(context);
		case SENSOR_TYPE_WIFI: return WifiSensor.getWifiSensor(context);
		case SENSOR_TYPE_BATTERY: return BatterySensor.getBatterySensor(context);
		case SENSOR_TYPE_PHONE_STATE: return PhoneStateSensor.getPhoneStateSensor(context);
		case SENSOR_TYPE_PROXIMITY: return ProximitySensor.getProximitySensor(context);
		case SENSOR_TYPE_SCREEN: return ScreenSensor.getScreenSensor(context);
		case SENSOR_TYPE_SMS: return SmsSensor.getSmsSensor(context);
		default: throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Unknown sensor id");
		}	
	}
	
}
