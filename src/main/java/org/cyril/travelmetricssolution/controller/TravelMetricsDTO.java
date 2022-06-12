package org.cyril.travelmetricssolution.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TravelMetricsDTO {
    @JsonProperty("distance")
    private final double distance;
    @JsonProperty("duration")
    private final int duration;
    @JsonProperty("meanSpeed")
    private final double meanSpeed;
    @JsonProperty("accelerationMax")
    private final double accelerationMax;
    @JsonProperty("startToStopDistance")
    private final double startToStopDistance;
    @JsonProperty("accelerations")
    private final double[] accelerations;

    public TravelMetricsDTO(double distance, int duration, double meanSpeed, double accelerationMax, double startToStopDistance, double[] accelerations) {
        this.distance = distance;
        this.duration = duration;
        this.meanSpeed = meanSpeed;
        this.accelerationMax = accelerationMax;
        this.startToStopDistance = startToStopDistance;
        this.accelerations = accelerations;
    }
}
