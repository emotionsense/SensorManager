/* **************************************************
 Copyright (c) 2014, Idiap
 Olivier Bornet, olivier.bornet@idiap.ch

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

package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.PhoneRadioData;
import com.ubhave.sensormanager.data.pull.PhoneRadioDataList;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class PhoneRadioProcessor extends AbstractProcessor
{
	public PhoneRadioProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public PhoneRadioDataList process(long pullSenseStartTimestamp, ArrayList<PhoneRadioData> phoneRadioDatas, SensorConfig sensorConfig)
	{
		PhoneRadioDataList phoneRadioDataList = new PhoneRadioDataList(pullSenseStartTimestamp, sensorConfig);
		if (setRawData)
		{
			phoneRadioDataList.setPhoneRadios(phoneRadioDatas);
		}
		return phoneRadioDataList;
	}

}
