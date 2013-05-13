package com.ubhave.sensormanager.process;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.process.pull.AudioProcessor;
import com.ubhave.sensormanager.process.push.BatteryProcessor;
import com.ubhave.sensormanager.process.push.ConnectionStateProcessor;
import com.ubhave.sensormanager.process.push.PhoneStateProcessor;
import com.ubhave.sensormanager.process.push.SMSProcessor;
import com.ubhave.sensormanager.process.push.ScreenProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public abstract class AbstractProcessor
{
	public static AbstractProcessor getProcessor(int sensorType, boolean setRawData, boolean setProcessedData) throws ESException
	{
		switch (sensorType)
		{
		case SensorUtils.SENSOR_TYPE_MICROPHONE:
			return new AudioProcessor(setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_BATTERY:
			return new BatteryProcessor(setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_CONNECTION_STATE:
			return new ConnectionStateProcessor(setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_PHONE_STATE:
			return new PhoneStateProcessor(setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_SCREEN:
			return new ScreenProcessor(setRawData, setProcessedData);
		case SensorUtils.SENSOR_TYPE_SMS:
			return new SMSProcessor(setRawData, setProcessedData);
		default:
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "No processor defined for this sensor.");
		}
	}
	
	protected final boolean setRawData, setProcessedData;
	
	public AbstractProcessor(final boolean rw, final boolean sp)
	{
		setRawData = rw;
		setProcessedData = sp;
	}
	
}
