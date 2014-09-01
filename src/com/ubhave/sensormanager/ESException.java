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

package com.ubhave.sensormanager;

public class ESException extends Exception
{
	private static final long serialVersionUID = -6952859423645368705L;
	private static final String NOT_GRANTED = " Permission not granted.";

	// error codes
	public static final int PERMISSION_DENIED = 8000;
	public static final int UNKNOWN_SENSOR_TYPE = 8001;
	public static final int UNKNOWN_SENSOR_NAME = 8002;
	public static final int SENSOR_ALREADY_SENSING = 8003;
	public static final int SENSOR_NOT_SENSING = 8004;
	public static final int INVALID_SENSOR_CONFIG = 8005;
	public static final int INVALID_STATE = 8007;
	public static final int OPERATION_NOT_SUPPORTED = 8011;
	public static final int INVALID_PARAMETER = 8012;
	public static final int SENSOR_UNAVAILABLE = 8014;

	private int errorCode;
	private String message;

	public ESException(int errorCode, String message)
	{
		super(message);
		this.errorCode = errorCode;
		if (errorCode == PERMISSION_DENIED)
		{
			this.message = message + NOT_GRANTED;
		}
		else
		{
			this.message = message;
		}
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public String getMessage()
	{
		return message;
	}
}
