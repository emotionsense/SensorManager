package com.ubhave.sensormanager.classifier;

import android.location.Location;

import com.ubhave.sensormanager.config.Constants;
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

		if (areSameLocations(currLoc, prevLoc))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
    private boolean areSameLocations(Location loc1, Location loc2)
    {
        if ((loc1 != null) && (loc2 != null))
        {
            if (loc1.distanceTo(loc2) < Constants.LOCATION_CHANGE_DISTANCE_THRESHOLD)
            {
                return true;
            }
        }
        if ((loc1 == null) && (loc2 == null))
        {
            return true;
        }
        return false;
    }


}
