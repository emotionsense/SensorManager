/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.sensormanager.classifier;

import java.util.ArrayList;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.pull.MotionSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AccelerometerData;

public class AccelerometerDataClassifier implements SensorDataClassifier
{
	@Override
	public boolean isInteresting(final SensorData sensorData, final SensorConfig sensorConfig)
	{

		AccelerometerData data = (AccelerometerData) sensorData;
		ArrayList<float[]> sensorReadings = data.getSensorReadings();

		ArrayList<float[][]> axisData = new ArrayList<float[][]>();

		if (sensorReadings.size() <= 9)
		{
			// too less data: stationary
			return false;
		}

		int vectorsIndex = 0;
		int noOfSets = 3;
		for (int setCount = 0; setCount < noOfSets; setCount++)
		{
			int size = 0;
			if (setCount < noOfSets - 1)
			{
				size = sensorReadings.size() / noOfSets;
			}
			else
			{
				size = sensorReadings.size() - ((noOfSets - 1) * (sensorReadings.size() / noOfSets));
			}
			float[][] axisSet = new float[size][3];
			for (int i = 0; i < axisSet.length; i++)
			{
				axisSet[i][0] = sensorReadings.get(vectorsIndex)[0];
				axisSet[i][1] = sensorReadings.get(vectorsIndex)[1];
				axisSet[i][2] = sensorReadings.get(vectorsIndex)[2];
				vectorsIndex++;
			}
			axisData.add(axisSet);
		}

		int status = 0; // Increment when apparently moving, decrement when not

		// Work out magnitude of accelerations for each set of three axis
		// magnitudes and put them here
		ArrayList<float[]> scalars = new ArrayList<float[]>();

		// Each value 'data' is of the form {{x0, y0, z0}, ..., {x24, y24, z24}}
		for (float[][] readings : axisData)
		{
			float[] set = new float[readings.length];
			// Each set of triples {{x, y z}, ...}

			for (int i = 0; i < readings.length; i++)
			{
				// Each triple {x, y, z}
				float temp = 0; // Store result of calculation so far
				for (int j = 0; j < 3; j++)
				{
					temp += readings[i][j] * readings[i][j]; // So we calculate
																// x**2 + y**2 +
																// z**2
				}
				set[i] = temp; // And this is the value for the ith set of
								// triples
			}
			scalars.add(set); // Set of scalars for a sampling interval
		}

		// Now compute the mean of each set
		float[] means = new float[axisData.size()];
		for (int i = 0; i < means.length; i++)
		{
			float mean = 0.0f;
			float[] scs = scalars.get(i);
			for (int j = 0; j < scs.length; j++)
			{
				mean += scs[j];
			}
			mean /= scs.length;
			means[i] = mean;
		}

		// And the standard deviation of each set
		double[] stddevs = new double[scalars.size()];
		int motionThreshold = (Integer) sensorConfig.getParameter(MotionSensorConfig.MOTION_THRESHOLD);
		for (int i = 0; i < stddevs.length; i++)
		{
			double stddev = 0.0;
			float[] scs = scalars.get(i);
			for (int j = 0; j < scs.length; j++)
			{
				stddev += (means[i] - scs[j]) * (means[i] - scs[j]);
			}
			stddev /= scs.length;
			stddev = Math.sqrt(stddev);
			stddevs[i] = stddev;

			if (stddev < motionThreshold)
			{
				// We don't seem to be moving
				status -= 1;
			}
			else
			{
				// We do seem to be moving
				status += 1;
			}
		}

		if (status < 0)
		{
			// stationary
			return false;
		}
		else
		{
			return true;
		}
	}

}
