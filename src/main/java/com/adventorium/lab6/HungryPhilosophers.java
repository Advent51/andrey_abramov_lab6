package com.adventorium.lab6;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Андрей on 23.05.2016.
 */
public class HungryPhilosophers {

    private final int NO_OF_PHILOSOPHER;
    private final int SIMULATION_MILLIS;

    HungryPhilosophers() {
        NO_OF_PHILOSOPHER = 5;
        SIMULATION_MILLIS = 1000 * 5;
        System.out.println("Five philosophers with util.concurrent:");
        startTroublesWithConcurrent();
    }

    private class ChopStick {

        Lock up = new ReentrantLock();
        private final int id;

        ChopStick(int id) {
            this.id = id;
        }

        boolean pickUp() throws InterruptedException {
            return up.tryLock(10, TimeUnit.MILLISECONDS);
        }

        void putDown() {
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

    private void startTroublesWithConcurrent() {

        ExecutorService executorService = Executors.newFixedThreadPool(NO_OF_PHILOSOPHER);

        Philosopher[] philosophers = new Philosopher[NO_OF_PHILOSOPHER];

        ChopStick[] chopSticks = new ChopStick[NO_OF_PHILOSOPHER];
        for (int i = 0; i < NO_OF_PHILOSOPHER; i++) {
            chopSticks[i] = new ChopStick(i);
        }

        for (int i = 0; i < NO_OF_PHILOSOPHER; i++) {
            philosophers[i] = new Philosopher(i, chopSticks[i], chopSticks[(i + 1) % NO_OF_PHILOSOPHER]);
            executorService.execute(philosophers[i]);
        }

        try {
            Thread.sleep(SIMULATION_MILLIS);
            for (Philosopher philosopher : philosophers) {
                philosopher.isTummyFull = true;
            }

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Philosopher philosopher : philosophers) {
                System.out.println(philosopher + " => No of Turns to Eat ="
                        + philosopher.getNoOfTurnsToEat());
            }
        }
    }
}
