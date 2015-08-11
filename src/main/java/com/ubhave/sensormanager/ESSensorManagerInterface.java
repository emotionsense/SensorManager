/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk
 Kiran Rachuri, kiran.rachuri@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

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

package com.ubhave.sensormanager;

import com.ubhave.sensormanager.data.SensorData;

public interface ESSensorManagerInterface
{
	/*
	 * Getting data from sensors
	 */
	public int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException;

	public void unsubscribeFromSensorData(int subscriptionId) throws ESException;

	public SensorData getDataFromSensor(int sensorId) throws ESException;
	
	/*
	 * Pause / Unpause
	 */
	
	public void pauseSubscription(int subscriptionId) throws ESException;
	
	public void unPauseSubscription(int subscriptionId) throws ESException;
	
	/*
	 * Getting/setting configuration parameters
	 */
	public void setSensorConfig(int sensorId, String configKey, Object configValue) throws ESException;
	
	public Object getSensorConfigValue(int sensorId, String configKey) throws ESException;
	
	public void setGlobalConfig(String configKey, Object configValue) throws ESException;
	
	public Object getGlobalConfig(String configKey) throws ESException;
	
}
