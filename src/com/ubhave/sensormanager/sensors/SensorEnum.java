/* **************************************************
 Copyright (c) 2014, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk

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

package com.ubhave.sensormanager.sensors;


public enum SensorEnum
{
	ACCELEROMETER (SensorUtils.SENSOR_NAME_ACCELEROMETER, SensorUtils.SENSOR_TYPE_ACCELEROMETER, SensorUtils.SENSOR_GROUP_PULL),
	BATTERY (SensorUtils.SENSOR_NAME_BATTERY, SensorUtils.SENSOR_TYPE_BATTERY, SensorUtils.SENSOR_GROUP_PUSH),
	BLUETOOTH (SensorUtils.SENSOR_NAME_BLUETOOTH, SensorUtils.SENSOR_TYPE_BLUETOOTH, SensorUtils.SENSOR_GROUP_PULL),
	LOCATION (SensorUtils.SENSOR_NAME_LOCATION, SensorUtils.SENSOR_TYPE_LOCATION, SensorUtils.SENSOR_GROUP_PULL),
	MICROPHONE (SensorUtils.SENSOR_NAME_MICROPHONE, SensorUtils.SENSOR_TYPE_MICROPHONE, SensorUtils.SENSOR_GROUP_PULL),
	PHONE_STATE (SensorUtils.SENSOR_NAME_PHONE_STATE, SensorUtils.SENSOR_TYPE_PHONE_STATE, SensorUtils.SENSOR_GROUP_PUSH),
	PROXIMITY (SensorUtils.SENSOR_NAME_PROXIMITY, SensorUtils.SENSOR_TYPE_PROXIMITY, SensorUtils.SENSOR_GROUP_PUSH),
	SCREEN (SensorUtils.SENSOR_NAME_SCREEN, SensorUtils.SENSOR_TYPE_SCREEN, SensorUtils.SENSOR_GROUP_PUSH),
	SMS (SensorUtils.SENSOR_NAME_SMS, SensorUtils.SENSOR_TYPE_SMS, SensorUtils.SENSOR_GROUP_PUSH),
	WIFI (SensorUtils.SENSOR_NAME_WIFI, SensorUtils.SENSOR_TYPE_WIFI, SensorUtils.SENSOR_GROUP_PULL),
	CONNECTION_STATE (SensorUtils.SENSOR_NAME_CONNECTION_STATE, SensorUtils.SENSOR_TYPE_CONNECTION_STATE, SensorUtils.SENSOR_GROUP_PUSH),
	SMS_CONTENT_READER (SensorUtils.SENSOR_NAME_SMS_CONTENT_READER, SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER, SensorUtils.SENSOR_GROUP_PULL),
	CALL_CONTENT_READER(SensorUtils.SENSOR_NAME_CALL_CONTENT_READER, SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER, SensorUtils.SENSOR_GROUP_PULL),
	GYROSCOPE (SensorUtils.SENSOR_NAME_GYROSCOPE, SensorUtils.SENSOR_TYPE_GYROSCOPE, SensorUtils.SENSOR_GROUP_PULL),
	LIGHT (SensorUtils.SENSOR_NAME_LIGHT, SensorUtils.SENSOR_TYPE_LIGHT, SensorUtils.SENSOR_GROUP_ENVIRONMENT),
	PHONE_RADIO (SensorUtils.SENSOR_NAME_PHONE_RADIO, SensorUtils.SENSOR_TYPE_PHONE_RADIO, SensorUtils.SENSOR_GROUP_PULL),
	CONNECTION_STRENGTH (SensorUtils.SENSOR_NAME_CONNECTION_STRENGTH, SensorUtils.SENSOR_TYPE_CONNECTION_STRENGTH, SensorUtils.SENSOR_GROUP_PUSH),
	PASSIVE_LOCATION (SensorUtils.SENSOR_NAME_PASSIVE_LOCATION, SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION, SensorUtils.SENSOR_GROUP_PUSH),
	AMBIENT_TEMPERATURE (SensorUtils.SENSOR_NAME_AMBIENT_TEMPERATURE, SensorUtils.SENSOR_TYPE_AMBIENT_TEMPERATURE, SensorUtils.SENSOR_GROUP_ENVIRONMENT),
	PRESSURE (SensorUtils.SENSOR_NAME_PRESSURE, SensorUtils.SENSOR_TYPE_PRESSURE, SensorUtils.SENSOR_GROUP_ENVIRONMENT),
	HUMIDITY (SensorUtils.SENSOR_NAME_HUMIDITY, SensorUtils.SENSOR_TYPE_HUMIDITY, SensorUtils.SENSOR_GROUP_ENVIRONMENT),
	MAGNETIC_FIELD (SensorUtils.SENSOR_NAME_MAGNETIC_FIELD, SensorUtils.SENSOR_TYPE_MAGNETIC_FIELD, SensorUtils.SENSOR_GROUP_PULL),
	STEP_COUNTER (SensorUtils.SENSOR_NAME_STEP_COUNTER, SensorUtils.SENSOR_TYPE_STEP_COUNTER, SensorUtils.SENSOR_GROUP_PULL),
	USER_INTERACTION (SensorUtils.SENSOR_NAME_INTERACTION, SensorUtils.SENSOR_TYPE_INTERACTION, SensorUtils.SENSOR_GROUP_USER);
	
	private final String name;
	private final int type;
	private final int group;

	private SensorEnum(String name, final int type, final int group)
	{
		this.name = name;
		this.type = type;
		this.group = group;
	}
	
	public boolean isPull()
	{
		return group == SensorUtils.SENSOR_GROUP_PULL;
	}
	
	public boolean isPush()
	{
		return group == SensorUtils.SENSOR_GROUP_PUSH;
	}
	
	public boolean isEnvironment()
	{
		return group == SensorUtils.SENSOR_GROUP_ENVIRONMENT;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getType()
	{
		return type;
	}
}
