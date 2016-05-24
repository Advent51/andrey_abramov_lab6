package com.adventorium.lab6;

import java.util.Random;

/**
 * Created by Андрей on 23.05.2016.
 */
public class HungryPhilosophersWithoutConcurrent {

    private final int NO_OF_PHILOSOPHER;
    private final int SIMULATION_MILLIS;

    HungryPhilosophersWithoutConcurrent() {
        NO_OF_PHILOSOPHER = 5;
        SIMULATION_MILLIS = 1000 * 5;
        System.out.println("Five philosophers without util.concurrent:");
        startTroublesWithoutConcurrent();
    }

    private class ChopStick {

        private final int id;
        volatile private int owner = -1;

        ChopStick(int id) {
            this.id = id;
        }

        synchronized void pickUp(int owner_id) {
            this.owner = owner_id;
        }

        synchronized void putDown() {
            this.owner = -1;
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

        private Thread t;
        private String threadName;

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
                    if (leftChopStick.owner == -1) {
                        leftChopStick.pickUp(this.id);
                        if (rightChopStick.owner == -1) {
                            rightChopStick.pickUp(this.id);
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

        public void start(String name) {
            this.threadName = name;
            System.out.println("Starting " + threadName);
            if (t == null) {
                t = new Thread(this, threadName);
                t.start();
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

    private void startTroublesWithoutConcurrent() {

        Philosopher[] philosophers = new Philosopher[NO_OF_PHILOSOPHER];

        ChopStick[] chopSticks = new ChopStick[NO_OF_PHILOSOPHER];
        for (int i = 0; i < NO_OF_PHILOSOPHER; i++) {
            chopSticks[i] = new ChopStick(i);
        }

        for (int i = 0; i < NO_OF_PHILOSOPHER; i++) {
            philosophers[i] = new Philosopher(i, chopSticks[i], chopSticks[(i + 1) % NO_OF_PHILOSOPHER]);
            philosophers[i].start(String.valueOf(i));
        }

        try {
            Thread.sleep(SIMULATION_MILLIS);
            for (Philosopher philosopher : philosophers) {
                philosopher.isTummyFull = true;
            }

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            for (Philosopher philosopher : philosophers) {
                System.out.println(philosopher + " => No of Turns to Eat ="
                        + philosopher.getNoOfTurnsToEat());
            }
        }
    }
}
