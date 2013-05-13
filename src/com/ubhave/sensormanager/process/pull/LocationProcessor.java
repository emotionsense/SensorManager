package com.ubhave.sensormanager.process.pull;

import android.location.Location;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.LocationData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class LocationProcessor extends AbstractProcessor
{

	public LocationProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
	}

	public LocationData process(long pullSenseStartTimestamp, Location lastLocation, SensorConfig sensorConfig)
	{
		LocationData locationData = new LocationData(pullSenseStartTimestamp, sensorConfig);

		if (setRawData)
		{
			locationData.setLocation(lastLocation);
		}

		if (setProcessedData)
		{
			// process
		}

		return locationData;
	}

}
