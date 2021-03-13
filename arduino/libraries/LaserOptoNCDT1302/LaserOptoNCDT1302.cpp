/* LaserOptoNCDT1302 */

#include "LaserOptoNCDT1302.h"
#define SERIAL_COM Serial
//#define SENSOR_DUMP_DATA

static const char MSG_END = '\r';

LaserOptoNCDT1302::LaserOptoNCDT1302(HardwareSerial * pSerial) {
  mSerial = pSerial;
}

void LaserOptoNCDT1302::setup(long BAUD) {
  mSerial->begin(BAUD);
}

int LaserOptoNCDT1302::sensor_value() {
  return mSensorValue;
}

bool LaserOptoNCDT1302::data_out() {
  return mDataOut;
}

boolean LaserOptoNCDT1302::available() {
  return mSerial->available();
}

int LaserOptoNCDT1302::data() {
  return mSerial->read();
}

void LaserOptoNCDT1302::write(char i) {
  mSerial->write(i);
}

void LaserOptoNCDT1302::update() {
  while (available()) {
    int mRead = data();
    if (mRead == MSG_END) {
      mSensorValue = mSensorMsg.trim().toInt();
      if (mSensorValue == 0) {
        SERIAL_COM.println(mSensorMsg);
#ifdef SENSOR_DUMP_DATA
      } else {
        SERIAL_COM.println(mSensorValue);
#endif
      }
      mSensorMsg = "";
    }
    else {
      mSensorMsg.concat(String((char)mRead));
    }
  }
}

void LaserOptoNCDT1302::cmd_header() {
  write(0x2B);
  write(0x2B);
  write(0x2B);
  write(0x0D);

  write(0x49);
  write(0x4C);
  write(0x44);
  write(0x31);
}

void LaserOptoNCDT1302::send_raw(String pStr) {
  writeStr(pStr);
}

void LaserOptoNCDT1302::writeStr(String pStr) {
  for (unsigned int i = 0; i < pStr.length(); i++) {
    char mChar = pStr.charAt(i);
    write(mChar);
  }
}

bool LaserOptoNCDT1302::hasReadingErrors(int pReading) {
  if (pReading == ERROR_NO_OBJECT_DETECTABLE
      || pReading == ERROR_OBJECT_TOO_CLOSE
      || pReading == ERROR_OBJECT_TOO_FAR_AWAY
      || pReading == ERROR_OBJECT_NOT_PROCESSABLE
      || pReading == ERROR_OBJECT_MOVING_TOWARDS_SENSOR
      || pReading == ERROR_OBJECT_MOVING_AWAY_FROM_SENSOR) {
    return true;
  } else {
    return false;
  }
}

void LaserOptoNCDT1302::GET_INFO() {
  cmd_header();

  write(0x20);
  write(0x49);
  write(0x00);
  write(0x02);
}

void LaserOptoNCDT1302::LASER_OFF() {
  cmd_header();

  write(0x20);
  write(0x86);
  write(0x00);
  write(0x02);
}

void LaserOptoNCDT1302::LASER_ON() {
  cmd_header();

  write(0x20);
  write(0x87);
  write(0x00);
  write(0x02);
}

void LaserOptoNCDT1302::DAT_OUT_ON() {
  mDataOut = true;

  cmd_header();

  write(0x20);
  write(0x77);
  write(0x00);
  write(0x02);
}

void LaserOptoNCDT1302::DAT_OUT_OFF() {
  mDataOut = false;

  cmd_header();

  write(0x20);
  write(0x76);
  write(0x00);
  write(0x02);
}

void LaserOptoNCDT1302::SET_OUTPUT_CHANNEL(int x) {
  cmd_header();

  write(0x20);
  write(0x90);
  write(0x00);
  write(0x03);

  write(0x00);
  write(0x00);
  write(0x00);
  write(x);
}


void LaserOptoNCDT1302::SET_OUTPUTMODE(int x) {
  cmd_header();

  write(0x20);
  write(0xF4);
  write(0x00);
  write(0x03);

  write(0x00);
  write(0x00);
  write(0x00);
  write(x);
}


void LaserOptoNCDT1302::ASCII_OUTPUT(int x) {
  cmd_header();

  write(0x20);
  write(0x88);
  write(0x00);
  write(0x03);

  write(0x00);
  write(0x00);
  write(0x00);
  write(x);
}

void LaserOptoNCDT1302::SET_BAUDRATE(int x) {
  cmd_header();

  write(0x20);
  write(0x80);
  write(0x00);
  write(0x03);

  write(0x00);
  write(0x00);
  write(0x00);
  write(x);
}

/* --- */

boolean LaserOptoNCDT1302::isBitSet(byte pValue, int pMask) {
  return (pValue & (1 << (pMask - 1))) > 0;
}










