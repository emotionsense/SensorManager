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

package com.ubhave.sensormanager.data;

import com.ubhave.sensormanager.config.SensorConfig;

public abstract class SensorData
{
	// for pull sensor: this is the time at which sensing cycle is started
	// for push sensor: this is the time at which data is received by the sensor
	private final long sensorDataTimestamp;
	private SensorData prevSensorData;
	private SensorConfig sensorConfig;
	
	protected boolean isDataProcessed;

	public SensorData(long sensorTimestamp, SensorConfig config)
	{
		sensorDataTimestamp = sensorTimestamp;
		this.sensorConfig = config;
		isDataProcessed = false;
	}
	
	public void setDataProcessed(boolean value)
	{
		isDataProcessed = value;
	}
	
	public boolean isDataProcessed()
	{
		return isDataProcessed;
	}

	public long getTimestamp()
	{
		return sensorDataTimestamp;
	}

	public SensorConfig getSensorConfig()
	{
		return sensorConfig;
	}

	public SensorData getPrevSensorData()
	{
		return prevSensorData;
	}
	
	public abstract int getSensorType();

	public void setPrevSensorData(SensorData prevSensorData)
	{
		// when a sensordata object is set as prev to
		// another sensordata
		// object then its prev should be set to null to
		// avoid memory leaks
		if (prevSensorData != null)
		{
			prevSensorData.setPrevSensorData(null);
		}

		this.prevSensorData = prevSensorData;
	}
}
