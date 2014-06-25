package com.ubhave.sensormanager.process.push;

import android.content.Context;
import android.location.Location;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.PassiveLocationData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class PassiveLocationProcessor extends AbstractProcessor {

	public PassiveLocationProcessor(Context context, boolean rw, boolean sp) {
		super(context, rw, sp);
	}

	public PassiveLocationData process(long pullSenseStartTimestamp, Location lastLocation, SensorConfig sensorConfig)
	{
		PassiveLocationData locationData = new PassiveLocationData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			locationData.setLocation(lastLocation);
		}
		return locationData;
	}

}
