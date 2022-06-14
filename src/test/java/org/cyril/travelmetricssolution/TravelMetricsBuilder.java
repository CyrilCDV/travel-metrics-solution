package org.cyril.travelmetricssolution;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public class TravelMetricsBuilder {
    double distance;
    double duration;
    double meanSpeed;
    double accelerationMax;
    double startToStopDistance;
    double[] accelerations;

    public TravelMetricsBuilder() {
    }

    public TravelMetricsBuilder distance(double distance) {
        this.distance = distance;
        return this;
    }
    public TravelMetricsBuilder duration(double duration) {
        this.duration = duration;
        return this;
    }
    public TravelMetricsBuilder meanSpeed(double meanSpeed) {
        this.meanSpeed = meanSpeed;
        return this;
    }
    public TravelMetricsBuilder accelerationMax(double accelerationMax) {
        this.accelerationMax = accelerationMax;
        return this;
    }
    public TravelMetricsBuilder startToStopDistance(double startToStopDistance) {
        this.startToStopDistance = startToStopDistance;
        return this;
    }
    public TravelMetricsBuilder accelerations(@NonNull double[] accelerations) {
        this.accelerations = accelerations;
        return this;
    }

    public JSONObject build() throws JSONException {
        final JSONObject travelMetrics = new JSONObject();
        travelMetrics.put("distance", distance);
        travelMetrics.put("duration", duration);
        travelMetrics.put("meanSpeed", meanSpeed);
        travelMetrics.put("accelerationMax", accelerationMax);
        travelMetrics.put("startToStopDistance", startToStopDistance);
        travelMetrics.put("accelerations", new JSONArray(accelerations));
        return travelMetrics;
    }

}
