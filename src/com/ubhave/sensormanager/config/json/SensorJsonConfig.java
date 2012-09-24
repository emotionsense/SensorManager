package com.ubhave.sensormanager.config.json;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ubhave.sensormanager.config.Constants;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.logs.ESLogger;

public class SensorJsonConfig extends JSONLoader
{
	private static final String TAG = "SensorJsonConfig";
	
	public static HashMap<String, SensorConfig> getSensorConfig()
	{
		try
		{
			String rawJSON = loadFileContents(Constants.SENSOR_CONFIG_JSON_FILE);
			if (rawJSON != null)
			{
				JSONParser p = new JSONParser();
				JSONObject data = (JSONObject) p.parse(rawJSON);
				
				JSONArray sensors = (JSONArray) data.get("sensors");
				
				HashMap<String, SensorConfig> sensorConfigMap = new HashMap<String, SensorConfig>();
				for (Object sensor : sensors)
				{
					SensorConfig sensorConfig = new SensorConfig();
					
					JSONObject sensorJson = (JSONObject)sensor;
					String sensorName = (String)sensorJson.get("sensor");
					String enabled = (String)sensorJson.get("enabled");
					
					sensorConfig.set(SensorConfig.SENSOR_NAME, sensorName);
					sensorConfig.set(SensorConfig.SENSOR_ENABLED, enabled);
					
					sensorConfigMap.put(sensorName, sensorConfig);
					
					ESLogger.log(TAG, "sensorName: " + sensorName + " enabled: " +  enabled);
				}
				return sensorConfigMap;
			}
		}
		catch (Exception exp)
		{
			ESLogger.error(TAG, exp);
		}
		return null;
	}

}
