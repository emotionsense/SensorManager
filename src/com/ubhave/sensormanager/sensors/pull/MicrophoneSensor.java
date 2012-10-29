package com.ubhave.sensormanager.sensors.pull;

import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.util.Utilities;

public class MicrophoneSensor extends AbstractPullSensor
{

	private final static String LOG_TAG = "MicrophoneSensor";
	private final MediaRecorder recorder;

	private String amplitudeString = "";

	private static MicrophoneSensor microphoneSensor;
	private static Object lock = new Object();

	public static MicrophoneSensor getMicrophoneSensor(Context context) throws ESException
	{
		if (microphoneSensor == null)
		{
			synchronized (lock)
			{
				if (microphoneSensor == null)
				{
					if (permissionGranted(context, "android.permission.RECORD_AUDIO"))
					{
						microphoneSensor = new MicrophoneSensor(context);
					}
					else
						throw new ESException(ESException.PERMISSION_DENIED, "Microphone : Permission not Granted");
				}
			}
		}
		return microphoneSensor;
	}

	private MicrophoneSensor(Context context)
	{
		super(context);
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile("/dev/null");
	}

	protected String getLogTag()
	{
		return LOG_TAG;
	}

	protected boolean startSensing()
	{
		try
		{
			// for amplitude
			amplitudeString = "";

			recorder.prepare();
			recorder.start();

			// query amplitude in an async thread
			(new Thread()
			{
				public void run()
				{
					// capture max amplitude @20Hz

					// ignore fist call
					recorder.getMaxAmplitude();

					while (isSensing())
					{
						amplitudeString = amplitudeString + ((amplitudeString.length() > 0) ? "," : "") + recorder.getMaxAmplitude();
						Utilities.sleep(50);
					}
				}
			}).start();

		}
		catch (IOException exp)
		{
			ESLogger.error(LOG_TAG, exp);
			return false;
		}
		return true;
	}

	protected void stopSensing()
	{
		// for amplitude
		recorder.stop();
		recorder.release();
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_MICROPHONE;
	}

	protected SensorData getMostRecentRawData()
	{
		return new MicrophoneData(pullSenseStartTimestamp, amplitudeString, sensorConfig.clone());
	}

}
