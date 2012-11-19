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

package com.ubhave.sensormanager.data.pushsensor;

import android.os.BatteryManager;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BatteryData extends SensorData
{
	private int level;
	private int scale;
	private int temperature;
	private int voltage;
	private int plugged;
	private int status;
	private int health;

	public BatteryData(long timestamp, int level, int scale, int temperature, int voltage, int plugged, int status, int health, SensorConfig sensorConfig)
	{
		super(timestamp, sensorConfig);
		this.level = level;
		this.scale = scale;
		this.temperature = temperature;
		this.voltage = voltage;
		this.plugged = plugged;
		this.status = status;
		this.health = health;
	}

	public int getBatteryLevel()
	{
		return level;
	}
	
	public int getScale()
	{
		return scale;
	}
	
	public int getTemperature()
	{
		return temperature;
	}
	
	public int getVoltage()
	{
		return voltage;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public int getHealth()
	{
		return health;
	}

	public boolean isCharging()
	{
		if (status == BatteryManager.BATTERY_STATUS_CHARGING)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getDataString()
	{
		return "removed";
//		return "Level " + level + "/" + scale + " Temperature " + temperature + " Voltage " + voltage + " Plugged " + plugged + " Status " + status + " " + getStatusString(status) + " Health " + health + " " + getHealthString(health);
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_BATTERY;
	}

}
