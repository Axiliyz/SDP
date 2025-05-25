package com.example.quiz;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomReadWriteLock {
    private final Lock writeLock = new ReentrantLock();
    private final Lock readLock = new ReentrantLock();
    private final AtomicInteger readers = new AtomicInteger(0);
    private final AtomicInteger writers = new AtomicInteger(0);
    private final AtomicInteger waitingWriters = new AtomicInteger(0);

    public void readLock() {
        readLock.lock();
        try {
            while (writers.get() > 0 || waitingWriters.get() > 0) {
                readLock.unlock();
                Thread.yield();
                readLock.lock();
            }
            readers.incrementAndGet();
        } finally {
            readLock.unlock();
        }
    }

    public void readUnlock() {
        readLock.lock();
        try {
            readers.decrementAndGet();
        } finally {
            readLock.unlock();
        }
    }

    public void writeLock() {
        waitingWriters.incrementAndGet();
        writeLock.lock();
        try {
            while (readers.get() > 0 || writers.get() > 0) {
                writeLock.unlock();
                Thread.yield();
                writeLock.lock();
            }
            writers.incrementAndGet();
        } finally {
            waitingWriters.decrementAndGet();
        }
    }

    public void writeUnlock() {
        writeLock.lock();
        try {
            writers.decrementAndGet();
        } finally {
            writeLock.unlock();
        }
    }
} 