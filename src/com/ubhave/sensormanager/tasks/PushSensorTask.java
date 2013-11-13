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

package com.ubhave.sensormanager.tasks;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.push.PushSensor;

public class PushSensorTask extends AbstractSensorTask implements SensorDataListener
{
	
	public PushSensorTask(SensorInterface sensor)
	{
		super(sensor);
	}

	public void run()
	{
		while (true)
		{
			try
			{
				if (state == RUNNING)
				{
					if (!(((PushSensor) sensor).isSensing()))
					{
						try
						{
							((PushSensor) sensor).startSensing(this);
						}
						catch (ESException exp)
						{
							exp.printStackTrace();
						}
					}
					else
					{
						synchronized (syncObject)
						{
							syncObject.wait();
						}
					}
				}
				else if ((state == PAUSED) || (state == STOPPED))
				{
					if (((PushSensor) sensor).isSensing())
					{
						try
						{
							((PushSensor) sensor).stopSensing(this);
						}
						catch (ESException e)
						{
							e.printStackTrace();
						}
					}
					if (state == PAUSED)
					{
						synchronized (syncObject)
						{
							syncObject.wait(pauseTime);
						}
					}
					else if (state == STOPPED)
					{
						synchronized (syncObject)
						{
							syncObject.wait();
						}
					}
					state = RUNNING;
				}
			}
			catch (InterruptedException ie)
			{
				// ignore
			}
		}
	}

	public void onDataSensed(SensorData data)
	{
		super.publishData(data);
	}

	public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
	{
	}
}
