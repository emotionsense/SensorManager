## Sensor Manager Documentation

#### What is the Sensor Manager library?

The ES Sensor Manager Library is a library project for Android application developers and researchers. The main goal of the project is to make accessing and polling for Android smartphone sensor data easy, highly configurable, and battery-friendly.

We take a very broad definition of the term “sensor:” we mean all of the different components of Android phones that can capture data about:

* The device’s physical behaviour. For example, its current location and movement.
* The phone’s social behaviour, such as calls and SMSs.
* The device status (e.g., screen state or battery level).

The goal of the ES2 Library is to provide quick and seamless access to the Android smartphone sensors, for both one-off and continuous data polling.

#### System Requirements
The ES2 library is available for all Android applications that are running version 2.3.3 and above.

Please note that this library has been built and tested using Android phones, and not the Android Emulator. We currently do not support development via the emulator.

#### Using the Library

See the library [usage](https://github.com/nlathia/SensorManager/blob/master/docs/usage.md) description, and the [ES-Library Examples](https://github.com/nlathia/ESLibrary-Examples) repository.

#### Questions and Contact

If you have any questions, please email [this google group](https://groups.google.com/forum/#!forum/es-library-developers).

#### Research Using the Sensor Manager

See [here](https://github.com/nlathia/SensorManager/blob/master/docs/research.md) for research papers and [here](https://github.com/nlathia/SensorManager/blob/master/docs/apps.md) for apps that use the Sensor Manager.

This library was initially developed as part of the [EPSRC Ubhave](http://ubhave.org/) (Ubiquitous and Social Computing for Positive Behaviour Change) Project, and was first used in the [Emotion Sense](http://emotionsense.org/) application.

#### Types of Sensors

The library defines two broad types of sensors: push sensors and pull sensors. 

##### Push Sensors
[Push sensors](https://github.com/nlathia/SensorManager/tree/master/src/com/ubhave/sensormanager/sensors/push) are the sensors that broadcast notifications about their events to the Android operating system. The library can seamlessly capture a range of these events while it is active. These include:

* **Battery**. This sensor can reveal the current battery level and if the phone’s battery is being charged.
* **Phone State**. While this sensor is active, it gives you access to the device’s call state. From this, you can know when the phone is ringing or off hook (i.e., in a call). The sensor also gives you access to a hashcode of the telephone number that the device is interacting with. In addition, it also gives information on the cell tower that the phone is connected to.
* **Connection State**. Use this sensor to detect events related to the phone connecting or disconnecting from the network.
* **Connection Strength**. Use this sensor to monitor the strength of the device's GSM signal.
* **SMS**. Capture events relating to SMSs being sent and received. This includes the length of the SMS, the number of words, whether it was sent or received, and the hashcode of the other device the phone is interacting with. To query for historical data, see the SMSContentReader sensor (below).
* **Proximity**. This sensor lets you know when there is something close to the phone’s screen. In general, the proximity sensor is used by the phone to know when to ignore screen events (so that, for example, you don’t click screen buttons while speaking on the phone).
* **Screen**. This captures when the phone’s screen is turned on or off.
* **Light**. This captures data from the device's light sensor (see [Android Environment Sensors](http://developer.android.com/guide/topics/sensors/sensors_environment.html)).
* **Passive Location**. This sensor registers a listener to passively receive location updates.

##### Pull Sensors
These sensors need to be actively polled in order to get data from them. There are two sub-categories here: fixed-window sensors that capture data for a predetermined amount of time, and variable-window sensors, that respond with a fixed amount of data, but the time it takes them to do so may vary with the sampling window is defined in terms of number of sampling cycles.

* **Microphone**. The microphone can record a sound clip. For example, the microphone can be set to record 5 seconds of audio data. By default, the audio clip is stored in a temp file which will be replaced the next time the sensor is used. 
* **Accelerometer** and **Gyroscope**. These sensors captures the 3-dimensional acceleration of the phone. The data from accelerometers has been used, for example, to determine whether the phone is moving or stationary. See [Android Motion Sensors](http://developer.android.com/guide/topics/sensors/sensors_motion.html).
* **Bluetooth**. This sensor allows you to seek out the ids of any nearby enabled Bluetooth devices.
* **Wi-Fi**. This allows you to scan for the Wi-Fi fingerprint of all access points that surround the phone.
* Location. This sensor allows you to actively poll for the device’s current fine-grained or coarse-grained location.
* **Application**, **Call Content** and **SMS Content**. These sensors allow you to query for the device’s recent running applications, call logs, and SMS logs.
* **Phone Radio**. Allows you to query for the nearby cell tower IDs.
* **Camera**. This sensor allows you to capture an image from the device's camera (note: currently not working).
