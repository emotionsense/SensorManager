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

package com.ubhave.sensormanager.config;

public class SensorManagerConstants
{
	public static final int STOP_SENSING_BATTERY_LEVEL = 20;

	public static final long ACCELEROMETER_SAMPLING_WINDOW_SIZE_MILLIS = 8000;
	public static final int BLUETOOTH_SAMPLING_CYCLES = 1;
	// bluetooth is operated in terms of number of sampling cycles
	// but adaptive sensing requires a specification in terms of
	// sampling window length, so define a constant that contains
	// an approximation of the sampling window length
	public static final int BLUETOOTH_SAMPLING_WINDOW_SIZE_PER_CYCLE_MILLIS = 12000;
	public static final long LOCATION_SAMPLING_WINDOW_SIZE_MILLIS = 60000;
	public static final long MICROPHONE_SAMPLING_WINDOW_SIZE_MILLIS = 5000;
	public static final int WIFI_SAMPLING_CYCLES = 1;
	// similarly for wifi
	public static final int WIFI_SAMPLING_WINDOW_SIZE_PER_CYCLE_MILLIS = 5000;
	// application sensor sensing cycles // this should always be 1
	public static final int APPLCATION_SAMPLING_CYCLES = 1;

	public static final long ACCELEROMETER_SLEEP_INTERVAL = 2 * 60 * 1000;
	public static final long BLUETOOTH_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final long LOCATION_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final long MICROPHONE_SLEEP_INTERVAL = 2 * 60 * 1000;
	public static final long WIFI_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final long APPLICATON_SLEEP_INTERVAL = 5 * 60 * 1000;

	// low battery threshold
	public static final int LOW_BATTERY_THRESHOLD_LEVEL = 20;

	// classifier thresholds
	public final static int ACCELEROMETER_MOVEMENT_THRESHOLD = 25;
	public final static int LOCATION_CHANGE_DISTANCE_THRESHOLD = 100;
	public static final int MICROPHONE_SOUND_THRESHOLD = 800;

	// adaptive sensing constants
	public final static double PROBABILITY_INITIAL_VALUE = 0.5;
	public final static double MIN_PROBABILITY_VALUE = 0.1;
	public final static double MAX_PROBABILITY_VALUE = 0.9;
	public final static double ALPHA_VALUE = 0.5;

}
