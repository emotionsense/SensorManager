package com.ubhave.sensormanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.Utilities;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pushsensor.BatteryData;
import com.ubhave.sensormanager.logs.DataLogger;
import com.ubhave.sensormanager.logs.ESLogger;
import com.ubhave.sensormanager.sensors.AbstractSensor;
import com.ubhave.sensormanager.sensors.SensorDataListener;
import com.ubhave.sensormanager.sensors.SensorInterface;
import com.ubhave.sensormanager.sensors.pull.AccelerometerSensor;
import com.ubhave.sensormanager.sensors.pull.BluetoothSensor;
import com.ubhave.sensormanager.sensors.pull.LocationSensor;
import com.ubhave.sensormanager.sensors.pull.MicrophoneSensor;
import com.ubhave.sensormanager.sensors.pull.PullSensor;
import com.ubhave.sensormanager.sensors.pull.WifiSensor;
import com.ubhave.sensormanager.sensors.push.BatterySensor;
import com.ubhave.sensormanager.sensors.push.PhoneStateSensor;
import com.ubhave.sensormanager.sensors.push.ProximitySensor;
import com.ubhave.sensormanager.sensors.push.PushSensor;
import com.ubhave.sensormanager.sensors.push.ScreenSensor;
import com.ubhave.sensormanager.sensors.push.SmsSensor;
import com.ubhave.sensormanager.service.ExperienceSenseService;
import com.ubhave.sensormanager.service.ServiceAlarmReceiver;

// this class manages all the sensors
public class Temp_ESSensorManager implements ESSensorManagerInterface
{
	private static final String TAG = "ESSensorManager";

	private static Temp_ESSensorManager sensorManager;
	private static Object lock = new Object();
	
	
	private final Context applicationContext;
	private final HashMap<Integer, SensorTask> sensorTaskMap;

	public static Temp_ESSensorManager getSensorManager(Context context) throws ESException
	{
		if (sensorManager == null)
		{
			synchronized (lock)
			{
				if (sensorManager == null)
				{
					if (AbstractSensor.permissionGranted(context, "android.permission.WAKE_LOCK"))
					{
						sensorManager = new Temp_ESSensorManager(context);
						ESLogger.log(TAG, "started.");
					}
					else throw new ESException(ESException.PERMISSION_DENIED, "Sensor Manager requires android.permission.WAKE_LOCK");
				}
			}
		}
		return sensorManager;
	}

	
	private Temp_ESSensorManager(Context appContext)
	{
		applicationContext = appContext;
		sensorTaskMap = new HashMap<Integer, SensorTask>();
		
		// PULL SENSORS
		SensorInterface[] PULL_SENSORS = new SensorInterface[]
		{
				AccelerometerSensor.getAccelerometerSensor(applicationContext),
				BluetoothSensor.getBluetoothSensor(applicationContext),
				LocationSensor.getLocationSensor(applicationContext),
				MicrophoneSensor.getMicrophoneSensor(applicationContext),
				WifiSensor.getWifiSensor(applicationContext)
		};
		
		
		for (SensorInterface aSensor : PULL_SENSORS)
		{
			if (aSensor != null)
			{
				PullSensorTask pullSensorTask = new PullSensorTask(aSensor);
				sensorTaskMap.put(aSensor.getSensorType(), pullSensorTask);
			}
		}

		// PUSH SENSORS
		SensorInterface[] PUSH_SENSORS = new SensorInterface[] {
				BatterySensor.getBatterySensor(applicationContext),
				PhoneStateSensor.getPhoneStateSensor(applicationContext),
				ProximitySensor.getProximitySensor(applicationContext),
				ScreenSensor.getScreenSensor(applicationContext),
				SmsSensor.getSmsSensor(applicationContext)
		};
		for (SensorInterface aSensor : PUSH_SENSORS)
		{
			if (aSensor != null)
			{
				PushSensorTask pushSensorTask = new PushSensorTask(aSensor);
				sensorTaskMap.put(aSensor.getSensorType(), pushSensorTask);
			}
		}
	}
	
//	public void startAllSensors()
//	{
//		// start sensor threads
//		for (SensorTask task : sensorTaskMap.values())
//		{
//			task.start();
//		}
//	}
	
//	public synchronized void stopAllSensors()
//	{
//		for (SensorTask task : sensorTaskMap.values())
//		{
//			task.stopTask();
//		}
//		try
//		{
//			onSensorsStopped();
//		}
//		catch (ESException exp)
//		{
//			ESLogger.error(TAG, exp);
//		}
//	}
	
//	public synchronized void pauseAllSensors(long pauseLength) throws ESException
//	{
//		for (SensorTask task : sensorTaskMap.values())
//		{
//			if (!task.isStopped())
//			{
//				task.pauseTask(pauseLength);
//			}
//		}
//	}

