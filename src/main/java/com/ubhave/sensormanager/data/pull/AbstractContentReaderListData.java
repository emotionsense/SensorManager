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

import java.util.ArrayList;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;

public abstract class AbstractContentReaderListData extends SensorData
{
	private final ArrayList<AbstractContentReaderEntry> contentList;

	public AbstractContentReaderListData(long sensorTimestamp, SensorConfig config)
	{
		super(sensorTimestamp, config);
		contentList = new ArrayList<AbstractContentReaderEntry>();
	}
	
	public void addContent(final AbstractContentReaderEntry entry)
	{
		contentList.add(entry);
	}
	
	public int size()
	{
		return contentList.size();
	}

	public ArrayList<AbstractContentReaderEntry> getContentList()
	{
		return contentList;
	}
	
	public int getNumberOfEntriesSince(final long timeLimit)
	{
		int count = 0;
		for (AbstractContentReaderEntry entry : contentList)
		{
			try
			{
				if (entry.getTimestamp() > timeLimit)
				{
					count++;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return count;
	}
}
