package com.ubhave.sensormanager.sensors.pull;


public class MicrophoneSensor //extends AbstractPullSensor
{

//	private final static String LOG_TAG = "MicrophoneSensor";
//
//	private final MediaRecorder recorder;
//	private AudioRecord audioRecord;
//	private int audioRecordBufferSize;
//
//	private String lastRecordedAudioFile;
//	private String currRecordedAudioFile;
//	private SensorConfig sensorConfig;
//
//	private boolean isSilent = true;
//	private double avgAmplitude;
//	
//	// based on tests, move to config TODO
//	private final double SILENCE_THRESHOLD = 200.0;
//
//	private static MicrophoneSensor microphoneSensor;
//	private static Object lock = new Object();
//
//	public static MicrophoneSensor getMicrophoneSensor(Context context) throws ESException
//	{
//		if (microphoneSensor == null)
//		{
//			synchronized (lock)
//			{
//				if (microphoneSensor == null)
//				{
//					if (permissionGranted(context, "android.permission.RECORD_AUDIO"))
//					{
//						microphoneSensor = new MicrophoneSensor(context);
//					}
//					else throw new ESException(ESException. PERMISSION_DENIED, "Microphone : Permission not Granted");
//				}
//			}
//		}
//		return microphoneSensor;
//	}
//
//	private MicrophoneSensor(Context context)
//	{
//		super(context);
//		recorder = new MediaRecorder();
//		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//		recorder.setOutputFile("/dev/null");
//
//		// for speech, emotion
//		// setup in the startSensing method
//
////		// create sounds directory
////		File file = new File(Constants.SOUNDS_DIR);
////		if (!file.exists())
////		{
////			file.mkdirs();
////		}
//	}
//
//	private String getMicRecorderConfig()
//	{
//		String micRecorderConfig = SensorConfig.MIC_RECORDER_VALUE_MEDIA_RECORDER;
//		if (sensorConfig != null)
//		{
//			String configValue = (String) sensorConfig.get(SensorConfig.MIC_RECORDER);
//			if (configValue != null)
//			{
//				micRecorderConfig = configValue;
//			}
//		}
//		return micRecorderConfig;
//	}
//
//	protected String getLogTag()
//	{
//		return LOG_TAG;
//	}
//
//	protected boolean startSensing(SensorConfig sensorConfig)
//	{
//		try
//		{
//			isSilent = true;
//
//			this.sensorConfig = sensorConfig;
//			currRecordedAudioFile = generateAudioFileName();
//			String micRecorderConfig = getMicRecorderConfig();
//			if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_MEDIA_RECORDER))
//			{
//				// for amplitude
//				recorder.prepare();
//				recorder.start();
//			}
//			else if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_AUDIO_RECORD))
//			{
//				// for speech, emotion recognition
//				int frequency = 8000;
//				int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
//				int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
//
//				audioRecordBufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding) * 100;
//				//
//				// audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
//				// frequency, channelConfiguration, audioEncoding,
//				// audioRecordBufferSize);
//				audioRecord = new AudioRecord(MediaRecorder.AudioSource.CAMCORDER, frequency, channelConfiguration, audioEncoding, audioRecordBufferSize);
//				audioRecord.startRecording();
//			}
//			else
//			{
//				ESLogger.error(LOG_TAG, "Invalid value for " + SensorConfig.MIC_RECORDER + " value: " + micRecorderConfig);
//				return false;
//			}
//		}
//		catch (IOException exp)
//		{
//			ESLogger.error(LOG_TAG, exp);
//			return false;
//		}
//		return true;
//	}
//
//	protected void stopSensing()
//	{
//		String micRecorderConfig = getMicRecorderConfig();
//		if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_MEDIA_RECORDER))
//		{
//			// for amplitude
//			recorder.stop();
//			recorder.release();
//		}
//		else if (micRecorderConfig.equals(SensorConfig.MIC_RECORDER_VALUE_AUDIO_RECORD))
//		{
//			// for speech, emotion recognition
//			short[] buffer = new short[audioRecordBufferSize];
//			int bufferReaderResult = stopAudioRecorder(buffer);
//			setSilentFlag(buffer, bufferReaderResult);
//			writeAudioData(buffer, bufferReaderResult);
//		}
//		else
//		{
//			ESLogger.error(LOG_TAG, "Invalid value for " + SensorConfig.MIC_RECORDER + " value: " + micRecorderConfig);
//		}
//	}
//
//	private void setSilentFlag(short[] buffer, int bufferReaderResult)
//	{
//		double avg = 0.0;
//		for (int i = 0; i < bufferReaderResult; i++)
//		{
//			avg += Math.abs(buffer[i]);
//		}
//		avgAmplitude = (avg / bufferReaderResult);
//		isSilent = (avgAmplitude < SILENCE_THRESHOLD);
//	}
//
//	private void writeAudioData(short[] buffer, int bufferReadResult)
//	{
//		File file = new File(currRecordedAudioFile);
//		// Delete any previous recording.
//		if (file.exists())
//		{
//			file.delete();
//		}
//		// Create the new file
//		try
//		{
//			file.createNewFile();
//		}
//		catch (IOException e)
//		{
//			throw new IllegalStateException("Failed to create " + file.toString());
//		}
//		try
//		{
//			OutputStream os = new FileOutputStream(file);
//			BufferedOutputStream bos = new BufferedOutputStream(os);
//			DataOutputStream dos = new DataOutputStream(bos);
//			// *important* JVM is big-endian
//			for (int i = 0; i < bufferReadResult; i++)
//			{
//				dos.writeShort(Short.reverseBytes(buffer[i]));
//			}
//			dos.close();
//			bos.close();
//			os.close();
//
//			// if it is silent, delete the file
//			if (isSilent)
//			{
//				file.delete();
//				lastRecordedAudioFile = currRecordedAudioFile;
//			}
//			else
//			{
//				// move to to_be_uploaded folder
//				File targetFile = new File(Constants.TO_BE_UPLOADED_LOGS_DIR + "/" + file.getName());
//				file.renameTo(targetFile);
//				lastRecordedAudioFile = targetFile.getAbsolutePath();
//			}
//
//			currRecordedAudioFile = null;
//		}
//		catch (Throwable t)
//		{
//			ESLogger.error(LOG_TAG, "Recording Failed");
//		}
//	}
//
//	private int stopAudioRecorder(short[] buffer)
//	{
//		try
//		{
//			int bufferReadResult = audioRecord.read(buffer, 0, audioRecordBufferSize);
//			audioRecord.stop();
//			audioRecord.release();
//			return bufferReadResult;
//		}
//		catch (Throwable t)
//		{
//			ESLogger.error(LOG_TAG, "Recording Failed");
//			return 0;
//		}
//	}
//
//	public int getSensorType()
//	{
//		return SensorList.SENSOR_TYPE_MICROPHONE;
//	}
//
//	protected SensorData getMostRecentRawData()
//	{
//		return new MicrophoneData(pullSenseStartTimestamp, recorder.getMaxAmplitude(), lastRecordedAudioFile, isSilent, avgAmplitude);
//	}

}
