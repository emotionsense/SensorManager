package com.ubhave.sensormanager.classifier;

import android.location.Location;

import com.ubhave.sensormanager.config.Utilities;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.LocationData;

public class LocationDataClassifier implements SensorDataClassifier
{

	public boolean isInteresting(SensorData sensorData)
	{
		LocationData data = (LocationData) sensorData;
		LocationData prevData = (LocationData) sensorData.getPrevSensorData();

		Location currLoc = null;
		Location prevLoc = null;

		if (data != null)
		{
			currLoc = data.getLocation();
		}

		if (prevData != null)
		{
			prevLoc = prevData.getLocation();
		}

		if (Utilities.areSameLocations(currLoc, prevLoc))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
