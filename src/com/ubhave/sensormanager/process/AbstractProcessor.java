package com.ubhave.sensormanager.process;

import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.process.pull.AccelerometerProcessor;
import com.ubhave.sensormanager.process.pull.ApplicationProcessor;
import com.ubhave.sensormanager.process.pull.BluetoothProcessor;
import com.ubhave.sensormanager.process.pull.CallContentReaderProcessor;
import com.ubhave.sensormanager.process.pull.CameraProcessor;
import com.ubhave.sensormanager.process.pull.GyroscopeProcessor;
import com.ubhave.sensormanager.process.pull.LocationProcessor;
import com.ubhave.sensormanager.process.pull.MicrophoneProcessor;
import com.ubhave.sensormanager.process.pull.SMSContentReaderProcessor;
import com.ubhave.sensormanager.process.pull.WifiProcessor;
import com.ubhave.sensormanager.process.push.BatteryProcessor;
import com.ubhave.sensormanager.process.push.ConnectionStateProcessor;
import com.ubhave.sensormanager.process.push.LightProcessor;
import com.ubhave.sensormanager.process.push.PhoneStateProcessor;
import com.ubhave.sensormanager.process.push.ProximityProcessor;
import com.ubhave.sensormanager.process.push.SMSProcessor;
import com.ubhave.sensormanager.process.push.ScreenProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public abstract class AbstractProcessor
{
	public static AbstractProcessor getProcessor(Context c, int sensorType, boolean setRawData, boolean setProcessedData) throws ESException
	{
		if (!setRawData && !setProcessedData)
		{
			throw new ESException(ESException.INVALID_STATE, "No data (raw/processed) requested from the processor");
		}
		
		switch (sensorType)
		{
		case SensorUtils.SENSOR_TYPE_ACCELEROMETER:
			return new AccelerometerProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_APPLICATION:
			return new ApplicationProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_BLUETOOTH:
			return new BluetoothProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_LOCATION:
			return new LocationProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_MICROPHONE:
			return new MicrophoneProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_WIFI:
			return new WifiProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_BATTERY:
			return new BatteryProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_CONNECTION_STATE:
			return new ConnectionStateProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_PHONE_STATE:
			return new PhoneStateProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_SCREEN:
			return new ScreenProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_SMS:
			return new SMSProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_PROXIMITY:
			return new ProximityProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER:
			return new CallContentReaderProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER:
			return new SMSContentReaderProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_CAMERA:
			return new CameraProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_GYROSCOPE:
            return new GyroscopeProcessor(c, setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_LIGHT:
			return new LightProcessor(c, setRawData, setProcessedData);
		default:
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "No processor defined for this sensor.");
		}
	}
	
	protected final boolean setRawData, setProcessedData;
	protected final Context appContext;
	
	public AbstractProcessor(final Context context, final boolean rw, final boolean sp)
	{
		this.appContext = context;
		this.setRawData = rw;
		this.setProcessedData = sp;
	}
	
}
