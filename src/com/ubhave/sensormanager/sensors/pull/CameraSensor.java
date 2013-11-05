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

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.config.sensors.pull.CameraConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.CameraData;
import com.ubhave.sensormanager.process.pull.CameraProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class CameraSensor extends AbstractPullSensor
{
	private final static String LOG_TAG = "CameraSensor";
	private String imageFileFullPath;

	private static CameraSensor cameraSensor;
	private static Object lock = new Object();

	private CameraData cameraData;

	private Camera camera;

	public static CameraSensor getCameraSensor(Context context) throws ESException
	{
		if (cameraSensor == null)
		{
			synchronized (lock)
			{
				if (cameraSensor == null)
				{
					if (permissionGranted(context, "android.permission.CAMERA"))
					{
						cameraSensor = new CameraSensor(context);
					}
					else
						throw new ESException(ESException.PERMISSION_DENIED, "Camera : Permission not Granted");
				}
			}
		}
		return cameraSensor;
	}

	private CameraSensor(Context context)
	{
		super(context);
	}

	protected String getLogTag()
	{
		return LOG_TAG;
	}

	protected boolean startSensing()
	{
		try
		{

			imageFileFullPath = applicationContext.getFilesDir().getAbsolutePath() + "/ESImage_"
					+ System.currentTimeMillis() + ".jpg";
			File file = new File(imageFileFullPath);
			if (file.exists())
			{
				file.delete();
			}

			int cameraType = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

			if (sensorConfig.getParameter(CameraConfig.CAMERA_TYPE) != null)
			{
				int cameraTypeConfig = (Integer) sensorConfig.getParameter(CameraConfig.CAMERA_TYPE);
				if (cameraTypeConfig == CameraConfig.CAMERA_TYPE_FRONT)
				{
					cameraType = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
				}
				else if (cameraTypeConfig == CameraConfig.CAMERA_TYPE_BACK)
				{
					cameraType = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
				}
			}

			camera = Camera.open(cameraType);
			camera.takePicture(null, null, callBack);

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	Camera.PictureCallback callBack = new Camera.PictureCallback()
	{

		public void onPictureTaken(byte[] data, Camera camera)
		{
			FileOutputStream outStream = null;
			try
			{
				outStream = new FileOutputStream(imageFileFullPath);
				outStream.write(data);
				outStream.close();

				notifySenseCyclesComplete();
			}
			catch (Exception e)
			{
				if (GlobalConfig.shouldLog())
				{
					Log.d("CAMERA", e.getMessage());
				}	
			}
		}
	};

	protected void processSensorData()
	{
		CameraProcessor processor = (CameraProcessor) getProcessor();
		cameraData = processor.process(pullSenseStartTimestamp, imageFileFullPath, sensorConfig.clone());
	}

	protected void stopSensing()
	{
		try
		{
			camera.release();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_CAMERA;
	}

	protected SensorData getMostRecentRawData()
	{
		return cameraData;
	}
}
