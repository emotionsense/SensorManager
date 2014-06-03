package com.ubhave.sensormanager.process.pull;

import java.util.ArrayList;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pullsensor.PhoneRadioData;
import com.ubhave.sensormanager.data.pullsensor.PhoneRadioDataList;
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
