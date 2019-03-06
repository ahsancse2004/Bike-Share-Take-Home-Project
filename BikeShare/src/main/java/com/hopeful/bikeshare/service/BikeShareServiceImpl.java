package com.hopeful.bikeshare.service;

import com.hopeful.bikeshare.model.Bike;
import com.hopeful.bikeshare.model.Sponsor;
import com.hopeful.bikeshare.model.Station;
import com.hopeful.bikeshare.util.RandomSelector;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("metroBikeSharingService")
public class BikeShareServiceImpl {
    public static final long CHECKED_OUT_STATION_ID = -1L;
    private static Map<Long, Sponsor> sponsors = new HashMap<>();
    private static Map<Long, Station> stations = new HashMap<>();
    private static Map<Long, Bike> bikes = new HashMap<>();

    static {
        Sponsor sponsorA = new Sponsor("Sponsor A");
        Sponsor sponsorB = new Sponsor("Sponsor B");
        Sponsor sponsorC = new Sponsor("Sponsor C");
        Sponsor sponsorD = new Sponsor("Sponsor D");

        sponsors.put(sponsorA.getId(), sponsorA);
        sponsors.put(sponsorB.getId(), sponsorB);
        sponsors.put(sponsorC.getId(), sponsorC);
        sponsors.put(sponsorD.getId(), sponsorD);

        Station station1 = new Station(10, 50, "Station 1");
        station1.getSponsorSet().add(sponsorA);
        station1.getSponsorSet().add(sponsorB);
        stations.put(station1.getId(), station1);

        Station station2 = new Station(5, 20, "Station 2");
        station2.getSponsorSet().add(sponsorB);
        station2.getSponsorSet().add(sponsorC);
        stations.put(station2.getId(), station2);

        Station station3 = new Station(3, 20, "Station 3");
        station3.getSponsorSet().add(sponsorC);
        stations.put(station3.getId(), station3);

        Station station4 = new Station(10, 10, "Station 4");
        station4.getSponsorSet().add(sponsorA);
        station4.getSponsorSet().add(sponsorC);
        station4.getSponsorSet().add(sponsorD);
        stations.put(station4.getId(), station4);
    }

    public boolean addBike(Bike bike) {
        Station station = getNextAvailableStation();
        if (station == null) {
            return false;
        } else {
            bikes.put(bike.getId(), bike);
            station.getBikeSet().add(bike);
            bike.setStationId(station.getId());
            return true;
        }
    }

    public boolean addSponsor(Sponsor sponsor) {
        Sponsor result = sponsors.put(sponsor.getId(), sponsor);
        return result == null;
    }

    public boolean addSponsor(Station station, Sponsor sponsor) {
        return station.getSponsorSet().add(sponsor);
    }

    public boolean removeSponsor(Station station, Sponsor sponsor) {
        return station.getSponsorSet().remove(sponsor);
    }

    public Bike checkoutBike(Station station) {
        //For every visit to the station increase the sponsor interaction regardless of checkout result.
        for (Sponsor sponsor : station.getSponsorSet()) {
            sponsor.addInteraction();
        }
        Set<Bike> availableBikes = station.getBikeSet();
        if (availableBikes.size() > 0) {
            Bike firstBike = availableBikes.toArray(new Bike[]{})[0];
            firstBike.setStationId(CHECKED_OUT_STATION_ID);//Set current station to -1 to indicate the bike currently does not belong to a station.
            availableBikes.remove(firstBike);
            return firstBike;
        } else {
            return null;
        }
    }

    public Station checkinBike(Bike bike) {
        for (Station station : stations.values()) {
            //For every visit to the station increase the sponsor interaction regardless of checkin result.
            for (Sponsor sponsor : station.getSponsorSet()) {
                sponsor.addInteraction();
            }
            Set<Bike> availableBikes = station.getBikeSet();
            if (availableBikes.size() < station.getCapacity()) {
                availableBikes.add(bike);
                bike.setStationId(station.getId());//Set stationId to current station.
                bike.incrementTripCounter();
                return station;
            }
        }
        return null;
    }

    private Station getNextAvailableStation() {
        List<Station> stationList = new ArrayList<>();
        for (Station station : stations.values()) {
            if (station.getCapacity() > station.getBikeSet().size()) {
                stationList.add(station);
            }
        }
        if (stationList.size() != 0) {
            return new RandomSelector(stationList).getRandomStation();
        } else {
            return null;
        }
    }

    public List<Sponsor> findAllSponsors() {
        return Arrays.asList(sponsors.values().toArray(new Sponsor[]{}));
    }

    public List<Station> findAllStations() {
        return Arrays.asList(stations.values().toArray(new Station[]{}));
    }

    public List<Bike> findAllBikes() {
        return Arrays.asList(bikes.values().toArray(new Bike[]{}));
    }

    public Sponsor findSponsorById(long id) {
        return sponsors.get(id);
    }

    public boolean isSponsorExist(Sponsor sponsor) {
        return sponsors.containsValue(sponsor);
    }

    public void saveSponsor(Sponsor sponsor) {
        sponsors.put(sponsor.getId(), sponsor);
    }

    public Bike findBikeById(long id) {
        return bikes.get(id);
    }

    public boolean isBikeExist(Bike bike) {
        return bikes.containsValue(bike);
    }

    public Station findStationById(long id) {
        return stations.get(id);
    }

    public boolean isStationExist(Station station) {
        return stations.containsValue(station);
    }

    public boolean saveStation(Station station) {
        if (!(station.getCapacity() == 3 || station.getCapacity() == 5 || station.getCapacity() == 10)) {
            return false;
        }
        stations.put(station.getId(), station);
        return true;
    }
}
