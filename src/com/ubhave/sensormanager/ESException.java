package com.ubhave.sensormanager;

public class ESException extends Exception
{
	private static final long serialVersionUID = -6952859423645368705L;
	
	// error codes
	public static final int PERMISSION_DENIED = 8000;
	public static final int UNKNOWN_SENSOR_TYPE = 8001;
	public static final int UNKNOWN_SENSOR_NAME = 8002;
	public static final int SENSOR_ALREADY_SENSING = 8003;
	public static final int SENSOR_NOT_SENSING = 8004;
	public static final int INVALID_SENSOR_CONFIG = 8005;
	public static final int CONFIG_NOT_SUPPORTED = 8006;
	public static final int INVALID_STATE = 8007;
	public static final int SENSOR_MANAGER_NOT_STARTED = 8008;
	public static final int EXPERIENCE_SERVICE_NOT_STARTED = 8009;
	public static final int CONFIG_SERVICE_NOT_STARTED = 8010;
	public static final int OPERATION_NOT_SUPPORTED = 8011;
	public static final int INVALID_PARAMETER = 8012;

	private int errorCode;
	private String message;

	public ESException(int errorCode, String message)
	{
		super(message);
		this.errorCode = errorCode;
		this.message = message;
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
