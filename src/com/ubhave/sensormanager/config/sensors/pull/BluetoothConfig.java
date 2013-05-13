package com.ubhave.sensormanager.config.sensors.pull;

public class BluetoothConfig
{
	
	/*
	 * Default values
	 */
	public static final long DEFAULT_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final int DEFAULT_SAMPLING_WINDOW_SIZE_PER_CYCLE_MILLIS = 12000;
	public static final int DEFAULT_SAMPLING_CYCLES = 1;
	
	// bluetooth is operated in terms of number of sampling cycles
	// but adaptive sensing requires a specification in terms of
	// sampling window length, so define a constant that contains
	// an approximation of the sampling window length
}
