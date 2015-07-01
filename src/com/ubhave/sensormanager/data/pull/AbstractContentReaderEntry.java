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

package com.ubhave.sensormanager.data.pull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public abstract class AbstractContentReaderEntry
{
	private final static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS dd MM yyyy Z z", Locale.US);
	private final static String LOCAL_TIME = "local_time_when_sensed";
	protected HashMap<String, String> contentMap;
	
	public AbstractContentReaderEntry()
	{
		contentMap = new HashMap<String, String>();
	}
	
	public void set(final String key, final String value)
	{
		contentMap.put(key, value);
		if (key == getTimestampKey())
		{
			try
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(getTimestamp());
				contentMap.put(LOCAL_TIME, formatter.format(calendar.getTime()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public String get(final String key)
	{
		return contentMap.get(key);
	}
	
	public Set<String> getKeys()
	{
		return contentMap.keySet();
	}
	
	public void setContentMap(final HashMap<String, String> map)
	{
		this.contentMap = map;
	}

	public long getTimestamp() throws Exception
	{
		return Long.valueOf(contentMap.get(getTimestampKey()));
	}
	
	protected abstract String getTimestampKey();
}
