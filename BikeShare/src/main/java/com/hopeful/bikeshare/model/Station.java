package com.hopeful.bikeshare.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Station {
    private static final AtomicLong idCounter = new AtomicLong();
    private long id;
    private int capacity;
    private int weight;
    private String name;
    private Set<Bike> bikeSet;
    private Set<Sponsor> sponsorSet;

    public Station() {  
        this.id = idCounter.incrementAndGet();
    }

    public Station(int capacity, int weight, String name) {
        this.id = idCounter.incrementAndGet();
        this.capacity = capacity;
        this.weight = weight;
        this.name = name;
        this.bikeSet = new HashSet<>();
        this.sponsorSet = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public Set<Sponsor> getSponsorSet() {
        return sponsorSet;
    }

    public Set<Bike> getBikeSet() {
        return bikeSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Station{id=" + id + ", capacity=" + capacity + ", weight=" + weight + ", name=" + name + "}";
    }
}
