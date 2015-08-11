/* **************************************************
 Copyright (c) 2014, Idiap
 Olivier Bornet, olivier.bornet@idiap.ch

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.sensormanager.data.pull;

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
