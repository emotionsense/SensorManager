package com.ubhave.sensormanager.process.pull;

import java.util.List;

import android.content.Context;
import android.location.Location;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.LocationData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class LocationProcessor extends AbstractProcessor
{
	public LocationProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public LocationData process(long pullSenseStartTimestamp, final List<Location> lastLocation, final SensorConfig sensorConfig)
	{
		LocationData locationData = new LocationData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			locationData.setLocations(lastLocation);
		}
		return locationData;
	}

}
