package com.ubhave.sensormanager.config.pull;

import com.ubhave.sensormanager.config.SensorConfig;

public class ContentReaderConfig
{
	public static final String TIME_LIMIT_MILLIS = "timeLimitMillis";
	public static final String ROW_LIMIT = "rowLimit";

	public static final int NO_ROW_LIMIT = -1;
	public static final long NO_TIME_LIMIT = 0;
	
	public static final int DEFAULT_ROW_LIMIT = 1000;
	public static final long DEFAULT_TIME_LIMIT_MILLIS = NO_TIME_LIMIT;
	
	public static final int DEFAULT_CONTENT_READER_SAMPLING_CYCLES = 1;
	public static final long DEFAULT_CONTENT_READER_SLEEP_INTERVAL = 6 * 60 * 60 * 1000;
	
	public static final String CONTENT_MAP_NUMBER_KEY = "number";
	public static final String CONTENT_MAP_ADDRESS_KEY = "address";
	public static final String CONTENT_MAP_WORDCOUNT_KEY = "bodyWordCount";
	public static final String CONTENT_MAP_CHARCOUNT_KEY = "bodyLength";
	
	public static final String SMS_CONTENT_ADDRESS_KEY = "address";
	public static final String SMS_CONTENT_TYPE_KEY = "type";
	public static final String SMS_CONTENT_DATE_KEY = "date";
	public static final String SMS_CONTENT_BODY_KEY = "body";
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_CONTENT_READER_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES, DEFAULT_CONTENT_READER_SAMPLING_CYCLES);
		sensorConfig.setParameter(TIME_LIMIT_MILLIS, DEFAULT_TIME_LIMIT_MILLIS);
		sensorConfig.setParameter(ROW_LIMIT, DEFAULT_ROW_LIMIT);
		return sensorConfig;
	}
}
