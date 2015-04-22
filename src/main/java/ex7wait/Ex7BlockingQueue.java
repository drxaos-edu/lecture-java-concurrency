package ex7wait;

import java.util.LinkedList;


class BlockingQueue<T> {

    private LinkedList<T> queue = new LinkedList<T>();
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T element) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }

        queue.add(element);
        notify(); // notifyAll() for multiple producer/consumer threads
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        T item = queue.remove();
        notify(); // notifyAll() for multiple producer/consumer threads
        return item;
    }

    public synchronized void printQueue() {
        for (T t : queue) {
            System.out.print(" " + t);
        }
    }
}

class Producer implements Runnable {

    BlockingQueue<Byte> q;

    public Producer(BlockingQueue<Byte> q) {
        this.q = q;
    }

    public volatile byte next = 0;

    @Override
    public void run() {
        try {

            while (true) {
                next = (byte) (Math.random() * 10 + 2);
                q.put(next);
                next = 0;
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {

    BlockingQueue<Byte> q;

    public Consumer(BlockingQueue<Byte> q) {
        this.q = q;
    }

    public volatile byte processing = 0;

    @Override
    public void run() {
        try {

            while (true) {
                processing = q.take();
                Thread.sleep(processing * 1000);
                processing = 0;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


public class Ex7BlockingQueue {
    public static void main(String[] args) {

        final BlockingQueue<Byte> q = new BlockingQueue<Byte>(10);

        Producer[] producers = new Producer[3];
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(q);
            new Thread(producers[i]).start();
        }

        Consumer[] consumers = new Consumer[3];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(q);
            new Thread(consumers[i]).start();
        }

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }

            System.out.print("prod: ");
            for (int i = 0; i < producers.length; i++) {
                System.out.print("  " + producers[i].next);
            }
            System.out.println();

            System.out.print("queue:  ");
            q.printQueue();
            System.out.println();

            System.out.print("cons: ");
            for (int i = 0; i < consumers.length; i++) {
                System.out.print("  " + consumers[i].processing);
            }
            System.out.println();
            System.out.println();
        }

    }
}
