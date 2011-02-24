package com.brianjolly.commute.model;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {

    Timer timer;
    float ticks;

    public MyTimer(int initialDelay, int ticksPerSecond) {
        ticks = 0;
        timer = new Timer();
        timer.schedule(new RemindTask(), initialDelay, ticksPerSecond * 1000);
            System.out.println("set up timer");
    }

    class RemindTask extends TimerTask {
        public void run() {
            ticks++;
            notifyListener();
        }
    }

    public static void main(String args[]) {
        System.out.println("About to schedule task.");
        System.out.println("Task scheduled.");
    }

    private TimerChangeListener timerChangeListener;

    public interface TimerChangeListener {
        void onTimerChange(float ticks);
    }

    public void setTimerChangeListener(TimerChangeListener t) {
        timerChangeListener = t;
    }

    private void notifyListener() {
        if (null != timerChangeListener) {
            timerChangeListener.onTimerChange(ticks);
        }
    }

}
