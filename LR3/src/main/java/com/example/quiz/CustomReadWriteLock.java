package com.example.quiz;

import java.util.concurrent.Semaphore;

public class CustomReadWriteLock {
    private final Semaphore readSemaphore = new Semaphore(1);
    private final Semaphore writeSemaphore = new Semaphore(1);
    private int readers = 0;

    public void readLock() throws InterruptedException {
        readSemaphore.acquire();
        try {
            if (readers == 0) {
                writeSemaphore.acquire();
            }
            readers++;
        } finally {
            readSemaphore.release();
        }
    }

    public void readUnlock() throws InterruptedException {
        readSemaphore.acquire();
        try {
            readers--;
            if (readers == 0) {
                writeSemaphore.release();
            }
        } finally {
            readSemaphore.release();
        }
    }

    public void writeLock() throws InterruptedException {
        writeSemaphore.acquire();
    }

    public void writeUnlock() {
        writeSemaphore.release();
    }
}