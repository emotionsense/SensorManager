package com.ubhave.sensormanager.tasks;

import com.ubhave.sensormanager.SensorDataListener;

public class Subscription
{
	private final AbstractSensorTask task;
	private final SensorDataListener listener;
	
	public Subscription(AbstractSensorTask task, SensorDataListener listener)
	{
		this.task = task;
		this.listener = listener;
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
	
	public boolean equals(Subscription s)
	{
		return (this.task == s.getTask() && this.listener == s.getListener());
	}
}
