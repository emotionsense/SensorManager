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

import android.Manifest;
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

public class CameraSensor extends AbstractMediaSensor
{
	private final static String LOG_TAG = "CameraSensor";
	private final static String IMAGE_FILE_PREFIX = "image";
	private final static String IMAGE_FILE_SUFFIX = ".jpg";

	private static CameraSensor cameraSensor;
	private static Object lock = new Object();

	private CameraData cameraData;
	private Camera camera;
	private File imageFile;

	public static CameraSensor getCameraSensor(final Context context) throws ESException
	{
		if (cameraSensor == null)
		{
			synchronized (lock)
			{
				if (cameraSensor == null)
				{
					if (permissionGranted(context, Manifest.permission.CAMERA))
					{
						cameraSensor = new CameraSensor(context);
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED, SensorUtils.SENSOR_NAME_CAMERA);
					}
				}
			}
		}
		return cameraSensor;
	}

	private CameraSensor(Context context)
	{
		super(context);
	}

	@Override
	protected String getLogTag()
	{
		return LOG_TAG;
	}
	
	@Override
	protected String getFileDirectory()
	{
		return (String) sensorConfig.getParameter(CameraConfig.IMAGE_FILES_DIRECTORY);
	}
	
	@Override
	protected String getFilePrefix()
	{
		return IMAGE_FILE_PREFIX;
	}
	
	@Override
	protected String getFileSuffix()
	{
		return IMAGE_FILE_SUFFIX;
	}

	protected boolean startSensing()
	{
		try
		{
			imageFile = getMediaFile();
			int cameraType = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
			if (sensorConfig.containsParameter(CameraConfig.CAMERA_TYPE))
			{
				cameraType = (Integer) sensorConfig.getParameter(CameraConfig.CAMERA_TYPE);
			}
			camera = Camera.open(cameraType);
			camera.startPreview();
			camera.takePicture(null, null, callBack);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private Camera.PictureCallback callBack = new Camera.PictureCallback()
	{
		public void onPictureTaken(byte[] data, Camera camera)
		{
			FileOutputStream outStream = null;
			try
			{
				outStream = new FileOutputStream(imageFile);
				outStream.write(data);
				outStream.close();
				camera.release();
				notifySenseCyclesComplete();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				if (GlobalConfig.shouldLog())
				{
					Log.d(LOG_TAG, e.getMessage());
				}	
			}
		}
	};

	protected void processSensorData()
	{
		CameraProcessor processor = (CameraProcessor) getProcessor();
		cameraData = processor.process(pullSenseStartTimestamp, imageFile.getAbsolutePath(), sensorConfig.clone());
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
