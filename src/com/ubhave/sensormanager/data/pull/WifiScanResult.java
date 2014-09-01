package com.ubhave.sensormanager.data.pull;

public class WifiScanResult
{
	private String ssid;
	private String bssid;
	private String capabilities;
	private int level;
	private int frequency;
	
	public WifiScanResult(String ssid, String bssid, String capabilities, int level, int frequency)
	{
		this.ssid = ssid;
		this.bssid = bssid;
		this.capabilities = capabilities;
		this.level = level;
		this.frequency = frequency;
	}
	
	public String getSsid()
	{
		return ssid;
	}

	public String getBssid()
	{
		return bssid;
	}

	public String getCapabilities()
	{
		return capabilities;
	}

	public int getLevel()
	{
		return level;
	}

	public int getFrequency()
	{
		return frequency;
	}

}
