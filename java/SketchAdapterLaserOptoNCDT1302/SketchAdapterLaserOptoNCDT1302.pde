import processing.serial.*;

AdapterLaserOptoNCDT1302 mLaser;
Serial mPort;
boolean isMeasuring;

void setup() {
  size(640, 480);

  textFont(createFont("Helvetica", 48));

  mLaser = new AdapterLaserOptoNCDT1302(mPort);
  mLaser.GET_INFO();
  mLaser.SET_OUTPUT_CHANNEL(AdapterLaserOptoNCDT1302.OUTPUT_CHANNEL_DIGITAL);
  mLaser.SET_OUTPUTMODE(AdapterLaserOptoNCDT1302.OUTPUTMODE_CONTINOUS);
  mLaser.DAT_OUT_ON();
}

void draw() {
  background(50);

  int mReading = mLaser.reading();
  String mError = mLaser.resolveReadingErrorCode(mReading);

  if (mError != null) {
    System.err.println("+++ READING ERROR");
    System.err.println(mError);
  }

  text("M" + mReading, 10, height / 2);
}

void serialEvent(Serial mSerial) {
  if (mSerial == null) {
    return;
  }

  if (mSerial.available() > 0) {
    int mByte = mSerial.read();
    if (!isMeasuring) {
      print((char) mByte);
    }
    if (mLaser != null) {
      mLaser.receiveData(mByte);
    }
  }
}

void keyPressed() {
  if (key == '1') {
    mLaser.GET_INFO();
    println();
  }
  if (key == '2') {
    mLaser.DAT_OUT_OFF();
    isMeasuring = false;
    println();
  }
  if (key == '3') {
    mLaser.DAT_OUT_ON();
    isMeasuring = true;
    println();
  }
  if (key == '4') {
    mLaser.SET_OUTPUTMODE(AdapterLaserOptoNCDT1302.OUTPUTMODE_CONTINOUS);
    println();
  }
  if (key == '5') {
    mLaser.SET_OUTPUTMODE(AdapterLaserOptoNCDT1302.OUTPUTMODE_TIME);
    println();
  }

  if (key == '6') {
    mLaser.LASER_OFF();
    println();
  }
  if (key == '7') {
    mLaser.LASER_ON();
    println();
  }

  if (key == '8') {
    mLaser.ASCII_OUTPUT(AdapterLaserOptoNCDT1302.ASCII_OUTPUT_BINARY);
    println();
  }
  if (key == '9') {
    mLaser.ASCII_OUTPUT(AdapterLaserOptoNCDT1302.ASCII_OUTPUT_ASCII);
    println();
  }

  if (key == '0') {
    mLaser.SET_OUTPUTTIME_MS(100);
    println();
  }
  if (key == '-') {
    mLaser.SET_OUTPUTTIME_MS(500);
    println();
  }
  if (key == 'q') {
    System.out.println("q");
    mLaser.SET_OUTPUT_CHANNEL(AdapterLaserOptoNCDT1302.OUTPUT_CHANNEL_DIGITAL);
  }
  if (key == 'w') {
    System.out.println("w");
    mLaser.SET_OUTPUT_CHANNEL(AdapterLaserOptoNCDT1302.OUTPUT_CHANNEL_ANALOG);
  }
}
