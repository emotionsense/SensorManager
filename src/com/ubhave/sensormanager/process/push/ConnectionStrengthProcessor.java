package com.ubhave.sensormanager.process.push;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pushsensor.ConnectionStrengthData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class ConnectionStrengthProcessor extends AbstractProcessor
{
	public ConnectionStrengthProcessor(final Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}

	public ConnectionStrengthData process(long recvTime, SensorConfig config,
			int strength) {
		ConnectionStrengthData data = new ConnectionStrengthData(recvTime,
				config);
		if (setRawData)
		{
			data.setStrength(strength);
		}
		return data;
	}

}
