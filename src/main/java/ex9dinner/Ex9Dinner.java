package ex9dinner;

import java.util.concurrent.locks.ReentrantLock;

class Fork {

    int number;

    public Fork(int number) {
        this.number = number;
    }

    volatile int philosopher = -1;

    public void take(int philosopher) {
        if (this.philosopher != -1) {
            System.out.println("Error: philosopher " + philosopher + " cannot take fork " + number + " because it's taken by philosopher " + this.philosopher);
            System.exit(1);
        }
        this.philosopher = philosopher;
        System.out.println("Philosopher " + philosopher + " take fork " + number);
    }

    public void put(int philosopher) {
        if (this.philosopher == -1) {
            System.out.println("Error: philosopher " + philosopher + " cannot put fork " + number + " because it's not taken");
            System.exit(1);
        }
        if (this.philosopher != philosopher) {
            System.out.println("Error: philosopher " + philosopher + " cannot put fork " + number + " because it's taken by philosopher " + this.philosopher);
            System.exit(1);
        }
        this.philosopher = -1;
        System.out.println("Philosopher " + philosopher + " put fork " + number);
    }

}

class Philosopher implements Runnable {

    int number;
    final Fork fork1;
    final Fork fork2;

    public Philosopher(int number, Fork fork1, Fork fork2) {
        this.number = number;
        this.fork1 = fork1;
        this.fork2 = fork2;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Philosopher " + number + " is thinking");
            try {
                Thread.sleep((int) (Math.random() * 10));
            } catch (InterruptedException e) {
            }
            System.out.println("Philosopher " + number + " starts eating");

            takeForks();

            try {
                Thread.sleep((int) (Math.random() * 10));
            } catch (InterruptedException e) {
            }
            System.out.println("Philosopher " + number + " ends eating");

            putForks();
        }
    }

    static volatile ReentrantLock[] locks = new ReentrantLock[5];

    static {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    void takeForks() {
        // TODO synchronize
        locks[fork1.number].lock();
        locks[fork2.number].lock();

        fork1.take(number);
        fork2.take(number);
    }

    void putForks() {
        // TODO synchronize
        fork1.put(number);
        fork2.put(number);

        locks[fork1.number].unlock();
        locks[fork2.number].unlock();
    }
}

public class Ex9Dinner {
    public static void main(String[] args) throws InterruptedException {

        Fork[] forks = new Fork[5];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Fork(i);
        }

        Philosopher[] philosophers = new Philosopher[5];
        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % forks.length]);
        }

        for (int i = 0; i < philosophers.length; i++) {
            new Thread(philosophers[i]).start();
        }
    }
}
