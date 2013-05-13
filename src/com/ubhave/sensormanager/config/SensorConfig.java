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

public class SensorConfig extends AbstractConfig implements Cloneable
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

	// accelerometer sensing delay
	public final static String ACCELEROMETER_SAMPLING_DELAY = "ACCELEROMETER_SAMPLING_DELAY";  
	
	// data preferences
	public final static String DATA_SET_RAW_VALUES = "RAW_DATA";
	public final static String DATA_EXTRACT_FEATURES = "EXTRACT_FEATURES";
	
	// location accuracy
	public final static String LOCATION_ACCURACY = "LOCATION_ACCURACY";
	public final static String LOCATION_ACCURACY_COARSE = "LOCATION_ACCURACY_COARSE";
	public final static String LOCATION_ACCURACY_FINE = "LOCATION_ACCURACY_FINE";

	// whether adaptive sensing is enabled for all sensors
	public final static String ADAPTIVE_SENSING_ENABLED = "ADAPTIVE_SENSING";

	public SensorConfig clone()
	{
		SensorConfig clonedSensorConfig = new SensorConfig();
		for (String key : configParams.keySet())
		{
			Object obj = configParams.get(key);
			clonedSensorConfig.setParameter(key, obj);
		}

		return clonedSensorConfig;
	}

}
