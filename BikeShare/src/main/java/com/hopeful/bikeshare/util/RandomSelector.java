package com.hopeful.bikeshare.util;

import com.hopeful.bikeshare.model.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSelector {
    private List<Station> stationList = new ArrayList<>();
    private int totalSum = 0;

    public RandomSelector(List<Station> stationList) {
        this.stationList = stationList;
        for (Station station : stationList) {
            totalSum = totalSum + station.getWeight();
        }
    }

    public Station getRandomStation() {
        int index = new Random().nextInt(totalSum);
        int sum = 0;
        int i = 0;
        while (sum < index) {
            sum = sum + stationList.get(i++).getWeight();
        }
        return stationList.get(Math.max(0, i - 1));
    }
}
