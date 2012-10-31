package com.ubhave.sensormanager.tasks;

import com.ubhave.sensormanager.SensorDataListener;

public class Subscription
{
	private final AbstractSensorTask task;
	private final SensorDataListener listener;
	private boolean isPaused = false;
	
	public Subscription(AbstractSensorTask task, SensorDataListener listener)
	{
		this.task = task;
		this.listener = listener;
		this.isPaused = false;
	}
	
	public AbstractSensorTask getTask()
	{
		return task;
	}
	
	public SensorDataListener getListener()
	{
		return listener;
	}
	
	public boolean register()
	{
		return task.registerSensorDataListener(listener);
	}
	
	public void unregister()
	{
		task.unregisterSensorDataListener(listener);
	}
	
	public void pause()
	{
		isPaused = true;
		task.unregisterSensorDataListener(listener);
	}
	
	public void unpause()
	{
		isPaused = false;
		task.registerSensorDataListener(listener);
	}
	
	public boolean isPaused()
	{
		return isPaused;
	}
	
	public boolean equals(Subscription s)
	{
		return (this.task == s.getTask() && this.listener == s.getListener());
	}
}
