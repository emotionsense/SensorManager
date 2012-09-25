package com.ubhave.sensormanager.service;

import java.io.File;
import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.R;
import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.logs.ESLogger;



public class ExperienceSenseService extends Service
{

	private static ExperienceSenseService esService = null;
	public PowerManager.WakeLock wakeLock;
	public WifiManager.WifiLock wifiLock;
	private static final int NOTIFICATION_ID = 1;
	private static final Object lock = new Object();

	private static final String TAG = "ExperienceSenseService";

	public void onCreate()
	{
		if (esService != null)
		{
			ESLogger.log(TAG, "Service already started");
		}
		else
		{
			synchronized (lock)
			{
				if (esService == null)
				{
					ESLogger.log(TAG, "Service starting");
					super.onCreate();
					acquireWakeLocks();
					createDirs();
					startServices();
					esService = this;
					ESLogger.log(TAG, "Service start complete");
				}
			}
		}
	}

	public static ExperienceSenseService getExperienceSenseService() throws ESException
	{
		if (esService == null)
		{
			throw new ESException(ESException.EXPERIENCE_SERVICE_NOT_STARTED, "ExperienceSenseService not started.");
		}
		return esService;
	}

	public void shutdown()
	{
		wakeLock.release();
		wifiLock.release();
	}

	private void startServices()
	{
		try {
			ESSensorManager.getSensorManager(this).startAllSensors();
		}
		catch(ESException e)
		{
			// Handle error
		}
		

		// test cases
		// ESTests esTests = new ESTests();
		// esTests.start();
	}

	private void acquireWakeLocks()
	{
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// Needed to keep processing
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wakelock");
		wakeLock.acquire();

		// Wifi lock
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null)
		{
			wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "WifiLock");
			wifiLock.acquire();
		}

	}

	private static final Class<?>[] mSetForegroundSignature = new Class[] { boolean.class };
	private static final Class<?>[] mStartForegroundSignature = new Class[] { int.class, Notification.class };
	private static final Class<?>[] mStopForegroundSignature = new Class[] { boolean.class };

	private NotificationManager mNotificationManager;
	private Method mSetForeground;
	private Method mStartForeground;
	private Method mStopForeground;
	private Object[] mSetForegroundArgs = new Object[1];
	private Object[] mStartForegroundArgs = new Object[2];
	private Object[] mStopForegroundArgs = new Object[1];

	void invokeMethod(Method method, Object[] args)
	{
		try
		{
			method.invoke(this, args);
		}
		catch (Exception exp)
		{
			// Should not happen.
			ESLogger.error(TAG, exp);
		}
	}

	/**
	 * This is a wrapper around the new startForeground method, using the older
	 * APIs if it is not available.
	 */
	void startForegroundCompat()
	{
		int icon = R.drawable.ic_launcher_es;
		String applicationName = this.getString(R.string.app_name);
		CharSequence tickerText = applicationName + " moving to background";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;

		Context context = getApplicationContext();
		CharSequence contentTitle = applicationName;
		CharSequence contentText = "Sensing Ongoing";
		PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		// If we have the new startForeground API, then use it.
		if (mStartForeground != null)
		{
			mStartForegroundArgs[0] = Integer.valueOf(NOTIFICATION_ID);
			mStartForegroundArgs[1] = notification;
			invokeMethod(mStartForeground, mStartForegroundArgs);
			return;
		}

		// Fall back on the old API.
		mSetForegroundArgs[0] = Boolean.TRUE;
		invokeMethod(mSetForeground, mSetForegroundArgs);
		mNotificationManager.notify(1, notification);
	}

	/**
	 * This is a wrapper around the new stopForeground method, using the older
	 * APIs if it is not available.
	 */
	void stopForegroundCompat(int id)
	{
		// If we have the new stopForeground API, then use it.
		if (mStopForeground != null)
		{
			mStopForegroundArgs[0] = Boolean.TRUE;
			invokeMethod(mStopForeground, mStopForegroundArgs);
			return;
		}

		// Fall back on the old API. Note to cancel BEFORE changing the
		// foreground state, since we could be killed at that point.
		if (mNotificationManager != null)
		{
			mNotificationManager.cancel(id);
			mSetForegroundArgs[0] = Boolean.FALSE;
			invokeMethod(mSetForeground, mSetForegroundArgs);
		}
	}

	public void startNotification()
	{
		// create a notification
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);

		try
		{
			mStartForeground = getClass().getMethod("startForeground", mStartForegroundSignature);
			mStopForeground = getClass().getMethod("stopForeground", mStopForegroundSignature);
		}
		catch (NoSuchMethodException e)
		{
			// Running on an older platform.
			ESLogger.log(TAG, "running on older platform");
			mStartForeground = mStopForeground = null;
		}
		if (mStartForeground == null)
		{
			try
			{
				mSetForeground = getClass().getMethod("setForeground", mSetForegroundSignature);
			}
			catch (NoSuchMethodException e)
			{
				ESLogger.error(TAG, e);
				throw new IllegalStateException("OS doesn't have Service.startForeground OR Service.setForeground!");
			}
		}

		startForegroundCompat();
	}

	public void stopNotification()
	{
		stopForegroundCompat(NOTIFICATION_ID);
	}

	public void onDestroy()
	{
		// Make sure our notification is gone.
		stopNotification();

		// TODO
		// close all threads, services here

		esService = null;
	}

	private void createDirs()
	{
		// create dirs

		String[] dirs = { Constants.APP_DIR_FULL_PATH, Constants.SOUNDS_DIR, Constants.DATA_LOGS_DIR, Constants.TO_BE_UPLOADED_LOGS_DIR, Constants.CONFIG_DIR };

		for (String dir : dirs)
		{
			File file = new File(dir);
			if (!file.exists())
			{
				file.mkdirs();
			}
		}
	}

	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
