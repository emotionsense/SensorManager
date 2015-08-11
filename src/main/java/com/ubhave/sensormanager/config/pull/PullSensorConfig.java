package com.ubhave.sensormanager.config.pull;

public class PullSensorConfig
{
	// sampling window size sets the data capture duration from the sensor, like
	// accelerometer sampling window
	public final static String SENSE_WINDOW_LENGTH_MILLIS = "SENSE_WINDOW_LENGTH_MILLIS";

	// number of sampling cycles sets the number of times a sensor samples the
	// data, and this is relevant for sensors like Bluetooth, Wifi, where there
	// is no fixed sampling window and the amount of sampling time
	// depends on the number of devices in the environment. the no. of cycles
	// sets the number of scans (wifi or bluetooth) to be performed
	public final static String NUMBER_OF_SENSE_CYCLES = "NUMBER_OF_SENSE_CYCLES";

	// length of sensing window per cycle of sensing, this is relevant for
	// bluetooth and wifi sensors where sense window is a function of number of
	// devices in the environment. the lengths are defined in the Constants
	// class
	public final static String SENSE_WINDOW_LENGTH_PER_CYCLE_MILLIS = "SENSE_WINDOW_LENGTH_PER_CYCLE_MILLIS";

	// this is the sleep interval between two consecutive sensor samplings
	public final static String POST_SENSE_SLEEP_LENGTH_MILLIS = "POST_SENSE_SLEEP_LENGTH_MILLIS";

	// whether adaptive sensing is enabled for all sensors
	public final static String ADAPTIVE_SENSING_ENABLED = "ADAPTIVE_SENSING";
	
	/*
	 * Default values for adaptive sensing
	 */
	public final static double PROBABILITY_INITIAL_VALUE = 0.5;
	public final static double DEFAULT_MIN_PROBABILITY_VALUE = 0.1;
	public final static double DEFAULT_MAX_PROBABILITY_VALUE = 0.9;
	public final static double ALPHA_VALUE = 0.5;
}
