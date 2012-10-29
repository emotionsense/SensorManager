/*
 * Smssensor
 */

package com.ubhave.sensormanager.sensors.push;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.pushsensor.SmsData;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SmsSensor extends AbstractCommunicationSensor
{
	private static final String TAG = "SmsSensor";

	private ContentObserver observer;
	private String prevMessageId;
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	private static SmsSensor smsSensor;
	private static Object lock = new Object();

	public static SmsSensor getSmsSensor(Context context) throws ESException
	{
		if (smsSensor == null)
		{
			synchronized (lock)
			{
				if (smsSensor == null)
				{
					if (permissionGranted(context, "android.permission.RECEIVE_SMS")
							&& permissionGranted(context, "android.permission.READ_SMS"))
					{
						smsSensor = new SmsSensor(context);
					}
					else throw new ESException(ESException. PERMISSION_DENIED, "SMS Sensor : Permission not Granted");
				}
			}
		}
		return smsSensor;
	}

	private SmsSensor(Context context)
	{
		super(context);
		// Create a content observer for sms
		observer = new ContentObserver(new Handler())
		{

			public void onChange(boolean selfChange)
			{
				if (isSensing)
				{
					// check last sent message
					Uri smsUri = Uri.parse("content://sms");
					Cursor cursor = applicationContext.getContentResolver().query(smsUri, null, null, null, null);
					// last sms sent is the fist in the list
					cursor.moveToNext();
					String content = cursor.getString(cursor.getColumnIndex("body"));
					int noOfWords = content.split(" ").length;
					String sentTo = cursor.getString(cursor.getColumnIndex("address"));
					String messageId = cursor.getString(cursor.getColumnIndex("_id"));

					// hash phone number
					sentTo = hashPhoneNumber(sentTo);

					if ((prevMessageId != null) && (prevMessageId.length() > 0) && (prevMessageId.equals(messageId)))
					{
						// ignore, message already logged
					}
					else
					{
						prevMessageId = messageId;

						// add sender and body length to smsActivity
						String logString = System.currentTimeMillis() + " " + SmsData.SMS_CONTENT_CHANGED + " " + content.length() + " words " + noOfWords + " address " + sentTo + " type " + cursor.getString(cursor.getColumnIndex("type")) + " timestamp "
								+ cursor.getString(cursor.getColumnIndex("date"));
						ESLogger.log(TAG, logString);
						logDataSensed(System.currentTimeMillis(), content.length(), noOfWords, sentTo, SmsData.SMS_CONTENT_CHANGED);
					}
				}
			}
		};
	}

	private void logDataSensed(long timestamp, int contentLength, int noOfWords, String addr, String eventType)
	{
		SmsData smsData = new SmsData(timestamp, contentLength, noOfWords, addr, eventType, sensorConfig.clone());
		onDataSensed(smsData);
	}

	public String getLogTag()
	{
		return TAG;
	}

	public int getSensorType()
	{
		return SensorUtils.SENSOR_TYPE_SMS;
	}

	protected void onBroadcastReceived(Context context, Intent intent)
	{
		if (intent.getAction().equals(SMS_RECEIVED))
		{
			Bundle bundle = intent.getExtras();
			SmsMessage[] smsMessagesArray = null;
			if (bundle != null)
			{
				// read the sms received
				try
				{
					Object[] pdusArray = (Object[]) bundle.get("pdus");
					smsMessagesArray = new SmsMessage[pdusArray.length];
					for (int i = 0; i < smsMessagesArray.length; i++)
					{
						smsMessagesArray[i] = SmsMessage.createFromPdu((byte[]) pdusArray[i]);
						String address = smsMessagesArray[i].getOriginatingAddress();
						String content = smsMessagesArray[i].getMessageBody();
						int contentLength = content.length();
						int noOfWords = content.split(" ").length;

						// hash phone number
						address = hashPhoneNumber(address);

						// add sender and body length to smsActivity
						String logString = System.currentTimeMillis() + " "+SmsData.SMS_RECEIVED+" " + contentLength + " words " + noOfWords + " address " + address + " timestamp " + smsMessagesArray[i].getTimestampMillis();
						ESLogger.log(TAG, logString);
						logDataSensed(System.currentTimeMillis(), contentLength, noOfWords, address, SmsData.SMS_RECEIVED);
					}
				}
				catch (Exception e)
				{
					ESLogger.error(TAG, e);
				}
			}
		}
	}

	protected IntentFilter[] getIntentFilters()
	{
		IntentFilter[] filters = new IntentFilter[1];
		filters[0] = new IntentFilter(SMS_RECEIVED);
		return filters;
	}

	protected boolean startSensing()
	{
		prevMessageId = "";

		// register content observer
		ContentResolver contentResolver = applicationContext.getContentResolver();
		contentResolver.registerContentObserver(Uri.parse("content://sms"), true, observer);
		return true;
	}

	protected void stopSensing()
	{
		applicationContext.getContentResolver().unregisterContentObserver(observer);
	}

}
