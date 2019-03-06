package com.hopeful.bikeshare.controller;

import com.hopeful.bikeshare.model.Bike;
import com.hopeful.bikeshare.model.Sponsor;
import com.hopeful.bikeshare.model.Station;
import com.hopeful.bikeshare.service.BikeShareServiceImpl;
import com.hopeful.bikeshare.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BikeShareApiController {
    private static final Logger logger = LoggerFactory.getLogger(BikeShareApiController.class);
    @Autowired
    private BikeShareServiceImpl bikeShareService;

    // -------------------Retrieve All Sponsors--------------------------------------------
    @RequestMapping(value = "/sponsor", method = RequestMethod.GET)
    public ResponseEntity<List<Sponsor>> listAllSponsors() {
        List<Sponsor> sponsors = bikeShareService.findAllSponsors();
        if (sponsors.isEmpty()) {
            return new ResponseEntity<>(sponsors, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sponsors, HttpStatus.OK);
    }

    // -------------------Retrieve Single Sponsor------------------------------------------
    @RequestMapping(value = "/sponsor/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSponsor(@PathVariable("id") long id) {
        logger.info("Fetching Sponsor with id {}", id);
        Sponsor sponsor = bikeShareService.findSponsorById(id);
        if (sponsor == null) {
            logger.error("Sponsor with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Sponsor with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sponsor, HttpStatus.OK);
    }

    // -------------------Create a Sponsor-------------------------------------------
    @RequestMapping(value = "/sponsor", method = RequestMethod.POST)
    public ResponseEntity<?> createSponsor(@RequestBody Sponsor sponsor) {
        logger.info("Creating Sponsor : {}", sponsor);
        if (bikeShareService.isSponsorExist(sponsor)) {
            logger.error("Unable to create. A Sponsor with name {} already exist", sponsor.getName());
            return new ResponseEntity<>(new CustomErrorType("Unable to create. A Sponsor with name " + sponsor.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        bikeShareService.saveSponsor(sponsor);
        return new ResponseEntity<>(sponsor, HttpStatus.CREATED);
    }

    // -------------------Retrieve All Stations--------------------------------------------
    @RequestMapping(value = "/station", method = RequestMethod.GET)
    public ResponseEntity<List<Station>> listAllStations() {
        List<Station> stations = bikeShareService.findAllStations();
        if (stations.isEmpty()) {
            return new ResponseEntity<>(stations, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stations, HttpStatus.OK);
    }

    // -------------------Retrieve Single Station------------------------------------------
    @RequestMapping(value = "/station/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getStation(@PathVariable("id") long id) {
        logger.info("Fetching Station with id {}", id);
        Station station = bikeShareService.findStationById(id);
        if (station == null) {
            logger.error("Station with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Station with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    // -------------------Create a Station-------------------------------------------
    @RequestMapping(value = "/station", method = RequestMethod.POST)
    public ResponseEntity<?> createStation(@RequestBody Station station) {
        logger.info("Creating Station : {}", station);
        if (bikeShareService.isStationExist(station)) {
            logger.error("Unable to create. A Station with name {} already exist", station.getName());
            return new ResponseEntity<>(new CustomErrorType("Unable to create. A Station with name " + station.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        if (bikeShareService.saveStation(station)) {
            return new ResponseEntity<>(station, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new CustomErrorType("Unable to create. Station capacity can only be either of 3 or 5 or 10"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    // -------------------Retrieve All Bikes--------------------------------------------
    @RequestMapping(value = "/bike", method = RequestMethod.GET)
    public ResponseEntity<List<Bike>> listAllBikes() {
        List<Bike> bikes = bikeShareService.findAllBikes();
        if (bikes.isEmpty()) {
            return new ResponseEntity<>(bikes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bikes, HttpStatus.OK);
    }

    // -------------------Retrieve Single Bike------------------------------------------
    @RequestMapping(value = "/bike/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getBike(@PathVariable("id") long id) {
        logger.info("Fetching Bike with id {}", id);
        Bike bike = bikeShareService.findBikeById(id);
        if (bike == null) {
            logger.error("Bike with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Bike with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bike, HttpStatus.OK);
    }

    // -------------------Create a Bike-------------------------------------------
    @RequestMapping(value = "/bike", method = RequestMethod.POST)
    public ResponseEntity<?> addBike(@RequestBody Bike bike) {
        logger.info("Creating Bike : {}", bike);
        if (bikeShareService.isBikeExist(bike)) {
            logger.error("Unable to create. A Bike with id {} already exist", bike.getId());
            return new ResponseEntity<>(new CustomErrorType("Unable to create. A Bike with id " + bike.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        if (bikeShareService.addBike(bike)) {
            return new ResponseEntity<>(bike, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new CustomErrorType("Unable to add Bike. All bike stations are either full or not yet created."), HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    // -------------------Add a Sponsor to a Station-------------------------------------------
    @RequestMapping(value = "/station/{station-id}/sponsor/{sponsor-id}", method = RequestMethod.POST)
    public ResponseEntity<?> addSponsorToStation(@PathVariable("station-id") long stationId, @PathVariable("sponsor-id") long sponsorId) {
        logger.info("Fetching Station with id : {}", stationId);
        Station station = bikeShareService.findStationById(stationId);
        if (station == null) {
            logger.error("Unable to find station with id : {}", stationId);
            return new ResponseEntity<>(new CustomErrorType("Unable to add Sponsor. A Station with id " + stationId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        logger.info("Fetching Sponsor with id : {}", sponsorId);
        Sponsor sponsor = bikeShareService.findSponsorById(sponsorId);
        if (sponsor == null) {
            logger.error("Unable to find sponsor with id : {}", sponsorId);
            return new ResponseEntity<>(new CustomErrorType("Unable to add Sponsor. A Sponsor with id " + sponsorId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        bikeShareService.addSponsor(station, sponsor);
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    // -------------------Remove a Sponsor from a Station-------------------------------------------
    @RequestMapping(value = "/station/{station-id}/sponsor/{sponsor-id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeSponsorFromStation(@PathVariable("station-id") long stationId, @PathVariable("sponsor-id") long sponsorId) {
        logger.info("Fetching Station with id : {}", stationId);
        Station station = bikeShareService.findStationById(stationId);
        if (station == null) {
            logger.error("Unable to find station with id : {}", stationId);
            return new ResponseEntity<>(new CustomErrorType("Unable to remove Sponsor. A Station with id " + stationId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        logger.info("Fetching Sponsor with id : {}", sponsorId);
        Sponsor sponsor = bikeShareService.findSponsorById(sponsorId);
        if (sponsor == null) {
            logger.error("Unable to find sponsor with id : {}", sponsorId);
            return new ResponseEntity<>(new CustomErrorType("Unable to remove Sponsor. A Sponsor with id " + sponsorId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        bikeShareService.removeSponsor(station, sponsor);
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    // -------------------Checkout a Bike from a Station-------------------------------------------
    @RequestMapping(value = "/checkout/{station-id}", method = RequestMethod.POST)
    public ResponseEntity<?> checkoutBikeFromStation(@PathVariable("station-id") long stationId) {
        logger.info("Fetching Station with id : {}", stationId);
        Station station = bikeShareService.findStationById(stationId);
        if (station == null) {
            logger.error("Unable to find station with id : {}", stationId);
            return new ResponseEntity<>(new CustomErrorType("Unable to checkout Bike. A Station with id " + stationId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        Bike checkoutBike = bikeShareService.checkoutBike(station);
        if (checkoutBike == null) {
            return new ResponseEntity<>(new CustomErrorType("Unable to checkout Bike. There are no bikes available to checkout"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(checkoutBike, HttpStatus.OK);
        }
    }

    // -------------------Checkin a Bike to a Station-------------------------------------------
    @RequestMapping(value = "/checkin/{bike-id}", method = RequestMethod.POST)
    public ResponseEntity<?> checkinBike(@PathVariable("bike-id") long bikeId) {
        logger.info("Fetching Bike with id : {}", bikeId);
        Bike bike = bikeShareService.findBikeById(bikeId);
        if (bike == null) {
            logger.error("Unable to find Bike with id : {}", bikeId);
            return new ResponseEntity<>(new CustomErrorType("Unable to checkin Bike. A Bike with id " + bikeId + " does not exist."), HttpStatus.NOT_FOUND);
        }
        if (bike.getStationId() != BikeShareServiceImpl.CHECKED_OUT_STATION_ID) {
            return new ResponseEntity<>(new CustomErrorType("Unable to checkin Bike. Bike with id " + bikeId + " is not yet checked out."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Station checkinStation = bikeShareService.checkinBike(bike);
        if (checkinStation == null) {
            return new ResponseEntity<>(new CustomErrorType("Unable to checkin Bike. All the stations are currently full."), HttpStatus.TOO_MANY_REQUESTS);
        } else {
            return new ResponseEntity<>(checkinStation, HttpStatus.OK);
        }
    }
}