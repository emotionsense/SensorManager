package com.ubhave.sensormanager.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ubhave.sensormanager.SurveyApplication;
import com.ubhave.sensormanager.logs.ESLogger;

public class Utilities
{

	private final static String LOG_TAG = "Utilities";

	public static String getCSVStringFromList(ArrayList<String> stringList)
	{
		StringBuffer csvString = new StringBuffer();
		for (String value : stringList)
		{
			if (csvString.length() > 0)
			{
				csvString.append(",");
			}
			csvString.append(value);
		}
		return csvString.toString();
	}

	public static int getMaxValue(Collection<Integer> collection)
	{
		int maxValue = Integer.MIN_VALUE;
		for (Integer value : collection)
		{
			if (value.intValue() > maxValue)
			{
				maxValue = value.intValue();
			}
		}
		return maxValue;
	}

	/**
	 * Takes a CSV string and separates out its components, returning the one
	 * with the given index.
	 * 
	 * @param index
	 *            the 0-based index of the desired component
	 * @param csvString
	 *            the CSV string
	 * @return the <code>index</code>th value in <code>csvString</code>
	 */
	public static String getSubStrFromCSVString(int index, String csvString)
	{
		StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
		int count = 0;
		while (tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			if (count == index)
			{
				return token;
			}
			count += 1;
		}
		return null;
	}

	public static String getDayOfTheWeekAndDayString(long timestamp)
	{
		String returnString = "Day_";
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		returnString += calendar.get(Calendar.DATE);
		return returnString;
	}

	public static void runAsThread(Runnable runnable)
	{
		new Thread(runnable).start();
	}

	public static void sleepWithRandomInc(long millis)
	{
		long inc = (long) ((0.1) * millis * Math.random());
		sleep(millis + inc);
	}

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
	}

	public static void print(String tag, Hashtable<String, Integer> userStatsMap)
	{
		for (String key : userStatsMap.keySet())
		{
			ESLogger.log(tag, key + "  :  " + userStatsMap.get(key));
		}
	}

	public static void copyFile(InputStream in, FileOutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) > 0)
		{
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	public static String getImei()
	{
		Context context = SurveyApplication.getContext();
		if (context == null)
		{
			Log.d(LOG_TAG, "Context is null");
		}
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (manager == null)
		{
			Log.d(LOG_TAG, "TelephonyManager is null");
		}
		String imeiPhone = manager.getDeviceId();
		return imeiPhone;
	}

	public static String convertStreamToString(InputStream is)
	{

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder("");

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append((line));
			}
		}
		catch (Exception exp)
		{
			ESLogger.error(LOG_TAG, exp);
		}
		ESLogger.log(LOG_TAG, "convertStreamToString() returning: " + sb.toString());
		return sb.toString();
	}

	public static void deleteFiles(String directory, File[] files)
	{
		for (File file : files)
		{
			file.delete();
		}
	}

	public static ArrayList<File[]> divideIntoBatches(File[] files, int batchSize)
	{
		ArrayList<File[]> returnList = new ArrayList<File[]>();
		int index = 0;
		ArrayList<File> list = null;
		while (index < files.length)
		{
			if (list == null)
			{
				list = new ArrayList<File>();
			}
			list.add(files[index]);

			// batch complete or last iteration
			if ((list.size() == batchSize) || (index == files.length - 1))
			{
				File[] currBatchFiles = list.toArray(new File[0]);
				returnList.add(currBatchFiles);
				list = null;
			}
			index++;
		}

		return returnList;
	}

	public static File[] getAllFiles(String directory, String fileExtension)
	{
		final String ext = fileExtension;
		File dir = new File(directory);
		File[] files = dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				if (name.endsWith(ext))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		});
		return files;
	}

	public static String zipFiles(String directory, String fileExtension, File[] files)
	{
		String fileType = fileExtension.substring(fileExtension.lastIndexOf(".") + 1, fileExtension.length());
		fileType = fileType.toUpperCase();

		// Create the ZIP file
		String outFilename = directory + "/" + getImei() + "_" + fileType + "_" + System.currentTimeMillis() + ".zip";
		try
		{
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

			// Compress the files
			for (File logFile : files)
			{
				FileInputStream in = new FileInputStream(logFile);

				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(logFile.getName()));

				// Transfer bytes from the file to the ZIP file
				int len;
				byte[] buf = new byte[1024];
				while ((len = in.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
		}
		catch (Exception exp)
		{
			ESLogger.error(LOG_TAG, exp);
		}

		return outFilename;
	}

	public static boolean isNetworkConnected()
	{
		Context context = SurveyApplication.getContext();
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// wifi
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi.isConnected())
		{
			return true;
		}
		// mobile network
		NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mMobile.isConnected())
		{
			return true;
		}

		return false;
	}

	public static boolean isWiFiConnected()
	{
		Context context = SurveyApplication.getContext();
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi.isConnected())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean isWiFiEnabled()
	{
		WifiManager wifiManager = (WifiManager) SurveyApplication.getContext().getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static String hashPhoneNumber(String phoneNumber)
	{
		if ((phoneNumber == null) || (phoneNumber.length() == 0))
		{
			return "";
		}

		// use only the last 10 digits
		if (phoneNumber.length() > 10)
		{
			phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
		}
		MessageDigest mDigest = null;
		try
		{
			mDigest = MessageDigest.getInstance(Constants.HASH_ALGORITHM);
		}
		catch (NoSuchAlgorithmException e)
		{
			ESLogger.error(LOG_TAG, e);
		}
		mDigest.reset();
		byte[] byteArray = mDigest.digest(phoneNumber.getBytes());
		String hash = "";
		for (int i = 0; i < byteArray.length; i++)
		{
			String hexString = Integer.toHexString(0xFF & byteArray[i]);
			if (hexString.length() == 1)
			{
				hexString = "0" + hexString;
			}
			hash += hexString.toUpperCase();
		}
		return hash;
	}

	public static boolean isPhoneCharging()
	{
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = SurveyApplication.getContext().registerReceiver(null, intentFilter);

		// charging or charged
		int chargeStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = ((chargeStatus == BatteryManager.BATTERY_STATUS_CHARGING) || (chargeStatus == BatteryManager.BATTERY_STATUS_FULL));

		return isCharging;
	}
	
    public static void addAllToSet(Set<String> set, String[] strArray)
    {
        for (String element : strArray)
        {
            if (!set.contains(element))
            {
                set.add(element);
            }
        }
    }
	
    public static boolean areSameDeviceAddrSets(String[] prevDevices, String[] currDevices)
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
        if (((prevDevices == null) || (prevDevices.length == 0))
                && ((currDevices == null) || (currDevices.length == 0)))
        {
            return true;
        }

        return false;
    }

    public static boolean areSameMacAddresses(String mac1, String mac2)
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

    public static boolean areSameLocations(Location loc1, Location loc2)
    {
        if ((loc1 != null) && (loc2 != null))
        {
            if (loc1.distanceTo(loc2) < Constants.LOCATION_CHANGE_DISTANCE_THRESHOLD)
            {
                return true;
            }
        }
        if ((loc1 == null) && (loc2 == null))
        {
            return true;
        }
        return false;
    }
}
