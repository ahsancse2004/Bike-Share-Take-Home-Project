package com.hopeful.bikeshare.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Bike {
    private static final AtomicLong idCounter = new AtomicLong();
    private long id;
    private String name;
    private long tripCounter = 0L;
    private long stationId;

    public Bike() {
        this.id = idCounter.incrementAndGet();
        this.name = "Bike " + id;
    }

    public Bike(String name) {
        this.id = idCounter.incrementAndGet();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTripCounter() {
        return tripCounter;
    }


    public void incrementTripCounter() {
        tripCounter++;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bike bike = (Bike) o;
        return id == bike.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Bike{id=" + id + ", name=" + name + ", tripCounter=" + tripCounter + '}';
    }
}
