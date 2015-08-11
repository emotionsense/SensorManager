package com.ubhave.sensormanager.process.push;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.SmsData;
import com.ubhave.sensormanager.process.CommunicationProcessor;

public class SMSProcessor extends CommunicationProcessor
{
	/*
	 * Note: Word Analysis of the SMS Message
	 * Requires a file of the form:
	 * word,category_1,...,category_n
	 */
	
	private final static String wordMapFile = "UNIMPLEMENTED";
	private final HashMap<String, ArrayList<String>> wordCategoryMap;
	
	public SMSProcessor(Context c, boolean rw, boolean sp)
	{
		super(c, rw, sp);
		wordCategoryMap = loadSentimentMap();
	}

	public SmsData process(long timestamp, SensorConfig config, String content, String address, String mesgType, String event)
	{
		SmsData data = new SmsData(timestamp, config);
		String[] words = content.split(" ");
		
		if (setRawData)
		{
			data.setNumberOfWords(words.length);
			data.setContentLength(content.length());
			data.setAddress(hashPhoneNumber(address));
			data.setMessageType(mesgType);
			data.setEventType(event);
		}
		
		if (setProcessedData)
		{
			/*
			 * WARNING: untested
			 */
			for (String word : words)
			{
				ArrayList<String> categories = getCategories(word);
				for (String category : categories)
				{
					data.addCategory(category);
				}
			}
		}	
		return data;
	}
	
	private ArrayList<String> getCategories(String word)
	{
		ArrayList<String> categories = new ArrayList<String>();
		for (String liwcWord : wordCategoryMap.keySet())
		{
			if (word.matches(liwcWord))
			{
				categories.addAll(wordCategoryMap.get(liwcWord));
			}
		}
		return categories;
	}
	
	private HashMap<String, ArrayList<String>> loadSentimentMap()
	{
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(appContext.getAssets().open(wordMapFile)));
			String line;
			while ((line = in.readLine()) != null)
			{
				String[] fields = line.split(",");
				String word = fields[0];
				ArrayList<String> categories = new ArrayList<String>();
				for (int i=1; i<fields.length; i++)
				{
					categories.add(fields[1]);
				}
				map.put(word, categories);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			map.clear();
		}
		return map;
	}
}
