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

package com.ubhave.sensormanager.classifier;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;

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

	@SuppressLint("DefaultLocale")
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
