/* **************************************************
 Copyright (c) 2014, Idiap
 Olivier Bornet, olivier.bornet@idiap.ch

This file was developed to add phone radio sensor to the SensorManager library
from https://github.com/nlathia/SensorManager.

The SensorManager library was developed as part of the EPSRC Ubhave (Ubiquitous
and Social Computing for Positive Behaviour Change) Project. For more
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

package com.ubhave.sensormanager.data.pull;

import java.util.ArrayList;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PhoneRadioDataList extends SensorData
{
	private ArrayList<PhoneRadioData> phoneRadios;

	public PhoneRadioDataList(long senseStartTimestamp, SensorConfig sensorConfig)
	{
		super(senseStartTimestamp, sensorConfig);
	}
	
	public void setPhoneRadios(ArrayList<PhoneRadioData> phoneRadios)
	{
		this.phoneRadios = phoneRadios;
	}

	public ArrayList<PhoneRadioData> getPhoneRadios()
	{
		return phoneRadios;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_PHONE_RADIO;
	}

}
