/* **************************************************
 Copyright (c) 2014, Idiap
 Hugues Salamin, hugues.salamin@idiap.ch

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

package com.ubhave.sensormanager.process.push;

import android.content.Context;
import android.location.Location;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.PassiveLocationData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class PassiveLocationProcessor extends AbstractProcessor
{

	public PassiveLocationProcessor(Context context, boolean rw, boolean sp)
	{
		super(context, rw, sp);
	}

	public PassiveLocationData process(long pullSenseStartTimestamp, Location lastLocation, SensorConfig sensorConfig)
	{
		PassiveLocationData locationData = new PassiveLocationData(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			locationData.setLocation(lastLocation);
		}
		return locationData;
	}

}
