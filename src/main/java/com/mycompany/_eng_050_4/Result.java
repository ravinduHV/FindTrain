package com.mycompany._eng_050_4;

import java.util.List;

public class Result {
    int distance;
    List<Character> path;

    public Result(int distance, List<Character> path) {
        this.distance = distance;
        this.path = path;
    }

    public int getDistance() {
        return distance;
    }

    public List<Character> getPath() {
        return path;
    }
}
