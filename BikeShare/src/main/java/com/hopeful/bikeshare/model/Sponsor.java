package com.hopeful.bikeshare.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Sponsor {
    private static final AtomicLong idCounter = new AtomicLong();
    private long id;
    private String name;
    private Long interactionCounter = 0L;

    public long getId() {
        return id;
    }

    public Sponsor() {
        this.id = idCounter.incrementAndGet();
    }

    public Sponsor(String name) {
        this.id = idCounter.incrementAndGet();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getInteractionCounter() {
        return interactionCounter;
    }

    public void addInteraction() {
        interactionCounter = interactionCounter + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sponsor sponsor = (Sponsor) o;
        return Objects.equals(name, sponsor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Sponsor{id=" + id + ", name='" + name + ", interactionCounter=" + interactionCounter + '}';
    }
}
