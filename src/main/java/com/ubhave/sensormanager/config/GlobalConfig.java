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

public class GlobalConfig extends AbstractConfig
{
	public final static String LOW_BATTERY_THRESHOLD = "LOW_BATTERY_THRESHOLD";
	public final static String PRINT_LOG_D_MESSAGES = "PRINT_LOG_D_MESSAGES";
	public final static String ACQUIRE_WAKE_LOCK = "ACQUIRE_WAKE_LOCK";

	private final static boolean DEFAULT_PRINT_LOG_D_MESSAGES = true;
	private final static int DEFAULT_LOW_BATTERY_THRESHOLD_LEVEL = 20;
	
	private static GlobalConfig globalConfig;
	private static final Object lock = new Object();

	public static GlobalConfig getGlobalConfig()
	{
		if (globalConfig == null)
		{
			synchronized (lock)
			{
				if (globalConfig == null)
				{
					globalConfig = getDefaultGlobalConfig();
				}
			}
		}
		return globalConfig;
	}

	private static GlobalConfig getDefaultGlobalConfig()
	{
		GlobalConfig config = new GlobalConfig();
		config.setParameter(LOW_BATTERY_THRESHOLD, DEFAULT_LOW_BATTERY_THRESHOLD_LEVEL);
		config.setParameter(PRINT_LOG_D_MESSAGES, DEFAULT_PRINT_LOG_D_MESSAGES);
		return config;
	}
	
	public static boolean shouldLog()
	{
		return (Boolean) getGlobalConfig().getParameter(PRINT_LOG_D_MESSAGES);
	}
}
