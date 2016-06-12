package com.javarush.test.level16.lesson13.bonus02.TMP;
import java.util.Random;

/**
 * Created by yur on 08.06.2016.
 */
public class Simultaneous implements Runnable {
    protected static final int THREADS = 10;

    private static class Semaphore {
        public boolean set = false;
    }

    protected static final Semaphore[] semaphores =
            new Semaphore[THREADS];
    protected static final Thread[] threads = new Thread[THREADS];

    protected static Random rnd = new Random();

    protected final int threadNum;

    protected  Simultaneous(int num) {
        this.threadNum = num;
    }

    protected static int getRndNumber() {
        return rnd.nextInt(1000)*10;
    }

    public void run() {
        synchronized (semaphores) {
            semaphores[this.threadNum].set = true;
            semaphores.notify();
        }

        final long startExec = System.currentTimeMillis();
        executeTask();
        final long finishExec = System.currentTimeMillis();
        System.out.println("Thread N" + this.threadNum + ". Start: " +
                startExec + ". End: " + finishExec +
        ". Total: " + (finishExec - startExec) + ".");
    }

    public void executeTask() {
        int temp = 1000;
        for (int i=0; i<getRndNumber(); i++)
            for (int j=0; j<getRndNumber(); j++)
                for (int k=0; k<getRndNumber(); k++)
                    temp = temp / temp * temp;
    }

    public static void main(String[] args) {
        for (int i=0; i<THREADS; i++) {
            semaphores[i] = new Semaphore();
        }

        System.out.println("Starting " + THREADS + " threads...");

        synchronized (Simultaneous.class) {
            synchronized (semaphores) {
                for (int i=0; i<THREADS; i++) {
                    final Simultaneous mainThread = new Simultaneous(i);
                    threads[i] = new Thread(mainThread, "Thread N" + i);
                    threads[i].setDaemon(true);
                    threads[i].start();
                }

                System.out.println("Waiting for simultaneous start...");

                boolean isAllStarted = false;
                while (!isAllStarted) {
                    try {
                        semaphores.wait();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    isAllStarted = true;
                    for (int i=0; i<THREADS; i++) {
                        if (!semaphores[i].set) {
                            isAllStarted = false;
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("Waiting for threads to finish execution...");

        for (int i=0; i<THREADS; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        System.out.println("Program terminated successfully.");
    }
}
