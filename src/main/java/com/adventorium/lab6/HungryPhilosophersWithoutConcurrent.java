package com.adventorium.lab6;

import java.util.Random;

/**
 * Created by Андрей on 23.05.2016.
 */
public class HungryPhilosophersWithoutConcurrent {

    private final int NO_OF_PHILOSOPHER;
    private final int SIMULATION_MILLIS;

    HungryPhilosophersWithoutConcurrent(){
        NO_OF_PHILOSOPHER = 5;
        SIMULATION_MILLIS = 1000 * 5;
        System.out.println("Five philosophers without util.concurrent:");
        startTroublesWithoutConcurrent();
    }

    private class ChopStick{

        private final int id;

        ChopStick(int id) {
            this.id = id;
        }

        synchronized boolean pickUp() {
            return up.tryLock(10, TimeUnit.MILLISECONDS);
        }

        synchronized void putDown() {
            up.unlock();
        }

        @Override
        public String toString() {
            return "Chopstick-" + id;
        }
    }

    private class Philosopher implements Runnable {

        private final int id;

        private final ChopStick leftChopStick;
        private final ChopStick rightChopStick;

        volatile boolean isTummyFull = false;
        private Random randomGenerator = new Random();
        private int noOfTurnsToEat = 0;

        Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick) {
            this.id = id;
            this.leftChopStick = leftChopStick;
            this.rightChopStick = rightChopStick;
        }

        @Override
        public void run() {

            try {
                while (!isTummyFull) {
                    think();
                    if (leftChopStick.pickUp()) {
                        if (rightChopStick.pickUp()) {
                            eat();
                            rightChopStick.putDown();
                        }
                        leftChopStick.putDown();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void think() throws InterruptedException {
            System.out.println(this + " is thinking");
            Thread.sleep(randomGenerator.nextInt(1000));
        }

        private void eat() throws InterruptedException {
            System.out.println(this + " is eating");
            noOfTurnsToEat++;
            Thread.sleep(randomGenerator.nextInt(1000));
        }

        public int getNoOfTurnsToEat() {
            return noOfTurnsToEat;
        }

        @Override
        public String toString() {
            return "Philosopher-" + id;
        }

    }

    private void startTroublesWithoutConcurrent(){

    }
}
