package com.ubhave.sensormanager.process;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommunicationProcessor extends AbstractProcessor
{
	public CommunicationProcessor(boolean rw, boolean sp)
	{
		super(rw, sp);
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
			hash += hexString.toUpperCase(); // TODO check this warning
		}
		return hash;
	}

}
