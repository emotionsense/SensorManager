package com.ubhave.sensormanager.sensors.push;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

import com.ubhave.sensormanager.logs.ESLogger;

public abstract class AbstractCommunicationSensor extends AbstractPushSensor
{

	private final static String LOG_TAG = "CommunicationSensor";
	
	public AbstractCommunicationSensor(Context context)
	{
		super(context);
	}
	
	protected String hashPhoneNumber(String phoneNumber)
	{
		if ((phoneNumber == null) || (phoneNumber.length() == 0))
		{
			return "";
		}

		// use only the last 10 digits
		if (phoneNumber.length() > 10)
		{
			phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
		}
		MessageDigest mDigest = null;
		try
		{
			mDigest = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException e)
		{
			ESLogger.error(LOG_TAG, e);
		}
		mDigest.reset();
		byte[] byteArray = mDigest.digest(phoneNumber.getBytes());
		String hash = "";
		for (int i = 0; i < byteArray.length; i++)
		{
			String hexString = Integer.toHexString(0xFF & byteArray[i]);
			if (hexString.length() == 1)
			{
				hexString = "0" + hexString;
			}
			hash += hexString.toUpperCase();
		}
		return hash;
	}

}
