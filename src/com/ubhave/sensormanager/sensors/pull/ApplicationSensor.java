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

package com.ubhave.sensormanager.sensors.pull;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.pullsensor.ApplicationData;
import com.ubhave.sensormanager.process.pull.ApplicationProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ApplicationSensor extends AbstractPullSensor
{
	private static final String TAG = "ApplicationSensor";

	private static ApplicationSensor applicationSensor;
	private ArrayList<String> runningApplications;
	private ApplicationData applicationData;
	private static Object lock = new Object();

	public static ApplicationSensor getApplicationSensor(Context context) throws ESException
	{
		if (applicationSensor == null)
		{
			synchronized (lock)
			{
				if (applicationSensor == null)
				{
					if (permissionGranted(context, "android.permission.GET_TASKS"))
					{
						applicationSensor = new ApplicationSensor(context);
					}
					else
						throw new ESException(ESException.PERMISSION_DENIED, "Application Sensor : Permission not Granted");
				}
			}
		}
		return applicationSensor;
	}

	private ApplicationSensor(Context context)
	{
		super(context);
	}

	protected String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_APPLICATION;
	}

	protected ApplicationData getMostRecentRawData()
	{
		return applicationData;
	}

	protected void processSensorData()
	{
		ApplicationProcessor processor = (ApplicationProcessor) getProcessor();
		applicationData = processor.process(pullSenseStartTimestamp, runningApplications, sensorConfig.clone());

	}

	protected boolean startSensing()
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					runningApplications = new ArrayList<String>();

					ActivityManager activityManager = (ActivityManager) applicationContext
							.getSystemService(Context.ACTIVITY_SERVICE);

					List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(50);
					PackageManager pm = applicationContext.getPackageManager();
					for (RunningTaskInfo ti : taskInfos)
					{
						try
						{
							CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(ti.baseActivity.getPackageName(),
									PackageManager.GET_META_DATA));
							String appDetails = "(" + c.toString() + ") " + ti.baseActivity.flattenToShortString();
							runningApplications.add(appDetails);
						}
						catch (NameNotFoundException e)
						{
							Log.e(TAG, Log.getStackTraceString(e));
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					// sensing complete
					notifySenseCyclesComplete();
				}
			}
		}.start();

		return true;
	}

	// Called when a scan is finished
	protected void stopSensing()
	{
	}

}
