package com.ubhave.sensormanager.data.pullsensor;

public class PhoneRadioData
{
	private String mcc;
	private String mnc;
	private int lac;
	private int cid;

	public PhoneRadioData(String mcc, String mnc, int lac, int cid)
	{
		this.mcc = mcc;
		this.mnc = mnc;
		this.lac = lac;
		this.cid = cid;
	}

	public String getMcc()
	{
		return mcc;
	}

	public String getMnc()
	{
		return mnc;
	}

	public int getLac()
	{
		return lac;
	}

	public int getCid()
	{
		return cid;
	}
}
