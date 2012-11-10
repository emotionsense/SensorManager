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
		return "Level " + level + "/" + scale + " Temperature " + temperature + " Voltage " + voltage + " Plugged " + plugged + " Status " + status + " " + getStatusString(status) + " Health " + health + " " + getHealthString(health);
	}

	public static String getHealthString(int healthValue)
	{
		switch (healthValue)
		{
		case BatteryManager.BATTERY_HEALTH_DEAD:
			return "BATTERY_HEALTH_DEAD";
		case BatteryManager.BATTERY_HEALTH_GOOD:
			return "BATTERY_HEALTH_GOOD";
		case BatteryManager.BATTERY_HEALTH_OVERHEAT:
			return "BATTERY_HEALTH_OVERHEAT";
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
			return "BATTERY_HEALTH_OVER_VOLTAGE";
		case BatteryManager.BATTERY_HEALTH_UNKNOWN:
			return "BATTERY_HEALTH_UNKNOWN";
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
			return "BATTERY_HEALTH_UNSPECIFIED_FAILURE";
		default:
			return "UNKNOWN";
		}
	}

	public static String getStatusString(int statusValue)
	{
		switch (statusValue)
		{
		case BatteryManager.BATTERY_STATUS_CHARGING:
			return "BATTERY_STATUS_CHARGING";
		case BatteryManager.BATTERY_STATUS_DISCHARGING:
			return "BATTERY_STATUS_DISCHARGING";
		case BatteryManager.BATTERY_STATUS_FULL:
			return "BATTERY_STATUS_FULL";
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
			return "BATTERY_STATUS_NOT_CHARGING";
		case BatteryManager.BATTERY_STATUS_UNKNOWN:
			return "BATTERY_STATUS_UNKNOWN";
		default:
			return "UNKNOWN";
		}
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_BATTERY;
	}

}
