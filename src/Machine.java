package gcode;

import java.io.*;
import java.util.*; 

import processing.data.*;
import processing.core.*;

import processing.serial.*;



public class Machine {
  Serial mSerial;
  boolean mWaiting;
  int mBaud = 115200; // 250000;
  
  boolean mLog = true;
  
  StringList mReplies;
  StringList mExecutedCommands;
  StringList mNewCommands;
  
  int lf = 10; // ASCII linefeed
  int cr = 13; // Carriage Return

  public Machine() {
    mReplies = new StringList();
    mExecutedCommands = new StringList();
    mNewCommands = new StringList();
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
    return ! mWaiting;
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
    mSerial.bufferUntil(lf);
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
    // how to concat two stringlists....
    for(int i=0; i< commands.size(); i++) {
      mNewCommands.append(commands.get(i));
    }
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
  
  public void poll() {
    if( mSerial == null) return;
    if( 0 == mSerial.available() ) return;
    // since we use bufferUntil linefeed  we should get a line.
    String ret = mSerial.readString().trim();
    
    if(mLog) {
      mReplies.append(ret);
    } else {
      System.out.println(ret); 
    }
    if( ret.equals("ok") ) {
       mWaiting = false;
       // if we have scheduled commands do them first
       if( mNewCommands.size() != 0 ) {
         if ( send(mNewCommands.get(0)) ) {
           mNewCommands.remove(0);
         }
       }
    }
  }  
  
  public void pre() {
    poll();
  }

  
} // end of class;