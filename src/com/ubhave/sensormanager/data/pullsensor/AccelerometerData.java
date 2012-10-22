package com.ubhave.sensormanager.data.pullsensor;

import java.util.ArrayList;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class AccelerometerData extends SensorData
{

	private ArrayList<float[]> sensorReadings;

	public AccelerometerData(long senseStartTimestamp, ArrayList<float[]> sensorReadings)
	{
		super(senseStartTimestamp);
		this.sensorReadings = sensorReadings;
	}

	public ArrayList<float[]> getSensorReadings()
	{
		return sensorReadings;
	}

	public String getDataString()
	{
		StringBuilder sb = new StringBuilder();

		// check if the x y z values of all vectors are same
		// this will generally happen if phone is stationary
		// in this case, only log the x y z values once

		if (sensorReadings.size() > 0)
		{

			float x1, y1, z1;

			float[] data1 = sensorReadings.get(0);
			x1 = data1[0];
			y1 = data1[1];
			z1 = data1[2];

			boolean sameVectors = true;

			for (float[] data : sensorReadings)
			{
				if ((x1 == data[0]) && (y1 == data[1]) && (z1 == data[2]))
				{
					// ignore
				}
				else
				{
					sameVectors = false;
					break;
				}
			}

			if (sameVectors)
			{
				sb.append(x1 + "," + y1 + "," + z1 + ";");
			}
			else
			{
				for (float[] data : sensorReadings)
				{
					sb.append(data[0] + "," + data[1] + "," + data[2] + ";");
				}
			}
		}

		return sb.toString();
	}
	
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_ACCELEROMETER;
	}

}
