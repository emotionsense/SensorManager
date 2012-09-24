package com.ubhave.sensormanager.data.pullsensor;

import java.io.File;

import com.ubhave.sensormanager.data.SensorData;

public class MicrophoneData extends SensorData
{
	private final double maxAmplitude;
	private final String recordedAudioFileFullPath;
	private final boolean isSilent;
	private final double avgAmplitude;

	public MicrophoneData(long senseStartTimestamp, double maxAmplitude, String audioFileFullPath, boolean isSilent, double avgAmplitude)
	{
		super(senseStartTimestamp);
		this.maxAmplitude = maxAmplitude;
		this.isSilent = isSilent;
		recordedAudioFileFullPath = audioFileFullPath;
		this.avgAmplitude =avgAmplitude; 
	}
	
	public boolean isSilent()
	{
		return isSilent;
	}

	public double getMaxAmplitude()
	{
		return maxAmplitude;
	}

	public String getRecordedAudioFileFullPath()
	{
		return recordedAudioFileFullPath;
	}
	
	public String getDataString() 
	{
		File file = new File(this.recordedAudioFileFullPath);
		return file.getName() + " " + this.isSilent + " " + avgAmplitude;
	}
}
