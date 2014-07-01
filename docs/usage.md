## Using the ES Sensor Manager

### Setup

We assume that you have downloaded the ```sensormanager.jar``` file from [here](https://github.com/nlathia/SensorManager/blob/master/bin/sensormanager.jar) and saved it in the ```libs/``` directory of your application project. 

To add the ES library via Eclipse:

1. Go to your Project Properties. On a Mac, you do so by selecting Project >> Properties.
2. In your project properties window, select Java Build Path
3. Select the Libraries tab
4. Click Add External JARs, and then find and select the ```sensormanager.jar``` file to add it to your project
5. Select the Order and Export tab
6. Make sure that the sensormanager.jar file is checked.

Alternatively, you just need to make sure that the following line is in your project ```.classpath:```

```
<classpathentry exported="true" kind="lib" path="lib/sensormanager.jar"/>
```

### Adding Permissions to your Application

The ES Sensor Manager will only be able to access sensor data if your application has the required permissions. Note that you **only need to add those permissions that suit your data collection needs**. You do not need to add all of the following, only those permissions for the sensors you will be accessing.

The easiest way to see what permissions a sensor requires is to look at the code: each sensor checks that its required permissions are granted at run time in their factory method. In particular, some sensors require more than one permission, while others (e.g., Location) require at least one permission.

There are some sensors that are not listed in the table (e.g. Accelerometer). These sensors do not need any additional permissions in order to be used.


<table>
<tbody>
<tr>
<td><b>Sensor</b></td>
<td><b>Permissions</b></td>
</tr>

<tr>
<td>SMS</td>
<td>android.permission.RECEIVE_SMS
<br>android.permission.READ_SMS
</td>
</tr>

<tr>
<td>Phone State</td>
<td>android.permission.PROCESS_OUTGOING_CALLS
<br>android.permission.READ_PHONE_STATE
</td>
</tr>

<tr>
<td>Wi-Fi</td>
<td>android.permission.ACCESS_WIFI_STATE
<br>android.permission.ACCESS_NETWORK_STATE
<br>android.permission.CHANGE_WIFI_STATE
</td>
</tr>

<tr>
<td>Microphone</td>
<td>android.permission.RECORD_AUDIO
</td>
</tr>

<tr>
<td>Location</td>
<td>android.permission.ACCESS_COARSE_LOCATION
<br>(or) android.permission.ACCESS_FINE_LOCATION
</td>
</tr>

<tr>
<td>Bluetooth</td>
<td>android.permission.BLUETOOTH
<br>android.permission.BLUETOOTH_ADMIN
</td>
</tr>

<tr>
<td>Connection State</td>
<td>android.permission.ACCESS_WIFI_STATE
<br>android.permission.ACCESS_NETWORK_STATE
</td>
</tr>

<tr>
<td>Camera</td>
<td>android.permission.CAMERA
</td>
</tr>

<tr>
<td>CallContentReader</td>
<td>android.permission.READ_CALL_LOG
</td>
</tr>

<tr>
<td>SMSContentReader</td>
<td>android.permission.READ_SMS_LOG
</td>
</tr>

<tr>
<td>Continuous Sensing</td>
<td>android.permission.WAKE_LOCK
</td>
</tr>

</tbody>
</table>

### Collecting Sensor Data

Depending on the application that you are implementing, there are two different ways that the library allows you to access sensor data. Broadly speaking, these are (a) one-off access, and (b) continuous sampling.

1. **One-Off Access**. When your application needs some sensor data (for example, 8 seconds from the accelerometer), you can call the sensor manager library and ask for the sensor to be sampled.
2. **Continuous Sampling**. When your application, instead, needs to collect sensor data more than just once, we have implemented a means for your Activity or Service to register that it is interested in receiving a particular sensor’s data: data will then be pushed to your Activity or Service after it has been sensed.

The Sensor Manager library has an interface that you can use to access everything that you need. This interface is found in the file: ```com.ubhave.sensormanager.ESSensorManagerInterface.java```.

In short, this interface allows you to:
* Get one sample of data from a sensor:
```
public SensorData getDataFromSensor(int sensorId) throws ESException;
```
* Subscribe and unsubscribe from receiving sensor data:
```
public int subscribeToSensorData(int sensorId, SensorDataListener listener) throws ESException;
public void unsubscribeFromSensorData(int subscriptionId) throws ESException;
```
* Pause and unpause a subscription:
```
public void pauseSubscription(int subscriptionId) throws ESException;
public void unPauseSubscription(int subscriptionId) throws ESException;
```
* Get and set sensor-specific configuration parameters
```
public void setSensorConfig(int sensorId, String configKey, Object configValue) throws ESException;
public Object getSensorConfigValue(int sensorId, String configKey) throws ESException;
```
* Get and set sensor-manager (global) configuration parameters  
```
public void setGlobalConfig(String configKey, Object configValue) throws ESException;
public Object getGlobalConfig(String configKey) throws ESException;
```

The sensor manager allows you to differentiate between different sensors via their id. These have all been defined as static integer identifiers in the ```SensorUtils.java``` class.

#### 
