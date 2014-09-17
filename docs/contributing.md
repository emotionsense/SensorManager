## Contributing to the Library

### Adding a Sensor

How can you add a new sensor to the library?

In the Sensor Manager library:

1. Implement the class that samples data from the sensor. Depending on the type of sensor, it should either inherit from AbstractPullSensor.java, AbstractPushSensor.java, or AbstractEnvironmentSensor.java, and be in the ```com.ubhave.sensormanager.sensors``` package.
2. Implement the data structure that holds your sensor data in the ```com.ubhave.sensormanager.data``` package.
3. Implement the data processor that creates an instance of the data structure in the ```com.ubhave.sensormanager.process``` package.
4. Update the ```SensorUtils.java``` class: add an int identifier, a String name, and update the ```getSensor()``` method to return your sensor. 
5. Add an entry in ```SensorEnum.java``` for your new sensor.

In the Sensor Data Manager library:

1. Add your Sensor data to JSON formatter in ```com.ubhave.dataformatter.json```.
2. Update the ```getJSONFormatter()``` method in ```DataFormatter.java``` to return your data formatter.
