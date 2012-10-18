package com.ubhave.sensormanager.sensors;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.pull.AccelerometerSensor;
import com.ubhave.sensormanager.sensors.pull.BluetoothSensor;
import com.ubhave.sensormanager.sensors.pull.LocationSensor;
import com.ubhave.sensormanager.sensors.pull.MicrophoneSensor;
import com.ubhave.sensormanager.sensors.pull.WifiSensor;
import com.ubhave.sensormanager.sensors.push.BatterySensor;
import com.ubhave.sensormanager.sensors.push.PhoneStateSensor;
import com.ubhave.sensormanager.sensors.push.ProximitySensor;
import com.ubhave.sensormanager.sensors.push.ScreenSensor;
import com.ubhave.sensormanager.sensors.push.SmsSensor;

public class SensorUtils
{
	private static String TAG = "SensorUtils";
	
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
	
	public final static String SENSOR_NAME_ACCELEROMETER = "accelerometer";
	public final static String SENSOR_NAME_BATTERY = "battery";
	public final static String SENSOR_NAME_BLUETOOTH = "bluetooth";
	public final static String SENSOR_NAME_LOCATION = "location";
	public final static String SENSOR_NAME_MICROPHONE = "microphone";
	public final static String SENSOR_NAME_PHONE_STATE = "phonestate";
	public final static String SENSOR_NAME_PROXIMITY = "proximity";
	public final static String SENSOR_NAME_SCREEN = "screen";
	public final static String SENSOR_NAME_SMS = "sms";
	public final static String SENSOR_NAME_WIFI = "wifi";
	
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
	
	public static boolean isPullSensor(int sensorType)
	{
		switch (sensorType)
		{
		case SENSOR_TYPE_ACCELEROMETER:
		case SENSOR_TYPE_BLUETOOTH:
		case SENSOR_TYPE_LOCATION:
		case SENSOR_TYPE_MICROPHONE:
		case SENSOR_TYPE_WIFI:
			return true;
		default:
			return false;
		}
	}
	
	public static ArrayList<SensorInterface> getAllSensors(Context applicationContext)
	{
		return getSensorList(ALL_SENSORS, applicationContext);
	}

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
				ESLogger.error(TAG, e);
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
		case SENSOR_TYPE_MICROPHONE: return MicrophoneSensor.getMicrophoneSensor(context);
		case SENSOR_TYPE_WIFI: return WifiSensor.getWifiSensor(context);
		case SENSOR_TYPE_BATTERY: return BatterySensor.getBatterySensor(context);
		case SENSOR_TYPE_PHONE_STATE: return PhoneStateSensor.getPhoneStateSensor(context);
		case SENSOR_TYPE_PROXIMITY: return ProximitySensor.getProximitySensor(context);
		case SENSOR_TYPE_SCREEN: return ScreenSensor.getScreenSensor(context);
		case SENSOR_TYPE_SMS: return SmsSensor.getSmsSensor(context);
		default: throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Unknown sensor id");
		}	
	}
	
	public static int getSensorType(String sensorName) throws ESException
	{
		if (sensorName.equals(SENSOR_NAME_ACCELEROMETER))
		{
			return SENSOR_TYPE_ACCELEROMETER;
		}
		else if (sensorName.equals(SENSOR_NAME_BATTERY))
		{
			return SENSOR_TYPE_BATTERY;
		}
		else if (sensorName.equals(SENSOR_NAME_BLUETOOTH))
		{
			return SENSOR_TYPE_BLUETOOTH;
		}
		else if (sensorName.equals(SENSOR_NAME_LOCATION))
		{
			return SENSOR_TYPE_LOCATION;
		}
		else if (sensorName.equals(SENSOR_NAME_MICROPHONE))
		{
			return SENSOR_TYPE_MICROPHONE;
		}
		else if (sensorName.equals(SENSOR_NAME_PHONE_STATE))
		{
			return SENSOR_TYPE_PHONE_STATE;
		}
		else if (sensorName.equals(SENSOR_NAME_PROXIMITY))
		{
			return SENSOR_TYPE_PROXIMITY;
		}
		else if (sensorName.equals(SENSOR_NAME_SCREEN))
		{
			return SENSOR_TYPE_SCREEN;
		}
		else if (sensorName.equals(SENSOR_NAME_SMS))
		{
			return SENSOR_TYPE_SMS;
		}
		else if (sensorName.equals(SENSOR_NAME_WIFI))
		{
			return SENSOR_TYPE_WIFI;
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_NAME, "unknown sensor name " + sensorName);
		}
	}
	
	public static String getSensorName(int sensorType) throws ESException
	{
		switch (sensorType)
		{
		case SensorUtils.SENSOR_TYPE_ACCELEROMETER:
			return SENSOR_NAME_ACCELEROMETER;
		case SensorUtils.SENSOR_TYPE_BATTERY:
			return SENSOR_NAME_BATTERY;
		case SensorUtils.SENSOR_TYPE_BLUETOOTH:
			return SENSOR_NAME_BLUETOOTH;
		case SensorUtils.SENSOR_TYPE_LOCATION:
			return SENSOR_NAME_LOCATION;
		case SensorUtils.SENSOR_TYPE_MICROPHONE:
			return SENSOR_NAME_MICROPHONE;
		case SensorUtils.SENSOR_TYPE_PHONE_STATE:
			return SENSOR_NAME_PHONE_STATE;
		case SensorUtils.SENSOR_TYPE_PROXIMITY:
			return SENSOR_NAME_PROXIMITY;
		case SensorUtils.SENSOR_TYPE_SCREEN:
			return SENSOR_NAME_SCREEN;
		case SensorUtils.SENSOR_TYPE_SMS:
			return SENSOR_NAME_SMS;
		case SensorUtils.SENSOR_TYPE_WIFI:
			return SENSOR_NAME_WIFI;
		default:
			throw new ESException(ESException.UNKNOWN_SENSOR_NAME, "unknown sensor type " + sensorType);
		}
	}
	
}
