package org.cyril.travelmetricssolution.controller;

public class MarseilleTravelData {
    private final int[] time;
    private final double[] speed;
    private final double[] latitude;
    private final double[] longitude;

    public static MarseilleTravelData getDugommierData() {
        final int[] time = new int[]{0, 2, 5, 7, 10};
        final double[] speed = new double[]{2.1, 3.5, 4.1, 3.2, 0.5};
        final double[] latitude = {43.3017349062319, 43.30106636470944, 43.30068102146434, 43.30003352054343,
                43.29943998906283};
        final double[] longitude = {5.379756460478574, 5.379801116306573, 5.380196639354559, 5.380712425815264,
                5.380826222306457};
        return new MarseilleTravelData(time, speed, latitude, longitude);
    }

    private MarseilleTravelData(int[] time, double[] speed, double[] latitude, double[] longitude) {
        this.time = time;
        this.speed = speed;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int[] getTime() {
        return time;
    }

    public double[] getSpeed() {
        return speed;
    }

    public double[] getLatitude() {
        return latitude;
    }

    public double[] getLongitude() {
        return longitude;
    }
}
