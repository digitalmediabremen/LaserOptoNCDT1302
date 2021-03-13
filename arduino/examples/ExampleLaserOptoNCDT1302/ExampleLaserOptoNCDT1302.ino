#include "LaserOptoNCDT1302.h"

LaserOptoNCDT1302 mSensor(&Serial2);

void setup() {
  Serial.begin(9600);

  mSensor.setup(9600);
  mSensor.GET_INFO();
  mSensor.LASER_ON();
  mSensor.SET_BAUDRATE(BAUDRATE_9600);
  mSensor.SET_OUTPUTMODE(OUTPUTMODE_CONTINOUS);
  mSensor.SET_OUTPUT_CHANNEL(OUTPUT_CHANNEL_DIGITAL);
  mSensor.ASCII_OUTPUT(ASCII_OUTPUT_ASCII); /* NOTE that it is ASCII */
  mSensor.DAT_OUT_ON();
}

void loop() {
  mSensor.update();
  if (mSensor.data_out()) {
    int mReading = mSensor.sensor_value();
    bool mHasErrors = mSensor.hasReadingErrors(mReading);
    if (!mHasErrors) {
      Serial.println(mReading);
    }
  }

  delay(10);
}
