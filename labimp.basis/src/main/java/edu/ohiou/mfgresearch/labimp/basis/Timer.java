package edu.ohiou.mfgresearch.labimp.basis;

/**
 * Title:        Timer class
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Sridharan Thiruppalli and Dusan N. Sormaz
 * @version 1.1
 */
//import java.util.Date;

 public class Timer {
  long startTime;
  final public static int MILLISECOND = 0;
  final public static int SECOND = 1;
  final public static int MINUTE = 2;
  final public static int HOUR = 3;
  final public static int HHMMSS = 4;
  final public static int RELATIVE = 0;
  final public static int ABSOLUTE = 1;

  public Timer() {
    this(RELATIVE);
  }

  public Timer ( int mode) {
    if (mode == RELATIVE)
      //      this();
      startTime = System.currentTimeMillis();
    else
      startTime =0;
      }

  public void resetTimer () {
    startTime = System.currentTimeMillis();
    }

  public float getTimer () {
    return getTimer (Timer.MINUTE);
  }

  public  float  getTimer ( int mode) {
    //Date newTime = new Date();
    long deltaTime = System.currentTimeMillis() - startTime;
    switch (mode) {
      case MILLISECOND:
	return (float) deltaTime;
      case SECOND:
	return (float) deltaTime / 1000;
      case MINUTE:
	return (float) deltaTime / (1000 * 60);
      case HOUR:
	return (float) deltaTime / (1000 * 60 *60);
      default:
	return (float) deltaTime;
      }
    }

  public String getTimerString () {
    return getTimerString (Timer.HHMMSS);
    }

  public String  getTimerString ( int mode) {
    long deltaTime = System.currentTimeMillis() - startTime;
    switch (mode) {
      case MILLISECOND:
	return "" + deltaTime;
      case SECOND:
	return "" + (float) deltaTime / 1000.;
      case MINUTE:
	return "" +(float) deltaTime / (1000. * 60.);
      case HOUR:
	return "" + (float) deltaTime / (1000. * 60. *60.);
      case HHMMSS: {
	long hour, minutes;
	long seconds;

	deltaTime = deltaTime/1000;  // in seconds
	seconds = (deltaTime % 60);  // take seconds only

	//subtract seconds, remove hours and convert to minutes
	minutes = ((deltaTime - seconds) % 3600) / 60;

	// subtract partial hour and divide to convert to hours
	hour = (deltaTime - (deltaTime % 3600))/ 3600 ;

	return "" + hour + ":" + minutes + ":" + seconds;
	}
      default:
	return "" + deltaTime;
      }
    }

 public void  show(){

    long deltaTime = System.currentTimeMillis() - startTime;

    System.out.println("time elapsed :  " + deltaTime + " milli seconds");
    formatTime(deltaTime);
 }

 private void formatTime(long time){
  long hour, minutes;
  long seconds;

  time = time/1000;

  hour = (time - (time % 3600))* 3600 ;
  minutes = ((time - (time % 60)) % 3600) / 60;
  seconds = (time % 60);

  System.out.println("time elapsed H:M:S  :- " + hour + " : " + minutes + " : " + seconds);

 }

 public static void main(String args []) {
  Timer t1 = new Timer();
  Timer t2 = new Timer(ABSOLUTE);
  System.out.println ("relative" + t1.getTimerString() );
  System.out.println ("absolute" + t2.getTimerString() );

  }


}