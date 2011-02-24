package com.brianjolly.commute;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {

  Timer timer;

  public MyTimer() {
    timer = new Timer();
    timer.schedule(new RemindTask(), 0, //initial delay
        1 * 100); //subsequent rate
  }

  class RemindTask extends TimerTask {
    int numWarningBeeps = 15;

    public void run() {
      if (numWarningBeeps > 0) {
        System.out.println("Beep!");
        numWarningBeeps--;
      } else {
        //numWarningBeeps = 15;
        System.out.println("Time's up!");
        timer.cancel(); //Not necessary because we call System.exit
        System.exit(0); //Stops the AWT thread (and everything else)
      }
    }
  }

  public static void main(String args[]) {
    System.out.println("About to schedule task.");
    System.out.println("Task scheduled.");
  }
}

