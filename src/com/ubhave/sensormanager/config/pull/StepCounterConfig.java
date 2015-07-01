/* **************************************************
 Copyright (c) 2014

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

package com.ubhave.sensormanager.config.pull;

import android.hardware.SensorManager;

import com.ubhave.sensormanager.config.SensorConfig;

public class StepCounterConfig
{
	/*
	 * Config keys
	 */
	public final static String SAMPLING_DELAY = "MOTION_SAMPLING_DELAY";
	public final static String LOW_PASS_ALPHA = "LOW_PASS_ALPHA";
	public final static String MOTION_THRESHOLD = "MOTION_THRESHOLD";
	
	/*
	 * Default values
	 */
	private static final long DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS = 10 * 1000L;
	private static final long DEFAULT_SLEEP_INTERVAL = 2 * 60 * 1000L;
	private static final int DEFAULT_SAMPLING_DELAY = SensorManager.SENSOR_DELAY_GAME;
	
	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, DEFAULT_SLEEP_INTERVAL);
		sensorConfig.setParameter(PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, DEFAULT_SAMPLING_WINDOW_SIZE_MILLIS);
		sensorConfig.setParameter(SAMPLING_DELAY, DEFAULT_SAMPLING_DELAY);
		return sensorConfig;
	}
}
