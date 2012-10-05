package com.ubhave.sensormanager.config;



public class SensorConfigUpdater
{
	private static final String TAG = "SensorConfigUpdater";
	
	// this method is called when the sensor needs updating (essentially
	// enabling/disabling) when the sensorConfig.json changes

//	public static void updateSensorConfig()
//	{
//		try
//		{
//			ESSensorManager esSensorManager = ESSensorManager.getSensorManager();
//			// load the sensor config file
//			HashMap<String, SensorConfig> sensorConfigMap = SensorJsonConfig.getSensorConfig();
//			// update sensor manager
//			for (String sensorName : sensorConfigMap.keySet())
//			{
//				int sensorType = SensorConfig.getSensorType(sensorName);
//				SensorConfig sensorConfig = sensorConfigMap.get(sensorName);
//				if (sensorConfig.isSensorEnabled())
//				{
//					ESLogger.log(TAG, "sensor enabled " + sensorName);
//					if (esSensorManager.isSensorStopped(sensorType))
//					{
//						esSensorManager.startSensor(sensorType);
//					}
//					else
//					{
//						// sensor is paused or running
//						ESLogger.log(TAG, "sensor already running or paused");
//					}
//				}
//				else
//				{
//					ESLogger.log(TAG, "sensor disabled " + sensorName);
//					if (esSensorManager.isSensorStopped(sensorType))
//					{
//						ESLogger.log(TAG, "sensor already stopped: " + sensorName);
//					}
//					else
//					{
//						esSensorManager.stopSensor(sensorType);
//					}
//				}
//			}
//		}
//		catch (Exception exp)
//		{
//			ESLogger.error(TAG, exp);
//		}
//	}

}
