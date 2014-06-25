package com.ubhave.sensormanager.data.pushsensor;

import android.location.Location;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PassiveLocationData extends SensorData {
	private Location location;

	public PassiveLocationData(long senseStartTimestamp, SensorConfig sensorConfig)
	{
		super(senseStartTimestamp, sensorConfig);
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public Location getLocation()
	{
		return location;
	}

	@Override
	public int getSensorType() {
		return SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION;
	}

}
