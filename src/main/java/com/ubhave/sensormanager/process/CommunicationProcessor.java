package com.ubhave.sensormanager.process;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.Context;

public abstract class CommunicationProcessor extends AbstractProcessor
{
	public CommunicationProcessor(Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
	}
	
	public static String hashPhoneNumber(String phoneNumber)
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
			e.printStackTrace();
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
			hash += hexString.toUpperCase(Locale.ENGLISH);
		}
		return hash;
	}
}
