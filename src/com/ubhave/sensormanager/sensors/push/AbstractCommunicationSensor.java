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

package com.ubhave.sensormanager.sensors.push;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

public abstract class AbstractCommunicationSensor extends AbstractPushSensor
{

//	private final static String LOG_TAG = "CommunicationSensor";
	
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
			hash += hexString.toUpperCase();
		}
		return hash;
	}

}