	private void onSensorsStarted()
	{
		startNotification();
		startServiceAlarm();
	}

//	private void onSensorsStopped() throws ESException
//	{
//		stopNotification();
//		stopServiceAlarm();
//	}
	
//	private void stopNotification() throws ESException
//	{
//		ExperienceSenseService.getExperienceSenseService().stopNotification();
//	}
	
//	private void stopServiceAlarm()
//	{
//		ServiceAlarmReceiver.cancelAlarm(applicationContext);
//	}

	private void startNotification()
	{
		// wait for experience service to start for a max of 10 seconds
		// as this is called during system start-up
		long totalWaitTime = 0;
		long sleepTime = 1000;
		ExperienceSenseService esService = null;
		while ((totalWaitTime < (long) (10 * 1000)) && (esService == null))
		{
			Utilities.sleep(sleepTime);
			totalWaitTime += sleepTime;
			try
			{
				esService = ExperienceSenseService.getExperienceSenseService();
			}
			catch (ESException e)
			{
				// ignore
			}
		}

		if (esService != null)
		{
			esService.startNotification();
		}
		else
		{
			ESLogger.error(TAG, " ExperienceSenseService.getExperienceSenseService() returned null.");
		}
	}

	

	private void startServiceAlarm()
	{
		ServiceAlarmReceiver.startAlarm(applicationContext);
	}

	

	

