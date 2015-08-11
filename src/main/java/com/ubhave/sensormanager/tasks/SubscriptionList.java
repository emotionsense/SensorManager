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

package com.ubhave.sensormanager.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;
import android.util.SparseArray;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.GlobalConfig;

public class SubscriptionList
{
	private static String TAG = "SubscriptionList";

	private final SparseArray<Subscription> subscriptionMap;
	private final Random keyGenerator;

	public SubscriptionList()
	{
		subscriptionMap = new SparseArray<Subscription>();
		keyGenerator = new Random();
	}

	public synchronized int registerSubscription(Subscription s) throws ESException
	{
		if (!s.register()) // subscription already exists
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d(TAG, "registerSubscription() subscription already exists for task: " + s.getTask().getSensorType()
						+ " listener: " + s.getListener());
			}
			
			for (int i = 0; i < subscriptionMap.size(); i++)
			{
				int subscriptionId = subscriptionMap.keyAt(i);
				Subscription subscription = subscriptionMap.get(subscriptionId);
				if (subscription.equals(s))
				{
					return subscriptionId;
				}
			}
			throw new ESException(ESException.INVALID_STATE, "Registered Subscription not found.");
		}
		else
		{
			int subscriptionId = randomKey();
			subscriptionMap.append(subscriptionId, s);
			if (GlobalConfig.shouldLog())
			{
				Log.d(TAG, "registerSubscription() new subscription created for task: " + s.getTask().getSensorType()
						+ " listener: " + s.getListener());
			}
			return subscriptionId;
		}
	}

	public synchronized Subscription removeSubscription(int subscriptionId)
	{
		Subscription s = subscriptionMap.get(subscriptionId);
		if (s == null)
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d(TAG, "removeSubscription() invalid subscription id: " + subscriptionId);
			}
			return null;
		}
		else
		{
			if (GlobalConfig.shouldLog())
			{
				Log.d(TAG, "removeSubscription() deleting subscription created for task: " + s.getTask().getSensorType()
						+ " listener: " + s.getListener());
			}
			subscriptionMap.delete(subscriptionId);
		}
		return s;
	}

	public synchronized List<Subscription> getAllSubscriptions()
	{
		ArrayList<Subscription> list = new ArrayList<Subscription>();
		if (GlobalConfig.shouldLog())
		{
			Log.d(TAG, "List size is: " + subscriptionMap.size());
		}
		
		for (int i = 0; i < subscriptionMap.size(); i++)
		{
			Subscription sub = subscriptionMap.valueAt(i);
			if (sub != null)
			{
				list.add(sub);
			}
		}
		return list;
	}

	public synchronized Subscription getSubscription(int subscriptionId)
	{
		return subscriptionMap.get(subscriptionId);
	}

	private int randomKey() throws ESException
	{
		int subscriptionId = keyGenerator.nextInt();
		int loopCount = 0;
		while (subscriptionMap.get(subscriptionId) != null)
		{
			if (loopCount > 1000)
				throw new ESException(ESException.INVALID_STATE, "Listener map >1000 key conflicts.");
			subscriptionId = keyGenerator.nextInt();
			loopCount++;
		}
		return subscriptionId;
	}
}
