package com.ubhave.sensormanager.classifier;

import java.util.HashSet;
import java.util.Set;

public abstract class SocialClassifier
{
	protected boolean areSameDeviceAddrSets(String[] prevDevices, String[] currDevices)
	{
		// both are non-empty
		if ((prevDevices != null) && (prevDevices.length > 0) && (currDevices != null) && (currDevices.length > 0))
		{
			// put both the device arrays in hash set to remove duplicates
			HashSet<String> prevHashSet = new HashSet<String>();
			addAllToSet(prevHashSet, prevDevices);

			HashSet<String> currHashSet = new HashSet<String>();
			addAllToSet(currHashSet, currDevices);

			if (prevHashSet.size() == currHashSet.size())
			{
				for (String prevDevice : prevHashSet)
				{
					// check if it is in currHashSet
					boolean found = false;
					for (String currDevice : currHashSet)
					{
						if (areSameMacAddresses(prevDevice, currDevice))
						{
							found = true;
							break;
						}
					}
					if (!found)
					{
						return false;
					}
				}
				return true;
			}
			return false;
		}

		// both are empty
		if (((prevDevices == null) || (prevDevices.length == 0)) && ((currDevices == null) || (currDevices.length == 0)))
		{
			return true;
		}

		return false;
	}

	private static void addAllToSet(Set<String> set, String[] strArray)
	{
		for (String element : strArray)
		{
			if (!set.contains(element))
			{
				set.add(element);
			}
		}
	}

	private static boolean areSameMacAddresses(String mac1, String mac2)
	{
		boolean sameMacs = false;
		if ((mac1 != null) && (mac1.length() > 0) && (mac2 != null) && (mac2.length() > 0))
		{
			if (mac1.toLowerCase().equals(mac2.toLowerCase()))
			{
				sameMacs = true;
			}
		}
		if (((mac1 == null) || (mac1.length() == 0)) && ((mac2 == null) || (mac2.length() == 0)))
		{
			return true;
		}
		return sameMacs;
	}
}