	public synchronized void startSensor(int sensorType) throws ESException
	{
		if (sensorTaskMap.containsKey(sensorType))
		{
			SensorTask sensorTask = sensorTaskMap.get(sensorType);
			sensorTask.startTask();
			onSensorsStarted();
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + sensorType);
		}
	}

	public synchronized void stopSensor(int sensorType) throws ESException
	{
		if (sensorTaskMap.containsKey(sensorType))
		{
			SensorTask sensorTask = sensorTaskMap.get(sensorType);
			sensorTask.stopTask();
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + sensorType);
		}
	}

	

	public synchronized void pauseSensor(int sensorType, long pauseLength) throws ESException
	{
		if (sensorTaskMap.containsKey(sensorType))
		{
			SensorTask sensorTask = sensorTaskMap.get(sensorType);
			sensorTask.pauseTask(pauseLength);
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + sensorType);
		}
	}

	public synchronized boolean isSensorStopped(int sensorType) throws ESException
	{
		if (sensorTaskMap.containsKey(sensorType))
		{
			SensorTask sensorTask = sensorTaskMap.get(sensorType);
			return sensorTask.isStopped();
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + sensorType);
		}
	}

	public synchronized void registerSensorDataListener(int sensorType, SensorConfig sensorConfig, SensorDataListener listener) throws ESException
	{
		if (sensorTaskMap.containsKey(sensorType))
		{
			SensorTask sensorTask = sensorTaskMap.get(sensorType);
			sensorTask.registerSensorDataListener(sensorConfig, listener);
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + sensorType);
		}
	}

	public synchronized void unregisterSensorDataListener(int sensorType, SensorDataListener listener) throws ESException
	{
		if (sensorTaskMap.containsKey(sensorType))
		{
			SensorTask sensorTask = sensorTaskMap.get(sensorType);
			sensorTask.unregisterSensorDataListener(listener);
		}
		else
		{
			throw new ESException(ESException.UNKNOWN_SENSOR_TYPE, "Invalid sensor type: " + sensorType);
		}
	}

	abstract class SensorTask extends Thread
	{
		protected SensorInterface sensor;
		protected Object syncObject = new Object();
		protected int state;
		protected long pauseTime;
		protected ArrayList<SensorDataListener> listenerList;
		protected ArrayList<SensorConfig> listenerConfigList;

		public static final int RUNNING = 6123;
		public static final int PAUSED = 6124;
		public static final int STOPPED = 6125;

		public abstract void run();

		public boolean isRunning()
		{
			if (state == RUNNING)
			{
				return true;
			}
			return false;
		}

		public boolean isPaused()
		{
			if (state == PAUSED)
			{
				return true;
			}
			return false;
		}

		public boolean isStopped()
		{
			if (state == STOPPED)
			{
				return true;
			}
			return false;
		}

		public void registerSensorDataListener(SensorConfig sensorConfig, SensorDataListener listener)
		{
			synchronized (listenerList)
			{
				listenerList.add(listener);
				listenerConfigList.add(sensorConfig);
			}
		}

		public void unregisterSensorDataListener(SensorDataListener listener)
		{
			synchronized (listenerList)
			{
				int index = listenerList.indexOf(listener);
				listenerList.remove(listener);
				listenerConfigList.remove(index);
			}
		}

		public void start()
		{
			state = STOPPED;
			super.start();
		}

		protected SensorConfig getSensorConfig()
		{
			SensorConfig sensorConfig;
			if (listenerConfigList.size() > 0)
			{
				sensorConfig = listenerConfigList.get(0);
			}
			else
			{
				sensorConfig = AbstractSensor.getDefaultSensorConfig(sensor.getSensorType());
			}
			return sensorConfig;
		}

		public void startTask() throws ESException
		{
			if (state == STOPPED)
			{
				synchronized (syncObject)
				{
					syncObject.notify();
				}
			}
			else
			{
				throw new ESException(ESException.INVALID_STATE, "cannot start() the sensor task in the pause or running state");
			}
		}

		protected void pauseTask(long delay) throws ESException
		{
			if (state == STOPPED)
			{
				throw new ESException(ESException.INVALID_STATE, "cannot pause() the sensor task in the stopped state");
			}
			else
			{
				pauseTime = delay;
				synchronized (syncObject)
				{
					state = PAUSED;
					this.interrupt();
				}

			}
		}

		protected void stopTask()
		{
			if (state == STOPPED)
			{
				// ignore
			}
			else
			{
				synchronized (syncObject)
				{
					state = STOPPED;
					this.interrupt();
				}
			}
		}

		protected void logData(SensorData sensorData)
		{
			if (sensorData != null)
			{
				String sensorName = AbstractSensor.getSensorName(sensor.getSensorType());
				ESLogger.log("SensorManager", "Data from: " + sensorName);
				DataLogger.getDataLogger().logData(sensorName, sensorData.toString());

				// update listeners
				for (SensorDataListener listener : listenerList)
				{
					listener.onDataSensed(sensorData);
				}

				// if the battery level is less than 20% then stop sensing
				if (sensorData instanceof BatteryData)
				{
					BatteryData batteryData = (BatteryData) sensorData;
					if ((batteryData.getBatteryLevel() < Constants.STOP_SENSING_BATTERY_LEVEL) && (!batteryData.isCharging()))
					{
//						try
//						{
//							// pause for 30 mins
//							ESSensorManager.this.pauseAllSensors(30 * 60 * 1000);
//						}
//						catch (ESException exp)
//						{
//							ESLogger.error(TAG, exp);
//						}
					}
				}
			}
		}

		public SensorTask(SensorInterface sensor)
		{
			this.sensor = sensor;
			listenerList = new ArrayList<SensorDataListener>();
			listenerConfigList = new ArrayList<SensorConfig>();
		}

	}

	// this is for scheduling pull sensors
	class PullSensorTask extends SensorTask
	{
		public PullSensorTask(SensorInterface sensor)
		{
			super(sensor);
		}

		public void run()
		{
			synchronized (syncObject)
			{
				while (true)
				{
					try
					{
						try
						{
							SensorConfig sensorConfig = getSensorConfig();

							if ((state == PAUSED) || (state == STOPPED))
							{
								if (state == PAUSED)
								{
									syncObject.wait(pauseTime);
								}
								else if (state == STOPPED)
								{
									syncObject.wait();
								}
								state = RUNNING;
								continue;
							}
							else
							{
								long samplingInterval = (Long) sensorConfig.get(SensorConfig.SENSOR_SLEEP_INTERVAL);
								syncObject.wait(samplingInterval);
								if ((state == PAUSED) || (state == STOPPED))
								{
									continue;
								}
							}

							// sense, this is a blocking call and returns when
							// the sensing is complete, the sensorConfig object
							// will have the sampling window, cycle information
							ESLogger.log("SensorManager", "Pulling from: " + AbstractSensor.getSensorName(sensor.getSensorType()));
							SensorData sensorData = ((PullSensor) sensor).sense(sensorConfig);
							// log sensed data
							logData(sensorData);
						}
						catch (InterruptedException exp)
						{
							// ignore
						}
					}
					catch (ESException e)
					{
						ESLogger.error(TAG, e);
						Utilities.sleep(30000);
					}
				}
			}

		}
	}

	// this class is for push sensor tasks
	class PushSensorTask extends SensorTask implements SensorDataListener
	{
		public PushSensorTask(SensorInterface sensor)
		{
			super(sensor);
		}

		public void run()
		{
			while (true)
			{
				try
				{
					if (state == RUNNING)
					{
						if (!(((PushSensor) sensor).isSensing()))
						{
							SensorConfig sensorConfig = getSensorConfig();
							try
							{
								((PushSensor) sensor).startSensing(sensorConfig, this);
							}
							catch (ESException exp)
							{
								ESLogger.error(TAG, exp);
							}
						}
						else
						{
							synchronized (syncObject)
							{
								syncObject.wait();
							}
						}
					}
					else if ((state == PAUSED) || (state == STOPPED))
					{
						if (((PushSensor) sensor).isSensing())
						{
							try
							{
								((PushSensor) sensor).stopSensing(this);
							}
							catch (ESException e)
							{
								ESLogger.error(TAG, e);
							}
						}
						if (state == PAUSED)
						{
							synchronized (syncObject)
							{
								syncObject.wait(pauseTime);
							}
						}
						else if (state == STOPPED)
						{
							synchronized (syncObject)
							{
								syncObject.wait();
							}
						}
						state = RUNNING;
					}
				}
				catch (InterruptedException ie)
				{
					// ignore
				}
			}
		}

		@Override
		public void onDataSensed(SensorData sensorData)
		{
			// log sensed data
			logData(sensorData);
		}
	}

}
