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
import java.util.ArrayList;

import android.Manifest;
import android.content.Context;
import android.media.MediaRecorder;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.pull.MicrophoneConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.MicrophoneData;
import com.ubhave.sensormanager.process.pull.MicrophoneProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class MicrophoneSensor extends AbstractMediaSensor
{
	private final static String LOG_TAG = "MicrophoneSensor";
	private final static String AUDIO_FILE_PREFIX = "audio";
	private final static String AUDIO_FILE_SUFFIX = ".3gpp";
	
	private static MicrophoneSensor microphoneSensor;
	private static Object lock = new Object();
	
	private MediaRecorder recorder;
	private File mediaFile;
	private ArrayList<Integer> maxAmplitudeList;
	private ArrayList<Long> timestampList;
	private MicrophoneData micData;
	private boolean isRecording;

	public static MicrophoneSensor getSensor(Context context) throws ESException
	{
		if (microphoneSensor == null)
		{
			synchronized (lock)
			{
				if (microphoneSensor == null)
				{
					if (permissionGranted(context, Manifest.permission.RECORD_AUDIO))
					{
						microphoneSensor = new MicrophoneSensor(context);
					}
					else
					{
						throw new ESException(ESException.PERMISSION_DENIED, SensorUtils.SENSOR_NAME_MICROPHONE);
					}
				}
			}
		}
		return microphoneSensor;
	}

	private MicrophoneSensor(Context context)
	{
		super(context);
		isRecording = false;
	}

	@Override
	protected String getLogTag()
	{
		return LOG_TAG;
	}
	
	@Override
	protected String getFileDirectory()
	{
		return (String) sensorConfig.getParameter(MicrophoneConfig.AUDIO_FILES_DIRECTORY);
	}
	
	@Override
	protected String getFilePrefix()
	{
		return AUDIO_FILE_PREFIX;
	}
	
	@Override
	protected String getFileSuffix()
	{
		return AUDIO_FILE_SUFFIX;
	}

	private int getSamplingRate()
	{
		try
		{
			return (Integer) sensorConfig.getParameter(MicrophoneConfig.SAMPLING_RATE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return MicrophoneConfig.DEFAULT_SAMPLING_RATE;
		}
	}

	private boolean prepareToSense()
	{
		try
		{
			maxAmplitudeList = new ArrayList<Integer>();
			timestampList = new ArrayList<Long>();
			mediaFile = getMediaFile();

			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setAudioSamplingRate(getSamplingRate());
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(mediaFile.getAbsolutePath());
			recorder.prepare();
			recorder.start();
			return true;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return false;
		}
	}

	protected boolean startSensing()
	{
		isRecording = prepareToSense();
		if (isRecording)
		{
			try
			{
				(new Thread()
				{
					public void run()
					{
						recorder.getMaxAmplitude();
						while (isSensing())
						{
							synchronized (recorder)
							{
								if (isRecording)
								{
									maxAmplitudeList.add(recorder.getMaxAmplitude());
									timestampList.add(System.currentTimeMillis());
								}
							}
							MicrophoneSensor.sleep(50);
						}
					}
				}).start();
				return true;
			}
			catch (Throwable e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	private static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}

	protected void stopSensing()
	{
		synchronized (recorder)
		{
			try
			{
				recorder.stop();
				recorder.reset(); 
				recorder.release();
				isRecording = false;
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_MICROPHONE;
	}

	@Override
	protected SensorData getMostRecentRawData()
	{
		return micData;
	}

	protected void processSensorData()
	{
		int[] maxAmpArray = new int[maxAmplitudeList.size()];
		long[] timestampArray = new long[timestampList.size()];
		for (int i = 0; (i < maxAmplitudeList.size() && i < timestampList.size()); i++)
		{
			maxAmpArray[i] = maxAmplitudeList.get(i);
			timestampArray[i] = timestampList.get(i);
		}

		MicrophoneProcessor processor = (MicrophoneProcessor) getProcessor();
		micData = processor.process(pullSenseStartTimestamp, maxAmpArray, timestampArray, mediaFile.getAbsolutePath(), sensorConfig.clone());
	}
}
