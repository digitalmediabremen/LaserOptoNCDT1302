public class AdapterLaserOptoNCDT1302 {

  private final processing.serial.Serial mSerial;

  private int mLoByte = 0;

  private int mHiByte = 0;

  protected int mReading;

  private int mDelay;

  public static final int READING_RANGE_MIN = 0; // that s not true there is a threshold somewhere around 40, right?

  public static final int READING_RANGE_MAX = 4096;

  public AdapterLaserOptoNCDT1302(processing.serial.Serial pSerial) {
    mSerial = pSerial;
    mDelay = 0;
    LASER_ON();
    ASCII_OUTPUT(ASCII_OUTPUT_BINARY);
  }

  public int min_reading_range() {
    return READING_RANGE_MIN;
  }

  public int max_reading_range() {
    return READING_RANGE_MAX;
  }

  public void command_delay(final int pDelay) {
    mDelay = pDelay;
  }

  public void receiveData(int inByte) {
    //        System.out.println(" > " + byte8ToString(inByte) + " - inByte: " + (char) inByte);
    if (isBitSet(inByte, 7)) {
      mHiByte = inByte;
    } else {
      mLoByte = inByte;
    }
    mReading = convertReadings(mHiByte, mLoByte);
  }

  public static int hi8Byte16(int mByte16) {
    return clamp16(mByte16) >> 8;
  }

  public static int lo8Byte16(int mByte16) {
    return clamp16(mByte16) - (hi8Byte16(mByte16) << 8);
  }

  private int convertReadings(int mHiByte, int mLoByte) {
    try {
      String mHiStr = byte8ToString(mHiByte);
      String mLoStr = byte8ToString(mLoByte);
      String mByte16 = "00" + mHiStr.substring(1, 8) + mLoStr.substring(1, 8);
      return Integer.parseInt(mByte16, 2);
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private void write(int i) {
    if (mSerial != null) {
      mSerial.write(i);
    }
    //        System.out.print((char) i);
  }

  protected void cmd_header() {
    if (mDelay > 0) {
      try {
        Thread.sleep(mDelay);
      } 
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    write(0x2B);
    write(0x2B);
    write(0x2B);
    write(0x0D);

    write(0x49);
    write(0x4C);
    write(0x44);
    write(0x31);
  }

  protected void cmd_footer() {
    /* nothing */
  }

  public void GET_INFO() {
    cmd_header();

    write(0x20);
    write(0x49);
    write(0x00);
    write(0x02);

    cmd_footer();
  }

  public void DAT_OUT_OFF() {
    cmd_header();

    write(0x20);
    write(0x76);
    write(0x00);
    write(0x02);

    cmd_footer();
  }

  public void DAT_OUT_ON() {
    cmd_header();

    write(0x20);
    write(0x77);
    write(0x00);
    write(0x02);

    cmd_footer();
  }

  public static final int OUTPUT_CHANNEL_ANALOG = 0;
  public static final int OUTPUT_CHANNEL_DIGITAL = 1; // RS422

  public void SET_OUTPUT_CHANNEL(int x) {
    cmd_header();

    write(0x20);
    write(0x90);
    write(0x00);
    write(0x03);

    write(0x00);
    write(0x00);
    write(0x00);
    write(x);

    cmd_footer();
  }

  public static final int OUTPUTMODE_CONTINOUS = 0;
  public static final int OUTPUTMODE_TIME = 1;
  public static final int OUTPUTMODE_TRIGGER = 2;

  public void SET_OUTPUTMODE(int x) {
    cmd_header();

    write(0x20);
    write(0xF4);
    write(0x00);
    write(0x03);

    write(0x00);
    write(0x00);
    write(0x00);
    write(x);

    cmd_footer();
  }

  public void LASER_OFF() {
    cmd_header();

    write(0x20);
    write(0x86);
    write(0x00);
    write(0x02);

    cmd_footer();
  }

  public void LASER_ON() {
    cmd_header();

    write(0x20);
    write(0x87);
    write(0x00);
    write(0x02);

    cmd_footer();
  }

  public static final int ASCII_OUTPUT_BINARY = 0;
  public static final int ASCII_OUTPUT_ASCII = 1;

  public void ASCII_OUTPUT(int x) {
    cmd_header();

    write(0x20);
    write(0x88);
    write(0x00);
    write(0x03);

    write(0x00);
    write(0x00);
    write(0x00);
    write(x);

    cmd_footer();
  }

  public void SET_OUTPUTTIME_MS(int x) {
    cmd_header();

    write(0x20);
    write(0xF5);
    write(0x00);
    write(0x03);

    write(0x00);
    write(0x00);
    write(hi8Byte16(x));
    write(lo8Byte16(x));

    cmd_footer();
  }

  public static final int BAUDRATE_115200 = 0;
  public static final int BAUDRATE_57600 = 1;
  public static final int BAUDRATE_38400 = 2;
  public static final int BAUDRATE_19200 = 3;
  public static final int BAUDRATE_9600 = 4;

  public void SET_BAUDRATE(int x) {
    cmd_header();

    write(0x20);
    write(0x80);
    write(0x00);
    write(0x03);

    write(0x00);
    write(0x00);
    write(0x00);
    write(x);

    cmd_footer();
  }

  public static final int ERROR_NO_OBJECT_DETECTABLE = 16370;

  public static final int ERROR_OBJECT_TOO_CLOSE = 16372;

  public static final int ERROR_OBJECT_TOO_FAR_AWAY = 16374;

  public static final int ERROR_OBJECT_NOT_PROCESSABLE = 16376;

  public static final int ERROR_OBJECT_MOVING_TOWARDS_SENSOR = 16380;

  public static final int ERROR_OBJECT_MOVING_AWAY_FROM_SENSOR = 16382;

  public String resolveReadingErrorCode(final int pReading) {
    String mErrorString = null;
    switch (pReading) {
    case 16370:
      mErrorString = "kein Objekt erkennbar";
      break;
    case 16372:
      mErrorString = "zu nah am Sensor";
      break;
    case 16374:
      mErrorString = "zu weit vom Sensor";
      break;
    case 16376:
      mErrorString = "Messobjekt nicht auswertbar";
      break;
    case 16380:
      mErrorString = "Messobjekt bewegt sich auf Sensor zu";
      break;
    case 16382:
      mErrorString = "Messobjekt bewegt sich vom Sensor weg";
      break;
    }
    return mErrorString;
  }

  public boolean hasReadingErrors(int mReading) {
    if (mReading == ERROR_NO_OBJECT_DETECTABLE
      || mReading == ERROR_OBJECT_TOO_CLOSE
      || mReading == ERROR_OBJECT_TOO_FAR_AWAY
      || mReading == ERROR_OBJECT_NOT_PROCESSABLE
      || mReading == ERROR_OBJECT_MOVING_TOWARDS_SENSOR
      || mReading == ERROR_OBJECT_MOVING_AWAY_FROM_SENSOR) {
      return true;
    } else {
      return false;
    }
  }

  public int reading() {
    return mReading;
  }

  /*
   *
   * CI-Mode: 1402
   * ILD 1302: Standard
   * A/N: 4120168
   * O/N: 000
   * S/N: 0911042
   * MR: 200
   * SoftVer: 1.005.4
   * BootVer: 1.002.1
   * Date: 09/11/13
   * Out Channel: analog
   * Analog Error: error value
   * Filter Type: moving average
   * Filter Number: 1
   * Scanrate: 750 Hz
   * type of digital output: binary
   * mode of analog/digital output: continuous
   * key status: auto lock
   * mode of save setting: save at each time
   * mode of extern input: as teach in
   * peak searching: global maximum
   * threshold: standard
   * Teach value 1: 0.00
   * Teach value 2: 16368.00
   *
   */

  public processing.serial.Serial serial() {
    return mSerial;
  }

  public static boolean isBitSet(int mByte8, int mBytePosition) {
    return isBitSetMask(mByte8, 1 << mBytePosition);
  }

  public static boolean isBitSetMask(int mByte8, int mMask) {
    return (clamp8(mByte8) & clamp8(mMask)) > 0;
  }

  public static int clamp8(int mByte) {
    return Math.max(0, Math.min((1 << 8) - 1, mByte));
  }

  public static int clamp16(int mByte) {
    return Math.max(0, Math.min((1 << 16) - 1, mByte));
  }

  public static String byte8ToString(int mByte) {
    mByte = clamp8(mByte);
    final int i = Integer.numberOfLeadingZeros(mByte) - 24;
    if (mByte == 0) {
      return "00000000";
    } else {
      return getLeadingZeros(i) + Integer.toBinaryString(mByte);
    }
  }

  public static String getLeadingZeros(int k) {
    StringBuilder mBuffer = new StringBuilder();
    for (int i = 0; i < k; i++) {
      mBuffer.append("0");
    }
    return mBuffer.toString();
  }
}
