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

package com.ubhave.sensormanager.data.push;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ScreenData extends SensorData
{
	private int screenStatus;

	public static final int SCREEN_OFF = 0;
	public static final int SCREEN_ON = 1;
	public static final int SCREEN_UNKNOWN = 2;

	public ScreenData(long recvTimestamp, SensorConfig sensorConfig)
	{
		super(recvTimestamp, sensorConfig);
	}
	
	public void setStatus(int s)
	{
		screenStatus = s;
	}
	
	public boolean isOff()
	{
		return screenStatus == SCREEN_OFF;
	}

	public boolean isOn()
	{
		return screenStatus == SCREEN_ON;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SCREEN;
	}
}
