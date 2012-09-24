package com.ubhave.sensormanager.sensors.pull;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.Utilities;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;
import com.ubhave.sensormanager.logs.ESLogger;

public class MicrophoneSensor extends AbstractPullSensor
{

	private final static String LOG_TAG = "MicrophoneSensor";

	private final MediaRecorder recorder;
	private AudioRecord audioRecord;
	private int audioRecordBufferSize;

	private String lastRecordedAudioFile;
	private String currRecordedAudioFile;
	private SensorConfig sensorConfig;

	private boolean isSilent = true;
	private double avgAmplitude;
	// based on tests
	private final double SILENCE_THRESHOLD = 200.0;

	private static MicrophoneSensor microphoneSensor;
	private static Object lock = new Object();

	public static MicrophoneSensor getMicrophoneSensor()
	{
		if (microphoneSensor == null)
		{
			synchronized (lock)
			{
				if (microphoneSensor == null)
				{
					microphoneSensor = new MicrophoneSensor();
				}
			}
		}
		return microphoneSensor;
	}

	private MicrophoneSensor()
	{
		// to get amplitude
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile("/dev/null");

		// for speech, emotion
		// setup in the startSensing method

		// create sounds directory
		File file = new File(Constants.SOUNDS_DIR);
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	private String getMicRecorderConfig()
	{
		String micRecorderConfig = SensorConfig.MIC_RECORDER_VALUE_MEDIA_RECORDER;
		if (sensorConfig != null)
		{
			String configValue = (String) sensorConfig.get(SensorConfig.MIC_RECORDER);
			if (configValue != null)
			{
				micRecorderConfig = configValue;
			}
		}
		return micRecorderConfig;
	}

	protected String getLogTag()
	{
		return LOG_TAG;
	}

	protected boolean startSensing(SensorConfig sensorConfig)
	{
		try
		{
			isSilent = true;

			this.sensorConfig = sensorConfig;
			currRecordedAudioFile = generateAudioFileName();
			String micRecorderConfig = getMicRecorderConfig();
			if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_MEDIA_RECORDER))
			{
				// for amplitude
				recorder.prepare();
				recorder.start();
			}
			else if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_AUDIO_RECORD))
			{
				// for speech, emotion recognition
				int frequency = 8000;
				int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
				int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

				audioRecordBufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding) * 100;
				//
				// audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
				// frequency, channelConfiguration, audioEncoding,
				// audioRecordBufferSize);
				audioRecord = new AudioRecord(MediaRecorder.AudioSource.CAMCORDER, frequency, channelConfiguration, audioEncoding, audioRecordBufferSize);
				audioRecord.startRecording();
			}
			else
			{
				ESLogger.error(LOG_TAG, "Invalid value for " + SensorConfig.MIC_RECORDER + " value: " + micRecorderConfig);
				return false;
			}
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
		String micRecorderConfig = getMicRecorderConfig();
		if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_MEDIA_RECORDER))
		{
			// for amplitude
			recorder.stop();
			recorder.release();
		}
		else if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_AUDIO_RECORD))
		{
			// for speech, emotion recognition
			short[] buffer = new short[audioRecordBufferSize];
			int bufferReaderResult = stopAudioRecorder(buffer);
			setSilentFlag(buffer, bufferReaderResult);
			writeAudioData(buffer, bufferReaderResult);
		}
		else
		{
			ESLogger.error(LOG_TAG, "Invalid value for " + SensorConfig.MIC_RECORDER + " value: " + micRecorderConfig);
		}
	}

	private void setSilentFlag(short[] buffer, int bufferReaderResult)
	{
		double avg = 0.0;
		for (int i = 0; i < bufferReaderResult; i++)
		{
			avg += Math.abs(buffer[i]);
		}
		avgAmplitude = (avg / bufferReaderResult);
		isSilent = (avgAmplitude < SILENCE_THRESHOLD);
	}

	private void writeAudioData(short[] buffer, int bufferReadResult)
	{
		File file = new File(currRecordedAudioFile);
		// Delete any previous recording.
		if (file.exists())
		{
			file.delete();
		}
		// Create the new file
		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			throw new IllegalStateException("Failed to create " + file.toString());
		}
		try
		{
			OutputStream os = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			DataOutputStream dos = new DataOutputStream(bos);
			// *important* JVM is big-endian
			for (int i = 0; i < bufferReadResult; i++)
			{
				dos.writeShort(Short.reverseBytes(buffer[i]));
			}
			dos.close();
			bos.close();
			os.close();

			// if it is silent, delete the file
			if (isSilent)
			{
				file.delete();
				lastRecordedAudioFile = currRecordedAudioFile;
			}
			else
			{
				// move to to_be_uploaded folder
				File targetFile = new File(Constants.TO_BE_UPLOADED_LOGS_DIR + "/" + file.getName());
				file.renameTo(targetFile);
				lastRecordedAudioFile = targetFile.getAbsolutePath();
			}

			currRecordedAudioFile = null;
		}
		catch (Throwable t)
		{
			ESLogger.error(LOG_TAG, "Recording Failed");
		}
	}

	private int stopAudioRecorder(short[] buffer)
	{
		try
		{
			int bufferReadResult = audioRecord.read(buffer, 0, audioRecordBufferSize);
			audioRecord.stop();
			audioRecord.release();
			return bufferReadResult;
		}
		catch (Throwable t)
		{
			ESLogger.error(LOG_TAG, "Recording Failed");
			return 0;
		}
	}

	private String generateAudioFileName()
	{
		String audioFileName = Utilities.getImei() + "_" + System.currentTimeMillis() + ".pcm";
		String audioFileFullPath = Constants.SOUNDS_DIR + "/" + audioFileName;
		return audioFileFullPath;
	}

	public int getSensorType()
	{
		return Constants.SENSOR_TYPE_MICROPHONE;
	}

	protected SensorData getMostRecentRawData()
	{
		return new MicrophoneData(pullSenseStartTimestamp, recorder.getMaxAmplitude(), lastRecordedAudioFile, isSilent, avgAmplitude);
	}

}
