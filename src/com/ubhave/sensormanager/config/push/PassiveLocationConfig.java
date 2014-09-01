/* **************************************************
 Copyright (c) 2014, Idiap
 Hugues Salamin, hugues.salamin@idiap.ch

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

package com.ubhave.sensormanager.config.push;

import com.ubhave.sensormanager.config.SensorConfig;

public class PassiveLocationConfig {
	// minimum time in Millisecond between two location updates
	public static final String MIN_TIME = "min_time";
	// Minimum distance in meter between two location updates
	public static final String MIN_DISTANCE = "min_distance";
	/*
	 * Default values
	 */
	public static final long DEFAULT_MIN_TIME = 10 * 1000;
	public static final float DEFAULT_MIN_DISTANCE = 10.0f;

	public static SensorConfig getDefault()
	{
		SensorConfig sensorConfig = new SensorConfig();
		sensorConfig.setParameter(MIN_TIME, DEFAULT_MIN_TIME);
		sensorConfig.setParameter(MIN_DISTANCE, DEFAULT_MIN_DISTANCE);
		return sensorConfig;
	}

}
