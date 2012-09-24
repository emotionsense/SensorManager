package com.ubhave.sensormanager.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ubhave.sensormanager.logs.ESLogger;

public class ServiceAlarmReceiver extends BroadcastReceiver
{

	private static final String TAG = "ServiceAlarmReceiver";
	private static final Object lock = new Object();

	public void onReceive(Context context, Intent intent)
	{
		ESLogger.log(TAG, "onReceive() method called");
		startServices(context);
	}

	public static void startAlarm(Context context)
	{
		synchronized (lock)
		{

			ESLogger.log(TAG, "startAlarm() called");

			// cancel any existing alarms
			cancelAlarm(context);

			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(context, ServiceAlarmReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, 0);

			// every 15 minutes
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, 15 * 60 * 1000, pi);
		}
	}

	public static void cancelAlarm(Context context)
	{
		ESLogger.log(TAG, "cancelAlarm() called");
		Intent intent = new Intent(context, ServiceAlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	private static void startServices(Context context)
	{
		synchronized (lock)
		{
			ESLogger.log(TAG, "startServices()");

			// start es service if it is not started
			Intent i = new Intent();
			i.setAction("com.lathia.experiencesense.ES_SERVICE");
			// this starts service, if it is not already running
			context.startService(i);
		}
	}

}
