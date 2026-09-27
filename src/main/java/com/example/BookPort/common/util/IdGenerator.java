package com.example.BookPort.common.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.BookPort.common.logging.Debugger;
/**
* ID Structure:
* ------------------------------------------------------------
* YY DDD MM SSSSSSSSS CCC
*
* Where:
* YY  -> Last 2 digits of current year
* DDD -> Day of year (001–365/366)
* MM  -> Module ID (identifies the service/module, e.g., Order, Payment)
* SSSSSSSSS -> Milliseconds elapsed since start of the current day
* CCC -> Counter for handling multiple requests within the same millisecond
* ------------------------------------------------------------
**/
public class IdGenerator {
	
	private Debugger d = new Debugger(this.getClass());
	
	private final int moduleId;
	
	private long lastTimestamp = -1L;
	/*
	 * AtomicInteger: In Java, an AtomicInteger is a class that provides an int
	 * value that can be updated atomically. It is part of the
	 * java.util.concurrent.atomic package and is primarily used in multi-threaded
	 * applications to perform thread-safe operations on a single variable without
	 * the overhead of explicit synchronization or locks.
	 */
    private final AtomicInteger counter = new AtomicInteger(0);

    private static final int MAX_COUNTER = 999;

    public IdGenerator(int moduleId) {
        this.moduleId = moduleId;
        d.dbg("IdGenerator bean created for module "+moduleId);
    }

    public synchronized String generateId() {
    	d.dbg("Inside generateId for module "+ this.moduleId);
        long currentMillis = System.currentTimeMillis();
        if (currentMillis < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards");
        }
        d.dbg("current MilliSecond "+ currentMillis);
        d.dbg("lastTimestamp "+lastTimestamp);
        d.dbg("counter: "+counter.get());
        if (currentMillis == lastTimestamp) {
            int currentCount = counter.incrementAndGet();
            if (currentCount > MAX_COUNTER) {
                currentMillis = waitNextMillis(currentMillis);
                counter.set(0);
            }
        } else {
            counter.set(0);
        }
        d.dbg("counter post millisecond check: " +counter.get());
        lastTimestamp = currentMillis;
        String id = buildId(currentMillis, counter.get());
        d.dbg("RETURNING ID : "+id);
        return id;
    }

    private long waitNextMillis(long currentMillis) {
        while (currentMillis == lastTimestamp) {
            currentMillis = System.currentTimeMillis();
        }
        return currentMillis;
    }

    private String buildId(long timestamp, int counter) {
        LocalDate date = LocalDate.now(ZoneId.systemDefault());

        int year = date.getYear() % 100;
        int dayOfYear = date.getDayOfYear();

        long millisSinceDayStart = getMillisSinceStartOfDay(timestamp);

        return String.format(
                "%02d%03d%02d%09d%03d",
                year,
                dayOfYear,
                moduleId,
                millisSinceDayStart,
                counter
        );
    }

    private long getMillisSinceStartOfDay(long timestamp) {
        LocalDate today = LocalDate.now();
        long startOfDay = today
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        return timestamp - startOfDay;
    }
	

}
