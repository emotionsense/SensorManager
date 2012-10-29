package com.ubhave.sensormanager.data.pullsensor;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import android.location.Location;

public class LocationData extends SensorData
{

	Location location;

	public LocationData(long senseStartTimestamp, Location location, SensorConfig sensorConfig)
	{
		super(senseStartTimestamp, sensorConfig);
		this.location = location;
	}

	public Location getLocation()
	{
		return location;
	}

	public String getDataString()
	{
		StringBuilder sb = new StringBuilder();

		if (location != null)
		{
			sb.append(location.getLatitude());
			sb.append(",");
			sb.append(location.getLongitude());
			sb.append(",");
			sb.append(location.getAccuracy());
			sb.append(",");
			sb.append(location.getSpeed());
			sb.append(",");
			sb.append(location.getBearing());
			sb.append(",");
			sb.append(location.getProvider());
			sb.append(",");
			sb.append(location.getSpeed());
			sb.append(",");
			sb.append(location.getTime());
			sb.append(";");
		}
		else
		{
			sb.append("NO_LOCATION_DATA");
		}

		return sb.toString();
	}
	
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_LOCATION;
	}

}
