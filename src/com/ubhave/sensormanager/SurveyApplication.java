package com.ubhave.sensormanager;

import android.app.Application;
import android.content.Context;

/**
 * Application class to provide the global context.
 */
public class SurveyApplication extends Application
{
	private static SurveyApplication instance;

	public SurveyApplication()
	{
		instance = this;
	}

	public static Context getContext()
	{
		return instance;
	}
}
