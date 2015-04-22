package ex3atomic;

import java.util.Arrays;

public class Ex3Atomic {

    static volatile int[] array = new int[]{0, 0};

    public static void main(String[] args) {

        Runnable worker = new Runnable() {
            @Override
            public void run() {
                while (array[0] < 1000000) {
                    for (int i = 0; i < array.length; i++) {
                        array[i] += 1;
                    }
                }
            }
        };

        Runnable printer = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Arrays.toString(array));
                    if (array[0] >= 1000000) {
                        return;
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        new Thread(printer).start();

        for (int i = 0; i < 10; i++) {
            new Thread(worker).start();
        }

    }


}
