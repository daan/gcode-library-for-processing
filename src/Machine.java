package gcode;

import java.io.*;
import java.util.*; 

import processing.data.*;
import processing.core.*;

import processing.serial.*;



public class Machine {
  Serial mSerial;
  boolean mWaiting = true;
  boolean mBooted = false;
  int mBaud = 115200; // 250000;
  
  boolean mLog = true;

  private StringList mNewReplies= new StringList();; // not yet processed;  
  StringList mReplies = new StringList();
  StringList mExecutedCommands= new StringList();;
  StringList mNewCommands= new StringList();;
  
  int lf = 10; // ASCII linefeed
  int cr = 13; // Carriage Return

  enum Firmware {
    OTHER,
    MARLIN,
    GRBL
  }

  Firmware mFirmware = Firmware.OTHER;


  public Machine() {
  }

  public boolean hasNewReplies() {
    return mReplies.size() > 0;
  }  
  public int numNewReplies() {
    return mReplies.size();
  }  
  
  public StringList getReplies()
  {
    StringList replies = mReplies;
    mReplies = new StringList();
    return replies;
  }

  static public String[] enumeratePorts() {
    return Serial.list();    
  }
  
  public void setBaudRate(int b) {
    mBaud = b;
  }
  
  public void forceReset() {
    mWaiting = false;
  }
    
  public boolean isConnected() {
    return mSerial != null;
  }

  public boolean isBusy() {
    return mWaiting;
  }
  
  public boolean isWaiting() {
    return mWaiting;
  }
  
  public void connect(PApplet a) 
  {
    String[] ports = Serial.list();
    connect(a, ports[ports.length-1], mBaud);
  }
  
  public void connect(PApplet a, String port) {
    mSerial = new Serial(a, port, mBaud); // "/dev/tty.usbmodem1451", 250000);
    mWaiting = false;
    a.registerMethod("pre", this);
  }
  public void connect(PApplet a, String port, int baud) {
    mBaud = baud;
    connect(a, port);
  }


  public void disconnect() {
    if( mSerial == null) return;
    mSerial.clear();
    mSerial.stop();
    mSerial = null;
    // FIXME:: deregister pre method
  }
  
  public boolean schedule(String[] commands) {
    if( mSerial == null) {
      System.out.println("not connected.... not scheduling");
      return false;
    }
    // how to concat two stringlists....
    for(int i=0; i< commands.length; i++) {
      mNewCommands.append(commands[i]);
    }
    return true;
  }
  public boolean schedule(StringList commands) {
    if( mSerial == null) {
      System.out.println("not connected.... not scheduling");
      return false;
    }
    mNewCommands.append(commands);
    return true;
  }
  public boolean schedule(String s) {
    if( mSerial == null) {
      System.out.println("not connected.... not scheduling");
      return false;
    }
    mNewCommands.append(s);
    return true;
  }

  public boolean schedule(GCode g) {
    if( mSerial == null) {
      System.out.println("not connected.... not scheduling");
      return false;
    }
    mNewCommands.append(g.toString());
    return true;
  }

  public boolean schedule(GCodeGraphics g) {
    if( mSerial == null) {
      System.out.println("not connected.... not scheduling");
      return false;
    }
    StringList s = g.getStringList();
    // how to concat two stringlists....
    mNewCommands.append(s);
    return true;
  }
  
  public boolean hasNext() {    
    if (0 != mNewCommands.size()) return true;
    return false;
  }
  
  public int numScheduledCommands() {
    return mNewCommands.size();
  }
  
  public boolean playNextIfReady() {
    if( mSerial == null) return false;
    if( ! hasNext()) return false; 
    if ( mWaiting ) return false;
    boolean ret = send( mNewCommands.get(0) );
    if (ret) mNewCommands.remove(0);
    return ret;
  }
  
  public boolean send(GCode g) {
    return send(g.toString());
  }

  public boolean send(String s) {
    if( mSerial == null) {
      System.out.println("not connected.... not sending");
      return false;
    }
    if( mWaiting ) {
      System.out.println("in waiting mode... not sending");
      return false;
    }
    mSerial.write(s);
    mSerial.write(lf); // line feed
    System.out.println("send: "+s);
    mWaiting = true;
        
    if(mLog) {
      mExecutedCommands.append(s);
    }
    
    return true;
  }
  
  public void home() {
    if (mFirmware == Firmware.GRBL) {
      schedule("$H");
    } else {
      schedule("G28");
    }
  }


  // serial character fetch routine. Buffer until, readLine was not consistent over multiple
  // firmwares. This function retrieves a line, trims it and
  String mFromMachine = "";
  void readSerial() {
    while (mSerial.available() > 0) {    
      char inByte = mSerial.readChar();
      if ( (inByte == 10) || (inByte == 13) ) {      
        mFromMachine = mFromMachine.replaceAll("[\\n\\t ]", "").trim();  // cleanup.
        if (mFromMachine.length() != 0) { 
          mNewReplies.append(mFromMachine);
          mFromMachine = "";
        }
      } else {
        mFromMachine += inByte;
      }
    }
  }

  public void poll() {

    // first send commands if available

    if(mWaiting==false) {
      if( mNewCommands.size() != 0 ) {
        
        if ( send(mNewCommands.get(0)) ) {
          mNewCommands.remove(0);
        }
      }
    }

    // get replies from machine

    readSerial();
    if (mNewReplies.size() == 0) return;
    String ret = mNewReplies.get(0);
    mNewReplies.remove(0);
    
    if(mLog) {
      mReplies.append(ret);
    } else {
      System.out.println(ret); 
    }
    
    // figure out firmware
    if (! mBooted ) {
      if (ret.substring(0,4).equals("Grbl") ) {
        System.out.println("grbl firmware detected");
        mFirmware = Firmware.GRBL;
        mBooted = true;
        mWaiting = false;
      }
      if ( ret.length() > 10) {
        if (ret.substring(0,11).equals("echo:Marlin")) {
          System.out.println("marlin firmware detected");

          mFirmware = Firmware.MARLIN;
          mBooted = true;
          mWaiting = false;
        } 
      }
    }
    if (ret.length() > 5) {
      if( ret.substring(0,5).equals("echo:")) {
        System.out.println("ignore echo");
      }
    }
    
    if( ret.equals("ok") ) {
       mWaiting = false;
       // if we have scheduled commands do them first
    }

  }  
  
  public void pre() {
    poll();
  }

  
} // end of class;