/* LaserOptoNCDT1302 */

#ifndef LaserOptoNCDT1302_h
#define LaserOptoNCDT1302_h

#include "Arduino.h"

const static int OUTPUT_CHANNEL_ANALOG = 0;
const static int OUTPUT_CHANNEL_DIGITAL = 1; // RS422
const static int OUTPUTMODE_CONTINOUS = 0;
const static int OUTPUTMODE_TIME = 1;
const static int OUTPUTMODE_TRIGGER = 2;
const static int ASCII_OUTPUT_BINARY = 0;
const static int ASCII_OUTPUT_ASCII = 1;
const static int BAUDRATE_115200 = 0;
const static int BAUDRATE_57600 = 1;
const static int BAUDRATE_38400 = 2;
const static int BAUDRATE_19200 = 3;
const static int BAUDRATE_9600 = 4;
const static int ERROR_NO_OBJECT_DETECTABLE = 16370;
const static int ERROR_OBJECT_TOO_CLOSE = 16372;
const static int ERROR_OBJECT_TOO_FAR_AWAY = 16374;
const static int ERROR_OBJECT_NOT_PROCESSABLE = 16376;
const static int ERROR_OBJECT_MOVING_TOWARDS_SENSOR = 16380;
const static int ERROR_OBJECT_MOVING_AWAY_FROM_SENSOR = 16382;

class LaserOptoNCDT1302 {

public:

  LaserOptoNCDT1302(HardwareSerial * pSerial);
  void setup(long BAUD);
  void GET_INFO();
  void LASER_OFF();
  void LASER_ON();
  void DAT_OUT_ON();
  void DAT_OUT_OFF();
  void SET_OUTPUT_CHANNEL(int x);
  void SET_OUTPUTMODE(int x);
  void ASCII_OUTPUT(int x);
  void SET_BAUDRATE(int x);

  void update();
  int sensor_value();
  void send_raw(String);
  void writeStr(String);
  bool data_out();
  bool hasReadingErrors(int pReading);

private:

  String mSensorMsg = String();
  int mSensorValue = 0;
  HardwareSerial * mSerial;
  boolean mDataOut = false;

  boolean available();
  int data();
  void write(char i);
  void cmd_header();
  boolean isBitSet(byte pValue, int pMask);
};

#endif





