package com.ubhave.sensormanager.logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.Utilities;

public class DataLogger
{

	private static final String TAG = "DataLogger";

	private static DataLogger dataLogger;
	private static Object lock = new Object();

	private final Timer timer;
	private final LogDataToFileTask logDataToFileTask;
	private final HashMap<String, DataHolder> dataMap;
	private final Context applicationContext;

	class DataHolder
	{
		StringBuilder cache;
		FileOutputStream fos;
	}

	public static DataLogger getDataLogger(Context context)
	{
		if (dataLogger == null)
		{
			synchronized (lock)
			{
				if (dataLogger == null)
				{
					dataLogger = new DataLogger(context);
				}
			}
		}
		return dataLogger;
	}

	private DataLogger(Context context)
	{
		applicationContext = context;
		dataMap = new HashMap<String, DataHolder>();
		logDataToFileTask = new LogDataToFileTask();
		timer = new Timer();
		// run every 5 mins
		timer.schedule(logDataToFileTask, 2 * 60 * 1000, 5 * 60 * 1000);
	}

	public void logData(String tag, String logString)
	{
		ESLogger.log(TAG, tag + " " + logString);
		synchronized (dataMap)
		{
			DataHolder dh;
			if (dataMap.containsKey(tag))
			{
				dh = dataMap.get(tag);
			}
			else
			{
				dh = new DataHolder();
				dh.cache = new StringBuilder();
				dh.fos = null;
				dataMap.put(tag, dh);
			}
			dh.cache.append(logString + "\n");
		}
	}

	public void moveFilesForUploading(String targetDir)
	{
		ESLogger.log(TAG, "moveFilesForUploading() called");
		synchronized (dataMap)
		{
			logDataToFileTask.run();
			for (String key : dataMap.keySet())
			{
				DataHolder dh = dataMap.get(key);
				if (dh.fos != null)
				{
					try
					{
						// close
						dh.fos.flush();
						dh.fos.close();
					}
					catch (IOException e)
					{
						ESLogger.error(TAG, e);
					}
				}
			}
			// clear hash map
			dataMap.clear();

			// move all files to targetDir folder
			File[] allLogFiles = Utilities.getAllFiles(Constants.DATA_LOGS_DIR, ".log");
			for (File file : allLogFiles)
			{
				file.renameTo(new File(targetDir + "/" + file.getName()));
			}
		}
	}

	class LogDataToFileTask extends TimerTask
	{
		public void run()
		{
			ESLogger.log(TAG, " run(), logging cached data to files");
			synchronized (dataMap)
			{
				// log the data in the data holder cache to
				// corresponding files
				for (String key : dataMap.keySet())
				{
					DataHolder dh = dataMap.get(key);
					try
					{
						if (dh.fos == null)
						{
							String fileName = Constants.DATA_LOGS_DIR + "/" + Utilities.getImei(applicationContext) + "_" + key + "_" + System.currentTimeMillis() + ".log";
							ESLogger.log(TAG, "Creating new log file: " + fileName);
							dh.fos = new FileOutputStream(fileName);
						}
						if (dh.cache.length() > 0)
						{
							ESLogger.log(TAG, "writing cached data to file for tag: " + key);
							// write to data log file
							dh.fos.write(dh.cache.toString().getBytes());
							dh.fos.flush();
							// clear cache
							dh.cache.delete(0, dh.cache.length());
						}
					}
					catch (IOException exp)
					{
						ESLogger.error(TAG, exp);
					}
				}
			}
		}
	}
}
