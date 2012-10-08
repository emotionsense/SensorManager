package com.ubhave.sensormanager.tasks;

import java.util.Random;

import android.util.SparseArray;

import com.ubhave.sensormanager.ESException;

public class SubscriptionList
{
	private final SparseArray<Subscription> subscriptionMap;
	private final Random keyGenerator;
	
	public SubscriptionList()
	{
		subscriptionMap = new SparseArray<Subscription>();
		keyGenerator = new Random();
	}
	
	public int registerSubscription(Subscription s) throws ESException
	{
		if (!s.register()) // subscription already exists
		{
			for (int i=0; i<subscriptionMap.size(); i++)
			{
				int subscriptionId = subscriptionMap.keyAt(i);
				Subscription subscription = subscriptionMap.get(subscriptionId);
				if (subscription.equals(s))
				{
					return subscriptionId;
				}
			}
		}
		
		int subscriptionId = randomKey();
		subscriptionMap.append(subscriptionId, s);
		return subscriptionId;
	}
	
	public Subscription removeSubscription(int subscriptionId)
	{
		Subscription s = subscriptionMap.get(subscriptionId);
		subscriptionMap.delete(subscriptionId);
		return s;
	}
	
	private int randomKey() throws ESException
	{
		int subscriptionId = keyGenerator.nextInt();
		int loopCount = 0;
		while (subscriptionMap.get(subscriptionId) != null)
		{
			if (loopCount > 1000) throw new ESException(ESException.INVALID_STATE, "Listener map >1000 key conflicts.");
			subscriptionId = keyGenerator.nextInt();
			loopCount++;
		}
		return subscriptionId;
	}
}
