package com.adventorium.lab6;

/**
 * Created by Андрей on 23.05.2016.
 */
public class Main {
    public static void main(String[] args) {
        OneHundredThreads oneHundredThreads = new OneHundredThreads();
        HungryPhilosophers hungryPhilosophers = new HungryPhilosophers();
        //HungryPhilosophersWithoutConcurrent hungryPhilosophersWithoutConcurrent = new HungryPhilosophersWithoutConcurrent();
        HungryPhilosophersWithoutConcurrentVer2 hungryPhilosophersWithoutConcurrentVer2 = new HungryPhilosophersWithoutConcurrentVer2();
    }
}
