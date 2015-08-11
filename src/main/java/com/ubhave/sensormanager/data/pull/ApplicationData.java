package com.ubhave.sensormanager.data.pull;

import java.util.HashMap;

public class ApplicationData extends HashMap<String, String>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5545990366264280102L;
	private final static String APP_LABEL = "application_label";
	private final static String BASE_ACTIVITY = "base_activity";
	private final static String NUM_ACTIVITIES = "num_activities";
	private final static String RUNNING_ACTIVITIES = "running_activities";
	
	public ApplicationData(String label, String base, int activities, int running)
	{
		put(APP_LABEL, label);
		put(BASE_ACTIVITY, base);
		put(NUM_ACTIVITIES, ""+activities);
		put(RUNNING_ACTIVITIES, ""+running);
	}
	
	public ApplicationData()
	{
		
	}
	
	public String getLabel()
	{
		return get(APP_LABEL);
	}
	
	public String getBaseActivityString()
	{
		return get(BASE_ACTIVITY);
	}
	
	public int numberOfActivities()
	{
		return Integer.valueOf(get(NUM_ACTIVITIES));
	}
	
	public int numberOfRunningActivities()
	{
		return Integer.valueOf(get(RUNNING_ACTIVITIES));
	}
}
