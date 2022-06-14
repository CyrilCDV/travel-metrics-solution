package org.cyril.travelmetricssolution;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public final class RawTravelDataJsonBuilder {

    LocalDate startTripDate;
    String departureCity;
    String arrivalCity;
    TripData tripData;

    public RawTravelDataJsonBuilder() {
    }

    public RawTravelDataJsonBuilder startTripDate(@NonNull LocalDate startTripDate) {
        this.startTripDate = startTripDate;
        return this;
    }

    public RawTravelDataJsonBuilder departureCity(@NonNull String departureCity) {
        this.departureCity = departureCity;
        return this;
    }

    public RawTravelDataJsonBuilder arrivalCity(@NonNull String arrivalCity) {
        this.arrivalCity = arrivalCity;
        return this;
    }

    public RawTravelDataJsonBuilder tripData(int[] time, double[] speed, double[] latitude, double[] longitude) {
        this.tripData = new TripData(time, speed, latitude, longitude);
        return this;
    }

    public JSONObject build() throws JSONException {
        final JSONObject rawTravelData = new JSONObject();
        rawTravelData.put("startTripDate", startTripDate.toString());
        rawTravelData.put("departureCity", departureCity);
        rawTravelData.put("arrivalCity", arrivalCity);
        rawTravelData.put("tripData", tripData.toJsonObject());
        return rawTravelData;
    }

    private static class TripData {
        private final int[] time;
        private final double[] speed;
        private final double[] latitude;
        private final double[] longitude;

        public TripData(int[] time, double[] speed, double[] latitude, double[] longitude) {
            this.time = time;
            this.speed = speed;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        private JSONObject toJsonObject() throws JSONException {
            final JSONObject tripData = new JSONObject();
            tripData.put("time", time != null ? new JSONArray(time) : null);
            tripData.put("speed", speed != null ? new JSONArray(speed) : null);
            tripData.put("latitude", latitude != null ? new JSONArray(latitude) : null);
            tripData.put("longitude", longitude != null ? new JSONArray(longitude) : null);
            return tripData;
        }
    }
}
