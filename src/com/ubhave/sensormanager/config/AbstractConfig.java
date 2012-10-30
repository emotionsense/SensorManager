package com.ubhave.sensormanager.config;

import java.util.HashMap;

public class AbstractConfig
{
	protected final HashMap<String, Object> configParams;

	public AbstractConfig()
	{
		configParams = new HashMap<String, Object>();
	}

	public void setParameter(String parameterName, Object parameterValue)
	{
		configParams.put(parameterName, parameterValue);
	}

	public Object getParameter(String parameterName)
	{
		Object parameterValue = null;
		if (configParams.containsKey(parameterName))
		{
			parameterValue = configParams.get(parameterName);
		}
		return parameterValue;
	}

	public boolean containsParameter(String parameterName)
	{
		if (configParams.containsKey(parameterName))
		{
			return true;
		}
		return false;
	}
	
	public void removeParameter(String parameterName)
	{
		if (configParams.containsKey(parameterName))
		{
			configParams.remove(parameterName);
		}
	}

}
