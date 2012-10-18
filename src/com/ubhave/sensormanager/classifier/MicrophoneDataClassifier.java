package com.ubhave.sensormanager.classifier;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.MicrophoneData;

public class MicrophoneDataClassifier implements SensorDataClassifier
{

	public boolean isInteresting(SensorData sensorData)
	{
		MicrophoneData data = (MicrophoneData) sensorData;
		if (isSilent(data.getAmplitudeString()))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
    private boolean isSilent(String amplitudeData)
    {
        amplitudeData = amplitudeData.trim();

        double avgAmplitude = 0;
        String[] amplitudes = amplitudeData.split(",");
        for (String aValue : amplitudes)
        {
            aValue = aValue.trim();
            double parsedValue = 0;
            try
            {
                parsedValue = Double.parseDouble(aValue);
            }
            catch (NumberFormatException nfe)
            {
                nfe.printStackTrace();
            }
            avgAmplitude += parsedValue;
        }
        avgAmplitude = avgAmplitude / amplitudes.length;
        if (avgAmplitude > Constants.MICROPHONE_SOUND_THRESHOLD)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

}
