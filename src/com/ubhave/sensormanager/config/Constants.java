package com.ubhave.sensormanager.config;

import android.os.Environment;

public class Constants
{

	public static final boolean TEST_MODE = true;
	public static final String SENSOR_CONFIG_JSON_FILE = "sensorConfig.json";
	
	public static final int STOP_SENSING_BATTERY_LEVEL = 20;

	public static final long EXPERIMENT_CONFIG_UPDATE_INTERVAL = 1 * 60 * 60 * 1000;
	public static final long LOG_FILE_UPLOAD_INTERVAL = 10 * 60 * 60 * 1000;

	public static final String HASH_ALGORITHM = "SHA-256";
	public static final String SERVER_UPLOAD_PASSWD = "w8145r265896q$485*438z";

	public static final String ROOT_DIR = "SensorManagerData";
	public static final String APP_DIR_FULL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ROOT_DIR;
	public static final String SOUNDS_DIR = APP_DIR_FULL_PATH + "/sounds";
	public static final String DATA_LOGS_DIR = APP_DIR_FULL_PATH + "/data_logs";
	public static final String CONFIG_DIR =  APP_DIR_FULL_PATH + "/config";
	public static final String TO_BE_UPLOADED_LOGS_DIR = APP_DIR_FULL_PATH + "/to_be_uploaded";

	public static final long ACCELEROMETER_SAMPLING_WINDOW_SIZE_MILLIS = 8000;
	public static final int BLUETOOTH_SAMPLING_CYCLES = 1;
	public static final long LOCATION_SAMPLING_WINDOW_SIZE_MILLIS = 60000;
	public static final long MICROPHONE_SAMPLING_WINDOW_SIZE_MILLIS = 5000;
	public static final int WIFI_SAMPLING_CYCLES = 1;

	public static final long ACCELEROMETER_SLEEP_INTERVAL = 2 * 60 * 1000;
	public static final long BLUETOOTH_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final long LOCATION_SLEEP_INTERVAL = 15 * 60 * 1000;
	public static final long MICROPHONE_SLEEP_INTERVAL = 2 * 60 * 1000;
	public static final long WIFI_SLEEP_INTERVAL = 15 * 60 * 1000;

	public static final int TRIGGER_MAX_CYCLES = 60;

	public final static String PHYSICAL_ACTIVITY_MOVING = "Moving";
	public final static String PHYSICAL_ACTIVITY_STATIONARY = "Stationary";
	public final static String PHYSICAL_ACTIVITY_UNKNOWN = "Unknown";

	public final static String AUDIO_MODEL_UNKNOWN = "Unknown";

	public final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	public final static int SENSOR_TYPE_BATTERY = 5002;
	public final static int SENSOR_TYPE_BLUETOOTH = 5003;
	public final static int SENSOR_TYPE_LOCATION = 5004;
	public final static int SENSOR_TYPE_MICROPHONE = 5005;
	public final static int SENSOR_TYPE_PHONE_STATE = 5006;
	public final static int SENSOR_TYPE_PROXIMITY = 5007;
	public final static int SENSOR_TYPE_SCREEN = 5008;
	public final static int SENSOR_TYPE_SMS = 5009;
	public final static int SENSOR_TYPE_WIFI = 5010;
	
	public final static String SENSOR_NAME_ACCELEROMETER = "accelerometer";
	public final static String SENSOR_NAME_BATTERY = "battery";
	public final static String SENSOR_NAME_BLUETOOTH = "bluetooth";
	public final static String SENSOR_NAME_LOCATION = "location";
	public final static String SENSOR_NAME_MICROPHONE = "microphone";
	public final static String SENSOR_NAME_PHONE_STATE = "phonestate";
	public final static String SENSOR_NAME_PROXIMITY = "proximity";
	public final static String SENSOR_NAME_SCREEN = "screen";
	public final static String SENSOR_NAME_SMS = "sms";
	public final static String SENSOR_NAME_WIFI = "wifi";
	
	// classifier thresholds
	public final static int LOCATION_CHANGE_DISTANCE_THRESHOLD = 100;
	public final static int ACCELEROMETER_MOVEMENT_THRESHOLD = 25;

}
